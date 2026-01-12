package gr.unipi.it.api;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import gr.unipi.it.models.Recipe;

public class MealApiClient {
    private static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";

    // Λειτουργία 1: Αναζήτηση με βάση το υλικό
    public List<Recipe> searchByIngredient(String ingredient) throws Exception {
        // Χρησιμοποιούμε trim() για να αφαιρέσουμε τυχόν κενά που μπερδεύουν το API
        String urlString = BASE_URL + "filter.php?i=" + ingredient.trim();
        return makeRequest(urlString);
    }

    // Λειτουργία 2: Λήψη Λεπτομερειών βάσει ID
    public Recipe getRecipeDetails(String id) throws Exception {
        String urlString = BASE_URL + "lookup.php?i=" + id;
        List<Recipe> meals = makeRequest(urlString);
        return (meals != null && !meals.isEmpty()) ? meals.get(0) : null;
    }

    // Λειτουργία 3: Πρόταση για τυχαία συνταγή
    public Recipe getRandomRecipe() throws Exception {
        String urlString = BASE_URL + "random.php";
        List<Recipe> meals = makeRequest(urlString);
        return (meals != null && !meals.isEmpty()) ? meals.get(0) : null;
    }

    // Βοηθητική μέθοδος για την επικοινωνία και την αποσειριοποίηση JSON
    private List<Recipe> makeRequest(String urlString) throws Exception {
        URL url = URI.create(urlString).toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        try (InputStreamReader reader = new InputStreamReader(conn.getInputStream())) {
            JsonObject jsonResponse = new Gson().fromJson(reader, JsonObject.class);
            if (jsonResponse == null || jsonResponse.get("meals") == null || jsonResponse.get("meals").isJsonNull()) {
                return null;
            }
            return new Gson().fromJson(jsonResponse.get("meals"), 
                    new TypeToken<List<Recipe>>(){}.getType());
        }
    }
}