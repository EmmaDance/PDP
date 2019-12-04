import java.util.List;

public class Worker implements Runnable {
//    res.set(i,res.get(i-k)+res.get(i));
    Sequence seq;
    Integer index;
    Integer k;

    public Worker(Sequence seq, Integer index, Integer k) {
        this.seq = seq;
        this.index = index;
        this.k = k;
    }

    public void run(){
        seq.add(index, seq.get(index-k));
    }

}
