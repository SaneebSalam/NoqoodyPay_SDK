package com.qmobileme.www.noqoodysdk_lib;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;


/**
 * Created by Saneeb Salam
 * on 07-11-2019.
 */
public class Activity_Payment_Option extends AppCompatActivity {

    RadioGroup radiogp;
    RadioButton radiopayviamobile, radiopayviadebit, radiopayviacredit, radiopayviapaypal, radiopayviaibcard;
    TextView Pay_payment;
    boolean isPayviaibcard = false, isPayviapaypal = false, isPayviamobile = false, isPayvianaps = false,
            isPayviacc = false;
    RelativeLayout progressBarlayout;
    String Reference, UserName, Password, Access_token, ErrorMessage, RedirectURL, PayviaibcardURL, PayviapaypalURL,
            PayviamobileURL, PayvianapsURL, PayviaccURL;
//    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_payment_option);

        if (getSupportActionBar() != null)
            getSupportActionBar().hide();


        radiogp = findViewById(R.id.radiogroup);
        radiopayviamobile = findViewById(R.id.payviamobile);
        radiopayviadebit = findViewById(R.id.payviadebit);
        radiopayviacredit = findViewById(R.id.payviacredit);
        radiopayviapaypal = findViewById(R.id.payviapaypal);
        radiopayviaibcard = findViewById(R.id.payviaibcard);
        progressBarlayout = findViewById(R.id.progressBarlayout);

        Pay_payment = findViewById(R.id.Pay_payment);

        final int random = new Random().nextInt(61) + 20;

        UserName = getIntent().getStringExtra(Noqoody_Keys.UserName);
        Password = getIntent().getStringExtra(Noqoody_Keys.Password);
        Reference = "WDN198110719105040357" + random;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        Login(UserName, Password);

//        try {
//            client = new OkHttpClient();
//            JSONObject json = new JSONObject();
//            json.put("grant_type", "password");
//            json.put("response_type", "token");
//            json.put("username", UserName);
//            json.put("password", Password);
//            String response = post("http://sandbox.noqoodypay.com/API/token", json.toString());
//            System.out.println(response);
//        } catch (IOException | JSONException e) {
//            e.printStackTrace();
//        }


    }

//    String run(String url) throws IOException {
//        Request request = new Request.Builder()
//                .url(url)
//                .build();
//
//        try (Response response = client.newCall(request).execute()) {
//            return response.body().string();
//        }
//    }

//    String post(String url, String  json) throws IOException {
//        RequestBody body = RequestBody.create(json, JSON);
//        Request request = new Request.Builder()
//                .url(url)
//                .post(body)
//                .build();
//        try (Response response = client.newCall(request).execute()) {
//            return response.body().string();
//        }
//    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        ReultMessage(false, "Payment Cancelled");
    }


    public void Login(final String UserName, final String Password) {
        progressBarlayout.setVisibility(View.VISIBLE);

        AndroidNetworking.post("http://sandbox.noqoodypay.com/API/token")
                .addBodyParameter("grant_type", "password")
                .addBodyParameter("response_type", "token")
                .addBodyParameter("username", UserName)
                .addBodyParameter("password", Password)
                .setTag("Login")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println("response: " + response.toString());

                        try {
                            Access_token = response.getString("access_token");
                            showDialog_pay();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            alert_finish("Login Error");
                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        System.out.println(error.getErrorCode() + " :" + error.getErrorBody());
                        System.out.println("error: " + error.toString());
                        progressBarlayout.setVisibility(View.GONE);
                        try {
                            ErrorMessage = new JSONObject(error.getErrorBody()).getString("error_description");
                            alert_finish(ErrorMessage);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            alert_finish("Login Error");
                        }
                        // handle error

                    }
                });
    }

    private void showDialog_pay() {

        progressBarlayout.setVisibility(View.VISIBLE);

        AndroidNetworking.get("http://sandbox.noqoodypay.com/API/api/Members/GetPaymentLinks/8Bf4e79AK?reference=" + Reference + "&description=DietnNut&amount=20&CustomerEmail=tomy@qmobileme.com&CustomerMobile=33880089&CustomerName=Fiona")
                .addHeaders("Authorization", "bearer " + Access_token)
                .setTag("showDialog")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        System.out.println("showDialog response: " + response.toString());

                        progressBarlayout.setVisibility(View.GONE);

                        try {

                            if (response.getBoolean("success")) {
//                                RedirectURL = response.getString("ValidationURL");
                                RedirectURL = "Home/GetStatus";
                                RedirectURL = RedirectURL.replace("\\", "");

                                System.out.println("RedirectURL: " + RedirectURL);
//                                JSONObject PaymentURL = response.getJSONObject("PaymentURL");

                                if (!response.getString("PaypalLink").equalsIgnoreCase("null")) {
                                    isPayviapaypal = true;
                                    PayviapaypalURL = response.getString("PaypalLink");
                                }
                                if (!response.getString("NapsLink").equalsIgnoreCase("null")) {
                                    isPayvianaps = true;
                                    PayvianapsURL = response.getString("NapsLink");
                                }
                                if (!response.getString("CreditCardLink").equalsIgnoreCase("null")) {
                                    isPayviacc = true;
                                    PayviaccURL = response.getString("CreditCardLink");
                                }
                                if (!response.getString("MobileLink").equalsIgnoreCase("null")) {
                                    isPayviamobile = true;
                                    PayviamobileURL = response.getString("MobileLink");
                                }
                                if (!response.getString("IBCardLink").equalsIgnoreCase("null")) {
                                    isPayviaibcard = true;
                                    PayviaibcardURL = response.getString("IBCardLink");
                                }
                                if (!response.getString("CyberSecureLink").equalsIgnoreCase("null")) {
                                    isPayviacc = true;
                                    PayviaccURL = response.getString("CyberSecureLink");
                                }


                                openpayment();

                            } else
                                alert_finish(response.getString("message"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressBarlayout.setVisibility(View.GONE);
                            alert_finish("Payment Error");

                        }
                    }

                    @Override
                    public void onError(ANError error) {
                        System.out.println(error.getErrorCode() + " :" + error.getErrorBody());
                        alert_finish("Payment Error");
                        progressBarlayout.setVisibility(View.GONE);
                    }
                });
    }

    private void openpayment() {

        if (!isPayviacc)
            radiopayviacredit.setVisibility(View.GONE);
        if (!isPayvianaps)
            radiopayviadebit.setVisibility(View.GONE);
        if (!isPayviapaypal)
            radiopayviapaypal.setVisibility(View.GONE);
        if (!isPayviaibcard)
            radiopayviaibcard.setVisibility(View.GONE);

        Pay_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radiogp.getCheckedRadioButtonId();


                if (selectedId == radiopayviamobile.getId()) {
//                alert_sendSMS("Do you want to pay " + MobilePrice + " QAR from your number", MobileKeyword);
//                    requestAppPermissions(new String[]{Manifest.permission.SEND_SMS}, SMSRequest);
                } else if (selectedId == radiopayviadebit.getId()) {
                    Activity_Payment_Option.this.Paymentweb(PayvianapsURL, RedirectURL);
//                    PayviaCard("RefillByNapsRequestMobile");
                } else if (selectedId == radiopayviacredit.getId()) {
                    Activity_Payment_Option.this.Paymentweb(PayviaccURL, RedirectURL);
                } else if (selectedId == radiopayviapaypal.getId()) {
                    Activity_Payment_Option.this.Paymentweb(PayviapaypalURL, RedirectURL);
                } else if (selectedId == radiopayviaibcard.getId()) {
                    Activity_Payment_Option.this.Paymentweb(PayviaibcardURL, RedirectURL);
                } else {
                    Activity_Payment_Option.this.alert_ok("Please choose one option");
                }
            }
        });

    }

    void Paymentweb(String url, String redirecturl) {


        Intent intent = new Intent(this, Activity_Payment.class);
        intent.putExtra(Noqoody_Keys.paymenturl, url);
        intent.putExtra(Noqoody_Keys.RedirectUrl, redirecturl);
//                                intent.putExtra(Noqoody_Keys.RedirectUrl, response.getString("RedirectUrl"));
        startActivityForResult(intent, 101);
        overridePendingTransition(R.anim.anim_slide_in_left,
                R.anim.anim_slide_out_left);
//        finish();

    }

    public void alert_finish(final String text) {
        new AlertDialog.Builder(this)
                .setMessage(text)
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ReultMessage(false, text);
                    }
                })
                .show();

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


    private void ReultMessage(boolean Status, String TransactionMessage) {

        Intent intent = new Intent();
        intent.putExtra(Noqoody_Keys.paymentresult, TransactionMessage);
        intent.putExtra(Noqoody_Keys.paymentresult_status, Status);

        setResult(Activity.RESULT_OK, intent);

        finish();

        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_right);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode == Activity.RESULT_OK) {
                Intent intent = new Intent();
                intent.putExtra(Noqoody_Keys.paymentresult, data.getStringExtra(Noqoody_Keys.paymentresult));
                intent.putExtra(Noqoody_Keys.paymentresult_status, data.getBooleanArrayExtra(Noqoody_Keys.paymentresult_status));

                setResult(Activity.RESULT_OK, intent);
                finish();

                overridePendingTransition(R.anim.anim_slide_in_right,
                        R.anim.anim_slide_out_right);
            }
        }
    }

}
