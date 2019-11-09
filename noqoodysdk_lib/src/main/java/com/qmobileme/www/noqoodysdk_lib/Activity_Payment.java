package com.qmobileme.www.noqoodysdk_lib;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Saneeb Salam
 * on 01-12-2018.
 */
public class Activity_Payment extends AppCompatActivity {

    WebView webview;
    String URL, RedirectUrl;
    RelativeLayout progressBarlayout;
//            RedirectUrl;
//    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_payment);

        if (getSupportActionBar() != null)
            getSupportActionBar().hide();

        webview = findViewById(R.id.webview);
        progressBarlayout = findViewById(R.id.progressBarlayout);


//        Glide.with(this)
//                .load(R.drawable.loading_anim)
//                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
//                .into(load);


        URL = getIntent().getStringExtra(Noqoody_Keys.paymenturl);
        RedirectUrl = getIntent().getStringExtra(Noqoody_Keys.RedirectUrl);
//        URL="https://www.iplayin.com/_checkout.html";
//        URL="http://www.iplayin.com/payresponse.aspx?referenceid=NV17082802155442401&success=True&TransactionID=QPTRN240282017822560&MobileNumber=97433300020&Amount=1&InvoiceNo=NPMP201728250225232399";
//        RedirectUrl = getIntent().getStringExtra(Noqoody_Keys.RedirectUrl);

        DisplayURL(URL);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        ReultMessage(false, "Payment Cancelled");
    }

    private void ReultMessage(boolean Status, String TransactionMessage) {

        Intent intent = new Intent();
        intent.putExtra(Noqoody_Keys.paymentresult, TransactionMessage);
        intent.putExtra(Noqoody_Keys.paymentresult_status, Status);

        setResult(Activity.RESULT_OK, intent);
        moveBackPage();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void DisplayURL(String value) {
//        dialog = ProgressDialog.show(this, "Loading", "Wait while loading...");
//        System.out.println("DisplayURL: " + value);
        progressBarlayout.setVisibility(View.VISIBLE);

        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setLoadsImagesAutomatically(true);
        webview.addJavascriptInterface(new MyJavaScriptInterface(this), "HtmlViewer");

        webview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                // Activities and WebViews measure progress with different scales.
                // The progress meter will automatically disappear when we reach 100%
//                MainActivity.this.setProgress(progress * 1000);
                if (progress == 100) {
                    progressBarlayout.setVisibility(View.GONE);
                }
            }
        });

        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                System.out.println("url.........: " + url);

//                if (url.contains("www.noqoodypal.com/api/api/Walletuser/PaymentResponse?")
//                        || (url.contains("www.noqoodypal.com/api/api/Walletuser/PaymentResponseCC?")
//                        || (url.contains("PaymentResponsePayPal"))
//                        || url.contains("www.noqoodypay.com/PayPal/API/PayPalApi/ExecutePayPalCancelPayment"))
//                        || url.toLowerCase().contains((Noqoody_Keys.URL + "Home/GetStatus").toLowerCase())
//                        || url.contains("www.qmobileme.com/iplainstore/api/api/Transaction/PaymentResponse")) {
                if (url.contains(RedirectUrl)) {

                    webview.loadUrl("javascript:HtmlViewer.showHTML" +
                            "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                }

//                if(url.contains("success=True")){
//                    alert_Success("Transaction Success");
//                }else if(url.contains("success=False")){
//                    alert_Success("Transaction Failed");
//                }


//                if (url.equalsIgnoreCase(RedirectUrl))
//                    webview.loadUrl("javascript:HtmlViewer.showHTML" +
//                            "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
            }
        });

        webview.loadUrl(value);
    }


//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (event.getAction() == KeyEvent.ACTION_DOWN) {
//            switch (keyCode) {
//                case KeyEvent.KEYCODE_BACK:
//                    if (webview.canGoBack()) {
//                        webview.goBack();
//                    } else {
//                        finish();
//                    }
//                    return true;
//            }
//
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    private class MyJavaScriptInterface {

        private Context ctx;

        MyJavaScriptInterface(Context ctx) {
            this.ctx = ctx;
        }

        @JavascriptInterface
        public void showHTML(String html) {
            try {
                Document doc = getDomElement(html);
                NodeList nl = doc.getElementsByTagName("html");
                for (int temp = 0; temp < nl.getLength(); temp++) {
                    org.w3c.dom.Node nNode = nl.item(temp);

                    Element eElement = (Element) nNode;

                    html = eElement.getElementsByTagName("body")
                            .item(0).getTextContent();
                    html = html.replace("\\", "");
                    if (html.startsWith("\""))
                        html = html.trim().substring(1, html.length() - 1);

                }
                JSONObject response = new JSONObject(html);

                Intent intent = new Intent();
                if (response.has("TransactionMessage"))
                    ReultMessage(response.getBoolean("IsOk"), response.getString("TransactionMessage"));
                else if (response.has("message"))
                    ReultMessage(response.getBoolean("IsOk"), response.getString("message"));
                else {
                    ReultMessage(response.getBoolean("IsOk"), response.getString("Message"));
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            System.out.println("html body: " + html);
        }
    }

    public Document getDomElement(String xml) {
        Document doc;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {

            DocumentBuilder db = dbf.newDocumentBuilder();

            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            doc = db.parse(is);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        }
        // return DOM
        return doc;
    }

    public final void moveBackPage() {
        finish();

        overridePendingTransition(R.anim.anim_slide_in_right,
                R.anim.anim_slide_out_right);

    }
}
