package book_management.controller;

import book_management.domain.Book;
import book_management.repository.BookRepository;
import book_management.view.BookInsertPan;
import book_management.view.BookSearchPan;
import book_management.view.BookUpdatePan;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class BookController extends JFrame {
    BookRepository repository = new BookRepository();
    BookSearchPan searchPan = new BookSearchPan();
    BookInsertPan insertPan = new BookInsertPan();
    BookUpdatePan updatePan = new BookUpdatePan();
    JTabbedPane tab = new JTabbedPane(JTabbedPane.TOP);
    JTable updateTable;

    public BookController() {
//        도서 검색
        searchPan.setBookList(repository.select("", 0));
        searchPan.putSearchResult();
        searchPan.getBtnSearch().addActionListener(btnSearchListener);
        tab.add("도서 검색", searchPan);

//        도서 추가
        insertPan.setBookList(repository.select("", 0));
        insertPan.putSearchResult();
        insertPan.getBtnInsert().addActionListener(btnInsertListener);
        tab.add("도서 추가", insertPan);

//        수정 및 삭제
        updatePan.setBookList(repository.select("", 0));
        updatePan.putSearchResult();
        updatePan.getBtnUpdate().addActionListener(btnUpdateListener);
        updateTable = updatePan.getTable();
        updateTable.addMouseListener(updatTableListener);
        tab.add("수정 및 삭제", updatePan);

        tab.addMouseListener(tabListener);
        add(tab);

        setTitle("도서관리 시스템");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 600);
        setLocationRelativeTo(null);    // 화면 가운데
        setVisible(true);
    }

    //    검색 버튼
    ActionListener btnSearchListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            ArrayList<Book> searchBookList = new ArrayList<>();
            searchBookList = repository.select(searchPan.getSearchWord(), searchPan.getCombo().getSelectedIndex());
            searchPan.setBookList(searchBookList);
            searchPan.putSearchResult();
        }
    };

    //    도서추가 버튼
    ActionListener btnInsertListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Book book = insertPan.getBook();
            repository.insert(book);
            insertPan.initInsertData();
            ArrayList<Book> insertBookList = new ArrayList<>();
            insertBookList = repository.select("", 0);
            insertPan.setBookList(insertBookList);
            insertPan.putSearchResult();
        }
    };

    //    수정 탭의 테이블 클릭 : 1번 클릭=입력칸 채우기, 더블클릭=삭제
    MouseAdapter updatTableListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getClickCount() == 1){
                int rowIndex = updateTable.getSelectedRow();
                updatePan.setTextField(rowIndex);
            }

            if (e.getClickCount() == 2){
                DefaultTableModel model = updatePan.getModel();
                int rowIndex = updateTable.getSelectedRow();
                String isbn = model.getValueAt(rowIndex, 0).toString();
                repository.delete(isbn);
                ArrayList<Book> updateBookList = new ArrayList<Book>();
                updateBookList = repository.select("", 0);
                updatePan.setBookList(updateBookList);
                updatePan.putSearchResult();
            }
        }
    };

    //    도서수정 버튼
    ActionListener btnUpdateListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Book book = updatePan.getBook();
            repository.update(book);
            ArrayList<Book> updateBookList = new ArrayList<>();
            updateBookList = repository.select("", 0);
            updatePan.setBookList(updateBookList);
            updatePan.putSearchResult();
        }
    };

    //    탭을 바꿀 때마다 최신 목록으로 새로고침
    MouseAdapter tabListener = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            ArrayList<Book> bookList = repository.select("", 0);
            int tabIndex = tab.getSelectedIndex();
            if(tabIndex == 0){
                searchPan.setBookList(bookList);
                searchPan.putSearchResult();
            }else if(tabIndex == 1){
                insertPan.setBookList(bookList);
                insertPan.putSearchResult();
            }else{
                updatePan.setBookList(bookList);
                updatePan.putSearchResult();
            }
        }
    };

    public static void main(String[] args) {
        new BookController();
    }
}