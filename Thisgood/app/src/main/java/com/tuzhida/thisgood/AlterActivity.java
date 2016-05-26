package com.tuzhida.thisgood;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
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

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Paul-Sartre on 2015/12/14.
 */
public class AlterActivity extends AppCompatActivity {
    // 2015/12/14 获取intent内的id 并传值
    private EditText coin;
    private Button add_date;
    private Button add_time;
    private TextView add_spsp;
    private Button ok;
    private Button cancel;
    private RadioGroup add_gr;
    private RadioButton add_a;//收入
    private RadioButton add_b;//支出
    private EditText add_et;
    private Spinner add_sp;
    private String type;
    private int id;
    private String user = "";
    private int tableName = 0;
    private Date atime;
    private long timeStemp;
    private String amark = "";
    String[] types = new String[]{"日常消费","衣服装饰", "工资奖金", "投资盈利", "出行交通",
            "娱乐聚会", "生活用品", "水电房租", "缴费清单", "股票收益", "其他"};
    Context context;
    SQLiteDatabase db = Connector.getDatabase();
    private P_income income;//收入表
    private P_consume consume;//支出表
    int year;
    int month;
    int day;
    int hour;
    int minute;
    boolean n=false;//判断spinner选中与否
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_NoActionBar);
        setContentView(R.layout.mchange);
        context = AlterActivity.this;
        add_spsp=(TextView)findViewById(R.id.add_spsp);
        coin = (EditText) findViewById(R.id.add_tx1);//金额
        add_date = (Button) findViewById(R.id.add_date);//日期
        add_time = (Button) findViewById(R.id.add_time);//时间
        add_gr = (RadioGroup) findViewById(R.id.add_gr);//收入支出
        add_a = (RadioButton) findViewById(R.id.add_a);//收入
        add_b = (RadioButton) findViewById(R.id.add_b);//支出
        ok = (Button) findViewById(R.id.add_ok);
        cancel = (Button) findViewById(R.id.add_cancel);
        add_et = (EditText) findViewById(R.id.add_et);//备注
        add_sp = (Spinner) findViewById(R.id.add_sp);//类型
        Intent x = (Intent) getIntent();
        tableName = x.getIntExtra("table", 0);//0是支出1是收入
        id = x.getIntExtra("id", 0);
        Calendar c = Calendar.getInstance(TimeZone.getTimeZone("GMT+00:00"));
        if (tableName == 0) {
            consume = DataSupport.find(P_consume.class, id);
            coin.setText(String.valueOf(consume.getCoin() * -1));//金额设置
            user = consume.getName();
            c.setTime(new java.util.Date((consume.getTime()) * 1000));
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH) + 1;
            day = c.get(Calendar.DAY_OF_MONTH);
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
            type = consume.getType();

            add_spsp.setText(type);
            add_et.setText(consume.getMark().toString());
            add_b.setChecked(true);
            add_date.setText(year + "-" + month + "-" + day);
            add_time.setText(hour+":"+minute);

        } else {
            income = DataSupport.find(P_income.class, id);
            coin.setText(String.valueOf(income.getCoin()));//金额设置
            user = income.getName();
            c.setTime(new java.util.Date((income.getTime()) * 1000));
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH) + 1;
            day = c.get(Calendar.DAY_OF_MONTH);
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
            type = income.getType();

            add_spsp.setText(type);

            add_et.setText(income.getMark().toString());
            add_a.setChecked(true);
            add_date.setText(year+"-"+month+"-"+day);
            add_time.setText(hour+":"+minute);
        }
        //日期时间
        add_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AlterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        add_date.setText(String.format("%d-%d-%d", year, monthOfYear + 1, dayOfMonth));
                    }
                }, year, month, day).show();
            }
        });
        add_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(AlterActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        add_time.setText(String.format("%d:%d", hourOfDay, minute));
                    }
                }, hour, minute, true).show();
            }
        });
        //表名
        add_gr.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int grid = group.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) AlterActivity.this.findViewById(grid);
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
                if (n) {
                    type = types[position];
                    add_spsp.setText(type);
                }
                n=true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                n=false;
            }
        });
        //确定修改账目
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
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    atime = df.parse(pmTime);
                    timeStemp = (atime.getTime() / 1000);
                } catch (Exception e) {
                    Log.i("MainActivity", "时间转换失败");
                }
                if (tableName == 1) {

                    ContentValues values=new ContentValues();
                    values.put("coin",Double.valueOf(coin.getText().toString()));
                    values.put("name",user);
                    values.put("time",timeStemp);
                    values.put("type",type);
                    values.put("mark", amark);
                    int a=DataSupport.update(P_income.class,values,id);
                    if (a>0) {
                        Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "修改失败", Toast.LENGTH_SHORT).show();
                    }
                    db.close();
                } else {
                    ContentValues values=new ContentValues();
                    values.put("coin", Double.valueOf(coin.getText().toString())*-1);
                    values.put("name", user);
                    values.put("time", timeStemp);
                    values.put("type", type);
                    values.put("mark", amark);
                    int b=DataSupport.update(P_consume.class,values,id);
                    if (b>0) {
                        Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "修改失败", Toast.LENGTH_SHORT).show();
                    }
                    db.close();
                }
                Intent ina = new Intent(AlterActivity.this, MainActivity.class);
                AlterActivity.this.finish();
                startActivity(ina);

            }
        });
        //取消添加账目
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.close();
                Intent ina = new Intent(AlterActivity.this, MainActivity.class);
                AlterActivity.this.finish();
                startActivity(ina);

            }
        });
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            Intent in1 = new Intent(AlterActivity.this, MainActivity.class);
            AlterActivity.this.finish();
            startActivity(in1);

        }
        return super.onKeyDown(keyCode, event);
    }
}
