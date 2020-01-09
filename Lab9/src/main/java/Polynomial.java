import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Polynomial implements Serializable {
    private List<Integer> coefficients;

    Polynomial() {
        this.coefficients = new ArrayList<>(1);
        this.coefficients.add(0);
    }

    Polynomial(List<Integer> coefficients) {
        this.coefficients = coefficients;
    }

    Polynomial(int degree) {
        this.coefficients = new ArrayList<>();
        for(int i = 0; i <= degree; i++)
            this.coefficients.add(0);
    }

    void generateValues(int degree) {
        this.coefficients = new ArrayList<>();
        Random random = new Random();
        for(int i = 0; i < degree; i++)
            this.coefficients.add(random.nextInt(10));
        if(degree == 0)
            this.coefficients.add(random.nextInt(10));
        else
            this.coefficients.add(random.nextInt(10) + 1);
    }

    int getDegree() {
        return coefficients.size() - 1;
    }

    void setCoefficient(int pos, int val) {
        this.coefficients.set(pos, val);
    }

    List<Integer> getCoefficients() {
        return coefficients;
    }

    @Override
    public String toString() {
        String str = "";
        for(int i = 0; i < this.coefficients.size(); i++)
            if(this.coefficients.get(i) != 0) {
                if (i == this.coefficients.size() - 1)
                    str += this.coefficients.get(i) + "x^" + i + "";
                else if(i == 0)
                    str += this.coefficients.get(i) + " + ";
                else
                    str += this.coefficients.get(i) + "x^" + i + " + ";
            }
        return str;
    }
}
