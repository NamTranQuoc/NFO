package hcmute.edu.vn.mssv18110323.shoppingmall.model.dao;

import android.content.Context;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import hcmute.edu.vn.mssv18110323.shoppingmall.model.dto.BrandDTO;
import hcmute.edu.vn.mssv18110323.shoppingmall.utils.DatabaseUtils;

public class BrandDAO {
    private static BrandDAO instance = null;

    private BrandDAO() {
    }

    public static BrandDAO getInstance() {
        if (instance == null) {
            instance = new BrandDAO();
        }
        return instance;
    }

    public ArrayList<BrandDTO> gets(Context context) {
        ArrayList<BrandDTO> result = new ArrayList<>();

        String query = "SELECT * FROM BRAND WHERE IS_DELETED = false;";
        ResultSet resultSet = DatabaseUtils.executeQuery(query, null, context);

        if (resultSet == null) {
            return result;
        }

        try {
            while (resultSet.next()) {
                BrandDTO userModel = new BrandDTO(resultSet);
                result.add(userModel);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return result;
    }
}
