package Activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication111111111.R;

import java.util.List;

import DataBase.Category;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    List<Category> list;
    OnItemClick listener;



    public CategoryAdapter(List<Category> list, OnItemClick listener) {
        this.list = list;
        this.listener = listener;
    }

    public void setList(List<Category> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Category category = list.get(position);

        holder.namedTv.setText(category.getName());
        holder.imgIcon.setImageResource(category.getIcon());

        holder.itemView.setOnClickListener(v -> {
            listener.onClick(category);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgIcon;
        TextView namedTv;

        public ViewHolder(View itemView) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.imgIcon);
            namedTv = itemView.findViewById(R.id.nameTv);
        }
    }
}