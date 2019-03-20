package com.gst.myviewstudy.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gst.myviewstudy.R;
import com.gst.myviewstudy.ui.bean.ViewInfo;
import com.gst.myviewstudy.utils.ViewHelper;

import java.util.List;

/**
 * author: GuoSongtao on 2018/2/8 11:55
 * email: 157010607@qq.com
 */

public class MainGamesAdapter extends RecyclerView.Adapter<MainGamesAdapter.GamesItemViewHolder> {
    private Context mCtx;
    private List<ViewInfo> viewInfos;
    private OnRecyclerViewItemOnClickedListener listener = null;

    public MainGamesAdapter(Context mCtx, List<ViewInfo> viewInfos) {
        this.mCtx = mCtx;
        this.viewInfos = viewInfos;
        try {
            listener = (OnRecyclerViewItemOnClickedListener) mCtx;
        } catch (Exception e) {
        }
    }

    public void setViewInfos(List<ViewInfo> viewInfos) {
        this.viewInfos.clear();
        this.viewInfos = viewInfos;
        notifyDataSetChanged();
    }

    @Override
    public GamesItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler_view_view_info, null);
        return new GamesItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GamesItemViewHolder holder, int position) {
        holder.viewNameTv.setText(viewInfos.get(position).getName());
        holder.viewNameTv.setOnClickListener(v -> {
            if (listener != null) listener.onItemClicked(v, viewInfos.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return viewInfos != null ? viewInfos.size() : 0;
    }

    class GamesItemViewHolder extends RecyclerView.ViewHolder {
        TextView viewNameTv;

        public GamesItemViewHolder(View itemView) {
            super(itemView);
            viewNameTv = ViewHelper.bindView(itemView, R.id.tv_gameName);
        }
    }


}
