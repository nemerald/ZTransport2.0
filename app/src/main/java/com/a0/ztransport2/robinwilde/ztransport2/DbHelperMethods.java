package com.a0.ztransport2.robinwilde.ztransport2;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.a0.ztransport2.robinwilde.ztransport2.Objects.TimeReport;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class DbHelperMethods {

    public static void postRequester(final Context context, JSONObject data, String url){

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, data, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    String responseString = (String) response.get("response");
                    String statusString = (String) response.get("status");
                    if(statusString.equals("200")){
                        Toast.makeText(context, context.getString(R.string.report_success), Toast.LENGTH_SHORT).show();
                    }
                    else if(statusString.equals("400")){
                        Toast.makeText(context, context.getString(R.string.report_failed), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(context, context.getString(R.string.error_network_error), Toast.LENGTH_SHORT).show();
                }
                else if(error!= null && error.networkResponse.statusCode==400) {
                    Toast.makeText(context,
                            context.getString(R.string.error_network_error_bad_request), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context,
                            String.valueOf(error.networkResponse.statusCode), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }
    public static void getRequester(final Context context, final PalletReportFragment.VolleyCallback callback){
        String urlJsonObj = "https://prod-15.northeurope.logic.azure.com:443/workflows/6533211b3671489e993edb348f13c3c4/triggers/request/paths/invoke?api-version=2016-10-01&sp=%2Ftriggers%2Frequest%2Frun&sv=1.0&sig=F24liE1532lDaexqEG8neoK_QVQIo4fMfctwg80B5pg";

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                urlJsonObj, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    String responseString = (String) response.get("status");
                    if(responseString.equals("200")){
                        JSONArray palletArray = response.getJSONArray("palletArray");
                        JSONObject lastRow = palletArray.getJSONObject(palletArray.length()-1);
                        callback.onSuccess(lastRow);
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
                Toast.makeText(context,
                        String.valueOf(error.networkResponse.statusCode), Toast.LENGTH_SHORT).show();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }
}
