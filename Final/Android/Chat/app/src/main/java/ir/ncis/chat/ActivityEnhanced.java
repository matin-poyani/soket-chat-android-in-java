package ir.ncis.chat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.text.MessageFormat;

@SuppressLint("Registered")
public class ActivityEnhanced extends AppCompatActivity {
    protected boolean exit = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (App.INFLATER == null) {
            App.INFLATER = getLayoutInflater();
        }
        App.ACTIVITY = this;
    }

    public void closeKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void runActivity(Class targetActivity) {
        runActivity(targetActivity, false);
    }

    public void runActivity(Class targetActivity, Bundle bundle) {
        runActivity(targetActivity, bundle, false);
    }

    public void runActivity(Class targetActivity, boolean finish) {
        runActivity(targetActivity, null, finish);
    }

    public void runActivity(Class targetActivity, Bundle bundle, boolean finish) {
        Log.i("DBG", MessageFormat.format("{0} => {1}", this.getClass().getName(), targetActivity.getName()));
        Intent intent = new Intent(this, targetActivity);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (finish) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            finish();
        }
        startActivity(intent);
    }

    public void checkedExit() {
        if (exit) {
            App.exit();
        } else {
            exit = true;
            App.toast(getString(R.string.message_exit));
            App.HANDLER.postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 2000);
        }
    }
}
