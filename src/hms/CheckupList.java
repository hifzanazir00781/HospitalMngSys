package hms;

/**
 *
 * @author pc planet
 */
class CNode{
    Checkup cu;
    CNode next,prev;

    public CNode() {
        //empty contructor
    }

    public CNode(Checkup cu) {
        this.cu = cu;
        this.next = null;
        this.prev = null;
    }
}
public class CheckupList {
    CNode head,tail;

    public CheckupList() {
        head=null;
        tail=null;
    }
    
    

    
    public void Enqueue(Checkup cu){
        CNode node = new CNode(cu);
         if(head==null || tail==null)//list empty
       {
           head = node;
           tail = node;
       }
        else if(head.cu.getPriority()<cu.getPriority()) // if we wanna create list
        {
           head.next=node;
           node.prev=head;
           head=node;
       }
        
         else if(tail.cu.getPriority()>=cu.getPriority()) // if we wanna create list
        {
           tail.next=node;
           node.prev=tail;
           tail=node;
       }
         else{
             CNode temp = tail;
             while(temp!=null){
                 if(temp.cu.getPriority()>=cu.getPriority()){
                     break;
                 }
                 temp=temp.next;
             }
             
             node.next=temp;
             node.prev=temp.prev;
             temp.prev.next=node;
             temp.prev=node;
         }
   }
    
    public Checkup dequeue(){
        if(head==null){
            return null;
        }
        CNode checkup = head;
        head = head.next;
        return checkup.cu;
    }
    
    public void print(){
        CNode temp=head;
        while(temp!=null){
            System.out.println(temp.cu.getPriority()+ "     " + temp.cu.getRecomendation());
            temp = temp.prev;
        }
    }
    public void addRecomendation(int index, String rec){
        CNode temp= head;
        int i=0;
        while(temp!=null){
            if(index==i){
                temp.cu.setRecomendation(rec);
                break;
            }
            i++;
            temp=temp.prev;
        }
    
    }
    
    public Patient getPatient(int index){
        CNode temp = head;
        int i=0;
        while(temp!=null){
            if(index==i){
                break;
            }
             i++;
             temp=temp.prev;
        }
       return temp.cu.getPatient();
    }
    
    public void Print(){
        CNode temp = head;
          
          while(temp!=null){
             
              System.out.println(temp.cu.getPriority()+"   "+ temp.cu.getRecomendation());
              temp=temp.prev;
          }
    }
     public int size(){
          CNode temp = head;
          int count= 0;
          while(temp!=null){
              count++;
              temp=temp.prev;
          }
          return count;
      }
 }

