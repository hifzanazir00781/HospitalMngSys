package hms;

/**
 *
 * @author pc planet
 */
class DNode{
    Doctor doctor;
    DNode next,prev;

    public DNode() {
        //empty contructor
    }

    public DNode(Doctor doctor) {
        this.doctor = doctor;
        this.next = null;
        this.prev = null;
    }
    
}
public class DoctorsList {
     DNode head,tail;

    public DoctorsList() {
        head=null;
        tail=null;
    }
     public void Insert(Doctor doctor){
       DNode node = new DNode(doctor);
       
       if(head==null || tail==null)//list empty
       {
           head = node;
           tail = node;
       }else // if we wanna create list
       {
           head.next=node;
           node.prev=head;
           head=node;
       }
   }
   public Doctor searchById(String id){
       DNode temp=head;
       while(temp!=null){
           if(temp.doctor.getId().equals(id))
           {
            return temp.doctor;
           }
           temp=temp.prev;
       }
       return null;
   }
   
      public Doctor searchByContact(String contact){
       DNode temp=head;
       while(temp!=null){
           if(temp.doctor.getContact().equals(contact))
           {
            return temp.doctor;
           }
           temp=temp.prev;
       }
       return null;
   }
      
        public void AllDoctorInfo(){
       DNode temp=head;
       while(temp!=null){
           
           System.out.println("Doctor ID = "+temp.doctor.getId()+"    Specialty ="+temp.doctor.getSpecialty());
           temp=temp.prev;
       }
       
   }
        public void PrintData(){
          DNode temp = head;
          int count = 0;
          while(temp!=null){
              count++;
              System.out.println(count +":    "+ temp.doctor.toString());
              temp=temp.prev;
          }
      }
    public int size(){
          DNode temp = head;
          int count= 0;
          while(temp!=null){
              count++;
              temp=temp.prev;
          }
          return count;
      }
    
    public Doctor getAtIndex(int index){
        DNode temp = head;
        for(int i=0;i<index;i++){
            temp=temp.prev;
        }
        return temp.doctor;
    }

    public void deleteById(String id) {
        if (head == null) return;

        DNode temp = head;
        while (temp != null) {
            if (temp.doctor.getId().equals(id)) {
                if (temp == head && temp == tail) { // Only one node
                    head = null;
                    tail = null;
                } else if (temp == head) { // Deleting head (last added)
                    head = temp.prev;
                    head.next = null;
                } else if (temp == tail) { // Deleting tail (first added)
                    tail = temp.next;
                    tail.prev = null;
                } else { // Deleting a middle node
                    temp.prev.next = temp.next;
                    temp.next.prev = temp.prev;
                }
                return; // Node deleted, exit
            }
            temp = temp.prev;
        }
    }
}
