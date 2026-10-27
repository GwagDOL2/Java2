package ai1027;

import center.CenterFrame2;

import javax.swing.*;
import java.awt.*;

public class BorderTest extends JFrame {
    public BorderTest() {
        super("BorderLayout 연습");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        //닫을때 프로그램이 종료되는 기본닫기 연산
        //JFrame의 5개의 영역(위치)에 버튼을 추가하시오.

        //1. 버튼객체 생성
        JButton btnNorth  = new JButton("North");
        JButton btnSouth  = new JButton("South");
        JButton btnEast   = new JButton("East");
        JButton btnWest   = new JButton("West");
        JButton btnCenter = new JButton("Center");

        //2. JFrame의 각 위치에 추가
        add(btnNorth,  BorderLayout.NORTH);
        add(btnSouth,  BorderLayout.SOUTH);
        add(btnEast,   BorderLayout.EAST);
        add(btnWest,   BorderLayout.WEST);
        add(btnCenter, BorderLayout.CENTER);

        int w = 250, h = 250;//높이 너비 250

        Point point = CenterFrame2.getLocation(w, h);
        setBounds(point.x, point.y, w, h);
        setVisible(true);
    }

    public static void main(String[] args) {
        new BorderTest();
    }
}
