package com.libin.loaderactivitytest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Libin
 */
public class DetailFragment extends LoaderFragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail , null);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startLoader(0 , null);
    }

    @Override
    protected Object loadDataInBackground(int requestCode, Bundle bundle) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "Loaded detail from background on fragment";
    }

    @Override
    protected void onLoaderResult(int requestCode, Object data) {
        showResult(data.toString());
    }

    private void showResult(String result){
        Toast.makeText(getContext() , result , Toast.LENGTH_LONG).show();
        TextView textView = (TextView) getView().findViewById(R.id.result_view);
        textView.setText(result);
    }
}
