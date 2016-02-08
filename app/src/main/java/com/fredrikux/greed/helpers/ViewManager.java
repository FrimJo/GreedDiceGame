package com.fredrikux.greed.helpers;

import android.view.View;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

/**
 * This class manages views. It has methods to sett text on {@code TextViews} and
 * disable/enable all {@code Views}. It uses a HashMap for representation.
 */
public class ViewManager {

    private final Map<Integer, View> mViewMap;


    /**
     * Creates and returns a new {@code ViewManager} object.
     */
    public ViewManager(){
        mViewMap = new HashMap<>();
    }

    /**
     * Adds a view to the manager.
     *
     * @param view the {@code View} to add.
     */
    public void addView(final View view){
        mViewMap.put(view.getId(), view);
    }

    /**
     * Enables the view with provided id, signals success or not.
     *
     * @param id the id of the view to manipulate.
     * @return true if the {@code View} exists, false otherwise.
     */
    public boolean enableView(final int id){
        View view = mViewMap.get(id);
        if(view != null){
            view.setEnabled(true);
            return true;
        }
        return false;
    }

    /**
     * Disables the view with provided id, signals success or not.
     *
     * @param id the id of the view to manipulate.
     * @return true if the {@code View} exists, false otherwise.
     */
    public boolean disableView(final int id){
        View view = mViewMap.get(id);
        if(view != null){
            view.setEnabled(false);
            return true;
        }
        return false;
    }

    /**
     * Sets a text for a {@code TextView} contain in this manager.
     *
     * @param id the id of the view to manipulate.
     * @param text the text to sett.
     * @return true if the {@code TextView} exists, false otherwise.
     */
    public boolean setText(final int id, String text) {
        TextView textView = (TextView) mViewMap.get(id);

        if(textView != null){
            textView.setText(text);
            return true;
        }
        return false;
    }
}
