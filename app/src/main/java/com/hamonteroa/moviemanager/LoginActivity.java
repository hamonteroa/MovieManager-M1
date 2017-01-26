package com.hamonteroa.moviemanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

/**
 * Created by hamonteroa on 1/9/17.
 */

public class LoginActivity extends AppCompatActivity {
    private static final String LOG_TAG = LoginActivity.class.getSimpleName();

    private Button mLoginButton;
    private TextView mDebugTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginButton = (Button) findViewById(R.id.login_button);
        mDebugTextView = (TextView) findViewById(R.id.debug_textView);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VolleySingleton.getInstance(getApplicationContext())
                        .addToRequestQueue(
                                new JsonObjectRequest(Request.Method.GET, TMDBNetworkUrl.getNewToken(), null, new Response.Listener<JSONObject>() {

                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Log.v(LOG_TAG, "fetchNewRequestToken-onResponse response: " + response);

                                        try {
                                            if (response.getBoolean(TMDBNetworkConstants.JSONResponseKeys.SUCCESS)) {
                                                User.getInstance().setRequestToken(response.getString(TMDBNetworkConstants.JSONResponseKeys.REQUEST_TOKEN));

                                                startActivity(new Intent(getApplicationContext(), AuthenticationActivity.class));
                                            } else {
                                                int statusCode = response.getInt(TMDBNetworkConstants.JSONResponseKeys.STATUS_CODE);
                                                String statusMessage = response.getString(TMDBNetworkConstants.JSONResponseKeys.STATUS_MESSAGE);

                                                Log.v(LOG_TAG, "requestNewSessionID statusCode: " + statusCode + ", statusMessage: " + statusCode);
                                            }
                                        } catch (JSONException e) {
                                            Log.e(LOG_TAG, "fetchNewRequestToken-onResponse exception: " + e.getMessage());
                                            e.printStackTrace();
                                        }

                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Log.v(LOG_TAG, "fetchNewRequestToken-onErrorResponse error: " + error);
                                    }
                                }));
            }
        });
    }
}
