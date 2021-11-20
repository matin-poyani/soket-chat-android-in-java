package Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.MessageFormat;
import java.util.ArrayList;

import Models.ModelChannel;
import ir.ncis.chat.App;
import ir.ncis.chat.R;

public class AdapterRecyclerChannel extends RecyclerView.Adapter {
    public ArrayList<ModelChannel> channels;

    public AdapterRecyclerChannel(ArrayList<ModelChannel> channels) {
        this.channels = channels;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(App.INFLATER.inflate(R.layout.adapter_channel, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ModelChannel channel = channels.get(position);
        ViewHolder vh = (ViewHolder) holder;
        vh.txtName.setText(channel.name);
        App.loadImage(MessageFormat.format("{0}channels/{1}.jpg", App.URL_PHOTOS, channel.id), vh.imgLogo);
    }

    @Override
    public int getItemCount() {
        return channels.size();
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgLogo;
        TextView txtName;

        ViewHolder(View v) {
            super(v);
            imgLogo = v.findViewById(R.id.imgLogo);
            txtName = v.findViewById(R.id.txtName);
        }
    }
}
