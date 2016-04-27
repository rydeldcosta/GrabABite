package com.example.rydeldcosta.findme;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by sudhanshu monga on 4/21/2016.
 */
public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.MyViewHolder> {
    private LayoutInflater inflater;
    List<ReviewItem> data = Collections.emptyList();
   // List<ReviewItem> data2 = Collections.emptyList();


    public ViewAdapter(Context context , List<ReviewItem> data){
        inflater = LayoutInflater.from(context);
        this.data = data ;


    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.custom_row , parent , false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ReviewItem current = data.get(position);
        holder.name.setText(current.name);
        holder.review.setText(current.review);
        //holder.icon.setImageResource();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name,review ;
        ImageView icon ;
        public MyViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.Rev_name);
            review = (TextView) itemView.findViewById(R.id.Rev_review);
            icon = (ImageView) itemView.findViewById(R.id.ListIcon);
        }
    }
}
