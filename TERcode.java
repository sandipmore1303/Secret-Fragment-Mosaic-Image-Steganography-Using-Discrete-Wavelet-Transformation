
import GiciFile.*;
import TER.TERcoder.*;

/**
 * Main class of TERcode application. TERcode is a coder of the Recommended Standard CCSDS 122.0-B-1 Blue Book.
 * <p>
 *
 * @author Group on Interactive Coding of Images (GICI)
 * @version 1.05
 */
public class TERcode{
	/**
	 * Main method of TERcode application. It takes program arguments, loads image and runs TER coder.
	 *
	 * @param args an array of strings that contains program parameters
	 */
	public static void main(String[] args){
		//Parse arguments
args=new String[4];
args[0]= "-i";
args[1]="lena_color.png";	
args[2]="-o";
args[3]="lena_color.png";		// Parse arguments

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
	public static void MAIN(String[] args)
        {
		//Parse arguments
args=new String[4];
args[0]= "-i";
args[1]="lena_color.png";	
args[2]="-o";
args[3]="lena_color.png";		// Parse arguments

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
