package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.border.LineBorder;
import javax.swing.*;
import java.awt.BorderLayout;
// import java.awt.*;

public class Board extends JPanel {

	private ArrayList<Square> allSquares = new ArrayList<Square>();

	public ArrayList<Square> getAllSquares(){
		return allSquares;
	}
	
	public Square getSquareAtIndex(int location) {
		return allSquares.get(location);
	}

	public Board(int xCoord, int yCoord, int width, int height) {
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setBounds(xCoord, yCoord, 612, 612);
		this.setLayout(null);
		initializeSquares();
	}

	private void initializeSquares() {
		
		// squares on the top
		Square square00 = new Square(6,6,100,100);
		this.add(square00);
		allSquares.add(square00);
		// pathSquares.add(square00);
		
		Square square01 = new Square(106,6,100,100);
		this.add(square01);
		allSquares.add(square01);
		
		Square square02 = new Square(206,6,100,100,1);
		this.add(square02);
		allSquares.add(square02);
		
		Square square03 = new Square(306,6,100,100);
		this.add(square03);
		allSquares.add(square03);
		
		Square square04 = new Square(406,6,100,100,2);
		this.add(square04);
		allSquares.add(square04);
		
		Square square05 = new Square(506,6,100,100);
		this.add(square05);
		allSquares.add(square05);
		

		// second line
		Square square06 = new Square(506,106,100,100,3);
		this.add(square06);
		allSquares.add(square06);
		
		Square square07 = new Square(406,106,100,100);
		this.add(square07);
		allSquares.add(square07);
		
		
		Square square08 = new Square(306,106,100,100,4);
		this.add(square08);
		allSquares.add(square08);
		
		Square square09 = new Square(206,106,100,100);
		this.add(square09);
		allSquares.add(square09);
		
		Square square10 = new Square(106,106,100,100,5);
		this.add(square10);
		allSquares.add(square10);
		

		Square square11 = new Square(6,106,100,100);
		this.add(square11);
		allSquares.add(square11);

		// third line
		Square square12 = new Square(6,206,100,100,6);
		this.add(square12);
		allSquares.add(square12);
		
		Square square13 = new Square(106,206,100,100);
		this.add(square13);
		allSquares.add(square13);

		Square square14 = new Square(206,206,100,100,7);
		this.add(square14);
		allSquares.add(square14);

		Square square15 = new Square(306,206,100,100);
		this.add(square15);
		allSquares.add(square15);

		Square square16 = new Square(406,206,100,100,8);
		this.add(square16);
		allSquares.add(square16);

		Square square17 = new Square(506,206,100,100);
		this.add(square17);
		allSquares.add(square17);

		// fourth line
		Square square18 = new Square(506,306,100,100,9);
		this.add(square18);
		allSquares.add(square18);

		Square square19 = new Square(406,306,100,100);
		this.add(square19);
		allSquares.add(square19);

		Square square20 = new Square(306,306,100,100,10);
		this.add(square20);
		allSquares.add(square20);

		Square square21 = new Square(206,306,100,100);
		this.add(square21);
		allSquares.add(square21);

		Square square22 = new Square(106,306,100,100,11);
		this.add(square22);
		allSquares.add(square22);

		Square square23 = new Square(6,306,100,100);
		this.add(square23);
		allSquares.add(square23);

		// fifth line
		Square square24 = new Square(6,406,100,100,12);
		this.add(square24);
		allSquares.add(square24);

		Square square25 = new Square(106,406,100,100);
		this.add(square25);
		allSquares.add(square25);

		Square square26 = new Square(206,406,100,100,13);
		this.add(square26);
		allSquares.add(square26);

		Square square27 = new Square(306,406,100,100);
		this.add(square27);
		allSquares.add(square27);

		Square square28 = new Square(406,406,100,100,14);
		this.add(square28);
		allSquares.add(square28);

		Square square29 = new Square(506,406,100,100);
		this.add(square29);
		allSquares.add(square29);

		// sixth line
		Square square30 = new Square(506,506,100,100,15);
		this.add(square30);
		allSquares.add(square30);

		Square square31 = new Square(406,506,100,100);
		this.add(square31);
		allSquares.add(square31);

		Square square32 = new Square(306,506,100,100,16);
		this.add(square32);
		allSquares.add(square32);

		Square square33 = new Square(206,506,100,100);
		this.add(square33);
		allSquares.add(square33);

		Square square34 = new Square(106,506,100,100);
		this.add(square34);
		allSquares.add(square34);

		String gifPath = "Spinderella_Game_Java\\src\\img//gif_35.gif"; // Replace with the actual path
        ImageIcon gifIcon = new ImageIcon(gifPath);
        JLabel gifLabel = new JLabel(gifIcon);

		Square square35 = new Square(6,506,100,100);
		square35.setLayout(new BorderLayout()); // Setting layout to add the label
		square35.add(gifLabel); // Adding the label to the square
		this.add(square35);
		allSquares.add(square35);
		
	 // Set images for specific squares
	String basePath = "Spinderella_Game_Java\\src\\img//";
	for (int i = 0; i < allSquares.size(); i++) {
    String imageFile = "";

    if (i == 0) {
        imageFile = "start.png";
    } else if (i == 34) {
        imageFile = "c.png";
    } 
	// else if (i == 35) {
    //     imageFile = "end.png";
    // } 
	else if (i % 2 == 0 && i != 0 && i <= 32) {
        imageFile = "point_f.png";
    } else if (i % 2 != 0 && i <= 33 && i!= 5 && i != 17 && i != 29 && i != 11 && i != 23) {
        imageFile = "ff.png";
    } else if (i == 5 || i == 17 || i == 29) {
        imageFile = "gg.png";
    } else if (i == 11 || i == 23) {
        imageFile = "gg2.png";
    }

    if (!imageFile.isEmpty()) {
        allSquares.get(i).setImage(basePath + imageFile);
    }
}

	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
}
