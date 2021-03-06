package com.a0.ztransport2.robinwilde.ztransport2;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

public class DbHelperMethods {

    private static String HTTP_OK="200";
    private static String HTTP_BAD_REQUEST="400";

    public static void postRequester(final Context context, final JSONObject data, String url, final VolleyCallback listener){

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, data, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    String responseString = (String) response.get("response");
                    String statusString = (String) response.get("status");
                    if(statusString.equals(HTTP_OK)){
                        listener.onSuccess(response);
                    }
                    else if(statusString.equals(HTTP_BAD_REQUEST)){
                        listener.onError(context.getString(R.string.call_failed));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if(error==null){
                    listener.onError(context.getString(R.string.error_network_error));
                }
                else if(error.networkResponse==null){
                    listener.onError(context.getString(R.string.error_control_with_admin));
                }
                else if(error!= null && error.networkResponse.statusCode==400) {
                    listener.onError(context.getString(R.string.error_network_error_bad_request));
                }
                else{
                    listener.onError(context.getString(R.string.error_network_error_bad_request)+": "+String.valueOf(error.networkResponse.statusCode));
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }
    public static void getRequester(final Context context, String url, JSONObject data, final VolleyCallback listener){

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, data, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    String responseString = (String) response.get("status");
                    if(responseString.equals("200")){
                        listener.onSuccess(response);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if(error.networkResponse==null){
                    listener.onError(context.getString(R.string.error_network_error));
                }
                else{
                    listener.onError(String.valueOf(error.networkResponse.statusCode));
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }
}
