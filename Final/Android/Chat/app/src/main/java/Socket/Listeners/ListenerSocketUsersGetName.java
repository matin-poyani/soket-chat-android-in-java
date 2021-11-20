package Socket.Listeners;

import Socket.Structures.StructUsersName;

public interface ListenerSocketUsersGetName extends ListenerBase {
    void OnResult(StructUsersName result);
}
