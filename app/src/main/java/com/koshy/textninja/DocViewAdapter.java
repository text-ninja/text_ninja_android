package com.koshy.textninja;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DocViewAdapter extends RecyclerView.Adapter<DocViewAdapter.ParagraphViewHolder> {
    String[] mParas;
    Context mContext;

    public DocViewAdapter(String[] paras, Context context) {
        this.mParas = paras;
        this.mContext = context;
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
    public void onBindViewHolder(@NonNull final ParagraphViewHolder paragraphViewHolder, int i) {
        paragraphViewHolder.paraTextView.setText(mParas[i]);
        paragraphViewHolder.paraTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (paragraphViewHolder.paraTextView.getTextColors().)
                paragraphViewHolder.paraTextView.setTextColor( mContext.getResources().getColor(R.color.colorPrimary));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mParas.length;
    }

    public class ParagraphViewHolder extends RecyclerView.ViewHolder {
        TextView paraTextView;

        public ParagraphViewHolder(@NonNull View itemView) {
            super(itemView);
            paraTextView = itemView.findViewById(R.id.paraTextView);
        }
    }
}
