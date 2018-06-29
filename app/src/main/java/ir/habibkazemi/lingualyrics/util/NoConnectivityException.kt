package ir.habibkazemi.lingualyrics.util

import java.io.IOException

/**
 * Created by habibkazemi on 5/21/18.
 */

class NoConnectivityException : IOException() {

    override val message: String?
        get() = "No connectivity exception"
}
