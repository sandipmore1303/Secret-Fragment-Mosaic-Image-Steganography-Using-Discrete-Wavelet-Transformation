
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
public class Secret_fragment_visible_mosaic_image_creation_Modular {

    private static BufferedImage image_oi;
    private static BufferedImage image_Secrete;
    private static BufferedImage image_Target;
    private static int row_tile_size = 8;
    private static int col_tile_size = 8;
    private static BufferedImage F, F_eq3, F_0, F_90, F_180, F_270;
    private static BufferedImage F_Target;
    private static BufferedImage F_rec_from_Tiles,F_stego;
    private static BufferedImage F_Target__rec_from_Tiles;
    private static OpenDialog secrete_i, target_i;
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
        stage1_1();
        stage1_2();
        //System.out.println("END OF ALG");

    }

    static void stage1_1() {

        //1 Divide secret image S into a sequence of n tile images of size NT, denoted as Stile =
//{T1, T2, …, Tn}; and divide target image T into another sequence of n target blocks.
        // select secrete iamge
        secrete_i = new ij.io.OpenDialog("Select Secrete image", "E:\\DATA\\YES_MAN\\JAVA_IMPL\\", "lenna.bmp");
        File f_oi;

        try {
            f_oi = new File(secrete_i.getDirectory() + secrete_i.getFileName());
            image_Secrete = ImageIO.read(f_oi);
        } catch (IOException e) {
        }


        // select target iamge
        target_i = new ij.io.OpenDialog("Select Target image", "E:\\DATA\\YES_MAN\\JAVA_IMPL\\", "lenna.bmp");

        try {
            f_oi = new File(target_i.getDirectory() + target_i.getFileName());
            image_Target = ImageIO.read(f_oi);
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



        meanRed_Secrete = new double[TOTAL_NO_BLOKS];
        meanGreen_Secrete = new double[TOTAL_NO_BLOKS];
        meanBlue_Secrete = new double[TOTAL_NO_BLOKS];
        std_dev_Red_Secrete = new double[TOTAL_NO_BLOKS];
        std_dev_Green_Secrete = new double[TOTAL_NO_BLOKS];
        std_dev_Blue_Secrete = new double[TOTAL_NO_BLOKS];
        avg_std_dev_Secrete = new double[TOTAL_NO_BLOKS];


        meanRed_Target = new double[TOTAL_NO_BLOKS];
        meanGreen_Target = new double[TOTAL_NO_BLOKS];
        meanBlue_Target = new double[TOTAL_NO_BLOKS];
        std_dev_Red_Target = new double[TOTAL_NO_BLOKS];
        std_dev_Green_Target = new double[TOTAL_NO_BLOKS];
        std_dev_Blue_Target = new double[TOTAL_NO_BLOKS];
        avg_std_dev_Target = new double[TOTAL_NO_BLOKS];


        blk_no = 0;

        for (int j = 0; j < lim_c && j % col_tile_size == 0; j = j + col_tile_size) {
            for (int i = 0; i < lim_r && i % row_tile_size == 0; i = i + row_tile_size) {   //////System.out.println("Block no="+blk_no);
                BufferedImage subimage_image_Secrete = image_Secrete.getSubimage(j, i, col_tile_size, row_tile_size);
                BufferedImage subimage_image_Target = image_Target.getSubimage(j, i, col_tile_size, row_tile_size);

                //secrete image
                //mean 

                meanRed_Secrete[blk_no] = mean(subimage_image_Secrete, 1);
                meanGreen_Secrete[blk_no] = mean(subimage_image_Secrete, 2);
                meanBlue_Secrete[blk_no] = mean(subimage_image_Secrete, 3);

                //std dev 
                std_dev_Red_Secrete[blk_no] = std_dev(subimage_image_Secrete, 1);
                std_dev_Green_Secrete[blk_no] = std_dev(subimage_image_Secrete, 2);
                std_dev_Blue_Secrete[blk_no] = std_dev(subimage_image_Secrete, 3);

                //avg std dev
                avg_std_dev_Secrete[blk_no] = (std_dev_Red_Secrete[blk_no] + std_dev_Green_Secrete[blk_no] + std_dev_Blue_Secrete[blk_no]) / 3.0;


                //target image
                //mean 
                meanRed_Target[blk_no] = mean(subimage_image_Target, 1);
                meanGreen_Target[blk_no] = mean(subimage_image_Target, 2);
                meanBlue_Target[blk_no] = mean(subimage_image_Target, 3);

                //std dev 
                std_dev_Red_Target[blk_no] = std_dev(subimage_image_Target, 1);
                std_dev_Green_Target[blk_no] = std_dev(subimage_image_Target, 2);
                std_dev_Blue_Target[blk_no] = std_dev(subimage_image_Target, 3);

                //avg std dev
                avg_std_dev_Target[blk_no] = (std_dev_Red_Target[blk_no] + std_dev_Green_Target[blk_no] + std_dev_Blue_Target[blk_no]) / 3.0;

                
                
                blk_no++;
            }
        }



        //Sort the blocks in  according to the average standard deviation
//values of the blocks; map in order the blocks in the sorted S
        //to those in the
//sorted   sorted Starget in a 1-to-1 manner; and reorder the mappings according to the indices of the tile images into a mapping sequence L of the form of T1 → Bj , T2 → Bj , etc   

        //sort according to avg _std_dev 
        //  sort Secrete
        Secrete = new selectionsort(avg_std_dev_Secrete);
        //Secrete.dispaly();
        //sort target
        Target = new selectionsort(avg_std_dev_Target);
        //Target.dispaly();

        // int mappingsequenceL[]=new int [blk_no];  
        //create mosaic image F from T as per sorted order

        //convert to 1D :T and F
        int F_1D_red[][] = new int[TOTAL_NO_BLOKS][row_tile_size * col_tile_size];
        int Secrete_1D_red[][] = new int[TOTAL_NO_BLOKS][row_tile_size * col_tile_size];
        int Target_1D_red[][] = new int[TOTAL_NO_BLOKS][row_tile_size * col_tile_size];

        int F_1D_green[][] = new int[TOTAL_NO_BLOKS][row_tile_size * col_tile_size];
        int Secrete_1D_green[][] = new int[TOTAL_NO_BLOKS][row_tile_size * col_tile_size];
        int Target_1D_green[][] = new int[TOTAL_NO_BLOKS][row_tile_size * col_tile_size];

        int F_1D_blue[][] = new int[TOTAL_NO_BLOKS][row_tile_size * col_tile_size];
        int Secrete_1D_blue[][] = new int[TOTAL_NO_BLOKS][row_tile_size * col_tile_size];
        int Target_1D_blue[][] = new int[TOTAL_NO_BLOKS][row_tile_size * col_tile_size];


        int F_Target_1D_red[][] = new int[TOTAL_NO_BLOKS][row_tile_size * col_tile_size];
        int F_Target_1D_green[][] = new int[TOTAL_NO_BLOKS][row_tile_size * col_tile_size];
        int F_Target_1D_blue[][] = new int[TOTAL_NO_BLOKS][row_tile_size * col_tile_size];

        blk_no = 0;
        int rgb = 0, red = 0, green = 0, blue = 0;
        F = new BufferedImage(image_Target.getWidth(), image_Target.getHeight(), BufferedImage.TYPE_INT_ARGB);
        F_Target = new BufferedImage(image_Target.getWidth(), image_Target.getHeight(), BufferedImage.TYPE_INT_ARGB);

        for (int j = 0; j < lim_c && j % col_tile_size == 0; j = j + col_tile_size) {
            for (int i = 0; i < lim_r && i % row_tile_size == 0; i = i + row_tile_size) {   //////System.out.println("Block no="+blk_no);
                BufferedImage F_subimage = F.getSubimage(j, i, col_tile_size, row_tile_size);
                BufferedImage subimage_image_Secrete = image_Secrete.getSubimage(j, i, col_tile_size, row_tile_size);
                BufferedImage subimage_image_Target = image_Target.getSubimage(j, i, col_tile_size, row_tile_size);
                int blk_pixelcount = 0;
                for (int mj = 0; mj < col_tile_size; mj++) {
                    for (int mi = 0; mi < row_tile_size; mi++) {
                        rgb = subimage_image_Secrete.getRGB(mj, mi);
                        red = (rgb & 0x00FF0000) >>> 16;
                        green = (rgb & 0x0000FF00) >>> 8;
                        blue = (rgb & 0x000000FF) >>> 0;
                        // F_1D_red[blk_no][blk_pixelcount]=red;
                        // F_1D_green[blk_no][blk_pixelcount]=green;
                        //F_1D_blue[blk_no][blk_pixelcount]=blue;

                        rgb = subimage_image_Secrete.getRGB(mj, mi);
                        red = (rgb & 0x00FF0000) >>> 16;
                        green = (rgb & 0x0000FF00) >>> 8;
                        blue = (rgb & 0x000000FF) >>> 0;
                        Secrete_1D_red[blk_no][blk_pixelcount] = red;
                        Secrete_1D_green[blk_no][blk_pixelcount] = green;
                        Secrete_1D_blue[blk_no][blk_pixelcount] = blue;

                        rgb = subimage_image_Target.getRGB(mj, mi);
                        red = (rgb & 0x00FF0000) >>> 16;
                        green = (rgb & 0x0000FF00) >>> 8;
                        blue = (rgb & 0x000000FF) >>> 0;
                        Target_1D_red[blk_no][blk_pixelcount] = red;
                        Target_1D_green[blk_no][blk_pixelcount] = green;
                        Target_1D_blue[blk_no][blk_pixelcount] = blue;



                        blk_pixelcount++;
                    }
                }
                //get next subimage of Secrete as per sorted order
                // F_subimage.set
                //BufferedImage subimage_image_Secrete = image_Secrete.getSubimage(j, i, col_tile_size, row_tile_size);
                blk_no++;
            }
        }

        //now form Image F 



        for (blk_no = 0; blk_no < TOTAL_NO_BLOKS; blk_no++) { //get  next block of secrete image into F
            for (int blk_pixelcount = 0; blk_pixelcount < (col_tile_size * row_tile_size); blk_pixelcount++) {
                F_1D_red[blk_no][blk_pixelcount] = Secrete_1D_red[Secrete.INDEX[blk_no]][blk_pixelcount];
                F_1D_green[blk_no][blk_pixelcount] = Secrete_1D_green[Secrete.INDEX[blk_no]][blk_pixelcount];
                F_1D_blue[blk_no][blk_pixelcount] = Secrete_1D_blue[Secrete.INDEX[blk_no]][blk_pixelcount];


                F_Target_1D_red[blk_no][blk_pixelcount] = Target_1D_red[Target.INDEX[blk_no]][blk_pixelcount];
                F_Target_1D_green[blk_no][blk_pixelcount] = Target_1D_green[Target.INDEX[blk_no]][blk_pixelcount];
                F_Target_1D_blue[blk_no][blk_pixelcount] = Target_1D_blue[Target.INDEX[blk_no]][blk_pixelcount];

            }
        }

        //convert F to 2D
        blk_no = 0;
        for (int j = 0; j < lim_c && j % col_tile_size == 0; j = j + col_tile_size) {
            for (int i = 0; i < lim_r && i % row_tile_size == 0; i = i + row_tile_size) {   //////System.out.println("Block no="+blk_no);
                BufferedImage F_subimage = F.getSubimage(j, i, col_tile_size, row_tile_size);
                BufferedImage F_Target_subimage = F_Target.getSubimage(j, i, col_tile_size, row_tile_size);
                int blk_pixelcount = 0;
                for (int mj = 0; mj < col_tile_size; mj++) {
                    for (int mi = 0; mi < row_tile_size; mi++) {
                        int alpha = 255;
                        red = F_1D_red[blk_no][blk_pixelcount];
                        green = F_1D_green[blk_no][blk_pixelcount];
                        blue = F_1D_blue[blk_no][blk_pixelcount];
                        rgb = (alpha << 24) | (red << 16) | (green << 8) | blue;
                        F_subimage.setRGB(mj, mi, rgb);


                        alpha = 255;
                        red = F_Target_1D_red[blk_no][blk_pixelcount];
                        green = F_Target_1D_green[blk_no][blk_pixelcount];
                        blue = F_Target_1D_blue[blk_no][blk_pixelcount];
                        rgb = (alpha << 24) | (red << 16) | (green << 8) | blue;
                        F_Target_subimage.setRGB(mj, mi, rgb);

                        blk_pixelcount++;
                    }
                }
                //get next subimage of Secrete as per sorted order
                // F_subimage.set
                //BufferedImage subimage_image_Secrete = image_Secrete.getSubimage(j, i, col_tile_size, row_tile_size);
                blk_no++;

            }
        }

        //Secrete.dispaly();


        //save F
        try {
            ImageIO.write(F, "png", new File(secrete_i.getDirectory() + "F.png"));
        } catch (IOException e) {
        }

        try {
            ImageIO.write(F_Target, "png", new File(secrete_i.getDirectory() + "F_Target.png"));
        } catch (IOException e) {
        }


        //recover from tiles original images ... secrte and target


    }

    static void stage1_2() throws IOException {
        F_eq3 = new BufferedImage(image_Target.getWidth(), image_Target.getHeight(), BufferedImage.TYPE_INT_ARGB);

        F_90 = new BufferedImage(image_Target.getWidth(), image_Target.getHeight(), BufferedImage.TYPE_INT_ARGB);
        F_180 = new BufferedImage(image_Target.getWidth(), image_Target.getHeight(), BufferedImage.TYPE_INT_ARGB);
        F_270 = new BufferedImage(image_Target.getWidth(), image_Target.getHeight(), BufferedImage.TYPE_INT_ARGB);
        F_0 = new BufferedImage(image_Target.getWidth(), image_Target.getHeight(), BufferedImage.TYPE_INT_ARGB);

        // Always wrap FileReader in BufferedReader.
        //BufferedReader bufferedReader = 
        //  new BufferedReader(new FileReader(fileName));
        //          BufferedWriter bufferedWriter_red = 
//                new BufferedWriter();
        File redf = new File(secrete_i.getDirectory()+"RED.txt");
        if (redf.exists()) {
            redf.delete();
        }
        File greenf = new File(secrete_i.getDirectory()+"GREEN.txt");
        if (greenf.exists()) {
            greenf.delete();
        }
        File bluef = new File(secrete_i.getDirectory()+"BLUE.txt");
        if (bluef.exists()) {
            bluef.delete();
        }
 File r=new File(secrete_i.getDirectory()+"RED.txt");
 File g=new File(secrete_i.getDirectory()+"GREEN.txt");
 File b=new File(secrete_i.getDirectory()+"BLUE.txt");
 FileWriter fileWriter_red = new FileWriter(r);
 FileWriter fileWriter_green = new FileWriter(g);
 FileWriter fileWriter_blue = new FileWriter(b);
 
 boolean overflow_red=false;
 boolean overflow_green=false;
 boolean overflow_blue=false;
 
 boolean underflow_red=false;
 boolean underflow_green=false;
 boolean underflow_blue=false;
 
  
 
 int blk_no = 0;
 
 int rgb = 0, red = 0, green = 0, blue = 0;
 for (int j = 0; j < lim_c && j % col_tile_size == 0; j = j + col_tile_size) {
    for (int i = 0; i < lim_r && i % row_tile_size == 0; i = i + row_tile_size) {   ////////System.out.println("Block no="+blk_no);
                //Stage 1.2－ performing color conversion between the tile images and target blocks.

                /*
                 * 6. For each pair Ti → Bji in mapping sequence L, let the means μc and μc′ of Ti and Bji
                 respectively be represented by 8 bits with values 0~255 and the standard deviation
                 quotients qc = σc′/σc by 7 bits with values 0.1~12.8 where c = r, g, b.
                 * 
                 * 
                 (1) the index of B; (2) the optimal rotation angle of T ; (3) the means of T
            m     and B and the related standard deviation quotients of all color channels; and (4) the
                 overflow/underflow residuals. These data are coded by binary strings respectively as
                 t1t2...tm, r1r2, m1m2...m48, q1q2...q21, and r1...rk, which together with the binary strings

                common for all //binary form m and k value
                
                 per blk //index of block B
                 * // optimal rotation angle of T (two bits)
                 * //mean of T
                 * //mean of B
                 * //standard deviation of quotients

                 * //overflow/underflow residuals
                   */
                //represent means of T i.e F and B in 8 bits 0-255
                //******************************************* to 


                //fileWriter_red.write(Integer.toString(Secrete.INDEX[blk_no])+" ");
                //fileWriter_green.write(Integer.toString(Secrete.INDEX[blk_no])+" ");
                //fileWriter_blue.write(Integer.toString(Secrete.INDEX[blk_no])+" ");



                //////System.out.println("Block no="+blk_no);
                BufferedImage subimage_image_Secrete = F.getSubimage(j, i, col_tile_size, row_tile_size);
                BufferedImage subimage_image_Target = F_Target.getSubimage(j, i, col_tile_size, row_tile_size);

                //secrete image
                //mean 

                meanRed_Secrete[blk_no] = mean(subimage_image_Secrete, 1);
                meanGreen_Secrete[blk_no] = mean(subimage_image_Secrete, 2);
                meanBlue_Secrete[blk_no] = mean(subimage_image_Secrete, 3);

                //std dev 
                std_dev_Red_Secrete[blk_no] = std_dev(subimage_image_Secrete, 1);
                std_dev_Green_Secrete[blk_no] = std_dev(subimage_image_Secrete, 2);
                std_dev_Blue_Secrete[blk_no] = std_dev(subimage_image_Secrete, 3);

                //avg std dev
                avg_std_dev_Secrete[blk_no] = (std_dev_Red_Secrete[blk_no] + std_dev_Green_Secrete[blk_no] + std_dev_Blue_Secrete[blk_no]) / 3.0;



                //target image
                //mean 
                meanRed_Target[blk_no] = mean(subimage_image_Target, 1);
                meanGreen_Target[blk_no] = mean(subimage_image_Target, 2);
                meanBlue_Target[blk_no] = mean(subimage_image_Target, 3);

                //std dev 
                std_dev_Red_Target[blk_no] = std_dev(subimage_image_Target, 1);
                std_dev_Green_Target[blk_no] = std_dev(subimage_image_Target, 2);
                std_dev_Blue_Target[blk_no] = std_dev(subimage_image_Target, 3);

                //avg std dev
                avg_std_dev_Target[blk_no] = (std_dev_Red_Target[blk_no] + std_dev_Green_Target[blk_no] + std_dev_Blue_Target[blk_no]) / 3.0;


                //////////////



                int rsm = (int) meanRed_Secrete[blk_no];
                int gsm = (int) meanGreen_Secrete[blk_no];
                int bsm = (int) meanBlue_Secrete[blk_no];
                //////System.out.println(""+rsm+" "+gsm+" "+bsm);

                toBinary rsm8_Secrete = new toBinary(rsm);
                toBinary gsm8_Secrete = new toBinary(gsm);
                toBinary bsm8_Secrete = new toBinary(bsm);


                int rtm = (int) meanRed_Target[blk_no];
                int gtm = (int) meanGreen_Target[blk_no];
                int btm = (int) meanBlue_Target[blk_no];

                toBinary rtm8_Target = new toBinary(rtm);
                toBinary gtm8_Target = new toBinary(gtm);
                toBinary btm8_Target = new toBinary(btm);

                //represent  standard deviation  quotients qc =qc'/qc by 7 bits 0.1 -12.8 
                //******************************************* to do*****************
            /* 0.1  0  
                 * 0.2  1
                 * 
                 * 12.8  127
                 * 
                 * qc_bin=(qc*10-1)in binary form
                 */
                //std_dev_Red_Target[blk_no]

                double r_std_dev_q = ((std_dev_Red_Target[blk_no] / std_dev_Red_Secrete[blk_no]));
                double g_std_dev_q = ((std_dev_Green_Target[blk_no] / std_dev_Green_Secrete[blk_no]));
                double b_std_dev_q = ((std_dev_Blue_Target[blk_no] / std_dev_Blue_Secrete[blk_no]));

                int r_std_dev_q_e = (int) (10.0 * (std_dev_Red_Target[blk_no] / std_dev_Red_Secrete[blk_no]) - 1.0);
                int g_std_dev_q_e = (int) (10.0 * (std_dev_Green_Target[blk_no] / std_dev_Green_Secrete[blk_no]) - 1.0);
                int b_std_dev_q_e = (int) (10.0 * (std_dev_Blue_Target[blk_no] / std_dev_Blue_Secrete[blk_no]) - 1.0);

                //System.out.println(""+r_std_dev_q*1+" "+g_std_dev_q*1+" "+b_std_dev_q*1);
                
                if(max_red_std_dev<r_std_dev_q && std_dev_Red_Secrete[blk_no]!=0  )
                {
                  max_red_std_dev=r_std_dev_q  ;
                }
                
                if(max_green_std_dev<g_std_dev_q && std_dev_Green_Secrete[blk_no]!=0 )
                { 
                  max_green_std_dev=g_std_dev_q  ;
                }
                 if(max_blue_std_dev<b_std_dev_q && std_dev_Blue_Secrete[blk_no]!=0 )
                {
                  max_blue_std_dev=b_std_dev_q  ;
                }
                 
                 
                 if(min_red_std_dev>r_std_dev_q && std_dev_Red_Secrete[blk_no]!=0  )
                {
                  min_red_std_dev=r_std_dev_q  ;
                }
                
                if(min_green_std_dev>g_std_dev_q && std_dev_Green_Secrete[blk_no]!=0 )
                { 
                  min_green_std_dev=g_std_dev_q  ;
                }
                 if(min_blue_std_dev>b_std_dev_q && std_dev_Blue_Secrete[blk_no]!=0 )
                {
                  min_blue_std_dev=b_std_dev_q  ;
                }
                   
                   if(max_std_dev<std_dev_Red_Target[blk_no] )
                {max_std_dev=std_dev_Red_Target[blk_no];            
                }
                 if(max_std_dev<std_dev_Red_Secrete[blk_no] )
                {max_std_dev=std_dev_Red_Secrete[blk_no];                    
                }  
                  if(max_std_dev<std_dev_Green_Target[blk_no] )
                {max_std_dev=std_dev_Green_Target[blk_no];            
                }
                 if(max_std_dev<std_dev_Green_Secrete[blk_no] )
                {max_std_dev=std_dev_Green_Secrete[blk_no];                    
                }  
                 if(max_std_dev<std_dev_Blue_Target[blk_no] )
                {max_std_dev=std_dev_Blue_Target[blk_no];            
                }
                 if(max_std_dev<std_dev_Blue_Secrete[blk_no] )
                {max_std_dev=std_dev_Blue_Secrete[blk_no];                    
                }  
                ////System.out.println(""+r_std_dev_q_e+" "+g_std_dev_q_e+" "+b_std_dev_q_e);

                //toBinary r7_std_dev_Secrete=new  toBinary(r_std_dev_q_e);
                //toBinary g7_std_dev_Secrete=new  toBinary(g_std_dev_q_e);
                //toBinary b7_std_dev_Secrete=new  toBinary(b_std_dev_q_e);

                /*
                 * 7. For each pixel pi in each tile image Ti of mosaic image F with color value ci where
                 c = r, g, b, transform ci into a new value ci′′ by Eq. (3); and if ci′′ is not smaller
                 than 255 (i.e., if an overflow occurs) or if it is not larger than 0 (i.e., if an
                 underflow occurs), assign ci′′ to be 255 or 0, respectively, and compute a residual
                 value for pixel pi by the way described in Section 3(C).
                 */
                //convert T tile into T'  tile by color transformation

                BufferedImage F_eq3_subimage = F_eq3.getSubimage(j, i, col_tile_size, row_tile_size);
                BufferedImage F_90_subimage = F_90.getSubimage(j, i, col_tile_size, row_tile_size);
                BufferedImage F_180_subimage = F_180.getSubimage(j, i, col_tile_size, row_tile_size);
                BufferedImage F_270_subimage = F_270.getSubimage(j, i, col_tile_size, row_tile_size);
                BufferedImage F_0_subimage = F_0.getSubimage(j, i, col_tile_size, row_tile_size);

                int blk_pixelcount = 0;

                for (int mj = 0; mj < col_tile_size; mj++) {
                    for (int mi = 0; mi < row_tile_size; mi++) {
                        int alpha = 255;
                        rgb = F.getRGB(mj + j, mi + i);
                        red = (rgb & 0x00FF0000) >>> 16;
                        green = (rgb & 0x0000FF00) >>> 8;
                        blue = (rgb & 0x000000FF) >>> 0;


                        //(σ c' / σ c )(ci − μc ) + μc'
                        //red=(int) (((std_dev_Red_Target[Target.INDEX[blk_no]])/(std_dev_Red_Secrete[Secrete.INDEX[blk_no]]))*(red-meanRed_Secrete[Secrete.INDEX[blk_no]])+meanRed_Target[Target.INDEX[blk_no]]);
                        //green=(int) (((std_dev_Green_Target[Target.INDEX[blk_no]])/(std_dev_Green_Secrete[Secrete.INDEX[blk_no]]))*(green-meanGreen_Secrete[Secrete.INDEX[blk_no]])+meanGreen_Target[Target.INDEX[blk_no]]);
                        //blue=(int) (((std_dev_Blue_Target[Target.INDEX[blk_no]])/(std_dev_Blue_Secrete[Secrete.INDEX[blk_no]]))*(blue-meanBlue_Secrete[Secrete.INDEX[blk_no]])+meanBlue_Target[Target.INDEX[blk_no]]);
                 
                        red = (int) ((std_dev_Red_Target[blk_no] / std_dev_Red_Secrete[blk_no]) * (red - meanRed_Secrete[blk_no]) + meanRed_Target[blk_no]);
                        green = (int) ((std_dev_Green_Target[blk_no] / std_dev_Green_Secrete[blk_no]) * (green - meanGreen_Secrete[blk_no]) + meanGreen_Target[blk_no]);
                        blue = (int) ((std_dev_Blue_Target[blk_no] / std_dev_Blue_Secrete[blk_no]) * (blue - meanBlue_Secrete[blk_no]) + meanBlue_Target[blk_no]);

                        //red=(int) (((std_dev_Red_Target[blk_no])/(std_dev_Red_Secrete[blk_no]))*(red-meanRed_Secrete[blk_no]));
                        //green=(int) (((std_dev_Green_Target[blk_no])/(std_dev_Green_Secrete[blk_no]))*(green-meanGreen_Secrete[blk_no]));
                        //blue=(int) (((std_dev_Blue_Target[blk_no])/(std_dev_Blue_Secrete[blk_no]))*(blue-meanBlue_Secrete[blk_no]));

                        //////System.out.println("RED="+red+"  GREEN="+green+"  BLUE="+blue);
                        //overflow
                        overflow_red=false;
                        overflow_green=false;
                        overflow_blue=false;
                        
                        
                        
                        if (red > 255) {
                            overflow_red=true;
                            //red = (int) meanRed_Secrete[blk_no];
                            red = 255;
                        }
                        if (green > 255) {
                            overflow_green=true;
                            //green = (int) meanGreen_Secrete[blk_no];
                            green =255;
                        }
                        if (blue > 255) {
                            overflow_blue=true;
                            //blue = (int) meanBlue_Secrete[blk_no];
                            blue =255;
                        }
                        //underflow
                        underflow_red=false;
                        underflow_green=false;
                        underflow_blue=false;
                        
                        if (red < 0) {
                            underflow_red=true;
                            //red = (int) meanRed_Secrete[blk_no];
                            red=0;
                        }
                        if (green < 0) {
                            underflow_green=true;
                            //green = (int) meanGreen_Secrete[blk_no];
                            green=0;
                        }
                        if (blue < 0) {
                            underflow_blue=true;
                            //blue = (int) meanBlue_Secrete[blk_no];
                            blue=0;
                        }
                     

                        rgb = (alpha << 24) | (red << 16) | (green << 8) | blue;

                        F_eq3_subimage.setRGB(mj, mi, rgb);
                        F_0_subimage.setRGB(mj, mi, rgb);
                        F_90_subimage.setRGB(mj, mi, rgb);
                        F_180_subimage.setRGB(mj, mi, rgb);
                        F_270_subimage.setRGB(mj, mi, rgb);
                        F_Target.setRGB(j + mj, i + mi, rgb);
                        blk_pixelcount++;
                    }
                }
                //Stage 1.3 - rotating the tile images.
//8. Compute the RMSE values of each color-transformed tile image Ti in F with
//respect to its corresponding target block Bji after rotating Ti into the directions 0o,
//90o, 180o and 270o; and rotate Ti into the optimal direction θo with the smallest
//RMSE value.

                BufferedImage rotateMyImage0 = rotate(F_0_subimage, 0.0);
                BufferedImage rotateMyImage90 = rotate(F_90_subimage, 90.0);
                BufferedImage rotateMyImage180 = rotate(F_180_subimage, 180.0);
                BufferedImage rotateMyImage270 = rotate(F_270_subimage, 270.0);
                for (int mj = 0; mj < col_tile_size; mj++) {
                    for (int mi = 0; mi < row_tile_size; mi++) {
                        rgb = rotateMyImage0.getRGB(mj, mi);
                        int alpha = 255;
                        red = (rgb & 0x00FF0000) >>> 16;
                        green = (rgb & 0x0000FF00) >>> 8;
                        blue = (rgb & 0x000000FF) >>> 0;
                        rgb = (alpha << 24) | (red << 16) | (green << 8) | blue;
                        F_0_subimage.setRGB(mj, mi, rgb);


                        rgb = rotateMyImage90.getRGB(mj, mi);
                        alpha = 255;
                        red = (rgb & 0x00FF0000) >>> 16;
                        green = (rgb & 0x0000FF00) >>> 8;
                        blue = (rgb & 0x000000FF) >>> 0;
                        rgb = (alpha << 24) | (red << 16) | (green << 8) | blue;
                        F_90_subimage.setRGB(mj, mi, rgb);


                        rgb = rotateMyImage180.getRGB(mj, mi);
                        alpha = 255;
                        red = (rgb & 0x00FF0000) >>> 16;
                        green = (rgb & 0x0000FF00) >>> 8;
                        blue = (rgb & 0x000000FF) >>> 0;
                        rgb = (alpha << 24) | (red << 16) | (green << 8) | blue;
                        F_180_subimage.setRGB(mj, mi, rgb);


                        rgb = rotateMyImage270.getRGB(mj, mi);
                        alpha = 255;
                        red = (rgb & 0x00FF0000) >>> 16;
                        green = (rgb & 0x0000FF00) >>> 8;
                        blue = (rgb & 0x000000FF) >>> 0;
                        rgb = (alpha << 24) | (red << 16) | (green << 8) | blue;
                        F_270_subimage.setRGB(mj, mi, rgb);

                    }
                }
            
                
                /*
                 * 
                 * Stage 1.4 - embedding the secret image recovery information.
                 9. For each tile image Ti in F, construct a bit stream Mi for recovering Ti as described
                 in Section 3(D), including the bit-segments which encode the data items of: (1)
                 the index of the corresponding target block Bji; (2) the optimal rotation angle θο of
                 T i ; (3) the means of Ti and Bji and the related standard deviation quotients of all
                 color channels; (4) the overflow/underflow residual values in Ti; (5) the number m
                 of bits to encode the index of a block; and (6) the number k of residual values.
                 10. Concatenate the bit streams Mi of all Ti in F in a raster-scan order to form a total
                 bit stream Mt; use the secret key K to encrypt Mt into another bit stream Mt′; and
                 embed Mt′ into F by reversible contrast mapping [7].
                 */

     
                //index of block B
                // optimal rotation angle of T (two bits)
                //mean of T
                //mean of B
                //standard deviation of quotients of RGB
                //overflow/underflow residual+
                //binary form m and k value
                
                int length = Integer.toBinaryString(TOTAL_NO_BLOKS).length();
                String toBinaryString = Integer.toBinaryString(Secrete.INDEX[blk_no]);
                if (toBinaryString.length() < length) {
                    int dif = length - toBinaryString.length();
                    for (int tv = 0; tv < dif; tv++) {
                        String zero = "0";
                        toBinaryString = zero.concat(toBinaryString);
                    }
                }
                //red.write(toBinaryString+" ");
                //green.write(toBinaryString +" ");
                //blue.write(toBinaryString +" ");

                // optimal rotation angle of T (two bits)

                length = Integer.toBinaryString(2).length();
                toBinaryString = Integer.toBinaryString(((int) (100 * Math.random()) % 4));
                if (toBinaryString.length() < length) {
                    int dif = length - toBinaryString.length();
                    for (int tv = 0; tv < dif; tv++) {
                        String zero = "0";
                        toBinaryString = zero.concat(toBinaryString);
                    }
                }

                //red.write(toBinaryString+" " );


                length = Integer.toBinaryString(2).length();
                toBinaryString = Integer.toBinaryString(((int) (100 * Math.random()) % 4));
                if (toBinaryString.length() < length) {
                    int dif = length - toBinaryString.length();
                    for (int tv = 0; tv < dif; tv++) {
                        String zero = "0";
                        toBinaryString = zero.concat(toBinaryString);
                    }
                }

                //green.write(toBinaryString +" ");

                length = Integer.toBinaryString(2).length();
                toBinaryString = Integer.toBinaryString(((int) (100 * Math.random()) % 4));
                if (toBinaryString.length() < length) {
                    int dif = length - toBinaryString.length();
                    for (int tv = 0; tv < dif; tv++) {
                        String zero = "0";
                        toBinaryString = zero.concat(toBinaryString);
                    }
                }

                //blue.write(toBinaryString +" ");

                //mean of T
                
                 length = 8;
                toBinaryString = Integer.toBinaryString(((int) meanRed_Target[blk_no]));
                if (toBinaryString.length() < length) {
                    int dif = length - toBinaryString.length();
                    for (int tv = 0; tv < dif; tv++) {
                        String zero = "0";
                        toBinaryString = zero.concat(toBinaryString);
                    }
                }

                //red.write(toBinaryString+" ");
                
                 length = 8;
                toBinaryString = Integer.toBinaryString(((int) meanGreen_Target[blk_no]));
                if (toBinaryString.length() < length) {
                    int dif = length - toBinaryString.length();
                    for (int tv = 0; tv < dif; tv++) {
                        String zero = "0";
                        toBinaryString = zero.concat(toBinaryString);
                    }
                }

                //green.write(toBinaryString+" ");
                
                
                 length = 8;
                toBinaryString = Integer.toBinaryString(((int) meanBlue_Target[blk_no]));
                if (toBinaryString.length() < length) {
                    int dif = length - toBinaryString.length();
                    for (int tv = 0; tv < dif; tv++) {
                        String zero = "0";
                        toBinaryString = zero.concat(toBinaryString);
                    }
                }

                //blue.write(toBinaryString +" ");

                //mean of B
                length = 8;
                toBinaryString = Integer.toBinaryString(((int) meanRed_Secrete[blk_no]));
                if (toBinaryString.length() < length) {
                    int dif = length - toBinaryString.length();
                    for (int tv = 0; tv < dif; tv++) {
                        String zero = "0";
                        toBinaryString = zero.concat(toBinaryString);
                    }
                }
                //red.write(toBinaryString+" ");
                
                length = 8;
                toBinaryString = Integer.toBinaryString(((int) meanGreen_Secrete[blk_no]));
                if (toBinaryString.length() < length) {
                    int dif = length - toBinaryString.length();
                    for (int tv = 0; tv < dif; tv++) {
                        String zero = "0";
                        toBinaryString = zero.concat(toBinaryString);
                    }
                }
                //green.write(toBinaryString+" " );
                
                length = 8;
                toBinaryString = Integer.toBinaryString(((int) meanBlue_Secrete[blk_no]));
                if (toBinaryString.length() < length) {
                    int dif = length - toBinaryString.length();
                    for (int tv = 0; tv < dif; tv++) {
                        String zero = "0";
                        toBinaryString = zero.concat(toBinaryString);
                    }
                }
                //blue.write(toBinaryString+" ");

                //standard deviation of quotients of RGB
                //s 
                length = 7;
                toBinaryString = Integer.toBinaryString(((int) (100*r_std_dev_q)));
                if (toBinaryString.length() < length) {
                    int dif = length - toBinaryString.length();
                    for (int tv = 0; tv < dif; tv++) {
                        String zero = "0";
                        toBinaryString = zero.concat(toBinaryString);
                    }
                }
                //red.write(toBinaryString+" ");
                
                length = 07;
                toBinaryString = Integer.toBinaryString(((int) (100*g_std_dev_q)));
                if (toBinaryString.length() < length) {
                    int dif = length - toBinaryString.length();
                    for (int tv = 0; tv < dif; tv++) {
                        String zero = "0";
                        toBinaryString = zero.concat(toBinaryString);
                    }
                }
                //fileWriter_green.write(toBinaryString+" ");
                
                length = 7;
                toBinaryString = Integer.toBinaryString(((int) (100*b_std_dev_q)));
                if (toBinaryString.length() < length) {
                    int dif = length - toBinaryString.length();
                    for (int tv = 0; tv < dif; tv++) {
                        String zero = "0";
                        toBinaryString = zero.concat(toBinaryString);
                    }
                }
                //fileWriter_blue.write(toBinaryString +" ");
                //overflow/underflow residual
                //(r_std_dev_q, ,meanBlue_Secrete([blk_no]));
                int[] CsCl =Cs_CL(r_std_dev_q,meanRed_Secrete[blk_no],meanRed_Target[blk_no]);
                
                length = 32;
                toBinaryString = Integer.toBinaryString(((int) (CsCl[0])));
                if (toBinaryString.length() < length) {
                    int dif = length - toBinaryString.length();
                    for (int tv = 0; tv < dif; tv++) {
                        String zero = "0";
                        toBinaryString = zero.concat(toBinaryString);
                    }
                }
                //fileWriter_red.write(toBinaryString+" ");
                
                if(toBinaryString.length()> max_red_res_length)
                    max_red_res_length=toBinaryString.length();
                
                
                length = 32;
                toBinaryString = Integer.toBinaryString(((int) (CsCl[1])));
                if (toBinaryString.length() < length) {
                    int dif = length - toBinaryString.length();
                    for (int tv = 0; tv < dif; tv++) {
                        String zero = "0";
                        toBinaryString = zero.concat(toBinaryString);
                    }
                }
                //red.write(toBinaryString+" ");
                 if(toBinaryString.length()> max_green_res_length)
                    max_green_res_length=toBinaryString.length();
                
                CsCl =Cs_CL(g_std_dev_q,meanGreen_Secrete[blk_no],meanGreen_Target[blk_no]);
                
                length = 32;
                toBinaryString = Integer.toBinaryString(((int) (CsCl[0])));
                if (toBinaryString.length() < length) {
                    int dif = length - toBinaryString.length();
                    for (int tv = 0; tv < dif; tv++) {
                        String zero = "0";
                        toBinaryString = zero.concat(toBinaryString);
                    }
                }
                //green.write(toBinaryString+" ");
                
                 if(toBinaryString.length()> max_blue_res_length)
                    max_blue_res_length=toBinaryString.length();
                
                
                length = 32;
                toBinaryString = Integer.toBinaryString(((int) (CsCl[1])));
                if (toBinaryString.length() < length) {
                    int dif = length - toBinaryString.length();
                    for (int tv = 0; tv < dif; tv++) {
                        String zero = "0";
                        toBinaryString = zero.concat(toBinaryString);
                    }
                }
                //green.write(toBinaryString+" ");
                
                CsCl =Cs_CL(b_std_dev_q,meanBlue_Secrete[blk_no],meanBlue_Target[blk_no]);
                
                length = 32;
                toBinaryString = Integer.toBinaryString(((int) (CsCl[0])));
                if (toBinaryString.length() < length) {
                    int dif = length - toBinaryString.length();
                    for (int tv = 0; tv < dif; tv++) {
                        String zero = "0";
                        toBinaryString = zero.concat(toBinaryString);
                    }
                }
                //blue.write(toBinaryString+" ");
                
                length = 32;
                toBinaryString = Integer.toBinaryString(((int) (CsCl[1])));
                if (toBinaryString.length() < length) {
                    int dif = length - toBinaryString.length();
                    for (int tv = 0; tv < dif; tv++) {
                        String zero = "0";
                        toBinaryString = zero.concat(toBinaryString);
                    }
                }
                //fileWriter_blue.write(toBinaryString+" " );
                
                
                //fileWriter_red.write("\n");
                //fileWriter_green.write("\n");
                //blue.write("\n");
                
                  
                blk_no++;
            }
        }
  
//System.out.println("max_red_res_length="+max_red_res_length+"max_green_res_length="+max_green_res_length+"max_blue_res_length="+max_blue_res_length+"max_red_std_dev="+max_red_std_dev+"max_green_std_dev="+max_green_std_dev+"max_blue_std_dev="+max_blue_std_dev);
//System.out.println("min_red_std_dev="+min_red_std_dev+"min_green_std_dev="+min_green_std_dev+"min_blue_std_dev="+min_blue_std_dev);
 
blk_no = 0;
        rgb = 0;
        red = 0;
        green = 0;
        blue = 0;
        F_rec_from_Tiles = new BufferedImage(image_Target.getWidth(), image_Target.getHeight(), BufferedImage.TYPE_INT_ARGB);
        F_Target__rec_from_Tiles = new BufferedImage(image_Target.getWidth(), image_Target.getHeight(), BufferedImage.TYPE_INT_ARGB);
        F_stego = new BufferedImage(image_Target.getWidth(), image_Target.getHeight(), BufferedImage.TYPE_INT_ARGB);


        //get subimages of tiles into array of BufferedImages
        BufferedImage F_ARRAY[] = new BufferedImage[TOTAL_NO_BLOKS];

        blk_no = 0;
        for (int j = 0; j < lim_c && j % col_tile_size == 0; j = j + col_tile_size) {
            for (int i = 0; i < lim_r && i % row_tile_size == 0; i = i + row_tile_size) {   //////System.out.println("Block no="+blk_no);
                F_ARRAY[Secrete.INDEX[blk_no]] = F.getSubimage(j, i, col_tile_size, row_tile_size);
                blk_no++;
            }
        }

        blk_no = 0;
        for (int j = 0; j < lim_c && j % col_tile_size == 0; j = j + col_tile_size) {
            for (int i = 0; i < lim_r && i % row_tile_size == 0; i = i + row_tile_size) {   //////System.out.println("Block no="+blk_no);
                for (int mj = 0; mj < col_tile_size; mj++) {
                    for (int mi = 0; mi < row_tile_size; mi++) {
                        rgb = F_ARRAY[blk_no].getRGB(mj, mi);
                        F_rec_from_Tiles.setRGB(j + mj, i + mi, rgb);
                        //.setRGB(j + mj, i + mi, rgb);
                        
                        
                    }
                }


                blk_no++;
            }
        }

        //get subimages of target tiles into array of BufferedImages
        BufferedImage[] F_ARRAY1 = new BufferedImage[TOTAL_NO_BLOKS];

        blk_no = 0;
        for (int j = 0; j < lim_c && j % col_tile_size == 0; j = j + col_tile_size) {
            for (int i = 0; i < lim_r && i % row_tile_size == 0; i = i + row_tile_size) {   //////System.out.println("Block no="+blk_no);
                F_ARRAY1[Target.INDEX[blk_no]] = F_Target.getSubimage(j, i, col_tile_size, row_tile_size);
                blk_no++;
            }
        }

        blk_no = 0;
        for (int j = 0; j < lim_c && j % col_tile_size == 0; j = j + col_tile_size) {
            for (int i = 0; i < lim_r && i % row_tile_size == 0; i = i + row_tile_size) {   //////System.out.println("Block no="+blk_no);
                for (int mj = 0; mj < col_tile_size; mj++) {
                    for (int mi = 0; mi < row_tile_size; mi++) {
                        rgb = F_ARRAY1[blk_no].getRGB(mj, mi);
                        F_Target__rec_from_Tiles.setRGB(j + mj, i + mi, rgb);
                        F_stego.setRGB(j + mj, i + mi, rgb);
                    }
                }

                blk_no++;
            }
        }

        //save F
        try {
            ImageIO.write(F_rec_from_Tiles, "png", new File(secrete_i.getDirectory() + "F_rec_from_Tiles.png"));
        } catch (IOException e) {
        }

        try {
            F_Target__rec_from_Tiles = ColorTransform(image_Secrete, image_Target);
            F_stego=ColorTransform(image_Secrete, image_Target);
            ImageIO.write(F_Target__rec_from_Tiles, "png", new File(secrete_i.getDirectory() + "F_Target__rec_from_Tiles.png"));

        } catch (IOException e) {
        }



        //////System.out.println("Size of  Secrete Image:"+"ROWS="+image_Secrete.getHeight()+"\tCOLUMNS="+image_Secrete.getWidth());
        //////System.out.println("Toatal_no_blks="+TOTAL_NO_BLOKS);


        //fileWriter_red.close();
        //fileWriter_blue.close();

        
        //embedd data
 
        //embed_data();
        
          stage1_2_main();
        //String args[]=new String[4];
        //args[0]= "-i";
        //args[1]= secrete_i.getDirectory() + "F_stego.png";	
        //args[2]="-o";
        //args[3]= secrete_i.getDirectory() + "F_stego_compressed.png";
       // TERcode(args);
        //TERdisplay();
        //display_images();
        ////System.out.println("END OF ALG");

    }
    static void stage1_2_main() throws IOException {
        F_eq3 = new BufferedImage(image_Target.getWidth(), image_Target.getHeight(), BufferedImage.TYPE_INT_ARGB);

        F_90 = new BufferedImage(image_Target.getWidth(), image_Target.getHeight(), BufferedImage.TYPE_INT_ARGB);
        F_180 = new BufferedImage(image_Target.getWidth(), image_Target.getHeight(), BufferedImage.TYPE_INT_ARGB);
        F_270 = new BufferedImage(image_Target.getWidth(), image_Target.getHeight(), BufferedImage.TYPE_INT_ARGB);
        F_0 = new BufferedImage(image_Target.getWidth(), image_Target.getHeight(), BufferedImage.TYPE_INT_ARGB);

        // Always wrap FileReader in BufferedReader.
        //BufferedReader bufferedReader = 
        //  new BufferedReader(new FileReader(fileName));
        //          BufferedWriter bufferedWriter_red = 
//                new BufferedWriter();
        File redf = new File(secrete_i.getDirectory()+"RED.txt");
        if (redf.exists()) {
            redf.delete();
        }
        File greenf = new File(secrete_i.getDirectory()+"GREEN.txt");
        if (greenf.exists()) {
            greenf.delete();
        }
        File bluef = new File(secrete_i.getDirectory()+"BLUE.txt");
        if (bluef.exists()) {
            bluef.delete();
        }
 File r=new File(secrete_i.getDirectory()+"RED.txt");
 File g=new File(secrete_i.getDirectory()+"GREEN.txt");
 File b=new File(secrete_i.getDirectory()+"BLUE.txt");
 FileWriter fileWriter_red = new FileWriter(r);
 FileWriter fileWriter_green = new FileWriter(g);
 FileWriter fileWriter_blue = new FileWriter(b);
 
 boolean overflow_red=false;
 boolean overflow_green=false;
 boolean overflow_blue=false;
 
 boolean underflow_red=false;
 boolean underflow_green=false;
 boolean underflow_blue=false;
 
  
   //2 the number m of bits to encode the index of a block
                 
                String toBinaryString = Integer.toBinaryString(TOTAL_NO_BLOKS);
                 
                fileWriter_red.write(toBinaryString+"\n");
                fileWriter_green.write(toBinaryString +"\n");
                fileWriter_blue.write(toBinaryString +"\n");
                //3 the number k of bits to encode residual values
                
                toBinaryString = Integer.toBinaryString(max_red_res_length);                 
                fileWriter_red.write(toBinaryString+"\n");
                
                toBinaryString = Integer.toBinaryString(max_green_res_length);
                fileWriter_green.write(toBinaryString +"\n");
                
                toBinaryString = Integer.toBinaryString(max_blue_res_length);
                fileWriter_blue.write(toBinaryString +"\n");
 int blk_no = 0;
 
 int rgb = 0, red = 0, green = 0, blue = 0;
 for (int j = 0; j < lim_c && j % col_tile_size == 0; j = j + col_tile_size) {
    for (int i = 0; i < lim_r && i % row_tile_size == 0; i = i + row_tile_size) {   ////////System.out.println("Block no="+blk_no);
                //Stage 1.2－ performing color conversion between the tile images and target blocks.

                /*
                 * 6. For each pair Ti → Bji in mapping sequence L, let the means μc and μc′ of Ti and Bji
                 respectively be represented by 8 bits with values 0~255 and the standard deviation
                 quotients qc = σc′/σc by 7 bits with values 0.1~12.8 where c = r, g, b.
                 * 
                 * 
                 (1) the index of B; (2) the optimal rotation angle of T ; (3) the means of T
            m     and B and the related standard deviation quotients of all color channels; and (4) the
                 overflow/underflow residuals. These data are coded by binary strings respectively as
                 t1t2...tm, r1r2, m1m2...m48, q1q2...q21, and r1...rk, which together with the binary strings

                common for all //binary form m and k value
                
                 per blk //index of block B
                 * // optimal rotation angle of T (two bits)
                 * //mean of T
                 * //mean of B
                 * //standard deviation of quotients

                 * //overflow/underflow residuals
                   */
                //represent means of T i.e F and B in 8 bits 0-255
                //******************************************* to 


                //fileWriter_red.write(Integer.toString(Secrete.INDEX[blk_no])+" ");
                //fileWriter_green.write(Integer.toString(Secrete.INDEX[blk_no])+" ");
                //fileWriter_blue.write(Integer.toString(Secrete.INDEX[blk_no])+" ");



                //////System.out.println("Block no="+blk_no);
                BufferedImage subimage_image_Secrete = F.getSubimage(j, i, col_tile_size, row_tile_size);
                BufferedImage subimage_image_Target = F_Target.getSubimage(j, i, col_tile_size, row_tile_size);

                //secrete image
                //mean 

                meanRed_Secrete[blk_no] = mean(subimage_image_Secrete, 1);
                meanGreen_Secrete[blk_no] = mean(subimage_image_Secrete, 2);
                meanBlue_Secrete[blk_no] = mean(subimage_image_Secrete, 3);

                //std dev 
                std_dev_Red_Secrete[blk_no] = std_dev(subimage_image_Secrete, 1);
                std_dev_Green_Secrete[blk_no] = std_dev(subimage_image_Secrete, 2);
                std_dev_Blue_Secrete[blk_no] = std_dev(subimage_image_Secrete, 3);

                //avg std dev
                avg_std_dev_Secrete[blk_no] = (std_dev_Red_Secrete[blk_no] + std_dev_Green_Secrete[blk_no] + std_dev_Blue_Secrete[blk_no]) / 3.0;



                //target image
                //mean 
                meanRed_Target[blk_no] = mean(subimage_image_Target, 1);
                meanGreen_Target[blk_no] = mean(subimage_image_Target, 2);
                meanBlue_Target[blk_no] = mean(subimage_image_Target, 3);

                //std dev 
                std_dev_Red_Target[blk_no] = std_dev(subimage_image_Target, 1);
                std_dev_Green_Target[blk_no] = std_dev(subimage_image_Target, 2);
                std_dev_Blue_Target[blk_no] = std_dev(subimage_image_Target, 3);

                //avg std dev
                avg_std_dev_Target[blk_no] = (std_dev_Red_Target[blk_no] + std_dev_Green_Target[blk_no] + std_dev_Blue_Target[blk_no]) / 3.0;


                //////////////



                int rsm = (int) meanRed_Secrete[blk_no];
                int gsm = (int) meanGreen_Secrete[blk_no];
                int bsm = (int) meanBlue_Secrete[blk_no];
                //////System.out.println(""+rsm+" "+gsm+" "+bsm);

                toBinary rsm8_Secrete = new toBinary(rsm);
                toBinary gsm8_Secrete = new toBinary(gsm);
                toBinary bsm8_Secrete = new toBinary(bsm);


                int rtm = (int) meanRed_Target[blk_no];
                int gtm = (int) meanGreen_Target[blk_no];
                int btm = (int) meanBlue_Target[blk_no];

                toBinary rtm8_Target = new toBinary(rtm);
                toBinary gtm8_Target = new toBinary(gtm);
                toBinary btm8_Target = new toBinary(btm);

                //represent  standard deviation  quotients qc =qc'/qc by 7 bits 0.1 -12.8 
                //******************************************* to do*****************
            /* 0.1  0  
                 * 0.2  1
                 * 
                 * 12.8  127
                 * 
                 * qc_bin=(qc*10-1)in binary form
                 */
                //std_dev_Red_Target[blk_no]

                double r_std_dev_q = ((std_dev_Red_Target[blk_no] / std_dev_Red_Secrete[blk_no]));
                double g_std_dev_q = ((std_dev_Green_Target[blk_no] / std_dev_Green_Secrete[blk_no]));
                double b_std_dev_q = ((std_dev_Blue_Target[blk_no] / std_dev_Blue_Secrete[blk_no]));

                int r_std_dev_q_e = (int) (10.0 * (std_dev_Red_Target[blk_no] / std_dev_Red_Secrete[blk_no]) - 1.0);
                int g_std_dev_q_e = (int) (10.0 * (std_dev_Green_Target[blk_no] / std_dev_Green_Secrete[blk_no]) - 1.0);
                int b_std_dev_q_e = (int) (10.0 * (std_dev_Blue_Target[blk_no] / std_dev_Blue_Secrete[blk_no]) - 1.0);

                 
                ////System.out.println(""+r_std_dev_q_e+" "+g_std_dev_q_e+" "+b_std_dev_q_e);

                //toBinary r7_std_dev_Secrete=new  toBinary(r_std_dev_q_e);
                //toBinary g7_std_dev_Secrete=new  toBinary(g_std_dev_q_e);
                //toBinary b7_std_dev_Secrete=new  toBinary(b_std_dev_q_e);

                /*
                 * 7. For each pixel pi in each tile image Ti of mosaic image F with color value ci where
                 c = r, g, b, transform ci into a new value ci′′ by Eq. (3); and if ci′′ is not smaller
                 than 255 (i.e., if an overflow occurs) or if it is not larger than 0 (i.e., if an
                 underflow occurs), assign ci′′ to be 255 or 0, respectively, and compute a residual
                 value for pixel pi by the way described in Section 3(C).
                 */
                //convert T tile into T'  tile by color transformation

                BufferedImage F_eq3_subimage = F_eq3.getSubimage(j, i, col_tile_size, row_tile_size);
                BufferedImage F_90_subimage = F_90.getSubimage(j, i, col_tile_size, row_tile_size);
                BufferedImage F_180_subimage = F_180.getSubimage(j, i, col_tile_size, row_tile_size);
                BufferedImage F_270_subimage = F_270.getSubimage(j, i, col_tile_size, row_tile_size);
                BufferedImage F_0_subimage = F_0.getSubimage(j, i, col_tile_size, row_tile_size);

                int blk_pixelcount = 0;

                for (int mj = 0; mj < col_tile_size; mj++) {
                    for (int mi = 0; mi < row_tile_size; mi++) {
                        int alpha = 255;
                        rgb = F.getRGB(mj + j, mi + i);
                        red = (rgb & 0x00FF0000) >>> 16;
                        green = (rgb & 0x0000FF00) >>> 8;
                        blue = (rgb & 0x000000FF) >>> 0;


                        //(σ c' / σ c )(ci − μc ) + μc'
                        //red=(int) (((std_dev_Red_Target[Target.INDEX[blk_no]])/(std_dev_Red_Secrete[Secrete.INDEX[blk_no]]))*(red-meanRed_Secrete[Secrete.INDEX[blk_no]])+meanRed_Target[Target.INDEX[blk_no]]);
                        //green=(int) (((std_dev_Green_Target[Target.INDEX[blk_no]])/(std_dev_Green_Secrete[Secrete.INDEX[blk_no]]))*(green-meanGreen_Secrete[Secrete.INDEX[blk_no]])+meanGreen_Target[Target.INDEX[blk_no]]);
                        //blue=(int) (((std_dev_Blue_Target[Target.INDEX[blk_no]])/(std_dev_Blue_Secrete[Secrete.INDEX[blk_no]]))*(blue-meanBlue_Secrete[Secrete.INDEX[blk_no]])+meanBlue_Target[Target.INDEX[blk_no]]);
                 
                        red = (int) ((std_dev_Red_Target[blk_no] / std_dev_Red_Secrete[blk_no]) * (red - meanRed_Secrete[blk_no]) + meanRed_Target[blk_no]);
                        green = (int) ((std_dev_Green_Target[blk_no] / std_dev_Green_Secrete[blk_no]) * (green - meanGreen_Secrete[blk_no]) + meanGreen_Target[blk_no]);
                        blue = (int) ((std_dev_Blue_Target[blk_no] / std_dev_Blue_Secrete[blk_no]) * (blue - meanBlue_Secrete[blk_no]) + meanBlue_Target[blk_no]);

                        //red=(int) (((std_dev_Red_Target[blk_no])/(std_dev_Red_Secrete[blk_no]))*(red-meanRed_Secrete[blk_no]));
                        //green=(int) (((std_dev_Green_Target[blk_no])/(std_dev_Green_Secrete[blk_no]))*(green-meanGreen_Secrete[blk_no]));
                        //blue=(int) (((std_dev_Blue_Target[blk_no])/(std_dev_Blue_Secrete[blk_no]))*(blue-meanBlue_Secrete[blk_no]));

                        //////System.out.println("RED="+red+"  GREEN="+green+"  BLUE="+blue);
                        //overflow
                        overflow_red=false;
                        overflow_green=false;
                        overflow_blue=false;
                        
                        
                        
                        if (red > 255) {
                            overflow_red=true;
                            //red = (int) meanRed_Secrete[blk_no];
                            red = 255;
                        }
                        if (green > 255) {
                            overflow_green=true;
                            //green = (int) meanGreen_Secrete[blk_no];
                            green =255;
                        }
                        if (blue > 255) {
                            overflow_blue=true;
                            //blue = (int) meanBlue_Secrete[blk_no];
                            blue =255;
                        }
                        //underflow
                        underflow_red=false;
                        underflow_green=false;
                        underflow_blue=false;
                        
                        if (red < 0) {
                            underflow_red=true;
                            //red = (int) meanRed_Secrete[blk_no];
                            red=0;
                        }
                        if (green < 0) {
                            underflow_green=true;
                            //green = (int) meanGreen_Secrete[blk_no];
                            green=0;
                        }
                        if (blue < 0) {
                            underflow_blue=true;
                            //blue = (int) meanBlue_Secrete[blk_no];
                            blue=0;
                        }
                     

                        rgb = (alpha << 24) | (red << 16) | (green << 8) | blue;

                        F_eq3_subimage.setRGB(mj, mi, rgb);
                        F_0_subimage.setRGB(mj, mi, rgb);
                        F_90_subimage.setRGB(mj, mi, rgb);
                        F_180_subimage.setRGB(mj, mi, rgb);
                        F_270_subimage.setRGB(mj, mi, rgb);
                        F_Target.setRGB(j + mj, i + mi, rgb);
                        blk_pixelcount++;
                    }
                }
                //Stage 1.3 - rotating the tile images.
//8. Compute the RMSE values of each color-transformed tile image Ti in F with
//respect to its corresponding target block Bji after rotating Ti into the directions 0o,
//90o, 180o and 270o; and rotate Ti into the optimal direction θo with the smallest
//RMSE value.

                BufferedImage rotateMyImage0 = rotate(F_0_subimage, 0.0);
                BufferedImage rotateMyImage90 = rotate(F_90_subimage, 90.0);
                BufferedImage rotateMyImage180 = rotate(F_180_subimage, 180.0);
                BufferedImage rotateMyImage270 = rotate(F_270_subimage, 270.0);
                for (int mj = 0; mj < col_tile_size; mj++) {
                    for (int mi = 0; mi < row_tile_size; mi++) {
                        rgb = rotateMyImage0.getRGB(mj, mi);
                        int alpha = 255;
                        red = (rgb & 0x00FF0000) >>> 16;
                        green = (rgb & 0x0000FF00) >>> 8;
                        blue = (rgb & 0x000000FF) >>> 0;
                        rgb = (alpha << 24) | (red << 16) | (green << 8) | blue;
                        F_0_subimage.setRGB(mj, mi, rgb);


                        rgb = rotateMyImage90.getRGB(mj, mi);
                        alpha = 255;
                        red = (rgb & 0x00FF0000) >>> 16;
                        green = (rgb & 0x0000FF00) >>> 8;
                        blue = (rgb & 0x000000FF) >>> 0;
                        rgb = (alpha << 24) | (red << 16) | (green << 8) | blue;
                        F_90_subimage.setRGB(mj, mi, rgb);


                        rgb = rotateMyImage180.getRGB(mj, mi);
                        alpha = 255;
                        red = (rgb & 0x00FF0000) >>> 16;
                        green = (rgb & 0x0000FF00) >>> 8;
                        blue = (rgb & 0x000000FF) >>> 0;
                        rgb = (alpha << 24) | (red << 16) | (green << 8) | blue;
                        F_180_subimage.setRGB(mj, mi, rgb);


                        rgb = rotateMyImage270.getRGB(mj, mi);
                        alpha = 255;
                        red = (rgb & 0x00FF0000) >>> 16;
                        green = (rgb & 0x0000FF00) >>> 8;
                        blue = (rgb & 0x000000FF) >>> 0;
                        rgb = (alpha << 24) | (red << 16) | (green << 8) | blue;
                        F_270_subimage.setRGB(mj, mi, rgb);

                    }
                }
            
                
                /*
                 * 
                 * Stage 1.4 - embedding the secret image recovery information.
                 9. For each tile image Ti in F, construct a bit stream Mi for recovering Ti as described
                 in Section 3(D), including the bit-segments which encode the data items of: (1)
                 the index of the corresponding target block Bji; (2) the optimal rotation angle θο of
                 T i ; (3) the means of Ti and Bji and the related standard deviation quotients of all
                 color channels; (4) the overflow/underflow residual values in Ti; (5) the number m
                 of bits to encode the index of a block; and (6) the number k of residual values.
                 10. Concatenate the bit streams Mi of all Ti in F in a raster-scan order to form a total
                 bit stream Mt; use the secret key K to encrypt Mt into another bit stream Mt′; and
                 embed Mt′ into F by reversible contrast mapping [7].
                 */

     
                 
                
                 
              
                //4.1 index of block B target
                 
                int length = Integer.toBinaryString(TOTAL_NO_BLOKS).length();
                 toBinaryString = Integer.toBinaryString(Target.INDEX[blk_no]);
                if (toBinaryString.length() < length) {
                    int dif = length - toBinaryString.length();
                    for (int tv = 0; tv < dif; tv++) {
                        String zero = "0";
                        toBinaryString = zero.concat(toBinaryString);
                    }
                }
                fileWriter_red.write(toBinaryString+" ");
                fileWriter_green.write(toBinaryString +" ");
                fileWriter_blue.write(toBinaryString +" ");
                //4.2 optimal rotation angle of T (two bits)
                
                // optimal rotation angle of T (two bits)

                length = Integer.toBinaryString(2).length();
                toBinaryString = Integer.toBinaryString(((int) (100 * Math.random()) % 4));
                if (toBinaryString.length() < length) {
                    int dif = length - toBinaryString.length();
                    for (int tv = 0; tv < dif; tv++) {
                        String zero = "0";
                        toBinaryString = zero.concat(toBinaryString);
                    }
                }

                fileWriter_red.write(toBinaryString+" " );


                length = Integer.toBinaryString(2).length();
                toBinaryString = Integer.toBinaryString(((int) (100 * Math.random()) % 4));
                if (toBinaryString.length() < length) {
                    int dif = length - toBinaryString.length();
                    for (int tv = 0; tv < dif; tv++) {
                        String zero = "0";
                        toBinaryString = zero.concat(toBinaryString);
                    }
                }

                fileWriter_green.write(toBinaryString +" ");

                length = Integer.toBinaryString(2).length();
                toBinaryString = Integer.toBinaryString(((int) (100 * Math.random()) % 4));
                if (toBinaryString.length() < length) {
                    int dif = length - toBinaryString.length();
                    for (int tv = 0; tv < dif; tv++) {
                        String zero = "0";
                        toBinaryString = zero.concat(toBinaryString);
                    }
                }

                fileWriter_blue.write(toBinaryString +" ");
                //4.3 mean of T
                
                //mean of T
                
                 length = 8;
                toBinaryString = Integer.toBinaryString(((int) meanRed_Target[blk_no]));
                if (toBinaryString.length() < length) {
                    int dif = length - toBinaryString.length();
                    for (int tv = 0; tv < dif; tv++) {
                        String zero = "0";
                        toBinaryString = zero.concat(toBinaryString);
                    }
                }

                fileWriter_red.write(toBinaryString+" ");
                
                 length = 8;
                toBinaryString = Integer.toBinaryString(((int) meanGreen_Target[blk_no]));
                if (toBinaryString.length() < length) {
                    int dif = length - toBinaryString.length();
                    for (int tv = 0; tv < dif; tv++) {
                        String zero = "0";
                        toBinaryString = zero.concat(toBinaryString);
                    }
                }

                fileWriter_green.write(toBinaryString+" ");
                
                
                 length = 8;
                toBinaryString = Integer.toBinaryString(((int) meanBlue_Target[blk_no]));
                if (toBinaryString.length() < length) {
                    int dif = length - toBinaryString.length();
                    for (int tv = 0; tv < dif; tv++) {
                        String zero = "0";
                        toBinaryString = zero.concat(toBinaryString);
                    }
                }

                fileWriter_blue.write(toBinaryString +" ");

                //4.4 mean of B
                //mean of B
                length = 8;
                toBinaryString = Integer.toBinaryString(((int) meanRed_Secrete[blk_no]));
                if (toBinaryString.length() < length) {
                    int dif = length - toBinaryString.length();
                    for (int tv = 0; tv < dif; tv++) {
                        String zero = "0";
                        toBinaryString = zero.concat(toBinaryString);
                    }
                }
                fileWriter_red.write(toBinaryString+" ");
                
                length = 8;
                toBinaryString = Integer.toBinaryString(((int) meanGreen_Secrete[blk_no]));
                if (toBinaryString.length() < length) {
                    int dif = length - toBinaryString.length();
                    for (int tv = 0; tv < dif; tv++) {
                        String zero = "0";
                        toBinaryString = zero.concat(toBinaryString);
                    }
                }
                fileWriter_green.write(toBinaryString+" " );
                //System.out.println();
                length = 8;
                toBinaryString = Integer.toBinaryString(((int) meanBlue_Secrete[blk_no]));
                if (toBinaryString.length() < length) {
                    int dif = length - toBinaryString.length();
                    for (int tv = 0; tv < dif; tv++) {
                        String zero = "0";
                        toBinaryString = zero.concat(toBinaryString);
                    }
                }
                fileWriter_blue.write(toBinaryString+" ");
                
                //4.5 standard deviation of quotients of RGB
                
                //standard deviation of quotients of RGB
                //s 
                //
                
              
                double max_std_dev=0;
                if(max_red_std_dev>max_green_std_dev && max_red_std_dev>max_blue_std_dev)
                    max_std_dev=max_red_std_dev;
                if(max_green_std_dev>max_red_std_dev && max_green_std_dev>max_blue_std_dev)
                    max_std_dev=max_green_std_dev;
                if(max_blue_std_dev>max_green_std_dev && max_blue_std_dev>max_red_std_dev)
                    max_std_dev=max_blue_std_dev;
                  
                max_red_std_dev=max_std_dev*100;
                max_green_std_dev=max_std_dev*100;
                max_blue_std_dev=max_std_dev*100;
               
                
                int max_red_std_dev_length=0;
                int min_red_std_dev_length=0;
                max_red_std_dev_length=Integer.toBinaryString(((int) (max_red_std_dev))).length();
                min_red_std_dev_length=Integer.toBinaryString(((int) (min_red_std_dev))).length();
                //System.out.println("min_red_std_dev_length="+min_red_std_dev_length+"max_red_std_dev_length="+max_red_std_dev_length);
                //System.out.println("min_red_std_dev="+min_red_std_dev+"max_red_std_dev ="+max_red_std_dev );
                //length = max_red_std_dev_length;
                length=12;
                toBinaryString = Integer.toBinaryString(((int) (r_std_dev_q*100)));
                //System.out.println(r_std_dev_q+" "+g_std_dev_q+" "+b_std_dev_q);
                
                //if()System.out.println(r_std_dev_q+" "+g_std_dev_q+" "+b_std_dev_q);
                if (toBinaryString.length() < length) {
                    int dif = length - toBinaryString.length();
                    for (int tv = 0; tv < dif; tv++) {
                        String zero = "0";
                        toBinaryString = zero.concat(toBinaryString);
                    }
                }
                fileWriter_red.write(toBinaryString+" ");
                //System.out.println(toBinaryString);
                int max_green_std_dev_length=0;
                int min_green_std_dev_length=0;
                max_green_std_dev_length=Integer.toBinaryString(((int) (max_green_std_dev))).length();
                min_green_std_dev_length=Integer.toBinaryString(((int) (min_green_std_dev))).length();
                //System.out.println("min_green_std_dev_length="+min_green_std_dev_length+"max_green_std_dev_length="+max_green_std_dev_length);
                //System.out.println("min_green_std_dev="+min_green_std_dev+"max_green_std_dev="+max_green_std_dev);
                
                length = max_green_std_dev_length;
                length=12;
                toBinaryString = Integer.toBinaryString(((int) (g_std_dev_q*100)));
                if (toBinaryString.length() < length) {
                    int dif = length - toBinaryString.length();
                    for (int tv = 0; tv < dif; tv++) {
                        String zero = "0";
                        toBinaryString = zero.concat(toBinaryString);
                    }
                }
                fileWriter_green.write(toBinaryString+" ");
                
                
                int max_blue_std_dev_length=0;
                int min_blue_std_dev_length=0;
                max_blue_std_dev_length=Integer.toBinaryString(((int) (max_blue_std_dev))).length();
                min_blue_std_dev_length=Integer.toBinaryString(((int) (min_blue_std_dev))).length();
                //System.out.println("min_blue_std_dev_length="+min_blue_std_dev_length+"max_blue_std_dev_length="+max_blue_std_dev_length);
                //System.out.println("min_blue_std_dev="+min_blue_std_dev+"max_blue_std_dev="+max_blue_std_dev);

                length = max_blue_std_dev_length;
                length=12;
                toBinaryString = Integer.toBinaryString(((int) (b_std_dev_q*100)));
                if (toBinaryString.length() < length) {
                    int dif = length - toBinaryString.length();
                    for (int tv = 0; tv < dif; tv++) {
                        String zero = "0";
                        toBinaryString = zero.concat(toBinaryString);
                    }
                }
                fileWriter_blue.write(toBinaryString+" ");
                
                //4.6 overflow
                //4.7 underflow residual+    



                //overflow/underflow residual
                //(r_std_dev_q, ,meanBlue_Secrete([blk_no]));
                int[] CsCl =Cs_CL(r_std_dev_q,meanRed_Secrete[blk_no],meanRed_Target[blk_no]);
                
                length =max_red_res_length;
                toBinaryString = Integer.toBinaryString(((int) (CsCl[0])));
                if (toBinaryString.length() < length) {
                    int dif = length - toBinaryString.length();
                    for (int tv = 0; tv < dif; tv++) {
                        String zero = "0";
                        toBinaryString = zero.concat(toBinaryString);
                    }
                }
                fileWriter_red.write(toBinaryString+" ");
                
                
                length = max_red_res_length;
                toBinaryString = Integer.toBinaryString(((int) (CsCl[1])));
                if (toBinaryString.length() < length) {
                    int dif = length - toBinaryString.length();
                    for (int tv = 0; tv < dif; tv++) {
                        String zero = "0";
                        toBinaryString = zero.concat(toBinaryString);
                    }
                }
                fileWriter_red.write(toBinaryString+" ");
                 
                
                
                CsCl =Cs_CL(g_std_dev_q,meanGreen_Secrete[blk_no],meanGreen_Target[blk_no]);
                
                length = max_green_res_length;
                toBinaryString = Integer.toBinaryString(((int) (CsCl[0])));
                if (toBinaryString.length() < length) {
                    int dif = length - toBinaryString.length();
                    for (int tv = 0; tv < dif; tv++) {
                        String zero = "0";
                        toBinaryString = zero.concat(toBinaryString);
                    }
                }
                fileWriter_green.write(toBinaryString+" ");
                 
                
                length = max_green_res_length;;
                toBinaryString = Integer.toBinaryString(((int) (CsCl[1])));
                if (toBinaryString.length() < length) {
                    int dif = length - toBinaryString.length();
                    for (int tv = 0; tv < dif; tv++) {
                        String zero = "0";
                        toBinaryString = zero.concat(toBinaryString);
                    }
                }
                fileWriter_green.write(toBinaryString+" ");
                
                
                CsCl =Cs_CL(b_std_dev_q,meanBlue_Secrete[blk_no],meanBlue_Target[blk_no]);
                
                length = max_blue_res_length;
                toBinaryString = Integer.toBinaryString(((int) (CsCl[0])));
                if (toBinaryString.length() < length) {
                    int dif = length - toBinaryString.length();
                    for (int tv = 0; tv < dif; tv++) {
                        String zero = "0";
                        toBinaryString = zero.concat(toBinaryString);
                    }
                }
                fileWriter_blue.write(toBinaryString+" ");
                
                length =  max_blue_res_length;
                toBinaryString = Integer.toBinaryString(((int) (CsCl[1])));
                if (toBinaryString.length() < length) {
                    int dif = length - toBinaryString.length();
                    for (int tv = 0; tv < dif; tv++) {
                        String zero = "0";
                        toBinaryString = zero.concat(toBinaryString);
                    }
                }
                fileWriter_blue.write(toBinaryString+" " );
                
               // max_std_dev
                length =  Integer.toBinaryString((int)max_std_dev).length();
                length = 8;
                toBinaryString = Integer.toBinaryString((int) std_dev_Red_Target[blk_no]);
                if (toBinaryString.length() < length) {
                    int dif = length - toBinaryString.length();
                    for (int tv = 0; tv < dif; tv++) {
                        String zero = "0";
                        toBinaryString = zero.concat(toBinaryString);
                    }
                }
                fileWriter_red.write(toBinaryString+" " );
                
                 length =  Integer.toBinaryString((int)max_std_dev).length();
                 length = 8;
                toBinaryString = Integer.toBinaryString((int) std_dev_Red_Secrete[blk_no]);
                if (toBinaryString.length() < length) {
                    int dif = length - toBinaryString.length();
                    for (int tv = 0; tv < dif; tv++) {
                        String zero = "0";
                        toBinaryString = zero.concat(toBinaryString);
                    }
                }
                fileWriter_red.write(toBinaryString+" " );
             
                   
               // max_std_dev
                length =  Integer.toBinaryString((int)max_std_dev).length();
                length = 8;
                toBinaryString = Integer.toBinaryString((int) std_dev_Green_Target[blk_no]);
                if (toBinaryString.length() < length) {
                    int dif = length - toBinaryString.length();
                    for (int tv = 0; tv < dif; tv++) {
                        String zero = "0";
                        toBinaryString = zero.concat(toBinaryString);
                    }
                }
                fileWriter_green.write(toBinaryString+" " );
                
                 length =  Integer.toBinaryString((int)max_std_dev).length();
                 length = 8;
                toBinaryString = Integer.toBinaryString((int) std_dev_Green_Secrete[blk_no]);
                if (toBinaryString.length() < length) {
                    int dif = length - toBinaryString.length();
                    for (int tv = 0; tv < dif; tv++) {
                        String zero = "0";
                        toBinaryString = zero.concat(toBinaryString);
                    }
                }
                fileWriter_green.write(toBinaryString+" " );
                
                  
               // max_std_dev
                length =  Integer.toBinaryString((int)max_std_dev).length();
                length = 8;
                toBinaryString = Integer.toBinaryString((int) std_dev_Blue_Target[blk_no]);
                if (toBinaryString.length() < length) {
                    int dif = length - toBinaryString.length();
                    for (int tv = 0; tv < dif; tv++) {
                        String zero = "0";
                        toBinaryString = zero.concat(toBinaryString);
                    }
                }
                fileWriter_blue.write(toBinaryString+" " );
                
                 length =  Integer.toBinaryString((int)max_std_dev).length();
                 length = 8;
                toBinaryString = Integer.toBinaryString((int) std_dev_Blue_Secrete[blk_no]);
                if (toBinaryString.length() < length) {
                    int dif = length - toBinaryString.length();
                    for (int tv = 0; tv < dif; tv++) {
                        String zero = "0";
                        toBinaryString = zero.concat(toBinaryString);
                    }
                }
                fileWriter_blue.write(toBinaryString+" " );
                
                
                fileWriter_red.write("\n");
                fileWriter_green.write("\n");
                fileWriter_blue.write("\n");
                
                  
                blk_no++;
            }
        }
  
////System.out.println("max_red_res_length="+max_red_res_length+"max_green_res_length="+max_green_res_length+"max_blue_res_length="+max_blue_res_length+"max_red_std_dev="+max_red_std_dev+"max_green_std_dev="+max_green_std_dev+"max_blue_std_dev="+max_blue_std_dev);
        blk_no = 0;
        rgb = 0;
        red = 0;
        green = 0;
        blue = 0;
        F_rec_from_Tiles = new BufferedImage(image_Target.getWidth(), image_Target.getHeight(), BufferedImage.TYPE_INT_ARGB);
        F_Target__rec_from_Tiles = new BufferedImage(image_Target.getWidth(), image_Target.getHeight(), BufferedImage.TYPE_INT_ARGB);
        F_stego = new BufferedImage(image_Target.getWidth(), image_Target.getHeight(), BufferedImage.TYPE_INT_ARGB);


        //get subimages of tiles into array of BufferedImages
        BufferedImage F_ARRAY[] = new BufferedImage[TOTAL_NO_BLOKS];

        blk_no = 0;
        for (int j = 0; j < lim_c && j % col_tile_size == 0; j = j + col_tile_size) {
            for (int i = 0; i < lim_r && i % row_tile_size == 0; i = i + row_tile_size) {   //////System.out.println("Block no="+blk_no);
                F_ARRAY[Secrete.INDEX[blk_no]] = F.getSubimage(j, i, col_tile_size, row_tile_size);
                blk_no++;
            }
        }

        blk_no = 0;
        for (int j = 0; j < lim_c && j % col_tile_size == 0; j = j + col_tile_size) {
            for (int i = 0; i < lim_r && i % row_tile_size == 0; i = i + row_tile_size) {   //////System.out.println("Block no="+blk_no);
                for (int mj = 0; mj < col_tile_size; mj++) {
                    for (int mi = 0; mi < row_tile_size; mi++) {
                        rgb = F_ARRAY[blk_no].getRGB(mj, mi);
                        F_rec_from_Tiles.setRGB(j + mj, i + mi, rgb);
                        //.setRGB(j + mj, i + mi, rgb);
                        
                        
                    }
                }


                blk_no++;
            }
        }

        //get subimages of target tiles into array of BufferedImages
        BufferedImage[] F_ARRAY1 = new BufferedImage[TOTAL_NO_BLOKS];

        blk_no = 0;
        for (int j = 0; j < lim_c && j % col_tile_size == 0; j = j + col_tile_size) {
            for (int i = 0; i < lim_r && i % row_tile_size == 0; i = i + row_tile_size) {   //////System.out.println("Block no="+blk_no);
                F_ARRAY1[Target.INDEX[blk_no]] = F_Target.getSubimage(j, i, col_tile_size, row_tile_size);
                blk_no++;
            }
        }

        blk_no = 0;
        for (int j = 0; j < lim_c && j % col_tile_size == 0; j = j + col_tile_size) {
            for (int i = 0; i < lim_r && i % row_tile_size == 0; i = i + row_tile_size) {   //////System.out.println("Block no="+blk_no);
                for (int mj = 0; mj < col_tile_size; mj++) {
                    for (int mi = 0; mi < row_tile_size; mi++) {
                        rgb = F_ARRAY1[blk_no].getRGB(mj, mi);
                        F_Target__rec_from_Tiles.setRGB(j + mj, i + mi, rgb);
                        F_stego.setRGB(j + mj, i + mi, rgb);
                    }
                }

                blk_no++;
            }
        }

        //save F
        try {
            ImageIO.write(F_rec_from_Tiles, "png", new File(secrete_i.getDirectory() + "F_rec_from_Tiles.png"));
        } catch (IOException e) {
        }

        try {
            F_Target__rec_from_Tiles = ColorTransform(image_Secrete, image_Target);
            F_stego=ColorTransform(image_Secrete, image_Target);
            ImageIO.write(F_Target__rec_from_Tiles, "png", new File(secrete_i.getDirectory() + "F_Target__rec_from_Tiles.png"));

        } catch (IOException e) {
        }



        //////System.out.println("Size of  Secrete Image:"+"ROWS="+image_Secrete.getHeight()+"\tCOLUMNS="+image_Secrete.getWidth());
        //////System.out.println("Toatal_no_blks="+TOTAL_NO_BLOKS);


        fileWriter_red.close();
        fileWriter_blue.close();

        
        //embedd data
 
        embed_data();
        
       
        String args[]=new String[4];
        args[0]= "-i";
        args[1]= secrete_i.getDirectory() + "F_stego.png";	
        args[2]="-o";
        args[3]= secrete_i.getDirectory() + "F_stego_compressed.png";
        TERcode(args);
        //TERdisplay();
        display_images();
        ////System.out.println("END OF ALG");

    }
 public static void TERdisplay( ){
		//Parse arguments
              String args[]=new String[2];
               args[0]= "-i";
               args[1]= secrete_i.getDirectory() + "F_stego_compressed.png.rec";
         	DisplayFrameParser parser = null;
		try{
			parser = new DisplayFrameParser(args);
		}catch(ErrorException e){
			////System.out.println("RUN ERROR:");
			e.printStackTrace();
			////System.out.println("Please report this error (specifying image type and parameters) to: gici-dev@abra.uab.es");
			System.exit(1);
		}catch(ParameterException e){
			////System.out.println("ARGUMENTS ERROR: " +  e.getMessage());
			System.exit(2);
		}
		
		//Get arguments from parser
		final String DInFile = parser.getDInFile();
		int resolutionLevels = parser.getResolutionLevels();
		int numberOfLayers =  parser.getNumberOfLayers();
		int targetBytes = parser.getTargetBytes();
		int[] channelList = parser.getChannelList();
		int yInit = parser.getYInit();
		int yLength = parser.getYLength();
		int xInit =  parser.getXInit();
		int xLength = parser.getXLength();
		int extractionType = parser.getExtractionType();
		
		try{
			InteractiveDecoder id = new InteractiveDecoder(DInFile);
			float gammaValue[] = new float[1];
			gammaValue[0] = TERDefaultValues.gammaValue;
			int completionMode[] = new int[1];
			completionMode[0] = TERDefaultValues.completionMode;
			boolean CVerbose[] = TERDefaultValues.CVerbose;


			float recoveredImage[][][] = null;
			id.setParameters(resolutionLevels,numberOfLayers,targetBytes,
					channelList,yInit,yLength,xInit,xLength,extractionType,
					gammaValue,completionMode,CVerbose,false);
			recoveredImage = id.run();
			String outFile = DInFile+"_rl_"+resolutionLevels+"_tb_"+getTarget(targetBytes);
			try{
				if (recoveredImage.length==1){
					SaveFile.SaveFileByExtension(recoveredImage,outFile+".pgm",id.getImageGeometry());
				} else if (recoveredImage.length==3){
					SaveFile.SaveFileByExtension(recoveredImage,outFile+".ppm",id.getImageGeometry());
				} else {
					SaveFile.SaveFileByExtension(recoveredImage,outFile+".raw",id.getImageGeometry());
				}
			} catch(Exception e){
				e.printStackTrace();
				System.err.println("Gici SaveFile ERROR: " + e.getMessage());
				System.exit(4);
			}
			
		} catch (Exception e){
			e.printStackTrace();
			System.err.println("Interactive Decoder ERROR: " + e.getMessage());
			System.exit(3);
		} 
		
	}

	private static String getTarget(int target){
		String s = Integer.toString(target);
		for(int k=s.length();k<6;k++){
			s = "0"+s;
		}
		return s;
	}
    

     
    private static void display_images() {


        JFrame frame_gi = new JFrame("Secrete  image");
        Panel panel_gi = new ShowImage(secrete_i.getDirectory() + secrete_i.getFileName());
        frame_gi.getContentPane().add(panel_gi);
        frame_gi.setSize(500, 500);
        frame_gi.setVisible(true);


        JFrame frame_bi = new JFrame("Target image");
        Panel panel_bi = new ShowImage(target_i.getDirectory() + target_i.getFileName());
        frame_bi.getContentPane().add(panel_bi);
        frame_bi.setSize(500, 500);
        frame_bi.setVisible(true);

        JFrame frame_gis = new JFrame("Secrete Tile image");
        Panel panel_gis = new ShowImage(secrete_i.getDirectory() + "F.png");
        frame_gis.getContentPane().add(panel_gis);
        frame_gis.setSize(500, 500);
        frame_gis.setVisible(true);


        JFrame frame_bit = new JFrame("Target tile image");
        Panel panel_bit = new ShowImage(target_i.getDirectory() + "F_Target.png");
        frame_bit.getContentPane().add(panel_bit);
        frame_bit.setSize(500, 500);
        frame_bit.setVisible(true);





        JFrame frame_bi2 = new JFrame("mosaic Target image F after color transformation");
        Panel panel_bi2 = new ShowImage(secrete_i.getDirectory() + "F_Target__rec_from_Tiles.png");
        frame_bi2.getContentPane().add(panel_bi2);
        frame_bi2.setSize(500, 500);
        frame_bi2.setVisible(true);
        
        
        JFrame frame_bi3 = new JFrame("Stego color Image ");
        Panel panel_bi3= new ShowImage(secrete_i.getDirectory() + "F_stego.png");
        frame_bi3.getContentPane().add(panel_bi3);
        frame_bi3.setSize(500, 500);
        frame_bi3.setVisible(true);


    }

    public static BufferedImage rotate(BufferedImage image_Secrete, double angle) {
        double t_r_src = 0.0, t_g_src = 0.0, t_b_src = 0.0, t_r_tgt = 0.0, t_g_tgt = 0.0, t_b_tgt = 0.0;
        double s_r_src = 0.0, s_g_src = 0.0, s_b_src = 0.0, s_r_tgt = 0.0, s_g_tgt = 0.0, s_b_tgt = 0.0;

        t_r_src = mean(image_Secrete, 1);
        t_g_src = mean(image_Secrete, 2);
        t_b_src = mean(image_Secrete, 3);


        t_r_tgt = -mean(image_Secrete, 1);
        t_g_tgt = -mean(image_Secrete, 2);
        t_b_tgt = -mean(image_Secrete, 3);



        //get covarince matrieces of both for R G B
        Matrix Cov_src = new Matrix(4, 4);

        Matrix Cov_tgt = new Matrix(4, 4);



        //get R G B og cuurent image into Matrix form
        Matrix src = new Matrix(image_Secrete.getHeight() * image_Secrete.getWidth(), 4);



        Matrix tgt = new Matrix(image_Secrete.getHeight() * image_Secrete.getWidth(), 4);


        //set values
        int rgb = 0, red = 0, green = 0, blue = 0;
        int row_no = 0;
        for (int i = 0; i < image_Secrete.getWidth(); i++) {
            for (int j = 0; j < image_Secrete.getHeight(); j++) {
                rgb = image_Secrete.getRGB(i, j);
                red = (rgb & 0x00FF0000) >>> 16;
                green = (rgb & 0x0000FF00) >>> 8;
                blue = (rgb & 0x000000FF) >>> 0;
                src.set(row_no, 0, red);
                src.set(row_no, 1, green);
                src.set(row_no, 2, blue);
                src.set(row_no, 3, 1);


                rgb = image_Target.getRGB(i, j);
                red = (rgb & 0x00FF0000) >>> 16;
                green = (rgb & 0x0000FF00) >>> 8;
                blue = (rgb & 0x000000FF) >>> 0;
                tgt.set(row_no, 0, red);
                tgt.set(row_no, 1, green);
                tgt.set(row_no, 2, blue);
                tgt.set(row_no, 3, 1);
                row_no++;

            }
        }

        MeanAndCovariance mc_src = new MeanAndCovariance(src);
        MeanAndCovariance mc_tgt = new MeanAndCovariance(tgt);

        Cov_src = mc_src.getCovariance();
        Cov_tgt = mc_tgt.getCovariance();
//use svd to decompose covariance matrices
        SingularValueDecomposition svd_Cov_src = Cov_src.svd();


        SingularValueDecomposition svd_Cov_tgt = Cov_tgt.svd();


        //form translation rotation and scaling matrix for images

        Matrix T_src = new Matrix(4, 4);
        Matrix R_src = new Matrix(4, 4);
        Matrix S_src = new Matrix(4, 4);

        Matrix T_tgt = new Matrix(4, 4);
        Matrix R_tgt = new Matrix(4, 4);
        Matrix S_tgt = new Matrix(4, 4);


        //for translation
        T_src.set(0, 0, 1.0);
        T_src.set(1, 0, 0.0);
        T_src.set(2, 0, 0.0);
        T_src.set(3, 0, 0.0);

        T_src.set(0, 1, 0.0);
        T_src.set(1, 1, 1.0);
        T_src.set(2, 1, 0.0);
        T_src.set(3, 1, 0.0);


        T_src.set(0, 2, 0.0);
        T_src.set(1, 2, 0.0);
        T_src.set(2, 2, 1.0);
        T_src.set(3, 2, 0.0);

        double[][] array = mc_src.getMean().getArray();
        T_src.set(0, 3, array[0][0]);
        T_src.set(1, 3, array[0][1]);
        T_src.set(2, 3, array[0][2]);
        T_src.set(3, 3, 1.0);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {//T_src.print(i, j);
            }
        }
        //T_src.print(1, 10);



        T_tgt.set(0, 0, 1.0);
        T_tgt.set(1, 0, 0.0);
        T_tgt.set(2, 0, 0.0);
        T_tgt.set(3, 0, 0.0);

        T_tgt.set(0, 1, 0.0);
        T_tgt.set(1, 1, 1.0);
        T_tgt.set(2, 1, 0.0);
        T_tgt.set(3, 1, 0.0);


        T_tgt.set(0, 2, 0.0);
        T_tgt.set(1, 2, 0.0);
        T_tgt.set(2, 2, 1.0);
        T_tgt.set(3, 2, 0.0);

        array = mc_tgt.getMean().getArray();
        T_tgt.set(0, 3, -array[0][0]);
        T_tgt.set(1, 3, -array[0][1]);
        T_tgt.set(2, 3, -array[0][2]);
        T_tgt.set(3, 3, 1.0);

        //T_tgt.print(1, 10);


        //for Rotation
        R_src = svd_Cov_src.getU();
        R_tgt = svd_Cov_tgt.getU().inverse();

        //for scaling

        S_src.set(0, 0, svd_Cov_src.getS().get(0, 0));
        S_src.set(1, 0, 0);
        S_src.set(2, 0, 0);
        S_src.set(3, 0, 0);

        S_src.set(0, 1, 0);
        S_src.set(1, 1, svd_Cov_src.getS().get(1, 1));
        S_src.set(2, 1, 0);
        S_src.set(3, 1, 0);

        S_src.set(0, 2, 0);
        S_src.set(1, 2, 0);
        S_src.set(2, 2, svd_Cov_src.getS().get(2, 2));
        S_src.set(3, 2, 0);

        S_src.set(0, 3, 0);
        S_src.set(1, 3, 0);
        S_src.set(2, 3, 0);
        S_src.set(3, 3, 1);







        S_tgt.set(0, 0, 1.0 / Math.sqrt(svd_Cov_tgt.getS().get(0, 0)));
        S_tgt.set(1, 0, 0);
        S_tgt.set(2, 0, 0);
        S_tgt.set(3, 0, 0);

        S_tgt.set(0, 1, 0);
        S_tgt.set(1, 1, 1.0 / Math.sqrt(svd_Cov_tgt.getS().get(1, 1)));
        S_tgt.set(2, 1, 0);
        S_tgt.set(3, 1, 0);

        S_tgt.set(0, 2, 0);
        S_tgt.set(1, 2, 0);
        S_tgt.set(2, 2, 1.0 / Math.sqrt(svd_Cov_tgt.getS().get(2, 2)));
        S_tgt.set(3, 2, 0);

        S_tgt.set(0, 3, 0);
        S_tgt.set(1, 3, 0);
        S_tgt.set(2, 3, 0);
        S_tgt.set(3, 3, 1);


        // R_src.print(9, 3);
        //////System.out.println(svd_Cov_src.getS().det());
        //////System.out.println(svd_Cov_tgt.getS().det());
        //svd_Cov_src.getS().print(9, 6);
        //S_src.print(9, 3);

        //svd_Cov_tgt.getS().print(9, 6);        
        //S_tgt.print(9, 3);
//form total transformation matix 
        Matrix T_overall = new Matrix(4, 4);


        //T_overall=T_src.times(R_src).times(S_src).times(S_tgt).times(R_tgt).times(T_tgt);
        //T_overall=T_src.times(R_src).times(S_src).times(S_tgt).times(R_tgt).times(T_tgt);
        T_overall=T_src.times(R_src).times(R_tgt).times(T_tgt);
        //T_overall = R_src.times(R_tgt);


        //for(int i=0;i<4;i++)
        // for(int j=0;j<4;j++)
        // T_overall.print(i, j);//
        //R_src.print(9, 6);
        //R_tgt.print(9, 6);
        //S_src.print(9, 6);
        //S_tgt.print(9, 6);
        //T_overall.print(9, 6);
        //apply it on target image to obtain result


        BufferedImage op = new BufferedImage(image_Target.getWidth(), image_Target.getHeight(), BufferedImage.TYPE_INT_ARGB);

        Matrix result = T_overall.times(tgt.transpose());
        result = result.transpose();
        //convert to iMAGE form
        int no_of_rows = 0;
        for (int i = 0; i < image_Secrete.getWidth(); i++) {
            for (int j = 0; j < image_Secrete.getHeight(); j++) {
                int alpha = 255;
                red = (int) result.get(no_of_rows, 0);
                green = (int) result.get(no_of_rows, 1);
                blue = (int) result.get(no_of_rows, 2);
                rgb = (alpha << 24) | (red << 16) | (green << 8) | blue;
                op.setRGB(i, j, rgb);
                no_of_rows++;
            }
        }

        return op;
    }

    private static BufferedImage ColorTransform(BufferedImage image_Secrete, BufferedImage image_Target) {
        //get  mean  of both

        double t_r_src = 0.0, t_g_src = 0.0, t_b_src = 0.0, t_r_tgt = 0.0, t_g_tgt = 0.0, t_b_tgt = 0.0;
        double s_r_src = 0.0, s_g_src = 0.0, s_b_src = 0.0, s_r_tgt = 0.0, s_g_tgt = 0.0, s_b_tgt = 0.0;

        t_r_src = mean(image_Secrete, 1);
        t_g_src = mean(image_Secrete, 2);
        t_b_src = mean(image_Secrete, 3);


        t_r_tgt = -mean(image_Secrete, 1);
        t_g_tgt = -mean(image_Secrete, 2);
        t_b_tgt = -mean(image_Secrete, 3);



        //get covarince matrieces of both for R G B
        Matrix Cov_src = new Matrix(4, 4);


        Matrix Cov_tgt = new Matrix(4, 4);





        //get R G B og cuurent image into Matrix form
        Matrix src = new Matrix(image_Secrete.getHeight() * image_Secrete.getWidth(), 4);



        Matrix tgt = new Matrix(image_Secrete.getHeight() * image_Secrete.getWidth(), 4);


        //set values
        int rgb = 0, red = 0, green = 0, blue = 0;
        int row_no = 0;
        for (int i = 0; i < image_Secrete.getWidth(); i++) {
            for (int j = 0; j < image_Secrete.getHeight(); j++) {
                rgb = image_Secrete.getRGB(i, j);
                red = (rgb & 0x00FF0000) >>> 16;
                green = (rgb & 0x0000FF00) >>> 8;
                blue = (rgb & 0x000000FF) >>> 0;
                src.set(row_no, 0, red);
                src.set(row_no, 1, green);
                src.set(row_no, 2, blue);
                src.set(row_no, 3, 1);


                rgb = image_Target.getRGB(i, j);
                red = (rgb & 0x00FF0000) >>> 16;
                green = (rgb & 0x0000FF00) >>> 8;
                blue = (rgb & 0x000000FF) >>> 0;
                tgt.set(row_no, 0, red);
                tgt.set(row_no, 1, green);
                tgt.set(row_no, 2, blue);
                tgt.set(row_no, 3, 1);
                row_no++;

            }
        }

        MeanAndCovariance mc_src = new MeanAndCovariance(src);
        MeanAndCovariance mc_tgt = new MeanAndCovariance(tgt);

        Cov_src = mc_src.getCovariance();
        Cov_tgt = mc_tgt.getCovariance();
//use svd to decompose covariance matrices
        SingularValueDecomposition svd_Cov_src = Cov_src.svd();


        SingularValueDecomposition svd_Cov_tgt = Cov_tgt.svd();


        //form translation rotation and scaling matrix for images

        Matrix T_src = new Matrix(4, 4);
        Matrix R_src = new Matrix(4, 4);
        Matrix S_src = new Matrix(4, 4);

        Matrix T_tgt = new Matrix(4, 4);
        Matrix R_tgt = new Matrix(4, 4);
        Matrix S_tgt = new Matrix(4, 4);


        //for translation
        T_src.set(0, 0, 1.0);
        T_src.set(1, 0, 0.0);
        T_src.set(2, 0, 0.0);
        T_src.set(3, 0, 0.0);

        T_src.set(0, 1, 0.0);
        T_src.set(1, 1, 1.0);
        T_src.set(2, 1, 0.0);
        T_src.set(3, 1, 0.0);


        T_src.set(0, 2, 0.0);
        T_src.set(1, 2, 0.0);
        T_src.set(2, 2, 1.0);
        T_src.set(3, 2, 0.0);

        double[][] array = mc_src.getMean().getArray();
        T_src.set(0, 3, array[0][0]);
        T_src.set(1, 3, array[0][1]);
        T_src.set(2, 3, array[0][2]);
        T_src.set(3, 3, 1.0);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {//T_src.print(i, j);
            }
        }
        //T_src.print(1, 10);



        T_tgt.set(0, 0, 1.0);
        T_tgt.set(1, 0, 0.0);
        T_tgt.set(2, 0, 0.0);
        T_tgt.set(3, 0, 0.0);

        T_tgt.set(0, 1, 0.0);
        T_tgt.set(1, 1, 1.0);
        T_tgt.set(2, 1, 0.0);
        T_tgt.set(3, 1, 0.0);


        T_tgt.set(0, 2, 0.0);
        T_tgt.set(1, 2, 0.0);
        T_tgt.set(2, 2, 1.0);
        T_tgt.set(3, 2, 0.0);

        array = mc_tgt.getMean().getArray();
        T_tgt.set(0, 3, -array[0][0]);
        T_tgt.set(1, 3, -array[0][1]);
        T_tgt.set(2, 3, -array[0][2]);
        T_tgt.set(3, 3, 1.0);

        //T_tgt.print(1, 10);


        //for Rotation
        R_src = svd_Cov_src.getU();
        R_tgt = svd_Cov_tgt.getU().inverse();

        //for scaling

        S_src.set(0, 0, svd_Cov_src.getS().get(0, 0));
        S_src.set(1, 0, 0);
        S_src.set(2, 0, 0);
        S_src.set(3, 0, 0);

        S_src.set(0, 1, 0);
        S_src.set(1, 1, svd_Cov_src.getS().get(1, 1));
        S_src.set(2, 1, 0);
        S_src.set(3, 1, 0);

        S_src.set(0, 2, 0);
        S_src.set(1, 2, 0);
        S_src.set(2, 2, svd_Cov_src.getS().get(2, 2));
        S_src.set(3, 2, 0);

        S_src.set(0, 3, 0);
        S_src.set(1, 3, 0);
        S_src.set(2, 3, 0);
        S_src.set(3, 3, 1);







        S_tgt.set(0, 0, 1.0 / Math.sqrt(svd_Cov_tgt.getS().get(0, 0)));
        S_tgt.set(1, 0, 0);
        S_tgt.set(2, 0, 0);
        S_tgt.set(3, 0, 0);

        S_tgt.set(0, 1, 0);
        S_tgt.set(1, 1, 1.0 / Math.sqrt(svd_Cov_tgt.getS().get(1, 1)));
        S_tgt.set(2, 1, 0);
        S_tgt.set(3, 1, 0);

        S_tgt.set(0, 2, 0);
        S_tgt.set(1, 2, 0);
        S_tgt.set(2, 2, 1.0 / Math.sqrt(svd_Cov_tgt.getS().get(2, 2)));
        S_tgt.set(3, 2, 0);

        S_tgt.set(0, 3, 0);
        S_tgt.set(1, 3, 0);
        S_tgt.set(2, 3, 0);
        S_tgt.set(3, 3, 1);


        // R_src.print(9, 3);
        //////System.out.println(svd_Cov_src.getS().det());
        //////System.out.println(svd_Cov_tgt.getS().det());
        //svd_Cov_src.getS().print(9, 6);
        //S_src.print(9, 3);

        //svd_Cov_tgt.getS().print(9, 6);        
        //S_tgt.print(9, 3);
//form total transformation matix 
        Matrix T_overall = new Matrix(4, 4);


        //T_overall=T_src.times(R_src).times(S_src).times(S_tgt).times(R_tgt).times(T_tgt);
        //T_overall=T_src.times(R_src).times(S_src).times(S_tgt).times(R_tgt).times(T_tgt);
        //T_overall=T_src.times(R_src).times(R_tgt).times(T_tgt);
        T_overall = R_src.times(R_tgt);


        //for(int i=0;i<4;i++)
        // for(int j=0;j<4;j++)
        // T_overall.print(i, j);//
        //R_src.print(9, 6);
        //R_tgt.print(9, 6);
        //S_src.print(9, 6);
        //S_tgt.print(9, 6);
        //T_overall.print(9, 6);
        //apply it on target image to obtain result


        BufferedImage op = new BufferedImage(image_Target.getWidth(), image_Target.getHeight(), BufferedImage.TYPE_INT_ARGB);

        Matrix result = T_overall.times(tgt.transpose());
        result = result.transpose();
        //convert to iMAGE form
        int no_of_rows = 0;
        for (int i = 0; i < image_Secrete.getWidth(); i++) {
            for (int j = 0; j < image_Secrete.getHeight(); j++) {
                int alpha = 255;
                red = (int) result.get(no_of_rows, 0);
                green = (int) result.get(no_of_rows, 1);
                blue = (int) result.get(no_of_rows, 2);
                rgb = (alpha << 24) | (red << 16) | (green << 8) | blue;
                op.setRGB(i, j, rgb);
                no_of_rows++;
            }
        }

        try {
            ImageIO.write(op, "png", new File(secrete_i.getDirectory() + "TRANSORMED.png"));
        } catch (IOException e) {
        }

        return op;
//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static double mean(BufferedImage subimage_image_Secrete, int C) {
        //    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        double Mean = 0;
        int red = 0, green = 0, blue = 0, rgb = 0;
        for (int i = 0; i < subimage_image_Secrete.getWidth(); i++) {
            for (int j = 0; j < subimage_image_Secrete.getHeight(); j++) {
                rgb = subimage_image_Secrete.getRGB(i, j);
                red = (rgb & 0x00FF0000) >>> 16;
                green = (rgb & 0x0000FF00) >>> 8;
                blue = (rgb & 0x000000FF) >>> 0;
                ////////System.out.println(rgb);
                ////////System.out.println(green);
                if (C == 1) {
                    Mean = Mean + red;
                }
                if (C == 2) {
                    Mean = Mean + green;
                }
                if (C == 3) {
                    Mean = Mean + blue;
                }
            }
        }
        Mean = Mean / (subimage_image_Secrete.getWidth() * subimage_image_Secrete.getHeight());
        //////System.out.println(Mean);
        return (Mean);
    }

    private static double std_dev(BufferedImage subimage_image_Secrete, int C) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        double Mean = 0;
        int red = 0, green = 0, blue = 0, rgb = 0;
        for (int i = 0; i < subimage_image_Secrete.getWidth(); i++) {
            for (int j = 0; j < subimage_image_Secrete.getHeight(); j++) {
                rgb = subimage_image_Secrete.getRGB(i, j);
                red = (rgb & 0x00FF0000) >>> 16;
                green = (rgb & 0x0000FF00) >>> 8;
                blue = (rgb & 0x000000FF) >>> 0;

                if (C == 1) {
                    Mean = Mean + red;
                }
                if (C == 2) {
                    Mean = Mean + green;
                }
                if (C == 3) {
                    Mean = Mean + blue;
                }
            }
        }

        Mean = (Mean / (subimage_image_Secrete.getWidth() * subimage_image_Secrete.getHeight()));



        double std_dev = 0;
        double tmp = 0, color_value = 0;
        for (int i = 0; i < subimage_image_Secrete.getWidth(); i++) {
            for (int j = 0; j < subimage_image_Secrete.getHeight(); j++) {
                rgb = subimage_image_Secrete.getRGB(i, j);
                red = (rgb & 0x00FF0000) >>> 16;
                green = (rgb & 0x0000FF00) >>> 8;
                blue = (rgb & 0x000000FF) >>> 0;

                if (C == 1) {
                    color_value = red;
                }
                if (C == 2) {
                    color_value = green;
                }
                if (C == 3) {
                    color_value = blue;
                }


                tmp = tmp + (color_value - Mean) * (color_value - Mean);

            }
        }


        tmp = tmp / (subimage_image_Secrete.getWidth() * subimage_image_Secrete.getHeight());
        std_dev = Math.sqrt(tmp);
        //////System.out.println(std_dev);
        return (std_dev);
    }

     private static int [] Cs_CL(double qc, double uc,double uc_) {
      
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        //cS = ⎡(1/ qc )(255 − cμ' ) + cμ ⎤ ; cL = ⎢(1/ qc )(0 − cμ' ) + cμ ⎥ 
        int C[];
        C = new int[2];
        //C[0]= (int)Math.ceil((1/qc)*(255 − uc_ )+uc)); 
        C[0]=(int )Math.floor(1/qc*(255-uc_)+uc);
        C[1]=(int )Math.floor(1/qc*(0-uc_)+uc);
         return C;

    }
static  int setLSB(int data,int bit){
int bitm=(bit & 0x00000001);
int datam=(data & 0xFFFFFFFe);

datam=(datam | bitm);

        return datam;
    
}   
int getLSB(int data){
        data=(data & 0x00000001);
       
        return data;
    
}
    private static void embed_data() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   
        FileReader inputStream = null;
       
        try {
            inputStream = new FileReader(secrete_i.getDirectory()+"RED.txt");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Secret_fragment_visible_mosaic_image_creation_Modular.class.getName()).log(Level.SEVERE, null, ex);
        }
            
       int c;
       int count=0;
        try {
            while ((c = inputStream.read()) != -1) {
                //////System.out.println(c);
                if(c!=10)
                count=count+1;
            }
        } catch (IOException ex) {
            Logger.getLogger(Secret_fragment_visible_mosaic_image_creation_Modular.class.getName()).log(Level.SEVERE, null, ex);
        }
        
         try {
            inputStream = new FileReader(secrete_i.getDirectory()+"RED.txt");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Secret_fragment_visible_mosaic_image_creation_Modular.class.getName()).log(Level.SEVERE, null, ex);
        }

   
      int DATA_red[]=new int[count];
      int k=0;
        try {
            while ((c = inputStream.read()) != -1) {
                if(c!=10){
                    if(c==48)
                DATA_red[k]=0;
                    if(c==49)
                DATA_red[k]=1;
                    k++;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Secret_fragment_visible_mosaic_image_creation_Modular.class.getName()).log(Level.SEVERE, null, ex);
        }
         
  
        
  //////System.out.println("Toatl bits embedded in RED plane="+DATA_red.length+"F_stego.getWidth()*F_stego.getWidth()="+F_stego.getWidth()*F_stego.getHeight());      
  k=0;       
  for (int j = 0; j <F_stego.getWidth() ; j ++) {
    for (int i = 0; i <F_stego.getHeight() ; i++) { 
        
        int rgb = F_stego.getRGB(j, i);
        int alpha = 255;
        int red = (rgb & 0x00FF0000) >>> 16;
        int green = (rgb & 0x0000FF00) >>> 8;
        int blue = (rgb & 0x000000FF) >>> 0;
        if(k<count){
            
        int setLSB_red = setLSB(red,DATA_red[k]);
             
        rgb = (alpha << 24) | (setLSB_red << 16) | (green << 8) | blue;
        ////System.out.println("j="+j+"\ti="+i+"\trgb="+rgb+"\tred="+red+"\tgreen="+green+"\tblue="+blue+"\tsetLSB_red="+setLSB_red+"\tDATA_red[k]="+DATA_red[k]);
        F_stego.setRGB(j, i,rgb);
        k++;
        }
    }
    }
  
  
  
  
     inputStream = null;
       
        try {
            inputStream = new FileReader(secrete_i.getDirectory()+"GREEN.txt");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Secret_fragment_visible_mosaic_image_creation_Modular.class.getName()).log(Level.SEVERE, null, ex);
        }
            
     
       count=0;
        try {
            while ((c = inputStream.read()) != -1) {
                //////System.out.println(c);
                if(c!=10)
                count=count+1;
            }
        } catch (IOException ex) {
            Logger.getLogger(Secret_fragment_visible_mosaic_image_creation_Modular.class.getName()).log(Level.SEVERE, null, ex);
        }
        
         try {
            inputStream = new FileReader(secrete_i.getDirectory()+"GREEN.txt");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Secret_fragment_visible_mosaic_image_creation_Modular.class.getName()).log(Level.SEVERE, null, ex);
        }

   
      int DATA_green[]=new int[count];
       k=0;
        try {
            while ((c = inputStream.read()) != -1) {
                if(c!=10){
                if(c==48)
                DATA_green[k]=0;
                    if(c==49)
                DATA_green[k]=1;
               k++;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Secret_fragment_visible_mosaic_image_creation_Modular.class.getName()).log(Level.SEVERE, null, ex);
        }
         
  
        
  //////System.out.println("xx="+DATA_green.length+"F_stego.getWidth()*F_stego.getWidth()="+F_stego.getWidth()*F_stego.getHeight());      
  k=0;       
  for (int j = 0; j <F_stego.getWidth() ; j ++) {
    for (int i = 0; i <F_stego.getHeight() ; i++) { 
        
        int rgb = F_stego.getRGB(j, i);
        int alpha = 255;
        int red = (rgb & 0x00FF0000) >>> 16;
        int green = (rgb & 0x0000FF00) >>> 8;
        int blue = (rgb & 0x000000FF) >>> 0;
        if(k<count){
        int setLSB_green = setLSB(green,DATA_green[k]);
             
        rgb = (alpha << 24) | (red << 16) | (setLSB_green << 8) | blue;
        //////System.out.println("j="+j+"i="+i);
         ////System.out.println("j="+j+"\ti="+i+"\trgb="+rgb+"\tred="+red+"\tgreen="+green+"\tblue="+blue+"\tsetLSB_green="+setLSB_green+"\tDATA_green[k]="+DATA_green[k]);
      
        F_stego.setRGB(j, i,rgb);
        k++;
        }
    }
    }
  
  
  
     inputStream = null;
       
        try {
            inputStream = new FileReader(secrete_i.getDirectory()+"BLUE.txt");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Secret_fragment_visible_mosaic_image_creation_Modular.class.getName()).log(Level.SEVERE, null, ex);
        }
            
     
       count=0;
        try {
            while ((c = inputStream.read()) != -1) {
                //////System.out.println(c);
                if(c!=10)
                count=count+1;
            }
        } catch (IOException ex) {
            Logger.getLogger(Secret_fragment_visible_mosaic_image_creation_Modular.class.getName()).log(Level.SEVERE, null, ex);
        }
        
         try {
            inputStream = new FileReader(secrete_i.getDirectory()+"BLUE.txt");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Secret_fragment_visible_mosaic_image_creation_Modular.class.getName()).log(Level.SEVERE, null, ex);
        }

   
      int DATA_blue[]=new int[count];
       k=0;
        try {
            while ((c = inputStream.read()) != -1) {
                if(c!=10){
                    DATA_blue[k]=0;
                    if(c==49)
                DATA_blue[k]=1;
                 
               k++;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Secret_fragment_visible_mosaic_image_creation_Modular.class.getName()).log(Level.SEVERE, null, ex);
        }
         
  
        
  //////System.out.println("xx="+DATA_blue.length+"F_stego.getWidth()*F_stego.getWidth()="+F_stego.getWidth()*F_stego.getHeight());      
  k=0;       
  for (int j = 0; j <F_stego.getWidth() ; j ++) {
    for (int i = 0; i <F_stego.getHeight() ; i++) { 
        
        int rgb = F_stego.getRGB(j, i);
        int alpha = 255;
        int red = (rgb & 0x00FF0000) >>> 16;
        int green = (rgb & 0x0000FF00) >>> 8;
        int blue = (rgb & 0x000000FF) >>> 0;
        if(k<count){
        int setLSB_blue= setLSB(blue,DATA_blue[k]);
             
        rgb = (alpha << 24) | (red << 16) | (green << 8) | setLSB_blue;
        //////System.out.println("j="+j+"i="+i);
        ////System.out.println("j="+j+"\ti="+i+"\trgb="+rgb+"\tred="+red+"\tgreen="+green+"\tblue="+blue+"\tsetLSB_blue="+setLSB_blue+"\tDATA_blue[k]="+DATA_blue[k]);
      
        F_stego.setRGB(j, i,rgb);
        k++;
        }
    }
    }
  
  
  
  
  
  
  
  
  
    try {
            ImageIO.write(F_stego, "png", new File(secrete_i.getDirectory() + "F_stego.png"));
        } catch (IOException e) {
        }

    }
   public static void TERcode(String args[])
    {
		//Parse arguments
//args=new String[4];
//args[0]= "-i";
//args[1]="lena_color.png";	
//args[2]="-o";
//args[3]="lena_color.png";		// Parse arguments

ArgsParserCoder argsParser = null;
		try{
			argsParser = new ArgsParserCoder(args);
		}catch(Exception e){
			System.err.println("TERcode : ARGUMENTS ERROR: " +  e.getMessage());
			//System.exit(1);
		}
		
		//Image load
		String imageFile = argsParser.getImageFile();
		LoadFile image = null;
		int[] imageGeometry = null;
		try{
			if(imageFile.endsWith(".raw") || imageFile.endsWith(".img")){
				imageGeometry = argsParser.getImageGeometry();
				
				//Check parameters of image geometry
				if((imageGeometry[0] <= 0) || (imageGeometry[1] <= 0) || (imageGeometry[2] <= 0)){
					throw new Exception("Image dimensions in \".raw\" or \".img\" data files must be positive (\"-h\" displays help).");
				}
				if((imageGeometry[3] < 0) || (imageGeometry[3] > 7)){
					throw new Exception("Image type in \".raw\" or \".img\" data must be between 0 to 7 (\"-h\" displays help).");
				}
				if((imageGeometry[4] != 0) && (imageGeometry[4] != 1)){
					throw new Exception("Image byte order  in \".raw\" or \".img\" data must be 0 or 1 (\"-h\" displays help).");
				}
				if((imageGeometry[5] != 0) && (imageGeometry[5] != 1)){
					throw new Exception("Image RGB specification in \".raw\" or \".img\" data must be between 0 or 1 (\"-h\" displays help).");
				}
				image = new LoadFile(imageFile, imageGeometry[0], imageGeometry[1], imageGeometry[2], imageGeometry[3], imageGeometry[4], imageGeometry[5] == 0 ? false: true);
			}else{
				image = new LoadFile(imageFile);	
			}
		}catch(Exception e){
			System.err.println("IMAGE LOADING ERROR: " + e.getMessage());
			System.exit(2);
		}
		
		//Get arguments from parser
		String outputFile = argsParser.getOutputFile();
		if (outputFile!=null){
			if(outputFile.compareTo("") == 0){
				outputFile = imageFile;
			}
		} else {
			outputFile = imageFile;
		}
		int outputFileType = argsParser.getOutputFileType();
		int[] part2Flag = argsParser.getPart2Flag();
		int[] part3Flag = argsParser.getPart3Flag();
		int[] part4Flag = argsParser.getPart4Flag();
		int[] segByteLimit = argsParser.getSegByteLimit();
		int[] DCStop = argsParser.getDCStop();
		int[] bitPlaneStop = argsParser.getBitPlaneStop();
		int[] stageStop = argsParser.getStageStop();
		int[] useFill = argsParser.getUseFill();
		int[] blocksPerSegment = argsParser.getBlocksPerSegment();
		int[] optDCSelect = argsParser.getOptDCSelect();
		int[] optACSelect = argsParser.getOptACSelect();
		int[] WTType = argsParser.getWTType();
		int[] signedPixels = argsParser.getSignedPixels();
		int[] transposeImg = argsParser.getTransposeImg();
		int[] codeWordLength = argsParser.getCodeWordLength();
		int[] customWtFlag = argsParser.getCustomWtFlag();
		float[] customWeight = argsParser.getCustomWeight();
		int[] WTLevels = argsParser.getWTLevels();
		int[] imageExtensionType = argsParser.getimageExtensionType();
		int[] WTOrder = argsParser.getWTOrder();
		int[] gaggleDCSize = argsParser.getGaggleDCSize();
		int[] gaggleACSize = argsParser.getGaggleACSize();
		int[] idDC = argsParser.getIdDC();
		int[] idAC = argsParser.getIdAC();
		float[] desiredDistortion = argsParser.getDesiredDistortion();
		int[] entropyAC = argsParser.getEntropyAC();
		int[] distortionMeasure = argsParser.getDistortionMeasure();
		int[] resolutionLevels = argsParser.getResolutionLevels();
		float[] compressionFactor = argsParser.getCompressionFactor();
		int[] pixelBitDepth = argsParser.getPixelBitDepth();
		boolean[] CVerbose = argsParser.getCVerbose();
		float[] bpppb = argsParser.getBpppb();
		int[] truncationPoints = argsParser.getTruncationPoints();
		int[] adjustHeaderParameters = argsParser.getAdjustHeaderParameters();
		int progressionOrder = argsParser.getProgressionOrder();
		int LSType = argsParser.getLSType();
		int[] LSComponents = argsParser.getLSComponents();
		int[] LSSubsValues = argsParser.getLSSubsValues();
		int[] coefficientsApproximationTypes = argsParser.getCoefficientsApproximation();
		int targetBytes[] = argsParser.getTargetBytes();
		float[] bps = argsParser.getBitsPerSample();
		
		int numberOfLayers = argsParser.getNumberOfLayers();
		int layerCreationType = argsParser.getLayerCreationType();
		int layerSizeType = argsParser.getLayerSizeType();
		int layerBytes[] = argsParser.getLayerBytes();
		
		int test3d = argsParser.getTest3d();
		int spectralWTLevels = argsParser.getSpectralWTLevels();
		int spectralWTType = argsParser.getSpectralWTType();
		
		//TER coder
		Coder idcCoder = new Coder(image.getImage(), image.getTypes(), image.getRGBComponents());
		
		if (pixelBitDepth == null){
			pixelBitDepth=image.getPixelBitDepth();
		}
		if (signedPixels==null){
			signedPixels=image.getSignedPixels();
		}
		image = null;
		System.gc(); //Free image load memory
		try{
			idcCoder.setParameters( outputFile , 
					outputFileType, 
					imageExtensionType,
					WTType,
					WTLevels,
					WTOrder,
					customWtFlag,
					customWeight,
					part2Flag,
					part3Flag,
					part4Flag,
					segByteLimit,
					DCStop,
					bitPlaneStop,
					stageStop,
					useFill,
					blocksPerSegment,
					optDCSelect,
					optACSelect,
					signedPixels,
					transposeImg,
					codeWordLength,
					pixelBitDepth,
					gaggleDCSize, gaggleACSize,
					idDC, idAC, desiredDistortion,
					distortionMeasure, entropyAC,
					resolutionLevels, compressionFactor,
					CVerbose,bps,
					truncationPoints,adjustHeaderParameters,
					progressionOrder, 
					LSType, LSComponents, LSSubsValues,
					coefficientsApproximationTypes,
					targetBytes, bpppb,
					numberOfLayers,layerCreationType,
					layerSizeType, layerBytes, 
					test3d, spectralWTLevels, spectralWTType
			);
		}catch(Exception e){
			e.printStackTrace();
			System.err.println("TERcoder PARAMETERS ERROR: " + e.getMessage());
			System.exit(3);
		}
		try{
			idcCoder.run();
		}catch(Exception e){
			e.printStackTrace();
			System.err.println("TERcoder RUNNING ERROR: " + e.getMessage());
			System.exit(4);
		}
		
		
	}   
}