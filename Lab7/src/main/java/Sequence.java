import sun.awt.Mutex;
import java.util.ArrayList;
import java.util.List;

public class Sequence {
    List<Integer> content;
    List<Mutex> mutexes;

    public Sequence(List<Integer> list) {
        content = new ArrayList<>();
        mutexes = new ArrayList<>();

        for(int i: list){
            content.add(i);
            mutexes.add(new Mutex());
        }
    }

    public void add(int index, int value){
        mutexes.get(index).lock();
        content.set(index, content.get(index)+value);
        mutexes.get(index).unlock();
    }

    public int get(int index){
        return content.get(index);
    }

    public int size(){
        return content.size();
    }

    public List<Integer> toList(){
        return content;
    }

    public String toString(){
        return content.toString();
    }



}
