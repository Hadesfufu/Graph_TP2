import java.util.*;

public class Main {

    final int size = 3;

    public HashMap<String, ArrayList<String>> matrices = new HashMap<>();
    public HashMap<String, int[][]> matriceData = new HashMap<>();
    public Deque<int[][]> queue = new ArrayDeque();

    public int nb = 0;

    public static void main(String[] args){
        Main m = new Main();
    }

    public Main() {
        int[][] rootC = {{1,2,3},{4,5,6},{7,8,9}};
        int[][] input = {{2,8,7}, {1,9,6}, {3 ,4,5}};
        int[][] input2 = {{1,2,3}, {8,5,6}, {7 ,4,9}};

        matrices.put(matrixToString(rootC), new ArrayList<>());
        queue.add(rootC);
        matriceData.put(matrixToString(rootC), rootC);

        System.out.println("Contruction du graphe en cours...");
        process();
        System.out.println("Fini: noeuds" + matrices.size());
        System.out.println("1 CC : " + parcoursEnLargeurCC(matrixToString(rootC)).size());

        System.out.println(matrixToString(input)+ " => "+matrixToString(rootC)+" obtenu en :"+parcoursEnLargeur(matrixToString(input), matrixToString(rootC))+" rotations");
        System.out.println(matrixToString(input2)+ " => "+matrixToString(rootC)+" obtenu en :"+parcoursEnLargeur(matrixToString(input2), matrixToString(rootC))+" rotations");


        System.out.println();
        System.out.println("nombre minimal de rotation : " + diametre(matrixToString(rootC)));
        dijkstra(matrixToString(input2), matrixToString(rootC),  true);
    }

    public void process(){
        int[][] rootC = {{1,2,3},{4,5,6},{7,8,9}};

        while(!queue.isEmpty()){
            calculate(queue.pop());
        }

        System.out.println("built");
    }

    private void showMatrix(int[][] matrix){
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                System.out.print(matrix[i][j]);
            }
            System.out.println("");
        }
    }

    public int getPoids(int[][] matriceA, int[][] matriceB){
        int poids = 0;
        int test = 0;
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                test = matriceA[i][j];
                if(test !=matriceB[i][j])
                    poids += test;
            }
        }
        return poids;
    }

    public void dijkstra(String root, String goal, Boolean calculatePoid){

        HashMap<String, Integer> poids = new HashMap<>();
        HashMap<String, String> parent = new HashMap<>();
        HashMap<Integer, LinkedList<String>> allPoids = new HashMap<>();
        poids.put(root, 0);
        parent.put(root, null);

        System.out.println("Djikstra init");
        for(String s: matrices.keySet()){
            if(s.equals(root))
                continue;
            if(matrices.get(root).contains(s)) {
                Integer currentPoid = 0;
                if(calculatePoid)
                    currentPoid = getPoids(stringToMatrix(root), stringToMatrix(s));
                else
                    currentPoid = 1;
                poids.put(s, currentPoid);
                if(!allPoids.containsKey(currentPoid))
                    allPoids.put(currentPoid, new LinkedList<>());
                allPoids.get(currentPoid).offer(s);
                parent.put(s, root);
            }
        }


        System.out.println("Djikstra process");

        Integer currentPoid = 0;

        int nb = 0;
        while(nb <matrices.keySet().size()){
            //recup x

            while(!allPoids.containsKey(currentPoid) || allPoids.get(currentPoid).isEmpty()) {
                /*if(allPoids.containsKey(currentPoid))
                    allPoids.remove(currentPoid);*/
                currentPoid++;
            }

            String x = allPoids.get(currentPoid).poll();

            if(x == null)
                System.out.println("Hey x is null");

            for (String s: matrices.get(x)){
                Integer newPoid = 0;
                if(calculatePoid)
                    newPoid = getPoids(stringToMatrix(x), stringToMatrix(s)) + poids.get(x);
                else
                    newPoid = 1 + poids.get(x);

                if(!poids.containsKey(s) || newPoid < poids.get(s)) {
                    poids.put(s,newPoid);
                    if(!allPoids.containsKey(newPoid))
                        allPoids.put(newPoid, new LinkedList<>());
                    allPoids.get(newPoid).offer(s);
                    parent.put(s, x);
                }
            }
            nb++;
        }

        String route = goal;
        System.out.println("All steps in reverse :");
        int etape = parent.size();
        while(route != null){
            System.out.print("Etape: "+etape);

            showMatrix(stringToMatrix(route));
            System.out.println();
            etape--;
            route = parent.get(route);
        }
        System.out.println("Poids : " + poids.get(goal));
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


    public int diametre(String root){
        Deque<String> q = new ArrayDeque<>();
        HashMap<String, Boolean> marques = new HashMap<>();
        HashMap<String, Integer> level = new HashMap<>();
        ArrayList<String> composante = new ArrayList<>();
        int lastLevel = 0;

        q.add(root);
        marques.put(root, true);
        level.put(root, 0);
        composante.add(root);
        while(!q.isEmpty()){
            String parent = q.poll();
            for(String cm : matrices.get(parent)){
                if(marques.get(cm) != null)
                    continue;
                marques.put(cm, true);
                q.offer(cm);
                //System.out.println(level.get(parent));
                level.put(cm, new Integer(level.get(parent).intValue()+1));
                composante.add(cm);
            }
            if(q.isEmpty())
                lastLevel = level.get(parent);
        }

        return lastLevel;
    }

    public int parcoursEnLargeur(String root, String goal){
        Deque<String> q = new ArrayDeque<>();

        //L'utilisation des 2 listes peut sembler redondant mais permet de facilement chercher soit par le level soit par le noeud
        HashMap<String, Integer> level = new HashMap<>(); // Contient le level pour un noeud donné
        HashMap<Integer, ArrayList<String>> level2 = new HashMap<>(); //contient tout les noeuds pour 1 level

        int lvlMatch=1 ;
        String matchedMatrix = "";

        q.add(root);
        level.put(root, 0);

        ArrayList tmp = new ArrayList<String>();
        tmp.add(root);
        level2.put(0,tmp);

        while(!q.isEmpty()){
            String parent = q.poll();
            for(String cm : matrices.get(parent)){
                if(cm.equals(goal)){
                    lvlMatch=  level.get(parent);
                    matchedMatrix = parent;
                }

                if(level.get(cm) != null)
                    continue;

                q.offer(cm);
                level.put(cm, level.get(parent)+1);

                if(level2.get( level.get(parent)+1) != null){
                    level2.get( level.get(parent)+1).add(cm);
                }else {
                    ArrayList tmpList = new ArrayList<String>();
                    tmpList.add(cm);
                    level2.put(level.get(parent)+1, tmpList);
                }
            }
        }

        int currentLvl = lvlMatch;
        String currentMatrix = matchedMatrix;
        HashMap<String, Rotation> enchainement = new HashMap<>();

        ArrayList<String> parents = new ArrayList<>();

        //Récupère le chemin suivant les parents
        while(currentLvl > 0) {
            for (String m : level2.get(currentLvl-1)) {
                ArrayList curr = matrices.get(m);
                if (curr.contains(currentMatrix)) {
                    parents.add(m);
                    currentLvl = level.get(m);
                    currentMatrix = m;
                    break;
                }
            }
        }

        String[]arrayParents = new String[parents.size()];

        parents.toArray(arrayParents);

        int etape=1;
        //Détermine la rotation pour passer d'une matrice à l'autre
        for(int i= arrayParents.length-1; i>0; i--){
            currentMatrix = arrayParents[i];
            String previousMatrix = arrayParents[i-1];

            for(Rotation rot: Rotation.values()) {
                int[][] newMatrix = calcul(rot, stringToMatrix(currentMatrix));
                String ref = matrixToString(newMatrix);

                if(ref.equals(previousMatrix)){
                    System.out.println("Etape "+etape+" : rotation "+rot+"\n");
                    showMatrix(stringToMatrix(currentMatrix));
                    System.out.println();
                    etape++;
                    break;
                }
            }
        }

        return lvlMatch-1;
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
