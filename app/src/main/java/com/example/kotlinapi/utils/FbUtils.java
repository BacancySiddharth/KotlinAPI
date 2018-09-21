package com.example.kotlinapi.utils;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.kotlinapi.R;
import com.example.kotlinapi.activity.MainActivity;
import com.facebook.FacebookSdk;
import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdError;
import com.facebook.ads.AdIconView;
import com.facebook.ads.AdSettings;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.NativeAdListener;
import com.facebook.ads.NativeBannerAd;

import java.util.ArrayList;
import java.util.List;

public class FbUtils {

    private InterstitialAd mInterstitialAd;
    private static final int MAX_NUMBER_OF_RETRIES = 3;
    private static final String TAG = "FACEBOOK";
    private boolean shouldLoadAd = true;
    private int retryCount = 0;
    private Context mContext;
    private NativeBannerAd nativeBannerAd;

    public FbUtils(Context mContext) {
        this.mContext = mContext;
    }

    public void initInterstitial() {
        AdSettings.addTestDevice("328404cebf50ec1fdb05795c0007a8a7");
        FacebookSdk.setIsDebugEnabled(true);
        mInterstitialAd = new InterstitialAd(mContext, "288150368579424_288150568579404");
        mInterstitialAd.setAdListener(new InterstitialAdListener() {

            @Override
            public void onInterstitialDisplayed(Ad ad) {
                Log.e(TAG, "onInterstitialDisplayed:" + ad.getPlacementId());
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                if (shouldLoadAd) {
                /* Change shouldLoadAd value to false,
                    or a new interstitial ad will show immediately
                    when previous interstitial ad gets dismissed. */
                    shouldLoadAd = false;
                    mInterstitialAd.loadAd();
                }

                Log.e(TAG, "onInterstitialDismissed:" + ad.getPlacementId());
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                // Stop retrying when it reaches to MAX_NUMBER_OF_RETRIES
                if (retryCount < MAX_NUMBER_OF_RETRIES) {
                    retryCount += 1;
                    mInterstitialAd.loadAd();
                    shouldLoadAd = true;
                }
                Log.e(TAG, "onError:" + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                mInterstitialAd.show();
                Log.e(TAG, "onAdLoaded:" + ad.getPlacementId());
            }

            @Override
            public void onAdClicked(Ad ad) {
                Log.e(TAG, "onAdClicked:" + ad.getPlacementId());
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                Log.e(TAG, "onLoggingImpression:" + ad.getPlacementId());
            }

        });
        mInterstitialAd.loadAd();
    }

    public void initBannerLoad(final ViewGroup vg) {
        nativeBannerAd = new NativeBannerAd(mContext, "288150368579424_288201408574320");
        nativeBannerAd.setAdListener(new NativeAdListener() {

            @Override
            public void onMediaDownloaded(Ad ad) {

            }

            @Override
            public void onError(Ad ad, AdError adError) {

            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Race condition, load() called again before last ad was displayed
                if (nativeBannerAd == null || nativeBannerAd != ad) {
                    return;
                }
                // Inflate Native Banner Ad into Container
                inflateAd(nativeBannerAd, vg);
            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }

        });
        // load the ad
        nativeBannerAd.loadAd();
    }

    private void inflateAd(NativeBannerAd nativeBannerAd, ViewGroup vg) {
        // Unregister last ad

        // Add the Ad view into the ad container.
        LayoutInflater inflater = LayoutInflater.from(mContext);
        // Inflate the Ad view.  The layout referenced is the one you created in the last step.
        LinearLayout adView = (LinearLayout) inflater.inflate(R.layout.native_banner, vg, false);
        vg.addView(adView);

        // Add the AdChoices icon
        RelativeLayout adChoicesContainer = adView.findViewById(R.id.ad_choices_container);
        AdChoicesView adChoicesView = new AdChoicesView(mContext, nativeBannerAd, true);
        adChoicesContainer.addView(adChoicesView, 0);

        // Create native UI using the ad metadata.
        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
        AdIconView nativeAdIconView = adView.findViewById(R.id.native_icon_view);
        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdCallToAction.setText(nativeBannerAd.getAdCallToAction());
        nativeAdCallToAction.setVisibility(
                nativeBannerAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdTitle.setText(nativeBannerAd.getAdvertiserName());
        nativeAdSocialContext.setText(nativeBannerAd.getAdSocialContext());
        sponsoredLabel.setText(nativeBannerAd.getSponsoredTranslation());

        // Register the Title and CTA button to listen for clicks.
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);
        nativeBannerAd.registerViewForInteraction(adView, nativeAdIconView, clickableViews);
    }

}
