package com.tuzhida.myview;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import com.tuzhida.thisgood.LoginActivity;

import com.tuzhida.thisgood.R;

import java.io.File;

/**
 * Created by Paul-Sartre on 2015/12/18.
 */
public class Myxinxi extends Fragment {
    private EditText qname;
    private Button xinxi_ok;
    private Button xinxi_cancel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.login03,container,false);
        qname=(EditText)rootView.findViewById(R.id.xinxi_qname);
        xinxi_ok=(Button)rootView.findViewById(R.id.xinxi_ok);
        xinxi_cancel=(Button)rootView.findViewById(R.id.xinxi_cancel);
        xinxi_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!qname.getText().toString().equals("")){
                    SharedPreferences sp = getActivity().getSharedPreferences("MainActivity",
                            Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("login", 1);//已登录
                    editor.putBoolean("isLoginto", false);
                    editor.putString("user", qname.getText().toString());
                    editor.commit();
                    Toast.makeText(getActivity(),"修改个人信息成功",Toast.LENGTH_SHORT).show();
                    Intent ina=new Intent(getActivity(), LoginActivity.class);
                    getActivity().finish();
                    startActivity(ina);

                }
            }
        });
        xinxi_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"必须填写该项内容，否则软件无法使用",Toast.LENGTH_LONG).show();
            }
        });

        return rootView;
    }

}