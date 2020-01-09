import mpi.MPI;
import mpi.Status;
import java.util.ArrayList;
import java.util.List;

class KaratsubaMultiplication {
    KaratsubaMultiplication() { }

    void run(String[] args) {
        MPI.Init(args);
        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        if (rank == 0) {
            Polynomial P = new Polynomial();
            Polynomial Q = new Polynomial();
            P.generateValues(10);
            Q.generateValues(10);
            long start = System.currentTimeMillis();
            Polynomial result = node(rank, size, P, Q);
            long end = System.currentTimeMillis();

            System.out.println(P);
            System.out.println(Q);
            System.out.println("Karatsuba: " + (end - start) + " ms");
            System.out.println("Karatsuba: " + result);

        } else
            worker(rank);
        MPI.Finalize();
    }

    private Polynomial node(int rank, int processCount, Polynomial P, Polynomial Q) {
        Polynomial result;

        int proc1 = rank + processCount/3;
        int proc2 = proc1 + processCount/3;

        // calculeaza cate procese are fiecare proces
        int processCount0 = proc1 - rank;
        int processCount1 = proc2 - proc1;
        int processCount2 = rank + processCount - proc2;
        int length = Math.max(P.getDegree(), Q.getDegree())/2;

        Polynomial P1 = new Polynomial(new ArrayList<>(P.getCoefficients().subList(0, length)));
        Polynomial P2 = new Polynomial(new ArrayList<>(P.getCoefficients().subList(length, P.getDegree()+1)));
        Polynomial Q1 = new Polynomial(new ArrayList<>(Q.getCoefficients().subList(0, length)));
        Polynomial Q2 = new Polynomial(new ArrayList<>(Q.getCoefficients().subList(length, Q.getDegree()+1)));

        int[] meta = new int[2];
        Polynomial[] send_P = new Polynomial[1];
        Polynomial[] send_Q = new Polynomial[1];
        Polynomial[] received = new Polynomial[1];

        meta[0] = processCount1;
        send_P[0] = P1;
        send_Q[0] = Q1;
        MPI.COMM_WORLD.Send(meta, 0, 1, MPI.INT, proc1, 0);
        MPI.COMM_WORLD.Send(send_P, 0, 1, MPI.OBJECT, proc1, 0);
        MPI.COMM_WORLD.Send(send_Q, 0, 1, MPI.OBJECT, proc1, 0);

        meta[0] = processCount2;
        send_P[0] = add(P1, P2);
        send_Q[0] = add(Q1, Q2);
        MPI.COMM_WORLD.Send(meta, 0, 1, MPI.INT, proc2, 0);
        MPI.COMM_WORLD.Send(send_P, 0, 1, MPI.OBJECT, proc2, 0);
        MPI.COMM_WORLD.Send(send_Q, 0, 1, MPI.OBJECT, proc2, 0);

        if (processCount0 < 3 || P2.getDegree() < 2 || Q2.getDegree() < 2)
            result = regularSequentialMultiplication(P2, Q2);
        else
            result = node(rank, processCount0, P2, Q2);

        MPI.COMM_WORLD.Recv(received, 0, 1, MPI.OBJECT, proc1, 0);
        P = received[0];
        MPI.COMM_WORLD.Recv(received, 0, 1, MPI.OBJECT, proc2, 0);
        Q = received[0];

        Polynomial r1 = power(result, 2 * length);
        Polynomial r2 = power(subtract(subtract(Q, result), P), length);
        return add(add(r1, r2), P);
    }

    private void worker(int rank) {
        Polynomial P, Q, result;
        Status recv;
        Polynomial[] recv1 = new Polynomial[1];
        Polynomial[] recv2 = new Polynomial[1];
        Polynomial[] send = new Polynomial[1];
        int[] meta = new int[1];
        int processCount;

        // primeste numarul de procese - meta - nr de procese
        recv = MPI.COMM_WORLD.Recv(meta, 0, 1, MPI.INT, MPI.ANY_SOURCE, 0);

        int parent = recv.source;
        processCount = meta[0];
        MPI.COMM_WORLD.Recv(recv1, 0, 1, MPI.OBJECT, parent, 0);
        MPI.COMM_WORLD.Recv(recv2, 0, 1, MPI.OBJECT, parent, 0);
        P = recv1[0];
        Q = recv2[0];

        if (processCount < 3 || P.getDegree() < 2 || Q.getDegree() < 2)
            result = regularSequentialMultiplication(P, Q);
        else
            result = node(rank, processCount, P, Q);

        send[0] = result;
        MPI.COMM_WORLD.Send(send, 0, 1, MPI.OBJECT, parent, 0);
    }

    private Polynomial regularSequentialMultiplication(Polynomial P, Polynomial Q) {
        int degree = P.getDegree() + Q.getDegree();
        List<Integer> coefficients = new ArrayList<>();
        for(int i = 0; i <= degree; i++)
            coefficients.add(0);
        for(int i = 0; i <= P.getDegree(); i++)
            for(int j = 0; j <= Q.getDegree(); j++)
                coefficients.set(i + j, coefficients.get(i + j) + P.getCoefficients().get(i) * Q.getCoefficients().get(j));
        return new Polynomial(coefficients);
    }

    private Polynomial add(Polynomial p1, Polynomial p2) {
        int minDegree = Math.min(p1.getDegree(), p2.getDegree());
        int maxDegree = Math.max(p1.getDegree(), p2.getDegree());
        List<Integer> coefficients = new ArrayList<>(maxDegree + 1);
        for(int i = 0; i <= minDegree; i++)
            coefficients.add(p1.getCoefficients().get(i) + p2.getCoefficients().get(i));
        if (minDegree != maxDegree)
            if (maxDegree == p1.getDegree())
                for (int i = minDegree + 1; i <= maxDegree; i++)
                    coefficients.add(p1.getCoefficients().get(i));
            else
                for (int i = minDegree + 1; i <= maxDegree; i++)
                    coefficients.add(p2.getCoefficients().get(i));
        return new Polynomial(coefficients);
    }

    private static Polynomial power(Polynomial p, int limit) {
        List<Integer> coefficients = new ArrayList<>();
        for (int i = 0; i < limit; i++)
            coefficients.add(0);
        for (int i = 0; i <= p.getDegree(); i++)
            coefficients.add(p.getCoefficients().get(i));
        return new Polynomial(coefficients);
    }

    private Polynomial subtract(Polynomial p1, Polynomial p2) {
        int minDegree = Math.min(p1.getDegree(), p2.getDegree());
        int maxDegree = Math.max(p1.getDegree(), p2.getDegree());
        List<Integer> coefficients = new ArrayList<>(maxDegree + 1);
        for (int i = 0; i <= minDegree; i++)
            coefficients.add(p1.getCoefficients().get(i) - p2.getCoefficients().get(i));
        if (minDegree != maxDegree)
            if (maxDegree == p1.getDegree())
                for (int i = minDegree + 1; i <= maxDegree; i++)
                    coefficients.add(p1.getCoefficients().get(i));
            else
                for (int i = minDegree + 1; i <= maxDegree; i++)
                    coefficients.add(p2.getCoefficients().get(i));

        int i = coefficients.size() - 1;
        while (coefficients.get(i) == 0 && i > 0) {
            coefficients.remove(i);
            i --;
        }
        return new Polynomial(coefficients);
    }
}
