package Activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication111111111.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import DataBase.MonthSum;
import DataBase.MyViewModel;
import DataBase.utils_db;


public class ExpenseFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private BarChart barChart;

    public ExpenseFragment() {
        // Required empty public constructor
    }



    public static ExpenseFragment newInstance(String param1, String param2) {
        ExpenseFragment fragment = new ExpenseFragment();
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
        return inflater.inflate(R.layout.fragment_expense, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        barChart = view.findViewById(R.id.barChart);

        setupBarChart();
    }


    private void setupBarChart(){

        MyViewModel viewModel = new ViewModelProvider(requireActivity()).get(MyViewModel.class);

        String type = utils_db.TYPE_EXPENSE;

        viewModel.getMonthlyExpenses(type).observe(getViewLifecycleOwner(), list -> {

            List<BarEntry> entries = new ArrayList<>();

            for(MonthSum item : list){
                int monthIndex = Integer.parseInt(item.month); // "01" -> 1
                entries.add(new BarEntry(monthIndex, (float)item.total));
            }

            BarDataSet dataSet = new BarDataSet(entries, type.equals("expense") ? "Expenses per Month" : "Income per Month");
            dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
            dataSet.setValueTextSize(12f);

            BarData data = new BarData(dataSet);
            barChart.setData(data);
            barChart.invalidate();


            XAxis xAxis = barChart.getXAxis();
            xAxis.setValueFormatter(new IndexAxisValueFormatter(new String[]{
                    "", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
            }));
            xAxis.setGranularity(1f);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);


            Legend legend = barChart.getLegend();
            legend.setTextColor(ContextCompat.getColor(requireActivity(),R.color.White_black_color));

            xAxis.setTextColor(ContextCompat.getColor(requireActivity(),R.color.White_black_color));

            YAxis leftAxis = barChart.getAxisLeft();
            leftAxis.setTextColor(ContextCompat.getColor(requireActivity(), R.color.White_black_color));

            YAxis rightAxis = barChart.getAxisRight();
            rightAxis.setTextColor(ContextCompat.getColor(requireActivity(), R.color.White_black_color));


            dataSet.setValueTextColor(ContextCompat.getColor(requireActivity(), R.color.White_black_color));


            float offset = TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    10,
                    getResources().getDisplayMetrics()
            );

            barChart.setExtraOffsets(0f, 0f, 0f, offset);


            barChart.getDescription().setEnabled(false);
        });
    }


}