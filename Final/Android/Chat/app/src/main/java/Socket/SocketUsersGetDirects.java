package Socket;

import Socket.Listeners.ListenerSocketUsersGetDirects;
import ir.ncis.chat.App;

public class SocketUsersGetDirects {

    public void run(String username, String password) {
        SocketClient.send("users", "getdirects", username, password);
    }

    public SocketUsersGetDirects setListener(ListenerSocketUsersGetDirects listener) {
        App.LISTENERS.put("users:getdirects", listener);
        return this;
    }
}
