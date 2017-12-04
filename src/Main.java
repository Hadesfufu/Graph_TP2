import jdk.nashorn.internal.parser.JSONParser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

public class Main {


    public static void main(String[] args){
        int[][] rootC = {{1,2,3},{4,5,6},{7,8,9}};
        Matrice.queueMatrice.add(new Matrice(rootC));
        Matrice.parcours();

        try {
            PrintWriter writer = new PrintWriter("matrix.json", "UTF-8");
            writer.println("[");
            for(Matrice mat : Matrice.allMatrices){
                writer.println(mat.writeToJson());
            }
            writer.println("]");
            writer.close();

            try (Stream<String> stream = Files.lines(Paths.get("matrix.json"))) {
                stream.forEach((line) -> {

                });

            }

        } catch (Exception e) {

        }
    }
}
