package hms;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * A factory class for creating all UI views of the application.
 * This class contains only static methods to build UI components.
 * It separates the UI creation logic from the application's business logic.
 */
public class ViewFactory {

    // ============= WELCOME SCREEN =============
    public static Node createWelcomeScreen(Runnable onDoctor, Runnable onPatient) {
        VBox container = new VBox(30);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(50));

        Label title = new Label("🏥 HIFZA & TAHIR'S HOSPITAL");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 48));
        title.setTextFill(Color.web("#1976D2"));

        Label subtitle = new Label("Hospital Management System");
        subtitle.setFont(Font.font("Arial", FontWeight.NORMAL, 24));
        subtitle.setTextFill(Color.web("#424242"));

        Label welcome = new Label("Welcome! Please select your role:");
        welcome.setFont(Font.font("Arial", FontWeight.NORMAL, 18));
        welcome.setTextFill(Color.web("#666666"));

        HBox roleCards = new HBox(30);
        roleCards.setAlignment(Pos.CENTER);

        VBox doctorCard = createRoleCard("👨‍⚕️ Doctor Login", "Login as Hospital Doctor", "#4CAF50", onDoctor);
        VBox patientCard = createRoleCard("🏥 Patient Login", "Login as Patient", "#2196F3", onPatient);

        roleCards.getChildren().addAll(doctorCard, patientCard);
        container.getChildren().addAll(title, subtitle, welcome, roleCards);

        ScrollPane scrollPane = new ScrollPane(container);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");

        return scrollPane;
    }

    // ============= LOGIN SCREEN =============
    public static Node createLoginScreen(String role, String color, String icon, Consumer<String[]> onLogin, Runnable onSignup, Runnable onBack) {
        VBox container = new VBox(20);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(50));
        container.setMaxWidth(500);

        Label header = new Label(icon + " " + role + " Login");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        header.setTextFill(Color.web(color));

        VBox formBox = new VBox(15);
        formBox.setPadding(new Insets(30));
        formBox.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setPrefHeight(40);
        usernameField.setStyle("-fx-font-size: 14px;");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setPrefHeight(40);
        passwordField.setStyle("-fx-font-size: 14px;");

        Button loginBtn = createStyledButton("🔓 Login", color);
        loginBtn.setPrefWidth(200);
        loginBtn.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            onLogin.accept(new String[]{username, password});
        });

        Hyperlink signupLink = new Hyperlink("Don't have an account? Sign up here");
        signupLink.setStyle("-fx-text-fill: " + color + ";");
        signupLink.setOnAction(e -> onSignup.run());

        Hyperlink backLink = new Hyperlink("← Back to Welcome Screen");
        backLink.setStyle("-fx-text-fill: #757575;");
        backLink.setOnAction(e -> onBack.run());

        formBox.getChildren().addAll(
                new Label("Username:"), usernameField,
                new Label("Password:"), passwordField,
                loginBtn, signupLink
        );

        container.getChildren().addAll(header, formBox, backLink);

        VBox centerWrapper = new VBox(container);
        centerWrapper.setAlignment(Pos.CENTER);
        centerWrapper.setPadding(new Insets(50));

        return centerWrapper;
    }

    // ============= SIGNUP SCREEN =============
    public static Node createSignupScreen(String role, Consumer<User> onSignup, Runnable onLogin, Runnable onBack) {
        VBox container = new VBox(20);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(50));
        container.setMaxWidth(500);

        String color = role.equals("Doctor") ? "#4CAF50" : "#2196F3";
        String icon = role.equals("Doctor") ? "👨‍⚕️" : "🏥";

        Label header = new Label(icon + " " + role + " Sign Up");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        header.setTextFill(Color.web(color));

        VBox formBox = new VBox(15);
        formBox.setPadding(new Insets(30));
        formBox.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 5);");

        TextField nameField = new TextField(); nameField.setPromptText("Full Name");
        TextField usernameField = new TextField(); usernameField.setPromptText("Username");
        PasswordField passwordField = new PasswordField(); passwordField.setPromptText("Password");
        PasswordField confirmPasswordField = new PasswordField(); confirmPasswordField.setPromptText("Confirm Password");

        ObservableList<Country> countries = FXCollections.observableArrayList(
                new Country("Pakistan", "+92", "🇵🇰", 10),
                new Country("India", "+91", "🇮🇳", 10),
                new Country("USA", "+1", "🇺🇸", 10),
                new Country("UK", "+44", "🇬🇧", 10),
                new Country("Saudi Arabia", "+966", "🇸🇦", 9)
        );

        ComboBox<Country> countryCodeCombo = new ComboBox<>(countries);
        countryCodeCombo.setPromptText("Code");
        countryCodeCombo.setPrefWidth(120);

        countryCodeCombo.setCellFactory(param -> new ListCell<Country>() {
            @Override
            protected void updateItem(Country item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getFlag() + " " + item.getName() + " (" + item.getCode() + ")");
            }
        });

        countryCodeCombo.setButtonCell(new ListCell<Country>() {
            @Override
            protected void updateItem(Country item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getFlag() + " " + item.getCode());
            }
        });

        countryCodeCombo.getSelectionModel().selectFirst();

        TextField contactNumberField = new TextField();
        contactNumberField.setPromptText("3001234567");

        HBox contactBox = new HBox(5, countryCodeCombo, contactNumberField);
        HBox.setHgrow(contactNumberField, Priority.ALWAYS);

        formBox.getChildren().addAll(
                new Label("Full Name:"), nameField,
                new Label("Username:"), usernameField,
                new Label("Password:"), passwordField,
                new Label("Confirm Password:"), confirmPasswordField,
                new Label("Contact:"), contactBox
        );

        final TextField specializationField = new TextField(); specializationField.setPromptText("Specialization");
        final TextField feeField = new TextField(); feeField.setPromptText("Fee");
        final ComboBox<String> shiftBox = new ComboBox<>();
        shiftBox.getItems().addAll("9 AM - 5 PM", "6 PM - 12 AM");
        shiftBox.setPromptText("Select Shift");
        shiftBox.setPrefWidth(500);

        if (role.equals("Doctor")) {
            formBox.getChildren().addAll(
                    new Label("Specialization:"), specializationField,
                    new Label("Consultation Fee:"), feeField,
                    new Label("Shift:"), shiftBox
            );
        }

        Button signupBtn = createStyledButton("📝 Sign Up", color);
        signupBtn.setPrefWidth(200);

        signupBtn.setOnAction(e -> {
            try {
                String name = nameField.getText();
                String username = usernameField.getText();
                String password = passwordField.getText();
                String confirm = confirmPasswordField.getText();
                Country selectedCountry = countryCodeCombo.getValue();
                String contactNumber = contactNumberField.getText();

                if (name.isEmpty() || username.isEmpty() || password.isEmpty() || contactNumber.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Please fill all required fields!");
                    return;
                }
                if (!password.equals(confirm)) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Passwords do not match!");
                    return;
                }
                String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";
                if (!password.matches(passwordPattern)) {
                    showAlert(Alert.AlertType.ERROR, "Weak Password", "Password must be at least 8 characters, with 1 uppercase, 1 lowercase, and 1 number.");
                    return;
                }
                if (selectedCountry == null) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Please select a country code.");
                    return;
                }
                if (!contactNumber.matches("\\d+")) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Contact number must only contain digits (0-9).");
                    return;
                }
                if (contactNumber.length() != selectedCountry.getLength()) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Contact number for " + selectedCountry.getName() + " must be " + selectedCountry.getLength() + " digits.");
                    return;
                }
                String contact = selectedCountry.getCode() + contactNumber;

                User newUser;
                if (role.equals("Doctor")) {
                    String spec = specializationField.getText();
                    String feeStr = feeField.getText();
                    String shift = (shiftBox.getValue() != null) ? shiftBox.getValue() : "9 AM - 5 PM";

                    if(spec.isEmpty() || feeStr.isEmpty()) {
                        showAlert(Alert.AlertType.ERROR, "Error", "Please fill specialization and fee!");
                        return;
                    }

                    int fee = Integer.parseInt(feeStr);
                    if (fee < 0) {
                        showAlert(Alert.AlertType.ERROR, "Error", "Consultation Fee cannot be negative.");
                        return;
                    }
                    newUser = new User(username, password, role, name, contact, "", spec, fee, shift);
                } else {
                    newUser = new User(username, password, role, name, contact, "", null, 0, null);
                }
                onSignup.accept(newUser);

            } catch (NumberFormatException nfe) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please enter a valid number for Consultation Fee!");
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred: " + ex.getMessage());
            }
        });

        Hyperlink loginLink = new Hyperlink("Already have an account? Login here");
        loginLink.setOnAction(ev -> onLogin.run());

        Hyperlink backLink = new Hyperlink("← Back to Welcome Screen");
        backLink.setOnAction(ev -> onBack.run());

        formBox.getChildren().addAll(signupBtn, loginLink);
        container.getChildren().addAll(header, formBox, backLink);

        ScrollPane scrollPane = new ScrollPane(container);
        scrollPane.setFitToWidth(true);
        return scrollPane;
    }

    // ============= DOCTOR DASHBOARD =============
    public static Node createDoctorDashboard(User currentUser, Runnable onMyPatients, Runnable onMySchedule, Runnable onAllPatients, Runnable onProfile, Runnable onManageSlots, Runnable onLogout) {
        VBox container = new VBox(30);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(50));

        VBox headerBox = new VBox(10);
        headerBox.setAlignment(Pos.CENTER);

        Label welcomeLabel = new Label("Welcome, Dr. " + currentUser.getName() + " 👨‍⚕️");
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        welcomeLabel.setTextFill(Color.web("#4CAF50"));

        Label roleLabel = new Label("Specialization: " + currentUser.getSpecialization());
        roleLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 18));
        roleLabel.setTextFill(Color.web("#666666"));

        Label shiftLabel = new Label("Working Shift: " + (currentUser.getShift() != null ? currentUser.getShift() : "N/A"));
        shiftLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        shiftLabel.setTextFill(Color.web("#2196F3"));

        headerBox.getChildren().addAll(welcomeLabel, roleLabel, shiftLabel);

        GridPane menuGrid = new GridPane();
        menuGrid.setHgap(30);
        menuGrid.setVgap(30);
        menuGrid.setAlignment(Pos.CENTER);

        VBox myPatientsCard = createMenuCard("👥 My Patients", "View patients assigned to you", "#4CAF50", onMyPatients);
        VBox myScheduleCard = createMenuCard("📅 My Schedule", "View your checkup queue", "#2196F3", onMySchedule);
        VBox allPatientsCard = createMenuCard("🏥 All Patients", "View all hospital patients", "#FF9800", onAllPatients);
        VBox profileCard = createMenuCard("⚙️ My Profile", "View and edit profile", "#9C27B0", onProfile);
        VBox slotsCard = createMenuCard("🕒 Manage Slots", "Set your available timings", "#E91E63", onManageSlots);

        menuGrid.add(myPatientsCard, 0, 0);
        menuGrid.add(myScheduleCard, 1, 0);
        menuGrid.add(allPatientsCard, 0, 1);
        menuGrid.add(profileCard, 1, 1);
        menuGrid.add(slotsCard, 0, 2, 2, 1);
        GridPane.setHalignment(slotsCard, HPos.CENTER);

        Button logoutBtn = new Button("🚪 Logout");
        logoutBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 10 30;");
        logoutBtn.setOnAction(e -> onLogout.run());

        container.getChildren().addAll(headerBox, menuGrid, logoutBtn);

        ScrollPane scrollPane = new ScrollPane(container);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");

        return scrollPane;
    }

    // ============= PATIENT DASHBOARD =============
    public static Node createPatientDashboard(User currentUser, Runnable onBookAppointment, Runnable onMyAppointments, Runnable onListDoctors, Runnable onProfile, Runnable onLogout) {
        VBox container = new VBox(30);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(50));

        VBox headerBox = new VBox(10);
        headerBox.setAlignment(Pos.CENTER);

        Label welcomeLabel = new Label("Welcome, " + currentUser.getName() + " 🏥");
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        welcomeLabel.setTextFill(Color.web("#2196F3"));

        Label idLabel = new Label("Patient ID: " + currentUser.getId());
        idLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 18));
        idLabel.setTextFill(Color.web("#666666"));

        headerBox.getChildren().addAll(welcomeLabel, idLabel);

        GridPane menuGrid = new GridPane();
        menuGrid.setHgap(30);
        menuGrid.setVgap(30);
        menuGrid.setAlignment(Pos.CENTER);

        VBox bookAppointmentCard = createMenuCard("📅 Book Appointment", "Schedule checkup with doctor", "#2196F3", onBookAppointment);
        VBox myAppointmentsCard = createMenuCard("📋 My Appointments", "View your appointment history", "#4CAF50", onMyAppointments);
        VBox doctorsListCard = createMenuCard("👨‍⚕️ View Doctors", "Browse available doctors", "#FF9800", onListDoctors);
        VBox profileCard = createMenuCard("⚙️ My Profile", "View and edit profile", "#9C27B0", onProfile);

        menuGrid.add(bookAppointmentCard, 0, 0);
        menuGrid.add(myAppointmentsCard, 1, 0);
        menuGrid.add(doctorsListCard, 0, 1);
        menuGrid.add(profileCard, 1, 1);

        Button logoutBtn = new Button("🚪 Logout");
        logoutBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 10 30;");
        logoutBtn.setOnAction(e -> onLogout.run());

        container.getChildren().addAll(headerBox, menuGrid, logoutBtn);

        ScrollPane scrollPane = new ScrollPane(container);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");

        return scrollPane;
    }

    // ============= DOCTOR FEATURES =============

    public static Node createMyPatientsView(ObservableList<CheckupRecord> myPatients, Consumer<CheckupRecord> onPrescribe) {
        VBox container = new VBox(20);
        container.setPadding(new Insets(30));

        HBox header = createSectionHeader("👥 My Patients");

        TableView<CheckupRecord> table = new TableView<>();
        table.setPrefHeight(500);

        TableColumn<CheckupRecord, String> nameCol = new TableColumn<>("Patient Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("patientName"));
        nameCol.setPrefWidth(200);

        TableColumn<CheckupRecord, String> priorityCol = new TableColumn<>("Priority");
        priorityCol.setCellValueFactory(new PropertyValueFactory<>("priority"));
        priorityCol.setPrefWidth(150);

        TableColumn<CheckupRecord, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(120);

        TableColumn<CheckupRecord, Void> actionCol = new TableColumn<>("Action");
        actionCol.setPrefWidth(150);
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Prescribe 📝");
            {
                btn.setStyle("-fx-background-color: #007BFF; -fx-text-fill: white; -fx-font-weight: bold;");
                btn.setOnAction(event -> {
                    CheckupRecord record = getTableView().getItems().get(getIndex());
                    if (record != null) {
                        onPrescribe.accept(record);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    CheckupRecord record = getTableView().getItems().get(getIndex());
                    if (record != null && "Pending".equalsIgnoreCase(record.getStatus())) {
                        setGraphic(btn);
                    } else {
                        setGraphic(null);
                    }
                }
            }
        });

        table.getColumns().addAll(nameCol, priorityCol, statusCol, actionCol);
        table.setItems(myPatients);

        VBox tableBox = new VBox(10, new Label("Assigned Patients:"), table);
        tableBox.setPadding(new Insets(20));
        tableBox.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        container.getChildren().addAll(header, tableBox);
        return new ScrollPane(container);
    }

    public static Optional<String> createPrescriptionDialog(CheckupRecord record) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Prescription for " + record.getPatientName());
        dialog.setHeaderText("Review symptoms and provide a prescription.");

        ButtonType saveButtonType = new ButtonType("Save Prescription", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextArea symptomsArea = new TextArea(record.getSymptoms());
        symptomsArea.setEditable(false);
        symptomsArea.setWrapText(true);
        symptomsArea.setPrefHeight(80);

        TextArea replyArea = new TextArea();
        replyArea.setPromptText("Enter prescription and advice here...");
        replyArea.setWrapText(true);
        replyArea.setPrefHeight(120);

        grid.add(new Label("Patient Symptoms:"), 0, 0);
        grid.add(symptomsArea, 1, 0);
        grid.add(new Label("Your Prescription:"), 0, 1);
        grid.add(replyArea, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButtonType) {
                return replyArea.getText();
            }
            return null;
        });

        return dialog.showAndWait();
    }

    public static Node createDoctorScheduleView(int emergency, int intermediate, int normal) {
        VBox container = new VBox(20);
        container.setPadding(new Insets(30));

        HBox header = createSectionHeader("📅 My Schedule");

        GridPane statsGrid = new GridPane();
        statsGrid.setHgap(20);
        statsGrid.setVgap(20);
        statsGrid.setAlignment(Pos.CENTER);

        VBox emergencyCard = createStatCard("🔴 Emergency", String.valueOf(emergency), "#f44336");
        VBox intermediateCard = createStatCard("🟡 Intermediate", String.valueOf(intermediate), "#FF9800");
        VBox normalCard = createStatCard("🟢 Normal", String.valueOf(normal), "#4CAF50");

        statsGrid.add(emergencyCard, 0, 0);
        statsGrid.add(intermediateCard, 1, 0);
        statsGrid.add(normalCard, 2, 0);

        container.getChildren().addAll(header, statsGrid);
        return new ScrollPane(container);
    }

    public static Node createDoctorProfileView(User currentUser) {
        VBox container = new VBox(20);
        container.setPadding(new Insets(30));
        container.setAlignment(Pos.CENTER);
        container.setMaxWidth(600);

        HBox header = createSectionHeader("⚙️ My Profile");

        VBox profileBox = new VBox(15);
        profileBox.setPadding(new Insets(30));
        profileBox.setStyle("-fx-background-color: white; -fx-background-radius: 15;");

        profileBox.getChildren().addAll(
                createLabel("Doctor ID: " + currentUser.getId()),
                createLabel("Name: Dr. " + currentUser.getName()),
                createLabel("Specialization: " + currentUser.getSpecialization()),
                createLabel("Contact: " + currentUser.getContact()),
                createLabel("Consultation Fee: Rs. " + currentUser.getFee())
        );

        container.getChildren().addAll(header, profileBox);
        return new ScrollPane(container);
    }

    public static Node createManageSlotsView(User currentUser, Runnable onBack) {
        VBox container = new VBox(20);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(50));
        container.setStyle("-fx-background-color: #f4f9ff;");

        Label header = new Label("📅 Manage Your Available Slots");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 24));

        String shift = currentUser.getShift();
        int startHour, endHour;

        if (shift != null && shift.contains("6 PM")) {
            startHour = 18; endHour = 22;
        } else {
            startHour = 9; endHour = 17;
        }

        GridPane slotsGrid = new GridPane();
        slotsGrid.setHgap(15); slotsGrid.setVgap(15);
        slotsGrid.setAlignment(Pos.CENTER);

        int row = 0, col = 0;
        for (int hour = startHour; hour < endHour; hour++) {
            for (int min : new int[]{0, 30}) {
                String period = (hour >= 12) ? " PM" : " AM";
                int displayHour = (hour > 12) ? hour - 12 : (hour == 0 ? 12 : hour);
                String time = String.format("%02d:%02d%s", displayHour, min, period);

                CheckBox cb = new CheckBox(time);
                cb.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

                if (currentUser.getAvailableSlots().containsKey(time)) {
                    cb.setSelected(true);
                }

                cb.setOnAction(e -> {
                    if (cb.isSelected()) {
                        currentUser.getAvailableSlots().put(time, false);
                        DatabaseHandler.updateSlotStatus(currentUser.getName(), time, false);
                    } else {
                        currentUser.getAvailableSlots().remove(time);
                        DatabaseHandler.updateSlotStatus(currentUser.getName(), time, true);
                    }
                });

                slotsGrid.add(cb, col, row);
                col++;
                if (col > 3) { col = 0; row++; }
            }
        }

        Button backBtn = createStyledButton("🔙 Save & Back", "#4CAF50");
        backBtn.setOnAction(e -> {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Your slots have been saved successfully!");
            onBack.run();
        });

        container.getChildren().addAll(header, new Label("Select available timings for Shift: " + shift), slotsGrid, backBtn);
        return new ScrollPane(container);
    }

    // ============= PATIENT FEATURES =============

    public static Node createBookAppointmentView(User currentUser, ObservableList<User> doctors, String preselectedDoctor, Consumer<CheckupRecord> onBook) {
        VBox container = new VBox(20);
        container.setPadding(new Insets(30));
        container.setAlignment(Pos.CENTER);

        HBox header = createSectionHeader("📅 Book Appointment");

        VBox formBox = new VBox(15);
        formBox.setPadding(new Insets(30));
        formBox.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);");
        formBox.setMaxWidth(500);

        ComboBox<String> doctorCombo = new ComboBox<>();
        doctorCombo.setPromptText("Select Doctor");
        doctorCombo.setPrefWidth(400);
        doctors.forEach(u -> doctorCombo.getItems().add(u.getName() + " (" + u.getSpecialization() + ")"));

        if (preselectedDoctor != null) {
            for (String item : doctorCombo.getItems()) {
                if (item.startsWith(preselectedDoctor)) {
                    doctorCombo.setValue(item);
                    break;
                }
            }
        }

        ComboBox<String> slotCombo = new ComboBox<>();
        slotCombo.setPromptText("Select Available Slot");
        slotCombo.setPrefWidth(400);

        doctorCombo.setOnAction(e -> {
            slotCombo.getItems().clear();
            String selectedDocInfo = doctorCombo.getValue();
            if (selectedDocInfo != null) {
                String docName = selectedDocInfo.split(" \\(")[0];
                doctors.stream()
                        .filter(u -> u.getName().equals(docName))
                        .findFirst()
                        .ifPresent(doc -> {
                            doc.getAvailableSlots().forEach((time, isBooked) -> {
                                if (!isBooked) slotCombo.getItems().add(time);
                            });
                            slotCombo.getItems().sort(String::compareTo);
                        });
            }
        });

        ComboBox<String> priorityCombo = new ComboBox<>();
        priorityCombo.getItems().addAll("1 - Normal", "2 - Intermediate", "3 - Emergency");
        priorityCombo.setPromptText("Select Priority");
        priorityCombo.setPrefWidth(400);

        TextArea symptomsArea = new TextArea();
        symptomsArea.setPromptText("Describe your symptoms (e.g. Fever, Headache)");
        symptomsArea.setPrefHeight(80);
        symptomsArea.setPrefWidth(400);

        Button bookBtn = createStyledButton("📅 Confirm Booking", "#2196F3");
        bookBtn.setPrefWidth(200);

        bookBtn.setOnAction(e -> {
            String selectedDocInfo = doctorCombo.getValue();
            String selectedSlot = slotCombo.getValue();
            String priority = priorityCombo.getValue();
            String symptoms = symptomsArea.getText().trim();

            if (selectedDocInfo == null || selectedSlot == null || priority == null) {
                showAlert(Alert.AlertType.ERROR, "Selection Error", "Please select a Doctor, Slot, and Priority!");
                return;
            }
            if (symptoms.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Symptoms Missing", "Please describe your symptoms!");
                return;
            }

            String docName = selectedDocInfo.split(" \\(")[0];
            User targetDoc = doctors.stream().filter(u -> u.getName().equals(docName)).findFirst().orElse(null);

            if (targetDoc != null) {
                if (targetDoc.getAvailableSlots().getOrDefault(selectedSlot, false)) {
                    showAlert(Alert.AlertType.ERROR, "Slot Taken", "This slot has already been booked by someone else!");
                    return;
                }

                String fullDateSlot = java.time.LocalDate.now().toString() + " (" + selectedSlot + ")";
                CheckupRecord record = new CheckupRecord(currentUser.getName(), docName, priority, fullDateSlot, "Pending", symptoms);
                onBook.accept(record);
            }
        });

        formBox.getChildren().addAll(
                new Label("Select Doctor:"), doctorCombo,
                new Label("Available Timing:"), slotCombo,
                new Label("Priority Level:"), priorityCombo,
                new Label("Describe Symptoms:"), symptomsArea,
                bookBtn
        );

        container.getChildren().addAll(header, formBox);
        return new ScrollPane(container);
    }

    public static Node createAppointmentsView(ObservableList<CheckupRecord> myAppointments, Consumer<CheckupRecord> onViewPrescription) {
        VBox container = new VBox(20);
        container.setPadding(new Insets(30));

        HBox header = createSectionHeader("📋 My Appointments");

        TableView<CheckupRecord> table = new TableView<>();
        table.setPrefHeight(500);

        TableColumn<CheckupRecord, String> doctorCol = new TableColumn<>("Doctor");
        doctorCol.setCellValueFactory(new PropertyValueFactory<>("doctorName"));
        doctorCol.setPrefWidth(250);

        TableColumn<CheckupRecord, String> dateCol = new TableColumn<>("Date & Time");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        dateCol.setPrefWidth(200);

        TableColumn<CheckupRecord, String> priorityCol = new TableColumn<>("Priority");
        priorityCol.setCellValueFactory(new PropertyValueFactory<>("priority"));
        priorityCol.setPrefWidth(150);

        TableColumn<CheckupRecord, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(150);
        statusCol.setCellFactory(column -> new TableCell<CheckupRecord, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null); setStyle("");
                } else {
                    setText(item);
                    setAlignment(Pos.CENTER);
                    if (item.equalsIgnoreCase("Pending")) {
                        setStyle("-fx-background-color: #FFF3E0; -fx-text-fill: #E65100; -fx-font-weight: bold; -fx-background-radius: 5;");
                    } else if (item.equalsIgnoreCase("Completed")) {
                        setStyle("-fx-background-color: #E8F5E9; -fx-text-fill: #2E7D32; -fx-font-weight: bold; -fx-background-radius: 5;");
                    } else {
                        setStyle("-fx-background-color: #E3F2FD; -fx-text-fill: #1565C0; -fx-font-weight: bold; -fx-background-radius: 5;");
                    }
                }
            }
        });

        TableColumn<CheckupRecord, Void> actionCol = new TableColumn<>("Action");
        actionCol.setPrefWidth(150);
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Button viewBtn = new Button("View Prescription 🔎");
            {
                viewBtn.setStyle("-fx-background-color: #0288D1; -fx-text-fill: white;");
                viewBtn.setOnAction(event -> {
                    CheckupRecord record = getTableView().getItems().get(getIndex());
                    if (record != null) onViewPrescription.accept(record);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    CheckupRecord record = getTableView().getItems().get(getIndex());
                    setGraphic(record != null && "Completed".equalsIgnoreCase(record.getStatus()) ? viewBtn : null);
                }
            }
        });

        table.getColumns().addAll(doctorCol, dateCol, priorityCol, statusCol, actionCol);
        table.setItems(myAppointments);

        VBox tableBox = new VBox(10, new Label("Your Appointments:"), table);
        tableBox.setPadding(new Insets(20));
        tableBox.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        container.getChildren().addAll(header, tableBox);
        return new ScrollPane(container);
    }

    public static Node createDoctorsListView(ObservableList<Doctor> doctors, Consumer<Doctor> onDelete) {
        VBox listBox = new VBox(10);
        listBox.setPadding(new Insets(20));
        listBox.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        Label title = new Label("📋 Available Doctors");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.setTextFill(Color.web("#1976D2"));

        TextField searchField = new TextField();
        searchField.setPromptText("🔍 Search by Doctor Name or Specialization...");
        searchField.setStyle("-fx-padding: 8; -fx-background-radius: 5; -fx-border-color: #ddd; -fx-border-radius: 5;");

        TableView<Doctor> table = new TableView<>();
        table.setPrefHeight(400);

        TableColumn<Doctor, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(80);

        TableColumn<Doctor, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(150);

        TableColumn<Doctor, String> specCol = new TableColumn<>("Specialization");
        specCol.setCellValueFactory(new PropertyValueFactory<>("specialty"));
        specCol.setPrefWidth(150);

        TableColumn<Doctor, String> contactCol = new TableColumn<>("Contact");
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
        contactCol.setPrefWidth(120);

        TableColumn<Doctor, Integer> feeCol = new TableColumn<>("Fee");
        feeCol.setCellValueFactory(new PropertyValueFactory<>("fees"));
        feeCol.setPrefWidth(100);

        TableColumn<Doctor, Void> actionCol = new TableColumn<>("Action");
        actionCol.setPrefWidth(120);
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Button deleteBtn = new Button("Delete 🗑️");
            {
                deleteBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-weight: bold;");
                deleteBtn.setOnAction(event -> {
                    Doctor doctor = getTableView().getItems().get(getIndex());
                    if (doctor != null) onDelete.accept(doctor);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteBtn);
            }
        });
        table.getColumns().addAll(idCol, nameCol, specCol, contactCol, feeCol, actionCol);

        FilteredList<Doctor> filteredData = new FilteredList<>(doctors, p -> true);
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredData.setPredicate(doctor -> {
                if (newVal == null || newVal.isEmpty()) return true;
                String filter = newVal.toLowerCase();
                return doctor.getName().toLowerCase().contains(filter) || doctor.getSpecialty().toLowerCase().contains(filter);
            });
        });

        table.setItems(filteredData);
        listBox.getChildren().addAll(title, searchField, table);
        return listBox;
    }

    public static Node createPatientsListView(ObservableList<Patient> patients, Consumer<Patient> onDelete) {
        VBox listBox = new VBox(10);
        listBox.setPadding(new Insets(20));
        listBox.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        Label title = new Label("📋 Registered Patients");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.setTextFill(Color.web("#1976D2"));

        TableView<Patient> table = new TableView<>();
        table.setPrefHeight(400);

        TableColumn<Patient, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(100);

        TableColumn<Patient, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(200);

        TableColumn<Patient, String> contactCol = new TableColumn<>("Contact");
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
        contactCol.setPrefWidth(150);

        TableColumn<Patient, Void> actionCol = new TableColumn<>("Action");
        actionCol.setPrefWidth(120);
        actionCol.setCellFactory(param -> new TableCell<>() {
            private final Button deleteBtn = new Button("Delete 🗑️");
            {
                deleteBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-weight: bold;");
                deleteBtn.setOnAction(event -> {
                    Patient patient = getTableView().getItems().get(getIndex());
                    if (patient != null) onDelete.accept(patient);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteBtn);
            }
        });

        table.getColumns().addAll(idCol, nameCol, contactCol, actionCol);
        table.setItems(patients);

        listBox.getChildren().addAll(title, table);
        return listBox;
    }

    public static Node createPatientProfileView(User currentUser, int appointmentCount) {
        VBox container = new VBox(20);
        container.setPadding(new Insets(30));
        container.setAlignment(Pos.CENTER);
        container.setMaxWidth(600);

        HBox header = createSectionHeader("⚙️ My Profile");

        VBox profileBox = new VBox(15);
        profileBox.setPadding(new Insets(30));
        profileBox.setStyle("-fx-background-color: white; -fx-background-radius: 15;");

        profileBox.getChildren().addAll(
                createLabel("Patient ID: " + currentUser.getId()),
                createLabel("Name: " + currentUser.getName()),
                createLabel("Contact: " + currentUser.getContact()),
                createLabel("Total Appointments: " + appointmentCount)
        );

        container.getChildren().addAll(header, profileBox);
        return new ScrollPane(container);
    }

    public static Node createPrescriptionScreenView(CheckupRecord record, Consumer<String> onFollowUp) {
        VBox container = new VBox(20);
        container.setPadding(new Insets(30));
        container.setAlignment(Pos.CENTER);
        container.setMaxWidth(700);

        HBox header = createSectionHeader("📝 Prescription Details");

        VBox detailsBox = new VBox(15);
        detailsBox.setPadding(new Insets(30));
        detailsBox.setStyle("-fx-background-color: white; -fx-background-radius: 15; -fx-border-color: #ccc; -fx-border-radius: 15;");

        detailsBox.getChildren().add(createDetailRow("Doctor:", "Dr. " + record.getDoctorName()));
        detailsBox.getChildren().add(createDetailRow("Appointment Date:", record.getDate()));

        TextArea symptomsArea = new TextArea(record.getSymptoms());
        symptomsArea.setEditable(false);
        symptomsArea.setWrapText(true);
        symptomsArea.setPrefHeight(100);

        TextArea prescriptionArea = new TextArea(record.getDoctorReply());
        prescriptionArea.setEditable(false);
        prescriptionArea.setWrapText(true);
        prescriptionArea.setPrefHeight(150);

        detailsBox.getChildren().addAll(createLabel("Your Symptoms:"), symptomsArea, createLabel("Doctor's Prescription:"), prescriptionArea);

        Button followupBtn = createStyledButton("Book Follow-up", "#FF9800");
        followupBtn.setOnAction(e -> onFollowUp.accept(record.getDoctorName()));

        HBox buttonBox = new HBox(20, followupBtn);
        buttonBox.setAlignment(Pos.CENTER);

        container.getChildren().addAll(header, detailsBox, buttonBox);
        return new ScrollPane(container);
    }

    // ============= HELPER METHODS =============

    private static VBox createRoleCard(String title, String description, String color, Runnable action) {
        VBox card = new VBox(15);
        card.setPrefSize(350, 200);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(30));
        card.setStyle(String.format(
                "-fx-background-color: white; -fx-background-radius: 15; -fx-border-radius: 15; -fx-border-color: %s; -fx-border-width: 3; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 5);",
                color
        ));

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        titleLabel.setTextFill(Color.web(color));

        Label descLabel = new Label(description);
        descLabel.setFont(Font.font("Arial", 14));
        descLabel.setTextFill(Color.web("#666666"));

        Button actionBtn = new Button("Continue");
        actionBtn.setPrefSize(150, 40);
        actionBtn.setStyle(String.format("-fx-background-color: %s; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 20;", color));
        actionBtn.setOnAction(e -> action.run());

        card.setOnMouseEntered(e -> card.setStyle(card.getStyle() + "-fx-cursor: hand;"));
        card.setOnMouseExited(e -> card.setStyle(card.getStyle().replace("-fx-cursor: hand;", "")));

        card.getChildren().addAll(titleLabel, descLabel, actionBtn);
        return card;
    }

    private static VBox createMenuCard(String title, String description, String color, Runnable action) {
        VBox card = new VBox(15);
        card.setPrefSize(400, 200);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(30));
        card.setStyle(String.format(
                "-fx-background-color: white; -fx-background-radius: 15; -fx-border-radius: 15; -fx-border-color: %s; -fx-border-width: 3; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 5);",
                color));

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        titleLabel.setTextFill(Color.web(color));

        Label descLabel = new Label(description);
        descLabel.setFont(Font.font("Arial", 14));
        descLabel.setTextFill(Color.web("#666666"));
        descLabel.setWrapText(true);
        descLabel.setAlignment(Pos.CENTER);

        Button actionBtn = new Button("Open");
        actionBtn.setPrefSize(120, 35);
        actionBtn.setStyle(String.format("-fx-background-color: %s; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 20;", color));
        actionBtn.setOnAction(e -> action.run());

        card.setOnMouseEntered(e -> card.setStyle(card.getStyle() + "-fx-cursor: hand;"));
        card.setOnMouseExited(e -> card.setStyle(card.getStyle().replace("-fx-cursor: hand;", "")));

        card.getChildren().addAll(titleLabel, descLabel, actionBtn);
        return card;
    }

    private static VBox createStatCard(String title, String value, String color) {
        VBox card = new VBox(10);
        card.setPrefSize(200, 120);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(20));
        card.setStyle(String.format("-fx-background-color: %s; -fx-background-radius: 15; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 5);", color));

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        titleLabel.setTextFill(Color.WHITE);

        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        valueLabel.setTextFill(Color.WHITE);

        card.getChildren().addAll(titleLabel, valueLabel);
        return card;
    }

    public static HBox createSectionHeader(String title) {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(10));

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        titleLabel.setTextFill(Color.web("#1976D2"));

        header.getChildren().add(titleLabel);
        return header;
    }

    public static HBox createDoctorBackButton(Runnable onBack) {
        HBox buttonBox = new HBox();
        buttonBox.setPadding(new Insets(10));
        Button backBtn = new Button("⬅ Back to Dashboard");
        backBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 5; -fx-padding: 10 20;");
        backBtn.setOnAction(e -> onBack.run());
        buttonBox.getChildren().add(backBtn);
        return buttonBox;
    }

    public static HBox createPatientBackButton(Runnable onBack) {
        HBox buttonBox = new HBox();
        buttonBox.setPadding(new Insets(10));
        Button backBtn = new Button("⬅ Back to Dashboard");
        backBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 5; -fx-padding: 10 20;");
        backBtn.setOnAction(e -> onBack.run());
        buttonBox.getChildren().add(backBtn);
        return buttonBox;
    }

    private static Button createStyledButton(String text, String color) {
        Button btn = new Button(text);
        btn.setPrefWidth(200);
        btn.setPrefHeight(40);
        btn.setStyle(String.format("-fx-background-color: %s; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 5;", color));
        return btn;
    }

    private static Label createLabel(String text) {
        Label label = new Label(text);
        label.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        label.setTextFill(Color.web("#333333"));
        return label;
    }

    private static HBox createDetailRow(String label, String value) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);
        Label labelNode = new Label(label);
        labelNode.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        labelNode.setMinWidth(150);

        Label valueNode = new Label(value);
        valueNode.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        valueNode.setWrapText(true);

        row.getChildren().addAll(labelNode, valueNode);
        return row;
    }

    private static void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}