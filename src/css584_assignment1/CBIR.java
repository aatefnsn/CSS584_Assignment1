/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ahmed_nada
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.*;

public class CBIR extends JFrame {

    private JLabel photographLabel = new JLabel();  // new jlabel  
    private JButton[] button; //creates an array of JButtons for images
    private int[] buttonOrder = new int[101]; //creates an array to keep up with the image order
    private double[] imageSize = new double[100]; //keeps up with the image sizes
    private GridLayout gridLayout1;
    private GridLayout gridLayout2;
    private GridLayout gridLayout3;
    private GridLayout gridLayout4;
    private JPanel panelBottom1;
    private JPanel panelBottom2;
    private JPanel panelTop;
    private JPanel buttonPanel;
    private int[][] intensityMatrix = new int[100][25]; // intensity matrix that will be used to copy the data from text file
    private int[][] colorCodeMatrix = new int[100][64]; // color code matrix that will be used to copy the data from text file
    //private Map<Double, LinkedList<Integer>> map;
    int picNo = 0; // variable to hold the pic/image index
    int imageCount = 1; //keeps up with the number of images displayed since the first page.
    int pageNo = 1; // variable to hold the page number

    public static void main(String args[]) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                CBIR app = new CBIR();
                app.setVisible(true);
            }
        });
    }

    public CBIR() {
        //The following lines set up the interface including the layout of the buttons and JPanels.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Content-Based Image Retrieval system: Please Select an Image and the preferred method (Color Code Method or Intensity Method)");
        panelBottom1 = new JPanel();
        panelBottom2 = new JPanel();
        panelTop = new JPanel();
        buttonPanel = new JPanel();
        gridLayout1 = new GridLayout(4, 5, 5, 5);
        gridLayout2 = new GridLayout(2, 1, 5, 5);
        gridLayout3 = new GridLayout(1, 2, 5, 5);
        gridLayout4 = new GridLayout(2, 3, 5, 5);
        setLayout(gridLayout2);
        panelBottom1.setLayout(gridLayout1);
        panelBottom2.setLayout(gridLayout1);
        panelTop.setLayout(gridLayout3);
        add(panelTop);
        add(panelBottom1);
        photographLabel.setVerticalTextPosition(JLabel.BOTTOM);
        photographLabel.setHorizontalTextPosition(JLabel.CENTER);
        photographLabel.setHorizontalAlignment(JLabel.CENTER);
        photographLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttonPanel.setLayout(gridLayout4);
        panelTop.add(photographLabel);

        panelTop.add(buttonPanel);
        JButton previousPage = new JButton("Previous Page"); // previous 20 images
        JButton nextPage = new JButton("Next Page"); // next 20 images
        JButton intensity = new JButton("Intensity"); // intensity button to select intensity method
        JButton colorCode = new JButton("Color Code"); // color code button to select color code method
        JButton refresh = new JButton("Refresh"); // refresh button to revert all images to their original positions without having to restart the program
        buttonPanel.add(previousPage);
        buttonPanel.add(nextPage);
        buttonPanel.add(intensity);
        buttonPanel.add(colorCode);
        buttonPanel.add(refresh);

        nextPage.addActionListener(new nextPageHandler()); // setting action for each button using ActionListener
        previousPage.addActionListener(new previousPageHandler());
        intensity.addActionListener(new intensityHandler());
        colorCode.addActionListener(new colorCodeHandler());
        refresh.addActionListener(new refreshHandler());
        setSize(1100, 750);
        // this centers the frame on the screen
        setLocationRelativeTo(null);

        button = new JButton[101];
        /*This for loop goes through the images in the database and stores them as icons and adds
         * the images to JButtons and then to the JButton array
         */
        for (int i = 1; i < 101; i++) {
            ImageIcon icon;
            BufferedImage image = null;
            try {
                image = ImageIO.read(new File("images/" + i + ".jpg")); // read the images from the images folder
                int width = image.getWidth(); // get the width as it will be used in calculating the distance
                int height = image.getHeight(); // get the height of the image as it will be used in calculating the distance
                imageSize[i - 1] = width * height; // add the image size width x height in the image size array
            } catch (IOException e) {
                System.out.println("Error occurred when reading the file.");
            }
            icon = new ImageIcon(getClass().getResource("images/" + i + ".jpg")); // add a new icon for each image
            if (icon != null) {
                button[i] = new JButton(icon); // add the image icon in the button array
                button[i].addActionListener(new IconButtonHandler(i, icon)); 
                buttonOrder[i] = i; // add the index of each button in the button order array that will be used later on for image preview
            }
        }

        readIntensityFile(); // after getting the images and creating the icons, read the intensity.txt file from the readImage class
        readColorCodeFile(); // read the colorcode.txt resulting from the read image class
        displayFirstPage(); // display first page method which shows the first 20 image icons
    }

    /*This method opens the intensity text file containing the intensity matrix with the histogram bin values for each image.
     * The contents of the matrix are processed and stored in a two dimensional array called intensityMatrix.
     */
    public void readIntensityFile() { // read the intesity.txt file function
        Scanner read;
        String line = "";
        int lineNumber = 0;
        try {
            read = new Scanner(new File("intensity.txt")); // a new scanner object to read the intensity.txt
            while (read.hasNextLine()) { // while loop the scanner object has another line do the following 
                line = read.nextLine(); // read the following line
                String[] cols = line.split(","); // create a string array filled with the intensity bins after removing the comma separator 
                //System.out.println(Arrays.toString(cols));
                for (int j = 0; j < 25; j++) {
                    intensityMatrix[lineNumber][j] = Integer.parseInt(cols[j]); // parse the string value as integer and then add them in intensity matrix 
                }
                lineNumber++;
            }

            //Print the intensity Matrix
            System.out.println("Reconstructed Intensity Matrix");
            for (int o = 0; o < 100; o++) {
                System.out.println(Arrays.toString(intensityMatrix[o])); // print the intensity matrix
            }
        } catch (FileNotFoundException EE) { // catch statement if the intensity.txt file was not created previously by running the readImage class functions
            System.out.println("The file intensity.txt does not exist");
        }
    }

    /*This method opens the color code text file containing the color code matrix with the histogram bin values for each image.
     * The contents of the matrix are processed and stored in a two dimensional array called colorCodeMatrix.
     */
    private void readColorCodeFile() { // for colorcode.txt we are doing the same as reading and parsing the intensity.txt
        //StringTokenizer token;
        Scanner read;
        //Double colorCodeBin;
        int lineNumber = 0;
        String line = "";
        try {
            read = new Scanner(new File("colorCodes.txt"));
            while (read.hasNextLine()) {
                line = read.nextLine();
                String[] cols = line.split(",");
                //System.out.println(Arrays.toString(cols));
                for (int j = 0; j < 64; j++) {
                    colorCodeMatrix[lineNumber][j] = Integer.parseInt(cols[j]);
                }
                lineNumber++;
            }

            //Print the color code Matrix
            System.out.println("Reconstructed colorCode Matrix");
            for (int o = 0; o < 100; o++) {
                System.out.println(Arrays.toString(colorCodeMatrix[o]));
            }

        } catch (FileNotFoundException EE) {
            System.out.println("The file colorCodes.txt does not exist");
        }

    }

    /*This method displays the first twenty images in the panelBottom.  The for loop starts at number one and gets the image
     * number stored in the buttonOrder array and assigns the value to imageButNo.  The button associated with the image is 
     * then added to panelBottom1.  The for loop continues this process until twenty images are displayed in the panelBottom1
     */
    private void displayFirstPage() {
        int imageButNo = 0;
        panelBottom1.removeAll(); // refresh the bottom panel
        for (int i = 1; i < 21; i++) {
            imageButNo = buttonOrder[i]; // get the image button number from the buttonOrder array
            panelBottom1.add(button[imageButNo]); // add the button of imageButton number to the bottom panel
            JLabel label1 = new JLabel(imageButNo + ".jpg"); // add a label for the button
            label1.setForeground(Color.BLACK); // set the color to balck
            label1.setFont(new Font("SansSerif", Font.BOLD, 28)); // set the font type and font size
            panelBottom1.add(label1); // add the label to the bottom panel for each new button
            imageCount++; // increment the image count
        }
        panelBottom1.revalidate(); // revalidate and repaint the bottom panel
        panelBottom1.repaint();
    }

    /*This class implements an ActionListener for each iconButton.  When an icon button is clicked, the image on the 
     * the button is added to the photographLabel and the picNo is set to the image number selected and being displayed.
     */
    private class IconButtonHandler implements ActionListener {
        int pNo = 0;
        ImageIcon iconUsed;
        IconButtonHandler(int i, ImageIcon j) {
            pNo = i; // variable to store the image number when clicked
            iconUsed = j;  //sets the icon to the one used in the button
        }
        public void actionPerformed(ActionEvent e) {
            photographLabel.setIcon(iconUsed);
            picNo = pNo; // set the picNo variable to the current icon image
        }
    }

    /*This class implements an ActionListener for the nextPageButton.  The last image number to be displayed is set to the 
     * current image count plus 20.  If the endImage number equals 101, then the next page button does not display any new 
     * images because there are only 100 images to be displayed.  The first picture on the next page is the image located in 
     * the buttonOrder array at the imageCount
     */
    private class nextPageHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            int imageButNo = 0;
            int endImage = imageCount + 20; // next page handler should show the images starting the image count till the image count + 20
            if (endImage <= 101) { // if statement to make sure endimage does not exceed the number of images
                panelBottom1.removeAll(); // refresh bottom panel 
                for (int i = imageCount; i < endImage; i++) { //for loop from the current image count to the endImage counter
                    imageButNo = buttonOrder[i]; // get the image index from the buttonOrder array
                    panelBottom1.add(button[imageButNo]);
                    JLabel label1 = new JLabel(imageButNo + ".jpg");
                    label1.setForeground(Color.BLACK);
                    label1.setFont(new Font("SansSerif", Font.BOLD, 28));
                    panelBottom1.add(label1);
                    imageCount++;
                }
                panelBottom1.revalidate();
                panelBottom1.repaint();
            }
        }
    }

    /*This class implements an ActionListener for the previousPageButton.  The last image number to be displayed is set to the 
     * current image count minus 40.  If the endImage number is less than 1, then the previous page button does not display any new 
     * images because the starting image is 1.  The first picture on the next page is the image located in 
     * the buttonOrder array at the imageCount
     */
    private class previousPageHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            int imageButNo = 0;
            int startImage = imageCount - 40; // the start image of the previous page is the current - 40 
            int endImage = imageCount - 20; // the end image of the previouis page is the current count - 20
            if (startImage >= 1) {
                panelBottom1.removeAll();
                /*The for loop goes through the buttonOrder array starting with the startImage value
             * and retrieves the image at that place and then adds the button to the panelBottom1.
                 */
                for (int i = startImage; i < endImage; i++) { // same for loop as in the nextPageHandler
                    imageButNo = buttonOrder[i];
                    panelBottom1.add(button[imageButNo]);
                    JLabel label1 = new JLabel(imageButNo + ".jpg");
                    label1.setForeground(Color.BLACK);
                    label1.setFont(new Font("SansSerif", Font.BOLD, 28));
                    panelBottom1.add(label1);
                    imageCount--; // decrementing the imageCount so if nextPAge is called again, it will display the 20 next images
                }
                panelBottom1.revalidate();
                panelBottom1.repaint();
            }
        }
    }
    
    private class refreshHandler implements ActionListener { // refreshes the images back to their original position

        public void actionPerformed(ActionEvent e) {
          //int imageButNo = 0;
          imageCount=1; // set the image count to 1 
        panelBottom1.removeAll(); // refresh the bottom panel
        
        for (int i = 1; i < 101; i++) {
                    buttonOrder[i]=i; // refresh the image positions to the original position in the button order                   
                }        
        displayFirstPage(); // call the first page function to display the first 20 image icons       
        }
    }

    /*This class implements an ActionListener when the user selects the intensityHandler button.  The image number that the
     * user would like to find similar images for is stored in the variable pic.  pic takes the image number associated with
     * the image selected and subtracts one to account for the fact that the intensityMatrix starts with zero and not one.
     * The size of the image is retrieved from the imageSize array.  The selected image's intensity bin values are 
     * compared to all the other image's intensity bin values and a score is determined for how well the images compare.
     * The images are then arranged from most similar to the least.
     */
    private class intensityHandler implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            double[] distance = new double[100]; // distance array to hold the distance value of each image to the other image in that index
            //map = new HashMap<Double, LinkedList<Integer>>();
            int pic = (picNo - 1); // the position of the pic in the intensity matrix starts at 0 while pictures start at so decrementing 1 at the beginning to adjust
            //System.out.println("Real Pic number is " + picNo + ".jpg but its place in the intensity array is " + pic);
            //System.out.println("Image size is " + imageSize[pic]);
            for (int row = 0; row < 100; row++) {
                //System.out.println("Row "+ row + " Image size is "+imageSize[row]);
                for (int column = 0; column < 25; column++) {
                    //double dist= (intensityMatrix[row][column]/imageSize[row]) - (intensityMatrix[pic][column]/imageSize[pic]);
                    double dist = (intensityMatrix[pic][column] / imageSize[pic]) - (intensityMatrix[row][column] / imageSize[row]); // calculating distance as in the assignmnet document

                    if (dist < 0) {
                        dist = dist * -1; // getting the modulus of the distance if -ve value then get the absolute +ve
                    }
                    //distance[row] += dist;
                    distance[row] = distance[row] + dist; // adding the distances together
                }
                //System.out.println("distance of row " + row + " is " + distance[row]);
            }

            //Print the Distance Array
            System.out.println("Intensity method Distance Matrix of picture " + picNo);
            for (int o = 0; o < 100; o++) {
                System.out.print((distance[o]) + " ,");
            }

            System.out.println("\n");
            buttonOrder[1] = picNo; // set the selected picture to be the first in the button order, 
            //display first starts at buttonOder of 1 so the selected image will always be displayed first
            for (int u = 2; u < 101; u++) { // for loop to go over all the remaining images
                double smallest = 50000000; // setting the smallest distance variable to a random big number
                int smallestIndex = -1; // setting the index of the smallest to -1
                for (int k = 0; k < 100; k++) { // for loop to go over the ditance array and find the smallest distance to another image and then get the index of that next similar image
                    if (distance[k] < smallest && distance[k] != 0) { // f distance is smaller and if it is not 0 (same picture or a picture that has been already listed as similar)
                        smallest = distance[k]; // update smallest value
                        smallestIndex = k; // update the index
                    }
                }
                distance[smallestIndex] = 0; // once found the smallest distance, set it now to 0 so we do not go over it once again
                ++smallestIndex; // increment the smallest index by 1 to match the button Order
                //System.out.println("Smallest Index is " + smallestIndex);
                buttonOrder[u] = smallestIndex; // update button order
            }
            for (int u = 1; u < 101; u++) {
                System.out.print(buttonOrder[u] + ", "); // printing the button 
                //Order which will be the sequnce of images from the highest similar to the least similar according to intensity method
            }
            imageCount = 1; // before re-displaying we need to make sure to revert the imageCount to 1 to start displaying images from the start
            displayFirstPage(); // call the display first images to show the first 20 images, clicking next will display the next 20 and so on
        }
    }

    /*This class implements an ActionListener when the user selects the colorCode button.  The image number that the
     * user would like to find similar images for is stored in the variable pic.  pic takes the image number associated with
     * the image selected and subtracts one to account for the fact that the intensityMatrix starts with zero and not one. 
     * The size of the image is retrieved from the imageSize array.  The selected image's intensity bin values are 
     * compared to all the other image's intensity bin values and a score is determined for how well the images compare.
     * The images are then arranged from most similar to the least.
     */
    private class colorCodeHandler implements ActionListener { // exactly the same as the previous intensityHandler except that the number of columns is 64 instead of 25

        public void actionPerformed(ActionEvent e) {
            double[] distance = new double[101];
            //map = new HashMap<Double, LinkedList<Integer>>();
            int pic = (picNo - 1);
            System.out.println("Image size is " + imageSize[pic]);
            for (int row = 0; row < 100; row++) {
                //System.out.println("Row "+ row + " Image size is "+imageSize[row]);
                for (int column = 0; column < 64; column++) {
                    //double dist= (intensityMatrix[row][column]/imageSize[row]) - (intensityMatrix[pic][column]/imageSize[pic]);
                    double dist = (colorCodeMatrix[pic][column] / imageSize[pic]) - (colorCodeMatrix[row][column] / imageSize[row]);

                    if (dist < 0) {
                        dist = dist * -1;
                    }

                    distance[row] = distance[row] + dist;
                }
                //System.out.println("distance of row " + row + " is " + distance[row]);
            }

            System.out.println("Color Code method Distance Matrix of picture " + picNo);
            for (int o = 0; o < 100; o++) {
                System.out.print((distance[o]) + " ,");
            }

            System.out.println("\n");
            buttonOrder[1] = picNo;
            for (int u = 2; u < 101; u++) {
                double smallest = 50000000;
                int smallestIndex = -1;
                for (int k = 0; k < 100; k++) {
                    if (distance[k] < smallest && distance[k] != 0) {
                        smallest = distance[k];
                        smallestIndex = k;
                    }
                }
                distance[smallestIndex] = 0;
                ++smallestIndex;
                //System.out.println("Smallest Index is " + smallestIndex);
                buttonOrder[u] = smallestIndex;
            }
            for (int u = 1; u < 101; u++) {
                System.out.print(buttonOrder[u] + ", ");
            }
            imageCount = 1;
            displayFirstPage();
        }
    }
}
