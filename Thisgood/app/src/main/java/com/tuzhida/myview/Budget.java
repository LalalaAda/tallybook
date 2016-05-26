package com.tuzhida.myview;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tuzhida.thisgood.MainActivity;
import com.tuzhida.thisgood.MyApplication;
import com.tuzhida.thisgood.R;

/**
 * Created by Paul-Sartre on 2015/12/13.
 */
public class Budget extends DialogFragment {
    private EditText tm_budget;
    private Button tm_cancel;
    private Button tm_ok;
    public interface onBudgetListener{
        void onBudgetInput(String budget);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题
        View view=inflater.inflate(R.layout.budget,container);
        tm_budget=(EditText)view.findViewById(R.id.tm_budget);
        tm_cancel=(Button)view.findViewById(R.id.tm_cancel);
        tm_ok=(Button)view.findViewById(R.id.tm_ok);
        tm_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tm_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBudgetListener listener=(onBudgetListener)getActivity();
                listener.onBudgetInput(tm_budget.getText().toString());
                dismiss();
            }
        });
        return view;
    }

}
