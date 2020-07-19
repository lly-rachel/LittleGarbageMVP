package com.example.littlegarbage.home;

public interface MainContract {

    interface Presenter{
        void loadData();
        void clickPass();
    }

    interface View{
        void onDataLoaded();
        void clickPassFinished();
    }
}
