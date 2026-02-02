package hms;

/**
 *
 * @author pc planet
 */
public class Patient {
    private String id, name, contact;


      public Patient() {
        this.id = null;
        this.name = null;
        this.contact = null;
    }

    public Patient(String id, String name, String contact) {
        this.id = id;
        this.name = name;
        this.contact = contact;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    @Override
    public String toString() {
        return "Patient{" + "id=" + id + ", name=" + name + ", contact=" + contact + '}';
    }
    
}

