package gr.unipi.it.api;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;

import gr.unipi.it.models.Recipe;

public class MealApiClientTest {

    @Test
    public void testSearchByIngredient() throws Exception {
        MealApiClient client = new MealApiClient();
        // Ελέγχουμε αν το API επιστρέφει όντως λίστα για το chicken
        List<Recipe> results = client.searchByIngredient("chicken");
        
        assertNotNull(results, "Το API δεν πρέπει να επιστρέφει null");
        assertFalse(results.isEmpty(), "Η λίστα συνταγών δεν πρέπει να είναι άδεια");
        
        // Έλεγχος αν η πρώτη συνταγή έχει όνομα (όπως είδαμε στην κονσόλα)
        assertNotNull(results.get(0).getName(), "Η συνταγή πρέπει να έχει όνομα");
    }
}