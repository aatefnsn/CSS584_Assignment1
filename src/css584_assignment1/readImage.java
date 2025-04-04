/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package css584_assignment1;

/**
 *
 * @author ahmed_nada
 */
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.lang.Object.*;
import javax.swing.*;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class readImage {

    int imageCount = 1; // counter to keep track of number of pictures
    int intensityBins[] = new int[25]; // intensity bin array for each picture
    int intensityMatrix[][] = new int[100][25]; // intensity bin matrix for al l00 pictures
    int colorCodeBins[] = new int[64]; // color code bin array for each picture
    int colorCodeMatrix[][] = new int[100][64]; // color code bin for all 100 pictures

    public readImage() {
        BufferedImage image = null;
        while (imageCount < 101) { // counter to get the whole 100 pictures in images folder
            try {
                image = ImageIO.read(new File("images/" + imageCount + ".jpg")); // read each image sequential
                //System.out.println("imageCount is " + imageCount);
                JFrame frame = new JFrame(); // create new frame
                frame.getContentPane().setLayout(new FlowLayout());
                frame.getContentPane().add(new JLabel(new ImageIcon(image)));
                frame.pack();
                int width = image.getWidth(); // get picture width to be used at getIntensity and getcolorCode functions
                int height = image.getHeight(); // get picture height to be used at getIntensity and getcolorCode functions
                //System.out.println("image width is " + width);
                //System.out.println("image height is " + height);
                //System.out.println("image width * height is " + height*width);
                getIntensity(image, height, width, imageCount); // call the getintensity function on each read image
                getColorCode(image, height, width, imageCount); // call the getColorcode function on each read image
                imageCount++; // increment the imageCount counter
            } catch (IOException e) {
                System.out.println("Error occurred when reading the file."); // printed if an exception is caught
            }
        }
        //System.out.println("imageCount is " + imageCount);
        System.out.println("Insity Matrix is \n");
        for (int o = 0; o < imageCount - 1; o++) { // for loop to print the intensity matrix
            //System.out.println(Arrays.toString(intensityMatrix[o]));
        }
        System.out.println("Color Code Matrix is \n");
        for (int o = 0; o < imageCount - 1; o++) { // for loop to print the color code matrix
            //System.out.println(Arrays.toString(colorCodeMatrix[o]));
        }

        writeIntensity(); // call the write intensity function to write the intensity matrix into text file
        writeColorCode(); // call the write color code function to write the color code matrix into text file
    }

    //intensity method 
    public void getIntensity(BufferedImage image, int height, int width, int imageCount) {
        //System.out.println("imageCount is " + imageCount);
        for (int x = 0; x < width; x = x + 1) { // nested for loop to go over each pixel in each image, going over width
            for (int y = 0; y < height; y = y + 1) { // going over height
                Color mycolor = new Color(image.getRGB(x, y)); // get RGB values in a new color object
                double I = (0.299 * mycolor.getRed()) + (0.587 * mycolor.getGreen()) + (0.114 * mycolor.getBlue()); // calculate the 
                //intensity of the pixel as per the equation in the assignment
                //System.out.println("Intensity is " + I + " for x=" + x + " and y=" + y); // print the intensity of each pixel
                if (I <= 10) { // if statements to create the intinsity bins, increement the bin value if the intensity falls into the bin intensity range
                    intensityBins[0]++;
                } else if (I <= 20) {
                    intensityBins[1]++;
                } else if (I <= 30) {
                    intensityBins[2]++;
                } else if (I <= 40) {
                    intensityBins[3]++;
                } else if (I <= 50) {
                    intensityBins[4]++;
                } else if (I <= 60) {
                    intensityBins[5]++;
                } else if (I <= 70) {
                    intensityBins[6]++;
                } else if (I <= 80) {
                    intensityBins[7]++;
                } else if (I <= 90) {
                    intensityBins[8]++;
                } else if (I <= 100) {
                    intensityBins[9]++;
                } else if (I <= 110) {
                    intensityBins[10]++;
                } else if (I <= 120) {
                    intensityBins[11]++;
                } else if (I <= 130) {
                    intensityBins[12]++;
                } else if (I <= 140) {
                    intensityBins[13]++;
                } else if (I <= 150) {
                    intensityBins[14]++;
                } else if (I <= 160) {
                    intensityBins[15]++;
                } else if (I <= 170) {
                    intensityBins[16]++;
                } else if (I <= 180) {
                    intensityBins[17]++;
                } else if (I <= 190) {
                    intensityBins[18]++;
                } else if (I <= 200) {
                    intensityBins[19]++;
                } else if (I <= 210) {
                    intensityBins[20]++;
                } else if (I <= 220) {
                    intensityBins[21]++;
                } else if (I <= 230) {
                    intensityBins[22]++;
                } else if (I <= 240) {
                    intensityBins[23]++;
                } else if (I <= 255) {
                    intensityBins[24]++;
                }
            }
        }
        //System.out.println("Insity Bin is \n" + Arrays.toString(intensityBins));
        for (int p = 0; p < 25; p++) { // for loop to copy the array into the intensity matrix
            intensityMatrix[imageCount - 1][p] = intensityBins[p]; // copy the intensity bin array for the current image to its row in the bigger intensity matrix
            intensityBins[p] = 0; // reset the intensity matrix before running sgain on the next image
        }
    }

    //color code method to calculate the color code representation of each image
    public void getColorCode(BufferedImage image, int height, int width, int imageCount) {
        for (int x = 0; x < width; x = x + 1) { // nested loop to go over each pixel of the image
            for (int y = 0; y < height; y = y + 1) {
                Color mycolor = new Color(image.getRGB(x, y)); // color that has the RGB values of each pixel

                int redbits = mycolor.getRed(); // get integer represntation of red colour
                //System.out.println("Red bits is "+redbits);
                String redbin = Integer.toBinaryString(redbits); // transform the red integer reprenstation into string
                redbin = (("00000000" + redbin).substring(redbin.length())).substring(0, 2); // transfor the string to the full byte represntation then get the first 2 bits substring
                //System.out.println("First 2 Red bits in binary is "+redbin);

                int greenbits = mycolor.getGreen(); // get integer represntation of green colour
                //System.out.println("Green bits is "+greenbits);
                String greenbin = Integer.toBinaryString(greenbits); // transform the green integer reprenstation into string
                greenbin = (("00000000" + greenbin).substring(greenbin.length())).substring(0, 2); // transfor the string to the full byte represntation then get the first 2 bits substring
                //System.out.println("First 2 Green bits in binary is "+greenbin);

                int bluebits = mycolor.getBlue(); //same as with red and green
                //System.out.println("Blue bits is "+bluebits);
                String bluebin = Integer.toBinaryString(bluebits);
                bluebin = (("00000000" + bluebin).substring(bluebin.length())).substring(0, 2);
                //System.out.println("First 2 Blue bits in binary is "+bluebin);

                String pixelcolorcode = redbin + greenbin + bluebin; // append the strings togehter
                //to form the pixel color code represntation first 2 bits of red, followed by first 2 bits of green followed by first 2 bits of blue
                //System.out.println("Pixel Color Code "+pixelcolorcode);

                int decimalValue = Integer.parseInt(pixelcolorcode, 2); //get the decimal value of the pixel color code
                //System.out.println("Pixel Color Code in decimal is "+decimalValue);
                colorCodeBins[decimalValue]++; // to get the color code bins, increment the bin representating the integer value of the color code
            }
        }
        //System.out.println("Color Code Bin is \n" + Arrays.toString(colorCodeBins));
        for (int p = 0; p < 64; p++) { // for loop to copy the color code bin array into the color code matrix
            colorCodeMatrix[imageCount - 1][p] = colorCodeBins[p];
            colorCodeBins[p] = 0;
        }
    }

    //This method writes the contents of the colorCode matrix to a file named colorCodes.txt.
    public void writeColorCode() {
        System.out.println("Color Code Matrix Columns length " + colorCodeMatrix[0].length);
        System.out.println("Color Code Matrix Rows length " + colorCodeMatrix.length);
        StringBuilder builder = new StringBuilder(); // string builder to be used to write the string representing the color code of one image into a txt file
        for (int i = 0; i < colorCodeMatrix.length; i++)//for each row
        {
            for (int j = 0; j < colorCodeMatrix[0].length; j++)//for each column
            {
                builder.append(colorCodeMatrix[i][j] + "");//append to the output string
                if (j < colorCodeMatrix.length - 1)//if this is not the last row element
                {
                    builder.append(",");//then add comma
                }
            }
            builder.append("\n");//append new line at the end of the row
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("colorCodes.txt")); // create and open a new file writer in a file named colorCodes.txt
            writer.write(builder.toString());//save the string representation of the board
            writer.close(); // close the buffered writer
            System.out.println("colorCodes.txt is closed and ready for parsing");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //This method writes the contents of the intensity matrix to a file called intensity.txt
    public void writeIntensity() {
        System.out.println("Intensity Matrix Columns length " + intensityMatrix[0].length);
        System.out.println("Intensity Matrix Rows length " + intensityMatrix.length);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < intensityMatrix.length; i++)//for each row
        {
            for (int j = 0; j < intensityMatrix[0].length; j++)//for each column
            {
                builder.append(intensityMatrix[i][j] + "");//append to the output string
                if (j < intensityMatrix.length - 1)//if this is not the last row element
                {
                    builder.append(",");//then add comma
                }
            }
            builder.append("\n");//append new line at the end of the row
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("intensity.txt")); // create and open a new file writer in a file named intensity.txt
            writer.write(builder.toString());//save the string representation of the board
            writer.close(); // close the buffered writer
            System.out.println("intensity.txt is closed and ready for parsing");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new readImage(); // create a new object of the readimage to get the 
        System.exit(0); // exit after the read the images, creating the intensity and color codes matrixes then writing them into their respective files
    }
}
