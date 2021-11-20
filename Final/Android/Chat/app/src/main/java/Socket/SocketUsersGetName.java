package Socket;

import Socket.Listeners.ListenerSocketUsersGetName;
import ir.ncis.chat.App;

public class SocketUsersGetName {

    public void run(int id) {
        SocketClient.send("users", "getname", String.valueOf(id));
    }

    public SocketUsersGetName setListener(ListenerSocketUsersGetName listener) {
        App.LISTENERS.put("users:getname", listener);
        return this;
    }
}
