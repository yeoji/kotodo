package com.yeoji.kotodo.util

import java.io.File
import java.net.URL

/**
 * This is a utility file that handles
 * anything to do with the resources folder
 *
 * Created by jq on 20/04/2017.
 */
object ResourcesUtil {
    /**
     * Returns the file path from the resources folder
     */
    fun getFilePathFromResources(path: String): String {
        val classLoader: ClassLoader = ClassLoader.getSystemClassLoader()
        val resourceUrl: URL? = classLoader.getResource(path)

        if(resourceUrl != null) {
            return resourceUrl.file
        }
        return ""
    }

    /**
     * Create a file in the resources folder
     * Returns the created file path
     */
    fun createFile(path: String): String {
        // use an existing path in the resources folder to get the resources path
        val existingPath: String = getFilePathFromResources("config")
        val resourcesPath: String = File(existingPath).parent

        // create the new file in the resources path
        val dirNames: String = path.substringBeforeLast(File.separator)
        val filePath: String = resourcesPath + File.separator + path

        File(resourcesPath + File.separator + dirNames).mkdirs()
        File(filePath).createNewFile()
        return filePath
    }
}