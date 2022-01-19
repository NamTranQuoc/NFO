package hcmute.edu.vn.mssv18110323.shoppingmall.model.dto;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CartDTO implements Serializable {
    private Long userId;
    private Long productId;
    private Long productTypeId;
    private Long quantity;
    private Integer status;
    private boolean isDeleted;

    //các giá trị truy vấn từ bảng khác
    private Long price;
    private String nameProduct;
    private String nameType;
    private String image;
    private Long stock;

    //không lưu db
    private boolean select = true;

    public CartDTO() {
    }

    public CartDTO(ResultSet resultSet) {
        try {
            userId = resultSet.getLong("USER_ID");
            productId = resultSet.getLong("PRODUCT_ID");
            productTypeId = resultSet.getLong("PRODUCT_TYPE_ID");
            quantity = resultSet.getLong("QUANTITY");
            status = resultSet.getInt("STATUS");
            isDeleted = resultSet.getBoolean("IS_DELETED");
            price = resultSet.getLong("PRICE");
            nameProduct = resultSet.getString("NAME_PRODUCT");
            nameType = resultSet.getString("NAME_TYPE");
            image = resultSet.getString("IMAGE");
            stock = resultSet.getLong("STOCK");
        } catch (SQLException E) {
            E.printStackTrace();
        }
    }

    public String getNameProductMini() {
        return nameProduct.length() < 59 ? nameProduct : nameProduct.substring(0, 56) + " ...";
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getStock() {
        return stock;
    }

    public void setStock(Long stock) {
        this.stock = stock;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public String getNameType() {
        return nameType;
    }

    public void setNameType(String nameType) {
        this.nameType = nameType;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(Long productTypeId) {
        this.productTypeId = productTypeId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
