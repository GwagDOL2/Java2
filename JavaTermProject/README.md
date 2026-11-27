# JavaTermProject - 술집 주문관리 시스템

Java Swing과 Oracle DB(JDBC)로 만든 술집 주문·매장 관리 프로그램입니다.
주문, 메뉴, 좌석, 직원을 조회·추가·수정·삭제(CRUD)할 수 있습니다.

## 사용 기술
- Java 21, Java Swing
- Oracle Database XE, JDBC (ojdbc)
- IntelliJ IDEA

## 구조
```
src/barorder
├─ controller   MainController  (프로그램 시작점, 화면 이벤트 처리)
├─ domain       Staff, Seat, Category, Menu, Order, OrderItem
├─ repository   JDBCConnector, *Repository (SQL 실행), DbSetup (테이블 생성)
└─ view         OrderPan, MenuPan, SeatPan, StaffPan (탭 화면)
sql/bar_order.sql   테이블 생성 + 샘플 데이터
```

## 데이터베이스
| 테이블 | 설명 |
|---|---|
| staff | 직원 |
| seat | 좌석 |
| category | 메뉴분류 |
| menu | 메뉴 (category 참조) |
| orders | 주문 (seat, staff 참조) |
| order_item | 주문 상세 (orders, menu 참조) |

## 실행 방법
1. Oracle에 `sql/bar_order.sql`을 실행하거나, `barorder.repository.DbSetup`을 한 번 실행해 테이블을 만든다.
2. `barorder.controller.MainController`를 실행한다.
