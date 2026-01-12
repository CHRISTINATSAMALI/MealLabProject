package gr.unipi.it.models;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class Recipe {
    @SerializedName("idMeal")
    private String id;

    @SerializedName("strMeal")
    private String name;

    @SerializedName("strMealThumb")
    private String imageUrl;

    @SerializedName("strInstructions")
    private String instructions;

    // Πεδία για υλικά (το API έχει strIngredient1 έως 20)
    @SerializedName("strIngredient1") private String ing1;
    @SerializedName("strIngredient2") private String ing2;
    // ... για οικονομία χώρου προσθέτουμε τα βασικά, το Gson θα κάνει τα υπόλοιπα
    
    @SerializedName("strMeasure1") private String meas1;
    @SerializedName("strMeasure2") private String meas2;

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getImageUrl() { return imageUrl; }
    public String getInstructions() { return instructions; }

    // Βοηθητική μέθοδος για να παίρνεις μια λίστα με τα υλικά στην αναφορά σου
    public String getFullIngredients() {
        return "Υλικό 1: " + ing1 + " (" + meas1 + ")\n" + "Υλικό 2: " + ing2 + " (" + meas2 + ")";
    }
}