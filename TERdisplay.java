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
 
import GiciException.*;
import GiciFile.SaveFile;
import TER.TERDisplayFrame.*;
import TER.TERInteractiveDecoder.InteractiveDecoder;



/**
 * Main class of TER display application. TERdisplay is an interactive application that displays a window of a compressed JPEG2000 codestream using a window. The decompression process only decodes those blocks that have to be shown.
 *
 * @author Group on Interactive Coding of Images (GICI)
 * @version 2.0
 */
public class TERdisplay{

	/**
	 * Main method of BOIEye application. It takes program arguments, and calls the BOIFrame class, who uses the Extractor class to obtain information about the JPEG2000 codestream and creates the window to manipulate it.
	 *
	 * @param args an array of strings that contains program parameters
	 */
	public static void main(String[] args)
        {
		//Parse arguments
               args=new String[2];
               args[0]= "-i";
               args[1]=  "F_stego_compressed.rec";
         	DisplayFrameParser parser = null;
		try{
			parser = new DisplayFrameParser(args);
		}catch(ErrorException e){
			System.out.println("RUN ERROR:");
			e.printStackTrace();
			System.out.println("Please report this error (specifying image type and parameters) to: gici-dev@abra.uab.es");
			System.exit(1);
		}catch(ParameterException e){
			System.out.println("ARGUMENTS ERROR: " +  e.getMessage());
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
}


