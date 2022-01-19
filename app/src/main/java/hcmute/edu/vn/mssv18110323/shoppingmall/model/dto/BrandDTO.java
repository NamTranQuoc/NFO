package hcmute.edu.vn.mssv18110323.shoppingmall.model.dto;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BrandDTO implements Serializable {
    private Long brandId;
    private String image;
    private String name;
    private boolean isDeleted;

    public BrandDTO(ResultSet resultSet) {
        try {
            brandId = resultSet.getLong("BRAND_ID");
            image = resultSet.getString("IMAGE");
            name = resultSet.getString("NAME");
            isDeleted = resultSet.getBoolean("IS_DELETED");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public BrandDTO() {
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
