package extapi;

import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * @author Norman Fomferra
 */
public interface ExtApi extends Library {
    ExtApi INSTANCE = (ExtApi) Native.loadLibrary("beam-extapi", ExtApi.class);

    double compute_tile(double[] tile, int size);
}
