import ij.*;
import ij.gui.*;
import ij.plugin.*;
/* Allows users to create a 3-D projection of a stack of images for use with Google Cardboard or blue and red anaglyph glasses. 
 * Google Cardboard projections can be produced in color or grayscale.
 * Anaglyph projections require a grayscale base image. 
 * Users who attempt to create an anaglyph image with a colored image stack will have the option to convert the stack to grayscale or to abort the projection process.
 * 
 * @author Joe Napoli
 * @author Tom Villani
 * */

public class Stereoscopic_3D_Projection implements PlugIn {
	
	static double totalRotation = 360, sliceSpace = 1, degStep = 10, startRot = 0, endRot = 360; //Variables for the 3D projection, initialized with default values
	
	
	public void run(String arg) {
		String ThreeDChoice, sliceUnits; //ThreeDChoice holds whether it's Anaglyph or Cardboard processing; sliceUnits holds the units of image slice spacing
		String[] rdoNames = {"Anaglyph", "Google Cardboard"};
		
		ImagePlus imp = IJ.getImage();
		
		//check stack slices
		if (imp.getNSlices() == 1) { // image is not a stack
			IJ.showMessage("Error: Image is not a stack!");
			return;
		}
		
		//autofills for default slice step and units:
		sliceSpace = imp.getCalibration().pixelDepth;
		sliceUnits = imp.getCalibration().getZUnit();
		
		GenericDialog dialog = new GenericDialog("Enter Image Info");
		
		dialog.addNumericField("Slice Spacing (in "+ sliceUnits + "): ", sliceSpace, 2);
		dialog.addNumericField("Total Rotation: ", totalRotation, 0);
		dialog.addNumericField("Degree Step: ", degStep, 0);
		dialog.addNumericField("Start Rotation: ", startRot, 0);
		dialog.addNumericField("End Rotation: ", endRot, 0);
		dialog.addRadioButtonGroup("Choose Your 3-D Medium: ", rdoNames, 1, 2, "Anaglyph");
		

		dialog.showDialog();
		if (dialog.wasCanceled()) return;
		
		
		//User input through dialog boxes: https://imagej.nih.gov/ij/developer/api/ij/gui/GenericDialog.html
		//get slice spacing in whatever measurement the metadata wants
		sliceSpace = (double)dialog.getNextNumber();
		//get total rotation degrees -- default 360
		totalRotation = (double)dialog.getNextNumber();
		//get degree step --default 10
		degStep = (double)dialog.getNextNumber();
		//get specific start degree of rotation
		startRot = (double)dialog.getNextNumber();
		//get specific end degree of rotation
		endRot = (double)dialog.getNextNumber();
		//get anaglyph or cardboard
		ThreeDChoice = dialog.getNextRadioButton();
		
		
		
		
		
		if (ThreeDChoice.equals("Anaglyph") && (imp.getType() != ImagePlus.GRAY8 && imp.getType() != ImagePlus.GRAY16 && imp.getType() != ImagePlus.GRAY32)) { //The image is not grayscale
			// Tell them they can't have a colored anaglyph image and offer to convert it to grayscale
			String[] yOrN = {"Yes","No"};
			GenericDialog aDialog = new GenericDialog("Error: Image Is Not Grayscale!");
			aDialog.addMessage("Colored images cannot be used to construct an anaglyph projection.");
			aDialog.addRadioButtonGroup("Convert colored image to grayscale?",yOrN, 1,2, "No"); 
			//NOTE: The initial image stack can be preserved if that imageplus window is closed without saving changes by the user.
			
			aDialog.showDialog();
			if (aDialog.wasCanceled()) return;
			
			if (aDialog.getNextRadioButton().equals("Yes")) {
				//convert the image stack to grayscale
				IJ.run(imp, "8-bit", "");
			}
			else return;
		}
		
		if (ThreeDChoice.equals("Anaglyph")) {
			//Code below is for doing the anaglyph (red/blue 3d)
			
			IJ.run(imp, "3D Project...", ("projection=[Brightest Point] axis=Y-Axis slice="+ sliceSpace +" initial="+ startRot +" total="+ totalRotation +" rotation="+ degStep +" lower=1 upper=255 opacity=0 surface=100 interior=50"));
			
			ImagePlus redPro = IJ.getImage(); //get the red projection we created
			redPro.setTitle("Red Projection"); //change its title
			
			
			//turn the projection for the left eye red.
			IJ.run(redPro, "Red", ""); 
			redPro.hide(); // hide it from the user so no changes can be made
			
			
			// make the projection for the right eye
			IJ.run(imp, "3D Project...", ("projection=[Brightest Point] axis=Y-Axis slice="+ sliceSpace +" initial="+ (startRot + 5) +" total="+ totalRotation +" rotation="+ degStep +" lower=1 upper=255 opacity=0 surface=100 interior=50"));
			
			ImagePlus bluePro = IJ.getImage(); //get the blue projection
			bluePro.setTitle("Blue Projection"); //change its title
			
			
			// Color the image blue
			IJ.run(bluePro, "Blue", "");
			bluePro.hide(); // hide it from the user so no changes can be made
			
			
			// merge the channels we just created into 1 image.
			redPro.show(); //VERY IMPORTANT. If show() is not run, the commands don't see the imagepro and an error occurs.
			bluePro.show();
			IJ.run(imp, "Merge Channels...", "c1=[" + redPro.getTitle() + "] c3=["+ bluePro.getTitle() +"] create");
			
		}
		
		if (ThreeDChoice.equals("Google Cardboard")) {
			//code in a 25% resize
			int newWidth = (int) (imp.getWidth() * 1.25); //could be slightly more accurate with Math.Round
			int newHeight = (int) (imp.getHeight() * 1.25);
			
			// of course, this will be used to set the width / height to 25% bigger than the starting image. 
			IJ.run(imp, "Canvas Size...", ("width=" + newWidth + " height=" + newHeight + " position=Center zero"));

			//   This generates the left eye image.
			IJ.run(imp, "3D Project...", ("projection=[Brightest Point] axis=Y-Axis slice="+ sliceSpace +" initial="+ startRot +" total="+ totalRotation +" rotation="+ degStep +" lower=1 upper=255 opacity=0 surface=100 interior=50"));

			ImagePlus leftEye = IJ.getImage(); // get the left eye projection
			leftEye.setTitle("Left Eye Projection");
			leftEye.hide();
			
			//  Add 5 degrees to initial= since the right eye image needs to be slightly displaced
			IJ.run(imp, "3D Project...", ("projection=[Brightest Point] axis=Y-Axis slice="+ sliceSpace +" initial="+ (startRot + 5) +" total="+ totalRotation +" rotation="+ degStep +" lower=1 upper=255 opacity=0 surface=100 interior=50"));

			ImagePlus rightEye = IJ.getImage(); // get the right eye projection
			rightEye.setTitle("Right Eye Projection");
			rightEye.hide();
			
			leftEye.show(); //VERY IMPORTANT. If show() is not run, the commands don't see the imagepro and an error occurs.
			rightEye.show();
			// combine the two product images.  
			IJ.run(imp, "Combine...", "stack1=["+ leftEye.getTitle()  +"] stack2=["+ rightEye.getTitle()  +"]");
	
			
		}

	}
	
}