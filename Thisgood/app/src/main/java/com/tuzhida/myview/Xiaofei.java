package com.tuzhida.myview;

import android.app.Fragment;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.tuzhida.models.P_consume;
import com.tuzhida.models.P_income;
import com.tuzhida.thisgood.MyApplication;
import com.tuzhida.thisgood.R;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by Paul-Sartre on 2015/12/17.
 */
public class Xiaofei extends Fragment {
    private static Context acontext;
    private SQLiteDatabase db= Connector.getDatabase();
    private long timeStemp1;
    private long timeStemp2;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        acontext= MyApplication.getsContext();
    }
    Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));//获取utc时间
    final int tyear = c.get(Calendar.YEAR); // 获取年
    final int tmonth = c.get(Calendar.MONTH)+1; // 获取月份，0表示1月份
    final int fmonth=tmonth+1;
    final int days=c.getActualMaximum(Calendar.DAY_OF_MONTH);//获取该月多少天即最大天数
    SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM");
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.myxiaofei, container, false);
        mLineChart = (LineChart) rootView.findViewById(R.id.spread_line_chart);


        LineData mLineData = getLineData(days);
        showChart(mLineChart, mLineData, Color.rgb(114, 188, 223));
        return rootView;
    }
    private LineChart mLineChart;

    // 设置显示的样式
    private void showChart(LineChart lineChart, LineData lineData, int color) {
        lineChart.setDrawBorders(false);  //是否在折线图上添加边框
        lineChart.setDescription("");// 数据描述
        // 如果没有数据的时候，会显示这个，类似listview的emtpyview
        lineChart.setNoDataTextDescription("暂无数据");
        lineChart.setDrawGridBackground(false); // 是否显示表格颜色
        lineChart.setGridBackgroundColor(Color.WHITE & 0x70FFFFFF); // 表格的的颜色，在这里是是给颜色设置一个透明度
        lineChart.setTouchEnabled(true); // 设置是否可以触摸
        lineChart.setDragEnabled(true);// 是否可以拖拽
        lineChart.setScaleEnabled(true);// 是否可以缩放
        lineChart.setPinchZoom(false);//
        lineChart.setBackgroundColor(color);// 设置背景
        XAxis xAxis= lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置 x轴在下方
        lineChart.setData(lineData); // 设置数据

        Legend mLegend = lineChart.getLegend(); // 设置比例图标示，就是那个一组y的value的


        mLegend.setForm(Legend.LegendForm.CIRCLE);// 样式
        mLegend.setFormSize(6f);// 字体
        mLegend.setTextColor(Color.WHITE);// 颜色

        lineChart.animateX(2500); // 立即执行的动画,x轴
    }

    /**
     * 生成一个数据
     * @param count 表示图表中有多少个坐标点
     * @return
     */
    private LineData getLineData(int count) {
        ArrayList<String> xValues = new ArrayList<String>();
        for (int i = 1; i <= count; i++) {
            // x轴显示的数据，这里默认使用数字下标显示
            xValues.add("" + i);
        }

        // y轴的数据
        ArrayList<Entry> yValues = new ArrayList<Entry>();
        Map<Integer,Double> tmap=getConsumes(1);
        for (int i = 0; i < count; i++) {
            float value = (float) (tmap.get(i)*1);
            yValues.add(new Entry(value, i));
        }

        // y轴的数据集合
        LineDataSet lineDataSet = new LineDataSet(yValues, "" /*显示在比例图上*/);
        // mLineDataSet.setFillAlpha(110);
        // mLineDataSet.setFillColor(Color.RED);

        //用y轴的集合来设置参数
        lineDataSet.setLineWidth(1.75f); // 线宽
        lineDataSet.setCircleSize(3f);// 显示的圆形大小
        lineDataSet.setColor(Color.RED);// 显示颜色
        lineDataSet.setCircleColor(Color.RED);// 圆形的颜色
        lineDataSet.setHighLightColor(Color.RED); // 高亮的线的颜色

        ArrayList<LineDataSet> lineDataSets = new ArrayList<>();
        lineDataSets.add(lineDataSet); // add the datasets

        // create a data object with the datasets
        LineData lineData = new LineData(xValues, lineDataSets);

        return lineData;
    }
    //获取每天数据
    private Map<Integer,Double> getConsumes(int a){
        Map<Integer,Double> amap=new HashMap<>();
        try {
            Date hh2 = simpleDateFormat.parse(tyear + "-" + fmonth );
            Date hh1=simpleDateFormat.parse(tyear + "-" + tmonth );
            timeStemp1=hh1.getTime()/1000;
            timeStemp2=hh2.getTime()/1000;
//            Log.i("MainActivity","最小月"+String.valueOf(timeStemp1));
//            Log.i("MainActivity", "最大月"+String.valueOf(timeStemp2));
        }catch(Exception e){
            Log.i("MainActivity", "时间错误");
        }
        List<P_consume> consumes= DataSupport.where("time between ? and ?",String.valueOf(timeStemp1),String.valueOf(timeStemp2)).find(P_consume.class);
        db.close();
        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
        long[] aa=new long[days];
        for (int i=1;i<=days;i++){
            try {
                Date hh1 = sdf.parse(tyear + "-" + tmonth + "-" + i);
                aa[i-1]=hh1.getTime()/1000;
            }catch (Exception e){
                Log.i("Xiaofei","每日时间错误");
            }
        }
        double[] bb=new double[days];//存储每日金额
        for (int i=0;i<days;i++){
            bb[i]=0.0;
        }
        for (int i=0;i<consumes.size();i++){
            for (int j=1;j<days;j++){
                if (consumes.get(i).getTime()<aa[j]){bb[j-1]+=(consumes.get(i).getCoin())*-1;break;}
            }
        }
        for (int i=0;i<days;i++){
            amap.put(i,bb[i]);
        }
        return amap;
    }
}
