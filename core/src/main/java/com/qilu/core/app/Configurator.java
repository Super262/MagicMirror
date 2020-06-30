package com.qilu.core.app;

import android.os.Handler;

import com.blankj.utilcode.util.Utils;
import com.joanzapata.iconify.IconFontDescriptor;
import com.joanzapata.iconify.Iconify;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Interceptor;

public class Configurator {

    private static final HashMap<Object,Object> QILU_CONFIGS= new HashMap<>();
    private static final Handler HANDLER = new Handler();
    private static final ArrayList<IconFontDescriptor> ICONS = new ArrayList<>();
    private static final ArrayList<Interceptor> INTERCEPTORS = new ArrayList<>();

    private Configurator(){
        QILU_CONFIGS.put(ConfigKeys.CONFIG_READY, false);
        QILU_CONFIGS.put(ConfigKeys.HANDLER, HANDLER);
    }
    //利用静态内部类进行初始化
    private static class Holder {
        private static final Configurator INSTANCE = new Configurator();
    }

    static Configurator getInstance() {
        return Holder.INSTANCE;
    }

    final HashMap<Object, Object> getQiluConfigs() {
        return QILU_CONFIGS;
    }

    public final void configure() {
        initIcons();
        QILU_CONFIGS.put(ConfigKeys.CONFIG_READY, true);
        Utils.init(Qilu.getApplicationContext());
    }

    private void initIcons() {
        if (ICONS.size() > 0) {
            final Iconify.IconifyInitializer initializer = Iconify.with(ICONS.get(0));
            for (int i = 1; i < ICONS.size(); i++) {
                initializer.with(ICONS.get(i));
            }
        }
    }

    public final Configurator withIcon(IconFontDescriptor descriptor) {
        ICONS.add(descriptor);
        return this;
    }
    public final Configurator withApiHost(String host) {
        QILU_CONFIGS.put(ConfigKeys.API_HOST, host);
        return this;
    }
    public final Configurator withLoaderDelayed(long delayed) {
        QILU_CONFIGS.put(ConfigKeys.LOADER_DELAYED, delayed);
        return this;
    }
    public final Configurator withInterceptor(Interceptor interceptor) {
        INTERCEPTORS.add(interceptor);
        QILU_CONFIGS.put(ConfigKeys.INTERCEPTOR, INTERCEPTORS);
        return this;
    }

    private void checkConfiguration() {
        final boolean isReady = (boolean) QILU_CONFIGS.get(ConfigKeys.CONFIG_READY);
        if (!isReady) {
            throw new RuntimeException("Configuration is not ready,call configure");
        }
    }
    @SuppressWarnings("unchecked")
    final <T> T getConfiguration(Object key) {
        checkConfiguration();
        final Object value = QILU_CONFIGS.get(key);
        if (value == null) {
            throw new NullPointerException(key.toString() + " IS NULL");
        }
        return (T) QILU_CONFIGS.get(key);
    }
}
