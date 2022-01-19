package hcmute.edu.vn.mssv18110323.shoppingmall.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hcmute.edu.vn.mssv18110323.shoppingmall.R;
import hcmute.edu.vn.mssv18110323.shoppingmall.model.dto.ProductTypeDTO;

public class ProductTypeManageAdapter extends RecyclerView.Adapter<ProductTypeManageAdapter.ViewHolder> {

    List<ProductTypeDTO> productTypeDTOS;
    Context context;

    public ProductTypeManageAdapter(Context context, List<ProductTypeDTO> productTypeDTOs) {
        this.context = context;
        this.productTypeDTOS = productTypeDTOs;
    }

    @NonNull
    @Override
    public ProductTypeManageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_type_product, parent, false);
        return new ProductTypeManageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductTypeManageAdapter.ViewHolder holder, int position) {
        ProductTypeDTO productTypeDTO = productTypeDTOS.get(position);
        holder.name.setText(productTypeDTO.getProductTypeName());
        holder.quantity.setText(productTypeDTO.getQuantity().toString());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getItemCount() > 1) {
                    productTypeDTOS.remove(position);
                    notifyItemRemoved(position);
                    notifyItemChanged(position);
                }
            }
        });

        holder.name.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                productTypeDTO.setProductTypeName(holder.name.getText().toString());
            }
        });

        holder.quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if (Long.parseLong(holder.quantity.getText().toString()) < 1 || holder.quantity.getText().toString().equals("")) {
                        holder.quantity.setText("1");
                    }
                    productTypeDTO.setQuantity(Long.parseLong(holder.quantity.getText().toString()));
                } catch (Exception e) {
                    holder.quantity.setText("1");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productTypeDTOS == null ? 0 : productTypeDTOS.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        EditText name, quantity;
        ImageView delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (EditText) itemView.findViewById(R.id.txtName);
            quantity = (EditText) itemView.findViewById(R.id.txtQuantity);
            delete = (ImageView) itemView.findViewById(R.id.btnCancel);
        }
    }
}
