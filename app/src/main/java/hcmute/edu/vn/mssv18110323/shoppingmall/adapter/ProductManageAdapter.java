package hcmute.edu.vn.mssv18110323.shoppingmall.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hcmute.edu.vn.mssv18110323.shoppingmall.R;
import hcmute.edu.vn.mssv18110323.shoppingmall.controller.AddOrEditProductActivity;
import hcmute.edu.vn.mssv18110323.shoppingmall.model.dao.ProductDAO;
import hcmute.edu.vn.mssv18110323.shoppingmall.model.dto.ProductDTO;
import hcmute.edu.vn.mssv18110323.shoppingmall.utils.Base64Utils;
import hcmute.edu.vn.mssv18110323.shoppingmall.utils.FormatUtils;

public class ProductManageAdapter extends RecyclerView.Adapter<ProductManageAdapter.ViewHolder> {

    List<ProductDTO> productDTOS;
    Context context;

    public ProductManageAdapter(Context context, String keyword) {
        this.productDTOS = ProductDAO.getInstance().search(keyword, context);
        this.context = context;
    }

    public void reloadData() {
        this.productDTOS = ProductDAO.getInstance().search("", context);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductManageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_manage, parent, false);
        return new ProductManageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductManageAdapter.ViewHolder holder, int position) {
        ProductDTO productDTO = productDTOS.get(position);
        holder.name.setText(productDTO.getName());
        holder.price.setText(FormatUtils.getShowPrice(productDTO.getPrice()));
        holder.img.setImageBitmap(Base64Utils.stringToBitmap(productDTO.getImage1()));
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddOrEditProductActivity.class);
                intent.putExtra("product_id", productDTO.getProductId());
                context.startActivity(intent);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int result = ProductDAO.getInstance().delete(productDTO.getProductId(), context);
                if (result > 0) {
                    productDTOS.remove(position);
                    notifyItemRemoved(position);
                    notifyItemChanged(position, productDTOS);
                    Toast.makeText(context, "Xóa sản phẩm thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Không thể xóa sản phẩm", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productDTOS == null ? 0 : productDTOS.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name, price;
        private ImageView img;
        private Button btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.nameProduct);
            img = (ImageView) itemView.findViewById(R.id.image);
            price = (TextView) itemView.findViewById(R.id.price);
            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);
        }
    }
}
