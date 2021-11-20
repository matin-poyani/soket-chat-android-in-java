package activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.MessageFormat;
import java.util.ArrayList;

import Adapters.AdapterRecyclerMessage;
import Models.ModelMessage;
import Socket.Listeners.ListenerSocketDirectsMessages;
import Socket.Listeners.ListenerSocketDirectsSend;
import Socket.Listeners.ListenerSocketGlobalNewDirect;
import Socket.Listeners.ListenerSocketUsersGetName;
import Socket.SocketDirectsMessages;
import Socket.SocketDirectsSend;
import Socket.SocketGlobalNewDirect;
import Socket.SocketUsersGetName;
import Socket.Structures.StructDirectsMessages;
import Socket.Structures.StructDirectsSend;
import Socket.Structures.StructGlobalMessage;
import Socket.Structures.StructUsersName;
import ir.ncis.chat.ActivityEnhanced;
import ir.ncis.chat.App;
import ir.ncis.chat.R;

public class DirectActivity extends ActivityEnhanced {
    private RecyclerView rvMessages;
    private int directId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direct);

        directId = getIntent().getIntExtra(getString(R.string.key_bundle_direct_id), 0);
        int partnerId = getIntent().getIntExtra(getString(R.string.key_bundle_partner_id), 0);
        if (directId == 0 || partnerId == 0) {
            finish();
        } else {
            App.loadImage(MessageFormat.format("{0}users/{1}.jpg", App.URL_PHOTOS, partnerId), ((ImageView) findViewById(R.id.imgLogo)));
            new SocketUsersGetName()
                    .setListener(new ListenerSocketUsersGetName() {
                        @Override
                        public void OnResult(StructUsersName result) {
                            if (result != null && result.data != null) {
                                ((TextView) findViewById(R.id.txtName)).setText(result.data.name);
                            }
                        }
                    })
                    .run(partnerId);
            rvMessages = findViewById(R.id.rvMessages);
            rvMessages.setAdapter(new AdapterRecyclerMessage(new ArrayList<ModelMessage>()));
            rvMessages.setLayoutManager(new LinearLayoutManager(App.ACTIVITY, LinearLayoutManager.VERTICAL, false));
            loadMessages();
        }

        initiateUpdater();

        findViewById(R.id.imgSend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText edtMessage = findViewById(R.id.edtMessage);
                String message = edtMessage.getText().toString().trim();
                if (!message.isEmpty()) {
                    new SocketDirectsSend()
                            .setListener(new ListenerSocketDirectsSend() {
                                @Override
                                public void OnResult(StructDirectsSend result) {
                                    if (result != null && result.code == 1) {
                                        AdapterRecyclerMessage adapter = (AdapterRecyclerMessage) rvMessages.getAdapter();
                                        adapter.messages.add(result.data);
                                        adapter.notifyDataSetChanged();
                                        rvMessages.scrollToPosition(adapter.getItemCount() - 1);
                                        edtMessage.setText(null);
                                        closeKeyboard(edtMessage);
                                    }
                                }
                            })
                            .run(directId, App.USER, App.PASS, message, null);
                }
            }
        });
    }

    private void initiateUpdater() {
        App.NEW_DIRECT_LISTENER = new ListenerSocketGlobalNewDirect() {
            @Override
            public void OnResult(StructGlobalMessage result) {
                if (result != null && result.data != null && result.data.direct_id == directId) {
                    AdapterRecyclerMessage adapter = (AdapterRecyclerMessage) rvMessages.getAdapter();
                    adapter.messages.add(result.data);
                    adapter.notifyDataSetChanged();
                    rvMessages.scrollToPosition(adapter.getItemCount() - 1);
                }
            }
        };
        new SocketGlobalNewDirect().setListener(App.NEW_DIRECT_LISTENER).run();
    }

    private void loadMessages() {
        new SocketDirectsMessages()
                .setListener(new ListenerSocketDirectsMessages() {
                    @Override
                    public void OnResult(StructDirectsMessages result) {
                        if (result != null && result.data != null) {
                            AdapterRecyclerMessage adapter = (AdapterRecyclerMessage) rvMessages.getAdapter();
                            adapter.messages.clear();
                            adapter.messages.addAll(result.data);
                            adapter.notifyDataSetChanged();
                            rvMessages.scrollToPosition(adapter.getItemCount() - 1);
                        }
                    }
                })
                .run(App.USER, App.PASS, directId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.NEW_DIRECT_LISTENER = null;
    }
}
