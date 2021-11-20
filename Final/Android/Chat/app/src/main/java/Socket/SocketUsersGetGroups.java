package Socket;

import Socket.Listeners.ListenerSocketUsersGetGroups;
import ir.ncis.chat.App;

public class SocketUsersGetGroups {

    public void run(String username, String password) {
        SocketClient.send("users", "getgroups", username, password);
    }

    public SocketUsersGetGroups setListener(ListenerSocketUsersGetGroups listener) {
        App.LISTENERS.put("users:getgroups", listener);
        return this;
    }
}
