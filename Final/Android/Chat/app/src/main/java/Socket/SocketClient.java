package Socket;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;

import ir.ncis.chat.App;
import ir.ncis.chat.R;

public class SocketClient {
    private static Socket socket;
    private static DataOutputStream out;
    private static BufferedReader in;
    private static Thread readerThread;
    private static SocketListener listener;

    public static void init(SocketListener socketListener) {
        listener = socketListener;
        createNewSocket();
    }

    private static void createNewSocket() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket();
                    socket.connect(new InetSocketAddress(App.SOCKET_ADDRESS, App.SOCKET_PORT), App.SOCKET_TIMEOUT);
                    if (socket != null) {
                        socket.setKeepAlive(true);
                        if (socket.isConnected()) {
                            out = new DataOutputStream(socket.getOutputStream());
                            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                            readerThread = new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    while (true) {
                                        if (readerThread.isInterrupted()) {
                                            Log.i("SOCKET", "ERROR: Interrupted.");
                                            App.toast(App.CONTEXT.getString(R.string.error_no_internet));
                                            try {
                                                socket.close();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            socket = null;
                                            break;
                                        }
                                        String response = read();
                                        if (response != null) {
                                            Log.i("SOCKET", "RESPONSE: " + response);
                                            if (listener != null) {
                                                listener.reaction(response);
                                            }
                                        }
                                        try {
                                            Thread.sleep(100);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });
                            readerThread.start();
                        }
                    }
                } catch (IOException e) {
                    Log.i("SOCKET", e.getMessage());
                    try {
                        if (socket != null) {
                            socket.close();
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private static String read() {
        if (socket != null && socket.isConnected() && !socket.isClosed() && in != null) {
            try {
                return in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void send(String... data) {
        final StringBuilder stringBuilder = new StringBuilder();
        int length = data.length;
        for (int i = 0; i < length; i++) {
            stringBuilder.append(data[i]);
            if (i < length - 1) {
                stringBuilder.append(":");
            }
        }
        App.HANDLER.post(new Runnable() {
            @Override
            public void run() {
                new AsyncSocket().execute(stringBuilder.toString());
            }
        });
    }

    private static void reconnect() {
        try {
            if (socket != null) {
                socket.close();
                socket = null;
            }
            createNewSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public interface SocketListener {
        void reaction(String response);
    }

    private static class AsyncSocket extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            if (!App.isOnline()) {
                Log.i("SOCKET", "ERROR: " + App.CONTEXT.getString(R.string.error_no_internet));
                App.toast(App.CONTEXT.getString(R.string.error_no_internet));
                return null;
            }
            while (socket == null || !socket.isConnected() || socket.isClosed() || out == null) {
                reconnect();
            }
            Log.i("SOCKET", "REQUEST: " + strings[0]);
            try {
                out.write(strings[0].getBytes());
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
                socket = null;
            }
            return null;
        }
    }
}
