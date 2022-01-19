package hcmute.edu.vn.mssv18110323.shoppingmall.controller;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.List;

import hcmute.edu.vn.mssv18110323.shoppingmall.R;
import hcmute.edu.vn.mssv18110323.shoppingmall.adapter.BrandAdapter;
import hcmute.edu.vn.mssv18110323.shoppingmall.adapter.CategoryAdapter;
import hcmute.edu.vn.mssv18110323.shoppingmall.adapter.ProductAdapter;
import hcmute.edu.vn.mssv18110323.shoppingmall.model.dao.BrochureDAO;
import hcmute.edu.vn.mssv18110323.shoppingmall.model.dto.BrochureDTO;
import hcmute.edu.vn.mssv18110323.shoppingmall.utils.Base64Utils;

public class MainActivity extends Navigate {

    CarouselView brochure;
    RecyclerView rvCategory, rvBrand, rvProduct;
    CategoryAdapter adapterCategory;
    BrandAdapter adapterBrand;
    ProductAdapter adapterProduct;
    LinearLayoutManager layoutManager, layoutManager1;
    GridLayoutManager gridLayoutManager;
    ScrollView scvMain;
    TextView nextPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);

        this.mapping();
        this.loadData();

        initCarouseView();

        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterProduct.getNextPage("");
            }
        });
    }

    private void mapping() {
        brochure = (CarouselView) findViewById(R.id.brochure);
        rvCategory = (RecyclerView) findViewById(R.id.rvCategory);
        rvBrand = (RecyclerView) findViewById(R.id.rvBrand);
        rvProduct = (RecyclerView) findViewById(R.id.rvProduct);
        scvMain = (ScrollView) findViewById(R.id.scvMain);
        nextPage = (TextView) findViewById(R.id.nextPage);

        menu.findItem(R.id.navHome).setVisible(false);
    }

    private void loadData() {
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rvCategory.setLayoutManager(layoutManager);
        adapterCategory = new CategoryAdapter(this);
        rvCategory.setAdapter(adapterCategory);
        adapterCategory.notifyDataSetChanged();

        layoutManager1 = new LinearLayoutManager(this);
        layoutManager1.setOrientation(RecyclerView.HORIZONTAL);
        rvBrand.setLayoutManager(layoutManager1);
        adapterBrand = new BrandAdapter(this);
        rvBrand.setAdapter(adapterBrand);
        adapterBrand.notifyDataSetChanged();

        gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvProduct.setLayoutManager(gridLayoutManager);
        adapterProduct = new ProductAdapter(this, user, "", nextPage);
        rvProduct.setAdapter(adapterProduct);
        adapterProduct.notifyDataSetChanged();
    }

    public void initCarouseView() {
        List<BrochureDTO> brochureDTOS = BrochureDAO.getInstance().gets(this);
        brochure.setPageCount(brochureDTOS.size());
        brochure.setImageListener(new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageBitmap(Base64Utils.stringToBitmap(brochureDTOS.get(position).getImage()));
            }
        });
    }
}