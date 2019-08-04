package com.example.contactlist.framework.util;

import android.app.Activity;
import android.content.Intent;

public class StartActivity {
    public static void startSpecificActivity(Activity activity, Class<?> otherActivityClass) {
        Intent intent = new Intent(activity, otherActivityClass);
        activity.startActivity(intent);
        activity.finish();
    }
}
