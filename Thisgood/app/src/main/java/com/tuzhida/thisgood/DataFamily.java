package com.tuzhida.thisgood;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tuzhida.bean.Family;
import com.tuzhida.models.P_consume;
import com.tuzhida.models.P_income;
import com.tuzhida.myview.FaAdapter;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Paul-Sartre on 2015/12/19.
 */
public class DataFamily extends AppCompatActivity {
    private RecyclerView mrv;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button upd;
    private long timeStemp1;
    private long timeStemp2;
    private String family="";
    private String name="";
    List<Family> mf;
    Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));//获取utc时间
    final int tyear = c.get(Calendar.YEAR); // 获取年
    final int tmonth = c.get(Calendar.MONTH)+1; // 获取月份，0表示1月份
    final int fmonth=tmonth+1;
    SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_NoActionBar);
        setContentView(R.layout.family);
        mrv=(RecyclerView)findViewById(R.id.rv_family);
        mLayoutManager=new LinearLayoutManager(this);
        mrv.setLayoutManager(mLayoutManager);
        upd=(Button)findViewById(R.id.update);

        SharedPreferences sp = getSharedPreferences("MainActivity", Context.MODE_PRIVATE);
        family=sp.getString("family","");
        name=sp.getString("user","user");
        Toast.makeText(DataFamily.this, "正在获取家庭数据中...", Toast.LENGTH_SHORT).show();
        final Thread mainT=new Thread(){
            @Override
            public void run() {
                //获取家庭数据
                BmobQuery<Family> query=new BmobQuery<>();
                query.addWhereEqualTo("family", family);
                query.findObjects(DataFamily.this, new FindListener<Family>() {
                    @Override
                    public void onSuccess(List<Family> list) {
                        mf = list;
                        Toast.makeText(DataFamily.this, "获取成功", Toast.LENGTH_SHORT).show();
                        //设置recyclerView的适配器
                        FaAdapter adapter = new FaAdapter(mf);
                        mrv.setAdapter(adapter);
                    }

                    @Override
                    public void onError(int i, String s) {
                        Toast.makeText(DataFamily.this, "获取家庭数据失败", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        };
        mainT.start();


        //同步按钮
        upd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DataFamily.this, "正在同步中...", Toast.LENGTH_SHORT).show();
                try {
                    Date hh2 = simpleDateFormat.parse(tyear + "-" + fmonth);
                    Date hh1 = simpleDateFormat.parse(tyear + "-" + tmonth);
                    timeStemp1 = hh1.getTime() / 1000;
                    timeStemp2 = hh2.getTime() / 1000;
                } catch (Exception e) {
                    Log.i("MainActivity", "时间错误");
                }
                SQLiteDatabase db = Connector.getDatabase();
                List<P_income> incomes = DataSupport.where("time between ? and ?", String.valueOf(timeStemp1), String.valueOf(timeStemp2)).find(P_income.class);
                List<P_consume> consumes = DataSupport.where("time between ? and ?", String.valueOf(timeStemp1), String.valueOf(timeStemp2)).find(P_consume.class);
                db.close();
                int su=DoUpdate(incomes, consumes, family, name);
                if (su==1) {
                    //刷新ACTIVITY
                    Intent ia = new Intent(DataFamily.this, DataFamily.class);
                    finish();
                    startActivity(ia);
                }

            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK&&event.getAction()==KeyEvent.ACTION_DOWN){
            Intent intent=new Intent(DataFamily.this,MainActivity.class);
            finish();
            startActivity(intent);

        }
        return super.onKeyDown(keyCode, event);
    }
    //将数据上传至服务器 服务器上设置 family+name+time 为主键 所以同时间的会覆盖
    private int DoUpdate(List<P_income> list1,List<P_consume> list2,String family,String user){
        Family mfamily=new Family();

        for (P_income income:list1){
            mfamily.setFamily(family);
            mfamily.setName(user);
            mfamily.setType("收入");
            mfamily.setCoin(income.getCoin());
            String date=new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm")
                    .format(new java.util.Date((income.getTime()) * 1000));
            mfamily.setTime(date);//时间为字符串类型
            mfamily.setCtime(income.getCreates());//账单创建时间字符串 精确到秒
            mfamily.save(this, new SaveListener() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailure(int i, String s) {
                    //Toast.makeText(DataFamily.this, "上传一条收入信息失败", Toast.LENGTH_SHORT).show();
                }
            });
        }
        for (P_consume consume:list2){
            mfamily.setFamily(family);
            mfamily.setName(user);
            mfamily.setType("支出");
            mfamily.setCoin(consume.getCoin());
            String date=new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm")
                    .format(new java.util.Date((consume.getTime()) * 1000));
            mfamily.setTime(date);//时间为字符串类型
            mfamily.setCtime(consume.getCreates());//账单创建时间字符串 精确到秒
            mfamily.save(this, new SaveListener() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onFailure(int i, String s) {
                    //Toast.makeText(DataFamily.this, "上传一条支出信息失败", Toast.LENGTH_SHORT).show();
                }
            });
        }
        Toast.makeText(DataFamily.this, "完成上传", Toast.LENGTH_SHORT).show();
        return 1;
    }

}
