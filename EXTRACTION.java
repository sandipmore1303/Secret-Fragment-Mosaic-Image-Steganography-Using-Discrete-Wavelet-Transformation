
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import GiciException.ErrorException;
import GiciException.ParameterException;
import GiciFile.LoadFile;
import GiciFile.SaveFile;
import Jama.Matrix;
import Jama.SingularValueDecomposition;
import TER.TERDisplayFrame.DisplayFrameParser;
import TER.TERInteractiveDecoder.InteractiveDecoder;
import TER.TERcoder.ArgsParserCoder;
import TER.TERcoder.Coder;
import ij.io.OpenDialog;
import java.awt.Graphics2D;
import java.awt.Panel;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author sam
 */
public class EXTRACTION {

    private static BufferedImage image_oi;
    private static BufferedImage image_Secrete;
    private static BufferedImage image_Target;
    private static int row_tile_size = 8;
    private static int col_tile_size = 8;
    private static BufferedImage F, F_eq3, F_0, F_90, F_180, F_270;
    private static BufferedImage F_Target;
    private static BufferedImage F_rec_from_Tiles,F_stego;
    private static BufferedImage F_Target__rec_from_Tiles;
    private static OpenDialog   target_i;
    private static int lim_c;
    private static int lim_r;
    private static int TOTAL_NO_BLOKS;
    private static selectionsort Target;
    private static selectionsort Secrete;
    private static double[] meanRed_Secrete;
    private static double[] meanGreen_Secrete;
    private static double[] meanBlue_Secrete;
    private static double[] std_dev_Red_Secrete;
    private static double[] std_dev_Green_Secrete;
    private static double[] std_dev_Blue_Secrete;
    private static double[] avg_std_dev_Secrete;
    private static double[] meanRed_Target;
    private static double[] meanGreen_Target;
    private static double[] meanBlue_Target;
    private static double[] std_dev_Red_Target;
    private static double[] std_dev_Green_Target;
    private static double[] std_dev_Blue_Target;
    private static double[] avg_std_dev_Target;
    
   private static int max_red_res_length=0;
   private static int max_green_res_length=0;
   private static int max_blue_res_length=0;
   private static int max_red_std_dev_length=0;
   private static int max_green_std_dev_length=0;
   private static int max_blue_std_dev_length=0;
  
   private static double max_red_std_dev=0;
   private static double max_green_std_dev=0;
   private static double max_blue_std_dev=0;
  
 private static double min_red_std_dev=10000000;
 private static double min_green_std_dev=10000000;
 private static double min_blue_std_dev=10000000;
 
 private static double max_std_dev=99999999999999999.0;
 
 public static void main(String args[]) throws IOException {
       
        //1 Divide secret image S into a sequence of n tile images of size NT, denoted as Stile =
        //{T1, T2, …, Tn}; and divide target image T into another sequence of n target blocks.
        // select secrete iamge
        target_i = new ij.io.OpenDialog("Select Stego image", "E:\\DATA\\YES_MAN\\JAVA_IMPL\\", "lenna.bmp");
        File f_oi;

        try {
            f_oi = new File(target_i.getDirectory() + target_i.getFileName());
            image_Secrete = ImageIO.read(f_oi);
        } catch (IOException e) {
        }


         

        //transform image using  rotation scaling shifting



        lim_c = image_Secrete.getWidth() - (image_Secrete.getWidth() % col_tile_size);//columns
        lim_r = image_Secrete.getHeight() - (image_Secrete.getHeight() % row_tile_size);//rows
        int blk_no = 0;
        


        for (int j = 0; j < lim_c && j % col_tile_size == 0; j = j + col_tile_size) {
            for (int i = 0; i < lim_r && i % row_tile_size == 0; i = i + row_tile_size) {
                blk_no++;
            }
        }
   
        TOTAL_NO_BLOKS = blk_no;


        
       extract_data();
        System.out.println("END OF ALG");

    }
 private static void extract_data() {
        List<String> readFile_red=null;
        try {
             readFile_red = readFile(target_i.getDirectory()+"_RED.txt"); 
        } catch (Exception ex) {
            Logger.getLogger(EXTRACTION.class.getName()).log(Level.SEVERE, null, ex);
        }
        Iterator<String> iterator_r = readFile_red.listIterator();
        Iterator<String> iterator_c_r = readFile_red.listIterator();
     
      //line 1: no of bits to encode block nos.
         String no_of_bits_to_encode_block_nos_r= iterator_r.next();
         //System.out.println(no_of_bits_to_encode_block_nos);
      //line 2: no of bits to encode residual values
         //String no_of_bits_to_encode_residual_valuesr=iterator_red.next();
         //System.out.println(no_of_bits_to_encode_residual_values);
      //line 3 onwards: blk index of secrete
         String data_r=null;
         int data_int_r[]=new int[9];
         int no_of_blks_r=0;
         System.out.println(readFile_red.size());
        while(iterator_c_r.hasNext())
         {no_of_blks_r++;
            String next = iterator_c_r.next();
            System.out.println(no_of_blks_r);
         }
        image_Secrete=new BufferedImage(image_Secrete.getWidth(), image_Secrete.getHeight(), BufferedImage.TYPE_INT_ARGB);
        
        double T_mean_r[]=new double[no_of_blks_r];
        double B_mean_r[]=new double[no_of_blks_r];
        double std_q_r[]=new double[no_of_blks_r];
        double std_dev_T_r[]=new double[no_of_blks_r];
        double std_dev_B_r[]=new double[no_of_blks_r];
        
        int no_of_blks_1_r=0;
        while(iterator_r.hasNext())
         {
             
         data_r=iterator_r.next();
         String[] split = data_r.split(" ");
         
         //System.out.println(data);
         if(split.length==7)
         {System.out.println(no_of_blks_1_r);
         no_of_blks_1_r++;
             for(int i=0;i<split.length;i++)
         {
             data_int_r[i]=Integer.parseInt(split[i],2);
          //System.out.println(i+"  "+split[i]);  
          }
         //4.1 index of block B target
////4.2 optimal rotation angle of T (two bits)
         //data_int[1]
//4.3 mean of T
         T_mean_r[no_of_blks_1_r]=data_int_r[2]*1.0;
//4.4 mean of B
         B_mean_r[no_of_blks_1_r] =data_int_r[3]*1.0;
//4.5 standard deviation of quotients of RGB
        std_q_r[no_of_blks_1_r] =data_int_r[4]*1.0;
//4.6 overflow
         //double CS=data_int[5]*1.0;
//4.7 underflow residual
         //double CL=data_int[6]*1.0;
// 4.8 std_dev_Red_Target
         std_dev_T_r[no_of_blks_1_r]=data_int_r[5]*1.0;
//4.9 std_dev_Red_Secrete
         std_dev_B_r[no_of_blks_1_r] =data_int_r[6]*1.0;   
 //System.out.println("std_dev_B="+std_dev_B[no_of_blks_1]);//+"std_dev_T="+std_dev_T+"T_mean="+T_mean +"B_mean"+B_mean);

        // no_of_blks_1++;
         }
                      
         }
              List<String> readFile_green=null;
        try {
             readFile_green = readFile(target_i.getDirectory()+"_RED.txt"); 
        } catch (Exception ex) {
            Logger.getLogger(EXTRACTION.class.getName()).log(Level.SEVERE, null, ex);
        }
        Iterator<String> iterator_g = readFile_green.listIterator();
        Iterator<String> iterator_c_g = readFile_green.listIterator();
     
      //line 1: no of bits to encode block nos.
         String no_of_bits_to_encode_block_nos_g= iterator_g.next();
         //System.out.println(no_of_bits_to_encode_block_nos);
      //line 2: no of bits to encode residual values
         //String no_of_bits_to_encode_gesidual_valuesr=iterator_ged.next();
         //System.out.println(no_of_bits_to_encode_gesidual_values);
      //line 3 onwards: blk index of secrete
         String data_g=null;
         int data_int_g[]=new int[9];
         int no_of_blks_g=0;
         System.out.println(readFile_green.size());
        while(iterator_c_g.hasNext())
         {no_of_blks_g++;
            String next = iterator_c_g.next();
            System.out.println(no_of_blks_g);
         }
        image_Secrete=new BufferedImage(image_Secrete.getWidth(), image_Secrete.getHeight(), BufferedImage.TYPE_INT_ARGB);
        
        double T_mean_g[]=new double[no_of_blks_g];
        double B_mean_g[]=new double[no_of_blks_g];
        double std_q_g[]=new double[no_of_blks_g];
        double std_dev_T_g[]=new double[no_of_blks_g];
        double std_dev_B_g[]=new double[no_of_blks_g];
        
        int no_of_blks_1_g=0;
        while(iterator_g.hasNext())
         {
             
         data_g=iterator_g.next();
         String[] split = data_g.split(" ");
         
         //System.out.println(data);
         if(split.length==7)
         {System.out.println(no_of_blks_1_g);
         no_of_blks_1_g++;
             for(int i=0;i<split.length;i++)
         {
             data_int_g[i]=Integer.parseInt(split[i],2);
          //System.out.println(i+"  "+split[i]);  
          }
         //4.1 index of block B target
////4.2 optimal rotation angle of T (two bits)
         //data_int[1]
//4.3 mean of T
         T_mean_g[no_of_blks_1_g]=data_int_g[2]*1.0;
//4.4 mean of B
         B_mean_g[no_of_blks_1_g] =data_int_g[3]*1.0;
//4.5 standard deviation of quotients of RGB
        std_q_g[no_of_blks_1_g] =data_int_g[4]*1.0;
//4.6 overflow
         //double CS=data_int[5]*1.0;
//4.7 underflow residual
         //double CL=data_int[6]*1.0;
// 4.8 std_dev_ged_Target
         std_dev_T_g[no_of_blks_1_g]=data_int_g[5]*1.0;
//4.9 std_dev_ged_Secrete
         std_dev_B_g[no_of_blks_1_g] =data_int_g[6]*1.0;   
 //System.out.println("std_dev_B="+std_dev_B[no_of_blks_1]);//+"std_dev_T="+std_dev_T+"T_mean="+T_mean +"B_mean"+B_mean);

        // no_of_blks_1++;
         }
                      
         }
         
 
         List<String> readFile_blue=null;
        try {
             readFile_blue = readFile(target_i.getDirectory()+"_RED.txt"); 
        } catch (Exception ex) {
            Logger.getLogger(EXTRACTION.class.getName()).log(Level.SEVERE, null, ex);
        }
        Iterator<String> iterator_b = readFile_blue.listIterator();
        Iterator<String> iterator_c_b = readFile_blue.listIterator();
     
      //line 1: no of bits to encode block nos.
         String no_of_bits_to_encode_block_nos_b= iterator_b.next();
         //System.out.println(no_of_bits_to_encode_block_nos);
      //line 2: no of bits to encode residual values
         //String no_of_bits_to_encode_besidual_valuesr=iterator_bed.next();
         //System.out.println(no_of_bits_to_encode_besidual_values);
      //line 3 onwards: blk index of secrete
         String data_b=null;
         int data_int_b[]=new int[9];
         int no_of_blks_b=0;
         System.out.println(readFile_blue.size());
        while(iterator_c_b.hasNext())
         {no_of_blks_b++;
            String next = iterator_c_b.next();
            System.out.println(no_of_blks_b);
         }
        image_Secrete=new BufferedImage(image_Secrete.getWidth(), image_Secrete.getHeight(), BufferedImage.TYPE_INT_ARGB);
        
        double T_mean_b[]=new double[no_of_blks_b];
        double B_mean_b[]=new double[no_of_blks_b];
        double std_q_b[]=new double[no_of_blks_b];
        double std_dev_T_b[]=new double[no_of_blks_b];
        double std_dev_B_b[]=new double[no_of_blks_b];
        
        int no_of_blks_1_b=0;
        while(iterator_b.hasNext())
         {
             
         data_b=iterator_b.next();
         String[] split = data_b.split(" ");
         
         //System.out.println(data);
         if(split.length==7)
         {System.out.println(no_of_blks_1_b);
         no_of_blks_1_b++;
             for(int i=0;i<split.length;i++)
         {
             data_int_b[i]=Integer.parseInt(split[i],2);
          //System.out.println(i+"  "+split[i]);  
          }
         //4.1 index of block B target
////4.2 optimal rotation angle of T (two bits)
         //data_int[1]
//4.3 mean of T
         T_mean_b[no_of_blks_1_b]=data_int_b[2]*1.0;
//4.4 mean of B
         B_mean_b[no_of_blks_1_b] =data_int_b[3]*1.0;
//4.5 standard deviation of quotients of RGB
        std_q_b[no_of_blks_1_b] =data_int_b[4]*1.0;
//4.6 overflow
         //double CS=data_int[5]*1.0;
//4.7 underflow residual
         //double CL=data_int[6]*1.0;
// 4.8 std_dev_bed_Target
         std_dev_T_b[no_of_blks_1_b]=data_int_b[5]*1.0;
//4.9 std_dev_bed_Secrete
         std_dev_B_b[no_of_blks_1_b] =data_int_b[6]*1.0;   
 //System.out.println("std_dev_B="+std_dev_B[no_of_blks_1]);//+"std_dev_T="+std_dev_T+"T_mean="+T_mean +"B_mean"+B_mean);

        // no_of_blks_1++;
         }
                      
         }
         
 

         
     // use the extracted means and related standard deviation quotients to recover the original pixel values in Ti according to Eq. 
         int count=-1;
         System.out.println("In extraction method");
         for (int j = 0; j < lim_c && j % col_tile_size == 0; j = j + col_tile_size) {
             
            for (int i = 0; i < lim_r && i % row_tile_size == 0; i = i + row_tile_size) {   //////System.out.println("Block no="+blk_no);
              // BufferedImage F_subimage = F.getSubimage(j, i, col_tile_size, row_tile_size);
                BufferedImage subimage_image_Secrete = image_Secrete.getSubimage(j, i, col_tile_size, row_tile_size);
                int blk_pixelcount = 0;count++;
                 System.out.println(count);
                 for (int mj = 0; mj < col_tile_size; mj++) {
                    for (int mi = 0; mi < row_tile_size; mi++) {
                       int alpha=255;
                        int rgb = subimage_image_Secrete.getRGB(mj, mi);
                        int red = (rgb & 0x00FF0000) >>> 16;
                        int green = (rgb & 0x0000FF00) >>> 8;
                        int blue = (rgb & 0x000000FF) >>> 0;
                        //Csecrete=std_dev_secrete/std_dev_target*(stegograyvalue-mean_target)+mean_secrete;
                        double Csecrete_r =std_dev_B_r[count]/std_dev_T_r[count]*(red*1.0-T_mean_r[count])+B_mean_r[count];                
                        red=(int)Csecrete_r;
                        double Csecrete_g =std_dev_B_g[count]/std_dev_T_g[count]*(green*1.0-T_mean_g[count])+B_mean_g[count];                
                        green=(int)Csecrete_g;
                        double Csecrete_b =std_dev_B_b[count]/std_dev_T_b[count]*(blue*1.0-T_mean_b[count])+B_mean_b[count];                
                        blue=(int)Csecrete_b;
                       //  if(red>255)
                       //      red=255;
                       //  if(red<0)
                         //    red=255;
                        rgb = (alpha << 24) | (red << 16) | (green << 8) | blue;
                        // System.out.println(count);
                         subimage_image_Secrete.setRGB( mj, mi,rgb);
                        
                    }
                }
  
            }
            
         
     }

try {
            ImageIO.write(image_Secrete, "png", new File(target_i.getDirectory()+"_Secrete_Image_Extracted.png"));
        } catch (IOException e) {
        }

 }
 private static List<String> readFile(String filename) throws Exception
  {
    String line = null;
    List<String> records = new ArrayList<String>();
    // wrap a BufferedReader around FileReader
    BufferedReader bufferedReader = new BufferedReader(new FileReader(filename));
    // use the readLine method of the BufferedReader to read one line at a time.
    // the readLine method returns null when there is nothing else to read.
    while ((line = bufferedReader.readLine()) != null)
    {records.add(line);
    }
   
    // close the BufferedReader when we're done
    bufferedReader.close();
    return records;
}
 private static void display_images() 
 {
        JFrame frame_gi = new JFrame("Secrete  image");
        Panel panel_gi = new ShowImage(target_i.getDirectory() + target_i.getFileName());
        frame_gi.getContentPane().add(panel_gi);
        frame_gi.setSize(500, 500);
        frame_gi.setVisible(true);


        JFrame frame_bi = new JFrame("Target image");
        Panel panel_bi = new ShowImage(target_i.getDirectory() + target_i.getFileName());
        frame_bi.getContentPane().add(panel_bi);
        frame_bi.setSize(500, 500);
        frame_bi.setVisible(true);

        JFrame frame_gis = new JFrame("Secrete Tile image");
        Panel panel_gis = new ShowImage(target_i.getDirectory()+target_i.getFileName()+"_F.png");
        frame_gis.getContentPane().add(panel_gis);
        frame_gis.setSize(500, 500);
        frame_gis.setVisible(true);


        JFrame frame_bit = new JFrame("Target tile image");
        Panel panel_bit = new ShowImage(target_i.getDirectory() +target_i.getFileName()+ "_F_Target.png");
        frame_bit.getContentPane().add(panel_bit);
        frame_bit.setSize(500, 500);
        frame_bit.setVisible(true);





        JFrame frame_bi2 = new JFrame("mosaic Target image F after color transformation");
        Panel panel_bi2 = new ShowImage(target_i.getDirectory()+target_i.getFileName()+"_F_Target__rec_from_Tiles.png");
        frame_bi2.getContentPane().add(panel_bi2);
        frame_bi2.setSize(500, 500);
        frame_bi2.setVisible(true);
        
        
        JFrame frame_bi3 = new JFrame("Stego color Image ");
        Panel panel_bi3= new ShowImage(target_i.getDirectory()+target_i.getFileName()+ "_F_stego.png");
        frame_bi3.getContentPane().add(panel_bi3);
        frame_bi3.setSize(500, 500);
        frame_bi3.setVisible(true);


    }
 
 
   
}