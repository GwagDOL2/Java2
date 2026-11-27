package barorder.domain;

// 좌석 (seat 테이블 한 행)
public class Seat {
    private int seatNo;
    private int capacity;
    private String area;
    private String seatStatus;

    public int getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(int seatNo) {
        this.seatNo = seatNo;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getSeatStatus() {
        return seatStatus;
    }

    public void setSeatStatus(String seatStatus) {
        this.seatStatus = seatStatus;
    }

    // 콤보박스에 보여줄 글자
    @Override
    public String toString() {
        return seatNo + "번 (" + area + ", " + capacity + "인)";
    }
}
