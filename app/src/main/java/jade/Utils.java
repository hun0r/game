package jade;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Utils {
    public static boolean any(boolean[] arr){
        for (int i = 0; i < arr.length; i++){
            if (arr[i]){
                return true;
            }
        }
        return false;
    } 
    public static String readFile(String fileName){
        File f = new File(fileName);
        String data = "";
        try (Scanner reader = new Scanner(f)){
            while (reader.hasNext()){
                data += reader.nextLine();
            }
        } catch (FileNotFoundException e){
            System.err.println("Resource " + fileName + " not found at " + f.getAbsolutePath() + "!");
        }
        return data;
    }
    public static String getResource(String fileName) throws FileNotFoundException, IOException, URISyntaxException{
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL url = classLoader.getResource(fileName);
        Path path = Paths.get(url.toURI());
        return Files.readString(path);
    };
    public static String getImportantResource(String fileName){
        try {
            return getResource(fileName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
