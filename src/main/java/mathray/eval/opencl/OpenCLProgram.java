package mathray.eval.opencl;

import static org.jocl.CL.*;

import org.jocl.CL;
import org.jocl.cl_command_queue;
import org.jocl.cl_context;
import org.jocl.cl_context_properties;
import org.jocl.cl_device_id;
import org.jocl.cl_kernel;
import org.jocl.cl_platform_id;
import org.jocl.cl_program;

import mathray.device.FunctionTypes;

public class OpenCLProgram implements FunctionTypes.FillerF, FunctionTypes.RepeatF {
  
  private static cl_context context;
  
  static {
    final int platformIndex = 0;
    final long deviceType = CL_DEVICE_TYPE_ALL;
    final int deviceIndex = 0;
    
    CL.setExceptionsEnabled(true);
    
    int numPlatformsArray[] = new int[1];
    clGetPlatformIDs(0, null, numPlatformsArray);
    int numPlatforms = numPlatformsArray[0];

    // Obtain a platform ID
    cl_platform_id platforms[] = new cl_platform_id[numPlatforms];
    clGetPlatformIDs(platforms.length, platforms, null);
    cl_platform_id platform = platforms[platformIndex];
    
    cl_context_properties contextProperties = new cl_context_properties();
    contextProperties.addProperty(CL_CONTEXT_PLATFORM, platform);
    
    // Obtain the number of devices for the platform
    int numDevicesArray[] = new int[1];
    clGetDeviceIDs(platform, deviceType, 0, null, numDevicesArray);
    int numDevices = numDevicesArray[0];
    
    // Obtain a device ID 
    cl_device_id devices[] = new cl_device_id[numDevices];
    clGetDeviceIDs(platform, deviceType, numDevices, devices, null);
    cl_device_id device = devices[deviceIndex];

    // Create a context for the selected device
    context = clCreateContext(
        contextProperties, 1, new cl_device_id[]{device}, 
        null, null, null);
    
    // Create a command-queue for the selected device
    cl_command_queue commandQueue = 
        clCreateCommandQueue(context, device, 0, null);
  }
  
  cl_program program;
  cl_kernel kernel;
  
  OpenCLProgram(String source, String kernelName) {
    program = clCreateProgramWithSource(context, 1, new String[] {source}, null, null);
    clBuildProgram(program, 0, null, null, null, null);
    clCreateKernel(program, kernelName, null);
  }

  @Override
  public void repeat(float[] args, float[] res) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void fill(float[] limits, int[] counts, float[] result) {
    // TODO Auto-generated method stub
    
  }

}
