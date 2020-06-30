package com.qilu.app;

import android.app.Application;

import com.joanzapata.iconify.fonts.EntypoModule;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.joanzapata.iconify.fonts.IoniconsModule;
import com.joanzapata.iconify.fonts.MaterialCommunityModule;
import com.joanzapata.iconify.fonts.MaterialModule;
import com.joanzapata.iconify.fonts.MeteoconsModule;
import com.joanzapata.iconify.fonts.SimpleLineIconsModule;
import com.joanzapata.iconify.fonts.TypiconsModule;
import com.joanzapata.iconify.fonts.WeathericonsModule;
import com.qilu.core.app.Qilu;
import com.qilu.core.util.storage.QiluPreference;


public class MainApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Qilu.init(this)
                .withIcon(new FontAwesomeModule())
                .withIcon(new EntypoModule())
                .withIcon(new TypiconsModule())
                .withIcon(new MaterialModule())
                .withIcon(new MaterialCommunityModule())
                .withIcon(new MeteoconsModule())
                .withIcon(new WeathericonsModule())
                .withIcon(new SimpleLineIconsModule())
                .withIcon(new IoniconsModule())
                .withApiHost("http://106.13.96.60:8888/")
                .withLoaderDelayed(3000)
                .configure();
    }
}

