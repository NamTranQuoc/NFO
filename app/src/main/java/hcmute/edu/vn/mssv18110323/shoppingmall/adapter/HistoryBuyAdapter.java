package hcmute.edu.vn.mssv18110323.shoppingmall.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hcmute.edu.vn.mssv18110323.shoppingmall.R;
import hcmute.edu.vn.mssv18110323.shoppingmall.controller.DetailProductActivity;
import hcmute.edu.vn.mssv18110323.shoppingmall.model.dao.CartDAO;
import hcmute.edu.vn.mssv18110323.shoppingmall.model.dto.CartDTO;
import hcmute.edu.vn.mssv18110323.shoppingmall.utils.Base64Utils;
import hcmute.edu.vn.mssv18110323.shoppingmall.utils.Const;
import hcmute.edu.vn.mssv18110323.shoppingmall.utils.FormatUtils;

public class HistoryBuyAdapter extends RecyclerView.Adapter<HistoryBuyAdapter.ViewHolder> {

    List<CartDTO> cartDTOS;
    Context context;

    public HistoryBuyAdapter(Long userId, Context context) {
        this.cartDTOS = CartDAO.getInstance().getByUserId(userId, Const.delivered, context);
        this.context = context;
    }

    public List<CartDTO> getCartDTOS() {
        return this.cartDTOS;
    }

    @NonNull
    @Override
    public HistoryBuyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new HistoryBuyAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryBuyAdapter.ViewHolder holder, int position) {
        CartDTO cartDTO = cartDTOS.get(position);
        holder.img.setImageBitmap(Base64Utils.stringToBitmap(cartDTO.getImage()));
        holder.nameProduct.setText(cartDTO.getNameProductMini());
        holder.nameType.setText(cartDTO.getNameType());
        holder.price.setText(FormatUtils.getShowPrice(cartDTO.getPrice() * cartDTO.getQuantity()));
        holder.txtQuantity.setText(cartDTO.getQuantity().toString());

        cartDTO.setSelect(false);
        holder.cbSelect.setChecked(false);

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailProductActivity.class);
                intent.putExtra("product_id", cartDTO.getProductId());
                context.startActivity(intent);
            }
        });

        holder.cbSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.cbSelect.isChecked()) {
                    cartDTO.setSelect(true);
                } else {
                    cartDTO.setSelect(false);
                }
            }
        });

        if (cartDTO.getStock() < 1) {
            holder.cbSelect.setEnabled(false);
        }

        holder.minusQuantity.setEnabled(false);

        holder.plusQuantity.setEnabled(false);

        holder.txtQuantity.setEnabled(false);
    }

    @Override
    public int getItemCount() {
        return cartDTOS == null ? 0 : cartDTOS.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nameProduct, nameType, price;
        private ImageView img;
        private ImageButton minusQuantity, plusQuantity;
        private EditText txtQuantity;
        private CheckBox cbSelect;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameProduct = (TextView) itemView.findViewById(R.id.nameProduct);
            img = (ImageView) itemView.findViewById(R.id.image);
            nameType = (TextView) itemView.findViewById(R.id.nameType);
            price = (TextView) itemView.findViewById(R.id.price);
            minusQuantity = (ImageButton) itemView.findViewById(R.id.subQuantity);
            plusQuantity = (ImageButton) itemView.findViewById(R.id.addQuantity);
            txtQuantity = (EditText) itemView.findViewById(R.id.txtQuantity);
            cbSelect = (CheckBox) itemView.findViewById(R.id.select);
        }
    }
}