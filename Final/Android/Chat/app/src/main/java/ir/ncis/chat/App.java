package ir.ncis.chat;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Process;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import Models.ModelUser;
import Socket.Listeners.ListenerBase;
import Socket.Listeners.ListenerSocketDirectsMessages;
import Socket.Listeners.ListenerSocketDirectsSend;
import Socket.Listeners.ListenerSocketGlobalNewDirect;
import Socket.Listeners.ListenerSocketUsersGetChannels;
import Socket.Listeners.ListenerSocketUsersGetDirects;
import Socket.Listeners.ListenerSocketUsersGetGroups;
import Socket.Listeners.ListenerSocketUsersGetName;
import Socket.Listeners.ListenerSocketUsersLogin;
import Socket.Listeners.ListenerSocketUsersRegister;
import Socket.SocketClient;
import Socket.Structures.StructDirectsMessages;
import Socket.Structures.StructDirectsSend;
import Socket.Structures.StructGlobalMessage;
import Socket.Structures.StructUsersChannels;
import Socket.Structures.StructUsersDirects;
import Socket.Structures.StructUsersGroups;
import Socket.Structures.StructUsersLogin;
import Socket.Structures.StructUsersName;
import Socket.Structures.StructUsersRegister;

@SuppressLint("StaticFieldLeak")
public class App extends Application {
    public static final ArrayList<Toast> TOASTS = new ArrayList<>();
    public static final Gson GSON = new GsonBuilder().setLenient().create();
    public static final Handler HANDLER = new Handler();
    public static final HashMap<String, ListenerBase> LISTENERS = new HashMap<>();
    public static final String SOCKET_ADDRESS = "192.168.1.100";
    public static final String URL_SERVER = "http://" + SOCKET_ADDRESS + "/chat/";
    public static final String URL_PHOTOS = URL_SERVER + "pic/";
    public static final int SOCKET_PORT = 5050;
    public static final int SOCKET_TIMEOUT = 2000;
    public static ActivityEnhanced ACTIVITY;
    public static Context CONTEXT;
    public static LayoutInflater INFLATER;
    public static ListenerSocketGlobalNewDirect NEW_DIRECT_LISTENER;
    public static ModelUser CURRENT_USER;
    public static SharedPreferences PREFERENCES;
    public static SharedPreferences.Editor EDITOR;
    public static String NAME, PASS, USER;

    public static boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) CONTEXT.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }
        if (networkInfo != null && networkInfo.isConnected()) {
            try {
                Runtime runtime = Runtime.getRuntime();
                java.lang.Process ipProcess = runtime.exec("/system/bin/ping -c 1 " + SOCKET_ADDRESS);
                int exitValue = ipProcess.waitFor();
                ipProcess.destroy();
                runtime.freeMemory();
                return (exitValue == 0);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static void exit() {
        for (Map.Entry<Thread, StackTraceElement[]> threadEntry : Thread.getAllStackTraces().entrySet()) {
            threadEntry.getKey().interrupt();
        }
        Process.killProcess(Process.myPid());
        HANDLER.removeCallbacksAndMessages(null);
        ACTIVITY.finish();
        System.exit(0);
    }

    public static String sha1(String value) {
        try {
            return String.format("%040x", new BigInteger(1, MessageDigest.getInstance("SHA-1").digest(value.getBytes())));
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static void snack(View parent, String message, String label, final Runnable action) {
        Snackbar
                .make(parent, message, Snackbar.LENGTH_INDEFINITE)
                .setAction(label, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        action.run();
                    }
                })
                .show();
    }

    public static void toast(String message) {
        toast(message, false);
    }

    @SuppressLint("InflateParams")
    public static void toast(final String message, final boolean longDuration) {
        HANDLER.post(new Runnable() {
            @Override
            public void run() {
                for (Toast toast : TOASTS) {
                    toast.cancel();
                }
                TOASTS.clear();
                View view = INFLATER.inflate(R.layout.toast, null);
                TextView txtMessage = view.findViewById(R.id.txtMessage);
                txtMessage.setText(message);
                Toast toast = new Toast(CONTEXT);
                toast.setDuration(longDuration ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
                toast.setView(view);
                toast.show();
                TOASTS.add(toast);
            }
        });
    }

    public static void loadImage(String imageUrl, ImageView target) {
        loadImage(imageUrl, target, true);
    }

    public static void loadImage(String imageUrl, ImageView target, boolean fromCache) {
        loadImageWithPicasso(imageUrl, target, fromCache);
    }

    private static void loadImageWithPicasso(final String imageUrl, final ImageView target, boolean fromCache) {
        if (fromCache) {
            Picasso.get()
                    .load(imageUrl)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder(R.mipmap.logo)
                    .error(R.mipmap.logo)
                    .into(target, new Callback() {
                        @Override
                        public void onSuccess() {
                            // Offline Cache hit
                        }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get()
                                    .load(imageUrl)
                                    .placeholder(R.mipmap.logo)
                                    .error(R.mipmap.logo)
                                    .into(target, new Callback() {
                                        @Override
                                        public void onSuccess() {
                                            // Online download
                                        }

                                        @Override
                                        public void onError(Exception e) {
                                            toast(CONTEXT.getString(R.string.error_loading_image));
                                        }
                                    });
                        }
                    });
        } else {
            Picasso.get()
                    .load(imageUrl)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .placeholder(R.mipmap.logo)
                    .error(R.mipmap.logo)
                    .into(target);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        CONTEXT = getApplicationContext();
        PREFERENCES = PreferenceManager.getDefaultSharedPreferences(CONTEXT);
        EDITOR = PREFERENCES.edit();
        EDITOR.apply();
        NAME = PREFERENCES.getString(getString(R.string.key_sp_name), null);
        PASS = PREFERENCES.getString(getString(R.string.key_sp_pass), null);
        USER = PREFERENCES.getString(getString(R.string.key_sp_user), null);
        Picasso.setSingletonInstance(new Picasso.Builder(this).downloader(new OkHttp3Downloader(this, Integer.MAX_VALUE)).build());

        SocketClient.init(new SocketClient.SocketListener() {
            @Override
            public void reaction(String response) {
                String action = "";
                try {
                    action = new JSONObject(response).getString("action");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ListenerBase listener = null;
                for (Map.Entry<String, ListenerBase> entry : LISTENERS.entrySet()) {
                    if (entry.getKey().equals(action)) {
                        listener = entry.getValue();
                        break;
                    }
                }
                switch (action) {
                    //region DIRECTS:MESSAGES
                    case "directs:messages": {
                        if (listener instanceof ListenerSocketDirectsMessages) {
                            final ListenerSocketDirectsMessages caseListener = (ListenerSocketDirectsMessages) listener;
                            final StructDirectsMessages data = App.GSON.fromJson(response, StructDirectsMessages.class);
                            App.HANDLER.post(new Runnable() {
                                @Override
                                public void run() {
                                    caseListener.OnResult(data);
                                }
                            });
                            LISTENERS.remove(action);
                        }
                    }
                    break;
                    //endregion
                    //region DIRECTS:SEND
                    case "directs:send": {
                        if (listener instanceof ListenerSocketDirectsSend) {
                            final ListenerSocketDirectsSend caseListener = (ListenerSocketDirectsSend) listener;
                            final StructDirectsSend data = App.GSON.fromJson(response, StructDirectsSend.class);
                            App.HANDLER.post(new Runnable() {
                                @Override
                                public void run() {
                                    caseListener.OnResult(data);
                                }
                            });
                            LISTENERS.remove(action);
                        }
                    }
                    break;
                    //endregion
                    //region GLOBAL:NEWDIRECT
                    case "global:newdirect": {
                        if (listener instanceof ListenerSocketGlobalNewDirect) {
                            final ListenerSocketGlobalNewDirect caseListener = (ListenerSocketGlobalNewDirect) listener;
                            final StructGlobalMessage data = App.GSON.fromJson(response, StructGlobalMessage.class);
                            final String finalAction = action;
                            HANDLER.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (NEW_DIRECT_LISTENER != null) {
                                        caseListener.OnResult(data);
                                    } else {
                                        LISTENERS.remove(finalAction);
                                    }
                                }
                            });
                        }
                    }
                    break;
                    //endregion
                    //region USERS:GETCHANNELS
                    case "users:getchannels": {
                        if (listener instanceof ListenerSocketUsersGetChannels) {
                            final ListenerSocketUsersGetChannels caseListener = (ListenerSocketUsersGetChannels) listener;
                            final StructUsersChannels data = App.GSON.fromJson(response, StructUsersChannels.class);
                            App.HANDLER.post(new Runnable() {
                                @Override
                                public void run() {
                                    caseListener.OnResult(data);
                                }
                            });
                            LISTENERS.remove(action);
                        }
                    }
                    break;
                    //endregion
                    //region USERS:GETDIRECTS
                    case "users:getdirects": {
                        if (listener instanceof ListenerSocketUsersGetDirects) {
                            final ListenerSocketUsersGetDirects caseListener = (ListenerSocketUsersGetDirects) listener;
                            final StructUsersDirects data = App.GSON.fromJson(response, StructUsersDirects.class);
                            App.HANDLER.post(new Runnable() {
                                @Override
                                public void run() {
                                    caseListener.OnResult(data);
                                }
                            });
                            LISTENERS.remove(action);
                        }
                    }
                    break;
                    //endregion
                    //region USERS:GETGROUPS
                    case "users:getgroups": {
                        if (listener instanceof ListenerSocketUsersGetGroups) {
                            final ListenerSocketUsersGetGroups caseListener = (ListenerSocketUsersGetGroups) listener;
                            final StructUsersGroups data = App.GSON.fromJson(response, StructUsersGroups.class);
                            App.HANDLER.post(new Runnable() {
                                @Override
                                public void run() {
                                    caseListener.OnResult(data);
                                }
                            });
                            LISTENERS.remove(action);
                        }
                    }
                    break;
                    //endregion
                    //region USERS:GETNAME
                    case "users:getname": {
                        if (listener instanceof ListenerSocketUsersGetName) {
                            final ListenerSocketUsersGetName caseListener = (ListenerSocketUsersGetName) listener;
                            final StructUsersName data = App.GSON.fromJson(response, StructUsersName.class);
                            App.HANDLER.post(new Runnable() {
                                @Override
                                public void run() {
                                    caseListener.OnResult(data);
                                }
                            });
                            LISTENERS.remove(action);
                        }
                    }
                    break;
                    //endregion
                    //region USERS:LOGIN
                    case "users:login": {
                        if (listener instanceof ListenerSocketUsersLogin) {
                            final ListenerSocketUsersLogin caseListener = (ListenerSocketUsersLogin) listener;
                            final StructUsersLogin data = App.GSON.fromJson(response, StructUsersLogin.class);
                            App.HANDLER.post(new Runnable() {
                                @Override
                                public void run() {
                                    caseListener.OnResult(data);
                                }
                            });
                            LISTENERS.remove(action);
                        }
                    }
                    break;
                    //endregion
                    //region USERS:REGISTER
                    case "users:register": {
                        if (listener instanceof ListenerSocketUsersRegister) {
                            final ListenerSocketUsersRegister caseListener = (ListenerSocketUsersRegister) listener;
                            final StructUsersRegister data = App.GSON.fromJson(response, StructUsersRegister.class);
                            App.HANDLER.post(new Runnable() {
                                @Override
                                public void run() {
                                    caseListener.OnResult(data);
                                }
                            });
                            LISTENERS.remove(action);
                        }
                    }
                    break;
                    //endregion
                }
            }
        });
    }
}
