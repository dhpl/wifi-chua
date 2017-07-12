package com.philong.wifichua.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philong.wifichua.R;
import com.philong.wifichua.model.Wifi;

import java.util.List;

/**
 * Created by Long on 7/6/2017.
 */

public class WifiAdapter extends RecyclerView.Adapter<WifiAdapter.WifiViewHolder>{

    private Context mContext;
    private List<Wifi> mWifiList;

    public WifiAdapter(Context context, List<Wifi> wifiList) {
        mContext = context;
        mWifiList = wifiList;
    }

    @Override
    public WifiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_wifi, parent, false);
        return new WifiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WifiViewHolder holder, int position) {
        Wifi wifi = mWifiList.get(position);
        holder.txtTen.setText(wifi.getName());
        holder.txtMatKhau.setText(wifi.getMatkhau());
        holder.txtDiaChi.setText(wifi.getDiachi());
    }

    @Override
    public int getItemCount() {
        return mWifiList.size();
    }

    public static class WifiViewHolder extends RecyclerView.ViewHolder{

        private TextView txtTen;
        private TextView txtDiaChi;
        private TextView txtMatKhau;

        public WifiViewHolder(View itemView) {
            super(itemView);
            txtTen = (TextView) itemView.findViewById(R.id.txtTenWifi);
            txtDiaChi = (TextView) itemView.findViewById(R.id.txtDiaChi);
            txtMatKhau = (TextView) itemView.findViewById(R.id.txtMatKhau);
        }
    }

}
