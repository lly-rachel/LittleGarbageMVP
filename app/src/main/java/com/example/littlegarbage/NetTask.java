package com.example.littlegarbage;

public interface NetTask<T> {

    void execute(T data,LoadTasksCallBack callBack);

}
