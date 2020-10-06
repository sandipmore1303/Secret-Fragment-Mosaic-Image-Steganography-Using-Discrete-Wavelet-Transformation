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
 


/**
 * This class is used by many others to obtain default values of TER coder. 
 * It's useful to put all default values here because many of them are used in more than one site.
 *
 * @author Group on Interactive Coding of Images (GICI)
 * @version 1.2
 */
public class TERDefaultValues{


	/**
	 * Definition in {@link TER.TERcoder.Coder#outputFileType}
	 */
	public static int outputFileType = 0;
		
	/**
	 * Definition in {@link TER.TERcoder.Coder#CVerbose}
	 */
	public static boolean[] CVerbose = {false, false, false};
	
	/**
	 * Definition in {@link GiciImageExtension.ImageExtension#imageExtensionType}
	 */
	 public static int imageExtensionType = 0; 	 // Default parameters corresponds to repeat last value if needed.

	 /**
	 * Definition in {@link GiciTransform.ForwardWaveletTransform#WTTypes}
	 */
	 public static int WTType = 4; // Default parameter: Integer 9/7M DWT
	 
	 /**
	 * Definition in {@link GiciTransform.ForwardWaveletTransform#WTLevels}
	 */
	 public static int WTLevels = 3; // Default parameter: 3 DWT Levels
	 
	 /**
	 * Definition in {@link GiciTransform.ForwardWaveletTransform#WTOrder}
	 */
	 public static int WTOrder = 0; // Default parameter: 0 (Horizontal-vertical)
	 
	 /**
	 * Definition in {@link TER.TERcoder.Weighting.ForwardWeighting#customWtFlag}
	 */
	 public static int customWtFlag = 0; // Default parameter: Weights recommended by CCSDS
	 
	 /**
	  * Definition in {@link TER.TERcoder.SegmentCoder.SegmentCode2D#segByteLimit}
	  */
	 public static int segByteLimit = (int) 1<< 27;//134217728; //Math.pow(2,27)
	 
	 /**
	 * Definition in {@link TER.TERcoder.SegmentCoder.SegmentCode2D#DCStop}
	 */
	 public static int DCStop = 0; 
	 
	 /**
	 * Definition in {@link TER.TERcoder.SegmentCoder.SegmentCode2D#bitPlaneStop}
	 */
	 public static int bitPlaneStop = 0; 
	 
	 
	 /**
	  * Definition in {@link TER.TERcoder.SegmentCoder.SegmentCode2D#stageStop}
	  */
	 public static int stageStop = 4;
	 
	 /**
	 * Definition in {@link TER.TERcoder.WriteFile.RecommendedOrder#useFill}
	 */
	 public static int useFill = 0;

	 /**
	 * Definition in {@link TER.TERcoder.SegmentCoder.SegmentCode2D#blocksPerSegment}
	 */
	 public static int blocksPerSegment = (int) 1<< 20;//1048576; //Math.pow(2,20);
	 
	 /**
	 * Definition in {@link TER.TERcoder.SegmentCoder.SegmentCode2D#optDCSelect}
	 */
	 public static int optDCSelect = 1;
	 
	 /**
	 * Definition in {@link TER.TERcoder.SegmentCoder.SegmentCode2D#optACSelect}
	 */
	 public static int optACSelect = 1;
	 
	 /**
	 * Definition in {@link TER.TERcoder.Coder#signedPixels}
	 */
	 public static int signedPixels = 0;
	 
	 /**
	  * Definition in {@link TER.TERcoder.Coder#pixelBitDepth}
	  */
	 public static int pixelBitDepth = 8;
		 
		 
	
	 /**
	 * Definition in {@link GiciTransform.TransposeImage#transposeImg}
	 */
	 public static int transposeImg = 0;
	 
	 /**
	 * Definition in {@link TER.TERcoder.WriteFile.RecommendedOrder#codeWordLength}
	 */
	 public static int codeWordLength = 0;
	 
	 /**
	 * Definition in {@link TER.TERcoder.SegmentCoder.SegmentCode2D#gaggleDCSize}
	 */
	 public static int gaggleDCSize = 16;
	 
	 /**
	 * Definition in {@link TER.TERcoder.SegmentCoder.SegmentCode2D#gaggleACSize}
	 */
	 public static int gaggleACSize = 16;
	 
	 /**
	 * Definition in {@link TER.TERcoder.SegmentCoder.SegmentCode2D#idDC}
	 */
	 public static int idDC = 0;

	 /**
	 * Definition in {@link TER.TERcoder.SegmentCoder.SegmentCode2D#idAC}
	 */
	 public static int idAC = 0;

	 /**
	 * Definition in {@link TER.TERcoder.SegmentCoder.EncodeGaggleAC#entropyAC}
	 */
	 public static int entropyAC = 1;

	 /**
	  * Definition in {@link TER.TERcoder.WriteFile.RecommendedHeader#part2Flag}
	  */
	 public static int part2Flag = 1;
	 
	 /**
	  * Definition in {@link TER.TERcoder.WriteFile.RecommendedHeader#part3Flag}
	  */
	 public static int part3Flag = 1;
	 
	 /**
	  *  Definition in {@link TER.TERcoder.WriteFile.RecommendedHeader#part4Flag}
	  */
	 public static int part4Flag = 1;
	 
	 /**
	  * Definition in {@link TER.TERcoder.SegmentCoder.DistortionCompute#distortionMeasure}
	  */
	 public static int distortionMeasure = 2;
	 
	 /**
	  * Definition in {@link GiciImageExtension.ImageExtension#padRows}
	  */
	 public static int padRows = 0;
	 
	 /**
	  * Definition in {@link TER.TERdecoder.SegmentDecoder.SegmentDecode2D#gammaValue}
	  */
	 public static float gammaValue = 0.375F;
	 
	 /**
	  * 
	  */
	 public static boolean minusHalf = false;
	 
	 /**
	  * Definition in {@link TER.TERcoder.WriteFile.RecommendedOrder#adjustHeaderParameters}
	  */
	 public static int adjustHeaderParameters = 0;
	 
	 /**
	  * Definition in {@link TER.TERcoder.WriteFile.RecommendedInterleaving#truncationPoints}
	  */
	 public static int  truncationPoints = 0;
	 
	 /**
	  *  Definition in {@link TER.TERdecoder.SegmentDecoder.InitialDecoding#completionMode}
	  */
	 public static int completionMode = 0;
	 
	 /**
	  * Definition in {@link  GiciTransform.LevelShift#LSType}
	  */
	 public static int LSType = 0;
	 
	 /**
	  * Definition in {@link  GiciTransform.LevelShift#LSComponents}
	  */
	 public static int LSComponents = 1;
	 
	 /**
	  * Definition in {@link GiciTransform.CoefficientsApproximation#approximationTypes}
	  */
	 public static int coefficientsApproximationTypes = 1;
	 
	 /**
	  * 
	  */
	 public static int progressionOrder = 0;
	 
	 /**
	  * 
	  */
	 public static int numberOfLayers = 1;
	 
	 /**
	  * 
	  */
	 public static int layerCreationType = 3;
	 
	 /**
	  * 
	  */
	 public static int layerSizeType = 2;
	 
	 /**
	  * 
	  */
	 public static float compressionFactor = 1;
	 
	 /**
	  * 
	  */
	 public static float desiredDistortion = 0.0F;
	 
	 
	 public static int displayResolutionLevels = 1;
	 
	 public static boolean rangeRecoveredPixels = false;
	 
	 /**
	  * This function converts an array of integer that represents components (p.e. if array is 0,2 indicates component 0 and 2) to an array of booleans that indicates if a component is marked or not (p.e. 0,2,4 is true,false,true,false,true) .
	  *
	  * @param intComponents an array of integers representing selected components
	  * @param size array size of components
	  * @param defaultValue indicates the default value
	  * 
	  * @return an array of booleans representing the components passed
	  */
	 static public boolean[] integerToBooleanComponents(int[] intComponents, int size, boolean defaultValue){
		 boolean[] boolComponents = null;
		 
		 if(intComponents != null){
			 int maxComponent = 0;
			 for(int i = 0; i < intComponents.length; i++){
				 if(maxComponent < intComponents[i]){
					 maxComponent = intComponents[i];
				 }
			 }
			 boolComponents = new boolean[maxComponent+1 > size? maxComponent+1: size];
			 for(int i = 0; i < boolComponents.length; i++){
				 boolComponents[i] = false;
			 }
			 for(int i = 0; i < intComponents.length; i++){
				 boolComponents[intComponents[i]] = true;
			 }
		 } else {
			 boolComponents = new boolean[size];
			 for(int k = 0; k < size; k++){
				 boolComponents[k] = defaultValue;
			 }
		 }
		 return(boolComponents);
	 }
}

