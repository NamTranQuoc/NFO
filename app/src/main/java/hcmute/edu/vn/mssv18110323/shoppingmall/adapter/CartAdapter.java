package hcmute.edu.vn.mssv18110323.shoppingmall.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
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

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    List<CartDTO> cartDTOS;
    TextView total;
    Context context;

    public CartAdapter(Long userId, TextView total, Context context) {
        this.cartDTOS = CartDAO.getInstance().getByUserId(userId, Const.add_to_cart, context);
        this.total = total;
        this.context = context;
    }

    public List<CartDTO> getCartDTOS() {
        return this.cartDTOS;
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        CartDTO cartDTO = cartDTOS.get(position);
        holder.img.setImageBitmap(Base64Utils.stringToBitmap(cartDTO.getImage()));
        holder.nameProduct.setText(cartDTO.getNameProductMini());
        holder.nameType.setText(cartDTO.getNameType());
        holder.txtQuantity.setText(cartDTO.getQuantity().toString());

        if (cartDTO.getStock() < 1) {
            holder.cbSelect.setChecked(false);
            holder.cbSelect.setEnabled(false);
            cartDTOS.get(position).setSelect(false);
            cartDTOS.get(position).setQuantity(0L);
            total.setText(totalPrice());
        } else if (cartDTO.getStock() < cartDTO.getQuantity()) {
            holder.txtQuantity.setText(cartDTO.getStock().toString());
            cartDTOS.get(position).setQuantity(cartDTO.getStock());
            total.setText(totalPrice());
        }

        holder.price.setText(FormatUtils.getShowPrice(cartDTO.getPrice() * cartDTO.getQuantity()));

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
                total.setText(totalPrice());
            }
        });

        holder.minusQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Long lQuantity = Long.parseLong(holder.txtQuantity.getText().toString());
                if (lQuantity - 1 > 0) {
                    lQuantity -= 1;
                    holder.txtQuantity.setText(lQuantity.toString());
                    cartDTO.setQuantity(lQuantity);
                    holder.price.setText(FormatUtils.getShowPrice(cartDTO.getPrice() * lQuantity));
                    total.setText(totalPrice());
                }
            }
        });

        holder.plusQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Long lQuantity = Long.parseLong(holder.txtQuantity.getText().toString());
                if (lQuantity + 1 <= cartDTO.getStock()) {
                    lQuantity += 1;
                    holder.txtQuantity.setText(lQuantity.toString());
                    cartDTO.setQuantity(lQuantity);
                    holder.price.setText(FormatUtils.getShowPrice(cartDTO.getPrice() * lQuantity));
                    total.setText(totalPrice());
                }
            }
        });

        holder.txtQuantity.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                holder.checkQuantity(cartDTO.getStock());
                cartDTO.setQuantity(Long.parseLong(holder.txtQuantity.getText().toString()));
                holder.price.setText(FormatUtils.getShowPrice(cartDTO.getPrice() * cartDTO.getQuantity()));
                total.setText(totalPrice());
                return false;
            }
        });

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartDAO.getInstance().delete(cartDTO, context);
                cartDTOS.remove(cartDTO);
                notifyDataSetChanged();
            }
        });
    }

    public String totalPrice() {
        Long total = 0L;
        for (CartDTO cartDTO : cartDTOS) {
            if (cartDTO.isSelect()) {
                total += cartDTO.getPrice() * cartDTO.getQuantity();
            }
        }
        if (total != 0) {
            total += Const.ship;
        }
        return FormatUtils.getShowPrice(total);
    }

    @Override
    public int getItemCount() {
        return cartDTOS == null ? 0 : cartDTOS.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nameProduct, nameType, price, remove;
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
            remove = (TextView) itemView.findViewById(R.id.remove);
        }

        private void checkQuantity(Long quantity) {
            try {
                String str = txtQuantity.getText().toString();
                Long lQuantity = Long.parseLong(str);
                if (lQuantity <= 0) {
                    txtQuantity.setText("1");
                } else if (lQuantity > quantity) {
                    txtQuantity.setText(quantity.toString());
                }
            } catch (Exception e) {
                txtQuantity.setText("1");
            }
        }
    }
}