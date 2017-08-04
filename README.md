# Stereoscopic_3D_Projection
Creates 3-D anaglyph and Google Cardboard compatible projections from image stacks in ImageJ.

# Installing
The jar is ready for installation on ImageJ and Fiji. It requires Java 8.0 or greater, and can be downloaded from here or installed from Fiji's plugin installer.
The source is attached both to the jar file and on its own for easy reference.

# Use
Both anaglyph and Google Cardboard compatible projections require a sequential image stack. 

Anaglyph projections require a grayscale image stack. If an anaglyph projection is attempted on a colored stack, the user will be prompted to convert the stack or abort the projection.

Slice spacing, total rotation, degree steps, and start and end rotation can be modified by the user, but are autofilled through metadata.
