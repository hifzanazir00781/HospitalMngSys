package hms;

/**
 *
 * @author pc planet
 */
public class Checkup {
    Doctor Doctor;
    Patient Patient;
    int Priority;
    String Recomendation,Date , status;

    public Checkup(Doctor Doctor, Patient Patient, int Priority, String Recomendation, String Date) {
        this.Doctor = Doctor;
        this.Patient = Patient;
        this.Priority = Priority;
        this.Recomendation = Recomendation;
        this.Date = Date;
        this.status = "Pending";
    }

    // Getter aur Setter for status
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Doctor getDoctor() {
        return Doctor;
    }

    public void setDoctor(Doctor Doctor) {
        this.Doctor = Doctor;
    }

    public Patient getPatient() {
        return Patient;
    }

    public void setPatient(Patient Patient) {
        this.Patient = Patient;
    }

    public int getPriority() {
        return Priority;
    }

    public void setPriority(int Priority) {
        this.Priority = Priority;
    }

    public String getRecomendation() {
        return Recomendation;
    }

    public void setRecomendation(String Recomendation) {
        this.Recomendation = Recomendation;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

    @Override
    public String toString() {
        return "Checkup{" + "Doctor=" + Doctor.toString() + ", Patient=" + Patient.toString() + ", Priority=" + Priority + ", Recomendation=" + Recomendation + ", Date=" + Date + '}';
    }
    
}

