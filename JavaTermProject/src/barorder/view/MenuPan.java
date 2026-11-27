package barorder.view;

import barorder.domain.Category;
import barorder.domain.Menu;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

// 메뉴 관리 탭 : 검색 + 목록 + 추가/수정/삭제
public class MenuPan extends JPanel {
    String[] header = {"메뉴번호", "메뉴명", "가격", "판매상태", "분류"};
    String[] comboStr = {"메뉴명", "분류"};
    String[] statusStr = {"판매중", "품절", "단종"};

    ArrayList<Menu> menuList = new ArrayList<Menu>();
    DefaultTableModel model;
    JTable table;
    JComboBox<String> comboSearch;
    JTextField textSearch;
    JButton btnSearch;
    JTextField textNo, textName, textPrice;
    JComboBox<String> comboStatus;
    JComboBox<Category> comboCategory;
    JButton btnInsert, btnUpdate, btnDelete, btnClear;

    public MenuPan() {
        setLayout(new BorderLayout());

//        검색 영역
        JPanel panN = new JPanel();
        comboSearch = new JComboBox<String>(comboStr);
        textSearch = new JTextField(20);
        btnSearch = new JButton("검색");
        panN.add(comboSearch);
        panN.add(textSearch);
        panN.add(btnSearch);
        add(panN, BorderLayout.NORTH);

//        목록 테이블
        model = new DefaultTableModel(header, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(table), BorderLayout.CENTER);

//        입력 영역
        JPanel panForm = new JPanel(new GridLayout(3, 4, 5, 5));
        textNo = new JTextField();
        textNo.setEditable(false);      // 메뉴번호(PK)는 자동 부여, 수정 불가
        textName = new JTextField();
        textPrice = new JTextField();
        comboStatus = new JComboBox<String>(statusStr);
        comboCategory = new JComboBox<Category>();

        panForm.add(new JLabel("메뉴번호", JLabel.CENTER));
        panForm.add(textNo);
        panForm.add(new JLabel("메뉴명", JLabel.CENTER));
        panForm.add(textName);
        panForm.add(new JLabel("가격", JLabel.CENTER));
        panForm.add(textPrice);
        panForm.add(new JLabel("판매상태", JLabel.CENTER));
        panForm.add(comboStatus);
        panForm.add(new JLabel("분류", JLabel.CENTER));
        panForm.add(comboCategory);
        panForm.add(new JLabel(""));
        panForm.add(new JLabel(""));

//        버튼 영역
        JPanel panBtn = new JPanel();
        btnInsert = new JButton("메뉴 추가");
        btnUpdate = new JButton("메뉴 수정");
        btnDelete = new JButton("메뉴 삭제");
        btnClear = new JButton("입력 초기화");
        panBtn.add(btnInsert);
        panBtn.add(btnUpdate);
        panBtn.add(btnDelete);
        panBtn.add(btnClear);

        JPanel panS = new JPanel(new BorderLayout());
        panS.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        panS.add(panForm, BorderLayout.CENTER);
        panS.add(panBtn, BorderLayout.SOUTH);
        add(panS, BorderLayout.SOUTH);
    }

    //    menuList 내용을 테이블에 출력
    public void putResult() {
        model.setRowCount(0);
        for (Menu menu : menuList) {
            Object[] row = {
                    menu.getMenuNo(), menu.getMenuName(), menu.getPrice(),
                    menu.getSaleStatus(), menu.getCategoryName()
            };
            model.addRow(row);
        }
    }

    //    분류 콤보박스 채우기
    public void setCategoryList(ArrayList<Category> categoryList) {
        Category selected = (Category) comboCategory.getSelectedItem();     // 고르고 있던 분류 기억
        comboCategory.removeAllItems();
        for (Category category : categoryList) {
            comboCategory.addItem(category);
            if (selected != null && selected.getCategoryCode().equals(category.getCategoryCode())) {
                comboCategory.setSelectedItem(category);
            }
        }
    }

    //    테이블에서 클릭한 행의 값을 입력칸에 채우기
    public void setTextField(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= menuList.size()) return;
        Menu menu = menuList.get(rowIndex);
        textNo.setText(String.valueOf(menu.getMenuNo()));
        textName.setText(menu.getMenuName());
        textPrice.setText(String.valueOf(menu.getPrice()));
        comboStatus.setSelectedItem(menu.getSaleStatus());
        for (int i = 0; i < comboCategory.getItemCount(); i++) {
            if (comboCategory.getItemAt(i).getCategoryCode().equals(menu.getCategoryCode())) {
                comboCategory.setSelectedIndex(i);
            }
        }
    }

    //    입력칸의 값으로 Menu 객체 생성 (메뉴번호가 비어 있으면 0)
    public Menu getMenu() {
        Menu menu = new Menu();
        if (!textNo.getText().isEmpty()) {
            menu.setMenuNo(Integer.parseInt(textNo.getText()));
        }
        menu.setMenuName(textName.getText().trim());
        menu.setPrice(Integer.parseInt(textPrice.getText().trim()));
        menu.setSaleStatus((String) comboStatus.getSelectedItem());
        Category category = (Category) comboCategory.getSelectedItem();
        menu.setCategoryCode(category.getCategoryCode());
        return menu;
    }

    //    입력칸 비우기
    public void clearTextField() {
        textNo.setText("");
        textName.setText("");
        textPrice.setText("");
        comboStatus.setSelectedIndex(0);
        table.clearSelection();
    }

    public void setMenuList(ArrayList<Menu> menuList) {
        this.menuList = menuList;
    }

    public String getSearchWord() {
        return textSearch.getText();
    }

    public JComboBox<String> getComboSearch() {
        return comboSearch;
    }

    public JTable getTable() {
        return table;
    }

    public JButton getBtnSearch() {
        return btnSearch;
    }

    public JButton getBtnInsert() {
        return btnInsert;
    }

    public JButton getBtnUpdate() {
        return btnUpdate;
    }

    public JButton getBtnDelete() {
        return btnDelete;
    }

    public JButton getBtnClear() {
        return btnClear;
    }
}
