/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package volume;

/**
 *
 * @author michel
 * @ Anna
 * This class contains the pre-computes gradients of the volume. This means calculates the gradient
 * at all voxel positions, and provides functions
 * to get the gradient at any position in the volume also continuous..
*/
public class GradientVolume {

    public GradientVolume(Volume vol) {
        volume = vol;
        dimX = vol.getDimX();
        dimY = vol.getDimY();
        dimZ = vol.getDimZ();
        data = new VoxelGradient[dimX * dimY * dimZ];
        compute();
        //Removed maxmag for safety, either have to always update it, or keep dirty flag.
        //Use getMaxGradientMagnitude function
//        maxmag = -1.0;
    }

    public VoxelGradient getGradient(int x, int y, int z) {
        return data[x + dimX * (y + dimY * z)];
    }

    private VoxelGradient interpolate(VoxelGradient g0, VoxelGradient g1, float factor) {
        /* To be implemented: this function linearly interpolates gradient vector g0 and g1 given the factor (t) 
            the resut is given at result. You can use it to tri-linearly interpolate the gradient */
    	float x = g0.x * factor + g1.x * (1 - factor);
    	float y = g0.y * factor + g1.y * (1 - factor);
    	float z = g0.z * factor + g1.z * (1 - factor);
        return new VoxelGradient(x,y,z);
    }
    
    public VoxelGradient getGradientNN(double[] coord) {
        /* Nearest neighbour interpolation applied to provide the gradient */
        if (coord[0] < 0 || coord[0] > (dimX-2) || coord[1] < 0 || coord[1] > (dimY-2)
                || coord[2] < 0 || coord[2] > (dimZ-2)) {
            return zero;
        }

        int x = (int) Math.round(coord[0]);
        int y = (int) Math.round(coord[1]);
        int z = (int) Math.round(coord[2]);
        
        return getGradient(x, y, z);
    }

    
    public VoxelGradient getGradientInterpolate(double[] coord) {
    /* To be implemented: Returns trilinear interpolated gradient based on the precomputed gradients. 
     *   Use function interpolate. Use getGradientNN as bases */
        
        if (coord[0] < 0 || coord[0] > (dimX-1) || coord[1] < 0 || coord[1] > (dimY-1)
                || coord[2] < 0 || coord[2] > (dimZ-1)) {
            return new VoxelGradient();
        }
        
        // Get the 8 corners around the coordinate.
        int x_f = (int) Math.floor(coord[0]);
        int x_b = (int) Math.ceil(coord[0]);
        int y_f = (int) Math.floor(coord[1]);
        int y_b = (int) Math.ceil(coord[1]);
        int z_f = (int) Math.floor(coord[2]);
        int z_b = (int) Math.ceil(coord[2]);
        
        //Calculate the distances in each axis.
        float dist_x = (float) Math.abs(x_f - coord[0]);
        float dist_y = (float) Math.abs(y_f - coord[1]);
        float dist_z = (float) Math.abs(z_f - coord[2]);
        
        //Get the 8 voxels around the coordinate
      	VoxelGradient voxel000 = getGradient(x_f, y_f, z_f);
      	VoxelGradient voxel100 = getGradient(x_b, y_f, z_f);
      	
      	VoxelGradient voxel010 = getGradient(x_f, y_b, z_f);
      	VoxelGradient voxel110 = getGradient(x_b, y_b, z_f);
      	
      	VoxelGradient voxel001 = getGradient(x_f, y_f, z_b);
      	VoxelGradient voxel101 = getGradient(x_b, y_f, z_b);
      	
      	VoxelGradient voxel011 = getGradient(x_f, y_b, z_b);
      	VoxelGradient voxel111 = getGradient(x_b, y_b, z_b);
  
  
      	//Apply trilinear by linear interpolating in 3 steps
      	//First x dimension, 4 times. 
      	VoxelGradient val_x1_front = interpolate(voxel000, voxel100, dist_x);
      	VoxelGradient val_x2_front = interpolate(voxel010 ,voxel110, dist_x);
      	
      	VoxelGradient val_x1_back = interpolate(voxel001, voxel101, dist_x);
      	VoxelGradient val_x2_back = interpolate(voxel011, voxel111, dist_x);
  
      	//Second y dimension, 2 times.
      	VoxelGradient val_y_front = interpolate(val_x1_front, val_x2_front, dist_y);
      	VoxelGradient val_y_back = interpolate(val_x1_back, val_x2_back, dist_y);
      	
      	//Lastly z dimension, 1 time.
      	VoxelGradient val_z = interpolate(val_y_front, val_y_back, dist_z);
      	
      	//Return the trilinear value for coord.
      	return val_z;
    }
    
  
    public void setGradient(int x, int y, int z, VoxelGradient value) {
        data[x + dimX * (y + dimY * z)] = value;
    }

    public void setVoxel(int i, VoxelGradient value) {
        data[i] = value;
    }

    public VoxelGradient getVoxel(int i) {
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
    
    /**
     * Compute the gradient based on the volume.
     * Borders are handled by getSafeVoxel which will give the closest existing voxel when 
     * a value is undefined.
     */
    private void compute() {
      int dim_x = volume.getDimX();
      int dim_y = volume.getDimY();
      int dim_z = volume.getDimZ();
      float tmp_x, tmp_y, tmp_z;
      for (int z = 0; z < dim_z; z++) {
        for (int y = 0; y < dim_y; y++) {
          for (int x = 0; x < dim_x; x++) {
            tmp_x = (volume.getSafeVoxel(x+1, y, z) - volume.getSafeVoxel(x-1, y, z)) / 2;
            tmp_y = (volume.getSafeVoxel(x, y+1, z) - volume.getSafeVoxel(x, y-1, z)) / 2;
            tmp_z = (volume.getSafeVoxel(x, y, z+1) - volume.getSafeVoxel(x-1, y, z-1)) / 2;
            setGradient(x,y,z,new VoxelGradient(tmp_x, tmp_y, tmp_z));
          }
        }
      }
    }
    
    private double setMaximum = Float.MIN_VALUE;
    public double getMaxGradientMagnitude() {
        float max = Float.MIN_VALUE;
        if (setMaximum != max) {
        	return setMaximum;
        }
        for (int i = 0; i < data.length; i++) {
          if (data[i].mag > max) {
            max = data[i].mag;
          }
        }
        setMaximum = max;
        return max;
    }
    
    private int dimX, dimY, dimZ;
    private VoxelGradient zero = new VoxelGradient();
    VoxelGradient[] data;
    Volume volume;
//    double maxmag;
}
