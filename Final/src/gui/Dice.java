package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class Dice extends JPanel {
	
	private ImageIcon diceImage;
	private Image image; //Declare image as an instance variable
	Random rnd = new Random();
	int faceValue = 1;
	
	public Dice(int xCoord, int yCoord, int width, int height) {
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setBounds(xCoord, yCoord, width, height);
		updateDiceImage(faceValue); // Initialize with face 1
	}

    public Dice(int xCoord, int yCoord, int width, int height, String labelString) {
        setBorder(new LineBorder(new Color(0, 0, 0)));
        setBounds(xCoord, yCoord, width, height);
        updateDiceImage2(faceValue); // Initialize with face 1
    }   

public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
        }
    }

public void updateDiceImage(int faceValue) {
        String imagePath = "Spinderella_Game_Java\\src\\img//";
        if (faceValue >= 1 && faceValue <= 6) { // Normal dice
            imagePath += "dice_" + faceValue + ".png";
        } 
		// else { // Special dice
        //     imagePath += "dice_" + (faceValue + 6) + ".png"; // Adjust according to your naming
        // }
        diceImage = new ImageIcon(imagePath);
        image = diceImage.getImage();
        repaint();
    }

public void updateDiceImage2(int faceValue) {
        String imagePath = "Spinderella_Game_Java\\src\\img//";
        if (faceValue >= 1 && faceValue <= 3) { // Normal dice
            imagePath += "dice_" + (7) + ".png";
        }else if(faceValue >= 4 && faceValue <= 5){
            imagePath += "dice_" + (8) + ".png";
        }else{
            imagePath += "dice_" + (9) + ".png";
        }
		
        diceImage = new ImageIcon(imagePath);
        image = diceImage.getImage();
        repaint();
    }

public void rollDice() {
        faceValue = rnd.nextInt(6) + 1;
        updateDiceImage(faceValue);
    }
    public void rollDice2() {
        faceValue = rnd.nextInt(6) + 1;
        updateDiceImage2(faceValue);
    }

public int getFaceValue() {
        return faceValue;
    }

public void setFaceValue(int value){
    this.faceValue = value;
}
}
