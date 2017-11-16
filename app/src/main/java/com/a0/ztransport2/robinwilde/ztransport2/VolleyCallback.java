package com.a0.ztransport2.robinwilde.ztransport2;

import org.json.JSONObject;

public interface VolleyCallback {
    void onSuccess(JSONObject result);
    void onError(String message);
}
