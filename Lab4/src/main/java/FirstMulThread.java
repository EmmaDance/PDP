import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

class FirstMulThread implements Runnable {
    private int startI;
    private int endI;
    private int m;
    private int width;
    private int[][] a;
    private int[][] b;
    private int[][] product;
    private Lock[] locks;
    private Condition[] conds;
    private boolean[] available;


    public FirstMulThread(int startI, int endI, int m, int width, int[][] a, int[][] b, int[][] product, Lock[] locks, Condition[] conds, boolean[] available) {
        this.startI = startI;
        this.endI = endI;
        this.m = m;
        this.width = width;
        this.a = a;
        this.b = b;
        this.product = product;
        this.locks = locks;
        this.conds = conds;
        this.available = available;
    }

    public void run() {
        System.out.println("A thread from the first set has started");
        computeMatrixCells(startI,endI,m,width,a,b,product, locks, conds, available);
    }

    static void oneCell(int i, int j, int m, int[][] a, int[][] b, int[][] product)
    {
        int sum = 0;
        for (int k = 0; k < m; k++)
        {
            sum += a[i][k] * b[k][j];
        }
        product[i][j] = sum;
    }

    private static void computeMatrixCells(int startI, int endI, int m, int width,  int[][] a,  int[][] b,  int[][] product, Lock[] locks, Condition[] conds, boolean[] available)
    {
        int i = startI,j;
        while (i <= endI)
        {
            for (j = 0; j < width; j++)
                oneCell(i, j, m, a, b, product);
            locks[i].lock();
            available[i] = true;
            conds[i].signalAll();
            locks[i].unlock();
            i++;
        }
    }
}


