package hcmute.edu.vn.mssv18110323.shoppingmall.model.dao;

import android.content.Context;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hcmute.edu.vn.mssv18110323.shoppingmall.model.dto.CartDTO;
import hcmute.edu.vn.mssv18110323.shoppingmall.utils.Const;
import hcmute.edu.vn.mssv18110323.shoppingmall.utils.DatabaseUtils;

public class CartDAO {
    private static CartDAO instance = null;

    private CartDAO() {
    }

    public static CartDAO getInstance() {
        if (instance == null) {
            instance = new CartDAO();
        }
        return instance;
    }

    public List<CartDTO> getByUserId(Long userId, int status, Context context) {
        ArrayList<CartDTO> result = new ArrayList<>();

        String query = "SELECT cart.*, product.NAME AS 'NAME_PRODUCT', product.IMAGE_1 AS 'IMAGE', product_type.NAME AS 'NAME_TYPE', product.PRICE AS 'PRICE', product_type.QUANTITY AS 'STOCK' FROM cart, product, product_type WHERE USER_ID = ? AND product.PRODUCT_ID = cart.PRODUCT_ID AND product_type.PRODUCT_TYPE_ID = cart.PRODUCT_TYPE_ID AND cart.IS_DELETED = false AND STATUS = ?;";
        ResultSet resultSet = DatabaseUtils.executeQuery(query, Arrays.asList(userId, status), context);

        if (resultSet == null) {
            return result;
        }

        try {
            while (resultSet.next()) {
                CartDTO model = new CartDTO(resultSet);
                result.add(model);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return result;
    }

    public Long insert(CartDTO cartDTO, Context context) {
        String query = "INSERT INTO cart (USER_ID, PRODUCT_ID, PRODUCT_TYPE_ID, QUANTITY) VALUE (?, ?, ?, ?);";

        List<Object> parameters = Arrays.asList(
                cartDTO.getUserId(),
                cartDTO.getProductId(),
                cartDTO.getProductTypeId(),
                cartDTO.getQuantity()
        );
        return (Long) DatabaseUtils.executeUpdateAutoIncrement(query, parameters, context);
    }

    public int upsert(CartDTO cartDTO, Context context) {
        String query = "INSERT INTO `cart` (`USER_ID`, `PRODUCT_ID`, `PRODUCT_TYPE_ID`, `QUANTITY`, `STATUS`) VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE QUANTITY= QUANTITY + VALUES(QUANTITY);";
        List<Object> para = new ArrayList<>();
        para.add(cartDTO.getUserId());
        para.add(cartDTO.getProductId());
        para.add(cartDTO.getProductTypeId());
        para.add(cartDTO.getQuantity());
        para.add(Const.add_to_cart);
        return DatabaseUtils.executeUpdate(query, para, context);
    }

    public int upsertMany(List<CartDTO> cartDTOS, Context context) {
        String query = "INSERT INTO `cart` (`USER_ID`, `PRODUCT_ID`, `PRODUCT_TYPE_ID`, `QUANTITY`, `STATUS`) VALUES (?, ?, ?, ?, ?)";
        List<Object> para = new ArrayList<>();
        for (CartDTO cartDTO : cartDTOS) {
            para.add(cartDTO.getUserId());
            para.add(cartDTO.getProductId());
            para.add(cartDTO.getProductTypeId());
            para.add(cartDTO.getQuantity());
            para.add(Const.add_to_cart);
            query += ", (?, ?, ?, ?, ?)";
        }
        query = query.substring(0, query.length() - 17) + " ON DUPLICATE KEY UPDATE QUANTITY=VALUES(QUANTITY);";
        return DatabaseUtils.executeUpdate(query, para, context);
    }

    public int updateStatus(List<CartDTO> cartDTOS, int status, Context context) {
        String query = "INSERT INTO `cart` (`USER_ID`, `PRODUCT_ID`, `PRODUCT_TYPE_ID`, `QUANTITY`, `STATUS`) VALUES (?, ?, ?, ?, ?)";
        List<Object> para = new ArrayList<>();
        for (CartDTO cartDTO : cartDTOS) {
            para.add(cartDTO.getUserId());
            para.add(cartDTO.getProductId());
            para.add(cartDTO.getProductTypeId());
            para.add(cartDTO.getQuantity());
            para.add(Const.delivered);
            query += ", (?, ?, ?, ?, ?)";
        }
        query = query.substring(0, query.length() - 17) + " ON DUPLICATE KEY UPDATE QUANTITY=QUANTITY+VALUES(QUANTITY);";
        int result = DatabaseUtils.executeUpdate(query, para, context);
        if (result > 0) {
            query = "DELETE FROM `cart` WHERE (`USER_ID` = ? AND `PRODUCT_ID` = ? AND `PRODUCT_TYPE_ID` = ? AND `STATUS` = ?)";
            para = new ArrayList<>();
            for (CartDTO cartDTO : cartDTOS) {
                para.add(cartDTO.getUserId());
                para.add(cartDTO.getProductId());
                para.add(cartDTO.getProductTypeId());
                para.add(Const.add_to_cart);
                query += " OR (`USER_ID` = ? AND `PRODUCT_ID` = ? AND `PRODUCT_TYPE_ID` = ? AND `STATUS` = ?)";
            }
            query = query.substring(0, query.length() - 83) + ";";
            DatabaseUtils.executeUpdate(query, para, context);
            query = "UPDATE `PRODUCT_TYPE` SET `QUANTITY` = ? WHERE `PRODUCT_TYPE_ID` = ?;";
            for (CartDTO cartDTO : cartDTOS) {
                DatabaseUtils.executeUpdate(query, Arrays.asList(cartDTO.getStock() - cartDTO.getQuantity(), cartDTO.getProductTypeId()), context);
            }
        }
        return result;
    }

    public int delete(CartDTO cartDTO, Context context) {
        String query = "UPDATE `cart` SET IS_DELETED = true WHERE `USER_ID` = ? AND `PRODUCT_ID` = ? AND `PRODUCT_TYPE_ID` = ? AND `STATUS` = ?;";
        return DatabaseUtils.executeUpdate(query, Arrays.asList(cartDTO.getUserId(), cartDTO.getProductId(), cartDTO.getProductTypeId(), cartDTO.getStatus()), context);
    }
}
