package com.libin.loaderactivitytest;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends LoaderActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.testLoader).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLoaderForResult(0 , null);
            }
        });

    }

    @Override
    protected Object loadDataInBackground(Bundle bundle) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onLoaderResult(int requestCode, Object data) {
        Toast.makeText(MainActivity.this , "onResult" , Toast.LENGTH_LONG).show();
    }
}
