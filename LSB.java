
import Jama.Matrix;
import ij.io.OpenDialog;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.imageio.ImageIO;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author sam
 */
public class LSB {    
    
    private static BufferedImage input_img=null;
    private static BufferedImage stego_img=null;
     
     

 int getLSB(int data){
        data=(data & 0x00000001);
       
        return data;
    
}
 static int setLSB(int data,int bit){
int bitm=(bit & 0x00000001);
int datam=(data & 0xFFFFFFFe);

datam=(datam | bitm);

        return datam;
    
}    
LSB(BufferedImage img ){
         input_img=img;
         stego_img=img;
}  
  void LSBEmbedd( String file,int C){
     
        // The name of the file to open.
        String fileName = file;

        // This will reference one line at a time
        int d=0;
     

        try {
            // FileReader reads text files in the default encoding.
            
                

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = 
                new BufferedReader(new FileReader(fileName));

            while((d = bufferedReader.read()) != -1) {
                //System.out.println(d);
                
                int count=0;
         int rgb1=0,red1=0,green1=0,blue1=0;
         
         
         
         
         for(int j=0;j<input_img.getWidth();j++)
            for(int i=0;i<input_img.getHeight();i++)  
             {  int alpha=255;
                rgb1=input_img.getRGB(j, i);
                red1=(rgb1 & 0x00FF0000)  >>> 16;   
                
                green1=(rgb1 & 0x0000FF00)  >>> 8;
                
                blue1=(rgb1 & 0x000000FF)  >>> 0;
                
                if(C==1 && d==48)red1=setLSB(red1, 0);
                if(C==1 && d==49)red1=setLSB(red1, 1);
               
                if(C==2 && d==48) green1=setLSB(green1, 0);
                if(C==2 && d==49) green1=setLSB(green1, 1);
              
                if(C==3 && d==48) blue1=setLSB(blue1, 0);
                if(C==3 && d==49) blue1=setLSB(blue1, 1);
                
                
                rgb1=(alpha << 24) | (red1 << 16) | (green1 << 8) | blue1; 
                //rgb2=
                stego_img.setRGB(j, i, rgb1);
                
             }
                 
            }	

            // Always close files.
            bufferedReader.close();			
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");				
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");					
            // Or we could just do this: 
            // ex.printStackTrace();
        }
      
         
             
//3) Mark the image by simple overwriting the bits identiﬁed in 2a and 2b with the bits of the watermark (payload and bits
//saved i n 2c).
   
         
    }
   
     private void saveTargetImage(String filneame) {
        try {    
          ImageIO.write(stego_img, "png", new File(filneame));
      }catch (IOException e) {}     
   
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
     public static void main (String args[])   {  
        //int ambiguisPairs = getAmbiguisPairs();
//         LSB nn=new LSB();
         System.out.println(setLSB(255,0));
         /*
         OpenDialog secrete_i;
         OpenDialog target_i;

         //System.out.println(setLSB(2,1));
         secrete_i = new ij.io.OpenDialog("Select  image", "E:\\DATA\\YES_MAN\\JAVA_IMPL\\", "lenna.bmp");
         File f_oi;
         BufferedImage image_Secrete = null;
         try {
             f_oi = new File(secrete_i.getDirectory() + secrete_i.getFileName());
             image_Secrete = ImageIO.read(f_oi);
         } catch (IOException e) {
         }

         BufferedImage image_Target = null;
         try {
             f_oi = new File(secrete_i.getDirectory() + secrete_i.getFileName());
             image_Target = ImageIO.read(f_oi);
         } catch (IOException e) {
         }

        //target_i=new ij.io.OpenDialog("Select text file","E:\\DATA\\YES_MAN\\JAVA_IMPL\\", "lenna.bmp");
         //f_oi=new File();
         LSB LSBObj = new LSB(image_Secrete);
         System.out.println("d");
         LSBObj.LSBEmbedd("RED.txt", 1);
         LSBObj.LSBEmbedd("GREEN.txt", 2);
         LSBObj.LSBEmbedd("BLUE.txt", 3);
         //LSBObj.saveTargetImage(secrete_i.getDirectory()+secrete_i.getFileName().substring(0,secrete_i.getFileName().indexOf("."))+"_LSB.png");
         LSBObj.saveTargetImage("LSB.png");
         System.out.println("d");
         */
     }


   
}


