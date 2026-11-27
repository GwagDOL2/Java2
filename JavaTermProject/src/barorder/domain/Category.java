package barorder.domain;

// 메뉴분류 (category 테이블 한 행)
public class Category {
    private String categoryCode;
    private String categoryName;

    public String getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    // 콤보박스에 보여줄 글자
    @Override
    public String toString() {
        return categoryName;
    }
}
