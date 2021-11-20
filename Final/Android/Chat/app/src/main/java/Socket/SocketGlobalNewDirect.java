package Socket;

import Socket.Listeners.ListenerSocketGlobalNewDirect;
import ir.ncis.chat.App;

public class SocketGlobalNewDirect {

    public void run() {
        SocketClient.send("global", "newdirect");
    }

    public SocketGlobalNewDirect setListener(ListenerSocketGlobalNewDirect listener) {
        App.LISTENERS.put("global:newdirect", listener);
        return this;
    }
}
