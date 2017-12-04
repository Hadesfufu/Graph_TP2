import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Main {


    public static void main(String[] args){
        int[][] rootC = {{1,2,3},{4,5,6},{7,8,9}};
        Matrice.queueMatrice.add(new Matrice(rootC));
        Matrice.parcours();

        try {
            FileOutputStream fos = new FileOutputStream("test.dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(Matrice.allMatrices);
        } catch (Exception e) {

        }
    }
}
