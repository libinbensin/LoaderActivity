package com.libin.loaderactivitytest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Libin
 */
public abstract class LoaderActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks{

    private static final String TAG = LoaderActivity.class.getSimpleName();

    /**
     * Key for loader id
     */
    private static final java.lang.String KEY_LOADER_ID = "LOADER_ID";
    /**
     * Key for loader bundle
     */
    private static final String KEY_LOADER_BUNDLE = "LOADER_BUNDLE";
    /**
     * Generate unique integer value for loader id.
     */
    private static AtomicInteger mUniqueLoaderId = new AtomicInteger(1);
    private int mLoaderId;
    private int mLoaderRequestCode = -1;
    private Bundle mLoaderBundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null) {
            mLoaderId = savedInstanceState.getInt(KEY_LOADER_ID , createUniqueLoaderId());
            mLoaderBundle = savedInstanceState.getBundle(KEY_LOADER_BUNDLE);
        }else {
            mLoaderId = createUniqueLoaderId();
        }

        getSupportLoaderManager().initLoader(mLoaderId , savedInstanceState , this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_LOADER_ID , mLoaderId);
        outState.putBundle(KEY_LOADER_BUNDLE , mLoaderBundle);
    }

    @Override
    public Loader onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<Object>(this) {
            @Override
            public Object loadInBackground() {
                return loadDataInBackground(args);
            }
            @Override
            protected void onStartLoading() {
                // load new data
                if(mLoaderRequestCode >= 0) {
                    forceLoad();
                    Log.d(TAG, "onStartLoading  called and forced to load data");
                }else {
                    Log.d(TAG, "onStartLoading  called but the loader is not requested");
                }
            }

            @Override
            protected void onStopLoading() {
                Log.d(TAG, "onStopLoading  called");
                cancelLoad();
                clearLoader();
            }

            @Override
            public void onCanceled(Object data) {
                Log.d(TAG, "onCanceled  called");
                clearLoader();
            }

            @Override
            protected void onReset() {
                Log.d(TAG, "onReset  called");
                clearLoader();
            }
        };
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        // deliver result
        onLoaderResult(mLoaderRequestCode , data);
        Log.d(TAG, "Loader of id = " + loader.getId() +  "delivered the result");
    }

    @Override
    public void onLoaderReset(Loader loader) {
        clearLoader();
        Log.d(TAG, "Loader of id = " + loader.getId() +  "is reset");
    }

    private void clearLoader() {
        mLoaderRequestCode = -1;
    }

    protected void startLoaderForResult(int requestCode, Bundle bundle) {
        Log.d(TAG, "Restarting loader for the request code = " + requestCode);
        mLoaderRequestCode = requestCode;
        getSupportLoaderManager().restartLoader(mLoaderId , bundle , this);
    }

    private int createUniqueLoaderId() {
        int loaderId =  mUniqueLoaderId.incrementAndGet();
        Log.d(TAG , "Created unique loader id = " + loaderId);
        return loaderId;
    }

    protected abstract Object loadDataInBackground(Bundle bundle);
    protected abstract void onLoaderResult(int requestCode , Object data);
}
