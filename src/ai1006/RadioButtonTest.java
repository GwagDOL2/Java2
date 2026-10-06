package ai1006;

import center.CenterFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class RadioButtonTest extends JFrame {
    ImageIcon[] imageIcons;
    JRadioButton[] radios;                         // 필드로 이동
    JLabel lbl;
    int selectedIndex;

    public RadioButtonTest(){
        setTitle("라디오버튼 테스트");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panNorth = new JPanel();
        String[] entertainers = {"아이브", "리센느", "캣츠아이"};
        radios = new JRadioButton[entertainers.length];
        imageIcons = new ImageIcon[entertainers.length];
        lbl = new JLabel();                         // 지역변수 선언 제거
        ButtonGroup group = new ButtonGroup();      // 추가

        int i = 0;
        for (String entertainer: entertainers){
            imageIcons[i] = new ImageIcon("imgs/img" + i + ".png");
            radios[i] = new JRadioButton(entertainer);   // 글자를 넣음
            group.add(radios[i]);
            radios[i].addItemListener(radioLister);      // 리스너 등록
            group.add(radios[i]);
            panNorth.add(radios[i++]);
        }
        radios[0].setSelected(true);
        lbl.setIcon(imageIcons[0]);
        add("North",panNorth);
        add ("Center", lbl);

        // setLayout(new FlowLayout());  // 삭제
        int w = 500, h = 500;
        int[] location = CenterFrame.getLocation(w, h);
        setBounds(location[0], location[1], w, h);// setLoction(x,y) + setSize(w,h)
        setVisible(true);
    }

    public static void main(String[] args) {
        new RadioButtonTest();
    }

    ItemListener radioLister = new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent e) {
            JRadioButton selectedRadio = (JRadioButton) e.getSource();
            if (selectedRadio == radios[0])
                lbl.setIcon(imageIcons[0]);
            else if (selectedRadio == radios[1])
                lbl.setIcon(imageIcons[1]);
            else if (selectedRadio == radios[2])
                lbl.setIcon(imageIcons[2]);
        }
    };
}