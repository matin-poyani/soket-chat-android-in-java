package AsyncTasks;

import android.os.AsyncTask;

import ir.ncis.chat.App;

public class AsyncOnline extends AsyncTask<Runnable, Void, Boolean> {
    private Runnable onSuccess, onFailure;

    @Override
    protected Boolean doInBackground(Runnable... runnables) {
        if (runnables != null) {
            if (runnables.length > 0) {
                onSuccess = runnables[0];
                if (runnables.length > 1) {
                    onFailure = runnables[1];
                }
            }
        }
        return App.isOnline();
    }

    @Override
    protected void onPostExecute(Boolean result) {
        if (result) {
            if (onSuccess != null) {
                onSuccess.run();
            }
        } else {
            if (onFailure != null) {
                onFailure.run();
            }
        }
    }
}
