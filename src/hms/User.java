package hms;

import java.util.HashMap;
import java.util.Map;

public class User {
    private String username, password, role, name, contact, id, specialization, shift;
    private int fee;
    private Map<String, Boolean> availableSlots = new HashMap<>();

    public User(String username, String password, String role, String name,
                String contact, String id, String specialization, int fee, String shift) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.name = name;
        this.contact = contact;
        this.id = id;

        // --- STEP 4 IMPLEMENTATION START ---
        // Agar specialization null hai (Patient ke liye), to "General" set kar do
        this.specialization = (specialization == null || specialization.isEmpty()) ? "General" : specialization;

        // Fees wahi rahegi jo pass hui
        this.fee = fee;

        // Agar shift null hai, to "N/A" (Not Applicable) set kar do
        this.shift = (shift == null || shift.isEmpty()) ? "N/A" : shift;
        // --- STEP 4 IMPLEMENTATION END ---

        if ("Doctor".equals(role)) {
            String[] times = {"09:00 AM", "09:30 AM", "10:00 AM", "10:30 AM", "11:00 AM", "11:30 AM", "12:00 PM"};
            for (String t : times) {
                availableSlots.put(t, false);
            }
        }
    }

    // --- Getters ---
    public Map<String, Boolean> getAvailableSlots() { return availableSlots; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public String getName() { return name; }
    public String getContact() { return contact; }
    public String getId() { return id; }
    public String getSpecialization() { return specialization; }
    public int getFee() { return fee; }
    public String getShift() { return shift; }

    public void addSlot(String slotTime) {
        if (availableSlots == null) {
            availableSlots = new HashMap<>();
        }
        availableSlots.put(slotTime, false);
    }
}