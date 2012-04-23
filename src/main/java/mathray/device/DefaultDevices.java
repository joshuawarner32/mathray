package mathray.device;

import java.util.HashSet;
import java.util.Set;

import mathray.eval.java.JavaDevice;
import mathray.eval.machine.VisitorDevice;
import mathray.eval.opencl.OpenCLDevice;

public class DefaultDevices {

  private static final VisitorDevice visitorDevice = new VisitorDevice();
  
  private static final JavaDevice javaDevice = new JavaDevice();
  
  private static final OpenCLDevice openclDevice = new OpenCLDevice();
  
  private static final Device allDevices;
  
  static {
    Set<Device> devices = new HashSet<Device>();
    devices.add(visitorDevice);
    
    allDevices = new MultiDevice(devices);
  }
  
}
