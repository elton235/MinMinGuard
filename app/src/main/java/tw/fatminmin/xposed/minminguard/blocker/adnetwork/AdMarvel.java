package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import tw.fatminmin.xposed.minminguard.Main;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;
import tw.fatminmin.xposed.minminguard.blocker.Util;
import android.view.View;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.XposedHelpers.ClassNotFoundError;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class AdMarvel extends Blocker {
    
    public final static String banner = "com.admarvel.android.ads.AdMarvelView";
    public final static String bannerPrefix = "com.admarvel.android.ads";

    public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam, final boolean removeAd) {
        try {
            Class<?> adView = XposedHelpers.findClass("com.admarvel.android.ads.AdMarvelView", lpparam.classLoader);
            XposedBridge.hookAllMethods(adView, "requestNewAd", new XC_MethodHook() {
                
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    Util.log(packageName, "Detect AdMarvelView requestNewAd in " + packageName);
                    
                    if(removeAd) {
                        param.setResult(new Object());
                        Main.removeAdView((View) param.thisObject, packageName, true);
                    }
                }
                
            });
            
            Class<?> interads = XposedHelpers.findClass("com.admarvel.android.ads.AdMarvelInterstitialAds", lpparam.classLoader);
            XposedBridge.hookAllMethods(interads, "requestNewInterstitialAd", new XC_MethodHook() {
                
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    Util.log(packageName, "Detect AdMarvelInterstitialAds requestNewInterstitialAd in " + packageName);
                    
                    if(removeAd) {
                        param.setResult(new Object());
                    }
                }
                
            });
            
            Util.log(packageName, packageName + " uses AdMarvelView");
        }
        catch(ClassNotFoundError e) {
            return false;
        }
        return true;
    }
    @Override
    public String getBannerPrefix() {
        return bannerPrefix;
    }

    @Override
    public String getBanner() {
        return banner;
    }
}
