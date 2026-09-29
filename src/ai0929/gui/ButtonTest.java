package ai0929.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonTest extends JFrame {
    public ButtonTest(){
        setLayout(new FlowLayout());
        setTitle("Button 컴포넌트");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        JButton btn = new JButton("메시지 대화상자 보이기");
        add(btn);
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "대화상자를 선택하셨네요.");
            }
        });
        setSize(500,200);
        setVisible(true);
    }

    public class ButtonActionLister implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    public static void main(String[] args) {
        new ButtonTest();
    }
}
