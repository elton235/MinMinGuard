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

public class OpenX extends Blocker {
    
    public final static String banner = "com.openx.ad.mobile.sdk.interfaces.OXMAdBannerView";
    public final static String bannerPrefix = "com.openx.ad.mobile.sdk.interfaces";

	@Override
	public String getBannerPrefix() {
		return bannerPrefix;
	}

	@Override
	public String getBanner() {
		return banner;
	}
	public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam, final boolean removeAd) {
		try {
			Class<?> adView = XposedHelpers.findClass("com.openx.ad.mobile.sdk.interfaces.OXMAdBannerView", lpparam.classLoader);
			XposedBridge.hookAllMethods(adView, "loadAd", new XC_MethodHook() {
				
				@Override
				protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
					Util.log(packageName, "Detect OXMAdBannerView loadAd in " + packageName);
					
					if(removeAd) {
						param.setResult(new Object());
						Main.removeAdView((View) param.thisObject, packageName, true);
					}
				}
				
			});
			Util.log(packageName, packageName + " uses OpenX");
		}
		catch(ClassNotFoundError e) {
			return false;
		}
		return true;
	}
}
