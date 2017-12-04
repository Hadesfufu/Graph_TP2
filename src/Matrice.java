import java.util.*;

public class Matrice {
    public static int m_size = 3;

    public static int nb= 0;

    public static ArrayList<Matrice> allMatrices = new ArrayList<>();

    public static Deque<Matrice> queueMatrice = new ArrayDeque<>();

    public Matrice parent;

    private int[][] matrice = new int[m_size][m_size];

    private HashMap<Rotation,Matrice> adjacent = new HashMap();

    public Matrice(int[][] matrice) {
        this.matrice = matrice;
    }

    public static void parcours(){
        while(!queueMatrice.isEmpty()){
            Matrice mat = queueMatrice.pop();
            mat.createChildren();
            if(Matrice.nb > 1000)
                break;
        }
    }

    public void createChildren(){
        for(Rotation rot: Rotation.values()){
            if(!adjacent.containsKey(rot))
                createMatrice(calcul(rot), rot);
            //showMatrix(calcul(rot));
        }
    }

    public void createMatrice(int[][] matrix, Rotation rot){
        if(!exist(matrix, rot)){
            Matrice newMatrice = new Matrice(matrix);
            newMatrice.register(Rotation.valueOf(-rot.getValue()), this);
            adjacent.put(rot, newMatrice);
            allMatrices.add(newMatrice);
            queueMatrice.add(newMatrice);
            System.out.println("new Matrix created : " + nb);
            nb++;
        }
    }

    public boolean isEquals(int[][] matrix){
        for(int i = 0; i < m_size; i++){
            for(int j = 0; j < m_size; j++){
                if(matrix[i][j] != matrice[i][j])
                    return false;
            }
        }
        return true;
    }

    public void register(Rotation rot, Matrice child){
        adjacent.put(rot, child);
    }

    public boolean exist(int[][] matrix, Rotation rot){
        for(Matrice mat: allMatrices){
            if(mat.isEquals(matrix)){
                this.register(rot, mat);
                return true;
            }
        }
        return false;
    }

    private void showMatrix(int[][] matrix){
        System.out.println("Matrix :");
        for(int i = 0; i < m_size; i++){
            for(int j = 0; j < m_size; j++){
                System.out.print(matrix[i][j]);
            }
            System.out.println("");
        }
    }

    private int[][] calcul(Rotation rot){
        int[][] newMatrice = new int[m_size][m_size];
        for(int i = 0; i < m_size; i++) {
            newMatrice[i] = Arrays.copyOf(matrice[i], m_size);
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
                    newMatrice[i+offsetY][j+offsetX] = matrice[2 - j - 1+offsetY][i+offsetX];
                }
            }
        }
        else{
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    newMatrice[i+offsetY][j+offsetX] = matrice[j+offsetY][2-i-1+offsetX];
                }
            }
        }
        return newMatrice;
    }


    public int[][] getMatrice() {
        return matrice;
    }
}
