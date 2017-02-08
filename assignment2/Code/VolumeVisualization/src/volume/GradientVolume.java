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

    private void interpolate(VoxelGradient g0, VoxelGradient g1, float factor, VoxelGradient result) {
        /* To be implemented: this function linearly interpolates gradient vector g0 and g1 given the factor (t) 
            the resut is given at result. You can use it to tri-linearly interpolate the gradient */
        
        
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

    
    public VoxelGradient getGradient(double[] coord) {
    /* To be implemented: Returns trilinear interpolated gradient based on the precomputed gradients. 
     *   Use function interpolate. Use getGradientNN as bases */

        return getGradient(0, 0, 0);
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
    
    public double getMaxGradientMagnitude() {
        float max = Float.MIN_VALUE;
        for (int i = 0; i < data.length; i++) {
          if (data[i].mag > max) {
            max = data[i].mag;
          }
        }
        return max;
    }
    
    private int dimX, dimY, dimZ;
    private VoxelGradient zero = new VoxelGradient();
    VoxelGradient[] data;
    Volume volume;
//    double maxmag;
}
