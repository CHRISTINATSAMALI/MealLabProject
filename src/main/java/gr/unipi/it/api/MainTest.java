package gr.unipi.it.api;

import java.util.List;
import gr.unipi.it.models.Recipe;

/**
 * Κύρια κλάση για τη χειροκίνητη δοκιμή του Meal API Client.
 * Χρησιμοποιείται για να βεβαιωθούμε ότι η σύνδεση με το API λειτουργεί σωστά.
 */
public class MainTest {
    public static void main(String[] args) {
        // Δημιουργία αντικειμένου για την κλήση του API
        MealApiClient recipeManager = new MealApiClient();
        
        try {
            String ingredientToSearch = "chicken";
            System.out.println(">>> Εκκίνηση διαδικασίας ανάκτησης δεδομένων για: " + ingredientToSearch);
            
            // Κλήση της μεθόδου αναζήτησης
            List<Recipe> fetchedRecipes = recipeManager.searchByIngredient(ingredientToSearch);
            
            if (fetchedRecipes != null && !fetchedRecipes.isEmpty()) {
                System.out.println(">>> Βρέθηκαν συνολικά " + fetchedRecipes.size() + " συνταγές.");
                System.out.println("--------------------------------------------------");
                
                // Εμφάνιση των αποτελεσμάτων με πιο καθαρή μορφή
                for (Recipe currentRecipe : fetchedRecipes) {
                    System.out.println("ID: [" + currentRecipe.getId() + "] | Όνομα: " + currentRecipe.getName());
                }
                
                System.out.println("--------------------------------------------------");
                System.out.println(">>> Η διαδικασία ολοκληρώθηκε επιτυχώς.");
            } else {
                System.out.println("Ενημέρωση: Δεν επιστράφηκαν αποτελέσματα από το API για το υλικό: " + ingredientToSearch);
            }
            
        } catch (Exception error) {
            // Πιο αναλυτικό μήνυμα σφάλματος
            System.err.println("Σφάλμα κατά την επικοινωνία με το API: " + error.getMessage());
            error.printStackTrace(); // Βοηθάει στο debugging αν κάτι πάει στραβά
        }
    }
}