package br.com.duoli.pricecompare.tools;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.google.android.gms.ads.AdView;

/**
 * Created by liped on 13/03/2017.
 */

public class CoordinatorBehavior extends CoordinatorLayout.Behavior<FloatingActionButton> {

    public CoordinatorBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        return dependency instanceof AdView;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        float translationY = Math.min(0, ViewCompat.getTranslationY(dependency) - dependency.getHeight());
        ViewCompat.setTranslationY(child, translationY);
        Log.d("behavior", "onDependentViewChanged: ");
        return false;
    }

    @Override
    public void onDependentViewRemoved(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        Log.d("behavior", "onDependentViewRemoved: ");
        ViewCompat.animate(child).translationY(0).start();
    }
}
