package center;

import javax.swing.*;
import java.awt.*;

public class SetFrame {
    public static void setFrame(JFrame frame, String title, int w, int h) {
        frame.setTitle(title);                                   // 창 제목
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    // 닫으면 프로그램 종료

        Point point = CenterFrame2.getLocation(w, h);            // 화면 가운데 좌표
        frame.setBounds(point.x, point.y, w, h);                 // 위치 + 크기
        frame.setVisible(true);                                  // 마지막에 화면 표시
    }
}
