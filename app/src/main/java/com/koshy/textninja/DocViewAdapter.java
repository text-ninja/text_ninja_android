package com.koshy.textninja;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class DocViewAdapter extends RecyclerView.Adapter<DocViewAdapter.ParagraphViewHolder> {
    ArrayList<String> mParas;
    Context mContext;
    public boolean[] mSelected;

    public DocViewAdapter(ArrayList<String> paras, Context context) {
        this.mParas = paras;
        this.mContext = context;
        mSelected = new boolean[paras.size()];
        Arrays.fill(mSelected, false);
    }

    @NonNull
    @Override
    public ParagraphViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.para_view, viewGroup, false);
//        Log.d(TAG, "onCreateViewHolder: Size : " + mRecipeJsonArray.length());
        return new ParagraphViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ParagraphViewHolder paragraphViewHolder, final int i) {
        paragraphViewHolder.paraTextView.setText(mParas.get(i));
        if(mSelected[i]) {
            paragraphViewHolder.paraTextView.setTextColor( mContext.getResources().getColor(R.color.colorPrimary));
        } else {
            paragraphViewHolder.paraTextView.setTextColor( mContext.getResources().getColor(android.R.color.tab_indicator_text));
        }
        paragraphViewHolder.paraTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (paragraphViewHolder.paraTextView.getTextColors().)
                if(!mSelected[i]) {
                    paragraphViewHolder.paraTextView.setTextColor( mContext.getResources().getColor(R.color.colorPrimary));
                } else {
                    paragraphViewHolder.paraTextView.setTextColor( mContext.getResources().getColor(android.R.color.tab_indicator_text));
                }
                mSelected[i] = !mSelected[i];
            }
        });
    }

    @Override
    public int getItemCount() {
        return mParas.size();
    }

    public void setNewVersion(ArrayList<String> strings) {
        mParas = strings;
        mSelected = new boolean[mParas.size()];
        Arrays.fill(mSelected, false);
        notifyDataSetChanged();
    }

    public class ParagraphViewHolder extends RecyclerView.ViewHolder {
        TextView paraTextView;

        public ParagraphViewHolder(@NonNull View itemView) {
            super(itemView);
            paraTextView = itemView.findViewById(R.id.paraTextView);
        }
    }
}
