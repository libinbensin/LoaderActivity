package com.libin.loaderactivitytest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

/**
 * @author Libin
 */
public class DetailActivity extends LoaderActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        showResult("Loading..");
        startLoader(0 , null);
    }

    @Override
    protected Object loadDataInBackground(int requestCode, Bundle bundle) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Loaded detail from background";
    }

    @Override
    protected void onLoaderResult(int requestCode, Object data) {
        showResult(data.toString());
    }

    private void showResult(String result){
        // Toast.makeText(this , data.toString() , Toast.LENGTH_LONG).show();
        TextView textView = (TextView) findViewById(R.id.detail_view);
        textView.setText(result);
    }
}
