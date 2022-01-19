package hcmute.edu.vn.mssv18110323.shoppingmall.model.dao;

import android.content.Context;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import hcmute.edu.vn.mssv18110323.shoppingmall.model.dto.BrochureDTO;
import hcmute.edu.vn.mssv18110323.shoppingmall.utils.DatabaseUtils;

public class BrochureDAO {
    private static BrochureDAO instance = null;

    private BrochureDAO() {
    }

    public static BrochureDAO getInstance() {
        if (instance == null) {
            instance = new BrochureDAO();
        }
        return instance;
    }

    public ArrayList<BrochureDTO> gets(Context context) {
        ArrayList<BrochureDTO> result = new ArrayList<>();

        String query = "SELECT * FROM brochure WHERE IS_DELETED = false;";
        ResultSet resultSet = DatabaseUtils.executeQuery(query, null, context);

        if (resultSet == null) {
            return result;
        }

        try {
            while (resultSet.next()) {
                BrochureDTO userModel = new BrochureDTO(resultSet);
                result.add(userModel);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return result;
    }
}
