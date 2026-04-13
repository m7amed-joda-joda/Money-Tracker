package Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.myapplication111111111.R;
import com.example.myapplication111111111.databinding.ActivityAddNewTransactionBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import DataBase.Category;
import DataBase.MyViewModel;
import DataBase.Transaction;
import DataBase.utils_db;

public class addNewTransaction extends AppCompatActivity implements OnItemClick {
ActivityAddNewTransactionBinding binding;
    String type = null;
    int cat_id = 0;
    Date date = null;
    double balance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAddNewTransactionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle("Add New Transaction");
            actionBar.setElevation(20);
        }


        MyViewModel viewModel = new ViewModelProvider(this).get(MyViewModel.class);


        CategoryAdapter categoryAdapter = new CategoryAdapter(new ArrayList<Category>(),this);
        Intent i = getIntent();
        balance = i.getExtras().getDouble("balance");


       viewModel.getAllCategories().observe(this, new Observer<List<Category>>() {
           @Override
           public void onChanged(List<Category> categories) {
               categoryAdapter.setList(categories);
           }
       });

        binding.recyclerCategories.setAdapter(categoryAdapter);
        binding.recyclerCategories.setLayoutManager(new GridLayoutManager(this, 4));
        binding.recyclerCategories.setHasFixedSize(true);




        binding.btnIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                    binding.typeTv.setText("Income");
                    type = utils_db.TYPE_INCOME;


            }
        });


        binding.btnExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.typeTv.setText("Expense");
                type = utils_db.TYPE_EXPENSE;


            }
        });



        binding.dateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        addNewTransaction.this,
                        (view, selectedYear, selectedMonth, selectedDay) -> {

                            selectedMonth = selectedMonth + 1;

                            String dateSTR = selectedDay + "/" + selectedMonth + "/" + selectedYear;
                            binding.dateBTN.setText(dateSTR);


                            Calendar selectedCalendar = Calendar.getInstance();
                            selectedCalendar.set(selectedYear, selectedMonth-1, selectedDay);

                            date = selectedCalendar.getTime();


                        },
                        year, month, day
                );

                datePickerDialog.show();

            }
       });





        binding.saveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount_STR = binding.amountET.getText().toString();
                String note = binding.noteET.getText().toString();
                
                if(amount_STR.isEmpty() || type == null || cat_id == 0 || date == null)
                {
                    Toast.makeText(addNewTransaction.this, "Invalid data", Toast.LENGTH_SHORT).show();
                }else
                {

                    if(Double.valueOf(amount_STR) > 0)
                    {
                        if(type.equals(utils_db.TYPE_INCOME))
                        {
                            Transaction transaction = new Transaction(Double.valueOf(amount_STR),type, cat_id,date,note);
                            viewModel.insertTransaction(transaction);
                            finish();
                        } else
                        {
                            if(Double.valueOf(amount_STR) <= balance  ){
                                Transaction transaction = new Transaction(Double.valueOf(amount_STR),type, cat_id,date,note);
                                viewModel.insertTransaction(transaction);
                                finish();
                            }else {
                                Toast.makeText(addNewTransaction.this, "Amount is not allowed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }else
                    {
                        Toast.makeText(addNewTransaction.this, "Amount is not allowed", Toast.LENGTH_SHORT).show();

                    }

                }
            }
        });





    }

    @Override
    public void onClick(Category category) {

        Toast.makeText(this, category.getName(), Toast.LENGTH_SHORT).show();
         cat_id = category.getId();
    }
}