
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

public class Main {

    final int size = 3;

    public HashMap<String, ArrayList<String>> matrices = new HashMap<>();
    //public HashMap<String, ArrayList<String>> matrices = new HashMap<>();
    public Deque<int[][]> queue = new ArrayDeque();

    public int nb = 0;

    public static void main(String[] args){
        Main m = new Main();
    }

    public Main() {
        int[][] rootC = {{1,2,3},{4,5,6},{7,8,9}};
        int[][] input = {{2,8,7}, {1,9,6}, {3,4,5}};

        matrices.put(matrixToString(rootC), new ArrayList<>());
        queue.add(rootC);
        process();
        System.out.println("All nodes :" + matrices.size());
        System.out.println("1 CC : " + parcoursEnLargeurCC(matrixToString(rootC)).size());

        int res = parcoursEnLargeur(matrixToString(input), matrixToString(rootC));

        System.out.println(res);
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
                nb++;
            }
            matrices.get(ref).add(parent);
            matrices.get(parent).add(ref);
        }
    }

    public ArrayList<String> parcoursEnLargeurCC(String root){
        Deque<String> q = new ArrayDeque<>();
        ArrayList<String> composante = new ArrayList<>();
        HashMap<String, Boolean> marques = new HashMap<>();

        q.add(root);
        composante.add(root);
        marques.put(root, true);
        while(!q.isEmpty()){
            for(String cm : matrices.get(q.pop())){
                if(marques.get(cm) != null)
                    continue;
                marques.put(cm, true);
                q.push(cm);
                composante.add(cm);
            }
        }

        return composante;
    }




    public int parcoursEnLargeur(String root, String goal){
        Deque<String> q = new ArrayDeque<>();
        HashMap<String, Boolean> marques = new HashMap<>();
        HashMap<String, Integer> level = new HashMap<>();

        q.add(root);
        marques.put(root, true);
        level.put(root, 0);


        while(!q.isEmpty()){
            String parent = q.pop();
            for(String cm : matrices.get(parent)){
                if(cm.equals(goal)){
                    return level.get(parent);
                }

                if(marques.get(cm) != null)
                    continue;

                marques.put(cm, true);
                q.push(cm);
                level.put(cm, level.get(parent)+1);
            }
        }

        return 1;
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

    public int[][] stringToMatrix(String matrix){
        int[][] m = new int[size][size];
        for(int i = 0; i < matrix.length(); i++){
            m[i/3][i%3] = Character.getNumericValue(matrix.toCharArray()[i]);
        }
        return m;
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
