
import Jama.Matrix;
import Jama.SingularValueDecomposition;
import ij.io.OpenDialog;
import java.awt.Graphics2D;
import java.awt.Panel;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


public class Secret_fragment_visible_mosaic_image_creation_RGB 
{   
    private static BufferedImage image_oi;
    private static BufferedImage image_Secrete;
    private static BufferedImage image_Target;
    static int row_tile_size=8;
    static int col_tile_size=8;   
    private static BufferedImage F,F_eq3,F_90,F_180,F_270;

    private static OpenDialog secrete_i,target_i;
    private static BufferedImage F_0;
    private static BufferedImage F_Target;
    private static BufferedImage F_rec_from_Tiles;
    private static BufferedImage F_Target__rec_from_Tiles;
    public static void main (String args[])   {
          
          //1 Divide secret image S into a sequence of n tile images of size NT, denoted as Stile =
//{T1, T2, …, Tn}; and divide target image T into another sequence of n target blocks.
         // select secrete iamge
          secrete_i=new ij.io.OpenDialog("Select Secrete image","E:\\DATA\\YES_MAN\\JAVA_IMPL\\", "lenna.bmp");
          File f_oi;
        
        try {f_oi=new File(secrete_i.getDirectory()+secrete_i.getFileName());
        image_Secrete = ImageIO.read(f_oi);
        } catch (IOException e) {}
        
        
         // select target iamge
        target_i=new ij.io.OpenDialog("Select Target image","E:\\DATA\\YES_MAN\\JAVA_IMPL\\", "lenna.bmp");
         
        try {f_oi=new File(target_i.getDirectory()+target_i.getFileName());
        image_Target = ImageIO.read(f_oi);
        } catch (IOException e) {}
        
        
         //transform image using  rotation scaling shifting
        BufferedImage transformedtarget=ColorTransform(image_Secrete,image_Target);
        
        
       int lim_c=image_Secrete.getWidth()-(image_Secrete.getWidth()%col_tile_size) ;//columns
       int lim_r=image_Secrete.getHeight()-(image_Secrete.getHeight()%row_tile_size) ;//rows
       int blk_no=0;
        
       
        
        for (int j=0; j<lim_c  && j%col_tile_size==0 ;j=j+col_tile_size)
            for(int i=0; i<lim_r  && i%row_tile_size==0;i=i+row_tile_size) 
          {                       
           blk_no++;   
          }
        final int TOTAL_NO_BLOKS=blk_no;      
        
        int Secrete_Image_tiles_Label_Index[]=new int[TOTAL_NO_BLOKS];
        int Target_Image_tiles_Label_Index[]=new int[TOTAL_NO_BLOKS];
        
        
        double meanRed_Secrete[]=new double[TOTAL_NO_BLOKS];
        double meanGreen_Secrete[]=new double[TOTAL_NO_BLOKS];
        double meanBlue_Secrete[]=new double[TOTAL_NO_BLOKS];       
        double std_dev_Red_Secrete[]=new double[TOTAL_NO_BLOKS];
        double std_dev_Green_Secrete[]=new double[TOTAL_NO_BLOKS];
        double std_dev_Blue_Secrete[]=new double[TOTAL_NO_BLOKS];
        double avg_std_dev_Secrete[]=new double[TOTAL_NO_BLOKS];
        
        
        double meanRed_Target[]=new double[TOTAL_NO_BLOKS];
        double meanGreen_Target[]=new double[TOTAL_NO_BLOKS];
        double meanBlue_Target[]=new double[TOTAL_NO_BLOKS];        
        double std_dev_Red_Target[]=new double[TOTAL_NO_BLOKS];
        double std_dev_Green_Target[]=new double[TOTAL_NO_BLOKS];
        double std_dev_Blue_Target[]=new double[TOTAL_NO_BLOKS];
        double avg_std_dev_Target[]=new double[TOTAL_NO_BLOKS];
        
        
        blk_no=0;     
        
        for (int j=0; j<lim_c  && j%col_tile_size==0 ;j=j+col_tile_size)
            for(int i=0; i<lim_r  && i%row_tile_size==0;i=i+row_tile_size) 
          {   //System.out.println("Block no="+blk_no);
              BufferedImage subimage_image_Secrete = image_Secrete.getSubimage(j, i, col_tile_size, row_tile_size);
              BufferedImage subimage_image_Target = image_Target.getSubimage(j, i, col_tile_size, row_tile_size);
              Secrete_Image_tiles_Label_Index[blk_no]=blk_no;
              Target_Image_tiles_Label_Index[blk_no]=blk_no;
              
              
              //secrete image
              //mean 
        
              meanRed_Secrete[blk_no]=mean(subimage_image_Secrete,1);
              meanGreen_Secrete[blk_no]=mean(subimage_image_Secrete,2);
              meanBlue_Secrete[blk_no]=mean(subimage_image_Secrete,3);
              
              //std dev 
              std_dev_Red_Secrete[blk_no]=std_dev(subimage_image_Secrete,1);
              std_dev_Green_Secrete[blk_no]=std_dev(subimage_image_Secrete,2);
              std_dev_Blue_Secrete[blk_no]=std_dev(subimage_image_Secrete,3);
              
              //avg std dev
              avg_std_dev_Secrete[blk_no]= (std_dev_Red_Secrete[blk_no]+std_dev_Green_Secrete[blk_no]+std_dev_Blue_Secrete[blk_no])/3.0;
              
             
             
             //target image
              //mean 
              meanRed_Target[blk_no]=mean(subimage_image_Target,1);
              meanGreen_Target[blk_no]=mean(subimage_image_Target,2);
              meanBlue_Target[blk_no]=mean(subimage_image_Target,3);
              
              //std dev 
              std_dev_Red_Target[blk_no]=std_dev(subimage_image_Target,1);
              std_dev_Green_Target[blk_no]=std_dev(subimage_image_Target,2);
              std_dev_Blue_Target[blk_no]=std_dev(subimage_image_Target,3);
              
              //avg std dev
              avg_std_dev_Target[blk_no]= (std_dev_Red_Target[blk_no]+std_dev_Green_Target[blk_no]+std_dev_Blue_Target[blk_no])/3.0;
              
            
              blk_no++;   
          }

        
        
        //Sort the blocks in  according to the average standard deviation
//values of the blocks; map in order the blocks in the sorted S
 //to those in the
//sorted   sorted Starget in a 1-to-1 manner; and reorder the mappings according to the indices of the tile images into a mapping sequence L of the form of T1 → Bj , T2 → Bj , etc   

        //sort according to avg _std_dev 
       //  sort Secrete
       selectionsort Secrete=new selectionsort(avg_std_dev_Secrete); 
       //Secrete.dispaly();
      //sort target
       selectionsort Target=new selectionsort(avg_std_dev_Target); 
       //Target.dispaly();
        
       // int mappingsequenceL[]=new int [blk_no];  
        //create mosaic image F from T as per sorted order
       
       //convert to 1D :T and F
       int F_1D_red[][]=new int[TOTAL_NO_BLOKS][row_tile_size*col_tile_size];
       int Secrete_1D_red[][]=new int[TOTAL_NO_BLOKS][row_tile_size*col_tile_size];
       int Target_1D_red[][]=new int[TOTAL_NO_BLOKS][row_tile_size*col_tile_size];
       
       int F_1D_green[][]=new int[TOTAL_NO_BLOKS][row_tile_size*col_tile_size];
       int Secrete_1D_green[][]=new int[TOTAL_NO_BLOKS][row_tile_size*col_tile_size];
       int Target_1D_green[][]=new int[TOTAL_NO_BLOKS][row_tile_size*col_tile_size];
       
       int F_1D_blue[][]=new int[TOTAL_NO_BLOKS][row_tile_size*col_tile_size];
       int Secrete_1D_blue[][]=new int[TOTAL_NO_BLOKS][row_tile_size*col_tile_size];
       int Target_1D_blue[][]=new int[TOTAL_NO_BLOKS][row_tile_size*col_tile_size];
       
              
       int F_Target_1D_red[][]=new int[TOTAL_NO_BLOKS][row_tile_size*col_tile_size];
       int F_Target_1D_green[][]=new int[TOTAL_NO_BLOKS][row_tile_size*col_tile_size];
       int F_Target_1D_blue[][]=new int[TOTAL_NO_BLOKS][row_tile_size*col_tile_size];
       
       blk_no=0;
       int rgb=0,red=0,green=0,blue=0;
        F = new BufferedImage(image_Target.getWidth(), image_Target.getHeight(),  BufferedImage.TYPE_INT_ARGB);
        F_Target = new BufferedImage(image_Target.getWidth(), image_Target.getHeight(),  BufferedImage.TYPE_INT_ARGB);
      
       for (int j=0; j<lim_c  && j%col_tile_size==0 ;j=j+col_tile_size)
          for(int i=0; i<lim_r  && i%row_tile_size==0;i=i+row_tile_size) 
          {   //System.out.println("Block no="+blk_no);
              BufferedImage F_subimage = F.getSubimage(j, i, col_tile_size, row_tile_size);
              BufferedImage subimage_image_Secrete = image_Secrete.getSubimage(j, i, col_tile_size, row_tile_size);
              BufferedImage subimage_image_Target = image_Target.getSubimage(j, i, col_tile_size, row_tile_size);
              int blk_pixelcount=0;
              for(int mj=0;mj<col_tile_size;mj++)
               for(int mi=0;mi<row_tile_size;mi++)
               { rgb=subimage_image_Secrete.getRGB(mj, mi);
                red=(rgb & 0x00FF0000)  >>> 16;           
                green=(rgb & 0x0000FF00)  >>> 8;
                blue=(rgb & 0x000000FF)  >>> 0;
               // F_1D_red[blk_no][blk_pixelcount]=red;
               // F_1D_green[blk_no][blk_pixelcount]=green;
                //F_1D_blue[blk_no][blk_pixelcount]=blue;
                
                rgb=subimage_image_Secrete.getRGB(mj, mi);
                red=(rgb & 0x00FF0000)  >>> 16;           
                green=(rgb & 0x0000FF00)  >>> 8;
                blue=(rgb & 0x000000FF)  >>> 0;
                Secrete_1D_red[blk_no][blk_pixelcount]=red;
                Secrete_1D_green[blk_no][blk_pixelcount]=green;
                Secrete_1D_blue[blk_no][blk_pixelcount]=blue;
                
                rgb=subimage_image_Target.getRGB(mj, mi);
                red=(rgb & 0x00FF0000)  >>> 16;           
                green=(rgb & 0x0000FF00)  >>> 8;
                blue=(rgb & 0x000000FF)  >>> 0;
                Target_1D_red[blk_no][blk_pixelcount]=red;
                Target_1D_green[blk_no][blk_pixelcount]=green;
                Target_1D_blue[blk_no][blk_pixelcount]=blue;                
                
                
                
                blk_pixelcount++;   
               }
              //get next subimage of Secrete as per sorted order
             // F_subimage.set
              //BufferedImage subimage_image_Secrete = image_Secrete.getSubimage(j, i, col_tile_size, row_tile_size);
              blk_no++;
          }
        
        //now form Image F 
       
       
    
       for(blk_no=0;blk_no<TOTAL_NO_BLOKS;blk_no++)
       { //get  next block of secrete image into F
           for(int blk_pixelcount=0;blk_pixelcount<(col_tile_size*row_tile_size);blk_pixelcount++ )
           {F_1D_red[blk_no][blk_pixelcount]=Secrete_1D_red[Secrete.INDEX[blk_no]][blk_pixelcount];
           F_1D_green[blk_no][blk_pixelcount]=Secrete_1D_green[Secrete.INDEX[blk_no]][blk_pixelcount];
           F_1D_blue[blk_no][blk_pixelcount]=Secrete_1D_blue[Secrete.INDEX[blk_no]][blk_pixelcount];
           
           
           F_Target_1D_red[blk_no][blk_pixelcount]=Target_1D_red[Target.INDEX[blk_no]][blk_pixelcount];
           F_Target_1D_green[blk_no][blk_pixelcount]=Target_1D_green[Target.INDEX[blk_no]][blk_pixelcount];
           F_Target_1D_blue[blk_no][blk_pixelcount]=Target_1D_blue[Target.INDEX[blk_no]][blk_pixelcount];
            
           }
       }
          
       //convert F to 2D
       blk_no=0;
       for (int j=0; j<lim_c  && j%col_tile_size==0 ;j=j+col_tile_size)
          for(int i=0; i<lim_r  && i%row_tile_size==0;i=i+row_tile_size) 
          {   //System.out.println("Block no="+blk_no);
              BufferedImage F_subimage = F.getSubimage(j, i, col_tile_size, row_tile_size);
              BufferedImage F_Target_subimage = F_Target.getSubimage(j, i, col_tile_size, row_tile_size);
               int blk_pixelcount=0;
              for(int mj=0;mj<col_tile_size;mj++)
               for(int mi=0;mi<row_tile_size;mi++)
               { int alpha =255;          
                red=F_1D_red[blk_no][blk_pixelcount];
                green=F_1D_green[blk_no][blk_pixelcount];
                blue=F_1D_blue[blk_no][blk_pixelcount];
                rgb=(alpha << 24) | (red << 16) | (green << 8) | blue;                
                F_subimage.setRGB(mj, mi,rgb);
                
                
                alpha =255;          
                red=F_Target_1D_red[blk_no][blk_pixelcount];
                green=F_Target_1D_green[blk_no][blk_pixelcount];
                blue=F_Target_1D_blue[blk_no][blk_pixelcount];
                rgb=(alpha << 24) | (red << 16) | (green << 8) | blue;                
                F_Target_subimage.setRGB(mj, mi,rgb);
                
                blk_pixelcount++;   
               }
              //get next subimage of Secrete as per sorted order
             // F_subimage.set
              //BufferedImage subimage_image_Secrete = image_Secrete.getSubimage(j, i, col_tile_size, row_tile_size);
              blk_no++;
          
          }
        
        //Secrete.dispaly();
       
       
       //save F
       try {    
          ImageIO.write(F, "png", new File(secrete_i.getDirectory()+"F.png"));
      }catch (IOException e) {} 
       
        try {    
          ImageIO.write(F_Target, "png", new File(secrete_i.getDirectory()+"F_Target.png"));
      }catch (IOException e) {} 
       

       //recover from tiles original images ... secrte and target
        
       int F_1D_red_rec_from_Tiles[][]=new int[TOTAL_NO_BLOKS][row_tile_size*col_tile_size];
       int Secrete_1D_red_rec_from_Tiles[][]=new int[TOTAL_NO_BLOKS][row_tile_size*col_tile_size];
       int Target_1D_red_rec_from_Tiles[][]=new int[TOTAL_NO_BLOKS][row_tile_size*col_tile_size];
       
       int F_1D_green_rec_from_Tiles[][]=new int[TOTAL_NO_BLOKS][row_tile_size*col_tile_size];
       int Secrete_1D_green_rec_from_Tiles[][]=new int[TOTAL_NO_BLOKS][row_tile_size*col_tile_size];
       int Target_1D_green_rec_from_Tiles[][]=new int[TOTAL_NO_BLOKS][row_tile_size*col_tile_size];
       
       int F_1D_blue_rec_from_Tiles[][]=new int[TOTAL_NO_BLOKS][row_tile_size*col_tile_size];
       int Secrete_1D_blue_rec_from_Tiles[][]=new int[TOTAL_NO_BLOKS][row_tile_size*col_tile_size];
       int Target_1D_blue_rec_from_Tiles[][]=new int[TOTAL_NO_BLOKS][row_tile_size*col_tile_size];
       
              
       int F_Target_1D_red_rec_from_Tiles[][]=new int[TOTAL_NO_BLOKS][row_tile_size*col_tile_size];
       int F_Target_1D_green_rec_from_Tiles[][]=new int[TOTAL_NO_BLOKS][row_tile_size*col_tile_size];
       int F_Target_1D_blue_rec_from_Tiles[][]=new int[TOTAL_NO_BLOKS][row_tile_size*col_tile_size];
       
        
        
        
        
        
        
/////////////////
        int binary_index_of_Target[];
        int binary_optimal_rotation_angle_of_Secrete[];
        int binary_means_of_Secrete[];
        int binary_means_of_Target[]; 
        int binary_standard_deviation_quotients[];
        int binary_overflow_underflow_residuals[];
        int M_string_of_Secrete[];
        int M_Total_string_of_Secrete[][];
        int M_key_modified_Total_string_of_Secrete[][];

        F_eq3 = new BufferedImage(image_Target.getWidth(), image_Target.getHeight(),  BufferedImage.TYPE_INT_ARGB);
        
        F_90 = new BufferedImage(image_Target.getWidth(), image_Target.getHeight(),  BufferedImage.TYPE_INT_ARGB);
        F_180 = new BufferedImage(image_Target.getWidth(), image_Target.getHeight(),  BufferedImage.TYPE_INT_ARGB);
        F_270 = new BufferedImage(image_Target.getWidth(), image_Target.getHeight(),  BufferedImage.TYPE_INT_ARGB);
        F_0 = new BufferedImage(image_Target.getWidth(), image_Target.getHeight(),  BufferedImage.TYPE_INT_ARGB);
    
        blk_no=0;
       for (int j=0; j<lim_c  && j%col_tile_size==0 ;j=j+col_tile_size)
          for(int i=0; i<lim_r  && i%row_tile_size==0;i=i+row_tile_size) 
          {   ////System.out.println("Block no="+blk_no);
              //Stage 1.2－ performing color conversion between the tile images and target blocks.
          
              
         
              
              /*
               * 6. For each pair Ti → Bji in mapping sequence L, let the means μc and μc′ of Ti and Bji
respectively be represented by 8 bits with values 0~255 and the standard deviation
quotients qc = σc′/σc by 7 bits with values 0.1~12.8 where c = r, g, b.


               */
              //represent means of T i.e F and B in 8 bits 0-255
          //******************************************* to 
              
             
               //System.out.println("Block no="+blk_no);
              BufferedImage subimage_image_Secrete = F.getSubimage(j, i, col_tile_size, row_tile_size);
              BufferedImage subimage_image_Target = F_Target.getSubimage(j, i, col_tile_size, row_tile_size);
            
              
              
              //secrete image
              //mean 
        
              meanRed_Secrete[blk_no]=mean(subimage_image_Secrete,1);
              meanGreen_Secrete[blk_no]=mean(subimage_image_Secrete,2);
              meanBlue_Secrete[blk_no]=mean(subimage_image_Secrete,3);
              
              //std dev 
              std_dev_Red_Secrete[blk_no]=std_dev(subimage_image_Secrete,1);
              std_dev_Green_Secrete[blk_no]=std_dev(subimage_image_Secrete,2);
              std_dev_Blue_Secrete[blk_no]=std_dev(subimage_image_Secrete,3);
              
              //avg std dev
              avg_std_dev_Secrete[blk_no]= (std_dev_Red_Secrete[blk_no]+std_dev_Green_Secrete[blk_no]+std_dev_Blue_Secrete[blk_no])/3.0;
              
             
             
             //target image
              //mean 
              meanRed_Target[blk_no]=mean(subimage_image_Target,1);
              meanGreen_Target[blk_no]=mean(subimage_image_Target,2);
              meanBlue_Target[blk_no]=mean(subimage_image_Target,3);
              
              //std dev 
              std_dev_Red_Target[blk_no]=std_dev(subimage_image_Target,1);
              std_dev_Green_Target[blk_no]=std_dev(subimage_image_Target,2);
              std_dev_Blue_Target[blk_no]=std_dev(subimage_image_Target,3);
              
              //avg std dev
              avg_std_dev_Target[blk_no]= (std_dev_Red_Target[blk_no]+std_dev_Green_Target[blk_no]+std_dev_Blue_Target[blk_no])/3.0;
              
              
              //////////////
              
              int rsm=(int) meanRed_Secrete[blk_no];
              int gsm=(int) meanGreen_Secrete[blk_no];
              int bsm=(int) meanBlue_Secrete[blk_no];
              //System.out.println(""+rsm+" "+gsm+" "+bsm);
              
              toBinary rsm8_Secrete=new  toBinary(rsm);
              toBinary gsm8_Secrete=new  toBinary(gsm);
              toBinary bsm8_Secrete=new  toBinary(bsm);
             
              
              int rtm=(int) meanRed_Target[blk_no];
              int gtm=(int) meanGreen_Target[blk_no];
              int btm=(int) meanBlue_Target[blk_no];
              
              toBinary rtm8_Target=new  toBinary(rtm);
              toBinary gtm8_Target=new  toBinary(gtm);
              toBinary btm8_Target=new  toBinary(btm);
             
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
              int r_std_dev=(int) (10.0*(std_dev_Red_Target[blk_no]/std_dev_Red_Secrete[blk_no])-1.0);
              int g_std_dev=(int) (10.0*(std_dev_Green_Target[blk_no]/std_dev_Green_Secrete[blk_no])-1.0);
              int b_std_dev=(int) (10.0*(std_dev_Blue_Target[blk_no]/std_dev_Blue_Secrete[blk_no])-1.0);
              
              //System.out.println(""+r_std_dev+" "+g_std_dev+" "+b_std_dev);
     //         toBinary r7_std_dev_Secrete=new  toBinary(r_std_dev);
//              toBinary g7_std_dev_Secrete=new  toBinary(g_std_dev);
             // toBinary b7_std_dev_Secrete=new  toBinary(b_std_dev);
             
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
            
               int blk_pixelcount=0;
              for(int mj=0;mj<col_tile_size;mj++)
               for(int mi=0;mi<row_tile_size;mi++)
               { int alpha =255; 
                rgb=F.getRGB(mj+j, mi+i); 
                red=(rgb & 0x00FF0000)  >>> 16;           
                green=(rgb & 0x0000FF00)  >>> 8;
                blue=(rgb & 0x000000FF)  >>> 0;
                 
                
                //(σ c' / σ c )(ci − μc ) + μc'
                //red=(int) (((std_dev_Red_Target[Target.INDEX[blk_no]])/(std_dev_Red_Secrete[Secrete.INDEX[blk_no]]))*(red-meanRed_Secrete[Secrete.INDEX[blk_no]])+meanRed_Target[Target.INDEX[blk_no]]);
                //green=(int) (((std_dev_Green_Target[Target.INDEX[blk_no]])/(std_dev_Green_Secrete[Secrete.INDEX[blk_no]]))*(green-meanGreen_Secrete[Secrete.INDEX[blk_no]])+meanGreen_Target[Target.INDEX[blk_no]]);
                //blue=(int) (((std_dev_Blue_Target[Target.INDEX[blk_no]])/(std_dev_Blue_Secrete[Secrete.INDEX[blk_no]]))*(blue-meanBlue_Secrete[Secrete.INDEX[blk_no]])+meanBlue_Target[Target.INDEX[blk_no]]);
                if(std_dev_Red_Secrete[blk_no]<=0) std_dev_Red_Secrete[blk_no]=0.1;
                if(std_dev_Red_Secrete[blk_no]>=12.8) std_dev_Red_Secrete[blk_no]=12.8;
                
                if(std_dev_Green_Secrete[blk_no]<=0) std_dev_Green_Secrete[blk_no]=0.1;
                if(std_dev_Green_Secrete[blk_no]>=12.8) std_dev_Green_Secrete[blk_no]=12.8;
                
                
                if(std_dev_Blue_Secrete[blk_no]<=0) std_dev_Blue_Secrete[blk_no]=0.1;
                if(std_dev_Blue_Secrete[blk_no]>=12.8) std_dev_Blue_Secrete[blk_no]=12.8;
                
                red=(int)   ( (std_dev_Red_Target[blk_no]/std_dev_Red_Secrete[blk_no])*(red-meanRed_Secrete[blk_no])+meanRed_Target[blk_no]);
                green=(int) ((std_dev_Green_Target[blk_no]/std_dev_Green_Secrete[blk_no])*(green-meanGreen_Secrete[blk_no])+meanGreen_Target[blk_no]);
                blue=(int)  ((std_dev_Blue_Target[blk_no]/std_dev_Blue_Secrete[blk_no])*(blue-meanBlue_Secrete[blk_no])+meanBlue_Target[blk_no]);
                
                //red=(int) (((std_dev_Red_Target[blk_no])/(std_dev_Red_Secrete[blk_no]))*(red-meanRed_Secrete[blk_no]));
                //green=(int) (((std_dev_Green_Target[blk_no])/(std_dev_Green_Secrete[blk_no]))*(green-meanGreen_Secrete[blk_no]));
                //blue=(int) (((std_dev_Blue_Target[blk_no])/(std_dev_Blue_Secrete[blk_no]))*(blue-meanBlue_Secrete[blk_no]));
                
                //System.out.println("RED="+red+"  GREEN="+green+"  BLUE="+blue);
                //overflow
                if(red>255)  {red=(int) meanRed_Secrete[blk_no];}
                if(green>255){green=(int) meanGreen_Secrete[blk_no];}
                if(blue>255) {blue=(int) meanBlue_Secrete[blk_no];}
                //underflow
                
                
                if(red<0)   {red=(int) meanRed_Secrete[blk_no];}
                if(green<0) {green=(int) meanGreen_Secrete[blk_no];}
                if(blue<0)   {blue=(int) meanBlue_Secrete[blk_no];}
                
                
                
                
                
                rgb=(alpha << 24) | (red << 16) | (green << 8) | blue;
                
                F_eq3_subimage.setRGB(mj, mi,rgb);
                F_0_subimage.setRGB(mj, mi,rgb);
                F_90_subimage.setRGB(mj, mi,rgb);
                F_180_subimage.setRGB(mj, mi,rgb);
                F_270_subimage.setRGB(mj, mi,rgb);
                F_Target.setRGB(j+mj, i+mi, rgb);
                blk_pixelcount++;   
               }
              //Stage 1.3 - rotating the tile images.
//8. Compute the RMSE values of each color-transformed tile image Ti in F with
//respect to its corresponding target block Bji after rotating Ti into the directions 0o,
//90o, 180o and 270o; and rotate Ti into the optimal direction θo with the smallest
//RMSE value.
              
              BufferedImage rotateMyImage0 = rotate(F_0_subimage,0.0);
              BufferedImage rotateMyImage90 = rotate(F_90_subimage,90.0);
              BufferedImage rotateMyImage180= rotate(F_180_subimage,180.0);
              BufferedImage rotateMyImage270 = rotate(F_270_subimage,270.0);
              for(int mj=0;mj<col_tile_size;mj++)
               for(int mi=0;mi<row_tile_size;mi++)
               { rgb=rotateMyImage0.getRGB(mj, mi);
                int alpha=255;
                red=(rgb & 0x00FF0000)  >>> 16;           
                green=(rgb & 0x0000FF00)  >>> 8;
                blue=(rgb & 0x000000FF)  >>> 0;
                rgb=(alpha << 24) | (red << 16) | (green << 8) | blue;
                F_0_subimage.setRGB(mj, mi, rgb);
                
                
                rgb=rotateMyImage90.getRGB(mj, mi);
                alpha=255;
                red=(rgb & 0x00FF0000)  >>> 16;           
                green=(rgb & 0x0000FF00)  >>> 8;
                blue=(rgb & 0x000000FF)  >>> 0;
                rgb=(alpha << 24) | (red << 16) | (green << 8) | blue;
                F_90_subimage.setRGB(mj, mi, rgb);
                
                
                rgb=rotateMyImage180.getRGB(mj, mi);
                alpha=255;
                red=(rgb & 0x00FF0000)  >>> 16;           
                green=(rgb & 0x0000FF00)  >>> 8;
                blue=(rgb & 0x000000FF)  >>> 0;
                rgb=(alpha << 24) | (red << 16) | (green << 8) | blue;
                F_180_subimage.setRGB(mj, mi, rgb);
                
                
                rgb=rotateMyImage270.getRGB(mj, mi);
                alpha=255;
                red=(rgb & 0x00FF0000)  >>> 16;           
                green=(rgb & 0x0000FF00)  >>> 8;
                blue=(rgb & 0x000000FF)  >>> 0;
                rgb=(alpha << 24) | (red << 16) | (green << 8) | blue;
                F_270_subimage.setRGB(mj, mi, rgb);
                
                
                
                
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
              
              
          
          blk_no++;
          }
       
       
       //save F_eq3
       try {    
          ImageIO.write(F_eq3, "png", new File(secrete_i.getDirectory()+"F_eq3.png"));
      }catch (IOException e) {} 
       
       
       
        try {    
          ImageIO.write(F_0, "png", new File(secrete_i.getDirectory()+"F_0.png"));
      }catch (IOException e) {} 
       
         try {    
          ImageIO.write(F_90, "png", new File(secrete_i.getDirectory()+"F_90.png"));
      }catch (IOException e) {} 
       
          try {    
          ImageIO.write(F_180, "png", new File(secrete_i.getDirectory()+"F_180.png"));
      }catch (IOException e) {} 
           try {    
          ImageIO.write(F_270, "png", new File(secrete_i.getDirectory()+"F_270.png"));
      }catch (IOException e) {} 
       
       blk_no=0;
       rgb=0;red=0;green=0;blue=0;
        F_rec_from_Tiles = new BufferedImage(image_Target.getWidth(), image_Target.getHeight(),  BufferedImage.TYPE_INT_ARGB);
        F_Target__rec_from_Tiles = new BufferedImage(image_Target.getWidth(), image_Target.getHeight(),  BufferedImage.TYPE_INT_ARGB);
      
        
      //get subimages of tiles into array of BufferedImages
        BufferedImage F_ARRAY[] = new BufferedImage [TOTAL_NO_BLOKS]; 
        
       blk_no=0;
       for (int j=0; j<lim_c  && j%col_tile_size==0 ;j=j+col_tile_size)
          for(int i=0; i<lim_r  && i%row_tile_size==0;i=i+row_tile_size) 
          {   //System.out.println("Block no="+blk_no);
             F_ARRAY[Secrete.INDEX[blk_no]]=F.getSubimage(j, i, col_tile_size, row_tile_size);
             blk_no++;
          }
       
       blk_no=0;
       for (int j=0; j<lim_c  && j%col_tile_size==0 ;j=j+col_tile_size)
          for(int i=0; i<lim_r  && i%row_tile_size==0;i=i+row_tile_size) 
          {   //System.out.println("Block no="+blk_no);
             for(int mj=0;mj<col_tile_size;mj++)
               for(int mi=0;mi<row_tile_size;mi++)
               {rgb=F_ARRAY[blk_no].getRGB(mj, mi); 
                F_rec_from_Tiles.setRGB(j+mj, i+mi, rgb);
               }
             
            
             blk_no++;
          }
        
       //get subimages of target tiles into array of BufferedImages
        BufferedImage[] F_ARRAY1= new BufferedImage [TOTAL_NO_BLOKS]; 
        
       blk_no=0;
       for (int j=0; j<lim_c  && j%col_tile_size==0 ;j=j+col_tile_size)
          for(int i=0; i<lim_r  && i%row_tile_size==0;i=i+row_tile_size) 
          {   //System.out.println("Block no="+blk_no);
             F_ARRAY1[Target.INDEX[blk_no]]=F_Target.getSubimage(j, i, col_tile_size, row_tile_size);
             blk_no++;
          }
       
       blk_no=0;
       for (int j=0; j<lim_c  && j%col_tile_size==0 ;j=j+col_tile_size)
          for(int i=0; i<lim_r  && i%row_tile_size==0;i=i+row_tile_size) 
          {   //System.out.println("Block no="+blk_no);
             for(int mj=0;mj<col_tile_size;mj++)
               for(int mi=0;mi<row_tile_size;mi++)
               {rgb=F_ARRAY1[blk_no].getRGB(mj, mi); 
                F_Target__rec_from_Tiles.setRGB(j+mj, i+mi, rgb);
               }
             
            
             blk_no++;
          }
        
     
       
       
       //save F
       try {    
          ImageIO.write(F_rec_from_Tiles, "png", new File(secrete_i.getDirectory()+"F_rec_from_Tiles.png"));
      }catch (IOException e) {} 
       
        try {    
          ImageIO.write(F_Target__rec_from_Tiles, "png", new File(secrete_i.getDirectory()+"F_Target__rec_from_Tiles.png"));
      }catch (IOException e) {} 
        
        
     
           
        //System.out.println("Size of  Secrete Image:"+"ROWS="+image_Secrete.getHeight()+"\tCOLUMNS="+image_Secrete.getWidth());
        //System.out.println("Toatal_no_blks="+TOTAL_NO_BLOKS);
        
        
        
        display_images();
     //System.out.println("END OF ALG");
          
      }
  private static void display_images() {
    
    
    JFrame frame_gi = new JFrame("Secrete  image");
    Panel panel_gi = new ShowImage(secrete_i.getDirectory()+secrete_i.getFileName());
    frame_gi.getContentPane().add(panel_gi);
    frame_gi.setSize(500, 500);
    frame_gi.setVisible(true);
    
    
    JFrame frame_bi = new JFrame("Target image");
    Panel panel_bi = new ShowImage(target_i.getDirectory()+target_i.getFileName());
    frame_bi.getContentPane().add(panel_bi);
    frame_bi.setSize(500, 500);
    frame_bi.setVisible(true);  
    
    JFrame frame_gis = new JFrame("Secrete Tile image");
    Panel panel_gis = new ShowImage(secrete_i.getDirectory()+"F.png");
    frame_gis.getContentPane().add(panel_gis);
    frame_gis.setSize(500, 500);
    frame_gis.setVisible(true);
    
    
    JFrame frame_bit = new JFrame("Target tile image");
    Panel panel_bit = new ShowImage(target_i.getDirectory()+"F_Target.png");
    frame_bit.getContentPane().add(panel_bit);
    frame_bit.setSize(500, 500);
    frame_bit.setVisible(true);  
    
    
     
    
    
    JFrame frame_bi2 = new JFrame("mosaic Target image F after color transformation");
    Panel panel_bi2 = new ShowImage(secrete_i.getDirectory()+"F_Target__rec_from_Tiles.png");
    frame_bi2.getContentPane().add(panel_bi2);
    frame_bi2.setSize(500, 500);
    frame_bi2.setVisible(true);  
    
    
  }
  private static double mean(BufferedImage subimage_image_Secrete,int C) {
    //    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   double Mean=0;
   int red=0,green=0,blue=0,rgb=0;
   for(int i=0;i<subimage_image_Secrete.getWidth();i++)
       for(int j=0;j<subimage_image_Secrete.getHeight();j++)
       {
                 rgb=subimage_image_Secrete.getRGB(i, j); 
                 red=(rgb & 0x00FF0000)  >>> 16;           
                 green=(rgb & 0x0000FF00)  >>> 8;
                 blue=(rgb & 0x000000FF)  >>> 0; 
                  ////System.out.println(rgb);
                   ////System.out.println(green);
                 if(C==1)
                     Mean=Mean+red;
                 if(C==2)
                     Mean=Mean+green; 
                 if(C==3)
                     Mean=Mean+blue; 
       }
   Mean=Mean  / (subimage_image_Secrete.getWidth()* subimage_image_Secrete.getHeight());
   //System.out.println(Mean);
   return (Mean);
    }
  private static double std_dev(BufferedImage subimage_image_Secrete, int C) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
   double Mean=0;
   int red=0,green=0,blue=0,rgb=0;
   for(int i=0;i<subimage_image_Secrete.getWidth();i++)
       for(int j=0;j<subimage_image_Secrete.getHeight();j++)
       {
                 rgb=subimage_image_Secrete.getRGB(i, j); 
                 red=(rgb & 0x00FF0000)  >>> 16;           
                 green=(rgb & 0x0000FF00)  >>> 8;
                 blue=(rgb & 0x000000FF)  >>> 0; 
                 
                 if(C==1)
                     Mean=Mean+red;
                 if(C==2)
                     Mean=Mean+green; 
                 if(C==3)
                     Mean=Mean+blue; 
       }
   
   Mean=(Mean  / (subimage_image_Secrete.getWidth()* subimage_image_Secrete.getHeight()));
   
   
   
   double std_dev=0;
   double tmp=0,color_value=0;
    for(int i=0;i<subimage_image_Secrete.getWidth();i++)
       for(int j=0;j<subimage_image_Secrete.getHeight();j++)
       {        rgb=subimage_image_Secrete.getRGB(i, j); 
                 red=(rgb & 0x00FF0000)  >>> 16;           
                 green=(rgb & 0x0000FF00)  >>> 8;
                 blue=(rgb & 0x000000FF)  >>> 0; 
                 
                 if(C==1)
                     color_value=red;
                 if(C==2)
                     color_value=green; 
                 if(C==3)
                     color_value=blue;
           
                 
               tmp=tmp+ (color_value-Mean)*(color_value-Mean);
               
       }
   
    
   tmp=tmp/(subimage_image_Secrete.getWidth()* subimage_image_Secrete.getHeight());
   std_dev=Math.sqrt(tmp);
   //System.out.println(std_dev);
   return (std_dev );
    }
 
public static BufferedImage rotate(BufferedImage image_Secrete, double angle) {
 
       double t_r_src=0.0,t_g_src=0.0,t_b_src=0.0,t_r_tgt=0.0,t_g_tgt=0.0,t_b_tgt=0.0;
       double s_r_src=0.0,s_g_src=0.0,s_b_src=0.0,s_r_tgt=0.0,s_g_tgt=0.0,s_b_tgt=0.0;
       
       t_r_src=mean(image_Secrete,1);
       t_g_src=mean(image_Secrete,2);
       t_b_src=mean(image_Secrete,3);
       
       
       t_r_tgt=-mean(image_Secrete,1);
       t_g_tgt=-mean(image_Secrete,2);
       t_b_tgt=-mean(image_Secrete,3);
       
      
       
        //get covarince matrieces of both for R G B
       Matrix Cov_src=new Matrix(4,4);
 
       
        Matrix Cov_tgt=new Matrix(4,4);
    
        
        
        
        
        //get R G B og cuurent image into Matrix form
        Matrix src=new Matrix(image_Secrete.getHeight()*image_Secrete.getWidth(),4);
        
      
       
        Matrix tgt=new Matrix(image_Secrete.getHeight()*image_Secrete.getWidth(),4);
  
        
        //set values
        int rgb=0,red=0,green=0,blue=0;
        int row_no=0;
      for(int i=0;i<image_Secrete.getWidth();i++)
       for(int j=0;j<image_Secrete.getHeight();j++)
       {         rgb=image_Secrete.getRGB(i, j); 
                 red=(rgb & 0x00FF0000)  >>> 16;           
                 green=(rgb & 0x0000FF00)  >>> 8;
                 blue=(rgb & 0x000000FF)  >>> 0;                  
                 src.set(row_no, 0, red);
                 src.set(row_no, 1, green);
                 src.set(row_no, 2, blue);
                 src.set(row_no, 3, 1);
                 
                 
                  rgb=image_Target.getRGB(i, j); 
                 red=(rgb & 0x00FF0000)  >>> 16;           
                 green=(rgb & 0x0000FF00)  >>> 8;
                 blue=(rgb & 0x000000FF)  >>> 0;                  
                 tgt.set(row_no, 0, red);
                 tgt.set(row_no, 1, green);
                 tgt.set(row_no, 2, blue);
                 tgt.set(row_no, 3, 1);
                 row_no++;
                 
       }

   MeanAndCovariance mc_src=new MeanAndCovariance(src);  
   MeanAndCovariance mc_tgt=new MeanAndCovariance(tgt);  
   
   Cov_src=mc_src.getCovariance();
   Cov_tgt=mc_tgt.getCovariance();
//use svd to decompose covariance matrices
        SingularValueDecomposition svd_Cov_src = Cov_src.svd();
         
        
        SingularValueDecomposition svd_Cov_tgt = Cov_tgt.svd();
        
        
        //form translation rotation and scaling matrix for images

        Matrix T_src=new Matrix(4,4);
        Matrix R_src=new Matrix(4,4);
        Matrix S_src=new Matrix(4,4);
        
        Matrix T_tgt=new Matrix(4,4);
        Matrix R_tgt=new Matrix(4,4);
        Matrix S_tgt=new Matrix(4,4);
        
        
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
        T_src.set(0, 3,array[0][0] );
        T_src.set(1, 3,array[0][1]);
        T_src.set(2, 3,array[0][2]);
        T_src.set(3, 3, 1.0);
        
        for(int i=0;i<4;i++)
        for(int j=0;j<4;j++)
            
        {//T_src.print(i, j);
            
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
        T_tgt.set(0, 3,-array[0][0] );
        T_tgt.set(1, 3,-array[0][1]);
        T_tgt.set(2, 3,-array[0][2]);
        T_tgt.set(3, 3, 1.0);
        
        //T_tgt.print(1, 10);
        
        
        //for Rotation
        R_src=svd_Cov_src.getU();
        R_tgt=svd_Cov_tgt.getU().inverse();
        
        //for scaling
         
        S_src.set(0,0,svd_Cov_src.getS().get(0, 0));
        S_src.set(1,0,0);
        S_src.set(2,0,0);
        S_src.set(3,0,0);
        
        S_src.set(0,1,0);
        S_src.set(1,1,svd_Cov_src.getS().get(1, 1));
        S_src.set(2,1,0);
        S_src.set(3,1,0);
        
        S_src.set(0,2,0);
        S_src.set(1,2,0);
        S_src.set(2,2,svd_Cov_src.getS().get(2, 2));
        S_src.set(3,2,0);
        
        S_src.set(0,3,0);
        S_src.set(1,3,0);
        S_src.set(2,3,0);
        S_src.set(3,3,1);
        
        
        
        
        
        
        
        S_tgt.set(0,0,1.0/Math.sqrt(svd_Cov_tgt.getS().get(0, 0)));
        S_tgt.set(1,0,0);
        S_tgt.set(2,0,0);
        S_tgt.set(3,0,0);
        
        S_tgt.set(0,1,0);
        S_tgt.set(1,1,1.0/Math.sqrt(svd_Cov_tgt.getS().get(1, 1)));
        S_tgt.set(2,1,0);
        S_tgt.set(3,1,0);
        
        S_tgt.set(0,2,0);
        S_tgt.set(1,2,0);
        S_tgt.set(2,2,1.0/Math.sqrt(svd_Cov_tgt.getS().get(2, 2)));
        S_tgt.set(3,2,0);
        
        S_tgt.set(0,3,0);
        S_tgt.set(1,3,0);
        S_tgt.set(2,3,0);
        S_tgt.set(3,3,1);
        
        
       // R_src.print(9, 3);
        //System.out.println(svd_Cov_src.getS().det());
        //System.out.println(svd_Cov_tgt.getS().det());
        //svd_Cov_src.getS().print(9, 6);
        //S_src.print(9, 3);
        
        //svd_Cov_tgt.getS().print(9, 6);        
        //S_tgt.print(9, 3);
//form total transformation matix 
         Matrix T_overall=new Matrix(4,4);
         
         
         //T_overall=T_src.times(R_src).times(S_src).times(S_tgt).times(R_tgt).times(T_tgt);
         //T_overall=T_src.times(R_src).times(S_src).times(S_tgt).times(R_tgt).times(T_tgt);
         //T_overall=T_src.times(R_src).times(R_tgt).times(T_tgt);
         T_overall=R_src.times(R_tgt);
         
         
          //for(int i=0;i<4;i++)
          // for(int j=0;j<4;j++)
        // T_overall.print(i, j);//
        //R_src.print(9, 6);
        //R_tgt.print(9, 6);
        //S_src.print(9, 6);
        //S_tgt.print(9, 6);
        //T_overall.print(9, 6);
        //apply it on target image to obtain result
         
         
         BufferedImage op=new BufferedImage(image_Target.getWidth(), image_Target.getHeight(),  BufferedImage.TYPE_INT_ARGB);
         
         Matrix result = T_overall.times(tgt.transpose());
         result=result.transpose();
         //convert to iMAGE form
         int no_of_rows=0;
         for(int i=0;i<image_Secrete.getWidth();i++)
           for(int j=0;j<image_Secrete.getHeight();j++)
           {    int alpha =255;              
                red=(int) result.get(no_of_rows, 0);
                green=(int) result.get(no_of_rows, 1);
                blue=(int) result.get(no_of_rows, 2);
                rgb=(alpha << 24) | (red << 16) | (green << 8) | blue;                
                op.setRGB(i, j,rgb);
                no_of_rows++;  
           }
         
return op; 
}    

    private static BufferedImage ColorTransform(BufferedImage image_Secrete, BufferedImage image_Target) {
        //get  mean  of both
       
       double t_r_src=0.0,t_g_src=0.0,t_b_src=0.0,t_r_tgt=0.0,t_g_tgt=0.0,t_b_tgt=0.0;
       double s_r_src=0.0,s_g_src=0.0,s_b_src=0.0,s_r_tgt=0.0,s_g_tgt=0.0,s_b_tgt=0.0;
       
       t_r_src=mean(image_Secrete,1);
       t_g_src=mean(image_Secrete,2);
       t_b_src=mean(image_Secrete,3);
       
       
       t_r_tgt=-mean(image_Secrete,1);
       t_g_tgt=-mean(image_Secrete,2);
       t_b_tgt=-mean(image_Secrete,3);
       
      
       
        //get covarince matrieces of both for R G B
       Matrix Cov_src=new Matrix(4,4);
 
       
        Matrix Cov_tgt=new Matrix(4,4);
    
        
        
        
        
        //get R G B og cuurent image into Matrix form
        Matrix src=new Matrix(image_Secrete.getHeight()*image_Secrete.getWidth(),4);
        
      
       
        Matrix tgt=new Matrix(image_Secrete.getHeight()*image_Secrete.getWidth(),4);
  
        
        //set values
        int rgb=0,red=0,green=0,blue=0;
        int row_no=0;
      for(int i=0;i<image_Secrete.getWidth();i++)
       for(int j=0;j<image_Secrete.getHeight();j++)
       {         rgb=image_Secrete.getRGB(i, j); 
                 red=(rgb & 0x00FF0000)  >>> 16;           
                 green=(rgb & 0x0000FF00)  >>> 8;
                 blue=(rgb & 0x000000FF)  >>> 0;                  
                 src.set(row_no, 0, red);
                 src.set(row_no, 1, green);
                 src.set(row_no, 2, blue);
                 src.set(row_no, 3, 1);
                 
                 
                  rgb=image_Target.getRGB(i, j); 
                 red=(rgb & 0x00FF0000)  >>> 16;           
                 green=(rgb & 0x0000FF00)  >>> 8;
                 blue=(rgb & 0x000000FF)  >>> 0;                  
                 tgt.set(row_no, 0, red);
                 tgt.set(row_no, 1, green);
                 tgt.set(row_no, 2, blue);
                 tgt.set(row_no, 3, 1);
                 row_no++;
                 
       }

   MeanAndCovariance mc_src=new MeanAndCovariance(src);  
   MeanAndCovariance mc_tgt=new MeanAndCovariance(tgt);  
   
   Cov_src=mc_src.getCovariance();
   Cov_tgt=mc_tgt.getCovariance();
//use svd to decompose covariance matrices
        SingularValueDecomposition svd_Cov_src = Cov_src.svd();
         
        
        SingularValueDecomposition svd_Cov_tgt = Cov_tgt.svd();
        
        
        //form translation rotation and scaling matrix for images

        Matrix T_src=new Matrix(4,4);
        Matrix R_src=new Matrix(4,4);
        Matrix S_src=new Matrix(4,4);
        
        Matrix T_tgt=new Matrix(4,4);
        Matrix R_tgt=new Matrix(4,4);
        Matrix S_tgt=new Matrix(4,4);
        
        
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
        T_src.set(0, 3,array[0][0] );
        T_src.set(1, 3,array[0][1]);
        T_src.set(2, 3,array[0][2]);
        T_src.set(3, 3, 1.0);
        
        for(int i=0;i<4;i++)
        for(int j=0;j<4;j++)
            
        {//T_src.print(i, j);
            
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
        T_tgt.set(0, 3,-array[0][0] );
        T_tgt.set(1, 3,-array[0][1]);
        T_tgt.set(2, 3,-array[0][2]);
        T_tgt.set(3, 3, 1.0);
        
        //T_tgt.print(1, 10);
        
        
        //for Rotation
        R_src=svd_Cov_src.getU();
        R_tgt=svd_Cov_tgt.getU().inverse();
        
        //for scaling
         
        S_src.set(0,0,svd_Cov_src.getS().get(0, 0));
        S_src.set(1,0,0);
        S_src.set(2,0,0);
        S_src.set(3,0,0);
        
        S_src.set(0,1,0);
        S_src.set(1,1,svd_Cov_src.getS().get(1, 1));
        S_src.set(2,1,0);
        S_src.set(3,1,0);
        
        S_src.set(0,2,0);
        S_src.set(1,2,0);
        S_src.set(2,2,svd_Cov_src.getS().get(2, 2));
        S_src.set(3,2,0);
        
        S_src.set(0,3,0);
        S_src.set(1,3,0);
        S_src.set(2,3,0);
        S_src.set(3,3,1);
        
        
        
        
        
        
        
        S_tgt.set(0,0,1.0/Math.sqrt(svd_Cov_tgt.getS().get(0, 0)));
        S_tgt.set(1,0,0);
        S_tgt.set(2,0,0);
        S_tgt.set(3,0,0);
        
        S_tgt.set(0,1,0);
        S_tgt.set(1,1,1.0/Math.sqrt(svd_Cov_tgt.getS().get(1, 1)));
        S_tgt.set(2,1,0);
        S_tgt.set(3,1,0);
        
        S_tgt.set(0,2,0);
        S_tgt.set(1,2,0);
        S_tgt.set(2,2,1.0/Math.sqrt(svd_Cov_tgt.getS().get(2, 2)));
        S_tgt.set(3,2,0);
        
        S_tgt.set(0,3,0);
        S_tgt.set(1,3,0);
        S_tgt.set(2,3,0);
        S_tgt.set(3,3,1);
        
        
       // R_src.print(9, 3);
        //System.out.println(svd_Cov_src.getS().det());
        //System.out.println(svd_Cov_tgt.getS().det());
        //svd_Cov_src.getS().print(9, 6);
        //S_src.print(9, 3);
        
        //svd_Cov_tgt.getS().print(9, 6);        
        //S_tgt.print(9, 3);
//form total transformation matix 
         Matrix T_overall=new Matrix(4,4);
         
         
         //T_overall=T_src.times(R_src).times(S_src).times(S_tgt).times(R_tgt).times(T_tgt);
         //T_overall=T_src.times(R_src).times(S_src).times(S_tgt).times(R_tgt).times(T_tgt);
         //T_overall=T_src.times(R_src).times(R_tgt).times(T_tgt);
         T_overall=R_src.times(R_tgt);
         
         
          //for(int i=0;i<4;i++)
          // for(int j=0;j<4;j++)
        // T_overall.print(i, j);//
        //R_src.print(9, 6);
        //R_tgt.print(9, 6);
        //S_src.print(9, 6);
        //S_tgt.print(9, 6);
        //T_overall.print(9, 6);
        //apply it on target image to obtain result
         
         
         BufferedImage op=new BufferedImage(image_Target.getWidth(), image_Target.getHeight(),  BufferedImage.TYPE_INT_ARGB);
         
         Matrix result = T_overall.times(tgt.transpose());
         result=result.transpose();
         //convert to iMAGE form
         int no_of_rows=0;
         for(int i=0;i<image_Secrete.getWidth();i++)
           for(int j=0;j<image_Secrete.getHeight();j++)
           {    int alpha =255;              
                red=(int) result.get(no_of_rows, 0);
                green=(int) result.get(no_of_rows, 1);
                blue=(int) result.get(no_of_rows, 2);
                rgb=(alpha << 24) | (red << 16) | (green << 8) | blue;                
                op.setRGB(i, j,rgb);
                no_of_rows++;  
           }
         
     try {    
          ImageIO.write(op, "png", new File(secrete_i.getDirectory()+"TRANSORMED.png"));
      }catch (IOException e) {}     
   
return op;        
//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
 
}