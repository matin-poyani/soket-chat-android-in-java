package Socket.Listeners;

import Socket.Structures.StructDirectsMessages;

public interface ListenerSocketDirectsMessages extends ListenerBase {
    void OnResult(StructDirectsMessages result);
}
