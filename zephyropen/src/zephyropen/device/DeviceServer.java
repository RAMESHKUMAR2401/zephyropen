package zephyropen.device;

import zephyropen.api.ZephyrOpen;

/**
 * <p>
 * A base class server for the Zephyr BlueTooth devices. Based on the naming conventions
 * from Zephyr, this server will create and connect to the bluetooth device.
 * 
 * If a connection can be established, the server will start a watch dog timer thread and
 * start reading the serial data. The connection will time out and be closed if the data
 * stream is lost, it is up to the daemon services to start new server processes to
 * maintain the connection.
 * <p>
 * http://www.zephyrtech.co.nz/
 * 
 * 
 * @author <a href="mailto:brad.zdanivsky@gmail.com">Brad Zdanivsky</a>
 */
public class DeviceServer {

    /** framework configuration */
    public static ZephyrOpen constants = ZephyrOpen.getReference();

    /** time to sleep while in watch dog thread */
    public final static int SPIN_TIME = 1000;

    /** the device to read from */
    private Device device = null;

    /**
     * <p>
     * Constructor for the DeviceServer. Use a factory to create a server for the specific
     * device based only on the naming convention from the manufacturing company.
     * 
     * @param deviceName
     *            is the blue tooth friendly name of the target device.
     */
    public DeviceServer() {
    	
        // Create a server 
        device = DeviceFactory.create();

        if (device == null) {
            constants.error("Can't create device, terminate.", this);
            constants.shutdown();
        }

        if (device.connect()) {

        	// keep checking the device delta
        	if( constants.getBoolean(ZephyrOpen.enableWatchDog))
        		new WatchDog(device).start();
           
            // blocking call 
            device.readDevice();

        } else {

            constants.info("can't connect ", this);
            
        }

        constants.info("closed device name = " + device.getDeviceName(), this);
        device.close();
        constants.shutdown();
    }

    
    /**
     * <p>
     * Constructor for the DeviceServer. Use a factory to create a server for the specific
     * device based only on the naming convention from the manufacturing company.
     * 
     * @param deviceName
     *            is the blue tooth friendly name of the target device.
    
    public DeviceServer(String dev, String addr) {
    	
    	constants.put(ZephyrOpen.userName, "brad");
    	constants.put(ZephyrOpen.deviceName, dev);
    	constants.put(ZephyrOpen.port, addr);

        // Create a server 
        device = DeviceFactory.create(); //dev, addr);

        if (device == null) {
            constants.error("Can't create device, terminate.", this);
            constants.shutdown();
        }

        if (device.connect()) {

        	// keep checking the device delta
        	if( constants.getBoolean(ZephyrOpen.enableWatchDog))
        		new WatchDog(device).start();
           
            // blocking call 
            device.readDevice();

        } else {

            constants.info("can't connect ", this);
            
        }

        constants.info("closed device name = " + device.getDeviceName(), this);
        device.close();
        constants.shutdown();
    } */

    
    
    // 
    // Java DeviceServer userName deviceName
    //
    /*
    public static void main(String[] args) {

        if (args.length == 2) {

            // configure the framework with properties file 
            constants.init();

            // properties file must supply the device Name 
            new DeviceServer(args[0], args[1]);
             
        } else System.out.println("can't start device with args = " + args.length);
    } 
     */
   
    
    /*
     * Use command line arguments to configure the framework with given properties file
     * 
     * {@code java DeviceServer polar.propetries} {@code java Server zephyr.properties}
    */
    public static void main(String[] args) {

        if (args.length == 1) {

            // configure the framework with properties file 
            constants.init(args[0]);

            // properties file must supply the device Name 
            new DeviceServer();
        }
    } 
    
    
}