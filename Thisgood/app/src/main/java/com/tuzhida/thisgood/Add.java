package com.tuzhida.thisgood;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.tuzhida.models.P_consume;
import com.tuzhida.models.P_income;
import org.litepal.tablemanager.Connector;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by Paul-Sartre on 2015/12/6.
 */
public class Add extends AppCompatActivity {
    private EditText coin;
    private Button add_date;
    private Button add_time;
    private Button ok;
    private Button cancel;
    private RadioGroup add_gr;
    private EditText add_et;
    private Spinner add_sp;
    private String user = "";
    private int tableName = 0;
    private Date atime;
    private long timeStemp;
    private String amark = "";
    private String atype = "";
    String[] types = new String[]{"日常消费","衣服装饰", "工资奖金", "投资盈利", "出行交通",
            "娱乐聚会", "生活用品", "水电房租", "缴费清单", "股票收益", "其他"};
    Context context;
    SQLiteDatabase db = Connector.getDatabase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_NoActionBar);
        setContentView(R.layout.madd);
        context = Add.this;
        SharedPreferences sp = getSharedPreferences("MainActivity", MODE_PRIVATE);
        //name
        user = sp.getString("user", "user");
        coin = (EditText) findViewById(R.id.add_tx1);
        add_date = (Button) findViewById(R.id.add_date);
        add_time = (Button) findViewById(R.id.add_time);
        add_gr = (RadioGroup) findViewById(R.id.add_gr);
        ok = (Button) findViewById(R.id.add_ok);
        cancel = (Button) findViewById(R.id.add_cancel);
        add_et = (EditText) findViewById(R.id.add_et);
        add_sp = (Spinner) findViewById(R.id.add_sp);
        Intent a = (Intent) getIntent();
        String aa = a.getStringExtra("Result");//接收来自Myc传来的值
        if(Double.valueOf(aa)<0){
            //假如穿过来的是负数 转为正数
            aa=String.valueOf((Double.valueOf(aa)) * -1);
        }
        //金额coin
        coin.setText(aa);

        //时间atime="";
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));//获取东八区时间
        final int tyear = c.get(Calendar.YEAR); // 获取年
        final int tmonth = c.get(Calendar.MONTH); // 获取月份，0表示1月份
        final int tday = c.get(Calendar.DAY_OF_MONTH); // 获取当前天数
        final int thour=c.get(Calendar.HOUR_OF_DAY);
        final int tminute=c.get(Calendar.MINUTE);
        final int ss=c.get(Calendar.SECOND);//获取秒
        int tmonth2=tmonth+1;
        add_date.setText(tyear+"-"+tmonth2+"-"+tday);
        add_time.setText(thour+":"+tminute);
        add_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Add.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        add_date.setText(String.format("%d-%d-%d", year, monthOfYear + 1, dayOfMonth));
//                        Log.i("MainActivity","成功添加");
                    }
                }, tyear, tmonth, tday).show();
            }
        });
        add_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(Add.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        add_time.setText(String.format("%d:%d", hourOfDay, minute));
                    }
                }, thour, tminute, true).show();
            }
        });

        //表名
        add_gr.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int grid = group.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) Add.this.findViewById(grid);
                if (rb.getText().equals("收入")) {
                    tableName = 1;//0是支出1是收入
                } else {
                    tableName = 0;
                }
            }
        });
        //类型
        add_sp.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, types));
        add_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                atype = types[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //确定添加账目  时间戳是根据gmt+8:00表示的。
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //备注
                if (add_et.getText() == null) {
                    amark = "";
                } else {
                    amark = add_et.getText().toString();
                }

                String pmTime = add_date.getText().toString() + " " + add_time.getText().toString() + ":00";
//                Log.i("MainActivity",pmTime);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    atime = df.parse(pmTime);//转成utc之后时间为+8小时
//                    timeStemp=(atime.getTime()-28800000)/1000;//将时间转换成utc时间戳并去毫秒数
                    timeStemp = (atime.getTime() / 1000);
//                    Log.i("MainActivity",String.valueOf(timeStemp));
                } catch (Exception e) {
                    Log.i("MainActivity", "时间转换失败");
                }
                if (tableName == 1) {
                    P_income aincome = new P_income();
                    aincome.setName(user);
                    aincome.setCoin(Double.valueOf(coin.getText().toString()));
                    aincome.setMark(amark);
                    aincome.setType(atype);
                    aincome.setTime(timeStemp);
                    aincome.setCreates(pmTime+":"+ss);//添加创建时间字符串 精确到秒
                    aincome.save();
                    if (aincome.save()) {
                        Toast.makeText(context, "添加一笔收入", Toast.LENGTH_SHORT).show();
                        SharedPreferences sp = getSharedPreferences("MainActivity", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("last", 1);
                        editor.commit();
                    } else {
                        Toast.makeText(context, "添加失败", Toast.LENGTH_SHORT).show();
                    }
                    db.close();
                } else {
                    P_consume aconsume = new P_consume();
                    aconsume.setName(user);
                    aconsume.setCoin(Double.valueOf(coin.getText().toString()) * -1);
                    aconsume.setMark(amark);
                    aconsume.setType(atype);
                    aconsume.setTime(timeStemp);
                    aconsume.setCreates(pmTime+":"+ss);
                    aconsume.save();
                    if (aconsume.save()) {
                        Toast.makeText(context, "添加一笔支出", Toast.LENGTH_SHORT).show();
                        SharedPreferences sp = getSharedPreferences("MainActivity", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("last", 2);
                        editor.commit();
                    } else {
                        Toast.makeText(context, "添加失败", Toast.LENGTH_SHORT).show();
                    }
                    db.close();
                }
                Intent ina = new Intent(Add.this, MainActivity.class);
                startActivity(ina);
                Add.this.finish();
            }
        });
        //取消添加账目
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.close();
                Intent ina = new Intent(Add.this, MainActivity.class);
                startActivity(ina);
                Add.this.finish();
            }
        });

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            Intent in1 = new Intent(Add.this, MainActivity.class);
            Add.this.finish();
            startActivity(in1);

        }
        return super.onKeyDown(keyCode, event);
    }


}
