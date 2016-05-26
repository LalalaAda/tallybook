package com.tuzhida.thisgood;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.Toast;

import com.tuzhida.myview.Change_in;
import com.tuzhida.myview.Change_out;
import com.tuzhida.myview.Theday;
import com.tuzhida.myview.Themonth;
import com.tuzhida.myview.Theweek;
import com.tuzhida.myview.Theyear;

/**
 * Created by Paul-Sartre on 2015/12/6.
 */
public class SeeActivity extends AppCompatActivity implements Change_in.ChangeInListener,Change_out.ChangeOutListener{
    private int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_NoActionBar);
        setContentView(R.layout.see);
        FragmentTransaction ft=getFragmentManager().beginTransaction();
        Intent inget=(Intent)getIntent();
        i=inget.getIntExtra("vv", 0);
        switch (i){
            case 1:
                ft.add(R.id.see,new Theday()).commit();
                break;
            case 2:
                ft.add(R.id.see,new Themonth()).commit();
                break;
            case 3:
                ft.add(R.id.see,new Theyear()).commit();
                break;
            case 4:
                ft.add(R.id.see,new Theweek()).commit();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK&&event.getAction()==KeyEvent.ACTION_DOWN){
            Intent intent=new Intent(SeeActivity.this,MainActivity.class);
            finish();
            startActivity(intent);

        }
        return super.onKeyDown(keyCode, event);
    }
    //接收来自DialogFragment的接口回调
    @Override
    public void onInChange(int j) {
        //当删除成功直接刷新activity中的fragment
        if (j>0){
            Toast.makeText(SeeActivity.this,"删除一条记录成功",Toast.LENGTH_SHORT).show();
            FragmentTransaction ft=getFragmentManager().beginTransaction();
            switch (i){
                case 1:
                    ft.replace(R.id.see,new Theday()).commit();
                    break;
                case 2:
                    ft.replace(R.id.see,new Themonth()).commit();
                    break;
                case 3:
                    ft.replace(R.id.see,new Theyear()).commit();
                    break;
                case 4:
                    ft.replace(R.id.see,new Theweek()).commit();
                    break;
            }

        }
    }

    @Override
    public void onOutChange(int j) {
        if (j>0){
            Toast.makeText(SeeActivity.this,"删除一条记录成功",Toast.LENGTH_SHORT).show();
            FragmentTransaction ft=getFragmentManager().beginTransaction();
            switch (i){
                case 1:
                    ft.replace(R.id.see, new Theday()).commit();
                    break;
                case 2:
                    ft.replace(R.id.see, new Themonth()).commit();
                    break;
                case 3:
                    ft.replace(R.id.see, new Theyear()).commit();
                    break;
                case 4:
                    ft.replace(R.id.see,new Theweek()).commit();
                    break;
            }
        }
    }
}
