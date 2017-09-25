package com.alia.sisdiary.ui.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.alia.sisdiary.R;
import com.alia.sisdiary.ui.controller.LoginController;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private TextView mErrorView;
    LoginController mLoginController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setupViews();
        mLoginController = new LoginController(this, loginListener);
        mLoginController.onViewCreated();
    }

    LoginController.LoginListener loginListener = new LoginController.LoginListener() {
        @Override
        public void onLoginSuccessful(LoginController.Account account) {
            // so we got account stored in SharedPrefs and can do anything with it
            finish();
        }

        @Override
        public void onLoginError(String errorMsg) {
            mErrorView.setText(errorMsg);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mLoginController.onActivityResult(requestCode, resultCode, data);
    }

    private void setupViews() {
        mErrorView = (TextView) findViewById(R.id.error_view);
    }


    @Override
    public void onBackPressed() {
        //Move the task containing this activity to the back of the activity stack, so
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

}
