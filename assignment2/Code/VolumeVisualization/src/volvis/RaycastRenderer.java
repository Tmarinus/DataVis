/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package volvis;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

import gui.RaycastRendererPanel;
import gui.TransferFunction2DEditor;
import gui.TransferFunctionEditor;
import java.awt.image.BufferedImage;
import util.TFChangeListener;
import util.VectorMath;
import volume.GradientVolume;
import volume.Volume;
import volume.VoxelGradient;

/**
 *
 * @author michel
 * @Anna
 * This class has the main code that generates the raycasting result image. 
 * The connection with the interface is already given.  
 * The different modes mipMode, slicerMode, etc. are already correctly updated
 */
public class RaycastRenderer extends Renderer implements TFChangeListener {

	private Volume volume = null;
    private GradientVolume gradients = null;
    RaycastRendererPanel panel;
    TransferFunction tFunc;
    TransferFunctionEditor tfEditor;
    TransferFunction2DEditor tfEditor2D;
    private boolean mipMode = false;
    private boolean slicerMode = true;
    private boolean compositingMode = false;
    private boolean tf2dMode = false;
    private boolean shadingMode = false;
    
    //Slowmode skips rendering frames to increase performance.
    private boolean slowMode = false;
    //Book keeping bool for frame skipping  
    private boolean skipOne = false;
    
    //Set the size of skipped elements when interacting
    private int incrementSize = 2;
    
    //Interpolate or NN
    private boolean interpolate = false;
    
    public void setInterpolate(boolean val){
    	interpolate = val;
    	changed();
    }
    
    // Select slicer colour calculation sort of legacy
    public enum SlicerType {
    	NEARESTNEIGH, TRANSFER
    }
    private SlicerType currSlicerType = SlicerType.NEARESTNEIGH;
    
    public void setSlicerType(SlicerType newType) {
    	currSlicerType = newType;
    }
    
    public void setSlowMode(boolean val) {
    	slowMode = val;
    }
    
    public RaycastRenderer() {
        panel = new RaycastRendererPanel(this);
        panel.setSpeedLabel("0");
    }

    public void setVolume(Volume vol) {
        System.out.println("Assigning volume");
        volume = vol;

        System.out.println("Computing gradients");
        gradients = new GradientVolume(vol);

        // set up image for storing the resulting rendering
        // the image width and height are equal to the length of the volume diagonal
        int imageSize = (int) Math.floor(Math.sqrt(vol.getDimX() * vol.getDimX() + vol.getDimY() * vol.getDimY()
                + vol.getDimZ() * vol.getDimZ()));
        if (imageSize % 2 != 0) {
            imageSize = imageSize + 1;
        }
        image = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_ARGB);
        tFunc = new TransferFunction(volume.getMinimum(), volume.getMaximum());
        tFunc.setTestFunc();
        tFunc.addTFChangeListener(this);
        tfEditor = new TransferFunctionEditor(tFunc, volume.getHistogram());
        
        tfEditor2D = new TransferFunction2DEditor(volume, gradients);
        tfEditor2D.addTFChangeListener(this);

        System.out.println("Finished initialization of RaycastRenderer");
    }

    public RaycastRendererPanel getPanel() {
        return panel;
    }

    public TransferFunction2DEditor getTF2DPanel() {
        return tfEditor2D;
    }
    
    public TransferFunctionEditor getTFPanel() {
        return tfEditor;
    }
     
    public void setShadingMode(boolean mode) {
        shadingMode = mode;
        changed();
    }
    
    public void setMIPMode() {
        setMode(false, true, false, false);
    }
    
    public void setSlicerMode() {
        setMode(true, false, false, false);
    }
    
    public void setCompositingMode() {
        setMode(false, false, true, false);
    }
    
    public void setTF2DMode() {
        setMode(false, false, false, true);
    }
    
    private void setMode(boolean slicer, boolean mip, boolean composite, boolean tf2d) {
        slicerMode = slicer;
        mipMode = mip;
        compositingMode = composite;
        tf2dMode = tf2d;        
        changed();
    }
    
        
    private void drawBoundingBox(GL2 gl) {
        gl.glPushAttrib(GL2.GL_CURRENT_BIT);
        gl.glDisable(GL2.GL_LIGHTING);
        gl.glColor4d(1.0, 1.0, 1.0, 1.0);
        gl.glLineWidth(1.5f);
        gl.glEnable(GL.GL_LINE_SMOOTH);
        gl.glHint(GL.GL_LINE_SMOOTH_HINT, GL.GL_NICEST);
        gl.glEnable(GL.GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

        gl.glBegin(GL.GL_LINE_LOOP);
        gl.glVertex3d(-volume.getDimX() / 2.0, -volume.getDimY() / 2.0, volume.getDimZ() / 2.0);
        gl.glVertex3d(-volume.getDimX() / 2.0, volume.getDimY() / 2.0, volume.getDimZ() / 2.0);
        gl.glVertex3d(volume.getDimX() / 2.0, volume.getDimY() / 2.0, volume.getDimZ() / 2.0);
        gl.glVertex3d(volume.getDimX() / 2.0, -volume.getDimY() / 2.0, volume.getDimZ() / 2.0);
        gl.glEnd();

        gl.glBegin(GL.GL_LINE_LOOP);
        gl.glVertex3d(-volume.getDimX() / 2.0, -volume.getDimY() / 2.0, -volume.getDimZ() / 2.0);
        gl.glVertex3d(-volume.getDimX() / 2.0, volume.getDimY() / 2.0, -volume.getDimZ() / 2.0);
        gl.glVertex3d(volume.getDimX() / 2.0, volume.getDimY() / 2.0, -volume.getDimZ() / 2.0);
        gl.glVertex3d(volume.getDimX() / 2.0, -volume.getDimY() / 2.0, -volume.getDimZ() / 2.0);
        gl.glEnd();

        gl.glBegin(GL.GL_LINE_LOOP);
        gl.glVertex3d(volume.getDimX() / 2.0, -volume.getDimY() / 2.0, -volume.getDimZ() / 2.0);
        gl.glVertex3d(volume.getDimX() / 2.0, -volume.getDimY() / 2.0, volume.getDimZ() / 2.0);
        gl.glVertex3d(volume.getDimX() / 2.0, volume.getDimY() / 2.0, volume.getDimZ() / 2.0);
        gl.glVertex3d(volume.getDimX() / 2.0, volume.getDimY() / 2.0, -volume.getDimZ() / 2.0);
        gl.glEnd();

        gl.glBegin(GL.GL_LINE_LOOP);
        gl.glVertex3d(-volume.getDimX() / 2.0, -volume.getDimY() / 2.0, -volume.getDimZ() / 2.0);
        gl.glVertex3d(-volume.getDimX() / 2.0, -volume.getDimY() / 2.0, volume.getDimZ() / 2.0);
        gl.glVertex3d(-volume.getDimX() / 2.0, volume.getDimY() / 2.0, volume.getDimZ() / 2.0);
        gl.glVertex3d(-volume.getDimX() / 2.0, volume.getDimY() / 2.0, -volume.getDimZ() / 2.0);
        gl.glEnd();

        gl.glBegin(GL.GL_LINE_LOOP);
        gl.glVertex3d(-volume.getDimX() / 2.0, volume.getDimY() / 2.0, -volume.getDimZ() / 2.0);
        gl.glVertex3d(-volume.getDimX() / 2.0, volume.getDimY() / 2.0, volume.getDimZ() / 2.0);
        gl.glVertex3d(volume.getDimX() / 2.0, volume.getDimY() / 2.0, volume.getDimZ() / 2.0);
        gl.glVertex3d(volume.getDimX() / 2.0, volume.getDimY() / 2.0, -volume.getDimZ() / 2.0);
        gl.glEnd();

        gl.glBegin(GL.GL_LINE_LOOP);
        gl.glVertex3d(-volume.getDimX() / 2.0, -volume.getDimY() / 2.0, -volume.getDimZ() / 2.0);
        gl.glVertex3d(-volume.getDimX() / 2.0, -volume.getDimY() / 2.0, volume.getDimZ() / 2.0);
        gl.glVertex3d(volume.getDimX() / 2.0, -volume.getDimY() / 2.0, volume.getDimZ() / 2.0);
        gl.glVertex3d(volume.getDimX() / 2.0, -volume.getDimY() / 2.0, -volume.getDimZ() / 2.0);
        gl.glEnd();

        gl.glDisable(GL.GL_LINE_SMOOTH);
        gl.glDisable(GL.GL_BLEND);
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glPopAttrib();

    }
    
    private boolean intersectLinePlane(double[] plane_pos, double[] plane_normal,
            double[] line_pos, double[] line_dir, double[] intersection) {

        double[] tmp = new double[3];

        for (int i = 0; i < 3; i++) {
            tmp[i] = plane_pos[i] - line_pos[i];
        }

        double denom = VectorMath.dotproduct(line_dir, plane_normal);
        if (Math.abs(denom) < 1.0e-8) {
            return false;
        }

        double t = VectorMath.dotproduct(tmp, plane_normal) / denom;

        for (int i = 0; i < 3; i++) {
            intersection[i] = line_pos[i] + t * line_dir[i];
        }

        return true;
    }

    private boolean validIntersection(double[] intersection, double xb, double xe, double yb,
            double ye, double zb, double ze) {

        return (((xb - 0.5) <= intersection[0]) && (intersection[0] <= (xe + 0.5))
                && ((yb - 0.5) <= intersection[1]) && (intersection[1] <= (ye + 0.5))
                && ((zb - 0.5) <= intersection[2]) && (intersection[2] <= (ze + 0.5)));

    }

    private void intersectFace(double[] plane_pos, double[] plane_normal,
            double[] line_pos, double[] line_dir, double[] intersection,
            double[] entryPoint, double[] exitPoint) {

        boolean intersect = intersectLinePlane(plane_pos, plane_normal, line_pos, line_dir,
                intersection);
        if (intersect) {

            //System.out.println("Plane pos: " + plane_pos[0] + " " + plane_pos[1] + " " + plane_pos[2]);
            //System.out.println("Intersection: " + intersection[0] + " " + intersection[1] + " " + intersection[2]);
            //System.out.println("line_dir * intersection: " + VectorMath.dotproduct(line_dir, plane_normal));

            double xpos0 = 0;
            double xpos1 = volume.getDimX();
            double ypos0 = 0;
            double ypos1 = volume.getDimY();
            double zpos0 = 0;
            double zpos1 = volume.getDimZ();

            if (validIntersection(intersection, xpos0, xpos1, ypos0, ypos1,
                    zpos0, zpos1)) {
                if (VectorMath.dotproduct(line_dir, plane_normal) > 0) {
                    entryPoint[0] = intersection[0];
                    entryPoint[1] = intersection[1];
                    entryPoint[2] = intersection[2];
                } else {
                    exitPoint[0] = intersection[0];
                    exitPoint[1] = intersection[1];
                    exitPoint[2] = intersection[2];
                }
            }
        }
    }
    
    TFColor tf2dRay(double[] entryPoint, double[] exitPoint, double[] viewVec, double sampleStep) {
        /* to be implemented:  You need to sample the ray and implement the MIP
         * right now it just returns yellow as a color
        */
    	TFColor finalColor= new TFColor(0,0,0,1);
    	short tmpGradient = 0;
        double[] coord = entryPoint;
        short tmpVoxelValue;
        double alpha;
        double radius = tfEditor2D.triangleWidget.radius;
        int intensity = tfEditor2D.triangleWidget.baseIntensity;
        TFColor baseColor = tfEditor2D.triangleWidget.color;
        // calculate max steps by dividing the volumetric data "cube" to the sample step we provide as parameter
        int maxSteps = (int) (VectorMath.distance(exitPoint, entryPoint) / sampleStep);
        int maxColor = volume.getMaximum();
        
        // while we are still in the cube, we iterate over the volumetric samples
        // and calculate their final color value until we reach the end of the cube
        for (int i = 0; i < maxSteps; i++) {
        	coord[0] = coord[0] - (viewVec[0] * sampleStep);
        	coord[1] = coord[1] - (viewVec[1] * sampleStep);
        	coord[2] = coord[2] - (viewVec[2] * sampleStep);
        	if (interpolate) {
	        	tmpGradient	 = (short) gradients.getGradientInterpolate(coord).mag;
	        	tmpVoxelValue = volume.getVoxelInterpolateTrilinear(coord);
        	} else {
	        	tmpGradient	 = (short) gradients.getGradientNN(coord).mag;
	        	tmpVoxelValue = volume.getVoxelNN(coord);
        	}
        	if (tmpGradient == 0 && tmpVoxelValue == intensity){
        		alpha = finalColor.a;
        	} else if (tmpGradient > 0 && intensity >= tmpVoxelValue - (tmpGradient * radius) &&
        				intensity <= tmpVoxelValue + (tmpGradient * radius)) {
        		alpha = 1 - (Math.abs(intensity - tmpVoxelValue)/(tmpGradient*radius));
        	} else {
        		alpha = 0;
        	}
        	finalColor.a = finalColor.a * (1 - alpha) + alpha;
        	finalColor.r = finalColor.r * (1 - alpha) + alpha * baseColor.r;
        	finalColor.g = finalColor.g * (1 - alpha) + alpha * baseColor.g;
        	finalColor.b = finalColor.b * (1 - alpha) + alpha * baseColor.b;
        	
        }
        return finalColor;
	}
    
    TFColor compositing(double[] entryPoint, double[] exitPoint, double[] viewVec, double sampleStep) {
        /* to be implemented:  You need to sample the ray and implement the MIP
         * right now it just returns yellow as a color
        */
    	TFColor finalColor= new TFColor(0,0,0,1);
    	TFColor tmpColor = null;
        double[] coord = entryPoint;
        
        // calculate max steps by dividing the volumetric data "cube" to the sample step we provide as parameter
        int maxSteps = (int) (VectorMath.distance(exitPoint, entryPoint) / sampleStep);
        int maxColor = volume.getMaximum();
        
        // while we are still in the cube, we iterate over the volumetric samples
        // and calculate their final color value until we reach the end of the cube
        for (int i = 0; i < maxSteps; i++) {
        	coord[0] = coord[0] - (viewVec[0] * sampleStep);
        	coord[1] = coord[1] - (viewVec[1] * sampleStep);
        	coord[2] = coord[2] - (viewVec[2] * sampleStep);
        	
        	if (interpolate) {
        		tmpColor = tFunc.getColor(volume.getVoxelInterpolateTrilinear(coord));
        	} else {
        		tmpColor = tFunc.getColor(volume.getVoxelNN(coord));
        	}
        	finalColor.a = finalColor.a * (1 - tmpColor.a) + tmpColor.a;
        	finalColor.r = finalColor.r * (1 - tmpColor.a) + tmpColor.a * tmpColor.r;
        	finalColor.g = finalColor.g * (1 - tmpColor.a) + tmpColor.a * tmpColor.g;
        	finalColor.b = finalColor.b * (1 - tmpColor.a) + tmpColor.a * tmpColor.b;
        	
        }
        return finalColor;
	}
    
    /**
     * Check if coord is beofre exit point. 
     * @param coord
     * @param pointStart pointEnd
     * @return true if pointStart< coord < point (in any dimension)
     */
    //CARE! now just check endpoint not sure if good,chance comments later
    private boolean coordInside(double[] coord, double[] pointStart, double[] pointEnd) {
    	if (coord[0] < pointEnd[0] && coord[1] < pointEnd[1] && coord[2] < pointEnd[2] &&
    		coord[0] >= pointStart[0] && coord[1] >= pointStart[1] && coord[2] >= pointStart[2]){
    		return true;
    	}
    	return false;
    }

	int traceRayMIP(double[] entryPoint, double[] exitPoint, double[] viewVec, double sampleStep) {
        /* to be implemented:  You need to sample the ray and implement the MIP
         * right now it just returns yellow as a color
         */
        double[] coord = entryPoint;
        
        // calculate max steps by dividing the volumetric data "cube" to the sample step we provide as parameter
        int maxSteps = (int) (VectorMath.distance(exitPoint, entryPoint) / sampleStep);
        int i = 0;
        // define variables to hold maximum intensity value found and current (temporary) value
        int maxColor = Integer.MIN_VALUE;
        int tmpColor = 0;
        
        // while we are still in the cube, we iterate over the z-dimension and compare the values. 
        // We keep only the maximum value and return it to be projected as final image on the plane
        while (i <= maxSteps){
        	coord[0] = coord[0] - (viewVec[0] * sampleStep);
        	coord[1] = coord[1] - (viewVec[1] * sampleStep);
        	coord[2] = coord[2] - (viewVec[2] * sampleStep);
        	if (interpolate) { 
        		tmpColor = volume.getVoxelInterpolateTrilinear(coord);
        	} else {
        		tmpColor = volume.getVoxelNN(coord);
        	}
        	if (maxColor < tmpColor){
        		maxColor = tmpColor;
        	}
        	i++;
        }
        return maxColor;
    }
   
    
   
    void computeEntryAndExit(double[] p, double[] viewVec, double[] entryPoint, double[] exitPoint) {

        for (int i = 0; i < 3; i++) {
            entryPoint[i] = -1;
            exitPoint[i] = -1;
        }

        double[] plane_pos = new double[3];
        double[] plane_normal = new double[3];
        double[] intersection = new double[3];

        VectorMath.setVector(plane_pos, volume.getDimX(), 0, 0);
        VectorMath.setVector(plane_normal, 1, 0, 0);
        intersectFace(plane_pos, plane_normal, p, viewVec, intersection, entryPoint, exitPoint);

        VectorMath.setVector(plane_pos, 0, 0, 0);
        VectorMath.setVector(plane_normal, -1, 0, 0);
        intersectFace(plane_pos, plane_normal, p, viewVec, intersection, entryPoint, exitPoint);

        VectorMath.setVector(plane_pos, 0, volume.getDimY(), 0);
        VectorMath.setVector(plane_normal, 0, 1, 0);
        intersectFace(plane_pos, plane_normal, p, viewVec, intersection, entryPoint, exitPoint);

        VectorMath.setVector(plane_pos, 0, 0, 0);
        VectorMath.setVector(plane_normal, 0, -1, 0);
        intersectFace(plane_pos, plane_normal, p, viewVec, intersection, entryPoint, exitPoint);

        VectorMath.setVector(plane_pos, 0, 0, volume.getDimZ());
        VectorMath.setVector(plane_normal, 0, 0, 1);
        intersectFace(plane_pos, plane_normal, p, viewVec, intersection, entryPoint, exitPoint);

        VectorMath.setVector(plane_pos, 0, 0, 0);
        VectorMath.setVector(plane_normal, 0, 0, -1);
        intersectFace(plane_pos, plane_normal, p, viewVec, intersection, entryPoint, exitPoint);

    }

    void raycast(double[] viewMatrix) {
        /* To be partially implemented:
            This function traces the rays through the volume. Have a look and check that you understand how it works.
            You need to introduce here the different modalities MIP/Compositing/TF2/ etc...*/

        double[] viewVec = new double[3];
        double[] uVec = new double[3];
        double[] vVec = new double[3];
        VectorMath.setVector(viewVec, viewMatrix[2], viewMatrix[6], viewMatrix[10]);
        VectorMath.setVector(uVec, viewMatrix[0], viewMatrix[4], viewMatrix[8]);
        VectorMath.setVector(vVec, viewMatrix[1], viewMatrix[5], viewMatrix[9]);


        int imageCenter = image.getWidth() / 2;

        double[] pixelCoord = new double[3];
        double[] entryPoint = new double[3];
        double[] exitPoint = new double[3];
        
        float sampleStep=0.2f;
        


        for (int j = 0; j < image.getHeight(); j++) {
            for (int i = 0; i < image.getWidth(); i++) {
                image.setRGB(i, j, 0);
            }
        }
        boolean activeResolution = false;
        int local_increment = 1;
        if (interactiveMode) {
        	activeResolution = true;
        	local_increment = incrementSize;
        }
        double max = volume.getMaximum();
        TFColor voxelColor = new TFColor();
        float pixelFloat = 0;
        for (int j = 0; j < image.getHeight(); j += local_increment) {
            for (int i = 0; i < image.getWidth(); i += local_increment) {
                // compute starting points of rays in a plane shifted backwards to a position behind the data set
                pixelCoord[0] = uVec[0] * (i - imageCenter) + vVec[0] * (j - imageCenter) - viewVec[0] * imageCenter
                        + volume.getDimX() / 2.0;
                pixelCoord[1] = uVec[1] * (i - imageCenter) + vVec[1] * (j - imageCenter) - viewVec[1] * imageCenter
                        + volume.getDimY() / 2.0;
                pixelCoord[2] = uVec[2] * (i - imageCenter) + vVec[2] * (j - imageCenter) - viewVec[2] * imageCenter
                        + volume.getDimZ() / 2.0;

                computeEntryAndExit(pixelCoord, viewVec, entryPoint, exitPoint);
                if ((entryPoint[0] > -1.0) && (exitPoint[0] > -1.0)) {
                    //System.out.println("Entry: " + entryPoint[0] + " " + entryPoint[1] + " " + entryPoint[2]);
                    //System.out.println("Exit: " + exitPoint[0] + " " + exitPoint[1] + " " + exitPoint[2]);
                    int pixelColor = 0;
                    if(compositingMode) { 
                        voxelColor = compositing(entryPoint,exitPoint,viewVec,sampleStep);
                        
                    }
                    /* set color to green if MipMode- see slicer function*/
                   if(mipMode) {
                        pixelColor= traceRayMIP(entryPoint,exitPoint,viewVec,sampleStep);
	                   voxelColor.r = pixelColor/max;
	                   voxelColor.g = voxelColor.r;
	                   voxelColor.b = voxelColor.r;
	                   voxelColor.a = pixelColor > 0 ? 1.0 : 0.0;
                   } else if (tf2dMode) {
                	   voxelColor = tf2dRay(entryPoint,exitPoint,viewVec,sampleStep);
                   }
                                
                   // BufferedImage expects a pixel color packed as ARGB in an int
                   int c_alpha = voxelColor.a <= 1.0 ? (int) Math.floor(voxelColor.a * 255) : 255;
                   int c_red = voxelColor.r <= 1.0 ? (int) Math.floor(voxelColor.r * 255) : 255;
                   int c_green = voxelColor.g <= 1.0 ? (int) Math.floor(voxelColor.g * 255) : 255;
                   int c_blue = voxelColor.b <= 1.0 ? (int) Math.floor(voxelColor.b * 255) : 255;
                   pixelColor = (c_alpha << 24) | (c_red << 16) | (c_green << 8) | c_blue;
                   image.setRGB(i, j, pixelColor);
                   if (activeResolution) {
                   	for (int incr_j = j; incr_j < j+local_increment && incr_j < image.getHeight(); incr_j++){
                   		for (int incr_i = i; incr_i < i+local_increment && incr_i < image.getWidth(); incr_i++){
                           	image.setRGB(incr_i, incr_j, pixelColor);
                       	}
                   	}
                   } else {
                   	image.setRGB(i, j, pixelColor);
                   }
                }
            }
        }


    }

    private int tf2Color(double[] entryPoint, double[] exitPoint, double[] viewVec, float sampleStep) {
		
		return 0;
	}

	void slicer(double[] viewMatrix) {

        // clear image
        for (int j = 0; j < image.getHeight(); j++) {
            for (int i = 0; i < image.getWidth(); i++) {
                image.setRGB(i, j, 0);
            }
        }
        boolean activeResolution = false;
        int local_increment = 1;
        if (interactiveMode) {
        	activeResolution = true;
        	local_increment = incrementSize;
        }
        // vector uVec and vVec define a plane through the origin, 
        // perpendicular to the view vector viewVec
        double[] viewVec = new double[3];
        double[] uVec = new double[3];
        double[] vVec = new double[3];
        VectorMath.setVector(viewVec, viewMatrix[2], viewMatrix[6], viewMatrix[10]);
        VectorMath.setVector(uVec, viewMatrix[0], viewMatrix[4], viewMatrix[8]);
        VectorMath.setVector(vVec, viewMatrix[1], viewMatrix[5], viewMatrix[9]);

        // image is square
        int imageCenter = image.getWidth() / 2;

        double[] pixelCoord = new double[3];
        double[] volumeCenter = new double[3];
        VectorMath.setVector(volumeCenter, volume.getDimX() / 2, volume.getDimY() / 2, volume.getDimZ() / 2);

        // sample on a plane through the origin of the volume data
        double max = volume.getMaximum();
        TFColor voxelColor = new TFColor();
        for (int j = 0; j < image.getHeight(); j += local_increment) {
            for (int i = 0; i < image.getWidth(); i += local_increment) {
                pixelCoord[0] = uVec[0] * (i - imageCenter) + vVec[0] * (j - imageCenter)
                        + volumeCenter[0];
                pixelCoord[1] = uVec[1] * (i - imageCenter) + vVec[1] * (j - imageCenter)
                        + volumeCenter[1];
                pixelCoord[2] = uVec[2] * (i - imageCenter) + vVec[2] * (j - imageCenter)
                        + volumeCenter[2];
                int val;
                //Switch to decide which color creation to apply.
                switch (currSlicerType) {
                case NEARESTNEIGH:
                	if (interpolate){
                		val = volume.getVoxelInterpolateTrilinear(pixelCoord);
                	} else {
                		val = volume.getVoxelNN(pixelCoord);
                	}
                    voxelColor.r = val/max;
                    voxelColor.g = voxelColor.r;
                    voxelColor.b = voxelColor.r;
                    voxelColor.a = val > 0 ? 1.0 : 0.0;  // this makes intensity 0 completely transparent and the rest opaque
                	break;
                case TRANSFER:
                	if (interpolate){
                		val = volume.getVoxelInterpolateTrilinear(pixelCoord);
                	} else {
                		val = volume.getVoxelNN(pixelCoord);
                	}
                    // Alternatively, apply the transfer function to obtain a color 
                    voxelColor = tFunc.getColor(val);
                	break;
                }
                
                
                // BufferedImage expects a pixel color packed as ARGB in an int
                int c_alpha = voxelColor.a <= 1.0 ? (int) Math.floor(voxelColor.a * 255) : 255;
                int c_red = voxelColor.r <= 1.0 ? (int) Math.floor(voxelColor.r * 255) : 255;
                int c_green = voxelColor.g <= 1.0 ? (int) Math.floor(voxelColor.g * 255) : 255;
                int c_blue = voxelColor.b <= 1.0 ? (int) Math.floor(voxelColor.b * 255) : 255;
                int pixelColor = (c_alpha << 24) | (c_red << 16) | (c_green << 8) | c_blue;
                
                if (activeResolution) {
                	for (int incr_j = j; incr_j < j+local_increment && incr_j < image.getHeight(); incr_j++){
                		for (int incr_i = i; incr_i < i+local_increment && incr_i < image.getWidth(); incr_i++){
                        	image.setRGB(incr_i, incr_j, pixelColor);
                    	}
                	}
                } else {
                	image.setRGB(i, j, pixelColor);
                }
            }
        }
    }


    @Override
    public void visualize(GL2 gl) {


        if (volume == null) {
            return;
        }
        drawBoundingBox(gl);

        gl.glGetDoublev(GL2.GL_MODELVIEW_MATRIX, viewMatrix, 0);
        
        long startTime = System.currentTimeMillis();
        if (!slowMode) {
	        if (slicerMode) {
	            slicer(viewMatrix);    
	        } else {
	            raycast(viewMatrix);
	        }
        } else {
        	if (skipOne) {
        		skipOne = false;
        	} else {
        		if (slicerMode) {
    	            slicer(viewMatrix);    
    	        } else {
    	            raycast(viewMatrix);
    	        }
        		skipOne = true;
        	}
        }
        long endTime = System.currentTimeMillis();
        double runningTime = (endTime - startTime);
        panel.setSpeedLabel(Double.toString(runningTime));

        Texture texture = AWTTextureIO.newTexture(gl.getGLProfile(), image, false);

        gl.glPushAttrib(GL2.GL_LIGHTING_BIT);
        gl.glDisable(GL2.GL_LIGHTING);
        gl.glEnable(GL.GL_BLEND);
        gl.glBlendFunc(GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA);

        // draw rendered image as a billboard texture
        texture.enable(gl);
        texture.bind(gl);
        double halfWidth = image.getWidth() / 2.0;
        gl.glPushMatrix();
        gl.glLoadIdentity();
        gl.glBegin(GL2.GL_QUADS);
        gl.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        gl.glTexCoord2d(0.0, 0.0);
        gl.glVertex3d(-halfWidth, -halfWidth, 0.0);
        gl.glTexCoord2d(0.0, 1.0);
        gl.glVertex3d(-halfWidth, halfWidth, 0.0);
        gl.glTexCoord2d(1.0, 1.0);
        gl.glVertex3d(halfWidth, halfWidth, 0.0);
        gl.glTexCoord2d(1.0, 0.0);
        gl.glVertex3d(halfWidth, -halfWidth, 0.0);
        gl.glEnd();
        texture.disable(gl);
        texture.destroy(gl);
        gl.glPopMatrix();

        gl.glPopAttrib();
        

        //If interactive mode is active, count the frames.
        if(interactiveMode) {
          frames++;
          //After counting frames for 1 second, update fps counter
          //Please note that initial activeStart is done in Renderer class.
          if (System.currentTimeMillis() - activeStart > 1000) {
            framesPerSec = frames;
            frames = 0;
            activeStart = System.currentTimeMillis();
            panel.setFpsLabel(Double.toString(framesPerSec));
          }
        }

        if (gl.glGetError() > 0) {
            System.out.println("some OpenGL error: " + gl.glGetError());
        }

    }
    private BufferedImage image;
    private double[] viewMatrix = new double[4 * 4];

    @Override
    public void changed() {
        for (int i=0; i < listeners.size(); i++) {
            listeners.get(i).changed();
        }
    }

    // UGLY for gui, science purposes.
	public void SetSlicerTransfer(boolean selected) {
		if (true) {
			currSlicerType = SlicerType.TRANSFER;
			changed();
		}
		
	}
	public void SetSlicerNN(boolean selected) {
		if (true) {
			currSlicerType = SlicerType.NEARESTNEIGH;
			changed();
		}
	}
	public void setIncrementSize(int size) {
		if (size > 0) {
			incrementSize = size;
		}
	}
}