package Socket.Listeners;

import Socket.Structures.StructGlobalMessage;

public interface ListenerSocketGlobalNewDirect extends ListenerBase {
    void OnResult(StructGlobalMessage result);
}
