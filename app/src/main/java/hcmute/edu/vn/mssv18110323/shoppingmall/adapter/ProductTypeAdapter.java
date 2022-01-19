package hcmute.edu.vn.mssv18110323.shoppingmall.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import hcmute.edu.vn.mssv18110323.shoppingmall.R;
import hcmute.edu.vn.mssv18110323.shoppingmall.model.dto.ProductTypeDTO;

import static hcmute.edu.vn.mssv18110323.shoppingmall.utils.Const.no_select_type;
import static hcmute.edu.vn.mssv18110323.shoppingmall.utils.Const.select_type;

public class ProductTypeAdapter extends RecyclerView.Adapter<ProductTypeAdapter.ViewHolder> {

    List<ProductTypeDTO> productTypeDTOS;
    TextView quantity;
    EditText txtQuantity;
    List<Button> list;
    boolean f = true;
    Long productTypeId;
    Button btnBuy;

    public ProductTypeAdapter(List<ProductTypeDTO> list, TextView quantity, EditText txtQuantity, Button btnBuy) {
        this.productTypeDTOS = list;
        this.quantity = quantity;
        this.txtQuantity = txtQuantity;
        this.list = new ArrayList<>();
        this.btnBuy = btnBuy;
    }

    //nếu không còn sản phẩm nào trong kho thì enable addToCart
    public void checkF() {
        if (!f) {
            btnBuy.setEnabled(false);
        }
    }

    public Long getProductTypeId() {
        return productTypeId;
    }

    @NonNull
    @Override
    public ProductTypeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_type_product, parent, false);
        return new ProductTypeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductTypeAdapter.ViewHolder holder, int position) {
        ProductTypeDTO productTypeDTO = productTypeDTOS.get(position);
        holder.name.setText(productTypeDTO.getProductTypeName());
        list.add(holder.name);

        if (productTypeDTO.getQuantity() == 0) {
            holder.name.setEnabled(false);
        }

        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productTypeId = productTypeDTO.getProductTypeId();
                quantity.setText(productTypeDTO.getQuantity().toString());
                for (Button b : list) {
                    b.setBackgroundColor(no_select_type);
                }
                holder.name.setBackgroundColor(select_type);
                String str = txtQuantity.getText().toString();
                Long lQuantity = Long.parseLong(str);
                if (lQuantity <= Long.parseLong(quantity.getText().toString()) && lQuantity > 0) {
                    txtQuantity.setText(str);
                } else {
                    txtQuantity.setText(productTypeDTO.getQuantity().toString());
                }
            }
        });

        if (f && productTypeDTO.getQuantity() != 0) {
            holder.name.callOnClick();
            f = false;
        }
    }

    @Override
    public int getItemCount() {
        return productTypeDTOS == null ? 0 : productTypeDTOS.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private Button name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (Button) itemView.findViewById(R.id.nameItem);
        }
    }
}
