package extapi;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;

/**
* @author Norman Fomferra
*/
public interface CLib extends Library {
    CLib INSTANCE = (CLib) Native.loadLibrary((Platform.isWindows() ? "msvcrt" : "c"), CLib.class);

    void printf(String format, Object... args);
}
