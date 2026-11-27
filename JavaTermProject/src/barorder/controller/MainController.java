package barorder.controller;

import barorder.domain.Menu;
import barorder.domain.Order;
import barorder.domain.OrderItem;
import barorder.domain.Seat;
import barorder.domain.Staff;
import barorder.repository.MenuRepository;
import barorder.repository.OrderRepository;
import barorder.repository.SeatRepository;
import barorder.repository.StaffRepository;
import barorder.view.MenuPan;
import barorder.view.OrderPan;
import barorder.view.SeatPan;
import barorder.view.StaffPan;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

// 프로그램 시작점 : 화면(View)에서 일어난 일을 받아 DB 작업(Repository)을 시키고 화면을 새로 그린다
public class MainController extends JFrame {
    MenuRepository menuRepository = new MenuRepository();
    StaffRepository staffRepository = new StaffRepository();
    SeatRepository seatRepository = new SeatRepository();
    OrderRepository orderRepository = new OrderRepository();

    OrderPan orderPan = new OrderPan();
    MenuPan menuPan = new MenuPan();
    SeatPan seatPan = new SeatPan();
    StaffPan staffPan = new StaffPan();
    JTabbedPane tab = new JTabbedPane(JTabbedPane.TOP);

    public MainController() {
        tab.add("주문 관리", orderPan);
        tab.add("메뉴 관리", menuPan);
        tab.add("좌석 관리", seatPan);
        tab.add("직원 관리", staffPan);
        add(tab);

        addOrderListener();
        addMenuListener();
        addSeatListener();
        addStaffListener();

//        탭을 바꿀 때마다 최신 내용으로 새로고침
        tab.addChangeListener(e -> run(() -> refreshAll()));

        setTitle("술집 주문관리 시스템");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 750);
        setLocationRelativeTo(null);    // 화면 가운데
        setVisible(true);

        run(() -> refreshAll());
    }

    //    ===== 화면 새로고침 =====
    void refreshAll() {
        refreshOrder();
        refreshMenu();
        refreshSeat();
        refreshStaff();
    }

    void refreshOrder() {
        orderPan.setSeatCombo(seatRepository.select());
        orderPan.setStaffCombo(staffRepository.select(""));
        orderPan.setMenuCombo(menuRepository.select("", 0));
        orderPan.setOrderList(orderRepository.select());
        orderPan.putOrderResult();
        orderPan.reselect();
        refreshItem();
    }

    //    선택된 주문의 상세(담긴 메뉴)만 다시 읽기
    void refreshItem() {
        int orderNo = orderPan.getSelectedOrderNo();
        if (orderNo == 0) {
            orderPan.setItemList(new ArrayList<OrderItem>());
        } else {
            orderPan.setItemList(orderRepository.selectItem(orderNo));
        }
        orderPan.putItemResult();
    }

    void refreshMenu() {
        menuPan.setCategoryList(menuRepository.selectCategory());
        menuPan.setMenuList(menuRepository.select(menuPan.getSearchWord(), menuPan.getComboSearch().getSelectedIndex()));
        menuPan.putResult();
    }

    void refreshSeat() {
        seatPan.setSeatList(seatRepository.select());
        seatPan.putResult();
    }

    void refreshStaff() {
        staffPan.setStaffList(staffRepository.select(staffPan.getSearchWord()));
        staffPan.putResult();
    }

    //    ===== 주문 관리 탭 =====
    void addOrderListener() {
//        주문 목록에서 행 클릭 : 입력칸 채우고 그 주문의 상세를 보여준다
        orderPan.getOrderTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                run(() -> {
                    orderPan.setTextField(orderPan.getOrderTable().getSelectedRow());
                    refreshItem();
                });
            }
        });

        orderPan.getBtnInsert().addActionListener(e -> run(() -> {
            Order order = orderPan.getOrder();
            orderRepository.insert(order);
            orderPan.clearTextField();
            refreshOrder();
        }));

        orderPan.getBtnUpdate().addActionListener(e -> run(() -> {
            if (orderPan.getSelectedOrderNo() == 0) {
                showMessage("수정할 주문을 목록에서 선택하세요.");
                return;
            }
            orderRepository.update(orderPan.getOrder());
            refreshOrder();
        }));

        orderPan.getBtnDelete().addActionListener(e -> run(() -> {
            int orderNo = orderPan.getSelectedOrderNo();
            if (orderNo == 0) {
                showMessage("삭제할 주문을 목록에서 선택하세요.");
                return;
            }
            if (confirm(orderNo + "번 주문을 삭제하시겠습니까?\n담긴 메뉴도 함께 삭제됩니다.")) {
                orderRepository.delete(orderNo);
                orderPan.clearTextField();
                refreshOrder();
            }
        }));

        orderPan.getBtnClear().addActionListener(e -> run(() -> {
            orderPan.clearTextField();
            refreshItem();
        }));

//        주문에 메뉴 담기 / 빼기
        orderPan.getBtnItemInsert().addActionListener(e -> run(() -> {
            if (orderPan.getSelectedOrderNo() == 0) {
                showMessage("메뉴를 담을 주문을 목록에서 선택하세요.");
                return;
            }
            orderRepository.insertItem(orderPan.getOrderItem());
            refreshOrder();
        }));

        orderPan.getBtnItemDelete().addActionListener(e -> run(() -> {
            int menuNo = orderPan.getSelectedItemMenuNo();
            if (menuNo == 0) {
                showMessage("뺄 메뉴를 주문 상세 목록에서 선택하세요.");
                return;
            }
            orderRepository.deleteItem(orderPan.getSelectedOrderNo(), menuNo);
            refreshOrder();
        }));
    }

    //    ===== 메뉴 관리 탭 =====
    void addMenuListener() {
        menuPan.getTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                menuPan.setTextField(menuPan.getTable().getSelectedRow());
            }
        });

        menuPan.getBtnSearch().addActionListener(e -> run(() -> refreshMenu()));

        menuPan.getBtnInsert().addActionListener(e -> run(() -> {
            Menu menu = menuPan.getMenu();
            if (menu.getMenuName().isEmpty()) {
                showMessage("메뉴명을 입력하세요.");
                return;
            }
            menuRepository.insert(menu);
            menuPan.clearTextField();
            refreshMenu();
        }));

        menuPan.getBtnUpdate().addActionListener(e -> run(() -> {
            Menu menu = menuPan.getMenu();
            if (menu.getMenuNo() == 0) {
                showMessage("수정할 메뉴를 목록에서 선택하세요.");
                return;
            }
            menuRepository.update(menu);
            refreshMenu();
        }));

        menuPan.getBtnDelete().addActionListener(e -> run(() -> {
            int rowIndex = menuPan.getTable().getSelectedRow();
            if (rowIndex < 0) {
                showMessage("삭제할 메뉴를 목록에서 선택하세요.");
                return;
            }
            int menuNo = (int) menuPan.getTable().getValueAt(rowIndex, 0);
            if (confirm(menuNo + "번 메뉴를 삭제하시겠습니까?")) {
                menuRepository.delete(menuNo);
                menuPan.clearTextField();
                refreshMenu();
            }
        }));

        menuPan.getBtnClear().addActionListener(e -> menuPan.clearTextField());
    }

    //    ===== 좌석 관리 탭 =====
    void addSeatListener() {
        seatPan.getTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                seatPan.setTextField(seatPan.getTable().getSelectedRow());
            }
        });

        seatPan.getBtnInsert().addActionListener(e -> run(() -> {
            Seat seat = seatPan.getSeat();
            seatRepository.insert(seat);
            seatPan.clearTextField();
            refreshSeat();
        }));

        seatPan.getBtnUpdate().addActionListener(e -> run(() -> {
            Seat seat = seatPan.getSeat();
            if (seat.getSeatNo() == 0) {
                showMessage("수정할 좌석을 목록에서 선택하세요.");
                return;
            }
            seatRepository.update(seat);
            refreshSeat();
        }));

        seatPan.getBtnDelete().addActionListener(e -> run(() -> {
            int rowIndex = seatPan.getTable().getSelectedRow();
            if (rowIndex < 0) {
                showMessage("삭제할 좌석을 목록에서 선택하세요.");
                return;
            }
            int seatNo = (int) seatPan.getTable().getValueAt(rowIndex, 0);
            if (confirm(seatNo + "번 좌석을 삭제하시겠습니까?")) {
                seatRepository.delete(seatNo);
                seatPan.clearTextField();
                refreshSeat();
            }
        }));

        seatPan.getBtnClear().addActionListener(e -> seatPan.clearTextField());
    }

    //    ===== 직원 관리 탭 =====
    void addStaffListener() {
        staffPan.getTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                staffPan.setTextField(staffPan.getTable().getSelectedRow());
            }
        });

        staffPan.getBtnSearch().addActionListener(e -> run(() -> refreshStaff()));

        staffPan.getBtnInsert().addActionListener(e -> run(() -> {
            Staff staff = staffPan.getStaff();
            if (staff.getStaffName().isEmpty() || staff.getPhone().isEmpty()) {
                showMessage("이름과 전화번호를 입력하세요.");
                return;
            }
            staffRepository.insert(staff);
            staffPan.clearTextField();
            refreshStaff();
        }));

        staffPan.getBtnUpdate().addActionListener(e -> run(() -> {
            Staff staff = staffPan.getStaff();
            if (staff.getStaffNo() == 0) {
                showMessage("수정할 직원을 목록에서 선택하세요.");
                return;
            }
            staffRepository.update(staff);
            refreshStaff();
        }));

        staffPan.getBtnDelete().addActionListener(e -> run(() -> {
            int rowIndex = staffPan.getTable().getSelectedRow();
            if (rowIndex < 0) {
                showMessage("삭제할 직원을 목록에서 선택하세요.");
                return;
            }
            int staffNo = (int) staffPan.getTable().getValueAt(rowIndex, 0);
            if (confirm(staffNo + "번 직원을 삭제하시겠습니까?")) {
                staffRepository.delete(staffNo);
                staffPan.clearTextField();
                refreshStaff();
            }
        }));

        staffPan.getBtnClear().addActionListener(e -> staffPan.clearTextField());
    }

    //    ===== 공통 =====
    //    작업을 실행하고, 잘못된 입력이나 DB 오류가 나면 프로그램이 죽지 않게 안내창을 띄운다
    void run(Runnable action) {
        try {
            action.run();
        } catch (NumberFormatException e) {
            showMessage("숫자를 입력해야 하는 칸을 확인하세요. (가격, 인원수, 수량, 수용인원)");
        } catch (IllegalArgumentException e) {
            showMessage("입사일은 2026-03-02 같은 형식으로 입력하세요.");
        } catch (NullPointerException e) {
            showMessage("콤보박스에서 선택할 항목이 없습니다. 먼저 좌석/직원/메뉴/분류를 등록하세요.");
        } catch (RuntimeException e) {
            showMessage(toKorean(e));
        }
    }

    //    자주 나오는 Oracle 오류를 알아보기 쉬운 말로 바꾼다
    String toKorean(RuntimeException e) {
        String message = String.valueOf(e.getMessage());
        if (e.getCause() != null) {
            message = message + "\n" + e.getCause().getMessage();
        }
        if (message.contains("ORA-00942")) {
            return "테이블이 없습니다. DbSetup을 먼저 한 번 실행하세요.";
        }
        if (message.contains("ORA-02292")) {
            return "다른 데이터에서 사용 중이라 삭제할 수 없습니다.\n(예: 주문에 들어 있는 메뉴, 주문을 받은 직원, 주문이 있는 좌석)";
        }
        if (message.contains("ORA-00001")) {
            return "이미 등록된 값입니다.\n(같은 전화번호의 직원, 또는 한 주문에 같은 메뉴를 두 번 담은 경우)";
        }
        if (message.contains("ORA-02290")) {
            return "허용되지 않는 값입니다. (가격은 0 이상, 인원수·수량·수용인원은 1 이상)";
        }
        if (message.contains("ORA-01400") || message.contains("ORA-01407")) {
            return "비워 둘 수 없는 칸이 비어 있습니다.";
        }
        if (message.contains("ORA-12899")) {
            return "입력한 글자가 너무 깁니다.";
        }
        return "작업 중 오류가 발생했습니다.\n" + message;
    }

    void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    boolean confirm(String message) {
        int result = JOptionPane.showConfirmDialog(this, message, "확인", JOptionPane.YES_NO_OPTION);
        return result == JOptionPane.YES_OPTION;
    }

    public static void main(String[] args) {
        new MainController();
    }
}
