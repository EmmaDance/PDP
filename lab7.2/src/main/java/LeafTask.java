import java.util.List;
import java.util.concurrent.BlockingQueue;

public class LeafTask implements Runnable {

    private int id;
    private int first;
    private int second;
    private BlockingQueue<Integer> queue;
    private int parent;
    private boolean isLeft;

    public LeafTask(int id, int first, int second, List<BlockingQueue<Integer>> leftq, List<BlockingQueue<Integer>> rightq) {
        this.id=id;
        this.first = first;
        this.second = second;
        this.isLeft = id%2==1;
        this.parent = (id-1)/2;
        if(isLeft)
            this.queue = leftq.get(parent);
        else
            this.queue = rightq.get(parent);
//        System.out.println("Leaf "+id+" sends to parent "+parent);
    }

    @Override
    public void run() {
        int carry = 0;
        while (first!=0 && second!=0){
            int fd = first%10;
            int sd = second%10;
            first /=10;
            second /= 10;
            int add = fd+sd+carry;
            int result = add%10;
            carry = add/10;
            try {
//                System.out.println("LEAF: "+fd+"+"+sd+"+carry="+result + " carry "+carry);
                queue.put(result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        int nz;
        if(first==0)
             nz = second;
        else
             nz = first;

        while (nz!=0){
            int d = nz%10;
            d+=carry;
            nz /= 10;
            try {
                queue.put(d);
                System.out.println("one queue "+d + " carry "+carry);
                carry = 0;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            if (carry==1)
                queue.put(1);
            queue.put(-1);
//            System.out.println("Leaf, id "+this.id);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
