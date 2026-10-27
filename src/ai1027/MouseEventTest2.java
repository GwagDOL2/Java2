package ai1027;

import center.SetFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseEventTest2 extends JFrame {
    // 리스너 안에서도 써야 하는 것들은 필드로 선언
    JLabel label = new JLabel("버튼을 눌러 색을 바꿔보세요");
    JButton btnRed    = new JButton("빨강");
    JButton btnYellow = new JButton("노랑");
    JButton btnBlue   = new JButton("파랑");

    public MouseEventTest2() {
        setLayout(new FlowLayout());

        // 버튼 3개에 같은 리스너 등록
        btnRed.addMouseListener(btnMouseListener);
        btnYellow.addMouseListener(btnMouseListener);
        btnBlue.addMouseListener(btnMouseListener);

        add(btnRed);
        add(btnYellow);
        add(btnBlue);
        add(label);

        SetFrame.setFrame(this, "MouseEvent Program2", 300, 250);
    }

    MouseListener btnMouseListener = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {
            // 어떤 버튼이 눌렸는지 확인
            if (e.getSource() == btnRed) {
                getContentPane().setBackground(Color.RED);
                label.setText("빨간색으로 바뀜");
            } else if (e.getSource() == btnYellow) {
                getContentPane().setBackground(Color.YELLOW);
                label.setText("노란색으로 바뀜");
            } else if (e.getSource() == btnBlue) {
                getContentPane().setBackground(Color.BLUE);
                label.setText("파란색으로 바뀜");
            }
            System.out.println(label.getText());
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    };

    public static void main(String[] args) {
        new MouseEventTest2();
    }
}
