package gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
// import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
// import java.util.Map;
import java.util.Queue;
import java.util.Random;
// import java.lang.Math;

// import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import java.awt.Image;
// import java.awt.Window;
import java.awt.Graphics;

// import gui.Dice;
// import gui.Player;
// import gui.Board;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class SpinderellaMain extends JFrame{

	private JPanel contentIncluder;
	static JTextArea infoConsole;
	JPanel playerAssetsPanel;
	CardLayout c1 = new CardLayout();
	static ArrayList<Player> ants_1 = new ArrayList<Player>();
	static ArrayList<Player> ants_2 = new ArrayList<Player>();
	static ArrayList<Spider> spiders = new ArrayList<Spider>();
	static Queue<Player> orderOfAnts = new LinkedList<>(); //check the order of ants in a Square
    static int ant_number = 0; // the total number of ants in a square
	static int turnCounter = 0;
	static int diceMove;
	JButton btnNextTurn;
	JButton btnRollDice;
	JButton btnSpider1, btnSpider2;
	JButton btnSpiderUp, btnSipderDown, btnSpiderLeft, btnSpiderRight;
	JButton btnAnt1, btnAnt2;
	JTextArea panelPlayer1TextArea;
	JTextArea panelPlayer2TextArea;
	Board gameBoard;
	Player player1; //as ant1
	Player player2;	//as ant2
	Player player3;	//as ant3
	Player player4;	//as ant4
	Spider spider1, spider2;
	
	static int turnSpider = 0; // the number of spider moves
	static int nowPlaying = 0; // 0 -> Red, 1 -> Blue
	static int spiderPlaying = 0; // 0 -> Spider1, 1 -> Spider2
	static int flagCatchAnt = 1;
	
	// Maps square numbers to a LIST of players currently on the square
	private HashMap<Integer, LinkedList<Player>> squareToAntsMap = new HashMap<>();
	
	private Tree tree;

	// Add a class-level variable to track pending tree movement
	private boolean pendingTreeMove = false;

 // Custom JPanel class to handle background images
class ImagePanel extends JPanel {
	private Image backgroundImage;

	private JLabel imageLabel;

	public ImagePanel(String imagePath) {
		backgroundImage = new ImageIcon(imagePath).getImage();
		this.setLayout(new BorderLayout());
        ImageIcon imageIcon = new ImageIcon(imagePath);
        imageLabel = new JLabel(imageIcon);
        this.add(imageLabel, BorderLayout.CENTER);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (backgroundImage != null) {
			g.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), this);
		}
	}
}

	// This method should be called whenever the tree moves
public void updateAntPosition(Player ant, int oldSquare, int newSquare) {
		//squareToAntsMap => Keep track of which Ant are in each square
		//This line is getting the list of ants in the new square.
		LinkedList<Player> antsInOldSquare = squareToAntsMap.getOrDefault(oldSquare, new LinkedList<>());
		LinkedList<Player> antsInNewSquare = squareToAntsMap.computeIfAbsent(newSquare, k -> new LinkedList<>());
		
		int movingAntIndex = antsInOldSquare.indexOf(ant);

		List<Player> subsequentAnts = new ArrayList<>(); //Used to store ants that arrive after the leading ant.
	
		// Identify subsequent ants
		// Identify any ants that are in the old square and are positioned after the moving ant!
		if (oldSquare != newSquare) {
			for (int i = movingAntIndex + 1; i < antsInOldSquare.size(); i++) {
				Player subsequentAnt = antsInOldSquare.get(i);
				subsequentAnts.add(subsequentAnt);
			}
	
			// Move the subsequent ants the same number of steps as the moving ant!
			for (Player subsequentAnt : subsequentAnts) {
				int moveSteps = newSquare - oldSquare;
				subsequentAnt.move(moveSteps);
			}
		}

		// Update the squares
		antsInOldSquare.removeAll(subsequentAnts);
		antsInOldSquare.remove(ant);
	
		// Add the ants to the new square in correct order
		if (!antsInNewSquare.isEmpty()) {
			antsInNewSquare.addAll(subsequentAnts);
			antsInNewSquare.addLast(ant); // Add the leading ant last to maintain the order
			
		} else {
			antsInNewSquare.add(ant); // If the new square was empty, add the leading ant first
			antsInNewSquare.addAll(subsequentAnts);
		}
	
		// If the old square is the tree's square and the ant's label has been modified, restore the ant's original label (*)
		// if (oldSquare == tree.getCurrentSquare() && ant.isLabelModified()) {
		// 	this.resetAntLabelAndColor(ant);
		// }

		// Update labels in both squares
		updateAntLabels(antsInOldSquare);
		updateAntLabels(antsInNewSquare);
	}
	
	
//Update the labels of all ants in a given LinkedList
private void updateAntLabels(LinkedList<Player> ants) {
		for (int i = 0; i < ants.size(); i++) {
			callUpdateLabelWithSuffix(ants.get(i), i + 1);
		}
	}
	
//Determining the new positions of the ants and their order in a square:

//Returns a string representing the ordinal suffix of that number
private String getOrdinalSuffix(int value) {
		if (value >= 11 && value <= 13) {
			return "th";
		}
		switch (value % 10) {
			case 1:
				return "-1";
			case 2:
				return "-2";
			case 3:
				return "-3";
			default:
				return "-4";
		}
	}

//Updates the label of the ant to reflect its order in a list (for example, "1st", "2nd", "3rd", etc.).
private void callUpdateLabelWithSuffix(Player ant, int order) {
	String suffix = getOrdinalSuffix(order);
	ant.updateLabel(suffix);
}


//Handles the interaction between the tree and the ants when an ant moves from an old square to a new square.
	private void updateTreeAndAntsInteraction(int oldSquare, int newSquare) {
		
		// Only proceed if the old square and new square are different
		if (oldSquare != newSquare) {
		// Reset ants categories at the old square
		LinkedList<Player> antsInOldSquare = squareToAntsMap.getOrDefault(oldSquare+1, new LinkedList<>());    

		for (Player ant : antsInOldSquare) {
		     ant.setUnderTree(false);
		}

		// Update ants at the new square
		LinkedList<Player> antsInNewSquare = squareToAntsMap.getOrDefault(newSquare, new LinkedList<>());
		for (Player ant : antsInNewSquare) {
		// This will update the label to "Under" and color to green 
		// It will also store the labels and the old color!
			ant.setUnderTree(true); 
		}
		}
	}

	private void handleTreeMovement() {
		// Gets the current square of the tree
		int oldSquare = tree.getCurrentSquare();
	
		boolean validInput = false;
	
		while (!validInput) {
			// Asks the user for the new square to move the tree to
			String input = JOptionPane.showInputDialog(SpinderellaMain.this, "Which square do you want to move the tree to?");
	
			try {
				// Parse the input as an integer
				int newSquare = Integer.parseInt(input);
	
				// Check if the entered value is within the range of 1 to 15
				if (newSquare >= 1 && newSquare <= 16) {
					tree.setCurrentSquare(newSquare - 1);
	
					// Sets the tree's current square to the new square.
					updateTreeAndAntsInteraction(oldSquare, newSquare);
	
					// The reason for doing this is to get the lists of ants that are currently in the old and new squares.
					// These lists can then be used to update the game state when the tree moves.
					LinkedList<Player> antsInOldSquare = squareToAntsMap.getOrDefault(oldSquare, new LinkedList<>());
					LinkedList<Player> antsInNewSquare = squareToAntsMap.getOrDefault(newSquare, new LinkedList<>());
	
					antsInOldSquare.removeAll(antsInNewSquare);
	
					squareToAntsMap.put(newSquare, antsInNewSquare); // updated list of ants in the new square.
	
					squareToAntsMap.put(oldSquare, antsInOldSquare); // updated list of ants in the old square.
	
					pendingTreeMove = false;
					validInput = true; // exit the loop if input is valid
				} else {
					JOptionPane.showMessageDialog(SpinderellaMain.this, "Invalid input. Please enter a number between 1 and 16.", "Error", JOptionPane.ERROR_MESSAGE);
				}
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(SpinderellaMain.this, "Invalid input. Please enter a valid integer.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}






	//capture event
	// public void captureEvent() {
	// 	ArrayList<Player> allAnts = new ArrayList<>(Arrays.asList(player1, player2, player3, player4));
 
    // 	for (int i = allAnts.size() - 1; i >= 0; i--) {
    //     	Player ant = allAnts.get(i);
    //     	if(ant.getCurrentSquareNumber() == spider1.getConvertedSquare() && !ant.isUnderTree()) {
    //         	int oldSquare = allAnts.get(i);
	// 			ant.setToStartDestination();
    //         	ant.resetLabel(); // Assuming you have a method in Player to reset the label
    //         	break; // Capture only one ant and exit the loop
    //     }
    // }

	public void captureEvent() {
		// Get the list of ants on the square where the spider is
		LinkedList<Player> antsOnSquare = squareToAntsMap.getOrDefault(spider1.getConvertedSquare(), new LinkedList<>());
	
		// Iterate over the ants on the square in their order of arrival
		for (int i = antsOnSquare.size() - 1 ; i >= 0; i--) {
			Player ant = antsOnSquare.get(i);
			if (!ant.isUnderTree()) { // If the ant is not under the tree
				int oldSquare = ant.getCurrentSquareNumber();
	
				ant.setToStartDestination(); // Move the ant to the start
				ant.resetLabel(); // Reset the ant's label
	
				// Remove the ant from its old square in squareToAntsMap
				antsOnSquare.remove(ant);
	
				// Update labels for the old square
				updateAntLabels(squareToAntsMap.get(oldSquare));
				flagCatchAnt = 0;
				break; // Capture only one ant and exit the loop
			} else {
			}
		}
	}

	public void WinCondition() {
		int currentSquareOfRedAnt1 = player1.getCurrentSquareNumber();
		int currentSquareOfRedAnt2 = player2.getCurrentSquareNumber();
		int currentSquareOfBlueAnt1 = player3.getCurrentSquareNumber();
		int currentSquareOfBlueAnt2 = player4.getCurrentSquareNumber();

		if(currentSquareOfRedAnt1 >= 17 && currentSquareOfRedAnt2 >= 17){
			displayWinningScreen("Red Player is the winner!","Spinderella_Game_Java\\src\\img//R.gif");
			// showError("Red Player is the winner!");
			btnNextTurn.setEnabled(false);
		}
		if(currentSquareOfBlueAnt1 >= 17 && currentSquareOfBlueAnt2 >= 17){
			displayWinningScreen("Blue Player is the winner!","Spinderella_Game_Java\\src\\img//B.gif");
			// showError("Blue Player is the winner!");
			btnNextTurn.setEnabled(false);
		}	
	}

	public void displayWinningScreen(String winnerMessage,String gifPath) {
    // Create and display the dialog
    JDialog winningDialog = new JDialog(this, "Game Over", true); // 'this' refers to the current JFrame
    winningDialog.setLayout(new BorderLayout());
    winningDialog.setSize(600, 400); // Set the size of the dialog
    winningDialog.setLocationRelativeTo(this); // Center it relative to the JFrame

    // Create a label to display the winning message
    // JLabel messageLabel = new JLabel(winnerMessage, SwingConstants.CENTER);
    // messageLabel.setFont(new Font("Arial", Font.BOLD, 16));
    // winningDialog.add(messageLabel, BorderLayout.CENTER);

	// Create a label for the image
	 // Create a label for the GIF
	 ImageIcon gifIcon = new ImageIcon(gifPath); // Load the GIF
	 JLabel gifLabel = new JLabel(gifIcon);
	winningDialog.add(gifLabel, BorderLayout.CENTER);

    // Create a button to close the dialog
    JButton closeButton = new JButton("Close");
    closeButton.addActionListener(e -> winningDialog.dispose());
    winningDialog.add(closeButton, BorderLayout.SOUTH);

    winningDialog.setVisible(true); // Show the dialog
}

	public SpinderellaMain() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setSize(1080,720);

		// Set the frame icon
		ImageIcon icon = new ImageIcon("Spinderella_Game_Java\\src\\img\\Spider_Symbol.png");
		
		
		setIconImage(icon.getImage());


		contentIncluder = new JPanel();
		contentIncluder.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentIncluder);
		contentIncluder.setLayout(null);

		JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setBorder(new LineBorder(new Color(0, 0, 0)));
		layeredPane.setBounds(6, 6, 632, 630);
		contentIncluder.add(layeredPane);

		gameBoard = new Board(6,6,612,612);
		gameBoard.setBackground(new Color(80, 255, 153));
		layeredPane.add(gameBoard);

		// ImageIcon icon = new ImageIcon("resources/Spider_Symbol.png"); // Correct the path if necessary
    	// this.setIconImage(icon.getImage());

		player1 = new Player(1, Color.RED,"Player1_RED"); // red ant number 1
		ants_1.add(player1);
		layeredPane.add(player1);

		player2 = new Player(2, Color.RED,"Player2_RED"); // red ant number 2
		ants_2.add(player2);
		layeredPane.add(player2);

		player3 = new Player(1, Color.BLUE,"Player1_BlUE"); // blue ant number 1
		ants_1.add(player3);
		layeredPane.add(player3);

		player4 = new Player(2, Color.BLUE,"Player2_BLUE"); // blue ant number 2
		ants_2.add(player4);
		layeredPane.add(player4);

		//Spider 1 : Catch Spider
		spider1 = new Spider(1, Color.BLACK);
		spiders.add(spider1);
		layeredPane.add(spider1);

		//Spider 2 : Control Spider
		spider2 = new Spider(2, Color.BLACK);
		spiders.add(spider2);
		layeredPane.add(spider2);

		// JPanel rightPanel = new JPanel();
		// rightPanel.setBackground(Color.LIGHT_GRAY);
		ImagePanel rightPanel = new ImagePanel("Spinderella_Game_Java\\src\\img//b1.gif"); // replace with your image path
		rightPanel.setLayout(null);

		rightPanel.setBorder(new LineBorder(new Color(0, 0, 0))); //(0.0.0 -> black)
		rightPanel.setBounds(634, 6, 419, 630);
		contentIncluder.add(rightPanel);
		rightPanel.setLayout(null);

		//Dice event
		Dice dice1 = new Dice(86, 210, 40, 40);
		rightPanel.add(dice1);

		Dice dice2 = new Dice(174, 210, 40, 40);
		rightPanel.add(dice2);

		Dice dice3 = new Dice(262,210,40,40, "unique");
		rightPanel.add(dice3);

		Random random = new Random();
    	int randomSquare = random.nextInt(Tree.getXLocationsOfTreeLength()); // Use the new method
    	tree = new Tree(randomSquare);
    	layeredPane.add(tree); // Adding the tree to your game board
	
		//Roll Dice Event
		btnRollDice = new JButton("Roll Dice!");

		btnRollDice.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnRollDice.setEnabled(false);
				
				// Start animation in a new thread
				new Thread(() -> {
					long startTime = System.currentTimeMillis();
					while((System.currentTimeMillis() - startTime) / 1000F < 1.25) { // 3-second animation
						// Randomly change dice faces
						dice1.rollDice();
						dice2.rollDice();
						dice3.rollDice2();
		
						try {
							Thread.sleep(125); // Control animation speed
						} catch (InterruptedException ex) {
							ex.printStackTrace();
						}
					}

				//Using to check tree movement
				// int temp = Integer.parseInt(JOptionPane.showInputDialog(null, "Nhập steps: ", JOptionPane.INFORMATION_MESSAGE));
				// dice3.setFaceValue(temp);
					// dice3.setFaceValue(1);
					// dice1.setFaceValue(6);

				// //Condition to take the value of dice
				if(dice3.getFaceValue() == 1 || dice3.getFaceValue() == 2 || dice3.getFaceValue() == 3) {
					diceMove = dice1.getFaceValue();
					//test cases dice move
					// diceMove = Integer.parseInt(JOptionPane.showInputDialog(null, "Nhập steps: ", JOptionPane.INFORMATION_MESSAGE));
					btnSpider1.setEnabled(false);
					btnSpider2.setEnabled(false);
					btnAnt1.setEnabled(true);
					btnAnt2.setEnabled(true);
					btnRollDice.setEnabled(false);
					btnNextTurn.setEnabled(true);
				} else if(dice3.getFaceValue() == 4 || dice3.getFaceValue() == 5 ) {
					diceMove = dice2.getFaceValue();
					turnSpider = diceMove;
					btnSpider1.setEnabled(true);
					btnSpider2.setEnabled(true);
					btnAnt1.setEnabled(false);
					btnAnt2.setEnabled(false);
					btnRollDice.setEnabled(false);
					btnNextTurn.setEnabled(true);
				} else if (dice3.getFaceValue() == 6) { // Call the method to handle tree movement
					
					//First Choice - 'Ant-Spider' or 'Tree':
					boolean validInput = false;
					// flagCatchAnt = 1;
					// Store the current (old) square of the tree
					//int oldSquare = tree.getCurrentSquare();

					while(!validInput){
						String choice = JOptionPane.showInputDialog(this, "Choose Ant-Spider(A-S) or Tree(T)");
						
						//If 'Ant-Spider' is Chosen:
						if ("A-S".equalsIgnoreCase(choice) || "S-A".equalsIgnoreCase(choice) || "AS".equalsIgnoreCase(choice) 	
							|| "SA".equalsIgnoreCase(choice) || "ant-spider".equalsIgnoreCase(choice) || "spider-ant".equalsIgnoreCase(choice)
								|| "AntSpider".equalsIgnoreCase(choice) || "SpiderAnt".equalsIgnoreCase(choice)){
						
						validInput = true; // which will end the loop
						
						String antOrSpider = JOptionPane.showInputDialog(this, "Choose Ant(A) or Spider(S)");
						//The actual movement will occur when the enabled buttons are clicked by the player
						
							if ("A".equalsIgnoreCase(antOrSpider) || "ant".equalsIgnoreCase(antOrSpider)) {
							// Enable Ant movement buttons
							diceMove = dice1.getFaceValue();
							btnAnt1.setEnabled(true);
							btnAnt2.setEnabled(true);
							} else if ("S".equalsIgnoreCase(antOrSpider) || "spider".equalsIgnoreCase(antOrSpider)) {
							// Enable Spider movement buttons
							diceMove = dice2.getFaceValue();
							turnSpider = diceMove; // Or other spider logic
							btnSpider1.setEnabled(true);
							btnSpider2.setEnabled(true);
							
						}
						// Indicate that tree movement is pending and should be handled after ant/spider action
						pendingTreeMove = true;

				} else if ("T".equalsIgnoreCase(choice) || "tree".equalsIgnoreCase(choice)) {
						validInput = true;
						// Asking for tree movement:
						// int oldSquare = tree.getCurrentSquare();
						// int newSquare = Integer.parseInt(JOptionPane.showInputDialog(this, "Which square do you want to move the tree to?"));
						// tree.setCurrentSquare(newSquare-1); // this method internally calls updateTreePositionOnBoard() within the Tree class.
						handleTreeMovement();
						
						// Call updateTreeAndAntsInteraction with both old and new square
						// Then prompt for Ant or Spider choice
						String antOrSpider = JOptionPane.showInputDialog(this, "Choose Ant(A) or Spider(S)");
						if ("A".equalsIgnoreCase(antOrSpider) || "ant".equalsIgnoreCase(antOrSpider)) {
							diceMove = dice1.getFaceValue();
							btnAnt1.setEnabled(true);
							btnAnt2.setEnabled(true);
						} else if ("S".equalsIgnoreCase(antOrSpider) || "spider".equalsIgnoreCase(antOrSpider)) {
							diceMove = dice2.getFaceValue();
							turnSpider = diceMove;
							btnSpider1.setEnabled(true);
							btnSpider2.setEnabled(true);
						}
					} else {
						JOptionPane.showMessageDialog(SpinderellaMain.this, "Invalid input. Please choose 'Ant-Spider' or 'Tree'", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
				btnRollDice.setEnabled(false);
    			btnNextTurn.setEnabled(true);
			}
				
				btnRollDice.setEnabled(false);
				// we have to add below 2 lines to avoid some GUI breakdowns.
				layeredPane.remove(gameBoard);
				layeredPane.add(gameBoard);
				repaint();
				updatePanelPlayer1TextArea();
				updatePanelPlayer2TextArea();
			}).start();
		}
		});
		btnRollDice.setBounds(81, 413, 246, 53);
		rightPanel.add(btnRollDice);
		btnRollDice.setEnabled(true);

//The first ant moves
		btnAnt1 = new JButton("Ant1");
		btnAnt1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				//move ant
				Player currentPlayer = ants_1.get(nowPlaying);
				
				// Check if the current player is not under the tree => Allow to move
				if(currentPlayer.isUnderTree()) {
					// Show error if the ant is under the tree and cannot move
				showError("This ant is under the tree and cannot move!");
	
				// Keep the buttons enabled, allowing the user to choose the other ant if it's not under the tree
				btnAnt2.setEnabled(true);
				return;
				}
				
				

				int oldSquare = currentPlayer.getCurrentSquareNumber(); //Save the ant's old square
				
				currentPlayer.move(diceMove);
				int newSquare = currentPlayer.getCurrentSquareNumber();

				// Update ant positions and labels
				updateAntPosition(currentPlayer, oldSquare, newSquare);
                if (pendingTreeMove) {
					handleTreeMovement();
					pendingTreeMove = false;
				}

		        //moveAntAndSubsequent(currentPlayer, oldSquare);

				//Win condition
				WinCondition();

				// Update the UI after the action
				infoConsole.setText("Click Next Turn to allow player " + (nowPlaying == 0 ? 1 : 2) + " to Roll Dice!");
				btnAnt1.setEnabled(false);
				btnAnt2.setEnabled(false);
			}
	 });
		btnAnt1.setBounds(35, 478, 65, 29);
		rightPanel.add(btnAnt1);

		//Set in order when the conditions meet become true!
		btnAnt1.setEnabled(false);


//The second ant moves
		btnAnt2 = new JButton("Ant2");
		btnAnt2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Get the current player based on the turn
				Player currentPlayer = ants_2.get(nowPlaying);
			
				// Check if the current player is under the tree
			if(currentPlayer.isUnderTree()) {
				// Show error if the ant is under the tree and cannot move
				showError("This ant is under the tree and cannot move!");
	
				// Keep the buttons enabled, allowing the user to choose the other ant if it's not under the tree
				btnAnt2.setEnabled(true);
				return;
			}
			
			
				// Allow the ant to move
				int oldSquare = currentPlayer.getCurrentSquareNumber();
				currentPlayer.move(diceMove);
				int newSquare = currentPlayer.getCurrentSquareNumber();

				// Update ant positions and labels
				updateAntPosition(currentPlayer, oldSquare, newSquare);
				//moveAntAndSubsequent(currentPlayer, oldSquare);
				if (pendingTreeMove) {
				handleTreeMovement();
				pendingTreeMove = false;
			    }
				//Win condition
				WinCondition();
				//Update the UI after the action
				infoConsole.setText("Click Next Turn to allow player "+ (nowPlaying==0 ? 1 : 2) +" to Roll Dice!");
			
				btnAnt2.setEnabled(false);
				btnAnt1.setEnabled(false);

		}
		});
		btnAnt2.setBounds(125, 478, 65, 29);
		rightPanel.add(btnAnt2);
		btnAnt2.setEnabled(false); //Same

		//Spider1 button
		btnSpider1 = new JButton("BlackS");
		btnSpider1.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				spiderPlaying = 0; //0 is Spider 1, 1 is Spider 2
				
				btnSpiderUp.setEnabled(true);
				btnSipderDown.setEnabled(true);
				btnSpiderLeft.setEnabled(true);
				btnSpiderRight.setEnabled(true);
				
				repaint();
			}

		});
		btnSpider1.setBounds(215, 478, 90, 29);
		rightPanel.add(btnSpider1);
		btnSpider1.setEnabled(false);

		//Spider2 button
		btnSpider2 = new JButton("WhiteS");
		btnSpider2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				spiderPlaying = 1;
				btnSpiderUp.setEnabled(true);
				btnSipderDown.setEnabled(true);
				btnSpiderLeft.setEnabled(true);
				btnSpiderRight.setEnabled(true);
				
				repaint();
				
			}

		});

		btnSpider2.setBounds(315, 478, 90, 29);
		rightPanel.add(btnSpider2);
		btnSpider2.setEnabled(false);

		//Spider up button
		btnSpiderUp = new JButton("Up");
		btnSpiderUp.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Spider currentSpider = spiders.get(spiderPlaying);
				
				int originalSquare = currentSpider.getSpiderCurrentSquare(); // Store original square
				
				
				boolean validMove = currentSpider.moveUp(); // Attempt to move
				validMove = true;
				// Implement this method to check distance rules
				boolean distanceRuleViolated = false;


				// Spider rule: Can not go forth and back
				btnSipderDown.setEnabled(false);
				btnSpiderLeft.setEnabled(true);
				btnSpiderRight.setEnabled(true);


				//Spider rule: Spider1 and Spider2 must stay on the range of 2 squares
				 if (spider1.getTheYDistance(spider2) > 0 && spider1.getTheXDistance(spider2) > 200) {
					showError("Spider1 and Spider2 are far away from each other! (More than 2 Squares)");
					validMove = false; // Invalidate the move
            		distanceRuleViolated = true;
					turnSpider++;
				} 
				else if (spider1.getTheYDistance(spider2) > 100 && spider1.getTheXDistance(spider2) > 100) {
					showError("Spider1 and Spider2 are far away from each other! (More than 2 Squares)");
					validMove = false; // Invalidate the move
            		distanceRuleViolated = true;
					turnSpider++;
				} 
				else if (spider1.getTheYDistance(spider2) > 200 && spider1.getTheXDistance(spider2) > 0) {
					showError("Spider1 and Spider2 are far away from each other! (More than 2 Squares)");
					validMove = false; // Invalidate the move
					distanceRuleViolated = true;
					turnSpider++;
				} 
				else if(spider1.getTheYDistance(spider2) > 300 ) {
					showError("Spider1 and Spider2 are far away from each other! (More than 2 Squares)");
					validMove = false; // Invalidate the move
					distanceRuleViolated = true;
					turnSpider++;
				} 

				//Check solision between Spider1 and Spider2
				if(spider1.getSpiderCurrentSquare() == spider2.getSpiderCurrentSquare()) {
					showError("Spider1 and Spider2 can not both stand on the same square!");
					validMove = false; // Invalidate the move
					distanceRuleViolated = true;
					turnSpider++;
				}
				if(!validMove || distanceRuleViolated) {
					// If the move is invalid or a rule is violated, revert to original position
					currentSpider.setSpiderCurrentSquare(originalSquare); // Reset to original square
					currentSpider.updatePosition(); // Update the position on the board
					//currentSpider.moveDown(); // Assuming this is a valid corrective move
				}
				
				// Perform capture event if the move was valid and didn't violate distance rules
				if(validMove && flagCatchAnt == 1 && spiderPlaying == 0) {
					captureEvent();
					validMove=false;
				}
				//End Spider turn
				turnSpider--;
				if(turnSpider == 0) {
					btnSpiderUp.setEnabled(false);
					btnSipderDown.setEnabled(false);
					btnSpiderLeft.setEnabled(false);
					btnSpiderRight.setEnabled(false);
					btnSpider1.setEnabled(false);
					btnSpider2.setEnabled(false);
					btnNextTurn.setEnabled(true);
				}
			}

		});
		btnSpiderUp.setBounds(160, 347, 80, 29);
		rightPanel.add(btnSpiderUp);
		btnSpiderUp.setEnabled(false);


		//Spider down button
		btnSipderDown = new JButton("Down");
		btnSipderDown.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Spider currentSpider = spiders.get(spiderPlaying);
				
				int originalSquare = currentSpider.getSpiderCurrentSquare(); // Store original square
				boolean validMove = currentSpider.moveDown(); // Attempt to move
				validMove = true;
				// currentSpider.moveDown();
				// //capture event (if happen)
				// captureEvent();
				// Check distance rules

				// Implement this method to check distance rules
				boolean distanceRuleViolated = false;
				
				// Spider rule: Can not go forth and back
				btnSpiderUp.setEnabled(false);
				btnSpiderLeft.setEnabled(true);
				btnSpiderRight.setEnabled(true);

				//Spider rule: Spider1 and Spider2 must stay on the range of 2 squares
				 if (spider1.getTheYDistance(spider2) > 0 && spider1.getTheXDistance(spider2) > 200) {
					showError("Spider1 and Spider2 are far away from each other! (More than 2 Squares)");
					validMove = false; // Invalidate the move
            		distanceRuleViolated = true;
					turnSpider++;
				} 
				else if (spider1.getTheYDistance(spider2) > 100 && spider1.getTheXDistance(spider2) > 100) {
					showError("Spider1 and Spider2 are far away from each other! (More than 2 Squares)");
					validMove = false; // Invalidate the move
            		distanceRuleViolated = true;
					turnSpider++;
				} 
				else if (spider1.getTheYDistance(spider2) > 200 && spider1.getTheXDistance(spider2) > 0) {
					showError("Spider1 and Spider2 are far away from each other! (More than 2 Squares)");
					validMove = false; // Invalidate the move
            		distanceRuleViolated = true;
					turnSpider++;
				} 
				else if(spider1.getTheYDistance(spider2) > 300 ) {
					showError("Spider1 and Spider2 are far away from each other! (More than 2 Squares)");
					validMove = false; // Invalidate the move
            		distanceRuleViolated = true;
					turnSpider++;
				} 
				
				//Check solision between Spider1 and Spider2
				if(spider1.getSpiderCurrentSquare() == spider2.getSpiderCurrentSquare()) {
					showError("Spider1 and Spider2 can not both stand on the same square!");
					validMove = false; // Invalidate the move
            		distanceRuleViolated = true;
					turnSpider++;
				}

				if(!validMove || distanceRuleViolated) {
					// If the move is invalid or a rule is violated, revert to original position
					currentSpider.setSpiderCurrentSquare(originalSquare); // Reset to original square
					currentSpider.updatePosition(); // Update the position on the board
					//currentSpider.moveDown(); // Assuming this is a valid corrective move
				}
				
				// Perform capture event if the move was valid and didn't violate distance rules
				if(validMove && flagCatchAnt == 1 && spiderPlaying == 0) {
					captureEvent();
					validMove=false;
				}
				//End Spider turn
				turnSpider--;
				if(turnSpider == 0) {
					btnSpiderUp.setEnabled(false);
					btnSipderDown.setEnabled(false);
					btnSpiderLeft.setEnabled(false);
					btnSpiderRight.setEnabled(false);
					btnSpider1.setEnabled(false);
					btnSpider2.setEnabled(false);
					btnNextTurn.setEnabled(true);
				}
			}

		});
		btnSipderDown.setBounds(160, 380, 80, 29);
		rightPanel.add(btnSipderDown);
		btnSipderDown.setEnabled(false);

		//Spider left button
		btnSpiderLeft = new JButton("Left");
		btnSpiderLeft.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Spider currentSpider = spiders.get(spiderPlaying);
				//currentSpider.moveLeft();
				//capture event (if happen)
				// captureEvent();
				
				boolean validMove = currentSpider.moveLeft();
				validMove = true;
				int originalSquare = currentSpider.getSpiderCurrentSquare(); // Store original square
				// Check distance rules
				boolean distanceRuleViolated = false;


				//Spider rule: Spider1 and Spider2 must stay on the range of 2 squares
				 if (spider1.getTheXDistance(spider2) > 0 && spider1.getTheYDistance(spider2) > 200) {
					showError("Spider1 and Spider2 are far away from each other! (More than 2 Squares)");
					validMove = false; // Invalidate the move
            		distanceRuleViolated = true;
					turnSpider++;
				} 
				else if (spider1.getTheXDistance(spider2) > 100 && spider1.getTheYDistance(spider2) > 100) {
					showError("Spider1 and Spider2 are far away from each other! (More than 2 Squares)");
					validMove = false; // Invalidate the move
            		distanceRuleViolated = true;
					turnSpider++;
				} 
				else if (spider1.getTheXDistance(spider2) > 200 && spider1.getTheYDistance(spider2) > 0) {
					showError("Spider1 and Spider2 are far away from each other! (More than 2 Squares)");
					validMove = false; // Invalidate the move
            		distanceRuleViolated = true;
					turnSpider++;
				} 
				else if(spider1.getTheXDistance(spider2) > 300 ) {
					showError("Spider1 and Spider2 are far away from each other! (More than 2 Squares)");
					validMove = false; // Invalidate the move
            		distanceRuleViolated = true;
					turnSpider++;
				} 
				// Spider rule: Can not go forth and back
				btnSpiderRight.setEnabled(false);
				btnSpiderUp.setEnabled(true);
				btnSipderDown.setEnabled(true);
				//Check solision between Spider1 and Spider2
				if(spider1.getSpiderCurrentSquare() == spider2.getSpiderCurrentSquare()) {
					showError("Spider1 and Spider2 can not both stand on the same square!");
					validMove = false; // Invalidate the move
            		distanceRuleViolated = true;
					turnSpider++;
				}

				if(!validMove || distanceRuleViolated) {
					// If the move is invalid or a rule is violated, revert to original position
					currentSpider.setSpiderCurrentSquare(originalSquare + 1); // Reset to original square
					currentSpider.updatePosition(); // Update the position on the board
					//currentSpider.moveDown(); // Assuming this is a valid corrective move
				}
				
				// Perform capture event if the move was valid and didn't violate distance rules
				if(validMove && flagCatchAnt == 1 && spiderPlaying == 0) {
					captureEvent();
					validMove=false;
				}
				//End Spider turn
				turnSpider--;
				if(turnSpider == 0) {
					btnSpiderUp.setEnabled(false);
					btnSipderDown.setEnabled(false);
					btnSpiderLeft.setEnabled(false);
					btnSpiderRight.setEnabled(false);
					btnSpider1.setEnabled(false);
					btnSpider2.setEnabled(false);
					btnNextTurn.setEnabled(true);
				}
			}

		});
		btnSpiderLeft.setBounds(80, 380, 80, 29);
		rightPanel.add(btnSpiderLeft);
		btnSpiderLeft.setEnabled(false);

		//Spider right button
		btnSpiderRight = new JButton("Right");
		btnSpiderRight.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				Spider currentSpider = spiders.get(spiderPlaying);
				//currentSpider.moveRight();
				//capture event (if happen)
				//captureEvent();

				boolean validMove = currentSpider.moveRight();
				validMove = true;
				int originalSquare = currentSpider.getSpiderCurrentSquare(); // Store original square
				
				// Check distance rules
				boolean distanceRuleViolated = false;

				//Spider rule: Spider1 and Spider2 must stay on the range of 2 squares
				 if (spider1.getTheXDistance(spider2) > 0 && spider1.getTheYDistance(spider2) > 200) {
					showError("Spider1 and Spider2 are far away from each other! (More than 2 Squares)");
					validMove = false; // Invalidate the move
            		distanceRuleViolated = true;
					turnSpider++;
				} 
				else if (spider1.getTheXDistance(spider2) > 100 && spider1.getTheYDistance(spider2) > 100) {
					showError("Spider1 and Spider2 are far away from each other! (More than 2 Squares)");
					validMove = false; // Invalidate the move
            		distanceRuleViolated = true;
					turnSpider++;
				} 
				else if (spider1.getTheXDistance(spider2) > 200 && spider1.getTheYDistance(spider2) > 0) {
					showError("Spider1 and Spider2 are far away from each other! (More than 2 Squares)");
					validMove = false; // Invalidate the move
            		distanceRuleViolated = true;
					turnSpider++;
				} 
				else if(spider1.getTheXDistance(spider2) > 300 ) {
					showError("Spider1 and Spider2 are far away from each other! (More than 2 Squares)");
					validMove = false; // Invalidate the move
            		distanceRuleViolated = true;
					turnSpider++;
				} 
				// Spider rule: Can not go forth and back
				btnSpiderLeft.setEnabled(false);
				btnSpiderUp.setEnabled(true);
				btnSipderDown.setEnabled(true);
				//Check solision between Spider1 and Spider2
				if(spider1.getSpiderCurrentSquare() == spider2.getSpiderCurrentSquare()) {
					showError("Spider1 and Spider2 can not both stand on the same square!");
					validMove = false; // Invalidate the move
            		distanceRuleViolated = true;
					turnSpider++;
				}

				if(!validMove || distanceRuleViolated) {
					// If the move is invalid or a rule is violated, revert to original position
					currentSpider.setSpiderCurrentSquare(originalSquare - 1); // Reset to original square
					currentSpider.updatePosition(); // Update the position on the board
					//currentSpider.moveDown(); // Assuming this is a valid corrective move
				}
				
				// Perform capture event if the move was valid and didn't violate distance rules
				if(validMove && flagCatchAnt == 1 && spiderPlaying == 0) {
					captureEvent();
					validMove=false;
				}

				//End Spider turn
				turnSpider--;
				if(turnSpider == 0) {
					btnSpiderUp.setEnabled(false);
					btnSipderDown.setEnabled(false);
					btnSpiderLeft.setEnabled(false);
					btnSpiderRight.setEnabled(false);
					btnSpider1.setEnabled(false);
					btnSpider2.setEnabled(false);
					btnNextTurn.setEnabled(true);
				}
			}

		});
		btnSpiderRight.setBounds(240, 380, 80, 29);
		rightPanel.add(btnSpiderRight);
		btnSpiderRight.setEnabled(false);



		btnNextTurn = new JButton("Next Turn");
		// Load the image as an ImageIcon
		// ImageIcon nextTurnIcon = new ImageIcon("F://Java_Project//Spinderella_Game_Java_Trinh_draft1 - Sao chép - Sao chép//Spinderella_Game_Java_Trinh_draft1 - Sao chép - Sao chép//Spinderella_Game_Java//Spinderella_Game_Java//src//img//Tree.png"); 
		// btnNextTurn.setIcon(nextTurnIcon);

		btnNextTurn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				btnRollDice.setEnabled(true);
				btnAnt1.setEnabled(false);
				btnAnt2.setEnabled(false);
				btnSpider1.setEnabled(false);
				btnSpider2.setEnabled(false);
				btnNextTurn.setEnabled(false);
				btnSpiderUp.setEnabled(false);
				btnSipderDown.setEnabled(false);
				btnSpiderLeft.setEnabled(false);
				btnSpiderRight.setEnabled(false);
				flagCatchAnt = 1;
				if(nowPlaying == 0) {
					nowPlaying = 1;
					
				} else if(nowPlaying == 1 ) {
					nowPlaying = 0;
					
				} 
				c1.show(playerAssetsPanel, ""+(nowPlaying==0 ? 1 : 2)); // maps 0 to 1 and 1 to 2
				updatePanelPlayer1TextArea();
				updatePanelPlayer2TextArea();
				infoConsole.setText("It's now player "+(nowPlaying==0 ? 1 : 2)+"'s turn!");
			}
		});
		
		btnNextTurn.setBounds(81, 519, 246, 53);
		rightPanel.add(btnNextTurn);
		btnNextTurn.setEnabled(false);

		//Display the information of the game
		JPanel test = new JPanel();
		test.setBounds(81, 265, 246, 68);
		rightPanel.add(test);
		test.setLayout(null);
		//Below are the notificaiton panel, which will be updated according to the game progress. Can be ignored for now (Understand later)
		playerAssetsPanel = new JPanel();
		playerAssetsPanel.setBounds(81, 10, 246, 189);
		rightPanel.add(playerAssetsPanel);
		playerAssetsPanel.setLayout(c1);

		JPanel panelPlayer1 = new JPanel();
		panelPlayer1.setBackground(Color.RED);
		playerAssetsPanel.add(panelPlayer1, "1");
		panelPlayer1.setLayout(null);

		JLabel panelPlayer1Title = new JLabel("Rules of Spinderella");
		panelPlayer1Title.setForeground(Color.WHITE);
		panelPlayer1Title.setHorizontalAlignment(SwingConstants.CENTER);
		panelPlayer1Title.setBounds(0, 6, 240, 16);
		panelPlayer1.add(panelPlayer1Title);

		panelPlayer1TextArea = new JTextArea();
		panelPlayer1TextArea.setBounds(10, 34, 230, 149);
		panelPlayer1.add(panelPlayer1TextArea);

		JPanel panelPlayer2 = new JPanel();
		panelPlayer2.setBackground(Color.BLUE);
		playerAssetsPanel.add(panelPlayer2, "2");
		panelPlayer2.setLayout(null);
		c1.show(playerAssetsPanel, ""+nowPlaying);

		JLabel panelPlayer2Title = new JLabel("Rules of Spinderella");
		panelPlayer2Title.setForeground(Color.WHITE);
		panelPlayer2Title.setHorizontalAlignment(SwingConstants.CENTER);
		panelPlayer2Title.setBounds(0, 6, 240, 16);
		panelPlayer2.add(panelPlayer2Title);

		panelPlayer2TextArea = new JTextArea();
		panelPlayer2TextArea.setBounds(10, 34, 230, 149);
		panelPlayer2.add(panelPlayer2TextArea);
		
		updatePanelPlayer1TextArea();
		updatePanelPlayer2TextArea();


		infoConsole = new JTextArea();
		infoConsole.setColumns(20);
		infoConsole.setRows(5);
		// 
		infoConsole.setBounds(6, 6, 234, 56);
		test.add(infoConsole);
		infoConsole.setLineWrap(true);
		infoConsole.setText("PLayer 1 starts the game by clicking Roll Dice!");
		playBackgroundMusic("Spinderella_Game_Java\\src\\img//forestSound_3.wav"); // replace with your audio file path
	}
	
	
	public void updatePanelPlayer2TextArea() {
		
		String rules1 = "The first dice: ant" + "\n";
		String rules2 = "The second dice: spider" + "\n";
		String rules3 = "The third dice: game control" + "\n" +
		"Only BlackSpider can catch the ant" + "\n" + "You can click NextTurn to skip a turn" +"\n"+
		"If you roll the third dice and get the leaf,"+"\n"+ "you can move the tree and spiders or ants"+"\n"+
		"Tree can protect the ants from the spiders"+"\n"+" by moving the square which has the ants";				
						
		panelPlayer2TextArea.setText(rules1);
		panelPlayer2TextArea.append(rules2);
		panelPlayer2TextArea.append(rules3);
	}

	public void updatePanelPlayer1TextArea() {
		String rules1 = "The first dice: ant" + "\n";
		String rules2 = "The second dice: spider" + "\n";
		String rules3 = "The third dice: game control" + "\n" +
		                    "Only BlackSpider can catch the ant" + "\n" + "You can click NextTurn to skip a turn" +"\n"+
							"If you roll the third dice and get the leaf,"+"\n"+ "you can move the tree and spiders or ants"+"\n"+
							"Tree can protect the ants from the spiders"+"\n"+" by moving the square which has the ants";					
						
		panelPlayer1TextArea.setText(rules1);
		panelPlayer1TextArea.append(rules2);
		panelPlayer1TextArea.append(rules3);
	}
	
	public void showError(String error) {
		JOptionPane.showMessageDialog(null, error, "Notifying", JOptionPane.ERROR_MESSAGE);
	}

	private void playBackgroundMusic(String filePath) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(filePath).getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }


	public static void main(String[] args) {
		SpinderellaMain frame = new SpinderellaMain();
		frame.setVisible(true);
		frame.setResizable(false);
	}

}

