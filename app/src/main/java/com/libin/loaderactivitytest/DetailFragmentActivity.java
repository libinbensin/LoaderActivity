package com.libin.loaderactivitytest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * @author Libin
 */
public class DetailFragmentActivity extends LoaderActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragement_loader);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if(fragment == null) {
            fragment = new DetailFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(fragment , DetailFragment.class.getSimpleName())
                    .commit();
        }
    }

    @Override
    protected Object loadDataInBackground(int requestCode, Bundle bundle) {
        return null;
    }

    @Override
    protected void onLoaderResult(int requestCode, Object data) {

    }
}
