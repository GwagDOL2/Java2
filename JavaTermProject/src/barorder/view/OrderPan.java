package barorder.view;

import barorder.domain.Menu;
import barorder.domain.Order;
import barorder.domain.OrderItem;
import barorder.domain.Seat;
import barorder.domain.Staff;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

// 주문 관리 탭 : 위쪽 = 주문 목록과 주문 입력, 아래쪽 = 선택한 주문에 담긴 메뉴
public class OrderPan extends JPanel {
    String[] orderHeader = {"주문번호", "주문일시", "좌석", "담당직원", "인원수", "결제수단", "결제상태", "합계"};
    String[] itemHeader = {"메뉴번호", "메뉴명", "가격", "수량", "금액", "요청사항"};
    String[] payMethodStr = {"(미정)", "카드", "현금", "계좌이체"};
    String[] payStatusStr = {"미결제", "결제완료", "취소"};

    ArrayList<Order> orderList = new ArrayList<Order>();
    ArrayList<OrderItem> itemList = new ArrayList<OrderItem>();
    DefaultTableModel orderModel, itemModel;
    JTable orderTable, itemTable;

    JTextField textOrderNo, textGuestCount;
    JComboBox<Seat> comboSeat;
    JComboBox<Staff> comboStaff;
    JComboBox<String> comboPayMethod, comboPayStatus;
    JButton btnInsert, btnUpdate, btnDelete, btnClear;

    JComboBox<Menu> comboMenu;
    JTextField textQuantity, textMemo;
    JButton btnItemInsert, btnItemDelete;
    JLabel labelItemTitle;

    public OrderPan() {
        setLayout(new GridLayout(2, 1, 0, 5));

//        ===== 위쪽 : 주문 =====
        orderModel = new DefaultTableModel(orderHeader, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        orderTable = new JTable(orderModel);
        orderTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel panOrderForm = new JPanel(new GridLayout(3, 4, 5, 5));
        textOrderNo = new JTextField();
        textOrderNo.setEditable(false);     // 주문번호(PK)는 자동 부여, 수정 불가
        textGuestCount = new JTextField();
        comboSeat = new JComboBox<Seat>();
        comboStaff = new JComboBox<Staff>();
        comboPayMethod = new JComboBox<String>(payMethodStr);
        comboPayStatus = new JComboBox<String>(payStatusStr);

        panOrderForm.add(new JLabel("주문번호", JLabel.CENTER));
        panOrderForm.add(textOrderNo);
        panOrderForm.add(new JLabel("인원수", JLabel.CENTER));
        panOrderForm.add(textGuestCount);
        panOrderForm.add(new JLabel("좌석", JLabel.CENTER));
        panOrderForm.add(comboSeat);
        panOrderForm.add(new JLabel("담당직원", JLabel.CENTER));
        panOrderForm.add(comboStaff);
        panOrderForm.add(new JLabel("결제수단", JLabel.CENTER));
        panOrderForm.add(comboPayMethod);
        panOrderForm.add(new JLabel("결제상태", JLabel.CENTER));
        panOrderForm.add(comboPayStatus);

        JPanel panOrderBtn = new JPanel();
        btnInsert = new JButton("주문 등록");
        btnUpdate = new JButton("주문 수정");
        btnDelete = new JButton("주문 삭제");
        btnClear = new JButton("입력 초기화");
        panOrderBtn.add(btnInsert);
        panOrderBtn.add(btnUpdate);
        panOrderBtn.add(btnDelete);
        panOrderBtn.add(btnClear);

        JPanel panOrderS = new JPanel(new BorderLayout());
        panOrderS.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 10));
        panOrderS.add(panOrderForm, BorderLayout.CENTER);
        panOrderS.add(panOrderBtn, BorderLayout.SOUTH);

        JPanel panOrder = new JPanel(new BorderLayout());
        panOrder.setBorder(BorderFactory.createTitledBorder("주문 목록"));
        panOrder.add(new JScrollPane(orderTable), BorderLayout.CENTER);
        panOrder.add(panOrderS, BorderLayout.SOUTH);
        add(panOrder);

//        ===== 아래쪽 : 주문 상세 =====
        itemModel = new DefaultTableModel(itemHeader, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        itemTable = new JTable(itemModel);
        itemTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel panItemForm = new JPanel();
        comboMenu = new JComboBox<Menu>();
        textQuantity = new JTextField("1", 3);
        textMemo = new JTextField(12);
        btnItemInsert = new JButton("메뉴 담기");
        btnItemDelete = new JButton("선택 메뉴 빼기");
        panItemForm.add(new JLabel("메뉴"));
        panItemForm.add(comboMenu);
        panItemForm.add(new JLabel("수량"));
        panItemForm.add(textQuantity);
        panItemForm.add(new JLabel("요청사항"));
        panItemForm.add(textMemo);
        panItemForm.add(btnItemInsert);
        panItemForm.add(btnItemDelete);

        labelItemTitle = new JLabel("위 목록에서 주문을 선택하세요.", JLabel.CENTER);

        JPanel panItem = new JPanel(new BorderLayout());
        panItem.setBorder(BorderFactory.createTitledBorder("주문 상세 (선택한 주문에 담긴 메뉴)"));
        panItem.add(labelItemTitle, BorderLayout.NORTH);
        panItem.add(new JScrollPane(itemTable), BorderLayout.CENTER);
        panItem.add(panItemForm, BorderLayout.SOUTH);
        add(panItem);
    }

    //    orderList 내용을 주문 테이블에 출력
    public void putOrderResult() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        orderModel.setRowCount(0);
        for (Order order : orderList) {
            Object[] row = {
                    order.getOrderNo(), format.format(order.getOrderTime()),
                    order.getSeatNo() + "번", order.getStaffName(), order.getGuestCount(),
                    order.getPayMethod() == null ? "" : order.getPayMethod(),
                    order.getPayStatus(), order.getTotalPrice()
            };
            orderModel.addRow(row);
        }
    }

    //    itemList 내용을 주문 상세 테이블에 출력
    public void putItemResult() {
        itemModel.setRowCount(0);
        int total = 0;
        for (OrderItem item : itemList) {
            int amount = item.getPrice() * item.getQuantity();
            total = total + amount;
            Object[] row = {
                    item.getMenuNo(), item.getMenuName(), item.getPrice(),
                    item.getQuantity(), amount, item.getMemo() == null ? "" : item.getMemo()
            };
            itemModel.addRow(row);
        }
        if (textOrderNo.getText().isEmpty()) {
            labelItemTitle.setText("위 목록에서 주문을 선택하세요.");
        } else {
            labelItemTitle.setText(textOrderNo.getText() + "번 주문  /  합계 " + total + "원");
        }
    }

    //    콤보박스 채우기
    public void setSeatCombo(ArrayList<Seat> seatList) {
        comboSeat.removeAllItems();
        for (Seat seat : seatList) {
            comboSeat.addItem(seat);
        }
    }

    public void setStaffCombo(ArrayList<Staff> staffList) {
        comboStaff.removeAllItems();
        for (Staff staff : staffList) {
            comboStaff.addItem(staff);
        }
    }

    //    판매중인 메뉴만 주문할 수 있다
    public void setMenuCombo(ArrayList<Menu> menuList) {
        comboMenu.removeAllItems();
        for (Menu menu : menuList) {
            if (menu.getSaleStatus().equals("판매중")) {
                comboMenu.addItem(menu);
            }
        }
    }

    //    목록을 새로 읽은 뒤에도 보고 있던 주문이 계속 선택되어 있게 한다
    public void reselect() {
        int orderNo = getSelectedOrderNo();
        for (int i = 0; i < orderList.size(); i++) {
            if (orderList.get(i).getOrderNo() == orderNo) {
                orderTable.setRowSelectionInterval(i, i);
                setTextField(i);
                return;
            }
        }
        textOrderNo.setText("");    // 그 주문이 없어졌으면 선택 해제
    }

    //    주문 테이블에서 클릭한 행의 값을 입력칸에 채우기
    public void setTextField(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= orderList.size()) return;
        Order order = orderList.get(rowIndex);
        textOrderNo.setText(String.valueOf(order.getOrderNo()));
        textGuestCount.setText(String.valueOf(order.getGuestCount()));
        for (int i = 0; i < comboSeat.getItemCount(); i++) {
            if (comboSeat.getItemAt(i).getSeatNo() == order.getSeatNo()) {
                comboSeat.setSelectedIndex(i);
            }
        }
        for (int i = 0; i < comboStaff.getItemCount(); i++) {
            if (comboStaff.getItemAt(i).getStaffNo() == order.getStaffNo()) {
                comboStaff.setSelectedIndex(i);
            }
        }
        if (order.getPayMethod() == null) {
            comboPayMethod.setSelectedIndex(0);
        } else {
            comboPayMethod.setSelectedItem(order.getPayMethod());
        }
        comboPayStatus.setSelectedItem(order.getPayStatus());
    }

    //    입력칸의 값으로 Order 객체 생성 (주문번호가 비어 있으면 0)
    public Order getOrder() {
        Order order = new Order();
        order.setOrderNo(getSelectedOrderNo());
        order.setGuestCount(Integer.parseInt(textGuestCount.getText().trim()));
        Seat seat = (Seat) comboSeat.getSelectedItem();
        order.setSeatNo(seat.getSeatNo());
        Staff staff = (Staff) comboStaff.getSelectedItem();
        order.setStaffNo(staff.getStaffNo());
        if (comboPayMethod.getSelectedIndex() == 0) {
            order.setPayMethod(null);       // (미정) = 아직 결제수단 없음
        } else {
            order.setPayMethod((String) comboPayMethod.getSelectedItem());
        }
        order.setPayStatus((String) comboPayStatus.getSelectedItem());
        return order;
    }

    //    입력칸의 값으로 OrderItem 객체 생성
    public OrderItem getOrderItem() {
        OrderItem item = new OrderItem();
        item.setOrderNo(getSelectedOrderNo());
        Menu menu = (Menu) comboMenu.getSelectedItem();
        item.setMenuNo(menu.getMenuNo());
        item.setQuantity(Integer.parseInt(textQuantity.getText().trim()));
        String memo = textMemo.getText().trim();
        item.setMemo(memo.isEmpty() ? null : memo);
        return item;
    }

    //    지금 선택된 주문번호 (없으면 0)
    public int getSelectedOrderNo() {
        if (textOrderNo.getText().isEmpty()) {
            return 0;
        }
        return Integer.parseInt(textOrderNo.getText());
    }

    //    주문 상세 테이블에서 선택한 메뉴번호 (없으면 0)
    public int getSelectedItemMenuNo() {
        int rowIndex = itemTable.getSelectedRow();
        if (rowIndex < 0 || rowIndex >= itemList.size()) {
            return 0;
        }
        return itemList.get(rowIndex).getMenuNo();
    }

    //    입력칸 비우기
    public void clearTextField() {
        textOrderNo.setText("");
        textGuestCount.setText("");
        comboPayMethod.setSelectedIndex(0);
        comboPayStatus.setSelectedIndex(0);
        textQuantity.setText("1");
        textMemo.setText("");
        orderTable.clearSelection();
    }

    public void setOrderList(ArrayList<Order> orderList) {
        this.orderList = orderList;
    }

    public void setItemList(ArrayList<OrderItem> itemList) {
        this.itemList = itemList;
    }

    public JTable getOrderTable() {
        return orderTable;
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

    public JButton getBtnItemInsert() {
        return btnItemInsert;
    }

    public JButton getBtnItemDelete() {
        return btnItemDelete;
    }
}
