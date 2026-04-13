package Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication111111111.R;
import com.example.myapplication111111111.databinding.ActivityMainBinding;

import java.util.ArrayList;

import DataBase.MyViewModel;
import DataBase.Transaction;
import DataBase.TransactionWithCategory;
import DataBase.utils_db;

public class MainActivity extends AppCompatActivity implements OnItemClick_transaction{
ActivityMainBinding binding ;
    double totalIncome;
    double totalExpense;
    MyViewModel viewModel;
    double balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());


        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle("Main Screen");
            actionBar.setElevation(20);
        }



        viewModel = new ViewModelProvider(this).get(MyViewModel.class);
        updateTotals();





            MyAdapter myAdapter = new MyAdapter(this,new ArrayList<>(), viewModel, this , new ArrayList<TransactionWithCategory>());
            binding.recyclerView.setAdapter(myAdapter);
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
            binding.recyclerView.setHasFixedSize(true);

        viewModel.getAllTransactions().observe(this, transactions -> {
            if(transactions != null){
                myAdapter.setTransactions(transactions);
            }
        });


        viewModel.getAllTransactionsWithCategory().observe(this, list -> {
            myAdapter.setTransactionsWithCategory(list);
        });



        binding.addTransaction.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent intent = new Intent(MainActivity.this, addNewTransaction.class);
            intent.putExtra("balance", balance );
            startActivity(intent);


        }
        });

        binding.myTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AllTransaction.class);
                intent.putExtra("balance",balance);
                startActivity(intent);
            }
        });




        binding.charts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this,Charts.class);
                startActivity(i);

            }
        });


    }

//    @Override
//    public void onClick(Transaction transaction) {
//        Intent i = new Intent(MainActivity.this, TransactionDetails.class);
//        i.putExtra("transaction_Key",transaction);
//        i.putExtra("balance", balance );
//        startActivity(i);
//    }



    @Override
    public void onClick(TransactionWithCategory item) {

        Intent i = new Intent(MainActivity.this, TransactionDetails.class);
        i.putExtra("transaction_Key", item);
        i.putExtra("balance", balance);
        startActivity(i);
    }
    @Override
    protected void onResume() {
        super.onResume();
        updateTotals();
    }

    private void updateTotals() {

        viewModel.getTotalIncome(utils_db.TYPE_INCOME, new DoubleValueListener() {
            @Override
            public void valueSubmit(Double value) {
                totalIncome = value != null ? value : 0;
                updateBalance();
            }
        });

        viewModel.getTotalExpense(utils_db.TYPE_EXPENSE, new DoubleValueListener() {
            @Override
            public void valueSubmit(Double value) {
                totalExpense = value != null ? value : 0;
                updateBalance();
            }
        });
    }

    private void updateBalance() {
        runOnUiThread(() -> {
             balance = totalIncome - totalExpense;
            binding.balanceTv.setText(balance + " $");
            binding.expenseTv.setText(totalExpense + "$ -");
            binding.incomeTv.setText(totalIncome + "$ +");
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.context_menu, menu);
        return true;
    }




    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.settings){
            startActivity(new Intent(this, Settings.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}