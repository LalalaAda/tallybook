package com.tuzhida.myview;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;



import com.tuzhida.models.P_income;

import com.tuzhida.thisgood.R;

import java.util.List;

/**
 * Created by Paul-Sartre on 2015/12/11.
 */
public class RinAdapter extends RecyclerView.Adapter<RinAdapter.Aview> {
    private List<P_income> incomes;
    public RinAdapter(List<P_income> list){
        incomes=list;
    }
    //自定义一个监听接口
    public interface MyOnItemClickListener{
        void onItemClick(View view, int position,int id);

    }
    private  MyOnItemClickListener myOnItemClickListener;
    //接收监听
    public void setOnItemClickLitener(MyOnItemClickListener mOnItemClickLitener)
    {
        this.myOnItemClickListener = mOnItemClickLitener;
    }
    @Override
    public Aview onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_item_in,parent,false);

        return new Aview(view)  ;
    }

    @Override
    public void onBindViewHolder(final Aview holder, int position) {
        holder.id=incomes.get(position).getId();
        holder.bigtype.setText("收入：");
        holder.type.setText(incomes.get(position).getType());
        holder.coin.setText(String.valueOf(incomes.get(position).getCoin()));
        holder.mark.setText(incomes.get(position).getMark());
        String date = new java.text.SimpleDateFormat("yyyy年MM月dd日-E HH:mm")
                .format(new java.util.Date((incomes.get(position).getTime()) * 1000));
        holder.time.setText(date);
        // id  监听事件处理查看修改记录 Change_in。java 自定义dialog
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myOnItemClickListener != null) {
                    int pos = holder.getLayoutPosition();
                    myOnItemClickListener.onItemClick(holder.itemView, pos, holder.id);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return incomes.size();
    }
    public static class Aview extends RecyclerView.ViewHolder {
        TextView bigtype;
        TextView type;
        TextView coin;
        TextView mark;
        TextView time;
        int id;
        public Aview(View itemView) {
            super(itemView);
            bigtype=(TextView)itemView.findViewById(R.id.aa_bigtype);
            type=(TextView)itemView.findViewById(R.id.aa_type);
            coin=(TextView)itemView.findViewById(R.id.aa_coin);
            mark=(TextView)itemView.findViewById(R.id.aa_mark);
            time=(TextView)itemView.findViewById(R.id.aa_time);

        }

    }
}

