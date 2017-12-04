
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

public class Main {

    final int size = 3;

    public HashMap<String, ArrayList<String>> matrices = new HashMap<>();
    public Deque<int[][]> queue = new ArrayDeque();

    public int nb = 0;

    public static void main(String[] args){
        Main m = new Main();

    }

    public Main() {
        int[][] rootC = {{1,2,3},{4,5,6},{7,8,9}};
        matrices.put(matrixToString(rootC), new ArrayList<>());
        queue.add(rootC);
        process();
    }

    public void process(){
        while(!queue.isEmpty()){
            calculate(queue.pop());
        }
    }

    private void showMatrix(int[][] matrix){
        System.out.println("Matrix :");
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                System.out.print(matrix[i][j]);
            }
            System.out.println("");
        }
    }

    public void calculate(int[][] matrix){
        String parent = matrixToString(matrix);
        int[][] newMatrix;
        String ref;
        for(Rotation rot: Rotation.values()){
            newMatrix = calcul(rot, matrix);
            ref = matrixToString(newMatrix);
            if(matrices.get(ref) == null){
                matrices.put(ref, new ArrayList<>());
                queue.add(newMatrix);
                System.out.println("new Matrix! " + nb);
                nb++;
            }
            matrices.get(ref).add(parent);
            matrices.get(parent).add(ref);
        }
    }









    public String matrixToString(int[][] matrix){
        String s = "";
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                s+=matrix[i][j];
            }
        }
        return s;
    }

    private int[][] calcul(Rotation rot, int[][] input){
        int[][] newMatrice = new int[size][size];
        for(int i = 0; i < size; i++) {
            newMatrice[i] = Arrays.copyOf(input[i], size);
        }

        int offsetX = 0;
        int offsetY = 0;
        boolean horaire = false;
        switch (rot) {
            case Horaire_HGauche:
                offsetX = 0;
                offsetY = 0;
                horaire=true;
                break;
            case Horaire_HDroite:
                offsetX = 1;
                offsetY = 0;
                horaire=true;
                break;
            case Horaire_BGauche:
                offsetX = 0;
                offsetY = 1;
                horaire=true;
                break;
            case Horaire_BDroite:
                offsetX = 1;
                offsetY = 1;
                horaire=true;
                break;
            case AntiHoraire_HGauche:
                offsetX = 0;
                offsetY = 0;
                horaire=false;
                break;
            case AntiHoraire_HDroite:
                offsetX = 1;
                offsetY = 0;
                horaire=false;
                break;
            case AntiHoraire_BGauche:
                offsetX = 0;
                offsetY = 1;
                horaire=false;
                break;
            case AntiHoraire_BDroite:
                offsetX = 1;
                offsetY = 1;
                horaire=false;
                break;
        }

        if(horaire) {
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    newMatrice[i+offsetY][j+offsetX] = input[2 - j - 1+offsetY][i+offsetX];
                }
            }
        }
        else{
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    newMatrice[i+offsetY][j+offsetX] = input[j+offsetY][2-i-1+offsetX];
                }
            }
        }
        return newMatrice;
    }
}
