import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class SecondMulThread implements Runnable{
    private int startI;
    private int endI;
    private int startJ;
    private int endJ;
    private int m;
    private int width;
    private int[][] a;
    private int[][] b;
    private int[][] product;
    private Lock[] locks;
    private Condition[] conds;
    private boolean[] available;

    public SecondMulThread(int startI, int endI, int startJ, int endJ, int m, int width, int[][] a, int[][] b, int[][] product, Lock[] locks, Condition[] conds, boolean[] available) {
        this.startI = startI;
        this.endI = endI;
        this.startJ = startJ;
        this.endJ = endJ;
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
        System.out.println("A thread from the second set has started");
        computeMatrixCells(startI,endI,startJ,endJ,m,width,a,b,product, locks, conds, available);
    }

    static void oneCell(int i, int j, int m, int[][] a, int[][] b, int[][] product)
    {
        System.out.println(i + " " + j);
        int sum = 0;
        for (int k = 0; k < m; k++)
        {
            sum += a[i][k] * b[k][j];
        }
        product[i][j] = sum;
    }

    private static void computeMatrixCells(int startI, int endI, int startJ, int endJ, int m, int width,  int[][] a,  int[][] b,  int[][] product, Lock[] locks, Condition[] conds, boolean[] available)
    {
        int i = startI, j;

        // first row
        locks[i].lock();
        try{
            while(!available[i]){
                conds[i].await();
            }
            // first row, starting frm startJ up to the end
            for (j = startJ; j < width; j++)
                oneCell(i, j, m, a, b, product);
        }
        catch (Exception e){
            System.out.println("error");

                        e.printStackTrace();
            e.printStackTrace();
        }
        finally {
            locks[i].unlock();
        }
        if (startI==endI) return;

        // the rest of the rows, except the last one
        i++;
        while (i < endI)
        {
            locks[i].lock();
            try{
                while(!available[i]) {
                    conds[i].await();
                }
                for (j = 0; j < width; j++)
                    oneCell(i, j, m, a, b, product);

            }
            catch (Exception e){
                System.out.println("error");
                            e.printStackTrace();
            }
            finally {
                locks[i].unlock();
            }
            i++;
        }

        // the last row, from the beginning up to endJ
        if(i>endI) return;
        locks[i].lock();
        try{
            while(!available[i]) {
                conds[i].await();
            }
            for (j = 0; j <= endJ; j++)
            {
                oneCell(i, j, m, a, b, product);
            }
        }
        catch (Exception e){
            System.out.println("error");

                        e.printStackTrace();
        }
        finally {
            locks[i].unlock();
        }
    }
}
