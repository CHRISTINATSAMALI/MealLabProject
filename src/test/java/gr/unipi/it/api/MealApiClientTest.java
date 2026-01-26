package gr.unipi.it.api;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import java.util.List;
import org.junit.jupiter.api.Test;
import gr.unipi.it.models.Recipe;

/**
 * Κλάση ελέγχου για την ορθότητα των κλήσεων στο Meal API.
 * Υλοποιήθηκε στο πλαίσιο της εργασίας για τη διασφάλιση της ποιότητας των δεδομένων.
 */
public class MealApiClientTest {

    // 1. Έλεγχος ότι η αναζήτηση επιστρέφει αποτελέσματα για υπαρκτά υλικά
    @Test
    public void shouldReturnRecipesForValidIngredient() throws Exception {
        MealApiClient apiHandler = new MealApiClient();
        
        // Δοκιμάζουμε την αναζήτηση με "chicken"
        List<Recipe> searchData = apiHandler.searchByIngredient("chicken");
        
        // Επιβεβαίωση ότι τα δεδομένα ήρθαν σωστά
        assertNotNull(searchData, "Σφάλμα: Η λίστα αποτελεσμάτων είναι null.");
        assertFalse(searchData.isEmpty(), "Σφάλμα: Δεν βρέθηκαν συνταγές για το υλικό 'chicken'.");
        
        // Έλεγχος ότι το πρώτο στοιχείο έχει έγκυρο τίτλο
        String firstRecipeTitle = searchData.get(0).getName();
        assertNotNull(firstRecipeTitle, "Σφάλμα: Η συνταγή δεν περιέχει όνομα.");
    }

    // 2. Έλεγχος συμπεριφοράς του συστήματος σε λανθασμένο υλικό
    @Test
    public void verifyNullResponseForUnknownIngredient() throws Exception {
        MealApiClient apiHandler = new MealApiClient();
        
        // Χρήση τυχαίου κειμένου που δεν αντιστοιχεί σε υλικό
        List<Recipe> emptyResults = apiHandler.searchByIngredient("nonExistentIngredient123");
        
        // Το API πρέπει να επιστρέφει null όταν δεν υπάρχει αντιστοιχία
        assertNull(emptyResults, "Το API θα έπρεπε να επιστρέψει null για άγνωστο υλικό.");
    }

    // 3. Έλεγχος ασφαλείας για λανθασμένο κωδικό συνταγής (ID)
    @Test
    public void checkSystemRobustnessWithInvalidRecipeId() throws Exception {
        MealApiClient apiHandler = new MealApiClient();
        
        // Δοκιμή με ένα προφανώς λανθασμένο ID
        Recipe mockResult = apiHandler.getRecipeDetails("000000");
        
        // Επιβεβαίωση ότι το σύστημα δεν "κρασάρει" αλλά επιστρέφει null
        assertNull(mockResult, "Η αναζήτηση με λάθος ID πρέπει να επιστρέφει null.");
    }
}