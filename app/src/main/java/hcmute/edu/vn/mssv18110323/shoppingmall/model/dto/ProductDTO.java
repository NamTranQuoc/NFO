package hcmute.edu.vn.mssv18110323.shoppingmall.model.dto;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import hcmute.edu.vn.mssv18110323.shoppingmall.utils.Base64Utils;

public class ProductDTO implements Serializable {
    private Long productId;
    private String image1;
    private String image2;
    private String image3;
    private String name;
    private Long price;
    private String desc;
    private Long brandId;
    private Long categoryId;
    private List<ProductTypeDTO> productTypeDTOS;
    private Long quantity;
    private boolean isDeleted;

    public ProductDTO(ResultSet resultSet) {
        try {
            productId = resultSet.getLong("PRODUCT_ID");
            image1 = resultSet.getString("IMAGE_1");
            image2 = resultSet.getString("IMAGE_2");
            image3 = resultSet.getString("IMAGE_3");
            name = resultSet.getString("NAME");
            price = resultSet.getLong("PRICE");
            desc = resultSet.getString("DESC");
            brandId = resultSet.getLong("BRAND_ID");
            categoryId = resultSet.getLong("CATEGORY_ID");
            isDeleted = resultSet.getBoolean("IS_DELETED");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ProductDTO() {

    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Long getQuantity() {
        quantity = 0L;
        for (ProductTypeDTO p : this.productTypeDTOS) {
            quantity += p.getQuantity();
        }
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public List<ProductTypeDTO> getProductTypeDTOS() {
        return productTypeDTOS;
    }

    public void setProductTypeDTOS(List<ProductTypeDTO> productTypeDTOS) {
        this.productTypeDTOS = productTypeDTOS;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getName() {
        return name;
    }

    public String getNameMini() {
        return name.length() < 23 ? name : name.substring(0, 20) + " ...";
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getDesc() {
        return Base64Utils.base64ToString(this.desc);
    }

    public void setDesc(String desc) {
        this.desc = Base64Utils.stringToBase64(desc);
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }
}
