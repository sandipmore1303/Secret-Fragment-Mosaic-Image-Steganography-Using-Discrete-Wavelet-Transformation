/*
 * TER Software - More than an implementation of CCSDS Recommendation for Image Data Compression
 * Copyright (C) 2007  Group on Interactive Coding of Images (GICI)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Group on Interactive Coding of Images (GICI)
 * Department of Information and Communication Engineering
 * Autonomous University of Barcelona
 * 08193 - Bellaterra - Cerdanyola del Valles (Barcelona)
 * Spain
 *
 * http://gici.uab.es
 * http://sourceforge.net/projects/ter
 * gici-info@deic.uab.es
 */
 
import GiciFile.SaveFile;
import TER.TERdecoder.ArgsParserDecoder;
import TER.TERdecoder.Decoder;


/**
 * Main class of TERDecode application. TERDecode is a decoder of the Recommended Standard CCSDS 122.0-B-1 Blue Book.
 * <p>
 *
 * @author Group on Interactive Coding of Images (GICI)
 * @version 1.2
 */
public class TERdecode{

	/**
	 * Main method of TERDecode application. It takes program arguments, loads an encoded image and runs TER decoder and saves recovered image to a file.
	 * 
	 * @param args an array of strings that contains program parameters
	 */
	public static void main(String[] args){
args=new String[4];
args[0]="-i";
args[1]="lena_color.png.rec";	
args[2]="-o";
args[3]="lena_color_rec.png";	
              // Parse arguments
		ArgsParserDecoder argsParser = null;
		try{
			argsParser = new ArgsParserDecoder(args);
		}catch(Exception e){
			System.err.println("TERdecode : ARGUMENTS ERROR: " +  e.getMessage());
			System.exit(1);
		}
		
		// Image load
		String imageFile = argsParser.getImageFile();
		int[] imageGeometry = null;
		try{		
			imageGeometry = argsParser.getImageGeometry();
			if(imageGeometry!=null){	
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
			}
		}catch(Exception e){
			System.err.println("IMAGE LOADING ERROR: " + e.getMessage());
			System.exit(2);
		}
		
		//Get arguments from parser
		String inputFile = argsParser.getInputFile();
		int[] segByteLimit = argsParser.getSegByteLimit();
		int[] DCStop = argsParser.getDCStop();
		int[] bitPlaneStop = argsParser.getBitPlaneStop();
		int[] stageStop = argsParser.getStageStop();
		float[] compressionFactor = argsParser.getCompressionFactor();
		boolean[] CVerbose = argsParser.getCVerbose();
		float[] gammaValue = argsParser.getGammaValue();
		int[] completionMode = argsParser.getCompletionMode();
		boolean minusHalf = argsParser.getMinusHalf();
		boolean rangeRecoveredPixels = argsParser.getRangeRecoveredPixels();
		int test3d = argsParser.getTest3d();
		int spectralWTLevels = argsParser.getSpectralWTLevels();
		int spectralWTType = argsParser.getSpectralWTType();
		
		Decoder terDecoder = new Decoder(inputFile);
		int[] pixelBitDepth = null;
		
		try{
			terDecoder.setParameters( 
					imageGeometry,
					segByteLimit,
					DCStop,
					bitPlaneStop,
					stageStop,
					pixelBitDepth,
					compressionFactor,
					gammaValue, minusHalf,
					completionMode,
					CVerbose,				
					test3d,spectralWTLevels,spectralWTType,
					rangeRecoveredPixels
			);
		}catch(Exception e){
			System.err.println("TERdecoder PARAMETERS ERROR: " + e.getMessage());
			System.exit(3);
		}
		
		try{
			float[][][] image = terDecoder.run();
			if (imageGeometry==null){
				imageGeometry = terDecoder.getImageGeometry();
			}
			try{
				SaveFile.SaveFileByExtension(image,imageFile,imageGeometry);
			} catch(Exception e){
				e.printStackTrace();
				System.err.println("Gici SaveFile ERROR: " + e.getMessage());
				System.exit(4);
			}
		}catch(Exception e){
			e.printStackTrace();
			System.err.println("TERdecoder RUNNING ERROR: " + e.getMessage());
			System.exit(5);
		}
		
		
	}
}
