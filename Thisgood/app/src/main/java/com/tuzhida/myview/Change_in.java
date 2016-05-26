package com.tuzhida.myview;

import android.app.DialogFragment;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.tuzhida.models.P_income;
import com.tuzhida.thisgood.AlterActivity;
import com.tuzhida.thisgood.MyApplication;
import com.tuzhida.thisgood.R;
import com.tuzhida.thisgood.SeeActivity;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;


/**
 * Created by Paul-Sartre on 2015/12/13.
 */
public class Change_in extends DialogFragment{
    private TextView cha_big;
    private TextView cha_coin;
    private TextView cha_type;
    private TextView cha_time;
    private TextView cha_beizhu;
    private Button cha_xiugai;
    private Button cha_shanchu;
    private Button cha_ok;
    public interface ChangeInListener{
        void onInChange(int i);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final int id=Integer.valueOf(getTag());
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题
        View view=inflater.inflate(R.layout.item_dialog,container);
        cha_big=(TextView)view.findViewById(R.id.cha_big);
        cha_coin=(TextView)view.findViewById(R.id.cha_coin);
        cha_type=(TextView)view.findViewById(R.id.cha_type);
        cha_time=(TextView)view.findViewById(R.id.cha_time);
        cha_beizhu=(TextView)view.findViewById(R.id.cha_beizhu);
        cha_xiugai=(Button)view.findViewById(R.id.cha_xiugai);
        cha_shanchu=(Button)view.findViewById(R.id.cha_shanchu);
        cha_ok=(Button)view.findViewById(R.id.cha_ok);
        SQLiteDatabase db = Connector.getDatabase();
        P_income income= DataSupport.find(P_income.class,id);
        cha_big.setText("收入");
        cha_coin.setText(income.getCoin()+"");
        cha_type.setText(income.getType());
        String date = new java.text.SimpleDateFormat("yyyy年MM月dd日-E HH:mm")
                .format(new java.util.Date((income.getTime()) * 1000));
        cha_time.setText(date);
        cha_beizhu.setText(income.getMark());
        cha_xiugai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //转一个修改Actciity
                Intent ina=new Intent(getActivity(), AlterActivity.class);
                ina.putExtra("id", id);
                ina.putExtra("table",1);//0是支出1是收入
                startActivity(ina);
                dismiss();
                getActivity().finish();
            }
        });
        cha_shanchu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i=DataSupport.delete(P_income.class,id);
                ChangeInListener cl=(ChangeInListener)getActivity();
                cl.onInChange(i);
                dismiss();
            }
        });
        cha_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        db.close();
        return view;
    }
}
