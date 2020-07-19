package com.example.littlegarbage;

public interface LoadTasksCallBack<T> {

    void onSuccess(T data);
    void onStart();
    void onFailed();
    void onFinish();

}
