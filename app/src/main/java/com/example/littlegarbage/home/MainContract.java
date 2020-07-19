package com.example.littlegarbage.home;

public interface MainContract {

    interface Presenter{
        void loadData();
    }

    interface View{
        void onDataLoaded();
    }
}
