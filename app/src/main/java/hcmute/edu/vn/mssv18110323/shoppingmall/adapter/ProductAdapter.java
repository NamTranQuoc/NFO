package hcmute.edu.vn.mssv18110323.shoppingmall.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hcmute.edu.vn.mssv18110323.shoppingmall.R;
import hcmute.edu.vn.mssv18110323.shoppingmall.controller.DetailProductActivity;
import hcmute.edu.vn.mssv18110323.shoppingmall.model.dao.ProductDAO;
import hcmute.edu.vn.mssv18110323.shoppingmall.model.dto.ProductDTO;
import hcmute.edu.vn.mssv18110323.shoppingmall.model.dto.UserDTO;
import hcmute.edu.vn.mssv18110323.shoppingmall.utils.Base64Utils;
import hcmute.edu.vn.mssv18110323.shoppingmall.utils.FormatUtils;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    List<ProductDTO> productDTOS;
    Context context;
    UserDTO userDTO;
    int page = 1;
    TextView nextPage;

    public ProductAdapter(Context context, UserDTO userDTO, String keyword, TextView nextPage) {
        this.productDTOS = ProductDAO.getInstance().search(keyword, page, context);
        this.context = context;
        this.userDTO = userDTO;
        this.nextPage = nextPage;
        if (ProductDAO.getInstance().search(keyword, page + 1, context).size() == 0) {
            nextPage.setVisibility(View.GONE);
        }
    }

    public void getNextPage(String keyword) {
        page++;
        this.productDTOS.addAll(ProductDAO.getInstance().search(keyword, page, context));
        notifyDataSetChanged();
        if (ProductDAO.getInstance().search(keyword, page + 1, context).size() == 0) {
            nextPage.setVisibility(View.GONE);
        }
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {
        ProductDTO productDTO = productDTOS.get(position);
        holder.name.setText(productDTO.getNameMini());
        holder.price.setText(FormatUtils.getShowPrice(productDTO.getPrice()));
        holder.img.setImageBitmap(Base64Utils.stringToBitmap(productDTO.getImage1()));
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailProductActivity.class);
                intent.putExtra("product_id", productDTO.getProductId());
                context.startActivity(intent);
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.nameItem);
            img = (ImageView) itemView.findViewById(R.id.imgItem);
            price = (TextView) itemView.findViewById(R.id.priceItem);
        }
    }
}
