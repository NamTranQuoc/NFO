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
import hcmute.edu.vn.mssv18110323.shoppingmall.adapter.CartAdapter;
import hcmute.edu.vn.mssv18110323.shoppingmall.model.dao.CartDAO;
import hcmute.edu.vn.mssv18110323.shoppingmall.model.dao.UserDAO;
import hcmute.edu.vn.mssv18110323.shoppingmall.model.dto.CartDTO;
import hcmute.edu.vn.mssv18110323.shoppingmall.model.dto.UserDTO;
import hcmute.edu.vn.mssv18110323.shoppingmall.utils.Const;
import hcmute.edu.vn.mssv18110323.shoppingmall.utils.FormatUtils;

public class CartActivity extends Navigate {

    RecyclerView rvListItem;
    UserDTO userDTO;
    LinearLayoutManager layoutManager;
    CartAdapter adapter;
    TextView totalPrice, lbShip;
    List<CartDTO> cartDTOS;
    Button btnBuy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_cart);
        super.onCreate(savedInstanceState);

        mapping();
        loadData();

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartDAO.getInstance().upsertMany(cartDTOS, CartActivity.this);
                List<CartDTO> buy = new ArrayList<>();
                for (int i = 0; i < cartDTOS.size(); i++) {
                    if (cartDTOS.get(i).isSelect()) {
                        buy.add(cartDTOS.get(i));
                        cartDTOS.remove(cartDTOS.get(i));
                        i--;
                    }
                }
                if (buy.size() > 0) {
                    int result = CartDAO.getInstance().updateStatus(buy, Const.delivered, CartActivity.this);
                    if (result != 0) {
                        loadListItem();
                        Toast.makeText(CartActivity.this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                        totalPrice.setText(adapter.totalPrice());
                    } else {
                        Toast.makeText(CartActivity.this, "Có lỗi đã xảy ra", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CartActivity.this, "Chưa chọn sản phẩm", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CartDAO.getInstance().upsertMany(cartDTOS, CartActivity.this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        CartDAO.getInstance().upsertMany(cartDTOS, CartActivity.this);
    }

    private void mapping() {
        rvListItem = (RecyclerView) findViewById(R.id.rvListItem);
        totalPrice = (TextView) findViewById(R.id.totalPrice);
        lbShip = (TextView) findViewById(R.id.lbShip);
        btnBuy = (Button) findViewById(R.id.btnBuy);
    }

    private void loadData() {
        userDTO = UserDAO.getInstance().getSharedPreferences(CartActivity.this);

        loadListItem();

        cartDTOS = adapter.getCartDTOS();
        totalPrice.setText(adapter.totalPrice());

        lbShip.setText("Vận chuyển: ₫" + FormatUtils.getShowPrice(Const.ship));

        //không cho bấm vào nút cart khi đang ở trong cart
        btnCart.setEnabled(false);
    }

    private void loadListItem() {
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        rvListItem.setLayoutManager(layoutManager);
        adapter = new CartAdapter(userDTO.getUserId(), totalPrice, this);
        rvListItem.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}