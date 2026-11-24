package book_management.view;

import book_management.domain.Book;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class BookUpdatePan extends JPanel {
    String[] header = {"도서번호", "도서명", "저자명", "가격", "재고량", "출판사", "카테고리"};
    String[] publishStr = {"한빛아카데미", "생능출판사", "민음사", "해냄출판사"};
    String[] categoryStr = {"IT도서", "소설", "수필", "시"};

    ArrayList<Book> bookList = new ArrayList<Book>();
    DefaultTableModel model;
    JTable table;
    JTextField textIsbn, textName, textAuthor, textPrice, textInventory;
    JComboBox<String> comboPublish, comboCategory;
    JButton btnUpdate;

    public BookUpdatePan() {
        setLayout(new BorderLayout());

//        테이블
        model = new DefaultTableModel(header, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(table), BorderLayout.CENTER);

//        수정 영역 (4행 4열)
        JPanel panS = new JPanel(new GridLayout(4, 4, 5, 5));
        panS.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        textIsbn = new JTextField();
        textIsbn.setEditable(false);    // 도서번호(PK)는 수정 불가
        textName = new JTextField();
        textAuthor = new JTextField();
        textPrice = new JTextField();
        textInventory = new JTextField();
        comboPublish = new JComboBox<String>(publishStr);
        comboCategory = new JComboBox<String>(categoryStr);
        btnUpdate = new JButton("도서수정");

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
        panS.add(btnUpdate);

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

    //    테이블에서 클릭한 행의 값을 아래 입력칸에 채우기
    public void setTextField(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= bookList.size()) return;
        Book book = bookList.get(rowIndex);
        textIsbn.setText(String.valueOf(book.getIsbn()));
        textName.setText(book.getName());
        textAuthor.setText(book.getAuthor());
        textPrice.setText(String.valueOf(book.getPrice()));
        textInventory.setText(String.valueOf(book.getInventory()));
        comboPublish.setSelectedItem(book.getPublish_name());
        comboCategory.setSelectedItem(book.getCategory_name());
    }

    //    입력칸의 값으로 Book 객체 생성
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

    public void setBookList(ArrayList<Book> bookList) {
        this.bookList = bookList;
    }

    public DefaultTableModel getModel() {
        return model;
    }

    public JTable getTable() {
        return table;
    }

    public JButton getBtnUpdate() {
        return btnUpdate;
    }
}