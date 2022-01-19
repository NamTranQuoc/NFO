package hcmute.edu.vn.mssv18110323.shoppingmall.controller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import hcmute.edu.vn.mssv18110323.shoppingmall.R;
import hcmute.edu.vn.mssv18110323.shoppingmall.adapter.ProductTypeManageAdapter;
import hcmute.edu.vn.mssv18110323.shoppingmall.model.dao.BrandDAO;
import hcmute.edu.vn.mssv18110323.shoppingmall.model.dao.CategoryDAO;
import hcmute.edu.vn.mssv18110323.shoppingmall.model.dao.ProductDAO;
import hcmute.edu.vn.mssv18110323.shoppingmall.model.dao.ProductTypeDAO;
import hcmute.edu.vn.mssv18110323.shoppingmall.model.dto.BrandDTO;
import hcmute.edu.vn.mssv18110323.shoppingmall.model.dto.CategoryDTO;
import hcmute.edu.vn.mssv18110323.shoppingmall.model.dto.ProductDTO;
import hcmute.edu.vn.mssv18110323.shoppingmall.model.dto.ProductTypeDTO;
import hcmute.edu.vn.mssv18110323.shoppingmall.utils.Base64Utils;
import hcmute.edu.vn.mssv18110323.shoppingmall.utils.Const;

public class AddOrEditProductActivity extends AppCompatActivity {

    Spinner sCategory, sBrand;
    LinearLayoutManager layoutManager1;
    ProductTypeManageAdapter adapter;
    RecyclerView rvTypeProduct;
    ProductDTO product;
    ImageView btnAddType, img1, img2, img3;
    EditText name, price, desc;
    List<CategoryDTO> categoryDTOS;
    List<BrandDTO> brandDTOS;
    ArrayAdapter<CategoryDTO> categoryDTOArrayAdapter;
    ArrayAdapter<BrandDTO> brandDTOArrayAdapter;
    Button btnSave;
    boolean has_image = false;
    int select_image = 0;
    private static final int PICK_IMAGE_REQUEST = 71;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_edit_product);

        mapping();
        loadData();

        btnAddType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductTypeDTO productTypeDTO = new ProductTypeDTO();
                productTypeDTO.setProductTypeName("");
                productTypeDTO.setQuantity(1L);
                product.getProductTypeDTOS().add(productTypeDTO);
                adapter.notifyItemChanged(product.getProductTypeDTOS().size(), product.getProductTypeDTOS());
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    for (ProductTypeDTO productTypeDTO : product.getProductTypeDTOS()) {
                        if (productTypeDTO.getProductTypeName().equals("")) {
                            throw new Exception("Vui lòng nhập đầy đủ thông tin loại sản phẩm");
                        }
                        productTypeDTO.setProductId(product.getProductId());
                    }
                    if (name.getText().toString().trim().equals("") || name.getText().toString() == null) {
                        throw new Exception("Vui lòng nhập tên sản phẩm");
                    }
                    if (!has_image) {
                        throw new Exception("Vui lòng chọn ít nhất một ảnh");
                    }
                    if (price.getText().toString().trim().equals("")) {
                        throw new Exception("Vui lòng nhập giá sản phẩm");
                    }
                    if (desc.getText().toString().trim().equals("")) {
                        throw new Exception("Vui lòng nhập mô tả");
                    }

                    product.setName(name.getText().toString().trim());
                    CategoryDTO categoryDTO = (CategoryDTO) sCategory.getSelectedItem();
                    product.setCategoryId(categoryDTO.getCategoryId());
                    BrandDTO brandDTO = (BrandDTO) sBrand.getSelectedItem();
                    product.setBrandId(brandDTO.getBrandId());
                    product.setImage1(Base64Utils.bitmapToString((((BitmapDrawable) img1.getDrawable()).getBitmap())));
                    product.setImage2(Base64Utils.bitmapToString((((BitmapDrawable) img2.getDrawable()).getBitmap())));
                    product.setImage3(Base64Utils.bitmapToString((((BitmapDrawable) img3.getDrawable()).getBitmap())));
                    product.setPrice(Long.parseLong(price.getText().toString()));
                    product.setDesc(desc.getText().toString());
                    int result = ProductDAO.getInstance().upsert(product, AddOrEditProductActivity.this);
                    if (result > 0) {
                        Intent intent = new Intent(getApplicationContext(), ProductManagerActivity.class);
                        startActivity(intent);
                        throw new Exception("Lưu sản phẩm thành công");
                    } else {
                        throw new Exception("Lưu sản phẩm thất bại");
                    }
                } catch (Exception e) {
                    Toast.makeText(AddOrEditProductActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_image = 1;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Chọn ảnh"), PICK_IMAGE_REQUEST);
            }
        });

        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_image = 2;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Chọn ảnh"), PICK_IMAGE_REQUEST);
            }
        });

        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_image = 3;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Chọn ảnh"), PICK_IMAGE_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath;
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                if (!has_image) {
                    img1.setImageBitmap(bitmap);
                    has_image = true;
                } else {
                    switch (select_image) {
                        case 2:
                            img2.setImageBitmap(bitmap);
                            break;
                        case 3:
                            img3.setImageBitmap(bitmap);
                            break;
                        default:
                            img1.setImageBitmap(bitmap);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void mapping() {
        sCategory = (Spinner) findViewById(R.id.sCategory);
        sBrand = (Spinner) findViewById(R.id.sBrand);
        rvTypeProduct = (RecyclerView) findViewById(R.id.rvTypeProduct);
        btnAddType = (ImageView) findViewById(R.id.btnAddType);
        name = (EditText) findViewById(R.id.txtName);
        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img2);
        img3 = (ImageView) findViewById(R.id.img3);
        btnSave = (Button) findViewById(R.id.btnSave);
        price = (EditText) findViewById(R.id.txtPrice);
        desc = (EditText) findViewById(R.id.txtDesc);
    }

    private void loadData() {
        categoryDTOS = CategoryDAO.getInstance().gets(AddOrEditProductActivity.this);
        categoryDTOArrayAdapter = new ArrayAdapter<CategoryDTO>(AddOrEditProductActivity.this, R.layout.support_simple_spinner_dropdown_item, categoryDTOS);
        sCategory.setAdapter(categoryDTOArrayAdapter);

        brandDTOS = BrandDAO.getInstance().gets(AddOrEditProductActivity.this);
        brandDTOArrayAdapter = new ArrayAdapter<BrandDTO>(AddOrEditProductActivity.this, R.layout.support_simple_spinner_dropdown_item, brandDTOS);
        sBrand.setAdapter(brandDTOArrayAdapter);

        img1.setImageBitmap(Base64Utils.stringToBitmap(Const.add_image));
        img2.setImageBitmap(Base64Utils.stringToBitmap(Const.add_image));
        img3.setImageBitmap(Base64Utils.stringToBitmap(Const.add_image));


        layoutManager1 = new LinearLayoutManager(this);
        layoutManager1.setOrientation(RecyclerView.VERTICAL);
        rvTypeProduct.setLayoutManager(layoutManager1);
        Long product_id = (Long) getIntent().getSerializableExtra("product_id");
        product = ProductDAO.getInstance().getById(product_id, AddOrEditProductActivity.this);
        if (product != null) {
            product.setProductTypeDTOS(ProductTypeDAO.getInstance().getByProductId(product.getProductId(), this));
            adapter = new ProductTypeManageAdapter(this, product.getProductTypeDTOS());
            name.setText(product.getName());
            sCategory.setSelection(getIndexCategory(product.getCategoryId()));
            sBrand.setSelection(getIndexBrand(product.getBrandId()));
            img1.setImageBitmap(Base64Utils.stringToBitmap(product.getImage1()));
            if (product.getImage2() != null) {
                img2.setImageBitmap(Base64Utils.stringToBitmap(product.getImage2()));
            }
            if (product.getImage3() != null) {
                img3.setImageBitmap(Base64Utils.stringToBitmap(product.getImage3()));
            }
            price.setText(product.getPrice().toString());
            desc.setText(product.getDesc());
            has_image = true;
        } else {
            ProductTypeDTO productTypeDTO = new ProductTypeDTO();
            productTypeDTO.setProductTypeName("Mặc định");
            productTypeDTO.setQuantity(1L);
            product = new ProductDTO();
            product.setProductTypeDTOS(new ArrayList<>());
            product.getProductTypeDTOS().add(productTypeDTO);
            adapter = new ProductTypeManageAdapter(this, product.getProductTypeDTOS());
        }
        rvTypeProduct.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private int getIndexCategory(Long id) {
        for (int i = 0; i < categoryDTOArrayAdapter.getCount(); i++) {
            if (categoryDTOArrayAdapter.getItem(i).getCategoryId().equals(id)) {
                return i;
            }
        }
        return 0;
    }

    private int getIndexBrand(Long id) {
        for (int i = 0; i < brandDTOArrayAdapter.getCount(); i++) {
            if (brandDTOArrayAdapter.getItem(i).getBrandId().equals(id)) {
                return i;
            }
        }
        return 0;
    }
}