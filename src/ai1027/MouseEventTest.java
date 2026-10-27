package ai1027;

import center.SetFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseEventTest extends JFrame {
    // 이벤트 결과를 보여줄 라벨 (리스너 안에서도 써야 해서 필드로 선언)
    JLabel label = new JLabel("마우스 이벤트가 여기에 표시됩니다");

    public MouseEventTest() {
        setLayout(new FlowLayout());

        JButton btn = new JButton("마우스이벤트 연습");
        btn.addMouseListener(btnMouseListener);   // 버튼에 마우스 감시자 등록
        add(btn);
        add(label);                               // 버튼 아래에 라벨 추가

        SetFrame.setFrame(this, "MouseEvent Program", 250, 250);
    }

    MouseListener btnMouseListener = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {
            System.out.println("버튼에 마우스가 클릭됨");
            label.setText("버튼에 마우스가 클릭됨");
        }

        @Override
        public void mousePressed(MouseEvent e) {
            System.out.println("버튼에 마우스가 눌러짐");
            label.setText("버튼에 마우스가 눌러짐");
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            System.out.println("버튼에 마우스가 해제됨");
            label.setText("버튼에 마우스가 해제됨");
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            System.out.println("버튼에 마우스가 들어옴");
            label.setText("버튼에 마우스가 들어옴");
        }

        @Override
        public void mouseExited(MouseEvent e) {
            System.out.println("버튼에 마우스가 나감");
            label.setText("버튼에 마우스가 나감");
        }
    };

    public static void main(String[] args) {
        new MouseEventTest();
    }
}
