import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;


public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        Polynomial p = new Polynomial(new ArrayList<>(Arrays.asList(1, 3, 2, 4)));
//        Polynomial q = new Polynomial(new ArrayList<>(Arrays.asList(6, 3, 7)));
        Polynomial p = new Polynomial(1000);
        Polynomial q = new Polynomial(1000);

//        System.out.println("pol p:" + p);
//        System.out.println("pol q" + q);
        System.out.println("\n");

        long startTime = System.currentTimeMillis();
        Polynomial result = Operations.multiplicationSequential(p, q);
        long endTime = System.currentTimeMillis();
        System.out.println("Simple sequential multiplication of polynomials: ");
        System.out.println("Execution time : " + (endTime - startTime) + " ms");
        System.out.println(result);

        long startTime2 = System.currentTimeMillis();
        Polynomial result2 = Operations.multiplicationKaratsubaSequential(p, q);
        long endTime2 = System.currentTimeMillis();
        System.out.println("Karatsuba sequential multiplication of polynomials: ");
        System.out.println("Execution time : " + (endTime2 - startTime2) + " ms");
        System.out.println(result2);

        long startTime3 = System.currentTimeMillis();
        Polynomial result3 = Operations.multiplicationParallelized(p, q,1);
        long endTime3 = System.currentTimeMillis();
        System.out.println("Simple parallelized multiplication of polynomials: ");
        System.out.println("Execution time : " + (endTime3 - startTime3) + " ms");
        System.out.println(result3);

        long startTime4 = System.currentTimeMillis();
        Polynomial result4 = Operations.multiplicationKaratsubaParallelized(p, q,1);
        long endTime4 = System.currentTimeMillis();
        System.out.println("Karatsuba parallelized multiplication of polynomials: ");
        System.out.println("Execution time : " + (endTime4 - startTime4) + " ms");
        System.out.println(result4);
    }
}
