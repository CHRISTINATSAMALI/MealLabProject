package gr.unipi.it.api;

import java.util.List;
import gr.unipi.it.models.Recipe;

public class MainTest {
    public static void main(String[] args) {
        MealApiClient client = new MealApiClient();
        try {
            System.out.println("--- Εναρξη Αναζήτησης για pasta ---");
            List<Recipe> recipes = client.searchByIngredient("chicken");
            
            if (recipes != null) {
                for (Recipe r : recipes) {
                    System.out.println("Συνταγή: " + r.getName() + " (ID: " + r.getId() + ")");
                }
                System.out.println("--- Τέλος Λίστας ---");
            } else {
                System.out.println("Δεν βρέθηκαν συνταγές.");
            }
        } catch (Exception e) {
            System.err.println("Πρόβλημα: " + e.getMessage());
        }
    }
}