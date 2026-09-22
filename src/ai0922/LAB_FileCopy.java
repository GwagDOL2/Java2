package ai0922;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class LAB_FileCopy {
    public static void main(String[] args) {

        try {
            // 원본 파일을 읽기 위한 객체
            BufferedReader br = new BufferedReader(
                    new FileReader("myData1.txt")
            );

            // 복사할 파일을 쓰기 위한 객체
            FileWriter fw = new FileWriter("myData2.txt");

            // 파일에서 읽은 한 줄을 저장할 변수
            String line = "";

            // 파일의 내용을 한 줄씩 읽는다
            while (true) {

                // 한 줄 읽기
                line = br.readLine();

                // 읽을 내용이 없으면 종료
                if (line == null)
                    break;

                // 읽은 내용을 myData2.txt에 저장
                fw.write(line + "\n");
            }

            // 파일 닫기
            br.close();
            fw.close();

            System.out.println("파일 복사 완료");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}