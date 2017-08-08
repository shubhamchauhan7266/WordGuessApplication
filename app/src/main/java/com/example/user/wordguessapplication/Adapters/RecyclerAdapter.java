package com.example.user.wordguessapplication.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.wordguessapplication.R;
import com.example.user.wordguessapplication.Models.WordGuessModel;

import java.util.ArrayList;

/**
 * <h1><font color="orange">RecyclerAdapter</font></h1>
 * Adapter class to store the data.
 * Created by Shubham Chauhan on 8/8/17.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private ArrayList<WordGuessModel> mFeedItemList;

    public RecyclerAdapter(ArrayList<WordGuessModel> feedItemList) {
        mFeedItemList = feedItemList;
    }

    public void setmFeedItemList(ArrayList<WordGuessModel> feedItemList) {
        mFeedItemList = feedItemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        return new RecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        WordGuessModel wordGuessModel = mFeedItemList.get(position);
        holder.tvSno.setText(String.valueOf(position));
        holder.tvWord.setText(wordGuessModel.getWord());
        holder.tvBulls.setText(wordGuessModel.getBull());
        holder.tvCows.setText(wordGuessModel.getCow());
    }

    @Override
    public int getItemCount() {
        return (null != mFeedItemList ? mFeedItemList.size() : 0);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvSno;
        private TextView tvWord;
        private TextView tvBulls;
        private TextView tvCows;

        ViewHolder(View itemView) {
            super(itemView);
            this.tvSno = itemView.findViewById(R.id.tv_sno);
            this.tvWord = itemView.findViewById(R.id.tv_word);
            this.tvBulls = itemView.findViewById(R.id.tv_bulls);
            this.tvCows = itemView.findViewById(R.id.tv_cows);}
    }
}
