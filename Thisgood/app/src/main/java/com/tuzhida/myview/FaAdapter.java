package com.tuzhida.myview;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tuzhida.bean.Family;
import com.tuzhida.thisgood.R;

import java.util.List;

/**
 * Created by Paul-Sartre on 2015/12/19.
 */
public class FaAdapter extends RecyclerView.Adapter<FaAdapter.Aview> {
    private List<Family> families;
    public FaAdapter(List<Family> list){families=list;}

    @Override
    public Aview onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.family_item,parent,false);
        return new Aview(view);
    }

    @Override
    public void onBindViewHolder(Aview holder, int position) {
        if (families.get(position).getType().equals("收入")){
            holder.type.setText("收入");
            holder.type.setTextColor(Color.rgb(0, 255, 0));
        }
        holder.name.setText(families.get(position).getName());
        holder.coin.setText(String.valueOf(families.get(position).getCoin()));
        holder.time.setText(families.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        if (families==null){
            return 0;
        }
        return families.size();
    }

    public static class Aview extends RecyclerView.ViewHolder{
        TextView type;
        TextView name;
        TextView coin;
        TextView time;
        public Aview(View itemView){
            super(itemView);
            type=(TextView)itemView.findViewById(R.id.family_type);
            name=(TextView)itemView.findViewById(R.id.family_user);
            coin=(TextView)itemView.findViewById(R.id.family_coin);
            time=(TextView)itemView.findViewById(R.id.family_time);
        }
    }
}
