package Socket.Listeners;

import Socket.Structures.StructUsersChannels;

public interface ListenerSocketUsersGetChannels extends ListenerBase {
    void OnResult(StructUsersChannels result);
}
