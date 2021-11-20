package Socket.Listeners;

import Socket.Structures.StructDirectsSend;

public interface ListenerSocketDirectsSend extends ListenerBase {
    void OnResult(StructDirectsSend result);
}
