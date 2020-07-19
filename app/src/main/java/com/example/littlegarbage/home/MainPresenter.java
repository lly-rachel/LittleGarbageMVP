package com.example.littlegarbage.home;

public class MainPresenter implements MainContract.Presenter{

    MainContract.View mView;

    public MainPresenter(MainContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void loadData() {
        mView.onDataLoaded();
    }

    @Override
    public void clickPass() {
        mView.clickPassFinished();
    }
}
