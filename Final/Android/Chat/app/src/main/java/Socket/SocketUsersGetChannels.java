package Socket;

import Socket.Listeners.ListenerSocketUsersGetChannels;
import ir.ncis.chat.App;

public class SocketUsersGetChannels {

    public void run(String username, String password) {
        SocketClient.send("users", "getchannels", username, password);
    }

    public SocketUsersGetChannels setListener(ListenerSocketUsersGetChannels listener) {
        App.LISTENERS.put("users:getchannels", listener);
        return this;
    }
}
