package com.qmobileme.www.noqoodypaysdk;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.qmobileme.www.noqoodysdk_lib.Activity_Payment_Option;
import com.qmobileme.www.noqoodysdk_lib.Noqoody_Keys;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        startActivity(new Intent(this, Activity_Payment_Option.class));


    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode == Activity.RESULT_OK) {
//                if (data.getBooleanExtra(com.qmobileme.www.noqoodysdk_lib.Noqoody_Keys.paymentresult_status, false))
                alert_ok(data.getStringExtra(Noqoody_Keys.paymentresult));
            }
        }
    }

    public void alert_ok(String text) {
        new AlertDialog.Builder(this)
                .setMessage(text)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();

    }

    public void Pay(View view) {

        Intent intent = new Intent(this, Activity_Payment_Option.class);
        intent.putExtra(Noqoody_Keys.UserName, "FionaTomy");
        intent.putExtra(Noqoody_Keys.Password, "Kt8$4S@n");
        startActivityForResult(intent, 101);
        overridePendingTransition(com.qmobileme.www.noqoodysdk_lib.R.anim.anim_slide_in_left,
                com.qmobileme.www.noqoodysdk_lib.R.anim.anim_slide_out_left);
    }
}
