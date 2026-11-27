-- 술집 주문·매장 관리 데이터베이스
-- 실행 순서: 테이블 생성 -> 샘플 데이터 입력
-- (다시 만들 때는 맨 아래 DROP 문의 주석을 풀어 먼저 실행)

-- 1. 테이블 생성
CREATE TABLE staff (
    staff_no    INT           PRIMARY KEY,
    staff_name  VARCHAR2(30)  NOT NULL,
    phone       VARCHAR2(20)  NOT NULL UNIQUE,
    grade       VARCHAR2(20)  DEFAULT '알바' NOT NULL
                CHECK (grade IN ('점장', '매니저', '알바')),
    hire_date   DATE          NOT NULL
);

CREATE TABLE seat (
    seat_no      INT           PRIMARY KEY,
    capacity     INT           DEFAULT 4 NOT NULL CHECK (capacity >= 1),
    area         VARCHAR2(20)  DEFAULT '홀' NOT NULL
                 CHECK (area IN ('홀', '룸', '테라스')),
    seat_status  VARCHAR2(20)  DEFAULT '빈자리' NOT NULL
                 CHECK (seat_status IN ('빈자리', '이용중', '예약', '정리중'))
);

CREATE TABLE category (
    category_code  CHAR(3)       PRIMARY KEY,
    category_name  VARCHAR2(30)  NOT NULL UNIQUE
);

CREATE TABLE menu (
    menu_no        INT           PRIMARY KEY,
    menu_name      VARCHAR2(60)  NOT NULL,
    price          INT           NOT NULL CHECK (price >= 0),
    sale_status    VARCHAR2(20)  DEFAULT '판매중' NOT NULL
                   CHECK (sale_status IN ('판매중', '품절', '단종')),
    category_code  CHAR(3)       NOT NULL REFERENCES category (category_code)
);

CREATE TABLE orders (
    order_no     INT           PRIMARY KEY,
    order_time   DATE          DEFAULT SYSDATE NOT NULL,
    guest_count  INT           DEFAULT 1 NOT NULL CHECK (guest_count >= 1),
    pay_method   VARCHAR2(20)  CHECK (pay_method IN ('카드', '현금', '계좌이체')),
    pay_status   VARCHAR2(20)  DEFAULT '미결제' NOT NULL
                 CHECK (pay_status IN ('미결제', '결제완료', '취소')),
    seat_no      INT           NOT NULL REFERENCES seat (seat_no),
    staff_no     INT           NOT NULL REFERENCES staff (staff_no)
);

CREATE TABLE order_item (
    order_no  INT            NOT NULL REFERENCES orders (order_no),
    menu_no   INT            NOT NULL REFERENCES menu (menu_no),
    quantity  INT            DEFAULT 1 NOT NULL CHECK (quantity >= 1),
    memo      VARCHAR2(200),
    PRIMARY KEY (order_no, menu_no)
);

-- 2. 샘플 데이터

INSERT INTO staff VALUES (1, '김점장', '010-1000-0001', '점장', TO_DATE('2023-03-02', 'YYYY-MM-DD'));
INSERT INTO staff VALUES (2, '박매니저', '010-1000-0002', '매니저', TO_DATE('2024-06-10', 'YYYY-MM-DD'));
INSERT INTO staff VALUES (3, '이서빙', '010-1000-0003', '알바', TO_DATE('2025-09-01', 'YYYY-MM-DD'));
INSERT INTO staff VALUES (4, '최주방', '010-1000-0004', '알바', TO_DATE('2026-01-15', 'YYYY-MM-DD'));

INSERT INTO seat VALUES (1, 4, '홀', '이용중');
INSERT INTO seat VALUES (2, 4, '홀', '빈자리');
INSERT INTO seat VALUES (3, 2, '홀', '빈자리');
INSERT INTO seat VALUES (4, 6, '룸', '예약');
INSERT INTO seat VALUES (5, 8, '룸', '빈자리');
INSERT INTO seat VALUES (6, 4, '테라스', '정리중');

INSERT INTO category VALUES ('A01', '안주');
INSERT INTO category VALUES ('A02', '주류');
INSERT INTO category VALUES ('A03', '음료');
INSERT INTO category VALUES ('A04', '식사');

INSERT INTO menu VALUES (1, '모둠감자튀김', 12000, '판매중', 'A01');
INSERT INTO menu VALUES (2, '치즈계란말이', 15000, '판매중', 'A01');
INSERT INTO menu VALUES (3, '먹태구이', 16000, '판매중', 'A01');
INSERT INTO menu VALUES (4, '골뱅이소면', 19000, '품절', 'A01');
INSERT INTO menu VALUES (5, '생맥주 500', 5000, '판매중', 'A02');
INSERT INTO menu VALUES (6, '소주', 5000, '판매중', 'A02');
INSERT INTO menu VALUES (7, '하이볼', 8000, '판매중', 'A02');
INSERT INTO menu VALUES (8, '콜라', 2500, '판매중', 'A03');
INSERT INTO menu VALUES (9, '사이다', 2500, '판매중', 'A03');
INSERT INTO menu VALUES (10, '해물라면', 7000, '판매중', 'A04');

INSERT INTO orders VALUES (1, TO_DATE('2026-11-20 19:10', 'YYYY-MM-DD HH24:MI'), 3, '카드', '결제완료', 2, 3);
INSERT INTO orders VALUES (2, TO_DATE('2026-11-20 20:40', 'YYYY-MM-DD HH24:MI'), 2, '현금', '결제완료', 3, 3);
INSERT INTO orders VALUES (3, TO_DATE('2026-11-21 18:30', 'YYYY-MM-DD HH24:MI'), 5, NULL, '취소', 4, 2);
INSERT INTO orders VALUES (4, TO_DATE('2026-11-21 21:05', 'YYYY-MM-DD HH24:MI'), 4, NULL, '미결제', 1, 3);

INSERT INTO order_item VALUES (1, 1, 1, NULL);
INSERT INTO order_item VALUES (1, 5, 3, NULL);
INSERT INTO order_item VALUES (1, 2, 1, '케첩 많이');
INSERT INTO order_item VALUES (2, 3, 1, NULL);
INSERT INTO order_item VALUES (2, 6, 2, NULL);
INSERT INTO order_item VALUES (4, 2, 1, NULL);
INSERT INTO order_item VALUES (4, 7, 4, '얼음 적게');
INSERT INTO order_item VALUES (4, 10, 2, '덜 맵게');

COMMIT;

-- 테이블 삭제 (자식 테이블부터)
-- DROP TABLE order_item;
-- DROP TABLE orders;
-- DROP TABLE menu;
-- DROP TABLE category;
-- DROP TABLE seat;
-- DROP TABLE staff;
