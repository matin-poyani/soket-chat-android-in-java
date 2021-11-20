package Socket.Listeners;

import Socket.Structures.StructUsersLogin;

public interface ListenerSocketUsersLogin extends ListenerBase {
    void OnResult(StructUsersLogin result);
}
