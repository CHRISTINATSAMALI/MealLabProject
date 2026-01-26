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
    // Η κεντρική διεύθυνση του API για τις συνταγές
    private static final String API_ENDPOINT = "https://www.themealdb.com/api/json/v1/1/";

    // Μέθοδος για αναζήτηση συνταγών με βάση το κύριο υλικό
    public List<Recipe> searchByIngredient(String mainIngredient) throws Exception {
        // Καθαρίζουμε το κείμενο από κενά στην αρχή και στο τέλος
        String cleanIngredient = mainIngredient.trim();
        String finalUrl = API_ENDPOINT + "filter.php?i=" + cleanIngredient;
        return executeApiCall(finalUrl);
    }

    // Μέθοδος για ανάκτηση όλων των λεπτομερειών μιας συνταγής μέσω του ID της
    public Recipe getRecipeDetails(String recipeId) throws Exception {
        String finalUrl = API_ENDPOINT + "lookup.php?i=" + recipeId;
        List<Recipe> recipeList = executeApiCall(finalUrl);
        // Επιστρέφουμε την πρώτη συνταγή αν υπάρχει, αλλιώς null
        return (recipeList != null && !recipeList.isEmpty()) ? recipeList.get(0) : null;
    }

    // Μέθοδος που προτείνει μια τυχαία συνταγή στον χρήστη
    public Recipe getRandomRecipe() throws Exception {
        String finalUrl = API_ENDPOINT + "random.php";
        List<Recipe> recipeList = executeApiCall(finalUrl);
        return (recipeList != null && !recipeList.isEmpty()) ? recipeList.get(0) : null;
    }

    // Βοηθητική μέθοδος που κάνει την πραγματική κλήση στο ίντερνετ
    private List<Recipe> executeApiCall(String targetUrl) throws Exception {
        URL url = URI.create(targetUrl).toURL();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        
        // Ορίζουμε Timeouts (5 δευτερόλεπτα) για να μην "παγώνει" η εφαρμογή
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        connection.setRequestMethod("GET");

        // Χρησιμοποιούμε try-with-resources για αυτόματο κλείσιμο του reader
        try (InputStreamReader apiReader = new InputStreamReader(connection.getInputStream())) {
            Gson parser = new Gson();
            JsonObject rootObject = parser.fromJson(apiReader, JsonObject.class);
            
            // Έλεγχος αν η απάντηση είναι κενή ή αν δεν υπάρχουν γεύματα
            if (rootObject == null || rootObject.get("meals") == null || rootObject.get("meals").isJsonNull()) {
                return null;
            }
            
            // Μετατροπή του JSON array "meals" σε λίστα αντικειμένων Recipe
            return parser.fromJson(rootObject.get("meals"), 
                    new TypeToken<List<Recipe>>(){}.getType());
        }
    }
}