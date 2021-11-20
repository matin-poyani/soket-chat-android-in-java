package Socket.Listeners;

import Socket.Structures.StructUsersGroups;

public interface ListenerSocketUsersGetGroups extends ListenerBase {
    void OnResult(StructUsersGroups result);
}
