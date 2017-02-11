/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package volume;

import java.io.File;
import java.io.IOException;

import volvis.TransferFunction;

/**
 *
 * @author michel
 * @Anna 
 * Volume object: This class contains the object and assumes that the distance between the voxels in x,y and z are 1 
 */
public class Volume {

    
    private int dimX, dimY, dimZ;
    private short[] data;
    private int[] histogram;
	
    public Volume(int xd, int yd, int zd) {
        data = new short[xd*yd*zd];
        dimX = xd;
        dimY = yd;
        dimZ = zd;
    }
    
    public Volume(File file) {
        
        try {
            VolumeIO reader = new VolumeIO(file);
            dimX = reader.getXDim();
            dimY = reader.getYDim();
            dimZ = reader.getZDim();
            data = reader.getData().clone();
            computeHistogram();
        } catch (IOException ex) {
            System.out.println("IO exception");
        }
        
    }
    
    
    public short getVoxel(int x, int y, int z) {
        return data[x + dimX*(y + dimY * z)];
    }
    
    public void setVoxel(int x, int y, int z, short value) {
        data[x + dimX*(y + dimY*z)] = value;
    }

    public void setVoxel(int i, short value) {
        data[i] = value;
    }
    
    public short getVoxelNN(double[] coord) {

		if (coord[0] < 0 || coord[0] > (dimX-1) || coord[1] < 0 || coord[1] > (dimY-1)
				|| coord[2] < 0 || coord[2] > (dimZ-1)) {
			return 0;
		}
		/* notice that in this framework we assume that the distance between neighbouring voxels is 1 in all directions*/
		int x = (int) Math.round(coord[0]); 
		int y = (int) Math.round(coord[1]);
		int z = (int) Math.round(coord[2]);
		return getVoxel(x, y, z);
    }
    
//    public short getVoxelInterpolate(double[] coord) {
//    	return getVoxelInterpolate(coord, false);
//    }
    
//    public short getVoxelInterpolate(double[] coord, boolean trilinear) {
//    /* to be implemented: get the trilinear interpolated value. 
//        The current implementation gets the Nearest Neightbour */
//        
//        if (coord[0] < 0 || coord[0] > (dimX-1) || coord[1] < 0 || coord[1] > (dimY-1)
//                || coord[2] < 0 || coord[2] > (dimZ-1)) {
//            return 0;
//        }
//        if (!trilinear){
//	        /* notice that in this framework we assume that the distance between neighbouring voxels is 1 in all directions*/
//	        int x = (int) Math.round(coord[0]); 
//	        int y = (int) Math.round(coord[1]);
//	        int z = (int) Math.round(coord[2]);
//
////	        System.out.print(getVoxel(x, y, z));
////	        System.out.print("\n");
//
//	        return getVoxel(x, y, z);
//        }else {
//        	return getVoxelInterpolateTrilinear(coord);
//        }
//    }
    
    public short getVoxelInterpolateTrilinear(double[] coord) {
    /* to be implemented: get the trilinear interpolated value. 
        The current implementation gets the Nearest Neightbour */
        
        if (coord[0] < 0 || coord[0] > (dimX-1) || coord[1] < 0 || coord[1] > (dimY-1)
                || coord[2] < 0 || coord[2] > (dimZ-1)) {
            return 0;
        }
        
        int x_f = (int) Math.floor(coord[0]);
        int x_b = (int) Math.ceil(coord[0]);
        int y_f = (int) Math.floor(coord[1]);
        int y_b = (int) Math.ceil(coord[1]);
        int z_f = (int) Math.floor(coord[2]);
        int z_b = (int) Math.ceil(coord[2]);
        
        double dist_x = Math.abs(x_f - coord[0]);
        double dist_y = Math.abs(y_f - coord[1]);
        double dist_z = Math.abs(z_f - coord[2]);
        

    	int voxel000 = getVoxel(x_f, y_f, z_f);
    	int voxel100 = getVoxel(x_b, y_f, z_f);
    	
    	int voxel010 = getVoxel(x_f, y_b, z_f);
    	int voxel110 = getVoxel(x_b, y_b, z_f);
    	
    	int voxel001 = getVoxel(x_f, y_f, z_b);
    	int voxel101 = getVoxel(x_b, y_f, z_b);
    	
    	int voxel011 = getVoxel(x_f, y_b, z_b);
    	int voxel111 = getVoxel(x_b, y_b, z_b);



    	double val_x1_front = voxel000 * (1 - dist_x) + voxel100 * dist_x;
    	double val_x2_front = voxel010 * (1 - dist_x) + voxel110 * dist_x;
    	
    	double val_x1_back = voxel001 * (1 - dist_x) + voxel101 * dist_x;
    	double val_x2_back = voxel011 * (1 - dist_x) + voxel111 * dist_x;

    	double val_y_front = val_x1_front * (1 - dist_y) + val_x2_front * dist_y;
    	double val_y_back = val_x1_back * (1 - dist_y) + val_x2_back * dist_y;
    	
    	short val_z = (short) (val_y_front * (1 - dist_z) + val_y_back * dist_z); 
    	return val_z;
        
    }

	public short getVoxel(int i) {
        return data[i];
    }
    
    public int getDimX() {
        return dimX;
    }
    
    public int getDimY() {
        return dimY;
    }
    
    public int getDimZ() {
        return dimZ;
    }

    public short getMinimum() {
        short minimum = data[0];
        for (int i=0; i<data.length; i++) {
            minimum = data[i] < minimum ? data[i] : minimum;
        }
        return minimum;
    }

    private short max = 0;
    public short getMaximum() {
    	if (max != 0){
    		return max;
    	}
        short maximum = data[0];
        for (int i=0; i<data.length; i++) {
            maximum = data[i] > maximum ? data[i] : maximum;
        }
        max = maximum;
        return maximum;
    }
 
    public int[] getHistogram() {
        return histogram;
    }
    
    private void computeHistogram() {
        histogram = new int[getMaximum() + 1];
        for (int i=0; i<data.length; i++) {
            histogram[data[i]]++;
        }
    }
}
