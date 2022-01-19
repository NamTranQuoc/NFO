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
import hcmute.edu.vn.mssv18110323.shoppingmall.model.dao.CategoryDAO;
import hcmute.edu.vn.mssv18110323.shoppingmall.model.dto.CategoryDTO;
import hcmute.edu.vn.mssv18110323.shoppingmall.utils.Base64Utils;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    List<CategoryDTO> categoryDTOS;
    Context context;

    public CategoryAdapter(Context context) {
        this.categoryDTOS = CategoryDAO.getInstance().gets(context);
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoryDTO categoryDTO = categoryDTOS.get(position);
        holder.name.setText(categoryDTO.getName());
        holder.img.setImageBitmap(Base64Utils.stringToBitmap(categoryDTO.getImage()));
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context.getApplicationContext(), SearchActivity.class);
                intent.putExtra("keyword", categoryDTO.getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryDTOS == null ? 0 : categoryDTOS.size();
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
