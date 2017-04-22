package com.yeoji.kotodo.data.properties

import com.yeoji.kotodo.util.ResourcesUtil
import java.io.File
import java.io.FileReader
import java.util.*

/**
 * This object holds the constants used
 * with regards to DataHandlers
 */
object DataHandlerConstants {
    const val DATA_HANDLER_PROP_FILE: String = "config/data/DataHandlers.properties"

    const val DATA_HANDLER_PROP_PREFIX: String = "Kotodo.DataHandler."

    const val COMMON_MODULE_PREFIX: String = "Common"
}

/**
 * This class handles the loading of data handler properties
 * It is a singleton class that allows access to the loaded properties
 *
 * Created by jq on 20/04/2017.
 */

class DataHandlerProperties private constructor() : Properties() {

    companion object Singleton {
        /**
         * The singleton instance of the DataHandlerProperties
         */
        private var dataHandlerProps: DataHandlerProperties? = null

        /**
         * Return the instance of DataHandlerProperties
         */
        fun getInstance(): DataHandlerProperties {
            if (dataHandlerProps == null) {
                dataHandlerProps = DataHandlerProperties()
            }

            return dataHandlerProps as DataHandlerProperties
        }
    }

    init {
        val resourceFile: String = ResourcesUtil.getFilePathFromResources(DataHandlerConstants.DATA_HANDLER_PROP_FILE)
        val propsFile: File = File(resourceFile)
        load(FileReader(propsFile))
    }

    /**
     * All properties defined in DataHandlers.properties
     * follow the structure:
     *
     * Kotodo.DataHandler.<module>.<property>
     *
     * This takes in the module and the property as params
     * And returns the property specified or empty if not found
     */
    fun getHandlerProperty(module: String, property: String): String {
        val key: String = DataHandlerConstants.DATA_HANDLER_PROP_PREFIX + module + "." + property
        return getProperty(key, "")
    }
}