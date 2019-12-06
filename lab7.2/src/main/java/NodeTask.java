import java.util.List;
import java.util.concurrent.BlockingQueue;

public class NodeTask implements Runnable {

    private int id;
    private BlockingQueue<Integer> leftQueue;
    private BlockingQueue<Integer> rightQueue;
    private BlockingQueue<Integer> parentQueue;
    private int parent;
    private boolean isLeft;

    public NodeTask(int id, List<BlockingQueue<Integer>> leftq, List<BlockingQueue<Integer>> rightq) {
        this.id = id;
        this.isLeft = id%2==1;
        this.parent = (id-1)/2;
        this.leftQueue = leftq.get(id);
        this.rightQueue = rightq.get(id);
        if(isLeft)
            this.parentQueue = leftq.get(parent);
        else
            this.parentQueue = rightq.get(parent);
//        System.out.println("Node "+id+" sends to parent "+parent);

    }

    @Override
    public void run() {
        // more items will be put in the queues
        boolean left = true;
        boolean right = true;
        int first=0, second=0,carry=0;
        while(left && right){
            try {
                first = leftQueue.take();
                second = rightQueue.take();

                if (first == -1){
                    // no more items will be put in the left queue
                    left = false;

                }

                if (second == -1){
                    // no more items will be put in the right queue
                    right = false;

                }

                if(!left || !right)
                    break;

                int add = first + second + carry;
                int result = add % 10;
                carry = add / 10;
//                System.out.println("NODE: "+first+"+"+second+"+carry="+add+" => "+result+ "+ " +carry +" carry");
                try {
                    parentQueue.put(result);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }



            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(left)
            oneQueue(leftQueue,carry, first);
        else if (right)
            oneQueue(rightQueue,carry, second);
        else {
            try {
                if (carry==1)
                    parentQueue.put(1);
                parentQueue.put(-1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
//        System.out.println("Node, id "+id);

    }

    private void oneQueue(BlockingQueue<Integer> queue, int carry, int x){
        while(true){
            try {

                if(x==-1){
                    if (carry==1)
                        queue.put(1);
                    // no more items will be put in the left queue
                    parentQueue.put(-1);
                    return;
                }

                int add = x+carry;
                int result = add%10;
                carry = add/10;

                try {
                    parentQueue.put(result);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                x = queue.take();


            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


}
