package Socket.Listeners;

import Socket.Structures.StructUsersDirects;

public interface ListenerSocketUsersGetDirects extends ListenerBase {
    void OnResult(StructUsersDirects result);
}
