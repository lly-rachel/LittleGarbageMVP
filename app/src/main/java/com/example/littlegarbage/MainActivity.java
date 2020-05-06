package com.example.littlegarbage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.littlegarbage.db.DBManeger;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DBManeger.initDB(this);

        search=findViewById(R.id.main_search);
        search.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Intent intent;

        switch (v.getId()){

            case R.id.main_search:
                intent=new Intent(this,SearchActivity.class);
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }

        startActivity(intent);
    }
}
