package Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication111111111.R;
import com.example.myapplication111111111.databinding.ActivityTransactionDetailsBinding;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import DataBase.Category;
import DataBase.MyViewModel;
import DataBase.Transaction;
import DataBase.TransactionWithCategory;
import DataBase.utils_db;

public class TransactionDetails extends AppCompatActivity implements OnItemClick{
    ActivityTransactionDetailsBinding binding;
    MyViewModel vm;
    int cat_id = 0;
    Date date = null;
    String catName = "";
    String oldType = "";
    Transaction passedTransaction ;
    double balance;

    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityTransactionDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle("Transaction Details");
            actionBar.setElevation(20);
        }



//
//         i = getIntent();
//         passedTransaction = (Transaction) i.getExtras().get("transaction_Key");

        TransactionWithCategory item =
                (TransactionWithCategory) getIntent().getSerializableExtra("transaction_Key");

        if (item == null || item.category == null) {
            finish();
            return;
        }

        String name = item.category.getName();
        int icon = item.category.getIcon();
        binding.categoryTv.setText(name);
        binding.iconImgView.setImageResource(icon);
        passedTransaction = item.transaction;



        vm = new ViewModelProvider(this).get(MyViewModel.class);



        binding.amountTv.setText(String.valueOf(passedTransaction.getAmount()));
        binding.noteTv.setText(passedTransaction.getNote());

        if(passedTransaction.getType().equals(utils_db.TYPE_INCOME))
        {

            binding.amountTv.setText(passedTransaction.getAmount()+" +");
            binding.amountTv.setTextColor(Color.parseColor("#49B84D"));
        }else {
            binding.amountTv.setText(passedTransaction.getAmount()+" -");
            binding.amountTv.setTextColor(Color.RED);


        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String dateSTR = sdf.format(passedTransaction.getDate());
        binding.dateTv.setText(dateSTR);
        binding.typeTv.setText(passedTransaction.getType());
        date = passedTransaction.getDate();




        binding.deleteBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TransactionDetails.this);
                final AlertDialog[] alert = new AlertDialog[1];

                builder.setCancelable(false);
                LayoutInflater inflater = LayoutInflater.from(TransactionDetails.this);

                View view = inflater.inflate(R.layout.coustome_alert_delete_transaction, null);

                Button cancel_BTN, confirm_BTN;

                confirm_BTN = view.findViewById(R.id.confirm_BTN);
                cancel_BTN = view.findViewById(R.id.cancel_BTN);

                confirm_BTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(TransactionDetails.this, "Deleted!", Toast.LENGTH_SHORT).show();


                        vm.deleteTransaction(passedTransaction);
                        finish();
                        alert[0].dismiss();
                    }
                });

                cancel_BTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        alert[0].dismiss();

                    }
                });

                builder.setView(view);


                alert[0] = builder.create();
                alert[0].show();


            }
        });


        binding.editBTn.setOnClickListener(v -> {


            AlertDialog.Builder builder = new AlertDialog.Builder(TransactionDetails.this);
            View view = LayoutInflater.from(TransactionDetails.this).inflate(R.layout.coustome_alert_edit_transaction, null);

            EditText amount_ET, note_ET;
            Button cancel_BTN, save_BTN,date_BTN;
            Button btnIncome, btnExpense;
            RecyclerView rv;
            TextView income_tv, type_Tv;

            amount_ET = view.findViewById(R.id.amount_ET_alert);
            note_ET =  view.findViewById(R.id.note_ET);
            cancel_BTN =  view.findViewById(R.id.cancel_BTN);
            save_BTN =  view.findViewById(R.id.save_BTN);
            date_BTN =  view.findViewById(R.id.date_BTN);
            btnIncome =  view.findViewById(R.id.btnIncome);
            btnExpense =  view.findViewById(R.id.btnExpense);
            rv =  view.findViewById(R.id.rv);
            income_tv =  view.findViewById(R.id.income_tv);
            type_Tv =  view.findViewById(R.id.type_Tv);



            amount_ET.setText(String.valueOf(passedTransaction.getAmount()));
            oldType = passedTransaction.getType();
            type_Tv.setText(oldType);
            note_ET.setText(passedTransaction.getNote());

            date_BTN.setText(dateSTR);


            CategoryAdapter categoryAdapter = new CategoryAdapter(new ArrayList<Category>(),TransactionDetails.this);
            Intent i = getIntent();
            balance = i.getExtras().getDouble("balance");


            vm.getAllCategories().observe(this, new Observer<List<Category>>() {
                @Override
                public void onChanged(List<Category> categories) {
                    categoryAdapter.setList(categories);
                }
            });


            rv.setAdapter(categoryAdapter);
            rv.setLayoutManager(new GridLayoutManager(TransactionDetails.this, 3));
            rv.setHasFixedSize(true);


            btnIncome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    type_Tv.setText("Income");
                        oldType = utils_db.TYPE_INCOME;
                }
            });
            btnExpense.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    type_Tv.setText("Expense");
                    oldType = utils_db.TYPE_EXPENSE;
                }
            });



            date_BTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int day = calendar.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(
                            TransactionDetails.this,
                            (view, selectedYear, selectedMonth, selectedDay) -> {

                                selectedMonth = selectedMonth + 1;

                                String dateSTR = selectedDay + "/" + selectedMonth + "/" + selectedYear;
                                date_BTN.setText(dateSTR);



                                Calendar selectedCalendar = Calendar.getInstance();
                                selectedCalendar.set(selectedYear, selectedMonth-1, selectedDay);

                                date = selectedCalendar.getTime();


                            },
                            year, month, day
                    );

                    datePickerDialog.show();

                }
            });




            income_tv.setText(catName);



            final AlertDialog[] alert = new AlertDialog[1];

            save_BTN.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        String amount_String = amount_ET.getText().toString();
        String note = note_ET.getText().toString();
        if(amount_String.isEmpty() || oldType == null|| cat_id == 0 || date == null)
        {
            Toast.makeText(TransactionDetails.this, "Invalid data!", Toast.LENGTH_SHORT).show();
            return;
        }else
        {
            if(Double.valueOf(amount_String)  > 0){

                if(oldType.equals(utils_db.TYPE_INCOME))
                {
                    Transaction transaction = new Transaction(Double.valueOf(amount_String),oldType,cat_id,date,note);
                    transaction.setId(passedTransaction.getId());
                    binding.amountTv.setText(amount_String);
                    binding.typeTv.setText(oldType);
                    binding.noteTv.setText(note);

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    String dateSTR = sdf.format(date);
                    binding.dateTv.setText(dateSTR);

                    vm.updateTransaction(transaction);
                    alert[0].dismiss();
                }else{

                    if(Double.valueOf(amount_String) <= balance  ) {

                        Transaction transaction = new Transaction(Double.valueOf(amount_String),oldType,cat_id,date,note);
                        transaction.setId(passedTransaction.getId());
                        binding.amountTv.setText(amount_String);
                        binding.typeTv.setText(oldType);
                        binding.noteTv.setText(note);

                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        String dateSTR = sdf.format(date);
                        binding.dateTv.setText(dateSTR);

                        vm.updateTransaction(transaction);
                        alert[0].dismiss();

                    }else{
                        Toast.makeText(TransactionDetails.this, "amount is not allowed!", Toast.LENGTH_SHORT).show();
                    }


                }


            }else{
                Toast.makeText(TransactionDetails.this, "Amount is not allowed!", Toast.LENGTH_SHORT).show();
            }
        }


    }
});
            cancel_BTN.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        alert[0].dismiss();
    }
});

            builder.setView(view);


            alert[0] = builder.create();
            alert[0].show();




        });


    }

    @Override
    public void onClick(Category category) {
        cat_id = category.getId();
        catName = category.getName();

    }
}