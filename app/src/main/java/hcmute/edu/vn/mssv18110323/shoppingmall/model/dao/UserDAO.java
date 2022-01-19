package hcmute.edu.vn.mssv18110323.shoppingmall.model.dao;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hcmute.edu.vn.mssv18110323.shoppingmall.model.dto.UserDTO;
import hcmute.edu.vn.mssv18110323.shoppingmall.utils.DatabaseUtils;
import hcmute.edu.vn.mssv18110323.shoppingmall.utils.GenerateUtils;
import hcmute.edu.vn.mssv18110323.shoppingmall.utils.HashUtils;
import hcmute.edu.vn.mssv18110323.shoppingmall.utils.MailUtils;

public class UserDAO {
    private static UserDAO instance = null;

    private UserDAO() {
    }

    public static UserDAO getInstance() {
        if (instance == null) {
            instance = new UserDAO();
        }
        return instance;
    }

    public UserDTO getSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        String sUser = sharedPreferences.getString("user", "");
        try {
            return new Gson().fromJson(sUser, UserDTO.class);
        } catch (Exception e) {
            return null;
        }
    }

    public ArrayList<UserDTO> gets(Context context) {
        ArrayList<UserDTO> result = new ArrayList<>();

        String query = "SELECT * FROM user WHERE IS_DELETED = false;";
        ResultSet resultSet = DatabaseUtils.executeQuery(query, null, context);

        if (resultSet == null) {
            return result;
        }

        try {
            while (resultSet.next()) {
                UserDTO userModel = new UserDTO(resultSet);
                result.add(userModel);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return result;
    }

    public UserDTO getById(Long id, Context context) {
        String query = "SELECT * FROM user WHERE USER_ID = ? AND IS_DELETED = false";
        ResultSet resultSet = DatabaseUtils.executeQuery(query, Arrays.asList(id), context);
        try {
            if (resultSet != null && resultSet.next()) {
                return new UserDTO(resultSet);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public Long insert(UserDTO dto, Context context) {
        String query = "INSERT INTO user(PHONE_NUMBER, EMAIL, PASSWORD, USER_TYPE, NAME, GENDER, DOB, STATUS, AVATAR)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

        List<Object> parameters = Arrays.asList(
                dto.getPhoneNumber(),
                dto.getEmail(),
                dto.getPassword(),
                dto.getUserType(),
                dto.getName(),
                dto.getGender(),
                dto.getdOB(),
                dto.getStatus(),
                dto.getAvatar()
        );
        return (Long) DatabaseUtils.executeUpdateAutoIncrement(query, parameters, context);
    }

    public int update(UserDTO dto, Context context) {
        String query = "UPDATE user SET PHONE_NUMBER = ?, EMAIL = ?, USER_TYPE = ?, NAME = ?, GENDER = ?, DOB = ?, STATUS = ?, AVATAR = ? WHERE USER_ID = ?;";

        List<Object> parameters = Arrays.asList(
                dto.getPhoneNumber() != null ? dto.getPhoneNumber() : "",
                dto.getEmail() != null ? dto.getEmail() : "",
                dto.getUserType(),
                dto.getName() != null ? dto.getName() : "",
                dto.getGender() != null ? dto.getGender() : "",
                dto.getdOB() != null ? dto.getdOB() : 958148418,
                dto.getStatus(),
                dto.getAvatar() != null ? dto.getAvatar() : "",
                dto.getUserId()
        );
        return DatabaseUtils.executeUpdate(query, parameters, context);
    }

    public int delete(Long id, Context context) {
        String query = "UPDATE user SET STATUS = false, IS_DELETED = true WHERE USER_ID = ?;";
        return DatabaseUtils.executeUpdate(query, Arrays.asList(id), context);
    }

    public UserDTO signIn(String userName, String password, Context context) {
        String query = "SELECT * FROM user WHERE (PHONE_NUMBER = ? OR EMAIL = ?) AND PASSWORD = ? AND STATUS = true AND IS_DELETED = false;";
        List<Object> para = Arrays.asList(
                userName,
                userName,
                HashUtils.getMd5(password)
        );
        ResultSet resultSet = DatabaseUtils.executeQuery(query, para, context);
        try {
            if (resultSet != null && resultSet.next()) {
                return new UserDTO(resultSet);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public UserDTO signInV2(String userName, String passwordHash, Context context) {
        String query = "SELECT * FROM user WHERE (PHONE_NUMBER = ? OR EMAIL = ?) AND PASSWORD = ? AND STATUS = true AND IS_DELETED = false;";
        List<Object> para = Arrays.asList(
                userName,
                userName,
                passwordHash
        );
        ResultSet resultSet = DatabaseUtils.executeQuery(query, para, context);
        try {
            if (resultSet != null && resultSet.next()) {
                return new UserDTO(resultSet);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public UserDTO checkOTP(String otp, Long userId, Context context) {
        String query = "SELECT * FROM user WHERE USER_ID = ? AND OTP = ? AND TIME > DATE_SUB(CURRENT_TIMESTAMP, INTERVAL 2 MINUTE) AND IS_DELETED = false;";
        List<Object> para = Arrays.asList(
                userId,
                otp
        );
        ResultSet resultSet = DatabaseUtils.executeQuery(query, para, context);
        try {
            if (resultSet != null && resultSet.next()) {
                UserDTO user = new UserDTO(resultSet);
                this.updateStatus(user.getUserId(), true, context);
                return user;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public UserDTO sendOTPAndCreateUser(UserDTO userDTO, Context context) {
        Long id = 0L;
        String otp = GenerateUtils.oneTimePassword();
        String query = "SELECT * FROM user WHERE (PHONE_NUMBER = ? OR EMAIL = ?) AND STATUS = false AND IS_DELETED = false;";
        List<Object> para = Arrays.asList(
                userDTO.getPhoneNumber() != null ? userDTO.getPhoneNumber() : "",
                userDTO.getEmail() != null ? userDTO.getEmail() : ""
        );
        ResultSet resultSet = DatabaseUtils.executeQuery(query, para, context);
        try {
            if (resultSet != null && resultSet.next()) {
                id = resultSet.getLong("USER_ID");
                userDTO.setUserId(id);
                query = "UPDATE user SET NAME = ?, OTP = ?, PHONE_NUMBER = ?, EMAIL = ?, PASSWORD = ?, DATE = CURRENT_TIMESTAMP WHERE USER_ID = ?;";
                DatabaseUtils.executeUpdate(query, Arrays.asList(userDTO.getName(), otp, userDTO.getPhoneNumber(), userDTO.getEmail(), HashUtils.getMd5(userDTO.getPassword()), id), context);
            } else {
                query = "INSERT INTO user(EMAIL, PASSWORD, NAME, OTP, PHONE_NUMBER)" +
                        "VALUES (?, ?, ?, ?, ?);";

                List<Object> para1 = Arrays.asList(
                        userDTO.getEmail(),
                        HashUtils.getMd5(userDTO.getPassword()),
                        userDTO.getName(),
                        otp,
                        userDTO.getPhoneNumber()
                );
                id = (Long) DatabaseUtils.executeUpdateAutoIncrement(query, para1, context);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        if (id != 0) {
            MailUtils mail = new MailUtils(context, userDTO.getEmail(), "Mã xác thực tạo tài  khoản", "OTP: " + otp);
            mail.execute();
            userDTO.setUserId(id);
            return userDTO;
        } else {
            query = "DELETE FROM user WHERE USER_ID = ?;";
            DatabaseUtils.executeQuery(query, Arrays.asList(id), context);
        }
        return null;
    }

    public boolean reSentOTP(UserDTO userDTO, Context context) {
        String otp = GenerateUtils.oneTimePassword();
        String query = "UPDATE user SET OTP = ?, TIME = CURRENT_TIMESTAMP WHERE USER_ID = ?;";
        if (DatabaseUtils.executeUpdate(query, Arrays.asList(otp, userDTO.getUserId()), context) != 0) {
            MailUtils mail = new MailUtils(context, userDTO.getEmail(), "Mã xác thực tạo tài  khoản", "OTP: " + otp);
            mail.execute();
            return true;
        }
        return false;
    }

    public UserDTO SentOTP(String username, Context context) {
        String otp = GenerateUtils.oneTimePassword();
        String query = "UPDATE user SET OTP = ?, TIME = CURRENT_TIMESTAMP WHERE (PHONE_NUMBER = ? OR EMAIL = ?) AND STATUS = true;";
        if (DatabaseUtils.executeUpdate(query, Arrays.asList(otp, username, username), context) != 0) {
            UserDTO userDTO = this.getByUsername(username, context);
            MailUtils mail = new MailUtils(context, userDTO.getEmail(), "Mã xác thực tạo tài  khoản", "OTP: " + otp);
            mail.execute();
            return userDTO;
        }
        return null;
    }

    public UserDTO getByUsername(String username, Context context) {
        String query = "SELECT * FROM user WHERE (PHONE_NUMBER = ? OR EMAIL = ?) AND STATUS = true AND IS_DELETED = false;";
        ResultSet resultSet = DatabaseUtils.executeQuery(query, Arrays.asList(username, username), context);
        try {
            if (resultSet != null && resultSet.next()) {
                return new UserDTO(resultSet);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public boolean existUserName(String email, String phone, Context context) {
        String query = "SELECT * FROM user WHERE (PHONE_NUMBER = ? OR EMAIL = ?) AND STATUS = true AND IS_DELETED = false;";
        List<Object> para = Arrays.asList(
                phone,
                email
        );
        ResultSet resultSet = DatabaseUtils.executeQuery(query, para, context);
        try {
            if (resultSet != null && resultSet.next()) {
                return true;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public boolean existUserName(String phone, Context context) {
        String query = "SELECT * FROM user WHERE PHONE_NUMBER = ? AND STATUS = true AND IS_DELETED = false;";
        List<Object> para = Arrays.asList(
                phone
        );
        ResultSet resultSet = DatabaseUtils.executeQuery(query, para, context);
        try {
            if (resultSet != null && resultSet.next()) {
                return true;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return false;
    }

    public boolean updateStatus(Long userId, boolean status, Context context) {
        String query = "UPDATE user SET STATUS=? WHERE USER_ID=?;";
        List<Object> para = Arrays.asList(
                status == true ? "1" : "0",
                userId
        );
        Integer result = DatabaseUtils.executeUpdate(query, para, context);
        if (result != 0) {
            return true;
        }
        return false;
    }

    public boolean updatePassword(Long id, String newPass, Context context) {
        String query = "UPDATE user SET PASSWORD = ? WHERE USER_ID = ?;";

        List<Object> parameters = Arrays.asList(
                HashUtils.getMd5(newPass),
                id
        );
        return DatabaseUtils.executeUpdate(query, parameters, context) != 0 ? true : false;
    }
}
