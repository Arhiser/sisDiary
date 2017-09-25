package com.alia.sisdiary.ui.controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.alia.sisdiary.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by arhis on 25.09.2017.
 */

public class LoginController extends UIController {

    private static final int REQUEST_GOOGLE_LOGIN = 9001;

    private static final String KEY_STORED_ACCOUNT = "LoginController.KEY_STORED_ACCOUNT";

    private SignInButton googleLoginButton;
    private LoginButton fbLoginButton;

    private static GoogleApiClient mGoogleApiClient;
    private CallbackManager mCallbackManager;
    private Gson gson;

    LoginListener listener;

    public interface LoginListener {
        void onLoginSuccessful(Account account);
        void onLoginError(String errorMsg);
    }

    public LoginController(FragmentActivity activity, LoginListener listener) {
        super(activity);
        this.listener = listener;
    }

    private SharedPreferences getSharedPrefs() {
        return activity.getSharedPreferences(activity.getString(R.string.pref_user_data), Context.MODE_PRIVATE);
    }

    private Gson getGson() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }

    @Override
    public void onViewCreated() {
        googleLoginButton = (SignInButton) activity.findViewById(R.id.google_login_button);
        googleLoginButton.setSize(SignInButton.SIZE_WIDE);
        fbLoginButton = (LoginButton) activity.findViewById(R.id.fb_login_button);

        googleLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLoginGoogle();
            }
        });

        mCallbackManager = CallbackManager.Factory.create();
        fbLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String userId = loginResult.getAccessToken().getUserId();
                AccessToken accessToken = loginResult.getAccessToken();
                handleFbResult(accessToken);

            }

            @Override
            public void onCancel() {
                listener.onLoginError("Log In was canceled");

            }

            @Override
            public void onError(FacebookException error) {
                listener.onLoginError("Log in error" + String.valueOf(error));
            }
        });
    }

    private void performLoginGoogle() {
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(activity)
                .enableAutoManage(activity, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        listener.onLoginError("Google services connection failed");
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        activity.startActivityForResult(signInIntent, REQUEST_GOOGLE_LOGIN);
    }

    private void handleFbResult(AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {
                        if (response.getError() == null) {
                            try {
                                Account account = new Account(LoginType.facebook, object.getString("id"),
                                        object.getJSONObject("picture").getJSONObject("data").getString("url"),
                                        object.getString("name"));
                                persistAccount(account);
                                listener.onLoginSuccessful(account);
                            } catch (JSONException e) {
                                listener.onLoginError("Error while extracting facebook account data");
                            }
                        }
                        else {
                            listener.onLoginError(response.getError().getErrorMessage());
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields","id,name,picture");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            Account account = new Account(LoginType.google, acct.getId(), String.valueOf(acct.getPhotoUrl()), acct.getDisplayName());
            listener.onLoginSuccessful(account);
        } else {
            listener.onLoginError("Google login failed");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_GOOGLE_LOGIN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void persistAccount(Account account) {
        getSharedPrefs().edit().putString(KEY_STORED_ACCOUNT, getGson().toJson(account)).commit();
    }

    public Account loadAccount() {
        SharedPreferences prefs = getSharedPrefs();
        if (prefs.contains(KEY_STORED_ACCOUNT)) {
            return getGson().fromJson(prefs.getString(KEY_STORED_ACCOUNT, ""), Account.class);
        }
        return null;
    }

    public enum LoginType {
        facebook, google
    }

    public static class Account {
        LoginType loginType;
        String userId;
        String photoUrl;
        String name;

        public Account() {
        }

        public Account(LoginType loginType, String userId, String photoUrl, String name) {
            this.loginType = loginType;
            this.userId = userId;
            this.photoUrl = photoUrl;
            this.name = name;
        }
    }
}
