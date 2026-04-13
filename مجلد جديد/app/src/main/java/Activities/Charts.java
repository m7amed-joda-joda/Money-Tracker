package Activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.myapplication111111111.R;
import com.example.myapplication111111111.databinding.ActivityChartsBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

public class Charts extends AppCompatActivity {

    ActivityChartsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityChartsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle("My Reports");
            actionBar.setElevation(20);
        }


        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new ExpenseFragment());
        fragments.add(new OverViewFragment());
        fragments.add(new IncomeFragment());


        ArrayList<String> names = new ArrayList<>();
        names.add("Expense");
        names.add("Over view");
        names.add("Income");


        FragmentAdapter adapter = new FragmentAdapter(this,fragments);
        binding.viewPager.setAdapter(adapter);

       new TabLayoutMediator(binding.tabLayout, binding.viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
           @Override
           public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {

               tab.setText(names.get(position));

           }
       }).attach();


    }
}