package Adapters;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.MessageFormat;
import java.util.ArrayList;

import Models.ModelDirect;
import Socket.Listeners.ListenerSocketUsersGetName;
import Socket.SocketUsersGetName;
import Socket.Structures.StructUsersName;
import activities.DirectActivity;
import ir.ncis.chat.App;
import ir.ncis.chat.R;

public class AdapterRecyclerDirect extends RecyclerView.Adapter {
    public ArrayList<ModelDirect> directs;

    public AdapterRecyclerDirect(ArrayList<ModelDirect> directs) {
        this.directs = directs;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(App.INFLATER.inflate(R.layout.adapter_direct, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ModelDirect direct = directs.get(position);
        final ViewHolder vh = (ViewHolder) holder;
        int userId = App.PREFERENCES.getInt(App.CONTEXT.getString(R.string.key_sp_id), 0);
        final int partnerId = direct.user1_id != userId ? direct.user1_id : direct.user2_id;
        new SocketUsersGetName()
                .setListener(new ListenerSocketUsersGetName() {
                    @Override
                    public void OnResult(StructUsersName result) {
                        if (result.code == 1) {
                            vh.txtName.setText(result.data.name);
                        }
                    }
                })
                .run(partnerId);
        App.loadImage(MessageFormat.format("{0}users/{1}.jpg", App.URL_PHOTOS, partnerId), vh.imgLogo);
        vh.lytRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt(App.CONTEXT.getString(R.string.key_bundle_direct_id), direct.id);
                bundle.putInt(App.CONTEXT.getString(R.string.key_bundle_partner_id), partnerId);
                App.ACTIVITY.runActivity(DirectActivity.class, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return directs.size();
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgLogo;
        TextView txtName;
        ViewGroup lytRoot;

        ViewHolder(View v) {
            super(v);
            imgLogo = v.findViewById(R.id.imgLogo);
            lytRoot = v.findViewById(R.id.lytRoot);
            txtName = v.findViewById(R.id.txtName);
        }
    }
}
