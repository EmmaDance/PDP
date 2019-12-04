import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Main {

    public static void main(String[] args) {

        List<Integer> input = Arrays.asList(1,5,2,4);
        System.out.println(Algorithm.computeSum(input, 3));
        Random random = new Random();

        List<List<Integer>> inputs = new ArrayList();
//        for (int i = 0; i < 100; i++) {
//            input.add(random.nextInt(1000) + 1);
//        }

        for (int i = 1; i <= 200; i++){
            inputs.add(getRandomList(i * 100, 100, 900));
        }

        Algorithm algorithm = new Algorithm();
        SequentialAlgorithm salgorithm = new SequentialAlgorithm();
        System.out.println("\nSequential algorithm");
        batchTesting(inputs, 1, algorithm);

        System.out.println("\nParallel algorithm");
        batchTesting(inputs, 1, salgorithm);

        long startTime = System.nanoTime();
        Algorithm.computeSum(input,3);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000;
        System.out.println("parallel: " + duration + " ms");

        startTime = System.nanoTime();
        SequentialAlgorithm.computeSum(input);
        endTime = System.nanoTime();
        duration = (endTime - startTime) / 1000000;
        System.out.println("sequential: " + duration + " ms");


    }

    private static List<Integer> getRandomList(int size, int min, int max) {

        List<Integer> result = new ArrayList<>(size);

        Random random = new Random();

        for (int i = 0; i < size; i++) {
            result.add(random.nextInt(max) + min);
        }

        return result;
    }

    private static void batchTesting(List<List<Integer>> inputs, int threadCount, Algorithm algorithm){
        for (int i = 0; i < inputs.size(); i++){
            test(i, inputs.get(i), threadCount, algorithm);
        }
    }

    private static void test(int size, List<Integer> input, int threadCount, Algorithm algorithm) {
        long startTime = System.nanoTime();
        Algorithm.computeSum(input, threadCount);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000;
        if (size == 0 || size == 99 || size == 199)
            System.out.println("Size " + (size+1)*100 + ": " + duration + " ms");
    }
}
