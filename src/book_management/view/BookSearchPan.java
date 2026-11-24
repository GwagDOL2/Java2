package book_management.view;

import book_management.domain.Book;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class BookSearchPan extends JPanel {
    String[] header = {"도서번호", "도서명", "저자명", "가격", "재고량", "출판사", "카테고리"};
    String[] comboStr = {"도서명", "출판사", "저자명"};

    ArrayList<Book> bookList = new ArrayList<Book>();
    DefaultTableModel model;
    JTable table;
    JComboBox<String> combo;
    JTextField textSearch;
    JButton btnSearch;

    public BookSearchPan() {
        setLayout(new BorderLayout());

//        검색 영역
        JPanel panN = new JPanel();
        combo = new JComboBox<String>(comboStr);
        textSearch = new JTextField(20);
        btnSearch = new JButton("검색");
        panN.add(combo);
        panN.add(textSearch);
        panN.add(btnSearch);
        add(panN, BorderLayout.NORTH);

//        결과 테이블
        model = new DefaultTableModel(header, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);
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

    public void setBookList(ArrayList<Book> bookList) {
        this.bookList = bookList;
    }

    public String getSearchWord() {
        return textSearch.getText();
    }

    public JComboBox<String> getCombo() {
        return combo;
    }

    public JButton getBtnSearch() {
        return btnSearch;
    }
}