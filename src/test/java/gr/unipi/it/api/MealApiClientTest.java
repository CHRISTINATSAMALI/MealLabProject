package gr.unipi.it.api;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import java.util.List;
import org.junit.jupiter.api.Test;
import gr.unipi.it.models.Recipe;

public class MealApiClientTest {

    // 1. Έλεγχος κανονικής λειτουργίας (Αυτό που είχες ήδη)
    @Test
    public void testSearchByIngredient() throws Exception {
        MealApiClient client = new MealApiClient();
        // Ελέγχουμε αν το API επιστρέφει όντως λίστα για το chicken
        List<Recipe> results = client.searchByIngredient("chicken");
        
        assertNotNull(results, "Το API δεν πρέπει να επιστρέφει null");
        assertFalse(results.isEmpty(), "Η λίστα συνταγών δεν πρέπει να είναι άδεια");
        assertNotNull(results.get(0).getName(), "Η συνταγή πρέπει να έχει όνομα");
    }

    // 2. Έλεγχος για υλικό που δεν υπάρχει (Negative Test)
    @Test
    public void testSearchWithInvalidIngredient() throws Exception {
        MealApiClient client = new MealApiClient();
        // Δίνουμε ένα υλικό που δεν υπάρχει (τυχαία γράμματα)
        List<Recipe> results = client.searchByIngredient("xyz123abc");
        
        // Το API επιστρέφει null αν δεν βρει τίποτα, οπότε το ελέγχουμε
        assertNull(results, "Για μη έγκυρο υλικό, το API πρέπει να επιστρέφει null");
    }

    // 3. Έλεγχος για ID που δεν υπάρχει (Negative Test)
    @Test
    public void testGetRecipeDetailsInvalidId() throws Exception {
        MealApiClient client = new MealApiClient();
        // Δίνουμε ένα ID που είναι απίθανο να υπάρχει
        Recipe result = client.getRecipeDetails("999999");
        
        assertNull(result, "Για μη υπαρκτό ID, η συνταγή πρέπει να είναι null");
    }
}