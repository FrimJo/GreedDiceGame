package com.fredrikux.greed.views;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.fredrikux.greed.R;
import com.fredrikux.greed.widgets.ExpandableEditTextLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This is the lan on activity. Her the user enters the players to attend the
 * game. This activity uses a special version of the {@code LinearLayout}
 * namly the {@code ExpandableEditTextLayout}, this layout makes it possible to
 * add any number of players to the game.
 */
public class LauncherActivity extends AppCompatActivity {

    private ExpandableEditTextLayout mExpandableEditTextLayout;
    private Bundle mSavedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        mSavedInstanceState = savedInstanceState;
        mExpandableEditTextLayout = setupExpandableFieldLayout();

        setUpContinueButton();
    }

    private ExpandableEditTextLayout setupExpandableFieldLayout() {

        ExpandableEditTextLayout expandableEditTextLayout
                = (ExpandableEditTextLayout) findViewById(R.id.name_container);

        // If the activity has ben saved
        if(mSavedInstanceState != null){

            // Fill it up with the saved values
            String[] players = mSavedInstanceState.getStringArray("players");
            expandableEditTextLayout.recreateLayout(players);

        }else{

            // Add default two text fields
            expandableEditTextLayout.addEditText();
            expandableEditTextLayout.addEditText();
        }

        return expandableEditTextLayout;
    }

    private Button setUpContinueButton() {

        Button continueButton = (Button) findViewById(R.id.continue_button);

        continueButton.setOnClickListener(createButtonListener());

        return continueButton;
    }

    @NonNull
    private View.OnClickListener createButtonListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get the content of the text fields
                String[] values = mExpandableEditTextLayout.getValues();

                // Create a array list using the content
                List<String> valueList = new ArrayList<>(Arrays.asList(values));

                // Remove all empty occurrences
                valueList.removeAll(Arrays.asList("", null));

                // Translate it back in to an array
                String[] players = valueList.toArray(new String[valueList.size()]);

                if(players.length > 0){

                    boolean valid = isFormValid(players);

                    if(valid){
                        loadBoardActivity(players);
                    }else{
                        showToast("Name can only contain A-Z and a-z");
                    }
                }else{
                    showToast("Please enter name");
                }
            }
        };
    }

    private void showToast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }

    private void loadBoardActivity(String[] players) {

        // Get the intent
        Intent intent = new Intent(LauncherActivity.this,
            BoardActivity.class);

        // Add the players choosen
        intent.putExtra("players", players);

        // Start the activity
        startActivity(intent);
    }

    private boolean isFormValid(String[] players) {
        boolean validate = true;

        // For each entry in the string
        for(String s : players) {

            // If it doesn't match the reg. exp. the array isn't valid
            if (!s.matches("[a-zA-Z]+")) {
                validate = false;
                break;
            }
        }
        return validate;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        String[] players = mExpandableEditTextLayout.getValues();
        outState.putStringArray("players", players);

        super.onSaveInstanceState(outState);
    }
}
