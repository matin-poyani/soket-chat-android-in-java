package Socket.Listeners;

import Socket.Structures.StructUsersRegister;

public interface ListenerSocketUsersRegister extends ListenerBase {
    void OnResult(StructUsersRegister result);
}
