package com.example.htn;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;

import java.util.ArrayList;
import java.util.List;

public class SecondFragment extends Fragment {

    //Chart stuff
    AnyChartView anyChartView;
    String[] labels = {"Grocery","Rocket League","Home"};
    int[] proportions = {30,80,10};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_second, container, false);
        // chart stuff

        anyChartView = view.findViewById(R.id.any_chart_view);
        setupChart();


        return view;
    }

    public void setupChart() {
        Pie pie = AnyChart.pie();
        List<DataEntry> dataEntryList = new ArrayList<>();

        for (int i = 0; i < proportions.length; i++) {
            dataEntryList.add(new ValueDataEntry(labels[i], proportions[i]));
        }
        pie.data(dataEntryList);
        anyChartView.setChart(pie);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });
    }
}