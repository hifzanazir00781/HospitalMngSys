package hms;

/**
 *
 * @author pc planet
 */
class PNode{
    Patient patient;
    PNode next,prev;

    public PNode() {
        //empty contructor
    }

    public PNode(Patient patient) {
        this.patient = patient;
        this.next = null;
        this.prev = null;
    }
    
}
public class PatientList {
   PNode head,tail;

    public PatientList() {
        head=null;
        tail=null;
    }

    public Patient getAtIndex(int index) {
    PNode temp = head;
    for (int i = 0; i < index; i++) {
        if (temp != null) temp = temp.prev;
    }
    return (temp != null) ? temp.patient : null;
    }
   public void Insert(Patient patient){
       PNode node = new PNode(patient);
       
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
   
   public Patient  searchById(String id){
       PNode temp=head;
       while(temp!=null){
           if(temp.patient.getId().equals(id))
           {
            return temp.patient;
           }
           temp=temp.prev;
       }
       return null;
   }
   
      public Patient  searchByContact(String contact){
       PNode temp=head;
       while(temp!=null){
           if(temp.patient.getContact().equals(contact))
           {
            return temp.patient;
           }
           temp=temp.prev;
       }
       return null;
   }
      public int size(){
          PNode temp = head;
          int count= 0;
          while(temp!=null){
              count++;
              temp=temp.prev;
          }
          return count;
      }
      
      public void PrintData(){
          PNode temp = head;
          int count = 0;
          while(temp!=null){
              count++;
              System.out.println(count +":    "+ temp.patient.toString());
              temp=temp.prev;
          }
      }
   
      public void deleteById(String id) {
        if (head == null) return;

        PNode temp = head;
        while (temp != null) {
            if (temp.patient.getId().equals(id)) {
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
