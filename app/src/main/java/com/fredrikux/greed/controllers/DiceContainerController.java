package com.fredrikux.greed.controllers;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.fredrikux.greed.R;
import com.fredrikux.greed.models.DiceContainerModel;

/**
 * This is the controller for the container containing all deices. This
 * controller is used as a {@code BaseAdapter} for the {@code GridView} in
 * {@code BoardActivity} to fill it with correct dices images representing
 * the values provided by the {@code DiceContainerModel}.
 */
public class DiceContainerController extends BaseAdapter {

    private final int[] mWhiteResource = {
            R.drawable.white1_t,
            R.drawable.white2_t,
            R.drawable.white3_t,
            R.drawable.white4_t,
            R.drawable.white5_t,
            R.drawable.white6_t
    };

    private final int[] mGreyResource = {
            R.drawable.grey1_t,
            R.drawable.grey2_t,
            R.drawable.grey3_t,
            R.drawable.grey4_t,
            R.drawable.grey5_t,
            R.drawable.grey6_t
    };

    private final DiceContainerModel.Listener mUpdateListener
            = new DiceContainerModel.Listener() {
        @Override
        public void onDataUpdate() {
            notifyDataSetChanged();
        }
    };

    private final DiceContainerModel mModel;
    private final ImageView[] mDiceImages;

    /**
     * Creates and returns a {@code DiceContainerController} object with
     * provided {@code DiceContainerModel} as base.
     *
     * @param context the context to use together with the dice images.
     * @param model the model fom which the values for the dices are fetched.
     */
    public DiceContainerController(final Context context, final DiceContainerModel model){
        mModel = model;
        mModel.setListener(mUpdateListener);
        final int size = model.getNrDices();
        mDiceImages = new ImageView[size];
        setUpImages(context);
    }

    private void setUpImages(final Context _context) {

        for(int i = 0; i < mDiceImages.length; i++){
            ImageView view = new ImageView(_context);
            view.setLayoutParams(new GridView.LayoutParams(185, 185));
            view.setScaleType(ImageView.ScaleType.FIT_CENTER);
            mDiceImages[i] = view;
        }
    }

    /**
     * Returns the number of dices in the {@code DiceContainerModel}.
     * @return number of dices.
     */
    @Override
    public int getCount() {
        return mModel.size();
    }

    /**
     * Fetches a specific dice at provided {@code position}.
     *
     * @param position the dice position, zero-based.
     * @return the found dice.
     */
    @Override
    public Object getItem(int position) {

        int value = mModel.getValue(position);
        boolean locked = mModel.isLocked(position);
        int resource = locked ? mGreyResource[value - 1] : mWhiteResource[value - 1];

        // Fetch the predefined image base on position and set the resource.
        ImageView image = mDiceImages[position];
        image.setImageResource(resource);

        return image;
    }

    /**
     * Returns the dice id, which is the same as it's position.
     *
     * @param position the dice position, zero-based.
     * @return the id which is the same as provided position.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Returns a {@code View} element representing a value and locked
     * status on a dice.
     *
     * @param position the position of the dice.
     * @param convertView current {@code View}.
     * @param parent the paren {@code ViewGroup}.
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Fetch the predefined image base on position
        ImageView image = mDiceImages[position];

        // Get the value of that dice
        int value = mModel.getValue(position);

        // Depending on if the dice is locked or not, get the right resource
        int resource;
        if (mModel.isLocked(position)) {
            resource = mGreyResource[value - 1];
        } else {
            resource = mWhiteResource[value - 1];
        }

        // Set the resource
        image.setImageResource(resource);
        return image;
    }
}
