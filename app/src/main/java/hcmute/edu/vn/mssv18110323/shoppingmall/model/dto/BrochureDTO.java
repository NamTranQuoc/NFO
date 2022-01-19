package hcmute.edu.vn.mssv18110323.shoppingmall.model.dto;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BrochureDTO implements Serializable {
    private Long brochureId;
    private String image;
    private boolean isDeleted;

    public BrochureDTO(ResultSet resultSet) {
        try {
            brochureId = resultSet.getLong("BROCHURE_ID");
            image = resultSet.getString("IMAGE");
            isDeleted = resultSet.getBoolean("IS_DELETED");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public BrochureDTO() {
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Long getBrochureId() {
        return brochureId;
    }

    public void setBrochureId(Long brochureId) {
        this.brochureId = brochureId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
