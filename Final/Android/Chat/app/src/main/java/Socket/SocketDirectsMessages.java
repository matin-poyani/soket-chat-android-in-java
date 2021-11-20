package Socket;

import Socket.Listeners.ListenerSocketDirectsMessages;
import ir.ncis.chat.App;

public class SocketDirectsMessages {

    public void run(String username, String password, int directId) {
        SocketClient.send("directs", "messages", username, password, String.valueOf(directId));
    }

    public SocketDirectsMessages setListener(ListenerSocketDirectsMessages listener) {
        App.LISTENERS.put("directs:messages", listener);
        return this;
    }
}
