package hms;
import java.sql.*; // Database se baat karne ke liye zaroori hai
import java.util.Map;

public class DatabaseHandler {
    // 1. Database file ka rasta (Ye aapke folder mein khud ban jayegi)
    private static final String URL = "jdbc:sqlite:hospital.db";

    // 2. Connection function: Ye database ka darwaza kholta hai
    public static Connection connect() {
        try {
            return DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.out.println("Database connect nahi ho saka: " + e.getMessage());
            return null;
        }
    }

    public static void addUser(User user) {
    String sql = "INSERT INTO users(username, password, role, name, id, contact, specialization, fee, shift) VALUES(?,?,?,?,?,?,?,?,?)";

    try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, user.getUsername());
        pstmt.setString(2, user.getPassword());
        pstmt.setString(3, user.getRole());
        pstmt.setString(4, user.getName());
        pstmt.setString(5, user.getId());
        pstmt.setString(6, user.getContact());
        pstmt.setString(7, user.getSpecialization());
        pstmt.setInt(8, user.getFee());
        pstmt.setString(9, user.getShift());
        pstmt.executeUpdate();
    } catch (SQLException e) {
        System.out.println("User add karne mein error: " + e.getMessage());
    }
}

public static void addAppointment(CheckupRecord record) {
    String sql = "INSERT INTO appointments(patientName, doctorName, priority, date_time, status, symptoms) VALUES(?,?,?,?,?,?)";

    try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, record.getPatientName());
        pstmt.setString(2, record.getDoctorName());
        pstmt.setString(3, record.getPriority());
        pstmt.setString(4, record.getDate());
        pstmt.setString(5, record.getStatus());
        pstmt.setString(6, record.getSymptoms());
        pstmt.executeUpdate();
    } catch (SQLException e) {
        System.out.println("Appointment save error: " + e.getMessage());
    }
}

    // 3. Setup function: Ye check karta hai ke tables bani hui hain ya nahi
    public static void setupDatabase() {
    // 1. Users Table
    String usersTable = "CREATE TABLE IF NOT EXISTS users (" +
                       "username TEXT PRIMARY KEY, " +
                       "password TEXT, " +
                       "role TEXT, " +
                       "name TEXT, " +
                       "id TEXT, " +
                       "contact TEXT, " +
                       "specialization TEXT, " +
                       "fee INTEGER, " +
                       "shift TEXT);";

    // 2. Appointments Table
    String appointmentsTable = "CREATE TABLE IF NOT EXISTS appointments (" +
                              "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                              "patientName TEXT, " +
                              "doctorName TEXT, " +
                              "priority TEXT, " +
                              "date_time TEXT, " +
                              "status TEXT, " +
                              "symptoms TEXT, " +
                              "doctorReply TEXT);";

    // 3. 🔥 Slots Table (Nayi table jo aapne mangi thi)
    String slotsTable = "CREATE TABLE IF NOT EXISTS slots (" +
                       "doctor_name TEXT, " +
                       "slot_time TEXT, " +
                       "is_booked INTEGER, " + 
                       "PRIMARY KEY (doctor_name, slot_time));";

    try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
        stmt.execute(usersTable);
        stmt.execute(appointmentsTable);
        stmt.execute(slotsTable); // Isay execute karna zaroori hai
        System.out.println("✅ Saari tables (Users, Appointments, Slots) database mein update ho gayi hain!");
    } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
    }
}

public static void loadUsers(Map<String, User> registeredUsers) {
    String sql = "SELECT * FROM users";
    try (Connection conn = connect(); 
         Statement stmt = conn.createStatement(); 
         ResultSet rs = stmt.executeQuery(sql)) {
        
        while (rs.next()) {
            // DEKHEIN: Yahan column names ko sequence mein set kiya hai taake mismatch na ho
            User u = new User(
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("role"),
                rs.getString("name"),
                rs.getString("contact"),       // RS column: contact
                rs.getString("id"),            // RS column: id
                rs.getString("specialization"),
                rs.getInt("fee"),
                rs.getString("shift")
            );
            
            // Trim() zaroori hai taake agar space ho toh login fail na ho
            registeredUsers.put(u.getUsername().trim(), u);
        }
        System.out.println("✅ " + registeredUsers.size() + " Users database se load ho chuke hain!");
        // DEBUG LINE: Taake aap terminal par dekh saken ke kaunse usernames load huay hain
        System.out.println("Available in Map: " + registeredUsers.keySet());
        
    } catch (SQLException e) {
        System.out.println("Data load karne mein error: " + e.getMessage());
    }
}

public static void updateAppointmentStatus(String patientName, String dateTime, String newStatus) {
    String sql = "UPDATE appointments SET status = ? WHERE patientName = ? AND date_time = ?";

    try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, newStatus);
        pstmt.setString(2, patientName);
        pstmt.setString(3, dateTime);
        pstmt.executeUpdate();
        System.out.println("✅ Status updated to " + newStatus);
    } catch (SQLException e) {
        System.out.println("Update error: " + e.getMessage());
    }
}

public static void deleteUserById(String id) {
    String sql = "DELETE FROM users WHERE id = ?";
    try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, id);
        int affectedRows = pstmt.executeUpdate();
        if (affectedRows > 0) {
            System.out.println("✅ User with ID " + id + " deleted from database.");
        }
    } catch (SQLException e) {
        System.out.println("User deletion error: " + e.getMessage());
    }
}

public static void savePrescriptionAndComplete(int appointmentId, String reply) {
    String sql = "UPDATE appointments SET status = 'Completed', doctorReply = ? WHERE id = ?";
    try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, reply);
        pstmt.setInt(2, appointmentId);
        pstmt.executeUpdate();
        System.out.println("✅ Prescription saved for appointment ID " + appointmentId);
    } catch (SQLException e) {
        System.out.println("Prescription save error: " + e.getMessage());
    }
}

// 1. Slot status update karne ke liye (Jab koi book kare)
public static void updateSlotStatus(String docName, String time, boolean booked) {
    String sql = "INSERT OR REPLACE INTO slots (doctor_name, slot_time, is_booked) VALUES (?, ?, ?)";
    try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, docName);
        pstmt.setString(2, time);
        pstmt.setInt(3, booked ? 1 : 0);
        pstmt.executeUpdate();
    } catch (SQLException e) {
        System.out.println("Slot update error: " + e.getMessage());
    }
}

// 2. Doctor ke slots load karne ke liye
public static void loadDoctorSlots(User doctor) {
    String sql = "SELECT * FROM slots WHERE doctor_name = ?";
    try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, doctor.getName());
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            doctor.getAvailableSlots().put(rs.getString("slot_time"), rs.getInt("is_booked") == 1);
        }
    } catch (SQLException e) {
        System.out.println("Slot load error: " + e.getMessage());
    }
}

 // ✅ YE WALA VERSION USE KAREIN (COMPLETE & SAFE)
public static void completeAppointment(String patientName, String docName, String slotTime) {
    String sql1 = "UPDATE appointments SET status = 'Completed' WHERE patientName = ? AND doctorName = ? AND date_time LIKE ?";
    String sql2 = "UPDATE slots SET is_booked = 0 WHERE doctor_name = ? AND slot_time = ?";

    try (Connection conn = connect()) {
        // 1. Appointment status update
        try (PreparedStatement pstmt1 = conn.prepareStatement(sql1)) {
            pstmt1.setString(1, patientName);
            pstmt1.setString(2, docName);
            pstmt1.setString(3, "%" + slotTime + "%"); 
            pstmt1.executeUpdate();
        }

        // 2. Doctor slot release
        try (PreparedStatement pstmt2 = conn.prepareStatement(sql2)) {
            pstmt2.setString(1, docName);
            pstmt2.setString(2, slotTime);
            pstmt2.executeUpdate();
        }
        
        System.out.println("✅ Database updated: Appointment Done & Slot Free!");
    } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
    }
}
}
