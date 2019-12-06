import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Algorithm
{
    public static Long addNumbers(List<Integer> numbers) {
        int n = numbers.size();
        List<BlockingQueue<Integer>> leftqueues = new ArrayList<>();
        List<BlockingQueue<Integer>> rightqueues = new ArrayList<>();
        for (int i =0; i< n/2; i++){
            leftqueues.add(new ArrayBlockingQueue<>(n));
            rightqueues.add(new ArrayBlockingQueue<>(n));
        }

        ExecutorService pool = Executors.newFixedThreadPool(n-1);

        int k = 0;
        Future<Long> result = pool.submit(new RootTask(leftqueues, rightqueues));
        for (int i = 1; i < n/2-1; i++){
            pool.execute(new NodeTask(i, leftqueues, rightqueues));
        }
        for (int i = n/2-1; i < n-1; i++){
            pool.execute(new LeafTask(i,numbers.get(k), numbers.get(k+1), leftqueues, rightqueues));
            k+=2;
        }
        pool.shutdown();
        try {
            pool.awaitTermination(15, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try { return result.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

}
