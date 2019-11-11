package com.qmobileme.www.noqoodysdk_lib;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by Saneeb Salam
 * on 11-11-2019.
 */
public class Pay {

    public Pay(Activity activity, String UserName, String Password) {

        Intent intent = new Intent(activity, Activity_Payment_Option.class);
        intent.putExtra(Noqoody_Keys.UserName, UserName);
        intent.putExtra(Noqoody_Keys.Password, Password);
        activity.startActivityForResult(intent, 101);
        activity.overridePendingTransition(com.qmobileme.www.noqoodysdk_lib.R.anim.anim_slide_in_left,
                com.qmobileme.www.noqoodysdk_lib.R.anim.anim_slide_out_left);
    }
}
