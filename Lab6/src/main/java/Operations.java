import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class Operations {

    public static Polynomial multiplication(Polynomial p1, Polynomial q) {

        // initialise array of coefficients of the resulting polynomial
        List<Integer> coeff = new ArrayList<>();
        int nr = p1.getSize() + q.getSize();
        for (int i = 0; i < nr; i++){
            coeff.add(0);
        }

        // multiply all the terms
        for (int i = 0; i < p1.getSize(); i++){
            for (int j = 0; j< q.getSize(); j++){
                int power = i+j;
                int c = p1.getCoeff().get(i)*q.getCoeff().get(j);
                coeff.set(power, coeff.get(power) + c);
            }
        }

        return new Polynomial(coeff);
    }

    public static Polynomial multiplicationSequential(Polynomial p, Polynomial q) {

        if (p.getSize()<=1||q.getSize()<=1)
            return multiplication(p,q);

        int half = Math.max(p.getSize(), q.getSize())/2;
        Polynomial p0 = new Polynomial(p.getCoeff().subList(0, half));
        Polynomial p1 = new Polynomial(p.getCoeff().subList(half, p.getSize()));
        Polynomial q0 = new Polynomial(q.getCoeff().subList(0, half));
        Polynomial q1 = new Polynomial(q.getCoeff().subList(half, q.getSize()));

        Polynomial r0 = multiplicationSequential(p0, q0);
        Polynomial r1 = multiplicationSequential(p1, q1);
        Polynomial r01 = multiplicationSequential(p0,q1);
        Polynomial r10 = multiplicationSequential(p1,q0);

        return add(r0, add(shift(r1,half*2), shift(add(r01, r10),half)));
    }



    public static Polynomial multiplicationParallelized(Polynomial p, Polynomial q, int level) throws InterruptedException, ExecutionException {

        if (level > 4)
            return multiplicationSequential(p, q);

        if (p.getSize()<=1||q.getSize()<=1)
            return multiplication(p,q);

        int half = Math.max(p.getSize(), q.getSize())/2;
        Polynomial p0 = new Polynomial(p.getCoeff().subList(0, half));
        Polynomial p1 = new Polynomial(p.getCoeff().subList(half, p.getSize()));
        Polynomial q0 = new Polynomial(q.getCoeff().subList(0, half));
        Polynomial q1 = new Polynomial(q.getCoeff().subList(half, q.getSize()));

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        Callable<Polynomial> task1 = () -> multiplicationParallelized(p0, q0, level+1 );
        Callable<Polynomial> task2 = () -> multiplicationParallelized(p1, q1, level+1 );
        Callable<Polynomial> task3 = () -> multiplicationParallelized(p0, q1, level+1 );
        Callable<Polynomial> task4 = () -> multiplicationParallelized(p1, q0, level+1 );

        Future<Polynomial> f1 = executor.submit(task1);
        Future<Polynomial> f2 = executor.submit(task2);
        Future<Polynomial> f3 = executor.submit(task3);
        Future<Polynomial> f4 = executor.submit(task4);

        executor.shutdown();

        Polynomial r0 = f1.get();
        Polynomial r1 = f2.get();
        Polynomial r01 = f3.get();
        Polynomial r10 = f4.get();

        executor.awaitTermination(60, TimeUnit.SECONDS);

        return add(r0, add(shift(r1,half*2), shift(add(r01, r10),half)));
    }



    public static Polynomial multiplicationKaratsubaSequential (Polynomial p, Polynomial q){
        if (p.getSize()<=1||q.getSize()<=1)
            return multiplication(p,q);

        int half = Math.max(p.getSize(), q.getSize())/2;
        Polynomial p0 = new Polynomial(p.getCoeff().subList(0, half));
        Polynomial p1 = new Polynomial(p.getCoeff().subList(half, p.getSize()));
        Polynomial q0 = new Polynomial(q.getCoeff().subList(0, half));
        Polynomial q1 = new Polynomial(q.getCoeff().subList(half, q.getSize()));

        Polynomial r0 = multiplicationKaratsubaSequential(p0, q0);
        Polynomial r1 = multiplicationKaratsubaSequential(p1, q1);
        Polynomial r01 = multiplicationKaratsubaSequential(add(p0, p1), add(q0,q1));
        Polynomial r0r1 = add(r0, r1);

        return add(r0, add(shift(subtract(r01, r0r1), half),shift(r1, half*2)));
    }



    public static Polynomial multiplicationKaratsubaParallelized (Polynomial p, Polynomial q, int level) throws InterruptedException, ExecutionException {
        if (level > 4)
            return multiplicationKaratsubaSequential(p, q);

        if (p.getSize()<=1||q.getSize()<=1)
            return multiplication(p,q);

        int half = Math.max(p.getSize(), q.getSize())/2;
        Polynomial p0 = new Polynomial(p.getCoeff().subList(0, half));
        Polynomial p1 = new Polynomial(p.getCoeff().subList(half, p.getSize()));
        Polynomial q0 = new Polynomial(q.getCoeff().subList(0, half));
        Polynomial q1 = new Polynomial(q.getCoeff().subList(half, q.getSize()));

        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        Callable<Polynomial> task1 = () -> multiplicationKaratsubaParallelized(p0, q0, level+1 );
        Callable<Polynomial> task2 = () -> multiplicationKaratsubaParallelized(p1, q1, level+1 );
        Callable<Polynomial> task3 = () -> multiplicationKaratsubaParallelized(add(p0, p1), add(q0,q1), level+1 );

        Future<Polynomial> f1 = executor.submit(task1);
        Future<Polynomial> f2 = executor.submit(task2);
        Future<Polynomial> f3 = executor.submit(task3);

        executor.shutdown();

        Polynomial r0 = f1.get();
        Polynomial r1 = f2.get();
        Polynomial r01 = f3.get();
        Polynomial r0r1 = add(r0, r1);

        executor.awaitTermination(60, TimeUnit.SECONDS);

        return add(r0, add(shift(subtract(r01, r0r1), half),shift(r1, half*2)));
    }





    // shifts the coefficients of the polynomial by n positions to the left
    private static Polynomial shift(Polynomial p, int n) {
        List<Integer> coeff = new ArrayList<>();
        for (int i = 0; i < n; i++)
            coeff.add(0);
        for (int i = 0; i < p.getSize(); i++){
            coeff.add(p.getCoeff().get(i));
        }
        return new Polynomial(coeff);
    }

    private static Polynomial add(Polynomial p, Polynomial q) {

        int minD = Math.min(p.getSize(), q.getSize());
        List<Integer> r = new ArrayList<>();
        for(int i = 0; i < minD; i++){
            r.add(p.getCoeff().get(i)+q.getCoeff().get(i));
        }
        if (p.getSize()>q.getSize()){
            for (int i = minD; i < p.getSize(); i++)
                r.add(p.getCoeff().get(i));
        }
        else {
            for (int i = minD; i < q.getSize(); i++)
                r.add(q.getCoeff().get(i));
        }

        return new Polynomial(r);
    }

    private static Polynomial subtract(Polynomial p, Polynomial q){
        int minD = Math.min(p.getSize(), q.getSize());
        List<Integer> r = new ArrayList<>();
        for(int i = 0; i < minD; i++){
            r.add(p.getCoeff().get(i)-q.getCoeff().get(i));
        }
        if (p.getSize()>q.getSize()){
            for (int i = minD; i < p.getSize(); i++)
                r.add(p.getCoeff().get(i));
        }
        else {
            for (int i = minD; i < q.getSize(); i++)
                r.add(-q.getCoeff().get(i));
        }

        return new Polynomial(r);
    }


}
