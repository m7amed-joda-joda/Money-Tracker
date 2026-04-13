package Activities;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.myapplication111111111.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DataBase.Category;
import DataBase.CategorySum;
import DataBase.MyViewModel;
import DataBase.utils_db;

public class OverViewFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private PieChart pieChart;
    private RadioGroup RadioGroup;


    public OverViewFragment() {
    }



    public static OverViewFragment newInstance(String param1, String param2) {
        OverViewFragment fragment = new OverViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_over_view, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        pieChart = view.findViewById(R.id.pieChart);
        RadioGroup = view.findViewById(R.id.radioGroup);
        setupChart(utils_db.TYPE_EXPENSE);
        RadioGroup.setOnCheckedChangeListener((group, checkedId) -> {

            if(checkedId == R.id.expenseRB){
                setupChart(utils_db.TYPE_EXPENSE);

            } else if(checkedId == R.id.incomeRB){
                setupChart(utils_db.TYPE_INCOME);
            }
        });



    }



    private void setupChart(String type){

        MyViewModel viewModel = new ViewModelProvider(requireActivity()).get(MyViewModel.class);

        Map<Integer, String> categoryMap = new HashMap<>();


        viewModel.getAllCategories().observe(getViewLifecycleOwner(), categories -> {

            categoryMap.clear();

            for(Category cat : categories){
                categoryMap.put(cat.getId(), cat.getName());
            }


            setChartData(viewModel, categoryMap, type);
        });
    }
    private void setChartData(MyViewModel viewModel, Map<Integer, String> categoryMap, String type){





        viewModel.getCategorySummary(type).observe(getViewLifecycleOwner(), list -> {

            List<PieEntry> entries = new ArrayList<>();

            for(CategorySum item : list){

                String name = categoryMap.get(item.categoryId);

                if(name == null){
                    name = "Unknown (" + item.categoryId + ")";
                }

                entries.add(new PieEntry((float) item.total, name));
            }

            List<Integer> colors = new ArrayList<>();

            for (int c : ColorTemplate.MATERIAL_COLORS) {
                colors.add(c);
            }

            for (int c : ColorTemplate.COLORFUL_COLORS) {
                colors.add(c);
            }

            for (int c : ColorTemplate.JOYFUL_COLORS) {
                colors.add(c);
            }


            PieDataSet dataSet = new PieDataSet(entries, type);
            dataSet.setColors(colors);
            dataSet.setValueFormatter(new PercentFormatter(pieChart));
            dataSet.setValueTextSize(8f);
            dataSet.setValueTextColor(Color.WHITE);

            pieChart.getDescription().setEnabled(false);
            pieChart.setCenterText(type);
            pieChart.setCenterTextSize(22f);

            Legend legend = pieChart.getLegend();
            legend.setTextColor(ContextCompat.getColor(requireActivity(),R.color.White_black_color));

            PieData data = new PieData(dataSet);

            pieChart.setUsePercentValues(true);
            pieChart.setData(data);
            pieChart.invalidate();
        });
    }
}