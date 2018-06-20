package habibkazemi.ir.lingualyrics_android.Log;

import android.support.annotation.Nullable;
import android.util.Log;

import timber.log.Timber;

public class CrashReportingTree extends Timber.Tree {
    @Override
    protected void log(int priority, @Nullable String tag,@Nullable  String message, @Nullable Throwable t) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG) {
            return;
        }

        // TODO: Log to crashLibrary
    }
}
