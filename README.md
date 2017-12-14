# Stereoscopic 3D Projection
This ImageJ plugin allows users to create 3-D anaglyph and Google Cardboard compatible projections from image stacks.
It was created by Joe Napoli for [Visikol, Inc](https://visikol.com/news/2017/9/5/teaming-up-with-young-talent-to-disrupt-bio-imaging).

# Installation
The jar is ready for installation on ImageJ and Fiji. It requires Java 8.0 or greater, and can be downloaded from here into the plugins directory or installed from Fiji's plugin installer.
The source is attached to the jar file and here on its own for easy reference.
For further documentation, see the official [Stereoscopic 3D Projection](https://imagej.net/Stereoscopic_3D_Projection) wiki page.

# Use
To create a new projection:

1. Open an image stack.
2. Launch the plugin.
3. Review slice spacing, total degrees of rotation, degree steps, and start and end rotation.
4. Select the projection medium -- either anaglyph or Google Cardboard.
    - If an anaglyph projection is attempted on a colored image stack, a prompt to convert the image stack to grayscale will appear. Color is incompatible with the lookup tables used in the anaglyph projection, and so the stack must be converted to grayscale to continue.
5. Select "Ok!"

# Examples

Below is an anaglyph projection was created from the T1 Head sample included within ImageJ using information pulled straight from the sample's metadata.

![Anaglyph Example 1](https://raw.githubusercontent.com/Joe-Napoli/Stereoscopic_3D_Projection/master/Sample%20Images/AnaglyphExample1.jpg)
![Anaglyph Product](https://raw.githubusercontent.com/Joe-Napoli/Stereoscopic_3D_Projection/master/Sample%20Images/AnaglyphExample2.jpg)

Like the anaglyph projection above, this Google Cardboard compatible projection was created using information included within the metadata of an ImageJ sample. Here, however, a fly brain is the subject.

![Cardboard Example 1](https://raw.githubusercontent.com/Joe-Napoli/Stereoscopic_3D_Projection/master/Sample%20Images/CardboardExample1.jpg)
![Cardboard Product](https://raw.githubusercontent.com/Joe-Napoli/Stereoscopic_3D_Projection/master/Sample%20Images/CardboardExample2.jpg)

Finally, a projection compatible with Google Cardboard and similar viewers was created from the sample bat cochlea included in ImageJ. 

![Bat Cochlea Volume Projection](https://raw.githubusercontent.com/Joe-Napoli/Stereoscopic_3D_Projection/master/Sample%20Images/BatProduct.gif)
 
