package Socket;

import Socket.Listeners.ListenerSocketDirectsSend;
import ir.ncis.chat.App;

public class SocketDirectsSend {

    public void run(int directId, String username, String password, String body, String media) {
        SocketClient.send("directs", "send", String.valueOf(directId), username, password, body, media);
    }

    public SocketDirectsSend setListener(ListenerSocketDirectsSend listener) {
        App.LISTENERS.put("directs:send", listener);
        return this;
    }
}
