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
import android.widget.TextView;
import android.widget.Toast;

import com.tuzhida.bean.Person;
import com.tuzhida.thisgood.LoginActivity;
import com.tuzhida.thisgood.R;

import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Paul-Sartre on 2015/12/19.
 */
public class Myregister extends Fragment {
    private EditText zname;
    private EditText zpas01;
    private EditText zpas02;
    private EditText zmname;
    private Button zok;
    private Button zcancel;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.login02,container,false);
        zname=(EditText)rootView.findViewById(R.id.login_user_input);
        zpas01=(EditText)rootView.findViewById(R.id.login_password_input);
        zpas02=(EditText)rootView.findViewById(R.id.login_password_input2);
        zmname=(EditText)rootView.findViewById(R.id.login_mmname);
        zok=(Button)rootView.findViewById(R.id.login_ok);
        zcancel=(Button)rootView.findViewById(R.id.login_cancel);
        zok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (zpas01.getText().toString().equals(zpas02.getText().toString())){
                    if (zmname.getText().toString().equals("")){
                        Toast.makeText(getActivity(),"请填写昵称",Toast.LENGTH_SHORT).show();
                    }else {
                        Register(zname.getText().toString(), zpas01.getText().toString(),zmname.getText().toString());
                    }
                }else {
                    Toast.makeText(getActivity(),"两次密码不同",Toast.LENGTH_SHORT).show();
                }

            }
        });
        zcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getActivity().getSharedPreferences("MainActivity",
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("login", 0);//转回登录
                editor.commit();
                Intent ina=new Intent(getActivity(),LoginActivity.class);
                startActivity(ina);
                getActivity().finish();
            }
        });
        return rootView;
    }
    private void Register(final String name,String pas, final String qname){
        Person person=new Person();
        person.setName(name);
        person.setPassword(pas);
        person.setFamily(name);//家庭名即账号
        person.save(getActivity(), new SaveListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(getActivity(),"恭喜您注册成功",Toast.LENGTH_LONG).show();
                SharedPreferences sp = getActivity().getSharedPreferences("MainActivity",
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("family", name);
                editor.putString("user", qname);
                editor.putInt("login", 1);
                editor.putBoolean("isLoginto", false);
                editor.commit();
                Intent ina=new Intent(getActivity(), LoginActivity.class);
                getActivity().finish();
                startActivity(ina);

            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(getActivity(),"注册失败",Toast.LENGTH_LONG).show();
            }
        });
    }
}
