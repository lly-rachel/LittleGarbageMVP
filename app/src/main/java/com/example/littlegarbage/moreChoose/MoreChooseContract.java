package com.example.littlegarbage.moreChoose;

import android.content.Context;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public interface MoreChooseContract {

    interface Presenter{
        void clickSure();
        void deleteFile(Context context);
        void ShowDialog(Context context, boolean isFirst, List<String> list, TextView citychooseTv, ListView citylist);

    }

    interface View{
        void deleteFileFinished();
        void clickSureFinished();
        void ShowDialogFinished();
    }
}
