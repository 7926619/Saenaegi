package com.saenaegi.lfree;

import java.lang.reflect.Field;
import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

public class FontApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        setDefaultFont(this, "DEFAULT", "KoPubDotumMedium.ttf");
        setDefaultFont(this, "SANS_SERIF", "KoPubDotumMedium.ttf");
        setDefaultFont(this, "SERIF", "KoPubDotumMedium.ttf");
    }

    public static void setDefaultFont(Context ctx,
                                      String staticTypefaceFieldName, String fontAssetName) {
        final Typeface regular = Typeface.createFromAsset(ctx.getAssets(),
                fontAssetName);
        replaceFont(staticTypefaceFieldName, regular);
    }

    protected static void replaceFont(String staticTypefaceFieldName,
                                      final Typeface newTypeface) {
        try {
            final Field StaticField = Typeface.class
                    .getDeclaredField(staticTypefaceFieldName);
            StaticField.setAccessible(true);
            StaticField.set(null, newTypeface);
        } catch (NoSuchFieldException e) {
            android.util.Log.d(null, "==================> " + e.toString());
        } catch (IllegalAccessException e) {
            android.util.Log.d(null, "==================> " + e.toString());
        }
    }
}
