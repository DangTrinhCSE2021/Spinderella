package gui;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
// import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JLabel;
import javax.swing.JPanel;
// import javax.swing.border.LineBorder;

public class Player extends JPanel {
	private Color color;
	private int playerNumber;
	
	private Color originalColor;
	private String originalLabel;

	private boolean isUnderTree = false;
	// private boolean isOnTree = false;

	JLabel lblPlayerNumber;

	private String antType;

	static int totalPlayers = 0; // we might need this number later on
	static HashMap<Integer, Integer> ledger= new HashMap<>();

	private int currentSquareNumber = 0; // where player is currently located on the board
	private int destination = 17; // where player wants to go

	public void setToStartDestination() {
		this.currentSquareNumber = 0;
		if (this.color == Color.RED && this.playerNumber == 1) {
			this.setLocation(xLocationsOfPlayer1_RED[currentSquareNumber], yLocationsOfPlayer1_RED[currentSquareNumber]);
		} else if (this.color == Color.RED && this.playerNumber == 2) {
			this.setLocation(xLocationsOfPlayer2_RED[currentSquareNumber], yLocationsOfPlayer2_RED[currentSquareNumber]);
		} else if (this.color == Color.BLUE && this.playerNumber == 1) {
			this.setLocation(xLocationsOfPlayer1_BLUE[currentSquareNumber], yLocationsOfPlayer1_BLUE[currentSquareNumber]);
		} else if (this.color == Color.BLUE && this.playerNumber == 2) {
			this.setLocation(xLocationsOfPlayer2_BLUE[currentSquareNumber], yLocationsOfPlayer2_BLUE[currentSquareNumber]);
		}
	}

	public int getCurrentSquareNumber() {
		return this.currentSquareNumber;
	}

	public int getPlayerNumber() {
		return playerNumber;
	}

	public boolean isUnderTree() { //Return false
		return this.isUnderTree;
	}

	//If isUnder is true (indicating the ant is now under the tree)
	public void setUnderTree(boolean isUnder) {
		if (isUnder && !this.isUnderTree) {
		// this.isUnderTree = isUnder;
		// Save original label and color only the first time the ant goes under the tree
		originalLabel = lblPlayerNumber.getText();
		originalColor = getBackground();
		lblPlayerNumber.setText("U");
		setBackground(Color.GREEN);
		//this.isUnderTree = isUnder;
		//Indicating the ant is under the tree
		// if (isUnder==true) { 
		// 	lblPlayerNumber.setText("U");
		// 	setBackground(Color.GREEN);
		}else if(!isUnder && this.isUnderTree){ //If isUnder is false (indicating the ant is no longer under the tree)
			// Restore original label and color only if the ant was previously under the tree
			lblPlayerNumber.setText(originalLabel);
			setBackground(originalColor);
			System.out.println("run");
			System.out.println(originalLabel + originalColor);
		}
		this.isUnderTree = isUnder; // Update the isUnderTree state at the end
	}

	
	public boolean isLabelModified() {
		return !lblPlayerNumber.getText().equals(String.valueOf(playerNumber));
	}

	public void resetLabel() {
		// Assuming playerNumber is the initial label (1 or 2)
		lblPlayerNumber.setText(String.valueOf(playerNumber));
	}
	
	private void updatePlayerPositionOnBoard() {
		int x = 0, y = 0;
		if (color == Color.RED && playerNumber == 1) {
			x = xLocationsOfPlayer1_RED[currentSquareNumber];
			y = yLocationsOfPlayer1_RED[currentSquareNumber];
		} else if (color == Color.RED && playerNumber == 2) {
			x = xLocationsOfPlayer2_RED[currentSquareNumber];
			y = yLocationsOfPlayer2_RED[currentSquareNumber];
		} else if (color == Color.BLUE && playerNumber == 1) {
			x = xLocationsOfPlayer1_BLUE[currentSquareNumber];
			y = yLocationsOfPlayer1_BLUE[currentSquareNumber];
		} else if (color == Color.BLUE && playerNumber == 2) {
			x = xLocationsOfPlayer2_BLUE[currentSquareNumber];
			y = yLocationsOfPlayer2_BLUE[currentSquareNumber];
		}
		setLocation(x, y);
	}

	public void setCurrentSquareNumber(int squareNumber) {
		this.currentSquareNumber = squareNumber;
		// Update the player's location on the board here as well
		updatePlayerPositionOnBoard();
	}

	 // Call this method to update the player's label
	public void updateLabel(String suffix) {
		if (suffix.equals("")) {
			lblPlayerNumber.setText(String.valueOf(playerNumber)); // Default label if alone
		} else {
			lblPlayerNumber.setText(playerNumber + suffix); // Updated label with order
		}
	}

	public String getAntLabel() {
		return lblPlayerNumber.getText();
	}

	public String getAntType() {
		return antType;
	}

	public Player(int playerNumber, Color color, String antType) {
		this.playerNumber = playerNumber;
		this.color = color;
		this.antType = antType;
		this.setBackground(color);
		lblPlayerNumber = new JLabel(""+playerNumber);
		lblPlayerNumber.setFont(new Font("Lucida Grande", Font.BOLD, 15));
		lblPlayerNumber.setForeground(Color.WHITE);
		this.add(lblPlayerNumber); 
		if(this.color == Color.RED) {	
			this.setBounds(playerNumber*30, 33, 28, 28); // need to fix here for adjustable player numbers
		} else {
			this.setBounds(playerNumber*30, 63, 28, 28); // need to fix here for adjustable player numbers
		}
		totalPlayers++;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}

	int[] xLocationsOfPlayer1_RED = {31,  231,  431, 
			531,  331,  131, 
			31,  231,  431,
			531,  331,  131, 
			31,  231,  431,
			531,  331,  131};

	int[] yLocationsOfPlayer1_RED = {33,  33,  33, 
			133,  133,  133, 
			233,  233,  233, 
			333,  333, 333,
			 433, 433, 433,
			 533, 533, 533};
	
	int[] xLocationsOfPlayer2_RED = {61,  261,  461, 
			561,  361,  161, 
			61,  261,  461, 
			561,  361,  161, 
			61,  261,  461, 
			561,  361,  161};
	int[] yLocationsOfPlayer2_RED = {33, 33, 33, 
			133, 133, 133, 
			233, 233, 233, 
			333, 333, 333,
			433, 433, 433, 
			533, 533, 533};

	int[] xLocationsOfPlayer1_BLUE = {31,  231,  431, 
			531,  331,  131, 
			31,  231,  431,
			531,  331,  131, 
			31,  231,  431,
			531,  331,  131};

	int[] yLocationsOfPlayer1_BLUE = {63,  63,  63, 
			163,  163,  163, 
			263,  263,  263, 
			363,  363, 363,
			 463, 463, 463,
			 563, 563, 563};
	
	int[] xLocationsOfPlayer2_BLUE = {61,  261,  461, 
			561,  361,  161, 
			61,  261,  461, 
			561,  361,  161, 
			61,  261,  461, 
			561,  361,  161};
	int[] yLocationsOfPlayer2_BLUE = {63,  63,  63, 
			163,  163,  163, 
			263,  263,  263, 
			363,  363, 363,
			 463, 463, 463,
			 563, 563, 563};
	
		//Represent the move action of the player
	public void move(int diceMove) {
		
		int targetSquare = (currentSquareNumber + diceMove);
		currentSquareNumber = targetSquare;
		//include the case when the player is at the end of the board
		if(currentSquareNumber < 17) {
			if (this.color == Color.RED && this.playerNumber == 1) {
				this.setLocation(xLocationsOfPlayer1_RED[targetSquare], yLocationsOfPlayer1_RED[targetSquare]);
		} else if (this.color == Color.RED && this.playerNumber == 2) {
				this.setLocation(xLocationsOfPlayer2_RED[targetSquare], yLocationsOfPlayer2_RED[targetSquare]);
		} else if (this.color == Color.BLUE && this.playerNumber == 1) {
				this.setLocation(xLocationsOfPlayer1_BLUE[targetSquare], yLocationsOfPlayer1_BLUE[targetSquare]);
		} else if (this.color == Color.BLUE && this.playerNumber == 2) {
				this.setLocation(xLocationsOfPlayer2_BLUE[targetSquare], yLocationsOfPlayer2_BLUE[targetSquare]);
		}

		//The ant is at the end of the board
	} 

	  else {
			if (this.color == Color.RED && this.playerNumber == 1) {
				this.setLocation(xLocationsOfPlayer1_RED[destination], yLocationsOfPlayer1_RED[destination]);
		} else if (this.color == Color.RED && this.playerNumber == 2) {
				this.setLocation(xLocationsOfPlayer2_RED[destination], yLocationsOfPlayer2_RED[destination]);
		} else if (this.color == Color.BLUE && this.playerNumber == 1) {
				this.setLocation(xLocationsOfPlayer1_BLUE[destination], yLocationsOfPlayer1_BLUE[destination]);
		} else if (this.color == Color.BLUE && this.playerNumber == 2) {
				this.setLocation(xLocationsOfPlayer2_BLUE[destination], yLocationsOfPlayer2_BLUE[destination]);
		}
		}
	// 	switch (this.antType) {
    //     case "Player1_RED":
    //         this.setLocation(xLocationsOfPlayer1_RED[targetSquare], yLocationsOfPlayer1_RED[targetSquare]);
    //         break;
    //     case "Player2_RED":
    //         this.setLocation(xLocationsOfPlayer2_RED[targetSquare], yLocationsOfPlayer2_RED[targetSquare]);
    //         break;
	// 	case "Player1_BLUE":
    //         this.setLocation(xLocationsOfPlayer1_BLUE[targetSquare], yLocationsOfPlayer1_BLUE[targetSquare]);
    //         break;
    //     case "Player2_BLUE":
    //         this.setLocation(xLocationsOfPlayer2_BLUE[targetSquare], yLocationsOfPlayer2_BLUE[targetSquare]);
    //         break;
	// }
}
}
