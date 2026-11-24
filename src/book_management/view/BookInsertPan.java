package book_management.view;

import book_management.domain.Book;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class BookInsertPan extends JPanel {
    String[] header = {"도서번호", "도서명", "저자명", "가격", "재고량", "출판사", "카테고리"};
    String[] publishStr = {"한빛아카데미", "생능출판사", "민음사", "해냄출판사"};
    String[] categoryStr = {"IT도서", "소설", "수필", "시"};

    ArrayList<Book> bookList = new ArrayList<Book>();
    DefaultTableModel model;
    JTable table;
    JTextField textIsbn, textName, textAuthor, textPrice, textInventory;
    JComboBox<String> comboPublish, comboCategory;
    JButton btnInsert;

    public BookInsertPan() {
        setLayout(new BorderLayout());

//        테이블
        model = new DefaultTableModel(header, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

//        입력 영역 (4행 4열)
        JPanel panS = new JPanel(new GridLayout(4, 4, 5, 5));
        panS.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        textIsbn = new JTextField();
        textName = new JTextField();
        textAuthor = new JTextField();
        textPrice = new JTextField();
        textInventory = new JTextField();
        comboPublish = new JComboBox<String>(publishStr);
        comboCategory = new JComboBox<String>(categoryStr);
        btnInsert = new JButton("도서추가");

        panS.add(new JLabel("도서번호", JLabel.CENTER));
        panS.add(textIsbn);
        panS.add(new JLabel("도서명", JLabel.CENTER));
        panS.add(textName);

        panS.add(new JLabel("저자명", JLabel.CENTER));
        panS.add(textAuthor);
        panS.add(new JLabel("가격", JLabel.CENTER));
        panS.add(textPrice);

        panS.add(new JLabel("재고량", JLabel.CENTER));
        panS.add(textInventory);
        panS.add(new JLabel("출판사", JLabel.CENTER));
        panS.add(comboPublish);

        panS.add(new JLabel("카테고리", JLabel.CENTER));
        panS.add(comboCategory);
        panS.add(new JLabel(""));
        panS.add(btnInsert);

        add(panS, BorderLayout.SOUTH);
    }

    //    bookList 내용을 테이블에 출력
    public void putSearchResult() {
        model.setRowCount(0);
        for (Book book : bookList) {
            Object[] row = {
                    book.getIsbn(), book.getName(), book.getAuthor(),
                    book.getPrice(), book.getInventory(),
                    book.getPublish_name(), book.getCategory_name()
            };
            model.addRow(row);
        }
    }

    //    입력한 값으로 Book 객체 생성
    public Book getBook() {
        Book book = new Book();
        book.setIsbn(Integer.parseInt(textIsbn.getText().trim()));
        book.setName(textName.getText().trim());
        book.setAuthor(textAuthor.getText().trim());
        book.setPrice(Integer.parseInt(textPrice.getText().trim()));
        book.setInventory(Integer.parseInt(textInventory.getText().trim()));
        book.setPublish_name((String) comboPublish.getSelectedItem());
        book.setCategory_name((String) comboCategory.getSelectedItem());
        return book;
    }

    //    추가 후 입력칸 초기화
    public void initInsertData() {
        textIsbn.setText("");
        textName.setText("");
        textAuthor.setText("");
        textPrice.setText("");
        textInventory.setText("");
        comboPublish.setSelectedIndex(0);
        comboCategory.setSelectedIndex(0);
    }

    public void setBookList(ArrayList<Book> bookList) {
        this.bookList = bookList;
    }

    public JButton getBtnInsert() {
        return btnInsert;
    }
}