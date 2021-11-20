package Adapters;

import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.MessageFormat;
import java.util.ArrayList;

import Models.ModelMessage;
import ir.ncis.chat.App;
import ir.ncis.chat.R;

public class AdapterRecyclerMessage extends RecyclerView.Adapter {
    public ArrayList<ModelMessage> messages;

    public AdapterRecyclerMessage(ArrayList<ModelMessage> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(App.INFLATER.inflate(R.layout.adapter_message, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final ModelMessage message = messages.get(position);
        final ViewHolder vh = (ViewHolder) holder;
        if (App.PREFERENCES.getInt(App.CONTEXT.getString(R.string.key_sp_id), 0) == message.user_id) {
            vh.lytMessage.setBackgroundResource(R.drawable.background_chat_user);
            vh.lytLogo1.setVisibility(View.VISIBLE);
            vh.lytLogo2.setVisibility(View.GONE);
            vh.txtName.setTextColor(ContextCompat.getColor(App.CONTEXT, R.color.white));
            App.loadImage(MessageFormat.format("{0}users/{1}.jpg", App.URL_PHOTOS, message.user_id), vh.imgLogo1);
        } else {
            vh.lytMessage.setBackgroundResource(R.drawable.background_chat_partner);
            vh.lytLogo1.setVisibility(View.GONE);
            vh.lytLogo2.setVisibility(View.VISIBLE);
            vh.txtName.setTextColor(ContextCompat.getColor(App.CONTEXT, R.color.primaryLight));
            App.loadImage(MessageFormat.format("{0}users/{1}.jpg", App.URL_PHOTOS, message.user_id), vh.imgLogo2);
        }
        vh.txtMessage.setText(message.body);
        vh.txtName.setText(message.user);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgLogo1, imgLogo2;
        TextView txtMessage;
        TextView txtName;
        ViewGroup lytLogo1, lytLogo2, lytMessage;

        ViewHolder(View v) {
            super(v);
            imgLogo1 = v.findViewById(R.id.imgLogo1);
            imgLogo2 = v.findViewById(R.id.imgLogo2);
            lytLogo1 = v.findViewById(R.id.lytLogo1);
            lytLogo2 = v.findViewById(R.id.lytLogo2);
            lytMessage = v.findViewById(R.id.lytMessage);
            txtMessage = v.findViewById(R.id.txtMessage);
            txtName = v.findViewById(R.id.txtName);
        }
    }
}
