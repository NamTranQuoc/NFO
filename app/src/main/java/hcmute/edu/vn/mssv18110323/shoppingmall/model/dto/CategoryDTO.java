package hcmute.edu.vn.mssv18110323.shoppingmall.model.dto;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import hcmute.edu.vn.mssv18110323.shoppingmall.utils.Const;

public class CategoryDTO implements Serializable {
    private Long categoryId;
    private String image;
    private String name;
    private boolean isDeleted;

    public CategoryDTO(ResultSet resultSet) {
        try {
            categoryId = resultSet.getLong("CATEGORY_ID");
            image = resultSet.getString("IMAGE");
            name = resultSet.getString("NAME");
            isDeleted = resultSet.getBoolean("IS_DELETED");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public CategoryDTO() {
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getImage() {
        if (image != null && image != "") return image;
        return Const.no_image;
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
