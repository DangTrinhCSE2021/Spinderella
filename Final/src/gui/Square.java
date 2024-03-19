package gui;

import java.awt.Color;
import java.awt.Graphics;

import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

//this class is used to determine the squares on the board
public class Square extends JPanel {
	JLabel lplSquareNumber;
	int number;
	private ImageIcon image;
	// private String name;
	String description;
	private int antSquareNumber;
	// JLabel nameLabel;
	static int totalSquares = 0;

	public void setImage(String imagePath) {
        image = new ImageIcon(imagePath);
        repaint();
    }

	public Square(int xCoord, int yCoord, int width, int height) {
		number = totalSquares;
		totalSquares++;
		// setBorder(new LineBorder(new Color(0, 0, 0)));
		setBounds(xCoord, yCoord, width, height);
		
		this.setLayout(null);
	}

	public Square(int xCoord, int yCoord, int width, int height, int squareNumber) {
		number = totalSquares;
		totalSquares++;
		this.antSquareNumber = squareNumber;
		// setBorder(new LineBorder(new Color(0, 0, 0)));
		setBounds(xCoord, yCoord, width, height);
		//mark number
		lplSquareNumber = new JLabel(""+squareNumber);
		lplSquareNumber.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		lplSquareNumber.setForeground(Color.WHITE);
		this.add(lplSquareNumber); 
		this.setLayout(null);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (this.number == 1 || this.number == 3 ||
				this.number == 7 || this.number == 9 ||
				this.number == 13 || this.number == 15 ||
				this.number == 19 || this.number == 21 ||
				this.number == 25 || this.number == 27 ||
				this.number == 31 || this.number == 33) {
			g.drawRect(0, 40, this.getWidth(), 20);
			g.setColor(Color.green);
			g.fillRect(0, 40, this.getWidth(), 20);
		}
		if (this.number == 5 || this.number == 17 || this.number == 29) {
			g.drawRect(40, 40, 20, 60);
			g.setColor(Color.GRAY);
			g.fillRect(40, 40, 20, 60);

			g.drawRect(0, 40, this.getWidth() / 2, 20);
			g.setColor(Color.GRAY);
			g.fillRect(0, 40, this.getWidth() / 2, 20);

		}
		if (this.number == 23 || this.number == 11) {
			g.drawRect(40, 40, 20, 60);
			g.setColor(Color.GRAY);
			g.fillRect(40, 40, 20, 60);

			g.drawRect(50, 40, this.getWidth() / 2, 20);
			g.setColor(Color.GRAY);
			g.fillRect(50, 40, this.getWidth() / 2, 20);
		}


		if (image != null) {
            g.drawImage(image.getImage(), 0, 0, getWidth(), getHeight(), this);
        }

		if (this.number == 2 || this.number == 4 || this.number == 6
				|| this.number == 8 || this.number == 10 || this.number == 12
				|| this.number == 14 || this.number == 16 || this.number == 18
				|| this.number == 20 || this.number == 22 || this.number == 24
				|| this.number == 26 || this.number == 28 || this.number == 30
				|| this.number == 32) {
					
			g.setColor(Color.cyan);
    	g.setFont(new Font("Lucida Grande", Font.BOLD, 15));
    	g.drawString(Integer.toString(this.antSquareNumber), 10, 20);
		}
	}

}
