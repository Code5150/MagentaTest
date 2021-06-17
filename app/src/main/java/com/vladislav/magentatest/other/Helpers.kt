package com.vladislav.magentatest.other

import com.vladislav.magentatest.ui.activities.MainActivity
import java.io.File

object Helpers {
    inline fun getImage(dir: File, id: Int): File =
        File(dir, "$id${MainActivity.JPG}")
}