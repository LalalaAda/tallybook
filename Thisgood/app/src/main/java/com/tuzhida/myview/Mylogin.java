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

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;


/**
 * Created by Paul-Sartre on 2015/12/18.
 */
public class Mylogin extends Fragment {
    private EditText login_user;
    private EditText login_password;
    private Button login_ok;
    private Button login_cancel;
    private TextView login_zhuce;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.login01,container,false);
        login_user=(EditText)rootView.findViewById(R.id.login_user_input);
        login_password=(EditText)rootView.findViewById(R.id.login_password_input);
        login_ok=(Button)rootView.findViewById(R.id.login_ok);
        login_cancel=(Button)rootView.findViewById(R.id.login_cancel);
        login_zhuce=(TextView)rootView.findViewById(R.id.login_zhuce);
        login_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username=login_user.getText().toString();
                String password=login_password.getText().toString();
                if (username.equals("") || password.equals("")) {
                    Toast.makeText(getActivity(), "用户名或密码不能为空", Toast.LENGTH_LONG).show();
                    return;
                }else {
                    Logina(username,password);
                }

            }
        });
        login_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });
        login_zhuce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getActivity().getSharedPreferences("MainActivity",
                        Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("login", 2);//转注册
                editor.commit();
                Intent ina=new Intent(getActivity(), LoginActivity.class);
                startActivity(ina);
                getActivity().finish();
            }
        });
        return rootView;
    }
    private void Logina(String name, final String pas){
        BmobQuery<Person> query=new BmobQuery<>();
        query.addWhereEqualTo("name", name);
        query.findObjects(getActivity(), new FindListener<Person>() {
            @Override
            public void onSuccess(List<Person> list) {
                for (Person person : list) {
                    if (person.getPassword().equals(pas)) {
                        SharedPreferences sp = getActivity().getSharedPreferences("MainActivity",
                                Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("family", person.getFamily());//添加家庭信息
                        editor.putInt("login", 1);//登录标志为已登录1
                        editor.putBoolean("isLoginto", true);//第一次登录过来的
                        editor.commit();
                        Toast.makeText(getActivity(),"登录成功",Toast.LENGTH_LONG).show();
                        Intent ina=new Intent(getActivity(), LoginActivity.class);
                        getActivity().finish();
                        startActivity(ina);

                    }
                }

            }
            @Override
            public void onError(int i, String s) {
                Toast.makeText(getActivity(),"登录失败",Toast.LENGTH_LONG).show();
            }
        });
    }

}
