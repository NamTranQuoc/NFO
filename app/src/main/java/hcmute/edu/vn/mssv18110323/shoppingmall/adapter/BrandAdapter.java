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
import hcmute.edu.vn.mssv18110323.shoppingmall.controller.SearchActivity;
import hcmute.edu.vn.mssv18110323.shoppingmall.model.dao.BrandDAO;
import hcmute.edu.vn.mssv18110323.shoppingmall.model.dto.BrandDTO;
import hcmute.edu.vn.mssv18110323.shoppingmall.utils.Base64Utils;

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.ViewHolder> {

    List<BrandDTO> brandDTOS;
    Context context;

    public BrandAdapter(Context context) {
        this.brandDTOS = BrandDAO.getInstance().gets(context);
        this.context = context;
    }

    @NonNull
    @Override
    public BrandAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new BrandAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BrandAdapter.ViewHolder holder, int position) {
        BrandDTO brandDTO = brandDTOS.get(position);
        holder.name.setText(brandDTO.getName());
        holder.img.setImageBitmap(Base64Utils.stringToBitmap(brandDTO.getImage()));
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(), SearchActivity.class);
                intent.putExtra("keyword", brandDTO.getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return brandDTOS == null ? 0 : brandDTOS.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.nameItem);
            img = (ImageView) itemView.findViewById(R.id.imgItem);
        }
    }
}
