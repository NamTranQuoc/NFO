package hcmute.edu.vn.mssv18110323.shoppingmall.model.dao;

import android.content.Context;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hcmute.edu.vn.mssv18110323.shoppingmall.model.dto.ProductDTO;
import hcmute.edu.vn.mssv18110323.shoppingmall.model.dto.ProductTypeDTO;
import hcmute.edu.vn.mssv18110323.shoppingmall.utils.Base64Utils;
import hcmute.edu.vn.mssv18110323.shoppingmall.utils.Const;
import hcmute.edu.vn.mssv18110323.shoppingmall.utils.DatabaseUtils;

public class ProductDAO {
    private static ProductDAO instance = null;

    private ProductDAO() {
    }

    public static ProductDAO getInstance() {
        if (instance == null) {
            instance = new ProductDAO();
        }
        return instance;
    }

    public ArrayList<ProductDTO> gets(Context context) {
        ArrayList<ProductDTO> result = new ArrayList<>();

        String query = "SELECT * FROM product WHERE IS_DELETED = false;";
        ResultSet resultSet = DatabaseUtils.executeQuery(query, null, context);

        if (resultSet == null) {
            return result;
        }

        try {
            while (resultSet.next()) {
                ProductDTO model = new ProductDTO(resultSet);
                result.add(model);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return result;
    }

    public ArrayList<ProductDTO> search(String keyword, Context context) {
        ArrayList<ProductDTO> result = new ArrayList<>();

        String query = "SELECT product.* FROM product, category, brand WHERE product.`BRAND_ID` = brand.`BRAND_ID` AND product.`CATEGORY_ID` = category.`CATEGORY_ID` AND product.`IS_DELETED` = FALSE AND (product.`NAME` LIKE ? OR  brand.`NAME` LIKE ? OR category.`NAME` LIKE ?) ORDER BY product.PRODUCT_ID DESC;";
        keyword = "%" + keyword + "%";
        ResultSet resultSet = DatabaseUtils.executeQuery(query, Arrays.asList(keyword, keyword, keyword), context);

        if (resultSet == null) {
            return result;
        }

        try {
            while (resultSet.next()) {
                ProductDTO model = new ProductDTO(resultSet);
                result.add(model);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return result;
    }

    public ArrayList<ProductDTO> search(String keyword, int page, Context context) {
        ArrayList<ProductDTO> result = new ArrayList<>();

        String query = "SELECT product.* FROM product, category, brand WHERE product.`BRAND_ID` = brand.`BRAND_ID` AND product.`CATEGORY_ID` = category.`CATEGORY_ID` AND product.`IS_DELETED` = FALSE AND (product.`NAME` LIKE ? OR  brand.`NAME` LIKE ? OR category.`NAME` LIKE ?) ORDER BY product.PRODUCT_ID DESC LIMIT ?, ?;";
        keyword = "%" + keyword + "%";
        ResultSet resultSet = DatabaseUtils.executeQuery(query, Arrays.asList(keyword, keyword, keyword, (page - 1) * Const.size, Const.size), context);

        if (resultSet == null) {
            return result;
        }

        try {
            while (resultSet.next()) {
                ProductDTO model = new ProductDTO(resultSet);
                result.add(model);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return result;
    }

    public ProductDTO getById(Long id, Context context) {
        String query = "SELECT * FROM product WHERE PRODUCT_ID = ? AND IS_DELETED = false";
        ResultSet resultSet = DatabaseUtils.executeQuery(query, Arrays.asList(id), context);
        try {
            if (resultSet != null && resultSet.next()) {
                return new ProductDTO(resultSet);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public int delete(Long id, Context context) {
        String query = "UPDATE product SET IS_DELETED = true WHERE PRODUCT_ID = ?;";
        return DatabaseUtils.executeUpdate(query, Arrays.asList(id), context);
    }

    public int upsert(ProductDTO productDTO, Context context) {
        if (productDTO.getProductId() != null) {
            //update
            String query = "UPDATE `product` SET `NAME` = ?, `CATEGORY_ID` = ?, `BRAND_ID` = ?, `IMAGE_1` = ?, `IMAGE_2` = ?, `IMAGE_3` = ?, `DESC` = ?, `PRICE` = ? WHERE `PRODUCT_ID` = ?;";
            int result = DatabaseUtils.executeUpdate(query, Arrays.asList(productDTO.getName(), productDTO.getCategoryId(), productDTO.getBrandId(), productDTO.getImage1(), productDTO.getImage2(), productDTO.getImage3(), Base64Utils.stringToBase64(productDTO.getDesc()), productDTO.getPrice(), productDTO.getProductId()), context);
            if (result > 0) {
                query = "UPDATE `product_type` SET `IS_DELETED` = true WHERE `PRODUCT_ID` = ?;";
                DatabaseUtils.executeUpdate(query, Arrays.asList(productDTO.getProductId()), context);
                query = "INSERT INTO `product_type` (`PRODUCT_ID`, `NAME`, `QUANTITY`) VALUES ";
                List<Object> para = new ArrayList<>();
                for (ProductTypeDTO productTypeDTO : productDTO.getProductTypeDTOS()) {
                    query += "(?, ?, ?), ";
                    para.add(productDTO.getProductId());
                    para.add(productTypeDTO.getProductTypeName());
                    para.add(productTypeDTO.getQuantity());
                }
                query = query.substring(0, query.length() - 2) + ";";
                DatabaseUtils.executeUpdate(query, para, context);
            }
            return result;
        } else {
            //insert
            String query = "INSERT INTO `product` (`IMAGE_1`, `IMAGE_2`, `IMAGE_3`, `NAME`, `PRICE`, `DESC`, `BRAND_ID`, `CATEGORY_ID`) VALUE (?, ?, ?, ?, ?, ?, ?, ?);";
            Long result = (Long) DatabaseUtils.executeUpdateAutoIncrement(query, Arrays.asList(productDTO.getImage1(), productDTO.getImage2(), productDTO.getImage3(), productDTO.getName(), productDTO.getPrice(), Base64Utils.stringToBase64(productDTO.getDesc()), productDTO.getBrandId(), productDTO.getCategoryId()), context);
            if (result > 0) {
                query = "INSERT INTO `product_type` (`PRODUCT_ID`, `NAME`, `QUANTITY`) VALUES ";
                List<Object> para = new ArrayList<>();
                for (ProductTypeDTO productTypeDTO : productDTO.getProductTypeDTOS()) {
                    query += "(?, ?, ?), ";
                    para.add(result);
                    para.add(productTypeDTO.getProductTypeName());
                    para.add(productTypeDTO.getQuantity());
                }
                query = query.substring(0, query.length() - 2) + ";";
                DatabaseUtils.executeUpdate(query, para, context);
            }
            return 1;
        }
    }
}
