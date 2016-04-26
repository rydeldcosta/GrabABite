package com.example.rydeldcosta.findme;

/**
 * Created by Rydel Dcosta on 4/26/2016.
 */
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

public class vivzAdapter extends RecyclerView.Adapter<vivzAdapter.MyViewHolder> {
    private LayoutInflater inflater;
    List<menu_Information> dataa = Collections.emptyList();


    public vivzAdapter(Context context , List<menu_Information> dataa){
        inflater = LayoutInflater.from(context);
        this.dataa = dataa ;


    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.custom_menu , parent , false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        menu_Information current = dataa.get(position);
        holder.itemname.setText(current.ItemName);
        holder.price.setText("Rs. "+String.valueOf(current.Price));
    }

    @Override
    public int getItemCount() {
        return dataa.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView itemname ;
        TextView price ;
        public MyViewHolder(View itemView) {
            super(itemView);
            itemname = (TextView) itemView.findViewById(R.id.menuitem);
            price = (TextView) itemView.findViewById(R.id.menuprice);
        }
    }
}
