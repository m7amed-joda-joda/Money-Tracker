package Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Transaction;

import com.example.myapplication111111111.R;
import com.example.myapplication111111111.databinding.ActivityAllTransactionBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import DataBase.Category;
import DataBase.MyViewModel;
import DataBase.TransactionWithCategory;
import DataBase.utils_db;

public class AllTransaction extends AppCompatActivity implements OnItemClick_transaction ,OnItemClick{
ActivityAllTransactionBinding binding;
MyViewModel viewModel;
    MyAdapter myAdapter;
Date startDate=null, endDate=null ;
String type = null;
int cat_id = 0;

double balance;

boolean isAllCategoriesSelected ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAllTransactionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle("My All Transaction");
            actionBar.setElevation(20);
        }

        viewModel = new ViewModelProvider(this).get(MyViewModel.class);

         myAdapter = new MyAdapter(this,new ArrayList<>(), viewModel, this , new ArrayList<TransactionWithCategory>());
        binding.transactionRV.setAdapter(myAdapter);
        binding.transactionRV.setLayoutManager(new LinearLayoutManager(this));
        binding.transactionRV.setHasFixedSize(true);


        viewModel.getAllTransactions().observe(this, transactions -> {
            if(transactions != null){
                myAdapter.setTransactions(transactions);
            }
        });


        Intent i = getIntent();
        i.getExtras().get("balance");

        viewModel.getAllTransactionsWithCategory().observe(this, list -> {
            myAdapter.setTransactionsWithCategory(list);
        });

        binding.dateFilterBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AllTransaction.this);
                View view = LayoutInflater.from(AllTransaction.this).inflate(R.layout.custome_alert_date_filtering, null);

                Button endDate_BTN, startDate_BTN,confirm_BTN;

                endDate_BTN = view.findViewById(R.id.endDate_BTN);
                startDate_BTN = view.findViewById(R.id.startDate_BTN);
                confirm_BTN = view.findViewById(R.id.confirm_BTN);

                startDate_BTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Calendar calendar = Calendar.getInstance();
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH);
                        int day = calendar.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog datePickerDialog = new DatePickerDialog(
                                AllTransaction.this,
                                (view, selectedYear, selectedMonth, selectedDay) -> {

                                    selectedMonth = selectedMonth + 1;

                                    String startDateSTR = selectedDay + "/" + selectedMonth + "/" + selectedYear;
                                    startDate_BTN.setText(startDateSTR);



                                    Calendar selectedCalendar = Calendar.getInstance();
                                    selectedCalendar.set(selectedYear, selectedMonth-1, selectedDay);
                                    startDate = selectedCalendar.getTime();


                                },
                                year, month, day
                        );

                        datePickerDialog.show();

                    }
                });
                endDate_BTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Calendar calendar = Calendar.getInstance();
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH);
                        int day = calendar.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog datePickerDialog = new DatePickerDialog(
                                AllTransaction.this,
                                (view, selectedYear, selectedMonth, selectedDay) -> {

                                    selectedMonth = selectedMonth + 1;

                                    String endDateSTR = selectedDay + "/" + selectedMonth + "/" + selectedYear;
                                    endDate_BTN.setText(endDateSTR);



                                    Calendar selectedCalendar = Calendar.getInstance();
                                    selectedCalendar.set(selectedYear, selectedMonth-1, selectedDay);
                                    endDate = selectedCalendar.getTime();



                                },
                                year, month, day
                        );

                        datePickerDialog.show();

                    }
                });


                final AlertDialog[] alert = new AlertDialog[1];

            confirm_BTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                if (endDate != null && startDate != null)
                {
                    viewModel.getAllTransactionsByDate(startDate, endDate).observe(AllTransaction.this, transactions -> {

                            myAdapter.setTransactions(transactions);


                    });

                }



                }
            });

                builder.setView(view);


                alert[0] = builder.create();
                alert[0].show();


            }
        });
        binding.filterBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AllTransaction.this);
                View view = LayoutInflater.from(AllTransaction.this).inflate(R.layout.custome_alert_filtering, null);

                Button endDate_BTN, startDate_BTN,confirm_BTN;
                CheckBox allCategories_CHbox;
                RadioGroup radioGroup;
                RadioButton expense_RB, income_RB;
                RecyclerView recyclerView;

                endDate_BTN = view.findViewById(R.id.endDate_BTN);
                startDate_BTN = view.findViewById(R.id.startDate_BTN);
                confirm_BTN = view.findViewById(R.id.confirm_BTN);
                expense_RB = view.findViewById(R.id.expense_RB);
                income_RB = view.findViewById(R.id.income_RB);
                radioGroup = view.findViewById(R.id.radioGroup);
                recyclerView = view.findViewById(R.id.recyclerViewFilter);
                allCategories_CHbox = view.findViewById(R.id.allCategories_CHbox);




                startDate_BTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Calendar calendar = Calendar.getInstance();
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH);
                        int day = calendar.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog datePickerDialog = new DatePickerDialog(
                                AllTransaction.this,
                                (view, selectedYear, selectedMonth, selectedDay) -> {

                                    selectedMonth = selectedMonth + 1;

                                    String startDateSTR = selectedDay + "/" + selectedMonth + "/" + selectedYear;
                                    startDate_BTN.setText(startDateSTR);



                                    Calendar selectedCalendar = Calendar.getInstance();
                                    selectedCalendar.set(selectedYear, selectedMonth-1, selectedDay);
                                    startDate = selectedCalendar.getTime();


                                },
                                year, month, day
                        );

                        datePickerDialog.show();

                    }
                });
                endDate_BTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Calendar calendar = Calendar.getInstance();
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH);
                        int day = calendar.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog datePickerDialog = new DatePickerDialog(
                                AllTransaction.this,
                                (view, selectedYear, selectedMonth, selectedDay) -> {

                                    selectedMonth = selectedMonth + 1;

                                    String endDateSTR = selectedDay + "/" + selectedMonth + "/" + selectedYear;
                                    endDate_BTN.setText(endDateSTR);



                                    Calendar selectedCalendar = Calendar.getInstance();
                                    selectedCalendar.set(selectedYear, selectedMonth-1, selectedDay);
                                    endDate = selectedCalendar.getTime();



                                },
                                year, month, day
                        );

                        datePickerDialog.show();

                    }
                });

                CategoryAdapter categoryAdapter = new CategoryAdapter(new ArrayList<Category>(),AllTransaction.this);
                recyclerView.setAdapter(categoryAdapter);
                recyclerView.setLayoutManager(new GridLayoutManager(AllTransaction.this, 4));
                recyclerView.setHasFixedSize(true);

                viewModel.getAllCategories().observe(AllTransaction.this, categories -> {
                    categoryAdapter.setList(categories);
                });

                final AlertDialog[] alert = new AlertDialog[1];

            confirm_BTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    isAllCategoriesSelected = allCategories_CHbox.isChecked();

                if (endDate != null && startDate != null)
                {
                    viewModel.getAllTransactionsByDate(startDate, endDate).observe(AllTransaction.this, transactions -> {
                      int checkedId = radioGroup.getCheckedRadioButtonId();
                      if(checkedId == income_RB.getId())
                      {
                          type = utils_db.TYPE_INCOME;

                      }else if(checkedId == expense_RB.getId()){

                            type = utils_db.TYPE_EXPENSE;
                        }

                        if(type == null || startDate == null || endDate == null){
                            Toast.makeText(AllTransaction.this, "Error in data", Toast.LENGTH_SHORT).show();
                        }else{



                            if(isAllCategoriesSelected == true)
                            {
                                viewModel.getAllTransactionsByDateType(startDate,endDate,type).observe(AllTransaction.this, transactions2 -> {

                                    myAdapter.setTransactions(transactions2);
                                });
                            }else{
                                if(cat_id !=0){

                                    viewModel.getAllTransactionsByDateCategoryType(startDate,endDate,type, cat_id).observe(AllTransaction.this, transactions1 -> {

                                        myAdapter.setTransactions(transactions1);
                                    });

                                }else {

                                    Toast.makeText(AllTransaction.this, "Error In Data", Toast.LENGTH_SHORT).show();
                                }


                            }



                        }

 });

                }



                }
            });

                builder.setView(view);


                alert[0] = builder.create();
                alert[0].show();


            }
        });


    }

    @Override
    public void onClick(TransactionWithCategory transaction) {
        Intent i = new Intent(AllTransaction.this, TransactionDetails.class);
        i.putExtra("transaction_Key",transaction);
        i.putExtra("balance", balance );
        startActivity(i);
    }

    @Override
    public void onClick(Category category) {
        cat_id = category.getId();
    }
}