package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
// import javax.swing.border.LineBorder;
import java.lang.Math;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import java.awt.Image;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// import java.awt.Graphics;
public class Spider extends JPanel {
    Random rnd = new Random();
    private int SpiderNumber;
    JLabel lblSpiderNumber;
    
    private int spiderFirstRespawnLocation[] = { 14, 15, 20, 21 };
    private int firstSpiderSquare;
    private int spiderCurrentSquare = spiderFirstRespawnLocation[firstSpiderSquare];
    // private int spiderCurrentSquare;
    private static List<Integer> usedSquares = new ArrayList<>();
    private int convertedSquare;
    
    // private Image spiderImage;

    private Image[] spiderImages; // Array to hold different images of the spider
    private int currentImageIndex = 0; // To keep track of the current image
    private Timer animationTimer;

    public void convertToAntSquare() {
        switch (this.spiderCurrentSquare) {
            case 2:
                this.convertedSquare = 1;
                break;
            case 4:
                this.convertedSquare = 2;
                break;
            case 11:
                this.convertedSquare = 3;
                break;
            case 9:
                this.convertedSquare = 4;
                break;
            case 7:
                this.convertedSquare = 5;
                break;
            case 12:
                this.convertedSquare = 6;
                break;
            case 14:
                this.convertedSquare = 7;
                break;
            case 16:
                this.convertedSquare = 8;
                break;
            case 23:
                this.convertedSquare = 9;
                break;
            case 21:
                this.convertedSquare = 10;
                break;
            case 19:
                this.convertedSquare = 11;
                break;
            case 24:
                this.convertedSquare = 12;
                break;
            case 26:
                this.convertedSquare = 13;
                break;
            case 28:
                this.convertedSquare = 14;
                break;
            case 35:
                this.convertedSquare = 15;
                break;
            case 33:
                this.convertedSquare = 16;
                break;
            case 31:
                this.convertedSquare = 17;
                break;
            default:
                break;
        }
    }

    
    public int getConvertedSquare() {
        return this.convertedSquare;
    }

    public void updatePosition() {
        int targetSquare = this.spiderCurrentSquare;
        this.setLocation(xLocationsOfSpider[targetSquare], yLocationsOfSpider[targetSquare]);
    }
    

    public Spider(int SpiderNumber, Color color) {
        this.SpiderNumber = SpiderNumber;

        // Load spider image based on spider number
        // Load all spider images
        spiderImages = new Image[5]; // Assuming 3 images for each spider
        for (int i = 0; i < spiderImages.length; i++) {
        //  String imagePath = SpiderNumber == 1 ? "/img/SpiderKill.png" : "/img/SpiderControl.png";
        //  System.out.println("Image path: " + getClass().getResource(imagePath));
        //  spiderImage = new ImageIcon(getClass().getResource(imagePath)).getImage();
        String imagePath = "/img/Spider" + (SpiderNumber == 1 ? "Kill" : "Control") + (i+1) + ".png";
        
        spiderImages[i] = new ImageIcon(getClass().getResource(imagePath)).getImage();
        }
         // Initialize and start the animation timer
         animationTimer = new Timer(500, new ActionListener() { // 500 ms for example
            @Override
            public void actionPerformed(ActionEvent e) {
                currentImageIndex = (currentImageIndex + 1) % spiderImages.length;
                repaint(); // Trigger a repaint to show the new image
            }
        });
        animationTimer.start();

        do {
            firstSpiderSquare = new Random().nextInt(spiderFirstRespawnLocation.length);
            spiderCurrentSquare = spiderFirstRespawnLocation[firstSpiderSquare];
        } while (usedSquares.contains(spiderCurrentSquare));
        // Check if the square is already used
        if (usedSquares.contains(spiderCurrentSquare)) {
            throw new IllegalArgumentException("Square " + spiderCurrentSquare + " is already occupied by another spider.");
        }
        usedSquares.add(spiderCurrentSquare);
        // convert the square to the ant square
        convertToAntSquare();
        // set the background color of the spider
        this.setBackground(color);
        
        lblSpiderNumber = new JLabel("Spider" + SpiderNumber);
        lblSpiderNumber.setFont(new Font("Lucida Grande", Font.BOLD, 15));
        lblSpiderNumber.setForeground(Color.WHITE);
        this.add(lblSpiderNumber);

        lblSpiderNumber.setVisible(false); // Hide the label
        // Set JPanel to be transparent
        
        this.setOpaque(false);
        
        this.setBounds(xLocationsOfSpider[spiderCurrentSquare],
                yLocationsOfSpider[spiderCurrentSquare], 70, 70);
    }

    public void setSpiderCurrentSquare(int spiderCurrentSquare) {
        this.spiderCurrentSquare = spiderCurrentSquare;
    }

    public int getSpiderNumber() {
        return SpiderNumber;
    }

    public int getSpiderCurrentSquare() {
        return this.spiderCurrentSquare;
    }

    public int getXLocationOfSpider() {
        return this.xLocationsOfSpider[spiderCurrentSquare];
    }

    public int getYLocationOfSpider() {
        return this.yLocationsOfSpider[spiderCurrentSquare];
    }

    public int getTheXDistance(Spider otherSpider) {
        return Math.abs(this.getXLocationOfSpider() - otherSpider.getXLocationOfSpider());
    }

    public int getTheYDistance(Spider otherSpider) {
        return Math.abs(this.getYLocationOfSpider() - otherSpider.getYLocationOfSpider());
    }

    // public void paintComponent(Graphics g) {
    //     super.paintComponent(g);
    // }

    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // if (spiderImage != null) {
        //     g.drawImage(spiderImage, 0, 0, this.getWidth(), this.getHeight(), this);
        // }
        if (spiderImages != null && spiderImages[currentImageIndex] != null) {
            g.drawImage(spiderImages[currentImageIndex], 0, 0, this.getWidth(), this.getHeight(), this);
        }
    }

    int[] xLocationsOfSpider = {
            15, 115, 215, 315, 415, 515,
            15, 115, 215, 315, 415, 515,
            15, 115, 215, 315, 415, 515,
            15, 115, 215, 315, 415, 515,
            15, 115, 215, 315, 415, 515,
            15, 115, 215, 315, 415, 515 };

    int[] yLocationsOfSpider = {
            15, 15, 15, 15, 15, 15,
            115, 115, 115, 115, 115, 115,
            215, 215, 215, 215, 215, 215,
            315, 315, 315, 315, 315, 315,
            415, 415, 415, 415, 415, 415,
            515, 515, 515, 515, 515, 515 };

    public boolean moveUp() {
        if (spiderCurrentSquare > 5) {
            int targetSquare = (spiderCurrentSquare - 6);
            spiderCurrentSquare = targetSquare;
            convertToAntSquare();
            this.setLocation(xLocationsOfSpider[targetSquare], yLocationsOfSpider[targetSquare]);
            return true; // Valid move
        } else {
            JOptionPane.showMessageDialog(null, "You can't move up");
            SpinderellaMain.turnSpider++;
            return false; // Invalid move
        }
    }

    public boolean moveDown() {
        if (spiderCurrentSquare < 30) {
            int targetSquare = (spiderCurrentSquare + 6);
            spiderCurrentSquare = targetSquare;
            convertToAntSquare();
            this.setLocation(xLocationsOfSpider[targetSquare], yLocationsOfSpider[targetSquare]);
            return true; // Valid move
        } else {
            JOptionPane.showMessageDialog(null, "You can't move down");
            SpinderellaMain.turnSpider++;
            return false; // Invalid move
        }
    }

    public boolean moveLeft() {
        if (spiderCurrentSquare % 6 != 0) {
            int targetSquare = (spiderCurrentSquare - 1);
            spiderCurrentSquare = targetSquare;
            convertToAntSquare();
            this.setLocation(xLocationsOfSpider[targetSquare], yLocationsOfSpider[targetSquare]);
            return true; // Valid move
        } else {
            JOptionPane.showMessageDialog(null, "You can't move left");
            SpinderellaMain.turnSpider++;
            return false; // Invalid move
        }
    }

    public boolean moveRight() {
        if (spiderCurrentSquare % 6 != 5) {
            int targetSquare = (spiderCurrentSquare + 1);
            spiderCurrentSquare = targetSquare;
            convertToAntSquare();
            this.setLocation(xLocationsOfSpider[targetSquare], yLocationsOfSpider[targetSquare]);
            return true; // Valid move}
        } else {
            JOptionPane.showMessageDialog(null, "You can't move right");
            SpinderellaMain.turnSpider++;
            return false; // Invalid move
        }
    }
}
