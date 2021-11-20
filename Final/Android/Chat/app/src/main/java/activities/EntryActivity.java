package activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.EditText;

import java.text.MessageFormat;

import Socket.Listeners.ListenerSocketUsersLogin;
import Socket.Listeners.ListenerSocketUsersRegister;
import Socket.SocketUsersLogin;
import Socket.SocketUsersRegister;
import Socket.Structures.StructUsersLogin;
import Socket.Structures.StructUsersRegister;
import ir.ncis.chat.ActivityEnhanced;
import ir.ncis.chat.App;
import ir.ncis.chat.R;

public class EntryActivity extends ActivityEnhanced {
    private AppCompatButton btnLogin, btnRegister;
    private EditText edtName, edtPass, edtUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        loadViews();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboards();
                edtName.setError(null);
                edtPass.setError(null);
                edtUser.setError(null);
                String pass = edtPass.getText().toString().trim();
                final String user = edtUser.getText().toString().trim();
                boolean flag = true;
                if (pass.isEmpty()) {
                    edtPass.setError(getString(R.string.error_required_field));
                    flag = false;
                }
                if (user.isEmpty()) {
                    edtUser.setError(getString(R.string.error_required_field));
                    flag = false;
                }
                if (flag) {
                    final String passHash = App.sha1(pass);
                    new SocketUsersLogin()
                            .setListener(new ListenerSocketUsersLogin() {
                                @Override
                                public void OnResult(StructUsersLogin result) {
                                    if (result.code == 1) {
                                        App.EDITOR
                                                .putInt(getString(R.string.key_sp_id), result.data.id)
                                                .putString(getString(R.string.key_sp_name), result.data.name)
                                                .putString(getString(R.string.key_sp_user), user)
                                                .putString(getString(R.string.key_sp_pass), passHash)
                                                .apply();
                                        App.toast(getString(R.string.message_welcome));
                                        runActivity(MainActivity.class, true);
                                    } else {
                                        App.toast(result.error);
                                    }
                                }
                            })
                            .run(user, passHash);
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeKeyboards();
                edtName.setError(null);
                edtPass.setError(null);
                edtUser.setError(null);
                final String name = edtName.getText().toString().trim();
                String pass = edtPass.getText().toString().trim();
                final String user = edtUser.getText().toString().trim();
                boolean flag = true;
                if (name.isEmpty()) {
                    edtName.setError(getString(R.string.error_required_field));
                    flag = false;
                }
                if (pass.isEmpty()) {
                    edtPass.setError(getString(R.string.error_required_field));
                    flag = false;
                }
                if (user.isEmpty()) {
                    edtUser.setError(getString(R.string.error_required_field));
                    flag = false;
                }
                if (flag) {
                    final String passHash = App.sha1(pass);
                    new SocketUsersRegister()
                            .setListener(new ListenerSocketUsersRegister() {
                                @Override
                                public void OnResult(StructUsersRegister result) {
                                    if (result.code == 1) {
                                        App.EDITOR
                                                .putInt(getString(R.string.key_sp_id), result.data)
                                                .putString(getString(R.string.key_sp_name), name)
                                                .putString(getString(R.string.key_sp_user), user)
                                                .putString(getString(R.string.key_sp_pass), passHash)
                                                .apply();
                                        App.toast(getString(R.string.message_welcome));
                                        runActivity(MainActivity.class, true);
                                    } else {
                                        App.toast(MessageFormat.format("{0}: {1}", getString(R.string.error_register), result.error));
                                    }
                                }
                            })
                            .run(name, user, passHash);
                }
            }
        });
    }

    private void loadViews() {
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        edtName = findViewById(R.id.edtName);
        edtPass = findViewById(R.id.edtPass);
        edtUser = findViewById(R.id.edtUser);
    }

    @Override
    public void onBackPressed() {
        closeKeyboards();
        checkedExit();
    }

    private void closeKeyboards() {
        closeKeyboard(edtName);
        closeKeyboard(edtPass);
        closeKeyboard(edtUser);
    }
}
