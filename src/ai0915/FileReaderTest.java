package ai0915;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileReaderTest {
    public static void main(String[] args) {
        try{
            //BufferedReader br = new BufferedReader(new FileReader("D:/2학기/javaproject/2학기/FileIOTest/myData1.txt"));
            BufferedReader br = new BufferedReader(new FileReader("myData1.txt"));
            String line ="";

            while (true){
                line = br.readLine();
                if(line== null)
                    break;
                System.out.println(line);
            }

            br.close();
        } catch (FileNotFoundException e){
            throw new RuntimeException(e);
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
