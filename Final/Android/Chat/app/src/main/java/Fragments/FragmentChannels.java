package Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import Adapters.AdapterRecyclerChannel;
import Models.ModelChannel;
import Socket.Listeners.ListenerSocketUsersGetChannels;
import Socket.SocketUsersGetChannels;
import Socket.Structures.StructUsersChannels;
import ir.ncis.chat.App;
import ir.ncis.chat.R;

public class FragmentChannels extends Fragment {
    private RecyclerView rvChannels;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_channels, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvChannels = view.findViewById(R.id.rvChannels);
        rvChannels.setAdapter(new AdapterRecyclerChannel(new ArrayList<ModelChannel>()));
        rvChannels.setLayoutManager(new LinearLayoutManager(App.ACTIVITY, LinearLayoutManager.VERTICAL, false));
        setUserVisibleHint(true);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            if (rvChannels != null) {
                loadChannels();
            }
        }
    }

    private void loadChannels() {
        new SocketUsersGetChannels()
                .setListener(new ListenerSocketUsersGetChannels() {
                    @Override
                    public void OnResult(StructUsersChannels result) {
                        if (result.code == 1) {
                            AdapterRecyclerChannel adapter = (AdapterRecyclerChannel) rvChannels.getAdapter();
                            adapter.channels.clear();
                            adapter.channels.addAll(result.data);
                            adapter.notifyDataSetChanged();
                        } else {
                            App.snack(
                                    App.ACTIVITY.findViewById(R.id.lytBackground),
                                    getString(R.string.error_no_internet),
                                    getString(R.string.label_try_again),
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            loadChannels();
                                        }
                                    }
                            );
                        }
                    }
                })
                .run(App.USER, App.PASS);
    }
}
