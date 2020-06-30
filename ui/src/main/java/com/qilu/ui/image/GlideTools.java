package com.qilu.ui.image;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.signature.ObjectKey;
import com.qilu.ui.dimen.ScreenUtils;

public class GlideTools {
    public static void showImagewithGlide(Context context, @Nullable String source, ImageView target){
        if(source!=null&&(!source.isEmpty())){
            final RequestOptions GLIDE_OPTIONS = new RequestOptions()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .signature(new ObjectKey(System.currentTimeMillis()))
                    .centerCrop();
            Glide.with(context)
                    .load(source)
                    .apply(GLIDE_OPTIONS)
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            target.setImageDrawable(resource);
                        }
                    });
            target.setScaleType(ImageView.ScaleType.FIT_XY);
            target.setAdjustViewBounds(true);
        }
    }

    public static void showImagewithGlide(Activity context, @Nullable String source, ImageView target){
        if(source!=null&&(!source.isEmpty())){
            final RequestOptions GLIDE_OPTIONS = new RequestOptions()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .signature(new ObjectKey(System.currentTimeMillis()))
                    .centerCrop();
            Glide.with(context)
                    .load(source)
                    .apply(GLIDE_OPTIONS)
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            target.setImageDrawable(resource);
                        }
                    });
            target.setScaleType(ImageView.ScaleType.FIT_XY);
            target.setAdjustViewBounds(true);
        }
    }

    public static void showImagewithGlide(FragmentActivity context, @Nullable String source, ImageView target){
        if(source!=null&&(!source.isEmpty())){
            final RequestOptions GLIDE_OPTIONS = new RequestOptions()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .signature(new ObjectKey(System.currentTimeMillis()))
                    .centerCrop();
            Glide.with(context)
                    .load(source)
                    .apply(GLIDE_OPTIONS)
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            target.setImageDrawable(resource);
                        }
                    });
            target.setScaleType(ImageView.ScaleType.FIT_XY);
            target.setAdjustViewBounds(true);
        }
    }

    public static void showImagewithGlide(Fragment context, @Nullable String source, ImageView target){
        if(source!=null&&(!source.isEmpty())){
            final RequestOptions GLIDE_OPTIONS = new RequestOptions()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .signature(new ObjectKey(System.currentTimeMillis()))
                    .centerCrop();
            Glide.with(context)
                    .load(source)
                    .apply(GLIDE_OPTIONS)
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            target.setImageDrawable(resource);
                        }
                    });
            target.setScaleType(ImageView.ScaleType.FIT_XY);
            target.setAdjustViewBounds(true);
        }
    }

    public static void showImagewithGlide(android.app.Fragment context, @Nullable String source, ImageView target){
        if(source!=null&&(!source.isEmpty())){
            final RequestOptions GLIDE_OPTIONS = new RequestOptions()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .signature(new ObjectKey(System.currentTimeMillis()))
                    .centerCrop();
            Glide.with(context)
                    .load(source)
                    .apply(GLIDE_OPTIONS)
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            target.setImageDrawable(resource);
                        }
                    });
            target.setScaleType(ImageView.ScaleType.FIT_XY);
            target.setAdjustViewBounds(true);
        }
    }

    public static void showImagewithGlide(View context, @Nullable String source, ImageView target){
        if(source!=null&&(!source.isEmpty())){
            final RequestOptions GLIDE_OPTIONS = new RequestOptions()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .signature(new ObjectKey(System.currentTimeMillis()))
                    .centerCrop();
            Glide.with(context)
                    .load(source)
                    .apply(GLIDE_OPTIONS)
                    .into(new SimpleTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            target.setImageDrawable(resource);
                        }
                    });
            target.setScaleType(ImageView.ScaleType.FIT_XY);
            target.setAdjustViewBounds(true);
        }
    }
}
