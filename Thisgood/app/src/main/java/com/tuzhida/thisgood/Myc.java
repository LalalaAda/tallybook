package com.tuzhida.thisgood;

import java.math.BigDecimal;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tuzhida.tools.Counts;

public class Myc extends AppCompatActivity implements View.OnClickListener {

    private TextView calcv;

    private static String fistNumber = "0";// 第一次输入的值
    private static String secondNumber = "0";// 第二次输入的值
    private static String num = "0";// 显示的结果
    private static int op = 0;//操作数
    private static int flg = 0;// dot数
    public Counts take = null;

    private int[] btidTake = {R.id.calc_idiv, R.id.calc_imu, R.id.calc_imi,
            R.id.calc_iadd};

    private Button[] buttonTake = new Button[btidTake.length];

    private int[] btidNum = {R.id.calc_i0, R.id.calc_i1, R.id.calc_i2, R.id.calc_i3,
            R.id.calc_i4, R.id.calc_i5, R.id.calc_i6, R.id.calc_i7, R.id.calc_i8, R.id.calc_i9,
            R.id.calc_idot};
    private Button[] buttons = new Button[btidNum.length];


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.calc_idot:
                if (flg == 0) {
                    num +=".";
                }
                break;
            case R.id.calc_value:
                num="0";
                break;
            case R.id.calc_i0:
                if (num.equals("0")) {
                    num = "0";
                } else {
                    num += "0";
                }
                break;
            case R.id.calc_i1:
                if (num.equals("0")) {
                    num = "1";
                } else {
                    num += "1";
                }
                break;
            case R.id.calc_i2:
                if (num.equals("0")) {
                    num = "2";
                } else {
                    num += "2";
                }
                break;
            case R.id.calc_i3:
                if (num.equals("0")) {
                    num = "3";
                } else {
                    num += "3";
                }
                break;
            case R.id.calc_i4:
                if (num.equals("0")) {
                    num = "4";
                } else {
                    num += "4";
                }
                break;
            case R.id.calc_i5:
                if (num.equals("0")) {
                    num = "5";
                } else {
                    num += "5";
                }
                break;
            case R.id.calc_i6:
                if (num.equals("0")) {
                    num = "6";
                } else {
                    num += "6";
                }
                break;
            case R.id.calc_i7:
                if (num.equals("0")) {
                    num = "7";
                } else {
                    num += "7";
                }
                break;
            case R.id.calc_i8:
                if (num.equals("0")) {
                    num = "8";
                } else {
                    num += "8";
                }
                break;
            case R.id.calc_i9:
                if (num.equals("0")) {
                    num = "9";
                } else {
                    num += "9";
                }
                break;

            case R.id.calc_iadd:
                if (op == 0) {
                    if (num.equals("0")){
                        break;
                    }
                    fistNumber = calcv.getText().toString();
                    num += "+";
                    op += 1;
                }
                break;
            case R.id.calc_imi:
                if (op == 0) {
                    if (num.equals("0")){
                        break;
                    }
                    fistNumber = calcv.getText().toString();
                    num += "-";
                    op += 2;
                }
                break;
            case R.id.calc_imu:
                if (op == 0) {
                    if (num.equals("0")){
                        break;
                    }
                    fistNumber = calcv.getText().toString();
                    num += "*";
                    op += 3;
                }
                break;
            case R.id.calc_idiv:
                if (op == 0) {
                    if (num.equals("0")){
                        break;
                    }
                    fistNumber = calcv.getText().toString();
                    num += "/";
                    op += 4;
                }
                break;
            case R.id.calc_ien:
                if (op == 0) {
                    Intent ina = new Intent();
                    ina.setClass(Myc.this, Add.class);
                    if (num.indexOf(".") == num.length() - 1) {
                        num = num.substring(0, num.length() - 1);
                        num+=".00";
//                        Log.i("MainActivity","小数点在最后面");
                    }else if(num.length()>1&&num.indexOf(".")==num.length()-2){
                        num+="0";
//                        Log.i("MainActivity","小数点在倒二");
                    }else if(num.indexOf(".")==-1){
                        num+=".00";
//                        Log.i("MainActivity","小数点没有");
                    }
                    ina.putExtra("Result", num);
                    num = "0";
                    Myc.this.finish();
                    startActivity(ina);

                } else {
                    if (op == 1) {
                        secondNumber = num.substring(num.indexOf("+") + 1, num.length());
                        num = calc(fistNumber, secondNumber, 1);
                        op = 0;
                    } else if (op == 2) {
                        secondNumber = num.substring(num.indexOf("-") + 1, num.length());
                        num = calc(fistNumber, secondNumber, 2);
                        op = 0;
                    } else if (op == 3) {
                        secondNumber = num.substring(num.indexOf("*") + 1, num.length());
                        num = calc(fistNumber, secondNumber, 3);
                        op = 0;
                    } else if (op == 4) {
                        secondNumber = num.substring(num.indexOf("/") + 1, num.length());
                        num = calc(fistNumber, secondNumber, 4);
                        op = 0;
                    }
                    calcv.setText(num);
                    op = 0;
                }
                break;
            case R.id.calc_idel:
                if (num.length() > 1) {
                    num = num.substring(0, num.length() - 1);
                } else {
                    num = "0";
                }
                break;

        }
        calcv.setText(num);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_NoActionBar);
        setContentView(R.layout.keypad);
        calcv = (TextView) findViewById(R.id.calc_value);
        calcv.setText("0");
        for (int i = 0; i < btidNum.length; i++) {
            buttons[i] = (Button) findViewById(btidNum[i]);
            buttons[i].setOnClickListener(this);
        }
        for (int i = 0; i < btidTake.length; i++) {
            buttonTake[i] = (Button) findViewById(btidTake[i]);
            buttonTake[i].setOnClickListener(this);
        }
        Button eq = (Button) findViewById(R.id.calc_ien);
        eq.setOnClickListener(this);
        Button del = (Button) findViewById(R.id.calc_idel);
        del.setOnClickListener(this);
        calcv.setOnClickListener(this);
    }

    private String calc(String n1, String n2, int iop) {
        BigDecimal number1 = new BigDecimal(n1);
        BigDecimal number2 = new BigDecimal(n2);
        BigDecimal number = BigDecimal.valueOf(0);
        switch (iop) {
            case 1:
                number = number1.add(number2);
                break;
            case 2:
                number = number1.subtract(number2);
                break;
            case 3:
                number = number1.multiply(number2);
                break;
            case 4:
                number = number1.divide(number2, 2, BigDecimal.ROUND_HALF_UP);
                break;

        }
        return number.toString();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            Intent in4=new Intent();
            in4.setClass(Myc.this, MainActivity.class);
            num="0";

            startActivity(in4);
            Myc.this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

}

