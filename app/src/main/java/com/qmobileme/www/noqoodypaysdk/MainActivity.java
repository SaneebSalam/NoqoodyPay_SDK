package com.qmobileme.www.noqoodypaysdk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.qmobileme.www.noqoodysdk_lib.Noqoody_Keys;
import com.qmobileme.www.noqoodysdk_lib.Pay;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        startActivity(new Intent(this, Activity_Payment_Option.class));


    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Noqoody_Keys.Activity_RequestCode) {
            if (resultCode == Activity.RESULT_OK) {
                if (data.getBooleanExtra(com.qmobileme.www.noqoodysdk_lib.Noqoody_Keys.paymentresult_status, false))
                    System.out.println("Success: " + data.getStringExtra(Noqoody_Keys.paymentresult));
                else
                    System.out.println("Failed: " + data.getStringExtra(Noqoody_Keys.paymentresult));
            }
        }
    }

    public void Pay(View view) {

        new Pay(MainActivity.this, "FionaTomy", "Kt8$4S@n");
    }
}
