package com.example.stopcall.app.activities;

import android.content.Intent;
import android.os.Bundle;
import com.example.stopcall.app.dal.DalFactory;
import roboguice.activity.RoboFragmentActivity;

public class MainActivity extends RoboFragmentActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        init();
        Intent intent = new Intent(this, ItemDetailActivity.class);
        startActivity(intent);
        finish();
    }

    private void init() {

        DalFactory dalFactory = new DalFactory(this.getApplicationContext());
    }
}
