package com.fredrikux.greed.widgets;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * This Widget creates a Linear Layout with methods to fill it with text fields
 * ({@code EditText}). When a user starts typing in the bottom most field a new
 * field is created beneath it.
 */
public class ExpandableEditTextLayout extends LinearLayout{

    private final Context mContext;
    private EditText mLastEditText;

    public ExpandableEditTextLayout(Context context) {
        this(context, null);
    }

    public ExpandableEditTextLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExpandableEditTextLayout(Context context, AttributeSet defStyleAttr, int defStyle){
        super(context, defStyleAttr, defStyle);
        mContext = context;
    }

    /**
     * Adds a default {@code EditText} to the bottom om the list.
     */
    public void addEditText(){
        addEditText("");
    }

    @NonNull
    private void addEditText(String text) {
        final EditText editText = new EditText(mContext);
        editText.setHint("Player name");
        editText.setSingleLine(true);
        editText.setText(text);

        editText.addTextChangedListener(getWatcher(editText));

        editText.setOnFocusChangeListener(getOnFocusListener(editText));

        addView(editText);

        mLastEditText = editText;
    }

    @NonNull
    private OnFocusChangeListener getOnFocusListener(final EditText editText) {
        return new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String hint = "";
                if (!hasFocus) {
                    hint = "Add another player";
                }

                editText.setHint(hint);
            }
        };
    }

    @NonNull
    private TextWatcher getWatcher(final EditText editText) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!hasNext(editText)) {
                    addEditText();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };
    }


    /**
     * Fetches all the values in the {@code EditText} contained in this list.
     *
     * @return a {@code String} array with all the {@code EditText} values.
     */
    @NonNull
    public String[] getValues(){
        int size = getChildCount();
        String[] textArray = new String[size];

        for(int i = 0; i < size; i++){
            EditText editText = (EditText) getChildAt(i);
            textArray[i] = editText.getText().toString();
        }

        return textArray;
    }

    /**
     * Recreates this expandable {@code EditText} layout from provided {@code
     * String}
     * array. If the length of the array is bigger then the number of
     * {@code EditText} in the list, then new {@code EditText} will be created.
     *
     * @param values the values to use for recreation.
     */
    @NonNull
    public void recreateLayout(String[] values){
        for(int i = 0; i < values.length; i++){
            EditText editText = (EditText) getChildAt(i);
            if(editText == null){
                addEditText(values[i]);
            }else{
                editText.setText(values[i]);
            }
        }
    }

    @NonNull
    private boolean hasNext(EditText editText) {
        return editText != mLastEditText;
    }

}
