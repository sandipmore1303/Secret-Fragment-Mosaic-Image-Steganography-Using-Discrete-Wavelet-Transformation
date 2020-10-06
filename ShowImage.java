import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
public class ShowImage extends Panel {
    BufferedImage  image;
	String imageName;

  public ShowImage(String in) {
    try {
  //System.out.println("Enter image name\n");
  //BufferedReader bf=new BufferedReader(new 
  //InputStreamReader(System.in));
   String imageName=in;
  File input = new File(imageName);
      image = ImageIO.read(input);
    } catch (IOException ie) {
      System.out.println("Error:"+ie.getMessage());
    }
  }
public ShowImage(BufferedImage br)
{
  image =br; 
}
  public void paint(Graphics g) {
    g.drawImage( image, 0, 0, null);
  }

  static public void MAIN(String args[]) throws Exception {
	
    JFrame frame_oi = new JFrame("Original image");
    Panel panel_oi = new ShowImage("D:\\ORIGINAL_IMAGES\\lenna.jpg");
    frame_oi.getContentPane().add(panel_oi);
    frame_oi.setSize(500, 500);
    frame_oi.setVisible(true);

    JFrame frame_wi = new JFrame("Watermarked image");
    Panel panel_wi = new ShowImage("D:\\SCDFT_WATERMARKED_IMAGES\\wlenna.jpg");
    frame_wi.getContentPane().add(panel_wi);
    frame_wi.setSize(500, 500);
    frame_wi.setVisible(true);

    JFrame frame_li = new JFrame("Watermark");
    Panel panel_li = new ShowImage("D:\\LOGO_IMAGES\\logo.jpg");
    frame_li.getContentPane().add(panel_li);
    frame_li.setSize(500, 500);
    frame_li.setVisible(true);	
  }
}