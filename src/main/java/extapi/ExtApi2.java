package extapi;

import com.sun.jna.Native;

/**
 * @author Norman Fomferra
 */
public class ExtApi2 {
    static {
        Native.register("beam-extapi");
    }

    public static native double compute_tile(double[] tile, int size);
}
