package com.tuzhida.thisgood;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tuzhida.models.Mypackage;
import com.tuzhida.models.P_consume;
import com.tuzhida.models.P_income;
import com.tuzhida.myview.Budget;
import com.tuzhida.myview.LoginOut;
import com.tuzhida.myview.MyProcessBar;
import com.tuzhida.myview.Theday;
import com.tuzhida.myview.Xiaofei;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.math.BigDecimal;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,Budget.onBudgetListener {

    private TextView top_bug;
    private TextView totalMoney;
    private TextView recent01;//类型
    private TextView recent02;//金额
    private MyProcessBar mpb;
    private double mbudget=0;//预算
    private Mypackage mypackage;
    private ImageView headImg;//头像
    private TextView userName;//用户名
    private TextView userDescription;//用户 我设定为所在家庭组名
    //2015/12/17 消费走势图 折线图
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        top_bug=(TextView)findViewById(R.id.top_bug);
        totalMoney = (TextView) findViewById(R.id.top_tv);
        recent01 = (TextView) findViewById(R.id.center_tv01);
        recent02 = (TextView) findViewById(R.id.center_tv02);
        mpb=(MyProcessBar)findViewById(R.id.budge_pb);
        final FragmentTransaction ft=getFragmentManager().beginTransaction();
        final SQLiteDatabase db = Connector.getDatabase();
        //用于存储本地用户名
        SharedPreferences sp = getSharedPreferences("MainActivity", Context.MODE_PRIVATE);

        boolean isLoginto=sp.getBoolean("isLoginto",true);//判断是否为第一次登录进来的
        final int last=sp.getInt("last", 1);
        mbudget = sp.getFloat("budget", 0);
        if (isLoginto){
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt("login", 3);//转到个人信息页面
            editor.commit();
            Intent ina=new Intent(MainActivity.this,LoginActivity.class);
            MainActivity.this.finish();
            startActivity(ina);

        }
        top_bug.setText("本月预算："+mbudget);
        final Thread mianT = new Thread() {
            @Override
            public void run() {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //根据值选择查询哪张表
                        if (last==1){
                            P_income income_l = DataSupport.findLast(P_income.class);
                            if (income_l != null) {
                                recent01.setText(income_l.getType());
                                recent02.setText(String.valueOf(income_l.getCoin()));
                            }
                        }else if (last==2){
                            P_consume consume_l=DataSupport.findLast(P_consume.class);
                            if (consume_l!=null){
                                recent01.setText(consume_l.getType());
                                recent02.setText(String.valueOf(consume_l.getCoin()));
                            }
                        }
                        if (mypackage==null){
                            mypackage=new Mypackage();
                            double tot=mypackage.getmonth_balance();
                            totalMoney.setText(String.valueOf(mbudget + tot));
                            String pro="0";
                            if (mbudget!=0) {
                                pro = "" + new BigDecimal(((tot + mbudget) / mbudget) * 100).setScale(0, BigDecimal.ROUND_HALF_UP);
                            }
                            mpb.setProgress(Integer.valueOf(pro));
                        }
                        ft.add(R.id.xiaofei,new Xiaofei()).commit();//添加消费趋势图
                        db.close();

                    }
                });
            }
        };
        mianT.start();
        //设置预算
        totalMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Budget().show(getFragmentManager(),"弹出预算设置dialog");
            }
        });

        //浮动的加
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iKeypad = new Intent();
                iKeypad.setClass(getApplicationContext(), Myc.class);

                startActivity(iKeypad);
                MainActivity.this.finish();

            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView=navigationView.getHeaderView(0);
        headImg=(ImageView)headerView.findViewById(R.id.userImage);
        userName=(TextView)headerView.findViewById(R.id.userName);
        userDescription=(TextView)headerView.findViewById(R.id.userDesc);
        userName.setText(sp.getString("user","user"));
        userDescription.setText(sp.getString("family","暂无家庭组"));
        //点击登录按钮 即头像
//        headerView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
//                startActivity(intent);
//                MainActivity.this.finish();
//            }
//        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            finish();
            System.exit(0);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Intent intent = new Intent(MainActivity.this, SeeActivity.class);
        if (id == R.id.nav_today) {
            intent.putExtra("vv", 1);
            MainActivity.this.finish();
            startActivity(intent);

        } else if (id == R.id.nav_month) {
            intent.putExtra("vv", 2);
            MainActivity.this.finish();
            startActivity(intent);

        } else if (id == R.id.nav_year) {
            intent.putExtra("vv", 3);
            MainActivity.this.finish();
            startActivity(intent);

        } else if (id == R.id.nav_week){
            intent.putExtra("vv", 4);
            MainActivity.this.finish();
            startActivity(intent);

        }else if (id==R.id.nav_tu){
            Intent intent1=new Intent(MainActivity.this,ChartActivity.class);
            MainActivity.this.finish();
            startActivity(intent1);

        } else if (id == R.id.nav_home) {
            Intent intent1=new Intent(MainActivity.this,DataFamily.class);
            MainActivity.this.finish();
            startActivity(intent1);
        }else if(id==R.id.nav_out){
            //注销
            new LoginOut().show(getFragmentManager(),"loginout");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBudgetInput(String budget) {
        if (Double.valueOf(budget)>0){
            mbudget=Double.valueOf(budget);
            Toast.makeText(this,"当月预算为"+mbudget,Toast.LENGTH_SHORT).show();
//        Log.i("MainActivity","预算为"+mbudget);
            SharedPreferences sp = getSharedPreferences("MainActivity", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putFloat("budget", (float) mbudget);
            editor.commit();
            //刷新页面
            finish();
            Intent ir=new Intent(MainActivity.this,MainActivity.class);
            startActivity(ir);
        }else {
            Toast.makeText(this,"该程序暂不支持预算为负，请重新设置预算",Toast.LENGTH_SHORT).show();
        }

    }
}
