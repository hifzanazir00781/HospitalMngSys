package hms;

/**
 *
 * @author pc planet
 */
public class Doctor {
     private String id,Name,Contact,specialty , shift;
       private int fees;

    public Doctor(String id, String Name, String Contact, String specialty, int fees, String shift) {
        this.id = id;
        this.Name = Name;
        this.Contact = Contact;
        this.specialty = specialty;
        this.fees = fees;
        this.shift = shift;
    }

    public String getShift() {
        return shift;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String Contact) {
        this.Contact = Contact;
    }

    public String getSpecialty() {
    return specialty; 
}

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public int getFees() {
        return fees;
    }

    public void setFees(int fees) {
        this.fees = fees;
    }

    @Override
    public String toString() {
        return "Doctor{" + "id=" + id + ", Name=" + Name + ", Contact=" + Contact + ", specialty=" + specialty + ", fees=" + fees + '}';
    }
       
}  

