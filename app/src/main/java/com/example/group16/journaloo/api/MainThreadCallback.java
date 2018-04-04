package com.example.group16.journaloo.api;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public abstract class MainThreadCallback implements Callback {
    private static final String TAG = MainThreadCallback.class.getSimpleName();

    abstract public void onFail(final Exception error);

    abstract public void onSuccess(final String responseBody);

    @Override
    public void onFailure(Call call, final IOException e) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                e.printStackTrace();
                onFail(e);
            }
        });
    }

    @Override
    public void onResponse(final Call call, final Response response) {
        if (!response.isSuccessful()) {
            onFailure(call, new IOException("Failed"));
            return;
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (response.isSuccessful()) {
                    try {
                        onSuccess(response.body().string());
                        return;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                onFailure(call, new IOException("Failed"));
            }
        });
    }

    private void runOnUiThread(Runnable task) {
        new Handler(Looper.getMainLooper()).post(task);
    }
}