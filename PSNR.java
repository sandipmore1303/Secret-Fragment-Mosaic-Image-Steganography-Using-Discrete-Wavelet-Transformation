
//import static com.sun.org.apache.xalan.internal.lib.ExsltMath.log;
import static ij.IJ.log;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import static java.lang.Math.log;
import java.math.BigDecimal;
import java.math.BigInteger;
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
public class PSNR {
    /* --------------------------------------------------------------------------------------- */
    
    /**
* Computes the peak signal to noise ratio for two images, using all four channels (R,G,B,Alpha).
*
* @param i1
* @param i2
* @return
*/
    public static Double psnr( BufferedImage i1, BufferedImage i2 ) {
     // Image features
     int cc1 = i1.getColorModel().getNumComponents();
        double bpp1 = i1.getColorModel().getPixelSize()/cc1;
     int cc2 = i2.getColorModel().getNumComponents();
        double bpp2 = i2.getColorModel().getPixelSize()/cc2;
        int cc = 4;
        if( cc1 == 3 && cc2 == 3 ) {
         cc = 3;
        }
        // Set up the summations:
        KahanSummation tr = new KahanSummation();
        KahanSummation tg = new KahanSummation();
        KahanSummation tb = new KahanSummation();
        KahanSummation ta = new KahanSummation();
        BigInteger br = BigInteger.valueOf(0);
        BigInteger bg = BigInteger.valueOf(0);
        BigInteger bb = BigInteger.valueOf(0);
        BigInteger ba = BigInteger.valueOf(0);

        // TODO How best can we cope with comparisons across colour-spaces?
        // Yes, in that this is what getRBG does, but note that getRGB reduced the depth to 8-bits!
        //i1.getColorModel().getColorSpace();
        //i1.getData().getSample(x, y, b);
        // i1.getData().getSampleModel().getSampleSize();
        // FIXME So, should we compare in the common colour-space, or compare the data, or both!?

        // TODO Decide on which accumulator to use. BitInteger or BigDecimal is probably best.
        for (int i = 0; i < i1.getWidth(); i++) {
            for (int j = 0; j < i1.getHeight(); j++) {
                final Color c1 = new Color(i1.getRGB(i, j));
                final Color c2 = new Color(i2.getRGB(i, j));
                final int dr = c1.getRed() - c2.getRed();
                final int dg = c1.getGreen() - c2.getGreen();
                final int db = c1.getBlue() - c2.getBlue();
                final int da = c1.getAlpha() - c2.getAlpha();
                tr.add( dr*dr );
                tg.add( dg*dg );
                tb.add( db*db );
                ta.add( da*da );
                br = br.add( BigInteger.valueOf(dr*dr));
                bg = bg.add( BigInteger.valueOf(dg*dg));
                bb = bb.add( BigInteger.valueOf(db*db));
                ba = ba.add( BigInteger.valueOf(da*da));
            }
        }
        // Compute the mean square error:
        double mse = (tr.getSum() + tg.getSum() + tb.getSum() + ta.getSum()) / (i1.getWidth() * i1.getHeight() * cc);
        //log.info("Mean square error: " + mse);
        //log.info("Mean square error 2: " + (br.doubleValue() + bg.doubleValue() + bb.doubleValue() + ba.doubleValue()) / (i1.getWidth() * i1.getHeight() * cc) );
        if (mse == 0) {
            //log.warning("mse == 0 and so psnr will be infinity!");
        }
        // Also do the BigInteger calculation:
        System.out.println("Got: br = "+br+", tr = "+tr.getSum());
        System.out.println("Got: bg = "+bg+", tg = "+tg.getSum());
        System.out.println("Got: bb = "+bb+", tb = "+tb.getSum());
        System.out.println("Got: ba = "+ba+", ta = "+ta.getSum());
        BigDecimal bmse = new BigDecimal("0.00");
        bmse = bmse.add(new BigDecimal(br));
        bmse = bmse.add(new BigDecimal(bg));
        bmse = bmse.add(new BigDecimal(bb));
        bmse = bmse.add(new BigDecimal(ba));
        bmse = new BigDecimal( bmse.doubleValue() / (i1.getWidth() * i1.getHeight() * cc) );
        System.out.println("bmse = "+bmse);
        //mse = bmse.doubleValue();
        // Get the bits per pixel:
        // FIXME This may be approx, and need to be tightened up for the case where channels have variable depths.
        if( bpp1 != bpp2 ) {
         //log.warning("Bits-per-pixel do not match up! bpp1 = "+bpp1+", bpp2 = "+bpp2);
        }
        double bpp = bpp1;
        System.out.println("read bpp = "+bpp);
        System.out.println("colcomp = "+cc);
        // FIXME Actually, using getRGB reduces each channel to 8 bits:
        bpp = 8;
        // The maximum is therefore:
        double max = Math.pow(2.0, bpp) - 1.0;
        // Compute the peak signal to noise ratio:
        double psnr = 10.0 * StrictMath.log10( max*max / mse );
        //log.info("Peak signal to noise ratio: " + psnr); //43.82041101171352
        //log.info("Peak signal to noise ratio (BigDecimal): " + 10.0 * StrictMath.log10(max*max/bmse.doubleValue()) );

        return new Double( psnr );
    }
    public static void main( String[] args ) throws IOException {
    	/*
    	JavaImageIOCompare c = new JavaImageIOCompare();
    	System.out.println("SD: "+c.describe().toXmlFormatted());
    	*/
    	for( String fmt : ImageIO.getReaderFileSuffixes() ) {
    		System.out.println("Reads "+fmt);
    	}
    	
        ij.io.OpenDialog oi =new ij.io.OpenDialog("Select image","E:\\DATA\\YES_MAN\\JAVA_IMPL\\", "lenna.bmp");
          
        BufferedImage i1 = ImageIO.read( new File(oi.getDirectory()+oi.getFileName()) ); // .im.png
    	System.out.println("i1 = "+i1);
        
    	oi =new ij.io.OpenDialog("Select image","E:\\DATA\\YES_MAN\\JAVA_IMPL\\", "lenna.bmp");
        BufferedImage i2 = ImageIO.read( new File(oi.getDirectory()+oi.getFileName()) );  // .p.tif.im.png
    	System.out.println("i2 = "+i2);
    	// Do the comparison...
    	double psnr = psnr(i1,i2);
    	System.out.println("psnr = "+psnr);
    	
    }
    
}
