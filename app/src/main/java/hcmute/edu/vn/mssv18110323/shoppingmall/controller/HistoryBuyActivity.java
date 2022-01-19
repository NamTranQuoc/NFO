package hcmute.edu.vn.mssv18110323.shoppingmall.controller;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import hcmute.edu.vn.mssv18110323.shoppingmall.R;
import hcmute.edu.vn.mssv18110323.shoppingmall.adapter.HistoryBuyAdapter;
import hcmute.edu.vn.mssv18110323.shoppingmall.model.dao.CartDAO;
import hcmute.edu.vn.mssv18110323.shoppingmall.model.dao.UserDAO;
import hcmute.edu.vn.mssv18110323.shoppingmall.model.dto.CartDTO;
import hcmute.edu.vn.mssv18110323.shoppingmall.model.dto.UserDTO;

public class HistoryBuyActivity extends Navigate {

    RecyclerView rvListItem;
    UserDTO userDTO;
    LinearLayoutManager layoutManager;
    HistoryBuyAdapter adapter;
    TextView lbShip;
    List<CartDTO> cartDTOS;
    Button btnReBuy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_history_buy);
        super.onCreate(savedInstanceState);

        mapping();
        loadData();

        btnReBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<CartDTO> cartDTOS1 = new ArrayList<>();
                for (CartDTO cartDTO : cartDTOS) {
                    if (cartDTO.isSelect()) {
                        cartDTO.setQuantity(1L);
                        cartDTOS1.add(cartDTO);
                    }
                }
                if (cartDTOS1.size() > 0) {
                    int result = CartDAO.getInstance().upsertMany(cartDTOS1, HistoryBuyActivity.this);
                    if (result > 0) {
                        Toast.makeText(HistoryBuyActivity.this, "Đã thêm sản phẩm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(HistoryBuyActivity.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(HistoryBuyActivity.this, "Chưa chọn sản phẩm", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void mapping() {
        rvListItem = (RecyclerView) findViewById(R.id.rvListItem);
        lbShip = (TextView) findViewById(R.id.lbShip);
        btnReBuy = (Button) findViewById(R.id.btnReBuy);
    }

    private void loadData() {
        userDTO = UserDAO.getInstance().getSharedPreferences(HistoryBuyActivity.this);

        loadListItem();

        cartDTOS = adapter.getCartDTOS();

        menu.findItem(R.id.navHistoryCart).setVisible(false);
    }

    private void loadListItem() {
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rvListItem.setLayoutManager(layoutManager);
        adapter = new HistoryBuyAdapter(userDTO.getUserId(), this);
        rvListItem.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}