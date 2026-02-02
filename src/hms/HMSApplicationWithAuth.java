package hms;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Scene;
// IN LINES KO BILKUL NA CHHERAIN - Ye conflict khatam karne ke liye hain
import javafx.scene.control.*; // Is se Label, Button, TableView sab mil jayenge
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Optional;
import java.util.stream.Collectors;

// Database connectivity
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.HashMap;
import java.util.Map;

public class HMSApplicationWithAuth extends Application {
    // User Authentication Storage
    private Map<String, User> registeredUsers = new HashMap<>();
    private User currentUser = null;
    // Data Storage
    private PatientList pList = new PatientList();
    private DoctorsList dList = new DoctorsList();
    private BorderPane mainLayout;
    private static ObservableList<CheckupRecord> globalQueueList = FXCollections.observableArrayList();
    private static int totalCheckups = 0;
    private static double totalRevenue = 0.0;

    @Override
    public void start(Stage primaryStage) {
        // 1. Database tables setup (Sirf tab banti hain agar na hon)
        DatabaseHandler.setupDatabase();

        // 2. 🔥 Database se Users load karke 'registeredUsers' Map mein bharna
        // Note: 'registeredUsers' wahi Map hai jo aap ne class ke start mein banaya hai
        DatabaseHandler.loadUsers(registeredUsers);

        // FIX: Populate dList and pList from registeredUsers so tables show data
        dList = new DoctorsList();
        pList = new PatientList();
        for (User u : registeredUsers.values()) {
            if ("Doctor".equals(u.getRole())) {
                dList.Insert(new Doctor(u.getId(), u.getName(), u.getContact(), u.getSpecialization(), u.getFee(), u.getShift()));
            } else if ("Patient".equals(u.getRole())) {
                pList.Insert(new Patient(u.getId(), u.getName(), u.getContact()));
            }
        }

        // 3. 🔥 Database se Appointments load karna
        loadAppointmentsFromDatabase();

        // 4. Dashboard ke stats update karna
        updateDashboardStats();


        primaryStage.setTitle("HIFZA & TAHIR'S HOSPITAL MANAGEMENT SYSTEM");

        mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: linear-gradient(to bottom, #e3f2fd, #ffffff);");

        // Show login screen first
        showWelcomeScreen();
        
        Scene scene = new Scene(mainLayout, 1200, 800);
        try {
            scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());
        } catch (Exception e) {
            // CSS file not found, continue without it
        }
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    private void handleSignUp(User newUser) {
        String role = newUser.getRole();
        String id;
        if (role.equals("Doctor")) {
            id = "D" + String.format("%03d", dList.size() + 1);
            Doctor docObj = new Doctor(id, newUser.getName(), newUser.getContact(), newUser.getSpecialization(), newUser.getFee(), newUser.getShift());
            dList.Insert(docObj);
        } else {
            id = "P" + String.format("%03d", pList.size() + 1);
            Patient patObj = new Patient(id, newUser.getName(), newUser.getContact());
            pList.Insert(patObj);
        }

        User userWithId = new User(newUser.getUsername(), newUser.getPassword(), role, newUser.getName(), newUser.getContact(), id, newUser.getSpecialization(), newUser.getFee(), newUser.getShift());

        registeredUsers.put(userWithId.getUsername(), userWithId);
        DatabaseHandler.addUser(userWithId);

        showAlert(Alert.AlertType.INFORMATION, "Success", "Account created! ID: " + userWithId.getId());
        showLoginScreen(role, role.equals("Doctor") ? "#4CAF50" : "#2196F3", role.equals("Doctor") ? "👨‍⚕️" : "🏥");
    }

    private void loadAppointmentsFromDatabase() {
        String sql = "SELECT * FROM appointments";
        try (Connection conn = DatabaseHandler.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            globalQueueList.clear();
            while (rs.next()) {
                CheckupRecord record = new CheckupRecord(
                        rs.getInt("id"),
                        rs.getString("patientName"),
                        rs.getString("doctorName"),
                        rs.getString("priority"),
                        rs.getString("date_time"),
                        rs.getString("status"),
                        rs.getString("symptoms"),
                        rs.getString("doctorReply")
                );
                globalQueueList.add(record);
            }
        } catch (SQLException e) {
            System.out.println("Appointments load karne mein error: " + e.getMessage());
        }
    }

    private void updateDashboardStats() {
        int count = globalQueueList.size();
        double revenue = 0;

        for (CheckupRecord record : globalQueueList) {
            if (record.getStatus().equalsIgnoreCase("Completed")) {
                revenue += 500; // Testing fee
            }
        }

        // Ab hum static variables ko update kar dete hain jo aapke dashboard cards use karte hain
        totalCheckups = count;
        totalRevenue = revenue;
    }

    // ============= WELCOME SCREEN =============
    private void showWelcomeScreen() {
        Node welcomeView = ViewFactory.createWelcomeScreen(
                () -> showLoginScreen("Doctor", "#4CAF50", "👨‍⚕️"),
                () -> showLoginScreen("Patient", "#2196F3", "🏥")
        );
        mainLayout.setCenter(welcomeView);
    }

    // ============= LOGIN SCREEN =============
    private void showLoginScreen(String role, String color, String icon) {
        Node loginView = ViewFactory.createLoginScreen(role, color, icon,
                credentials -> {
                    if (authenticateUser(credentials[0], credentials[1], role)) {
                        if (role.equals("Doctor")) {
                            showDoctorDashboard();
                        } else {
                            showPatientDashboard();
                        }
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password!");
                    }
                },
                () -> showSignupScreen(role),
                this::showWelcomeScreen
        );
        mainLayout.setCenter(loginView);
    }

    // ============= SIGNUP SCREEN =============
    private void showSignupScreen(String role) {
        Node signupView = ViewFactory.createSignupScreen(role,
                this::handleSignUp,
                () -> showLoginScreen(role, role.equals("Doctor") ? "#4CAF50" : "#2196F3", role.equals("Doctor") ? "👨‍⚕️" : "🏥"),
                this::showWelcomeScreen
        );
        mainLayout.setCenter(signupView);
    }

    // ============= MANAGE SLOTS SCREEN =============
    private void showManageSlots() {
        // Ensure slots are loaded from DB before showing the screen
        DatabaseHandler.loadDoctorSlots(currentUser);
        Node manageSlotsView = ViewFactory.createManageSlotsView(currentUser, this::showDoctorDashboard);
        BorderPane layout = new BorderPane(new ScrollPane(manageSlotsView));
        layout.setTop(ViewFactory.createDoctorBackButton(this::showDoctorDashboard));
        mainLayout.setCenter(layout);
    }

    // ============= AUTHENTICATION =============
    private boolean authenticateUser(String username, String password, String role) {
        User user = registeredUsers.get(username);
        if (user != null && user.getPassword().equals(password) && user.getRole().equals(role)) {
            currentUser = user;
            return true;
        }
        return false;
    }

    // ============= DOCTOR DASHBOARD =============
    private void showDoctorDashboard() {
        // 🔥 Sab se pehle database se slots load karein takay data purana na ho
        DatabaseHandler.loadDoctorSlots(currentUser);

        Node doctorDashboard = ViewFactory.createDoctorDashboard(currentUser,
                this::showMyPatients,
                this::showDoctorSchedule,
                this::showAllPatientsForDoctor,
                this::showDoctorProfile,
                this::showManageSlots,
                () -> {
                    currentUser = null;
                    showWelcomeScreen();
                }
        );
        mainLayout.setCenter(doctorDashboard);
    }

    // ============= PATIENT DASHBOARD =============
    private void showPatientDashboard() {
        Node patientDashboard = ViewFactory.createPatientDashboard(currentUser,
                () -> showBookAppointment(),
                this::showMyAppointments,
                this::showDoctorsListForPatient,
                this::showPatientProfile,
                () -> {
                    currentUser = null;
                    showWelcomeScreen();
                }
        );
        mainLayout.setCenter(patientDashboard);
    }
    // ============= DOCTOR FEATURES =============

    private void showMyPatients() {
        ObservableList<CheckupRecord> myPatients = globalQueueList.stream()
                .filter(r -> r.getDoctorName().contains(currentUser.getName()) && "Pending".equalsIgnoreCase(r.getStatus()))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        myPatients.sort((a, b) -> b.getPriority().compareTo(a.getPriority()));

        Node myPatientsView = ViewFactory.createMyPatientsView(myPatients, this::showPrescriptionDialog);

        BorderPane layout = new BorderPane(myPatientsView);
        layout.setTop(ViewFactory.createDoctorBackButton(this::showDoctorDashboard));
        mainLayout.setCenter(layout);
    }

    private void showPrescriptionDialog(CheckupRecord record) {
        Optional<String> result = ViewFactory.createPrescriptionDialog(record);

        result.ifPresent(reply -> {
            if (!reply.trim().isEmpty()) {
                DatabaseHandler.savePrescriptionAndComplete(record.getAppointmentId(), reply);
                record.setStatus("Completed");
                record.setDoctorReply(reply);
                showMyPatients(); // Refresh the view
            } else {
                showAlert(Alert.AlertType.WARNING, "Input Missing", "Prescription cannot be empty.");
            }
        });
    }

    private void showDoctorSchedule() {
        int emergencyCount = 0, normalCount = 0, intermediateCount = 0;

        for (CheckupRecord record : globalQueueList) {
            if (record.getDoctorName().contains(currentUser.getName())) {
                if (record.getPriority().contains("Emergency")) emergencyCount++;
                else if (record.getPriority().contains("Intermediate")) intermediateCount++;
                else normalCount++;
            }
        }

        Node scheduleView = ViewFactory.createDoctorScheduleView(emergencyCount, intermediateCount, normalCount);

        BorderPane layout = new BorderPane(scheduleView);
        layout.setTop(ViewFactory.createDoctorBackButton(this::showDoctorDashboard));
        mainLayout.setCenter(layout);
    }

    private void showAllPatientsForDoctor() {
        ObservableList<Patient> allPatients = FXCollections.observableArrayList();
        for (int i = 0; i < pList.size(); i++) {
            allPatients.add(pList.getAtIndex(i));
        }

        Node patientsListView = ViewFactory.createPatientsListView(allPatients, this::handlePatientDeletion);

        BorderPane layout = new BorderPane(new ScrollPane(patientsListView));
        layout.setTop(ViewFactory.createDoctorBackButton(this::showDoctorDashboard));
        mainLayout.setCenter(layout);
    }

    private void showDoctorProfile() {
        Node profileView = ViewFactory.createDoctorProfileView(currentUser);
        BorderPane layout = new BorderPane(profileView);
        layout.setTop(ViewFactory.createDoctorBackButton(this::showDoctorDashboard));
        mainLayout.setCenter(layout);
    }

    // ============= PATIENT FEATURES =============

    private void showBookAppointment(String... preselectedDoctor) {
        ObservableList<User> doctors = registeredUsers.values().stream()
                .filter(u -> "Doctor".equals(u.getRole()))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        String preselected = (preselectedDoctor.length > 0) ? preselectedDoctor[0] : null;

        Node bookAppointmentView = ViewFactory.createBookAppointmentView(currentUser, doctors, preselected, record -> {
            // Find the doctor to check for concurrency
            User targetDoc = registeredUsers.values().stream()
                    .filter(u -> u.getName().equals(record.getDoctorName()))
                    .findFirst().orElse(null);

            if (targetDoc != null) {
                String slot = record.getDate().substring(record.getDate().indexOf("(") + 1, record.getDate().indexOf(")"));

                // Concurrency Check
                if (targetDoc.getAvailableSlots().getOrDefault(slot, false)) {
                    showAlert(Alert.AlertType.ERROR, "Slot Taken", "This slot has just been booked. Please select another one.");
                    // Refresh view to show updated slots
                    showBookAppointment(preselectedDoctor);
                    return;
                }

                // Update in memory and DB
                targetDoc.getAvailableSlots().put(slot, true);
                DatabaseHandler.updateSlotStatus(targetDoc.getName(), slot, true);

                DatabaseHandler.addAppointment(record);
                globalQueueList.add(record);
                totalCheckups++;

                showAlert(Alert.AlertType.INFORMATION, "Booking Success", "Success! Your appointment with Dr. " + record.getDoctorName() + " for slot " + slot + " has been confirmed.");
                showPatientDashboard();
            }
        });

        BorderPane layout = new BorderPane(bookAppointmentView);
        layout.setTop(ViewFactory.createPatientBackButton(this::showPatientDashboard));
        mainLayout.setCenter(layout);
    }

    private void showMyAppointments() {
        ObservableList<CheckupRecord> myAppointments = globalQueueList.stream()
                .filter(r -> r.getPatientName().equals(currentUser.getName()))
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        Node appointmentsView = ViewFactory.createAppointmentsView(myAppointments, this::showPrescriptionScreen);

        BorderPane layout = new BorderPane(appointmentsView);
        layout.setTop(ViewFactory.createPatientBackButton(this::showPatientDashboard));
        mainLayout.setCenter(layout);
    }

    private void showDoctorsListForPatient() {
        ObservableList<Doctor> allDoctors = FXCollections.observableArrayList();
        for (int i = 0; i < dList.size(); i++) {
            allDoctors.add(dList.getAtIndex(i));
        }

        Node doctorsListView = ViewFactory.createDoctorsListView(allDoctors, this::handleDoctorDeletion);

        BorderPane layout = new BorderPane(new ScrollPane(doctorsListView));
        layout.setTop(ViewFactory.createPatientBackButton(this::showPatientDashboard));
        mainLayout.setCenter(layout);
    }

    private void showPatientProfile() {
        Node profileView = ViewFactory.createPatientProfileView(currentUser, getPatientAppointmentCount());
        BorderPane layout = new BorderPane(profileView);
        layout.setTop(ViewFactory.createPatientBackButton(this::showPatientDashboard));
        mainLayout.setCenter(layout);
    }

    private void showPrescriptionScreen(CheckupRecord record) {
        Node prescriptionView = ViewFactory.createPrescriptionScreenView(record, this::showBookAppointment);
        BorderPane layout = new BorderPane(prescriptionView);
        layout.setTop(ViewFactory.createPatientBackButton(this::showMyAppointments));
        mainLayout.setCenter(layout);
    }

    // ============= DATA HANDLING & HELPERS =============

    private void handleDoctorDeletion(Doctor doctor) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete Dr. " + doctor.getName() + "? This action cannot be undone.", ButtonType.YES, ButtonType.NO);
        confirmAlert.setHeaderText("Confirm Deletion");
        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                User userToDelete = registeredUsers.values().stream()
                        .filter(u -> u.getId().equals(doctor.getId()))
                        .findFirst().orElse(null);

                if (userToDelete != null) {
                    DatabaseHandler.deleteUserById(doctor.getId());
                    registeredUsers.remove(userToDelete.getUsername());
                    dList.deleteById(doctor.getId());
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Dr. " + doctor.getName() + " has been deleted.");
                    // Refresh the view
                    if (currentUser.getRole().equals("Doctor")) showAllPatientsForDoctor();
                    else showDoctorsListForPatient();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Could not find user credentials to delete.");
                }
            }
        });
    }

    private void handlePatientDeletion(Patient patient) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete patient " + patient.getName() + "? This action cannot be undone.", ButtonType.YES, ButtonType.NO);
        confirmAlert.setHeaderText("Confirm Deletion");
        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                User userToDelete = registeredUsers.values().stream()
                        .filter(u -> u.getId().equals(patient.getId()))
                        .findFirst().orElse(null);

                if (userToDelete != null) {
                    DatabaseHandler.deleteUserById(patient.getId());
                    registeredUsers.remove(userToDelete.getUsername());
                    pList.deleteById(patient.getId());
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Patient " + patient.getName() + " has been deleted.");
                    // Refresh view
                    if (currentUser.getRole().equals("Doctor")) showAllPatientsForDoctor();
                    else showDoctorsListForPatient();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Could not find user credentials to delete.");
                }
            }
        });
    }

    private int getPatientAppointmentCount() {
        int count = 0;
        for (CheckupRecord record : globalQueueList) {
            if (record.getPatientName().equals(currentUser.getName())) {
                count++;
            }
        }
        return count;
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}