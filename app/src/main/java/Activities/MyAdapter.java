package Activities;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication111111111.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import DataBase.MyViewModel;
import DataBase.Transaction;
import DataBase.TransactionWithCategory;
import DataBase.utils_db;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {

    Context context ;
//    List<Transaction> transactions ;
    List<TransactionWithCategory> transactionsWithCategories ;
    MyViewModel viewModel;
    OnItemClick_transaction listener;

    public MyAdapter(Context context, List<Transaction> transactions, MyViewModel viewModel, OnItemClick_transaction listener,  List<TransactionWithCategory> transactionsWithCategories) {
        this.context = context;
//        this.transactions = transactions;
        this.viewModel = viewModel;
        this.listener = listener;
        this.transactionsWithCategories = transactionsWithCategories ;
    }

    public void setTransactions(List<Transaction> transactions) {
//        this.transactions = transactions;
        notifyDataSetChanged();
    }

    public void setTransactionsWithCategory(List<TransactionWithCategory> list){
        this.transactionsWithCategories = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.transaction_item, parent, false);

        MyHolder mh = new MyHolder(v);


        return mh;
    }


    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        if (transactionsWithCategories == null || position >= transactionsWithCategories.size())
            return;

        TransactionWithCategory twc = transactionsWithCategories.get(position);
        Transaction transaction = twc.transaction;

        if (transaction.getType().equals(utils_db.TYPE_INCOME)) {
            holder.amountTv.setText(transaction.getAmount() + " +");
            holder.amountTv.setTextColor(Color.parseColor("#49B84D"));
        } else {
            holder.amountTv.setText(transaction.getAmount() + " -");
            holder.amountTv.setTextColor(Color.RED);
        }

        holder.transaction_dateTv.setText(
                new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        .format(transaction.getDate())
        );

        holder.category_nameTv.setText(twc.category.getName());
        holder.category_icon.setImageResource(twc.category.getIcon());

        holder.itemView.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();

            if (pos != RecyclerView.NO_POSITION &&
                    pos < transactionsWithCategories.size()) {
                listener.onClick(transactionsWithCategories.get(pos));
            }
        });
    }


    @Override
    public int getItemCount() {
        return transactionsWithCategories == null ? 0 : transactionsWithCategories.size();
    }

    public  class MyHolder extends RecyclerView.ViewHolder {

        TextView amountTv , transaction_dateTv , category_nameTv ;
        ImageView category_icon ;



        public MyHolder(@NonNull View itemView) {
            super(itemView);

            amountTv   =   itemView.findViewById(R.id.amountTv);
            transaction_dateTv   =   itemView.findViewById(R.id.transaction_dateTv);
            category_nameTv   =   itemView.findViewById(R.id.category_nameTv);
            category_icon   =   itemView.findViewById(R.id.category_icon);


        }
    }
}
