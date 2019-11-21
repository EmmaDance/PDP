import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Polynomial {

    private List<Integer> coeff;
    public Polynomial(List<Integer> coeff) {
        this.coeff = coeff;
    }
    public Polynomial(int deg) {
        coeff = new ArrayList<>(deg + 1);
        for (int i = 0; i < deg; i++) {
            coeff.add(i);
        }
    }

    public List<Integer> getCoeff() {
        return coeff;
    }

    private int getDegree() {
        return this.coeff.size()-1;
    }

    int getSize(){
        return this.coeff.size();
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int i = getDegree(); i >= 0; i--) {
            if ( coeff.get(i) == 0)
                continue;
            str.append(" ").append(coeff.get(i)).append("x^").append(i).append(" +");
        }
        str.deleteCharAt(str.length() - 1); //delete the last +
        return str.toString();
    }



}
