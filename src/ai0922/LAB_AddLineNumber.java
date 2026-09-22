package ai0922;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LAB_AddLineNumber {
    public static void main(String[] args) {

        try {
            // myData1.txt 파일을 읽기 위한 BufferedReader
            BufferedReader br = new BufferedReader(
                    new FileReader("myData1.txt")
            );

            // 파일에서 읽은 한 줄을 저장할 변수
            String line = "";

            // 줄 번호를 1부터 시작
            int cnt = 1;

            // 파일을 한 줄씩 읽는다
            while (true) {

                // 한 줄 읽기
                line = br.readLine();

                // 더 이상 읽을 내용이 없으면 반복 종료
                if (line == null)
                    break;

                // 줄 번호 + " : " + 파일의 내용 출력
                // cnt++는 현재 cnt를 사용한 후 1 증가한다
                System.out.println(cnt++ + " : " + line);
            }

            // 파일 닫기
            br.close();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
