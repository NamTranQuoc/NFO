package hcmute.edu.vn.mssv18110323.shoppingmall.model.dao;

import android.content.Context;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import hcmute.edu.vn.mssv18110323.shoppingmall.model.dto.CategoryDTO;
import hcmute.edu.vn.mssv18110323.shoppingmall.utils.DatabaseUtils;

public class CategoryDAO {
    private static CategoryDAO instance = null;

    private CategoryDAO() {
    }

    public static CategoryDAO getInstance() {
        if (instance == null) {
            instance = new CategoryDAO();
        }
        return instance;
    }

    public ArrayList<CategoryDTO> gets(Context context) {
        ArrayList<CategoryDTO> result = new ArrayList<>();

        String query = "SELECT * FROM category WHERE IS_DELETED = false;";
        ResultSet resultSet = DatabaseUtils.executeQuery(query, null, context);

        if (resultSet == null) {
            return result;
        }

        try {
            while (resultSet.next()) {
                CategoryDTO userModel = new CategoryDTO(resultSet);
                result.add(userModel);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return result;
    }
}
