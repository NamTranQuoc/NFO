package hcmute.edu.vn.mssv18110323.shoppingmall.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import hcmute.edu.vn.mssv18110323.shoppingmall.R;
import hcmute.edu.vn.mssv18110323.shoppingmall.adapter.ProductManageAdapter;

public class ProductManagerActivity extends Navigate {

    RecyclerView rvProduct;
    LinearLayoutManager layoutManager;
    ProductManageAdapter adapter;
    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_product_manager);
        super.onCreate(savedInstanceState);

        mapping();
        loadData();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddOrEditProductActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.reloadData();
    }

    private void mapping() {
        rvProduct = (RecyclerView) findViewById(R.id.rvProduct);
        btnAdd = (Button) findViewById(R.id.btnAdd);

        menu.findItem(R.id.navHistoryCart).setVisible(false);
        menu.findItem(R.id.navHome).setVisible(false);
        menu.findItem(R.id.navInformation).setVisible(false);
        menu.findItem(R.id.navChangePassword).setVisible(false);
    }

    private void loadData() {
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rvProduct.setLayoutManager(layoutManager);
        adapter = new ProductManageAdapter(this, "");
        rvProduct.setAdapter(adapter);
    }
}