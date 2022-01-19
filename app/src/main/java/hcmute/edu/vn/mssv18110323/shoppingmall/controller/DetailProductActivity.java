package hcmute.edu.vn.mssv18110323.shoppingmall.controller;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.List;

import hcmute.edu.vn.mssv18110323.shoppingmall.R;
import hcmute.edu.vn.mssv18110323.shoppingmall.adapter.ProductTypeAdapter;
import hcmute.edu.vn.mssv18110323.shoppingmall.model.dao.CartDAO;
import hcmute.edu.vn.mssv18110323.shoppingmall.model.dao.ProductDAO;
import hcmute.edu.vn.mssv18110323.shoppingmall.model.dao.ProductTypeDAO;
import hcmute.edu.vn.mssv18110323.shoppingmall.model.dao.UserDAO;
import hcmute.edu.vn.mssv18110323.shoppingmall.model.dto.CartDTO;
import hcmute.edu.vn.mssv18110323.shoppingmall.model.dto.ProductDTO;
import hcmute.edu.vn.mssv18110323.shoppingmall.model.dto.UserDTO;
import hcmute.edu.vn.mssv18110323.shoppingmall.utils.Base64Utils;
import hcmute.edu.vn.mssv18110323.shoppingmall.utils.FormatUtils;

public class DetailProductActivity extends Navigate {

    CarouselView imgProduct;
    TextView name, price, desc, quantity;
    RecyclerView typeProducts;
    ProductDTO product;
    LinearLayoutManager layoutManager;
    ProductTypeAdapter adapterTypeProduct;
    EditText txtQuantity;
    ImageButton minusQuantity, plusQuantity, btnMenu, btnCart;
    Button addToCart;
    UserDTO userDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_detaill_product);
        super.onCreate(savedInstanceState);

        mapping();
        loadData();

        minusQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Long lQuantity = Long.parseLong(txtQuantity.getText().toString());
                if (lQuantity - 1 > 0) {
                    lQuantity -= 1;
                    txtQuantity.setText(lQuantity.toString());
                }
            }
        });

        plusQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Long lQuantity = Long.parseLong(txtQuantity.getText().toString());
                if (lQuantity + 1 <= Long.parseLong(quantity.getText().toString())) {
                    lQuantity += 1;
                    txtQuantity.setText(lQuantity.toString());
                }
            }
        });

        txtQuantity.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                checkQuantity();
                return false;
            }
        });

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartDTO cartDTO = new CartDTO();
                cartDTO.setUserId(userDTO.getUserId());
                cartDTO.setQuantity(Long.parseLong(txtQuantity.getText().toString()));
                cartDTO.setProductId(product.getProductId());
                cartDTO.setProductTypeId(adapterTypeProduct.getProductTypeId());
                int result = CartDAO.getInstance().upsert(cartDTO, DetailProductActivity.this);
                if (result > 0) {
                    Toast.makeText(DetailProductActivity.this, "Sản phẩm đã được thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DetailProductActivity.this, "Đã có lỗi xảy ra khi theo vào giỏ hàng", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkQuantity() {
        try {
            String str = txtQuantity.getText().toString();
            Long lQuantity = Long.parseLong(str);
            if (lQuantity <= 0) {
                txtQuantity.setText("1");
            } else if (lQuantity > Long.parseLong(quantity.getText().toString())) {
                txtQuantity.setText(quantity.getText().toString());
            }
        } catch (Exception e) {
            txtQuantity.setText("1");
        }

    }

    private void mapping() {
        imgProduct = (CarouselView) findViewById(R.id.imgProduct);
        name = (TextView) findViewById(R.id.lbProductName);
        price = (TextView) findViewById(R.id.lbPrice);
        typeProducts = (RecyclerView) findViewById(R.id.rvTypeProduct);
        desc = (TextView) findViewById(R.id.lbDesc);
        quantity = (TextView) findViewById(R.id.quantity);
        txtQuantity = (EditText) findViewById(R.id.txtQuantity);
        minusQuantity = (ImageButton) findViewById(R.id.subQuantity);
        plusQuantity = (ImageButton) findViewById(R.id.addQuantity);
        addToCart = (Button) findViewById(R.id.addToCart);
        btnCart = (ImageButton) findViewById(R.id.btnCart);
    }

    private void loadData() {
        Long product_id = (Long) getIntent().getSerializableExtra("product_id");
        product = ProductDAO.getInstance().getById(product_id, DetailProductActivity.this);
        product.setProductTypeDTOS(ProductTypeDAO.getInstance().getByProductId(product.getProductId(), this));

        //set ảnh
        List<String> listImg = new ArrayList<>();
        if (product.getImage1() != null && !product.getImage1().equals(""))
            listImg.add(product.getImage1());
        if (product.getImage2() != null && !product.getImage2().equals(""))
            listImg.add(product.getImage2());
        if (product.getImage3() != null && !product.getImage3().equals(""))
            listImg.add(product.getImage3());
        imgProduct.setPageCount(listImg.size());
        imgProduct.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageBitmap(Base64Utils.stringToBitmap(listImg.get(position)));
            }
        });
        //set tên
        name.setText(product.getName());
        //set giá
        price.setText(FormatUtils.getShowPrice(product.getPrice()));
        //set loại sản phẩn
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        typeProducts.setLayoutManager(layoutManager);
        adapterTypeProduct = new ProductTypeAdapter(product.getProductTypeDTOS(), quantity, txtQuantity, addToCart);
        typeProducts.setAdapter(adapterTypeProduct);
        adapterTypeProduct.notifyDataSetChanged();
        adapterTypeProduct.checkF();
        //set mô tả
        desc.setText(product.getDesc());
        //set số lượng
        quantity.setText(product.getQuantity().toString());
        //get user
        userDTO = UserDAO.getInstance().getSharedPreferences(DetailProductActivity.this);
    }
}