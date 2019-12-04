package com.easymi.component.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.services.core.PoiItem;
import com.easymi.component.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C), 2012-2018, Sichuan Xiaoka Technology Co., Ltd.
 * FileName:
 * @Author: hufeng
 * Date: 2018/12/24 下午1:10
 * Description:
 * History:
 */

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.Holder> {

    private List<PoiItem> poiList;
    private Context context;

    public interface OnItemClickListener {
        /**
         * 列表点击监听
         * @param item
         */
        void onItemClick(PoiItem item);
    }

    private OnItemClickListener itemClickListener;

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public PlaceAdapter(Context context) {
        this.context = context;
        poiList = new ArrayList<>();
    }

    /**
     * 设置数据
     * @param poiList
     */
    public void setPoiList(List<PoiItem> poiList) {
        this.poiList = poiList;
        notifyDataSetChanged();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        PoiItem poi = poiList.get(position);
        //商圈
        holder.poiText.setText(poi.getTitle());
        //详细地址
        holder.addressText.setText(poi.getSnippet());
        if(null != itemClickListener){
            holder.root.setOnClickListener(view -> itemClickListener.onItemClick(poi));
        }
    }

    @Override
    public int getItemCount() {
        return poiList.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView poiText;
        TextView addressText;
        View root;

        Holder(View itemView) {
            super(itemView);
            root = itemView;
            poiText = itemView.findViewById(R.id.poi);
            addressText = itemView.findViewById(R.id.address);
        }
    }
}
