package gr.unipi.it.api;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import gr.unipi.it.models.Recipe;

public class MainFrame extends JFrame {
    private static final long serialVersionUID = 1L;
    private static final String DATA_FILE = "meals_data.json";

    private JTextField txtIngredient; 
    private JButton btnSearch, btnRandom, btnViewFavs, btnViewCooked; 
    private JList<String> listRecipes; 
    private DefaultListModel<String> listModel;
    private MealApiClient client;     
    private JLabel lblImage;             
    private List<Recipe> currentRecipes; 

    private List<Recipe> favoriteRecipes = new ArrayList<>();
    private List<Recipe> cookedRecipes = new ArrayList<>();

    public MainFrame() {
        setTitle("Meal Lab App");
        setSize(1000, 600); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        client = new MealApiClient();
        loadData(); // Φόρτωση αποθηκευμένων δεδομένων κατά την εκκίνηση 

        JPanel topPanel = new JPanel();
        txtIngredient = new JTextField(15);
        btnSearch = new JButton("Αναζήτηση");
        btnRandom = new JButton("Τυχαία Συνταγή");
        btnViewFavs = new JButton("Favorites ❤️");
        btnViewCooked = new JButton("Cooked ✅");
        
        topPanel.add(new JLabel("Υλικό:"));
        topPanel.add(txtIngredient);
        topPanel.add(btnSearch);
        topPanel.add(btnRandom);
        topPanel.add(new JSeparator(SwingConstants.VERTICAL));
        topPanel.add(btnViewFavs);
        topPanel.add(btnViewCooked);

        listModel = new DefaultListModel<>();
        listRecipes = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(listRecipes);

        JPanel eastPanel = new JPanel(new BorderLayout());
        lblImage = new JLabel("Επιλέξτε μια συνταγή", SwingConstants.CENTER);
        lblImage.setPreferredSize(new Dimension(350, 350));
        lblImage.setBorder(BorderFactory.createTitledBorder("Φωτογραφία"));

        JPanel actionButtonsPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        JButton btnAddFav = new JButton("Προσθήκη στα Favorites");
        JButton btnAddCooked = new JButton("Σήμανση ως Cooked");
        actionButtonsPanel.add(btnAddFav);
        actionButtonsPanel.add(btnAddCooked);

        eastPanel.add(lblImage, BorderLayout.CENTER);
        eastPanel.add(actionButtonsPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(eastPanel, BorderLayout.EAST);

        btnSearch.addActionListener(e -> performSearch());
        btnRandom.addActionListener(e -> showRandomRecipe());
        btnViewFavs.addActionListener(e -> showListWindow("Favorite Recipes", favoriteRecipes));
        btnViewCooked.addActionListener(e -> showListWindow("Cooked Recipes", cookedRecipes));

        btnAddFav.addActionListener(e -> {
            addToList(favoriteRecipes, "Favorites ❤️");
            saveData(); // Αποθήκευση μετά την προσθήκη 
        });
        
        btnAddCooked.addActionListener(e -> {
            addToList(cookedRecipes, "Cooked ✅");
            saveData(); // Αποθήκευση μετά την προσθήκη 
        });

        listRecipes.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int index = listRecipes.getSelectedIndex();
                if (index != -1 && currentRecipes != null) {
                    showImage(currentRecipes.get(index).getImageUrl());
                }
            }
        });

        listRecipes.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    int index = listRecipes.locationToIndex(evt.getPoint());
                    if (index != -1 && currentRecipes != null) {
                        showRecipeDetails(currentRecipes.get(index).getId());
                    }
                }
            }
        });
    }

    // Μέθοδος για αποθήκευση σε τοπικό αρχείο JSON 
    private void saveData() {
        try (FileWriter writer = new FileWriter(DATA_FILE)) {
            Gson gson = new Gson();
            Map<String, List<Recipe>> data = new HashMap<>();
            data.put("favorites", favoriteRecipes);
            data.put("cooked", cookedRecipes);
            gson.toJson(data, writer);
        } catch (Exception e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    // Μέθοδος για φόρτωση από τοπικό αρχείο JSON 
    private void loadData() {
        File file = new File(DATA_FILE);
        if (file.exists()) {
            try (FileReader reader = new FileReader(file)) {
                Gson gson = new Gson();
                java.lang.reflect.Type type = new TypeToken<Map<String, List<Recipe>>>(){}.getType();
                Map<String, List<Recipe>> data = gson.fromJson(reader, type);
                if (data != null) {
                    favoriteRecipes = data.getOrDefault("favorites", new ArrayList<>());
                    cookedRecipes = data.getOrDefault("cooked", new ArrayList<>());
                }
            } catch (Exception e) {
                System.err.println("Error loading data: " + e.getMessage());
            }
        }
    }

    private void addToList(List<Recipe> targetList, String listName) {
        int index = listRecipes.getSelectedIndex();
        if (index != -1 && currentRecipes != null) {
            Recipe selected = currentRecipes.get(index);
            if (!targetList.contains(selected)) {
                targetList.add(selected);
                JOptionPane.showMessageDialog(this, "Προστέθηκε στη λίστα " + listName);
            } else {
                JOptionPane.showMessageDialog(this, "Υπάρχει ήδη στη λίστα!");
            }
        }
    }

    private void showListWindow(String title, List<Recipe> recipes) {
        JDialog dialog = new JDialog(this, title, true);
        dialog.setSize(400, 450);
        dialog.setLayout(new BorderLayout());

        DefaultListModel<String> model = new DefaultListModel<>();
        for (Recipe r : recipes) model.addElement(r.getName());
        JList<String> jlist = new JList<>(model);
        
        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 5, 5));
        JButton btnRemove = new JButton("Διαγραφή ❌");
        JButton btnMove = new JButton(title.contains("Favorite") ? "Μαγειρεύτηκε ✅" : "Επιστροφή ❤️");

        btnRemove.addActionListener(e -> {
            int idx = jlist.getSelectedIndex();
            if (idx != -1) {
                recipes.remove(idx);
                model.remove(idx);
                saveData(); // Αποθήκευση μετά τη διαγραφή 
                JOptionPane.showMessageDialog(dialog, "Διαγράφηκε επιτυχώς!");
            }
        });

        btnMove.addActionListener(e -> {
            int idx = jlist.getSelectedIndex();
            if (idx != -1) {
                Recipe r = recipes.remove(idx);
                model.remove(idx);
                if (title.contains("Favorite")) {
                    if (!cookedRecipes.contains(r)) cookedRecipes.add(r);
                } else {
                    if (!favoriteRecipes.contains(r)) favoriteRecipes.add(r);
                }
                saveData(); // Αποθήκευση μετά τη μετακίνηση 
                JOptionPane.showMessageDialog(dialog, "Η συνταγή μετακινήθηκε!");
            }
        });

        btnPanel.add(btnRemove);
        btnPanel.add(btnMove);
        dialog.add(new JScrollPane(jlist), BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void performSearch() {
        String ingredient = txtIngredient.getText().trim();
        if (ingredient.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Δώστε ένα υλικό.");
            return;
        }
        try {
            listModel.clear();
            this.currentRecipes = client.searchByIngredient(ingredient);
            if (currentRecipes != null) {
                for (Recipe r : currentRecipes) listModel.addElement(r.getName());
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Σφάλμα: " + ex.getMessage());
        }
    }

    private void showRandomRecipe() {
        try {
            Recipe random = client.getRandomRecipe();
            if (random != null) {
                listModel.clear();
                listModel.addElement(random.getName());
                this.currentRecipes = java.util.Arrays.asList(random);
                listRecipes.setSelectedIndex(0);
                displayDetailsWindow(random);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Σφάλμα: " + ex.getMessage());
        }
    }

    private void showRecipeDetails(String id) {
        try {
            Recipe fullRecipe = client.getRecipeDetails(id);
            if (fullRecipe != null) displayDetailsWindow(fullRecipe);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Σφάλμα: " + ex.getMessage());
        }
    }

    private void displayDetailsWindow(Recipe r) {
        JTextArea textArea = new JTextArea(15, 45);
        textArea.setText("ΣΥΝΤΑΓΗ: " + r.getName() + "\n\n" +
                         "ΥΛΙΚΑ:\n" + r.getFullIngredients() + "\n\n" +
                         "ΟΔΗΓΙΕΣ:\n" + r.getInstructions());
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(textArea), "Λεπτομέρειες", JOptionPane.PLAIN_MESSAGE);
    }

    private void showImage(String urlString) {
        try {
            ImageIcon icon = new ImageIcon(new java.net.URL(urlString));
            Image img = icon.getImage().getScaledInstance(350, 350, Image.SCALE_SMOOTH);
            lblImage.setIcon(new ImageIcon(img));
            lblImage.setText(""); 
        } catch (Exception ex) {
            lblImage.setIcon(null);
            lblImage.setText("Σφάλμα εικόνας");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}