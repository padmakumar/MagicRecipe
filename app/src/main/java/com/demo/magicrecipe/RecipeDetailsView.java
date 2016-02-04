package com.demo.magicrecipe;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by DELL on 03-02-2016.
 */
public class RecipeDetailsView extends Activity {
    private WebView webView;
    String mRecipeLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_view);

        Intent intent = getIntent();
        mRecipeLink = intent.getStringExtra(Constant.RECIPE_URL);

        webView = (WebView) findViewById(R.id.webview);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setBuiltInZoomControls(true);
        settings.setSupportZoom(false);

        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setInitialScale(40);
        webView.setScrollbarFadingEnabled(true);

        webView.loadUrl(mRecipeLink);

        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                return true;
            }

            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                final AlertDialog alertDialog = new AlertDialog.Builder(
                        RecipeDetailsView.this).create();
                alertDialog.setTitle("Error");
                alertDialog.setMessage(description);
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                return;
                            }
                        });
                alertDialog.show();
            }

        });
    }

}
