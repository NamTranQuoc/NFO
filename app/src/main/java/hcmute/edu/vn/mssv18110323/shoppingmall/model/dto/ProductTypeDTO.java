package hcmute.edu.vn.mssv18110323.shoppingmall.model.dto;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductTypeDTO implements Serializable {
    private Long productTypeId;
    private Long productId;
    private String productTypeName;
    private Long quantity;
    private boolean isDeleted;

    public ProductTypeDTO(ResultSet resultSet) {
        try {
            productTypeId = resultSet.getLong("PRODUCT_TYPE_ID");
            productId = resultSet.getLong("PRODUCT_ID");
            productTypeName = resultSet.getString("NAME");
            quantity = resultSet.getLong("QUANTITY");
            isDeleted = resultSet.getBoolean("IS_DELETED");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ProductTypeDTO() {

    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Long getQuantity() {
        return quantity != null ? quantity : 0L;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(Long productTypeId) {
        this.productTypeId = productTypeId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductTypeName() {
        return productTypeName;
    }

    public void setProductTypeName(String productTypeName) {
        this.productTypeName = productTypeName;
    }
}
