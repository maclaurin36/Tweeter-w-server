package edu.byu.cs.tweeter.client.model.service.backgroundTask.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.model.service.backgroundTask.BackgroundTask;
import edu.byu.cs.tweeter.client.model.service.observer.ServiceObserver;

public abstract class BackgroundTaskHandler<T extends ServiceObserver> extends Handler {

    protected final T observer;

    public BackgroundTaskHandler(T observer) {
        super(Looper.getMainLooper());
        this.observer = observer;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        boolean success = msg.getData().getBoolean(BackgroundTask.SUCCESS_KEY);
        if (success) {
            handleSuccessMessage(msg.getData());
        } else if (msg.getData().containsKey(BackgroundTask.MESSAGE_KEY)) {
            String rawFailureMessage = msg.getData().getString(BackgroundTask.MESSAGE_KEY);
            String formattedFailureMessage = getFailureMessage() + ": " + rawFailureMessage;
            handleTaskFailure(formattedFailureMessage);
        } else if (msg.getData().containsKey(BackgroundTask.EXCEPTION_KEY)) {
            Exception ex = (Exception) msg.getData().getSerializable(BackgroundTask.EXCEPTION_KEY);
            String exceptionMessage = getFailureMessage() + " because of exception: " + ex.getMessage();
            handleTaskFailure(exceptionMessage);
        }
    }

    protected abstract void handleSuccessMessage(Bundle data);
    protected void handleTaskFailure(String message) {
        observer.handleFailure(message);
    }
    protected abstract String getFailureMessage();
}
