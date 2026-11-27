package barorder.view;

import barorder.domain.Seat;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

// 좌석 관리 탭 : 목록 + 추가/수정/삭제
public class SeatPan extends JPanel {
    String[] header = {"좌석번호", "수용인원", "위치", "상태"};
    String[] areaStr = {"홀", "룸", "테라스"};
    String[] statusStr = {"빈자리", "이용중", "예약", "정리중"};

    ArrayList<Seat> seatList = new ArrayList<Seat>();
    DefaultTableModel model;
    JTable table;
    JTextField textNo, textCapacity;
    JComboBox<String> comboArea, comboStatus;
    JButton btnInsert, btnUpdate, btnDelete, btnClear;

    public SeatPan() {
        setLayout(new BorderLayout());

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
        JPanel panForm = new JPanel(new GridLayout(2, 4, 5, 5));
        textNo = new JTextField();
        textNo.setEditable(false);      // 좌석번호(PK)는 자동 부여, 수정 불가
        textCapacity = new JTextField();
        comboArea = new JComboBox<String>(areaStr);
        comboStatus = new JComboBox<String>(statusStr);

        panForm.add(new JLabel("좌석번호", JLabel.CENTER));
        panForm.add(textNo);
        panForm.add(new JLabel("수용인원", JLabel.CENTER));
        panForm.add(textCapacity);
        panForm.add(new JLabel("위치", JLabel.CENTER));
        panForm.add(comboArea);
        panForm.add(new JLabel("상태", JLabel.CENTER));
        panForm.add(comboStatus);

//        버튼 영역
        JPanel panBtn = new JPanel();
        btnInsert = new JButton("좌석 추가");
        btnUpdate = new JButton("좌석 수정");
        btnDelete = new JButton("좌석 삭제");
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

    //    seatList 내용을 테이블에 출력
    public void putResult() {
        model.setRowCount(0);
        for (Seat seat : seatList) {
            Object[] row = {
                    seat.getSeatNo(), seat.getCapacity(), seat.getArea(), seat.getSeatStatus()
            };
            model.addRow(row);
        }
    }

    //    테이블에서 클릭한 행의 값을 입력칸에 채우기
    public void setTextField(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= seatList.size()) return;
        Seat seat = seatList.get(rowIndex);
        textNo.setText(String.valueOf(seat.getSeatNo()));
        textCapacity.setText(String.valueOf(seat.getCapacity()));
        comboArea.setSelectedItem(seat.getArea());
        comboStatus.setSelectedItem(seat.getSeatStatus());
    }

    //    입력칸의 값으로 Seat 객체 생성 (좌석번호가 비어 있으면 0)
    public Seat getSeat() {
        Seat seat = new Seat();
        if (!textNo.getText().isEmpty()) {
            seat.setSeatNo(Integer.parseInt(textNo.getText()));
        }
        seat.setCapacity(Integer.parseInt(textCapacity.getText().trim()));
        seat.setArea((String) comboArea.getSelectedItem());
        seat.setSeatStatus((String) comboStatus.getSelectedItem());
        return seat;
    }

    //    입력칸 비우기
    public void clearTextField() {
        textNo.setText("");
        textCapacity.setText("");
        comboArea.setSelectedIndex(0);
        comboStatus.setSelectedIndex(0);
        table.clearSelection();
    }

    public void setSeatList(ArrayList<Seat> seatList) {
        this.seatList = seatList;
    }

    public JTable getTable() {
        return table;
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
