package hcmute.edu.vn.mssv18110323.shoppingmall.model.dao;

import android.content.Context;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hcmute.edu.vn.mssv18110323.shoppingmall.model.dto.ProductTypeDTO;
import hcmute.edu.vn.mssv18110323.shoppingmall.utils.DatabaseUtils;

public class ProductTypeDAO {
    private static ProductTypeDAO instance = null;

    private ProductTypeDAO() {
    }

    public static ProductTypeDAO getInstance() {
        if (instance == null) {
            instance = new ProductTypeDAO();
        }
        return instance;
    }

    public List<ProductTypeDTO> getByProductId(Long productId, Context context) {
        ArrayList<ProductTypeDTO> result = new ArrayList<>();

        String query = "SELECT * FROM PRODUCT_TYPE WHERE PRODUCT_ID = ? AND IS_DELETED = false;";
        ResultSet resultSet = DatabaseUtils.executeQuery(query, Arrays.asList(productId), context);

        if (resultSet == null) {
            return result;
        }

        try {
            while (resultSet.next()) {
                ProductTypeDTO model = new ProductTypeDTO(resultSet);
                result.add(model);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return result;
    }
}
