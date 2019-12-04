import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Algorithm {

    public static Sequence computeSum(List<Integer> input, int noOfThreads){
        Sequence seq = new Sequence(input);
        ExecutorService threadpool;

        int k;
        for(k=1; k<seq.size(); k = k*2){
            threadpool= Executors.newFixedThreadPool(noOfThreads);
            for(int i = 2*k-1; i<seq.size(); i+= 2*k){
                threadpool.execute(new Worker(seq,i,k));
            }

            threadpool.shutdown();

            try {
                threadpool.awaitTermination(100, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }


        k=k/4;
        for(;k>0;k=k/2){
            threadpool= Executors.newFixedThreadPool(noOfThreads);
            for(int i = 3*k-1; i<seq.size(); i+= 2*k){
                threadpool.execute(new Worker(seq,i,k));
            }
            threadpool.shutdown();

            try {
                threadpool.awaitTermination(100, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return seq;

    }
}
