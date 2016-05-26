package com.tuzhida.models;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Paul-Sartre on 2015/12/13.
 */
public class Mypackage {
    private long timeStemp1;
    private long timeStemp2;
    Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));//获取utc时间
    final int tyear = c.get(Calendar.YEAR); // 获取年
    final int tmonth = c.get(Calendar.MONTH)+1; // 获取月份，0表示1月份
    final int fmonth=tmonth+1;
    SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM");
    SQLiteDatabase db = Connector.getDatabase();
    /**
     * 获取当月余额
     */
    public double getmonth_balance(){
        try {
            Date hh1=simpleDateFormat.parse(tyear + "-" + tmonth );
            Date hh2 = simpleDateFormat.parse(tyear + "-" + fmonth );
            timeStemp1=hh1.getTime()/1000;
            timeStemp2=hh2.getTime()/1000;
        }catch(Exception e){
            Log.i("MainActivity", "时间错误");
        }
        List<P_income> incomes= DataSupport.where("time between ? and ?", String.valueOf(timeStemp1), String.valueOf(timeStemp2)).find(P_income.class);
        List<P_consume> consumes= DataSupport.where("time between ? and ?",String.valueOf(timeStemp1),String.valueOf(timeStemp2)).find(P_consume.class);
        db.close();
        double in=0;
        double out=0;
        double total=0;
        for (int i=0;i<incomes.size();i++){
            in+=incomes.get(i).getCoin();
        }
        for (int j=0;j<consumes.size();j++){
            out+=consumes.get(j).getCoin();
        }
        total=in+out;
        return total;
    }
}
