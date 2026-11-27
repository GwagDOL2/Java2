package barorder.view;

import barorder.domain.Staff;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.util.ArrayList;

// 직원 관리 탭 : 검색 + 목록 + 추가/수정/삭제
public class StaffPan extends JPanel {
    String[] header = {"직원번호", "이름", "전화번호", "직급", "입사일"};
    String[] gradeStr = {"알바", "매니저", "점장"};

    ArrayList<Staff> staffList = new ArrayList<Staff>();
    DefaultTableModel model;
    JTable table;
    JTextField textSearch;
    JButton btnSearch;
    JTextField textNo, textName, textPhone, textHireDate;
    JComboBox<String> comboGrade;
    JButton btnInsert, btnUpdate, btnDelete, btnClear;

    public StaffPan() {
        setLayout(new BorderLayout());

//        검색 영역
        JPanel panN = new JPanel();
        textSearch = new JTextField(20);
        btnSearch = new JButton("검색");
        panN.add(new JLabel("이름"));
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
        textNo.setEditable(false);      // 직원번호(PK)는 자동 부여, 수정 불가
        textName = new JTextField();
        textPhone = new JTextField();
        textHireDate = new JTextField();
        comboGrade = new JComboBox<String>(gradeStr);

        panForm.add(new JLabel("직원번호", JLabel.CENTER));
        panForm.add(textNo);
        panForm.add(new JLabel("이름", JLabel.CENTER));
        panForm.add(textName);
        panForm.add(new JLabel("전화번호", JLabel.CENTER));
        panForm.add(textPhone);
        panForm.add(new JLabel("직급", JLabel.CENTER));
        panForm.add(comboGrade);
        panForm.add(new JLabel("입사일 (예: 2026-03-02)", JLabel.CENTER));
        panForm.add(textHireDate);
        panForm.add(new JLabel(""));
        panForm.add(new JLabel(""));

//        버튼 영역
        JPanel panBtn = new JPanel();
        btnInsert = new JButton("직원 추가");
        btnUpdate = new JButton("직원 수정");
        btnDelete = new JButton("직원 삭제");
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

    //    staffList 내용을 테이블에 출력
    public void putResult() {
        model.setRowCount(0);
        for (Staff staff : staffList) {
            Object[] row = {
                    staff.getStaffNo(), staff.getStaffName(), staff.getPhone(),
                    staff.getGrade(), staff.getHireDate()
            };
            model.addRow(row);
        }
    }

    //    테이블에서 클릭한 행의 값을 입력칸에 채우기
    public void setTextField(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= staffList.size()) return;
        Staff staff = staffList.get(rowIndex);
        textNo.setText(String.valueOf(staff.getStaffNo()));
        textName.setText(staff.getStaffName());
        textPhone.setText(staff.getPhone());
        comboGrade.setSelectedItem(staff.getGrade());
        textHireDate.setText(String.valueOf(staff.getHireDate()));
    }

    //    입력칸의 값으로 Staff 객체 생성 (직원번호가 비어 있으면 0)
    public Staff getStaff() {
        Staff staff = new Staff();
        if (!textNo.getText().isEmpty()) {
            staff.setStaffNo(Integer.parseInt(textNo.getText()));
        }
        staff.setStaffName(textName.getText().trim());
        staff.setPhone(textPhone.getText().trim());
        staff.setGrade((String) comboGrade.getSelectedItem());
        staff.setHireDate(Date.valueOf(textHireDate.getText().trim()));   // yyyy-MM-dd 형식만 가능
        return staff;
    }

    //    입력칸 비우기
    public void clearTextField() {
        textNo.setText("");
        textName.setText("");
        textPhone.setText("");
        textHireDate.setText("");
        comboGrade.setSelectedIndex(0);
        table.clearSelection();
    }

    public void setStaffList(ArrayList<Staff> staffList) {
        this.staffList = staffList;
    }

    public String getSearchWord() {
        return textSearch.getText();
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
