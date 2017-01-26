package com.hamonteroa.moviemanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.hamonteroa.moviemanager.model.User;
import com.hamonteroa.moviemanager.sync.TMDBNetworkConstants;
import com.hamonteroa.moviemanager.sync.TMDBNetworkUrl;
import com.hamonteroa.moviemanager.sync.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class AuthenticationActivity extends AppCompatActivity {
    private static final String LOG_TAG = AuthenticationActivity.class.getSimpleName();
    private WebView mWebView;
    /*
    The Movie DB
UserName: hamonteroa
Password: wrathchildD1
API KEY: c3d2f84e3eb79f0d9c0a2096e922ff11
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        mWebView = (WebView) findViewById(R.id.authentication_webView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl(TMDBNetworkUrl.loginWithToken());
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.v(LOG_TAG, "WebViewClient-shouldOverrideUrlLoading url: " + url);

                try {
                    String urlPath = (new URL(url).getPath()).toString();
                    String expectedUrlPath = (new StringBuilder("/authenticate/").append(User.getInstance().getRequestToken()).append("/allow")).toString();
                    if (urlPath.equals(expectedUrlPath)) requestNewSessionID();
                } catch (Exception e) {
                    Log.v(LOG_TAG, "exception: " + e.getMessage());
                    e.printStackTrace();
                }

                return false;
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient());
    }

    private void requestNewSessionID() {
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(
                new JsonObjectRequest(Request.Method.GET, TMDBNetworkUrl.getNewSessionID(),
                        null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v(LOG_TAG, "requestNewSessionID response: " + response);
                        try {
                            if (response.getBoolean(TMDBNetworkConstants.JSONResponseKeys.SUCCESS)) {
                                User.getInstance().setSessionID(response.getString(TMDBNetworkConstants.JSONResponseKeys.SESSION_ID));
                                requestUserID();

                            } else {
                                int statusCode = response.getInt(TMDBNetworkConstants.JSONResponseKeys.STATUS_CODE);
                                String statusMessage = response.getString(TMDBNetworkConstants.JSONResponseKeys.STATUS_MESSAGE);

                                Log.v(LOG_TAG, "requestNewSessionID statusCode: " + statusCode + ", statusMessage: " + statusCode);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
                )
        );
    }

    private void requestUserID() {
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(
                new JsonObjectRequest(Request.Method.GET, TMDBNetworkUrl.getUserID(),
                        null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.v(LOG_TAG, "requestUserID response: " + response);
                        try {
                            User.getInstance().setUserID(response.getString(TMDBNetworkConstants.JSONResponseKeys.USER_ID));
                            //User.saveUserInstance(AuthenticationActivity.this);
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
                )
        );
    }
}
