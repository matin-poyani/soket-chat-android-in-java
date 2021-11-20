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

import Adapters.AdapterRecyclerGroup;
import Models.ModelGroup;
import Socket.Listeners.ListenerSocketUsersGetGroups;
import Socket.SocketUsersGetGroups;
import Socket.Structures.StructUsersGroups;
import ir.ncis.chat.App;
import ir.ncis.chat.R;

public class FragmentGroups extends Fragment {
    private RecyclerView rvGroups;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_groups, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvGroups = view.findViewById(R.id.rvGroups);
        rvGroups.setAdapter(new AdapterRecyclerGroup(new ArrayList<ModelGroup>()));
        rvGroups.setLayoutManager(new LinearLayoutManager(App.ACTIVITY, LinearLayoutManager.VERTICAL, false));
        setUserVisibleHint(true);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser) {
            if (rvGroups != null) {
                loadGroups();
            }
        }
    }

    private void loadGroups() {
        new SocketUsersGetGroups()
                .setListener(new ListenerSocketUsersGetGroups() {
                    @Override
                    public void OnResult(StructUsersGroups result) {
                        if (result.code == 1) {
                            AdapterRecyclerGroup adapter = (AdapterRecyclerGroup) rvGroups.getAdapter();
                            adapter.groups.clear();
                            adapter.groups.addAll(result.data);
                            adapter.notifyDataSetChanged();
                        } else {
                            App.snack(
                                    App.ACTIVITY.findViewById(R.id.lytBackground),
                                    getString(R.string.error_no_internet),
                                    getString(R.string.label_try_again),
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            loadGroups();
                                        }
                                    }
                            );
                        }
                    }
                })
                .run(App.USER, App.PASS);
    }
}
