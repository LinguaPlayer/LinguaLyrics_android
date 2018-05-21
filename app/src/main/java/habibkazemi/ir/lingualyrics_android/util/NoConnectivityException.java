package habibkazemi.ir.lingualyrics_android.util;

import java.io.IOException;

/**
 * Created by habibkazemi on 5/21/18.
 */

public class NoConnectivityException extends IOException {

    @Override
    public String getMessage() {
        return "No connectivity exception";
    }

}
