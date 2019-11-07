
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Scanner;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class main {
    static Scanner input = new Scanner(System.in);

    static void initMatrix( int[][] arr, int height, int width)
    {
        for (int i = 0; i < height; i++)
        {
            for (int j = 0; j < width; j++)
            {
                arr[i][j] = i + j;

            }
        }
    }

    static void displayMatrix( int[][] arr, int height, int width)
    {
        for (int i = 0; i < height; i++)
        {
            for (int j = 0; j < width; j++)
            {
                System.out.print(arr[i][j] + "  ");

            }
            System.out.println();
        }
    }

    private static void initLocksConditions(int height, Lock[] locks, Condition[] conds) {
        for (int i = 0; i < height; i++){
            locks[i] = new ReentrantLock();
            conds[i] = locks[i].newCondition();
        }
    }


    public static void main(String[] args) {


        int height1, width1, height2, width2, height3, width3,height4, width4, height5, width5, m;

        System.out.println("First matrix");
        System.out.println("Number of rows (between 1 and 100): ");
        height1 = input.nextInt();
        System.out.println("Number of columns (between 1 and 100): ");
        width1 = input.nextInt();

        height2 = width1;
        m = width1;

        System.out.println("Second matrix");
        System.out.println("Number of rows will be " + height2);
        System.out.println("Number of columns (between 1 and 100): ");
        width2 = input.nextInt();

        height3 = height1;
        width3 = width2;

        height4 = width3;
        System.out.println("Third matrix");
        System.out.println("Number of rows will be " + height4);
        System.out.println("Number of columns (between 1 and 100): ");
        width4 = input.nextInt();

        width5 = width4;
        height5 = height1;
        int m2 = height4;

        int[][] a = new int[height1][width1];
        int[][] b = new int[height2][width2];
        int[][] product1 = new int[height3][width3];
        int[][] c = new int[height4][width4];
        int[][] product2 = new int[height5][width5];

        initMatrix( a, height1, width1);
        initMatrix( b, height2, width2);
        initMatrix( c, height4, width4);

        final Lock[] locks = new Lock[height1];
        final Condition[] conds = new Condition[height1];
        boolean[] available = new boolean[height1];

        initLocksConditions(height1, locks, conds);

        int n1, n2;
        System.out.println("Enter the number of threads in the first set");
        n1 = input.nextInt();
        if (n1>height3*width3) n1 = height3*width3;
        System.out.println("Enter the number of threads in the second set");
        n2 = input.nextInt();
        if (n2>height5*width5) n2 = height5*width5;

        Thread[] threads1 = new Thread[n1];
        Thread[] threads2 = new Thread[n2];

        int startI = 0;
        int endI = -1;

        /////////////////////////////////////////////////////////////

        for (int i = 0; i < n1 - 1; i++)
        {
            endI += height3/n1;
            Runnable worker1 = new FirstMulThread(startI,endI, m,width3,a,b,product1, locks, conds, available);
//            worker1.run();
            threads1[i] = new Thread(worker1);
            // update startI and startJ for the next thread
            startI = endI+1;

        }

        endI = height3 - 1;

        // last thread
        Runnable worker1 = new FirstMulThread(startI,endI,m,width3,a,b,product1, locks, conds, available);
        threads1[n1-1] = new Thread(worker1);

        ///////////////////////////////////////////////////////////

        startI = 0;
        endI = 0;
        int startJ = 0;
        int endJ = -1;

        for (int i = 0; i < n2 - 1; i++)
        {
            endJ += height5 * width5 / n2;
            while (endJ >= width5)
            {
                endI++;
                endJ -= width5;
            }
//            System.out.println(startI + " " + endI + " " + startJ + " " + endJ);
            Runnable worker = new SecondMulThread(startI,endI,startJ,endJ,m2,width5,product1,c,product2, locks, conds, available);
            threads2[i] = new Thread(worker);

            // update startI and startJ for the next thread
            startI = endI;
            startJ = endJ + 1;
            if (startJ >= width3)
            {
                startI++;
                startJ -= width3;
            }
        }
        endI = height5 - 1;
        endJ = width5 - 1;

        // last thread
//        System.out.println(startI + " " + endI + " " + startJ + " " + endJ);
        Runnable worker = new SecondMulThread(startI,endI,startJ,endJ,m2,width5,product1,c,product2, locks, conds, available);
//        worker.run();
        threads2[n2-1] = new Thread(worker);

        ////////////////////////////////////////////////////////////

        for (int i = 0; i < n1; i++)
            threads1[i].start();
//            threads1[i].run();
        for (int i = 0; i < n2; i++)
            threads2[i].start();
//            threads2[i].run();


        try {
            for (int i = 0; i < n1; i++)
                threads1[i].join();
            for (int i = 0; i < n2; i++)
                threads2[i].join();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        System.out.println();
        System.out.println("First product");
        displayMatrix( product1, height3, width3);

//        displayMatrix(c, height4, width4);

        System.out.println();
        System.out.println("Final result");
        displayMatrix(product2, height5, width5);


    }


}
