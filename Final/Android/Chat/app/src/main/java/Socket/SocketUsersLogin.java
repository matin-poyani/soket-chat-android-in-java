package Socket;

import Socket.Listeners.ListenerSocketUsersLogin;
import ir.ncis.chat.App;

public class SocketUsersLogin {

    public void run(String username, String password) {
        SocketClient.send("users", "login", username, password);
    }

    public SocketUsersLogin setListener(ListenerSocketUsersLogin listener) {
        App.LISTENERS.put("users:login", listener);
        return this;
    }
}
