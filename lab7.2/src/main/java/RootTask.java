import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;

public class RootTask implements Callable<Long> {

    private BlockingQueue<Integer> leftQueue;
    private BlockingQueue<Integer> rightQueue;
    long finalResult=0;
    int pos = 1;

    public RootTask(List<BlockingQueue<Integer>> leftq, List<BlockingQueue<Integer>> rightq) {
        this.leftQueue = leftq.get(0);
        this.rightQueue = rightq.get(0);
    }

    @Override
    public Long call() throws Exception {
        // more items will be put in the queues
        boolean left = true;
        boolean right = true;
        int first, second,carry=0;
        while(true){
            try {
                first = leftQueue.take();
                second = rightQueue.take();
//                System.out.println("first " + first + "second "+second);
                if(first==-1){
                    // no more items will be put in the left queue
                    left = false;
                }

                if (second==-1) {
                    // no more items will be put in the right queue
                    right = false;
                }
                if(!right||!left)
                    break;

                int add = first+second+carry;
                int result = add%10;
                carry = add/10;
//                System.out.println("ROOT: "+first+"+"+second+"+carry="+add+" => "+result+ "+ " +carry +" carry");
                finalResult += pos*result;
                pos *= 10;

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }

        if(left)
            oneQueue(leftQueue,carry, first);
        else if (right)
            oneQueue(rightQueue,carry, second);
        else {
            if(carry==1){
                finalResult += pos;
            }
        }
        return finalResult;
    }

    private void oneQueue(BlockingQueue<Integer> queue, int carry, int x){
        while(true){
            try {
                int add = x+carry;
                int result = add%10;
                carry = add/10;
                finalResult += pos*result;
                pos *= 10;

                x = queue.take();
                if(x==-1){
                    // no more items will be put in the queue
                    return;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
