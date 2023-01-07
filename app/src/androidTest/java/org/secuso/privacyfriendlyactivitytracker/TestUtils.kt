package org.secuso.privacyfriendlyactivitytracker

import android.content.Context
import android.os.Environment
import androidx.test.core.app.ApplicationProvider
import java.io.File


class TestUtils {
    companion object {
        @JvmStatic
        fun readFileToString(file: File): String? {
            return file.readText()
        }

        @JvmStatic
        fun getResourceString(id: Int): String? {
            val targetContext: Context = ApplicationProvider.getApplicationContext()
            return targetContext.resources.getString(id)
        }

        @JvmStatic
        fun isExternalStorageAvailable(): Boolean {
            val extStorageState = Environment.getExternalStorageState()
            return if (Environment.MEDIA_MOUNTED == extStorageState) {
                true
            } else false
        }
    }
}