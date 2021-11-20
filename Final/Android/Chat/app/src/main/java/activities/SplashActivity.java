package activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ProgressBar;

import AsyncTasks.AsyncOnline;
import Socket.Listeners.ListenerSocketUsersLogin;
import Socket.SocketUsersLogin;
import Socket.Structures.StructUsersLogin;
import ir.ncis.chat.ActivityEnhanced;
import ir.ncis.chat.App;
import ir.ncis.chat.R;

public class SplashActivity extends ActivityEnhanced {
    private ProgressBar prgLoading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        prgLoading = findViewById(R.id.prgLoading);

        checkOnlineStatus();
    }

    private void checkOnlineStatus() {
        prgLoading.setVisibility(View.VISIBLE);
        new AsyncOnline().execute(
                new Runnable() {
                    @Override
                    public void run() {
                        App.HANDLER.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dismiss();
                            }
                        }, 1000);
                    }
                },
                new Runnable() {
                    @Override
                    public void run() {
                        prgLoading.setVisibility(View.INVISIBLE);
                        App.snack(
                                findViewById(R.id.lytBackground),
                                getString(R.string.error_no_internet),
                                getString(R.string.label_try_again),
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        checkOnlineStatus();
                                    }
                                }
                        );
                    }
                });
    }

    private void dismiss() {
        if (App.USER != null && App.PASS != null) {
            tryLogin();
        } else {
            runActivity(EntryActivity.class, true);
        }
    }

    private void tryLogin() {
        new AsyncOnline()
                .execute(new Runnable() {
                             @Override
                             public void run() {
                                 new SocketUsersLogin()
                                         .setListener(new ListenerSocketUsersLogin() {
                                             @Override
                                             public void OnResult(StructUsersLogin result) {
                                                 if (result != null) {
                                                     if (result.code == 1) {
                                                         App.CURRENT_USER = result.data;
                                                         App.toast(getString(R.string.message_welcome));
                                                         runActivity(MainActivity.class, true);
                                                     } else {
                                                         App.toast(result.error);
                                                         runActivity(EntryActivity.class, true);
                                                     }
                                                 } else {
                                                     App.toast(getString(R.string.error_no_internet));
                                                     App.HANDLER.postDelayed(new Runnable() {
                                                         @Override
                                                         public void run() {
                                                             App.exit();
                                                         }
                                                     }, 2000);
                                                 }
                                             }
                                         })
                                         .run(App.USER, App.PASS);
                             }
                         },
                        new Runnable() {
                            @Override
                            public void run() {
                                App.snack(
                                        findViewById(R.id.lytBackground),
                                        getString(R.string.error_no_internet),
                                        getString(R.string.label_try_again),
                                        new Runnable() {
                                            @Override
                                            public void run() {
                                                tryLogin();
                                            }
                                        }
                                );
                            }
                        });
    }

    @Override
    public void onBackPressed() {
        App.toast(getString(R.string.message_wait));
    }
}