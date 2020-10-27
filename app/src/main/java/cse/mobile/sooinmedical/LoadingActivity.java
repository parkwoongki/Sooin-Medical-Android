package cse.mobile.sooinmedical;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class LoadingActivity extends AppCompatActivity {
    static final String TAG = "ThreadTest";
    boolean mRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        getSupportActionBar().hide();
    }

    /* 그냥 써보고 싶어서 AsyncTask 써봄 */

    @Override
    protected void onStart() {
        super.onStart();

        CounterTask taskCounter = new CounterTask();
        mRunning = true;
        taskCounter.execute();
    }

    @Override
    protected void onStop() {
        super.onStop();

        mRunning = false;
    }

    private class CounterTask extends AsyncTask<Integer, Integer, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            Intent sIntent = new Intent(getApplicationContext(), EmailPasswordActivity.class);
            startActivity(sIntent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }

        @Override
        protected void onProgressUpdate(Integer[] values) { // 여러개 받을 수 있따.
            super.onProgressUpdate(values);
        }

        @Override
        protected Integer doInBackground(Integer[] values) {
            int i = 0;
            for (i = 0; i < 2 && mRunning; i++) {
                Log.i(TAG, "[" + Thread.currentThread().getName() + "] Count : " + i);
                publishProgress(i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            return i;
        }
    }
}
