package com.libin.loaderactivitytest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ListActivity extends LoaderActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.testLoader).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showResult("Loading..");
                startLoaderForResult(0 , null);
            }
        });

        findViewById(R.id.testDetail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ListActivity.this , DetailActivity.class));
            }
        });

    }

    @Override
    protected Object loadDataInBackground(int requestCode, Bundle bundle) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Loaded list items from background";
    }

    @Override
    protected void onLoaderResult(int requestCode, Object data) {
       showResult(data.toString());
    }


    private void showResult(String result){
        // Toast.makeText(this , data.toString() , Toast.LENGTH_LONG).show();
        TextView textView = (TextView) findViewById(R.id.listTextView);
        textView.setText(result);
    }

}
