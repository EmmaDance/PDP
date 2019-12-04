import java.util.List;
import java.util.concurrent.ExecutorService;

public class SequentialAlgorithm extends Algorithm{

    public static Sequence computeSum(List<Integer> input){
        Sequence seq = new Sequence(input);
        Sequence res = new Sequence(input);

        int k;
        for(k=1; k<seq.size(); k = k*2){
            for(int i = 2*k-1; i<seq.size(); i+= 2*k){
                res.add(i,res.get(i-k));
            }
        }

        k=k/4;
        for(;k>0;k=k/2){
            for(int i = 3*k-1; i<seq.size(); i+= 2*k){
                res.add(i,res.get(i-k));
            }
        }

        return res;

//        List<Integer> result = new ArrayList<>();
//        result.add(input.get(0));
//
//        for (int i = 1; i < input.size(); i++) {
//            result.add(input.get(i) + result.get(i - 1));
//        }
//
//        return result;

    }
}
