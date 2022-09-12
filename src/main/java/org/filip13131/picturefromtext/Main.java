package org.filip13131.picturefromtext;

import org.apache.commons.io.IOUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        //https://stackoverflow.com/questions/6001211/format-of-type-int-rgb-and-type-int-argb
        BufferedImage image = ImageIO.read(new File("posterWB3.png"));
        System.out.println(image.getHeight());
        System.out.println(image.getWidth());
        String everything;
        FileInputStream inputStream = new FileInputStream("ATYD.txt");
        try {
            everything = IOUtils.toString(inputStream);
        } finally {
            inputStream.close();
        }
        try {
            File myObj = new File("target/test-outputs/SimpleGenerated.pdf");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        SimplePdfCreator creator = new SimplePdfCreator(new FileOutputStream("target/test-outputs/SimpleGenerated.pdf"));


        List<Integer> lineOfPixels = new ArrayList<>();
        int charCount =0;
        List<List<Integer>> grid = new ArrayList<>();

        for (int y = 0; y< image.getHeight(); y++){
            List<Integer> Line0 = new ArrayList<>();
            List<Integer> Line1 = new ArrayList<>();
            List<Integer> Line2 = new ArrayList<>();
            List<Integer> Line3 = new ArrayList<>();

            for (int x = 0; x<image.getWidth(); x++){
                int argb = image.getRGB(x,y);

                lineOfPixels=Main.getPositionsInAPixel(argb & 0xFF);
                for (int i =0 ; i<4; i++){
                    for(int j = 0 ; j<4; j++){

                        if (lineOfPixels.get(j+i*4)==1){
                            switch (i){
                                case 0:
                                    Line0.add(1);
                                    break;
                                case 1:
                                    Line1.add(1);
                                    break;
                                case 2:
                                    Line2.add(1);
                                    break;
                                case 3:
                                    Line3.add(1);
                                    break;


                            }

//                            creator.print(10+(x*0.6*4)+(j*0.6) , 2370 - (y*0.6*4) - (i*0.6) ,"X");
                            charCount++;
                        }else{
                            switch (i){
                                case 0:
                                    Line0.add(0);
                                    break;
                                case 1:
                                    Line1.add(0);
                                    break;
                                case 2:
                                    Line2.add(0);
                                    break;
                                case 3:
                                    Line3.add(0);
                                    break;


                            }
                        }

                    }
                }
                //creator.print(10 + (x*5*0.6) , 2370 - (y*5*0.6), "X");
            }
            grid.add(Line0);
            grid.add(Line1);
            grid.add(Line2);
            grid.add(Line3);
        }
        int currentIndex = 0;
        for (int i =0 ; i< grid.size(); i++){
            StringBuilder line = new StringBuilder();
            for(int j = 0 ; j < grid.get(i).size(); j++){

                if (currentIndex<everything.length()) {
                    if (grid.get(i).get(j) == 1) {
                        line.append(everything.charAt(currentIndex));
                        currentIndex++;

                    } else {
                        line.append(" ");
                    }
                }
            }
            double y = 1927;
            double ix6;
            ix6= i*0.6;
            BigDecimal asdf = new BigDecimal(ix6).setScale(2, RoundingMode.HALF_UP);
            ix6 = asdf.doubleValue();
            System.out.println(ix6);
            y-=ix6;
            System.out.println(y);
            asdf = new BigDecimal(y).setScale(2, RoundingMode.HALF_UP);
            y = asdf.doubleValue();
            System.out.println(y);
            creator.print(114.8 , y, line.toString());
        }
        System.out.println(charCount);
        creator.storePage();


        creator.close();

    }

    public static List<Integer> getPositionsInAPixel(int darkness){
        List<Integer> xyz = new ArrayList<>(9);

        for( int i=0; i<16; i++){
            xyz.add(i);
        }
        Collections.shuffle(xyz);
        List<Integer> Positions = new ArrayList<>(9);
        for( int i=0; i<16; i++){
            Positions.add(0);
        }
        darkness = 255 - darkness;
        int numberOfPixFilled = darkness/16;
        if(numberOfPixFilled<2){
            numberOfPixFilled+=1;
        }
        if(numberOfPixFilled==15){
            numberOfPixFilled+=1;
        }
        if(numberOfPixFilled<15){
            numberOfPixFilled+=2;
        }
        if( numberOfPixFilled>5 && numberOfPixFilled <16){
            numberOfPixFilled+=1;
        }
        if( numberOfPixFilled>7 && numberOfPixFilled <16){
            numberOfPixFilled+=1;
        }
        if(numberOfPixFilled==15){
            numberOfPixFilled+=1;
        }
        if( numberOfPixFilled>12 && numberOfPixFilled <15){
            numberOfPixFilled+=2;
        }
        for ( int i =0 ; i<numberOfPixFilled; i++){
            Positions.set(xyz.get(i) , 1);
        }

        return Positions;
    }
}
