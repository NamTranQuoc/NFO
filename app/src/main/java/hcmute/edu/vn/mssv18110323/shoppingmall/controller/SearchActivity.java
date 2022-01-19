package hcmute.edu.vn.mssv18110323.shoppingmall.controller;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import hcmute.edu.vn.mssv18110323.shoppingmall.R;
import hcmute.edu.vn.mssv18110323.shoppingmall.adapter.ProductAdapter;

public class SearchActivity extends Navigate {
    GridLayoutManager gridLayoutManager;
    RecyclerView rvProduct;
    ProductAdapter adapterProduct;
    TextView nextPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_search);
        super.onCreate(savedInstanceState);

        mapping();
        loadData();

        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterProduct.getNextPage(txtSearch.getText().toString());
            }
        });
    }

    private void mapping() {
        rvProduct = (RecyclerView) findViewById(R.id.rvProduct);
        nextPage = (TextView) findViewById(R.id.nextPage);
    }

    private void loadData() {
        txtSearch.setText(this.getIntent().getStringExtra("keyword"));
        gridLayoutManager = new GridLayoutManager(this, 2);
        gridLayoutManager.setOrientation(RecyclerView.VERTICAL);
        rvProduct.setLayoutManager(gridLayoutManager);
        search();
    }

    @Override
    protected void search() {
        loadSearch();
    }

    private void loadSearch() {
        adapterProduct = new ProductAdapter(this, user, txtSearch.getText().toString(), nextPage);
        rvProduct.setAdapter(adapterProduct);
        adapterProduct.notifyDataSetChanged();
    }
}