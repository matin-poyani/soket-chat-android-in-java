package Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.MessageFormat;
import java.util.ArrayList;

import Models.ModelGroup;
import ir.ncis.chat.App;
import ir.ncis.chat.R;

public class AdapterRecyclerGroup extends RecyclerView.Adapter {
    public ArrayList<ModelGroup> groups;

    public AdapterRecyclerGroup(ArrayList<ModelGroup> groups) {
        this.groups = groups;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(App.INFLATER.inflate(R.layout.adapter_group, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ModelGroup group = groups.get(position);
        ViewHolder vh = (ViewHolder) holder;
        vh.txtName.setText(group.name);
        App.loadImage(MessageFormat.format("{0}groups/{1}.jpg", App.URL_PHOTOS, group.id), vh.imgLogo);
    }

    @Override
    public int getItemCount() {
        return groups.size();
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
