package com.libin.loaderactivitytest;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Activity to load data on worker thread using {@link AsyncTaskLoader}
 * and deliver the result on UI Thread.
 *
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
     * Key for loader request code
     */
    private static final String KEY_LOADER_REQUEST_CODE = "LOADER_REQUEST_CODE";
    /**
     * Generate unique integer value for loader id.
     */
    private static AtomicInteger mUniqueLoaderId = new AtomicInteger(0);
    private int mLoaderId;
    private int mLoaderRequestCode = -1;
    private Bundle mLoaderBundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null) {
            mLoaderId = savedInstanceState.getInt(KEY_LOADER_ID);
            mLoaderBundle = savedInstanceState.getBundle(KEY_LOADER_BUNDLE);
            mLoaderRequestCode = savedInstanceState.getInt(KEY_LOADER_REQUEST_CODE);
            if(mLoaderRequestCode >= 0) {
                getSupportLoaderManager().initLoader(mLoaderId, savedInstanceState, this);
            }
        }else {
            mLoaderId = createUniqueLoaderId();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_LOADER_ID , mLoaderId);
        outState.putBundle(KEY_LOADER_BUNDLE , mLoaderBundle);
        outState.putInt(KEY_LOADER_REQUEST_CODE , mLoaderRequestCode);
    }

    @Override
    public Loader onCreateLoader(final int id, final Bundle args) {
        return new AsyncTaskLoader<Object>(this) {
            @Override
            public Object loadInBackground() {
                return loadDataInBackground(mLoaderRequestCode , args);
            }
            @Override
            protected void onStartLoading() {
                // load new data
                if(mLoaderRequestCode >= 0) {
                    forceLoad();
                    Log.d(TAG, "onStartLoading called for id = " + id + " and forced to load data");
                }else {
                    Log.d(TAG, "onStartLoading called for id = " + id + " but the loader is not requested");
                }
            }

            @Override
            protected void onStopLoading() {
                Log.d(TAG, "onStopLoading called for loader id = "  + id);
            }

            @Override
            public void onCanceled(Object data) {
                Log.d(TAG, "onCanceled  called for loader id = " + id);
                clearLoader();
            }

            @Override
            protected void onReset() {
                Log.d(TAG, "onReset  called for loader id = " + id);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader loader, final Object data) {
        // deliver result
        // Though the load finished is called on UI thread, no fragment transaction (commit or show)
        // can be done on onLoadFinished, so running the result on a runnable.
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                onLoaderResult(mLoaderRequestCode , data);
                clearLoader();
            }
        });
        Log.d(TAG, "Loader of id = " + loader.getId() +  " delivered the result");
    }

    @Override
    public void onLoaderReset(Loader loader) {
        Log.d(TAG, "Loader of id = " + loader.getId() +  " is reset");
    }

    private void clearLoader() {
        mLoaderRequestCode = -1;
    }

    /**
     * Request loader for the background callback
     *
     * @param requestCode The request code
     * @param bundle The {@link Bundle} to pass on the {#on}
     */
    protected void startLoaderForResult(int requestCode, Bundle bundle) {
        Log.d(TAG, "Restarting loader for request code = " + requestCode);
        mLoaderRequestCode = requestCode;
        getSupportLoaderManager().restartLoader(mLoaderId , bundle , this);
    }

    public int createUniqueLoaderId() {
        int loaderId =  mUniqueLoaderId.incrementAndGet();
        Log.d(TAG , "Created unique loader id = " + loaderId);
        return loaderId;
    }

    /**
     * Called on a worker thread to perform the actual load and to return
     * the result of the load operation.
     * @param requestCode The request code to identify the request.
     * @param bundle The {@link Bundle} passed on the {#startLoaderForResult}
     * @return The result of the load operation.
     */
    protected abstract Object loadDataInBackground(int requestCode , Bundle bundle);

    /**
     * Called to deliver the result on UI Thread
     * @param requestCode The request code to identify the request.
     * @param data The result of the load operation
     */
    protected abstract void onLoaderResult(int requestCode , Object data);
}
