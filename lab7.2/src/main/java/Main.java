import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        Random random = new Random();
        int n = 8;
        List<Integer> numbers = random.ints(n,100,999).boxed().collect(Collectors.toList());
        System.out.println(numbers);

        long resTest = Algorithm.addNumbers(numbers);
        System.out.println(resTest);
        long s = 0;
        for (int x : numbers)
            s+=x;
        System.out.println("sequential: "+s);

        int m = 8;
        List<Integer> largeNumbers = random.ints(m,10000000,99999999).boxed().collect(Collectors.toList());
        System.out.println(largeNumbers);

        long startTime, endTime, duration;
        startTime = System.nanoTime();
        Long res =Algorithm.addNumbers(largeNumbers);
        endTime = System.nanoTime();
        duration = (endTime - startTime) / 1000000;
        System.out.println("parallel: " + duration + " ms");
        System.out.println(res);

        startTime = System.nanoTime();
        s = 0;
        for (int x : largeNumbers)
            s+=x;
        endTime = System.nanoTime();
        duration = (endTime - startTime) / 1000000;
        System.out.println("sequential: " + duration + " ms");
        System.out.println(s);

    }
}
