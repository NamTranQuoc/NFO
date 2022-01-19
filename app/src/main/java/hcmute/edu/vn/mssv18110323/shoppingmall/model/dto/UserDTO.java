package hcmute.edu.vn.mssv18110323.shoppingmall.model.dto;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;

import hcmute.edu.vn.mssv18110323.shoppingmall.utils.Const;

public class UserDTO implements Serializable {
    private Long userId;
    private String phoneNumber;
    private String email;
    private String password;
    private String userType;
    private String name;
    private Boolean gender;
    private Long dOB;
    private Boolean status;
    private String avatar;
    private boolean isDeleted;

    public UserDTO(ResultSet resultSet) {
        try {
            userId = resultSet.getLong("USER_ID");
            phoneNumber = resultSet.getString("PHONE_NUMBER");
            email = resultSet.getString("EMAIL");
            password = resultSet.getString("PASSWORD");
            userType = resultSet.getString("USER_TYPE");
            name = resultSet.getString("NAME");
            gender = resultSet.getBoolean("GENDER");
            dOB = resultSet.getLong("DOB");
            status = resultSet.getBoolean("STATUS");
            avatar = resultSet.getString("AVATAR");
            isDeleted = resultSet.getBoolean("IS_DELETED");

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }


    public UserDTO() {
    }

    public UserDTO(String phoneNumber, String email, String password, String userType, String name, Boolean gender, Long dOB, Boolean status, String avatar) {
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.userType = userType;
        this.name = name;
        this.gender = gender;
        this.dOB = dOB;
        this.status = status;
        this.avatar = avatar;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public Long getdOB() {
        return dOB;
    }

    public void setdOB(Long dOB) {
        this.dOB = dOB;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getAvatar() {
        if (avatar != null && avatar != "") return avatar;
        return Const.no_avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
