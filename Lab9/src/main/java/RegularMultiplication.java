import java.util.ArrayList;
import mpi.*;

class RegularMultiplication {
    RegularMultiplication() { }

    void run(String[] args) {
        MPI.Init(args);
        int rank = MPI.COMM_WORLD.Rank();
        if (rank == 0)
            node();
        else
            worker();
        MPI.Finalize();
    }

    private void node() {
        Polynomial P = new Polynomial();
        Polynomial Q = new Polynomial();
        P.generateValues(10);
        Q.generateValues(10);
        System.out.println(P);
        System.out.println(Q);
        long start = System.currentTimeMillis();

        int processCount = MPI.COMM_WORLD.Size();
        int size = (P.getDegree() + Q.getDegree() + 1)/(processCount - 1);

        int i = 0;
        for (int j = 1; j < processCount; j++) {
            Polynomial[] send_P = new Polynomial[1]; // accepta numai sub forma de vector
            Polynomial[] send_Q = new Polynomial[1]; // creeaza un vector cu un polinom
            int[] send_i = new int[1]; // inceput
            int[] send_end = new int[1]; // final
            send_P[0] = P;
            send_Q[0] = Q;
            send_i[0] = i;

            // seteaza finalul, daca cumva nu s-a impartit exact la nr de procese
            if (i + size >= P.getDegree() + Q.getDegree() - 1)
                send_end[0] = P.getDegree() + Q.getDegree() + 1;
            else {
                send_end[0] = i + size;
            }

            // j - nr procesului
            MPI.COMM_WORLD.Send(send_P, 0, 1, MPI.OBJECT, j, 0);
            MPI.COMM_WORLD.Send(send_Q, 0, 1, MPI.OBJECT, j, 0);

            MPI.COMM_WORLD.Send(send_i, 0, 1, MPI.INT, j, 0);
            MPI.COMM_WORLD.Send(send_end, 0, 1, MPI.INT, j, 0);
            i = i + size;
        }

        // toate rezultatele pe care le primeste
        ArrayList<Polynomial> results = new ArrayList<>();
        for (i = 1; i < processCount; i++) {
            Polynomial[] received = new Polynomial[1];
            MPI.COMM_WORLD.Recv(received, 0, 1, MPI.OBJECT, i, 0);
            results.add(received[0]);
        }

        // parcurge si le aduna
        Polynomial result = new Polynomial(results.get(0).getDegree());
        for (i = 0; i <= result.getDegree(); i++)
            for (Polynomial result1 : results)
                result.setCoefficient(i, result.getCoefficients().get(i) + result1.getCoefficients().get(i));
        long end = System.currentTimeMillis();
        System.out.println("Regular: " + (end - start) + " ms");
        System.out.println("Regular: " + result + "\n\n");
        System.out.println("");
    }

    private void worker() {
        Polynomial P;
        Polynomial Q;
        Polynomial[] received_P = new Polynomial[1];
        Polynomial[] received_Q = new Polynomial[1];
        Polynomial[] send = new Polynomial[1];
        int[] received_i = new int[1];
        int[] received_end = new int[1];
        int startPos, endPos;

        // receive la inceput final
        MPI.COMM_WORLD.Recv(received_P, 0, 1, MPI.OBJECT, 0, 0);
        MPI.COMM_WORLD.Recv(received_Q, 0, 1, MPI.OBJECT, 0, 0);
        MPI.COMM_WORLD.Recv(received_i, 0, 1, MPI.INT, 0, 0);
        MPI.COMM_WORLD.Recv(received_end, 0, 1, MPI.INT, 0, 0);

        P = received_P[0];
        Q = received_Q[0];
        startPos = received_i[0];
        endPos = received_end[0];

        Polynomial result = new Polynomial(P.getDegree() + Q.getDegree());
        for (int i = startPos; i < endPos; i++) {
            for (int j = 0; j <= i; j++) {
                if (j <= P.getDegree() && (i - j) <= Q.getDegree()) {
                    int value = P.getCoefficients().get(j) * Q.getCoefficients().get(i - j);
                    result.setCoefficient(i, result.getCoefficients().get(i) + value);
                }
            }
        }
        send[0] = result;

        // trimite inapoi rezultatul = un polinom care are o bucata din rezultat
        MPI.COMM_WORLD.Send(send, 0, 1, MPI.OBJECT, 0, 0);
    }
}
