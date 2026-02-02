package hms;

public class CheckupRecord {
    private int appointmentId;
    private String patientName;
    private String doctorName;
    private String priority;
    private String date;   // Naya
    private String status; // Naya
    private String symptoms;
    private String doctorReply;

    // Constructor for loading from DB
    public CheckupRecord(int id, String p, String d, String pr, String dt, String st, String sym, String reply) {
        this.appointmentId = id;
        this.patientName = p;
        this.doctorName = d;
        this.priority = pr;
        this.date = dt;
        this.status = st;
        this.symptoms = sym;
        this.doctorReply = reply;
    }

    // Constructor for creating a new appointment
    public CheckupRecord(String p, String d, String pr, String dt, String st, String sym) {
        this.appointmentId = 0; // Not yet in DB
        this.patientName = p;
        this.doctorName = d;
        this.priority = pr;
        this.date = dt;
        this.status = st;
        this.symptoms = sym;
        this.doctorReply = null; // No reply yet
    }

    // Getters
    public int getAppointmentId() { return appointmentId; }
    public String getPatientName() { return patientName; }
    public String getDoctorName() { return doctorName; }
    public String getPriority() { return priority; }
    public String getDate() { return date; }
    public String getStatus() { return status; }
    public String getSymptoms() { return symptoms; }
    public String getDoctorReply() { return doctorReply; }

    // Setters
    public void setStatus(String status) { this.status = status; } // Status badalne ke liye
    public void setDoctorReply(String reply) { this.doctorReply = reply; }
}