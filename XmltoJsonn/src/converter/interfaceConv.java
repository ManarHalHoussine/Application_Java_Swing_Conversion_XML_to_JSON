
package converter;
import org.pushingpixels.substance.api.skin.SubstanceBusinessBlackSteelLookAndFeel;
import org.json.JSONObject;
import org.json.XML;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;

public class interfaceConv extends JFrame {

    private JTextArea inputArea;
    private JTextArea outputArea;

    public interfaceConv() {
        super("XML/JSON Converter");

        // Initialisation Substance Look and Feel
        try {
            UIManager.setLookAndFeel(new SubstanceBusinessBlackSteelLookAndFeel());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        // Personnalisation des composants
        JButton openFileButton = new JButton("Open File");
        openFileButton.setBackground(new Color(65, 105, 225)); // Couleur bleu royal
        openFileButton.setForeground(Color.black);
        openFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openFileChooser();
            }
        });

        JButton convertButton = new JButton("Convert");
        convertButton.setBackground(new Color(0, 128, 0)); // Couleur vert foncé
        convertButton.setForeground(Color.black);
        convertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                convertInput();
            }
        });

        inputArea = new JTextArea(10, 40);
        outputArea = new JTextArea(10, 40);
        outputArea.setEditable(false);

        // Création du panneau principal avec BoxLayout
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // Section Nord
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
        northPanel.add(new JLabel("Enter XML or JSON code:"));
        northPanel.add(new JScrollPane(inputArea));
        panel.add(northPanel);

        // Section Centre
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.add(convertButton);
        centerPanel.add(openFileButton);
        panel.add(centerPanel);

        // Section Sud
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BoxLayout(southPanel, BoxLayout.Y_AXIS));
        southPanel.add(new JLabel("Output:"));
        southPanel.add(new JScrollPane(outputArea));
        panel.add(southPanel);

        // Ajout du panneau au contenu de la fenêtre
        getContentPane().add(panel);

        // Configuration de la fenêtre
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void convertInput() {
        String input = inputArea.getText().trim();

        if (!input.isEmpty()) {
            try {
                if (isXML(input)) {//verifier si le text entree ressemble à de xmlen appel de la méthode isXML(input)
                    outputArea.setText(convertXmlToJson(input));// cette méthode est responsable sur la conversion de code de xml to json
                } else if (isJson(input)) {
                    outputArea.setText(convertJsonToXml(input));// cette méthode est responsable sur la conversion de code de json to xml
                } else {
                    outputArea.setText("Il y'a un erreur entrer le code XML ou JSON ");
                }
            } catch (Exception ex) {
                outputArea.setText("Conversion error: " + ex.getMessage());
            }
        } else {
            outputArea.setText("la zone text est vide ");
        }
    }

    public boolean isXML(String input) {
        return input.trim().startsWith("<");//d'apres l'elimination des espaces au debut et au fin on verifier si le debut de code est <
    }

    public boolean isJson(String input) {
        return input.trim().startsWith("{");
    }

    public String convertXmlToJson(String xmlInput) {
        JSONObject jsonObject = XML.toJSONObject(xmlInput);
        return jsonObject.toString(4);//'toString(4) pour obtenir une représentation formatée du JSON avec une indentation de 4 espaces.
    }

    public String convertJsonToXml(String jsonInput) {
        JSONObject json = new JSONObject(jsonInput);
        return XML.toString(json);
    }

    private void openFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
       

        FileNameExtensionFilter filter = new FileNameExtensionFilter("XML and JSON Files", "xml", "json");
//Crée un filtre de type FileNameExtensionFilter pour restreindre 
//le type de fichiers qui peuvent être sélectionnés dans la boîte de dialogue.    
        fileChooser.setFileFilter(filter);
//Applique le filtre au JFileChooser pour restreindre la sélection aux fichiers XML et JSON.
        int result = fileChooser.showOpenDialog(this);
//Affiche la boîte de dialogue de sélection de fichier et attend que l'utilisateur fasse son choix.
//this est passé en tant que composant parent pour centrer la boîte de dialogue par rapport à la fenêtre principale.
        if (result == JFileChooser.APPROVE_OPTION) {
//Vérifie si l'utilisateur a choisi un fichier en cliquant sur le bouton "Open" dans la boîte de dialogue.
            String selectedFilePath = fileChooser.getSelectedFile().getAbsolutePath();
//Obtient le chemin absolu du fichier sélectionné à partir de la boîte de dialogue.
            loadFileContent(selectedFilePath);
//Appelle la méthode loadFileContent avec le chemin absolu du fichier sélectionné pour 
//charger le contenu du fichier dans la zone de texte.
        }
    }

    private void loadFileContent(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
  //pour lire le contenu du fichier spécifié (filePath) à l'aide d'un FileReader
            StringBuilder content = new StringBuilder();
            //pour stocker le contenu du fichier.
            String line;//pour stocker chaque ligne lue du fichier
            while ((line = br.readLine()) != null) {
   //Lit chaque ligne du fichier à l'aide de readLine() jusqu'à ce que 
   //la fin du fichier soit atteinte (null est retourné).
                content.append(line).append("\n");
   //Ajoute la ligne actuelle suivie d'un saut de ligne au StringBuilder.
            }
            inputArea.setText(content.toString().trim());
            //Configure le texte de la zone de texte inputArea avec le contenu du fichier, 
            //préalablement stocké dans le StringBuilder
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
      }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                new interfaceConv();
            }
        });
    }
}
