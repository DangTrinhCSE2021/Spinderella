package gui;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import java.awt.Image;


public class Tree extends JPanel {
    private int currentSquare = 0; //where player is currently located on the board
    private JLabel lblTree;


    // Coordinate arrays for the tree
    public static int[] xLocationsOfTree = {288, 488, 588, 388, 188, 88, 288, 488, 588, 388, 188, 88, 288, 488, 588, 388}; // Your X coordinates
    public int[] yLocationsOfTree = {33, 33, 133, 133, 133, 233, 233, 233, 333, 333, 333, 433, 433, 433, 533, 533}; // Your Y coordinates

    // Getter for xLocationsOfTree
     public int[] getXLocationsOfTree() {
        return xLocationsOfTree;
    }

    public int getCurrentSquare() {
        return this.currentSquare;
    }


    public void setCurrentSquare(int squareNumber) {
        this.currentSquare = squareNumber;
        updateTreePositionOnBoard();
    }

    private void updateTreePositionOnBoard() {
        // Similar to Player class, update the tree's position based on currentSquare
        int x = xLocationsOfTree[currentSquare];
        int y = yLocationsOfTree[currentSquare];
        setLocation(x, y);
    }

    // New method to get the length of xLocationsOfTree
    public static int getXLocationsOfTreeLength() {
        return xLocationsOfTree.length;
    }

    // Constructor
    public Tree(int square) {
        this.currentSquare = square;
        initTree();
    }

    //Initializes the tree's appearance and position
    private void initTree() {
        this.setBorder(new LineBorder(new Color(0, 0, 0)));
        this.setBackground(Color.BLACK); // Setting the color to black
        this.setOpaque(true);
        this.setBounds(xLocationsOfTree[currentSquare], yLocationsOfTree[currentSquare], 20, 20); 
        this.setLayout(null);

        // JLabel for the tree
        lblTree = new JLabel();
        lblTree.setFont(new Font("Lucida Grande", Font.BOLD, 15));
        lblTree.setForeground(Color.WHITE);
        this.add(lblTree); 
        lblTree.setBounds(0, 0, 20, 20); 

         // Load the image
        
         String imagePath = "Spinderella_Game_Java\\src\\img//Tree_2.png";

         // Load the image
         ImageIcon treeIcon = new ImageIcon(imagePath);

          // Optionally, resize the image to fit the label
         Image image = treeIcon.getImage(); 
         Image newimg = image.getScaledInstance(lblTree.getWidth(), lblTree.getHeight(), Image.SCALE_SMOOTH);
         treeIcon = new ImageIcon(newimg);
 
         // Set the icon to the label
         lblTree.setIcon(treeIcon);
        // lblTree.setText("");
    }
}
