package com.tuzhida.myview;

import android.app.Fragment;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tuzhida.models.P_consume;
import com.tuzhida.models.P_income;
import com.tuzhida.thisgood.MyApplication;
import com.tuzhida.thisgood.R;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class Theyear extends Fragment {
    private TextView what;
    private RecyclerView mrv;
    private RecyclerView mrv2;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.LayoutManager mLayoutManager2;
    private static Context acontext;
    private long timeStemp1;
    private long timeStemp2;
    SQLiteDatabase db = Connector.getDatabase();
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        acontext= MyApplication.getsContext();
    }
    Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));//获取utc时间
    final int tyear = c.get(Calendar.YEAR); // 获取年
    final int fyear=tyear+1;
    SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy");
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.theday,container,false);
        mrv=(RecyclerView)rootView.findViewById(R.id.rv_td01);
        mrv2=(RecyclerView)rootView.findViewById(R.id.rv_td02);
        what=(TextView)rootView.findViewById(R.id.see_what);
        what.setText("本年详情");
        mLayoutManager=new LinearLayoutManager(acontext);
        mLayoutManager2=new LinearLayoutManager(acontext);
        mrv.setLayoutManager(mLayoutManager);
        mrv2.setLayoutManager(mLayoutManager2);
        try {
            Date hh2 = simpleDateFormat.parse(fyear+"");
            Date hh1=simpleDateFormat.parse(tyear+"");
            timeStemp1=hh1.getTime()/1000;
            timeStemp2=hh2.getTime()/1000;
//            Log.i("MainActivity","最小年"+String.valueOf(timeStemp1));
//            Log.i("MainActivity", "最大年"+String.valueOf(timeStemp2));
        }catch(Exception e){
            Log.i("MainActivity","时间错误");
        }
        List<P_income> incomes= DataSupport.where("time between ? and ?", String.valueOf(timeStemp1), String.valueOf(timeStemp2)).find(P_income.class);
        List<P_consume> consumes= DataSupport.where("time between ? and ?",String.valueOf(timeStemp1),String.valueOf(timeStemp2)).find(P_consume.class);
        db.close();
        RinAdapter adapter=new RinAdapter(incomes);
        adapter.setOnItemClickLitener(new RinAdapter.MyOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position,int id) {
                new Change_in().show(getFragmentManager(),String.valueOf(id));
            }
        });
        RoutAdapter adapter1=new RoutAdapter(consumes);
        adapter1.setOnItemClickLitener(new RoutAdapter.MyOnItemClickListener(){
            @Override
            public void onItemClick(View view, int position, int id) {
                new Change_out().show(getFragmentManager(),String.valueOf(id));
            }
        });
        mrv.setAdapter(adapter);
        mrv2.setAdapter(adapter1);
        return rootView;
    }


}