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

import Adapters.AdapterRecyclerDirect;
import Models.ModelDirect;
import Socket.Listeners.ListenerSocketUsersGetDirects;
import Socket.SocketUsersGetDirects;
import Socket.Structures.StructUsersDirects;
import ir.ncis.chat.App;
import ir.ncis.chat.R;

public class FragmentDirects extends Fragment {
    private RecyclerView rvDirects;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_directs, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvDirects = view.findViewById(R.id.rvDirects);
        rvDirects.setAdapter(new AdapterRecyclerDirect(new ArrayList<ModelDirect>()));
        rvDirects.setLayoutManager(new LinearLayoutManager(App.ACTIVITY, LinearLayoutManager.VERTICAL, false));
        setUserVisibleHint(true);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            if (rvDirects != null) {
                loadDirects();
            }
        }
    }

    private void loadDirects() {
        new SocketUsersGetDirects()
                .setListener(new ListenerSocketUsersGetDirects() {
                    @Override
                    public void OnResult(StructUsersDirects result) {
                        if (result.code == 1) {
                            AdapterRecyclerDirect adapter = (AdapterRecyclerDirect) rvDirects.getAdapter();
                            adapter.directs.clear();
                            adapter.directs.addAll(result.data);
                            adapter.notifyDataSetChanged();
                        } else {
                            App.snack(
                                    App.ACTIVITY.findViewById(R.id.lytBackground),
                                    getString(R.string.error_no_internet),
                                    getString(R.string.label_try_again),
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            loadDirects();
                                        }
                                    }
                            );
                        }
                    }
                })
                .run(App.USER, App.PASS);
    }
}
