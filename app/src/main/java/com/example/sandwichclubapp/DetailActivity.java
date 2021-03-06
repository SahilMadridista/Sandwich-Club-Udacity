package com.example.sandwichclubapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sandwichclubapp.model.Sandwich;
import com.example.sandwichclubapp.utils.JsonUtils;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    private TextView alsoKnownAsTextView, placeOfOriginTextView, descriptionTextView, ingredientsTextView, titleTextView;

    private Sandwich sandwich;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView ingredientsIv = findViewById(R.id.image_iv);
        alsoKnownAsTextView = findViewById(R.id.tv_also_known_as);
        placeOfOriginTextView = findViewById(R.id.tv_place_of_origin);
        ingredientsTextView = findViewById(R.id.tv_ingredients);
        descriptionTextView = findViewById(R.id.tv_description);
        titleTextView = findViewById(R.id.titletext);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI();
        Picasso.with(this)
                .load(sandwich.getImage())
                .placeholder(R.drawable.photo_icon)
                .error(R.drawable.error_icon)
                .into(ingredientsIv);

        setTitle("Sandwich Club");
        titleTextView.setText(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI() {

        String alsoKnownAs = sandwich.getAlsoKnownAs().toString();
        alsoKnownAsTextView.setText(alsoKnownAs.substring(1, alsoKnownAs.length() - 1));
        placeOfOriginTextView.setText(sandwich.getPlaceOfOrigin());
        String ingredients = sandwich.getIngredients().toString();
        ingredientsTextView.setText(ingredients.substring(1, ingredients.length() - 1));
        descriptionTextView.setText(sandwich.getDescription());

    }
}
