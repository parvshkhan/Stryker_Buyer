package com.app.stryker;

import android.content.Context;

import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;

/**
 * Created by admin on 3/6/2017.
 */
public class TokenRetryPolicy implements RetryPolicy {


    public TokenRetryPolicy(Context context, Contact contact) {

    }

    @Override
    public int getCurrentTimeout() {
        return 0;
    }

    @Override
    public int getCurrentRetryCount() {
        return 0;
    }

    @Override
    public void retry(VolleyError error) throws VolleyError {

    }
}
