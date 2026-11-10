package ai1103;

import center.SetFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class MenuTest_1 extends JFrame {

    JTextArea textArea = new JTextArea();
    JFileChooser fileChooser = new JFileChooser();
    JFrame frame;

    File currentFile = null;   // 현재 열려 있는(또는 저장된) 파일

    public MenuTest_1() {
        frame = this;
        JScrollPane scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        add(scrollPane);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");

        menuBar.add(fileMenu);
        menuBar.add(editMenu);

        // File 메뉴
        JMenuItem newMenuItem = new JMenuItem("New");
        JMenuItem openMenuItem = new JMenuItem("Open");
        JMenuItem saveMenuItem = new JMenuItem("Save");      // 추가
        JMenuItem closeMenuItem = new JMenuItem("Close");

        fileMenu.add(newMenuItem);
        fileMenu.add(openMenuItem);
        fileMenu.add(saveMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(closeMenuItem);

        newMenuItem.addActionListener(fileListener);
        openMenuItem.addActionListener(fileListener);
        saveMenuItem.addActionListener(fileListener);
        closeMenuItem.addActionListener(fileListener);

        // Edit 메뉴
        JMenuItem whiteMenuItem = new JMenuItem("White");
        JMenuItem blueMenuItem = new JMenuItem("Blue");
        JMenuItem magentaMenuItem = new JMenuItem("Magenta");

        editMenu.add(whiteMenuItem);
        editMenu.add(blueMenuItem);
        editMenu.add(magentaMenuItem);

        whiteMenuItem.addActionListener(backgroundListener);
        blueMenuItem.addActionListener(backgroundListener);
        magentaMenuItem.addActionListener(backgroundListener);

        // 창 설정
        SetFrame.setFrame(this, "메뉴만들기", 500, 500);
        setTitle("메뉴만들기");
    }

    // File 메뉴 이벤트
    ActionListener fileListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            File selectedFile;
            int result;

            switch (e.getActionCommand()) {

                case "New":
                    textArea.setText("");
                    currentFile = null;
                    setTitle("메뉴만들기");
                    break;

                case "Open":
                    result = fileChooser.showOpenDialog(frame);

                    if (result == JFileChooser.APPROVE_OPTION) {
                        selectedFile = fileChooser.getSelectedFile();

                        if (selectedFile != null) {
                            // 경로가 아니라 파일 내용을 읽어서 표시
                            textArea.setText(readFile(selectedFile));
                            textArea.setCaretPosition(0);
                            currentFile = selectedFile;
                            setTitle("메뉴만들기 - " + selectedFile.getName());
                        }
                    }
                    break;

                case "Save":
                    // 아직 저장한 적 없는 새 문서면 저장 위치를 고르게 함
                    if (currentFile == null) {
                        result = fileChooser.showSaveDialog(frame);

                        if (result != JFileChooser.APPROVE_OPTION) {
                            break;   // 취소하면 저장하지 않음
                        }
                        currentFile = fileChooser.getSelectedFile();
                    }

                    if (saveFile(currentFile)) {
                        setTitle("메뉴만들기 - " + currentFile.getName());
                        JOptionPane.showMessageDialog(frame, "저장되었습니다.\n" + currentFile.getAbsolutePath());
                    }
                    break;

                case "Close":
                    System.exit(0);
                    break;
            }
        }
    };

    // Edit 메뉴 이벤트
    ActionListener backgroundListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            Color color = null;

            switch (e.getActionCommand()) {

                case "White":
                    color = Color.WHITE;
                    break;

                case "Blue":
                    color = Color.BLUE;
                    break;

                case "Magenta":
                    color = Color.MAGENTA;
                    break;
            }
            textArea.setBackground(color);
        }
    };

    // 파일 읽기
    public String readFile(File selectedFile) {
        StringBuilder totalLines = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(selectedFile))) {
            String line;

            while ((line = br.readLine()) != null) {
                totalLines.append(line).append("\n");
            }

        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(frame, "File not found");

        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "파일을 읽는 중 오류가 발생했습니다.");
        }

        return totalLines.toString();
    }

    // 파일 저장
    public boolean saveFile(File file) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write(textArea.getText());
            return true;

        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "파일을 저장하는 중 오류가 발생했습니다.");
            return false;
        }
    }


    public static void main(String[] args) {
        new MenuTest_1();
    }
}