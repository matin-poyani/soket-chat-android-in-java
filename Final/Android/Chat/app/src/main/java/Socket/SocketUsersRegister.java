package Socket;

import Socket.Listeners.ListenerSocketUsersRegister;
import ir.ncis.chat.App;

public class SocketUsersRegister {

    public void run(String name, String username, String password) {
        SocketClient.send("users", "register", name, username, password);
    }

    public SocketUsersRegister setListener(ListenerSocketUsersRegister listener) {
        App.LISTENERS.put("users:register", listener);
        return this;
    }
}
