package com.tuzhida.myview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tuzhida.models.P_consume;
import com.tuzhida.thisgood.R;

import java.util.List;

/**
 * Created by Paul-Sartre on 2015/12/11.
 */
public class RoutAdapter extends RecyclerView.Adapter<RoutAdapter.Aview> {
    private List<P_consume> consumes;
    public RoutAdapter(List<P_consume> list){
        consumes=list;
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
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_item_out,parent,false);
        return new Aview(view)  ;
    }

    @Override
    public void onBindViewHolder(final Aview holder, int position) {
        holder.id=consumes.get(position).getId();
        holder.bigtype.setText("支出：");
        holder.type.setText(consumes.get(position).getType());
        holder.coin.setText(String.valueOf(consumes.get(position).getCoin()));
        holder.mark.setText(consumes.get(position).getMark());
        String date = new java.text.SimpleDateFormat("yyyy年MM月dd日-E HH:mm")
                .format(new java.util.Date((consumes.get(position).getTime()) * 1000));
        holder.time.setText(date);
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
        return consumes.size();
    }
    public static class Aview extends RecyclerView.ViewHolder{
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

