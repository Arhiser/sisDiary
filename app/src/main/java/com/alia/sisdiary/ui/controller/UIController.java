package com.alia.sisdiary.ui.controller;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;

/**
 * Created by arhis on 25.09.2017.
 */

public class UIController {

    protected FragmentActivity activity;

    public UIController(FragmentActivity activity) {
        this.activity = activity;
    }

    public void onViewCreated() {

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
}
