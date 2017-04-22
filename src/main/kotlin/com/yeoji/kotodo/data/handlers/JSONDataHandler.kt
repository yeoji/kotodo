package com.yeoji.kotodo.data.handlers

import com.github.salomonbrys.kotson.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.stream.JsonReader
import com.yeoji.kotodo.controller.todos.Todo
import com.yeoji.kotodo.data.DataHandlerInterface
import com.yeoji.kotodo.data.properties.DataHandlerProperties
import com.yeoji.kotodo.util.ResourcesUtil
import java.io.EOFException
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.util.logging.Level
import java.util.logging.Logger

/**
 * This class implements DataHandlerInterface
 * It provides the ability to save and load todos
 * into a local JSON file.
 *
 * Created by jq on 20/04/2017.
 */

class JSONDataHandler : DataHandlerInterface {

    /**
     * The JSONDataHandler module prefix used in the properties
     */
    val JSON_MODULE_PREFIX: String = "JSON"

    /**
     * Logger of JSONDataHandler
     */
    val logger: Logger = Logger.getLogger(JSONDataHandler::class.toString())

    /**
     * The location of the JSON file to save/load from
     */
    var filePath: String

    init {
        val relativePath: String = DataHandlerProperties.getInstance().getHandlerProperty(JSON_MODULE_PREFIX, "FilePath")
        this.filePath = ResourcesUtil.getFilePathFromResources(relativePath)

        // create the file if it doesn't exist
        if (filePath.isEmpty()) {
            this.filePath = ResourcesUtil.createFile(relativePath)
        }
    }

    /**
     * Saves the list of todos provided in the param
     * into a local JSON file.
     */
    override fun saveData(todos: List<Todo>) {
        val gson: Gson = GsonBuilder().setPrettyPrinting().create()
        val jsonTodos: JsonArray = convertTodosToJson(todos)

        // write JSON to file
        val fileWriter: FileWriter = FileWriter(File(filePath), false)
        gson.toJson(jsonTodos, fileWriter)
        fileWriter.close()
    }

    /**
     * Loads a list of todos from a local JSON file
     */
    override fun loadData(): List<Todo> {
        val gson: Gson = GsonBuilder().setPrettyPrinting().create()
        val fileReader: FileReader = FileReader(File(filePath))
        val jsonReader: JsonReader = gson.newJsonReader(fileReader)

        // load todos data from JSON file
        val todos: List<Todo> = readJsonData(jsonReader)
        jsonReader.close()

        return todos
    }

    /**
     * Read the list of Todos from the JSON file
     */
    private fun readJsonData(jsonReader: JsonReader): List<Todo> {
        val todos = arrayListOf<Todo>()

        try {
            jsonReader.beginArray()
            while (jsonReader.hasNext()) {
                // read todo from JSON
                jsonReader.beginObject()

                jsonReader.nextName()
                val id = jsonReader.nextInt()
                jsonReader.nextName()
                val description = jsonReader.nextString()
                jsonReader.nextName()
                val completed = jsonReader.nextBoolean()

                jsonReader.endObject()

                todos.add(Todo(id, description, completed))
            }
            jsonReader.endArray()
        } catch(e: EOFException) {
            // file was empty, log warning and proceed
            logger.log(Level.INFO, "Todos file was empty: Initializing with 0 todos!")
        }

        return todos
    }

    /**
     * Converts a list of todos into a JsonArray object
     */
    private fun convertTodosToJson(todos: List<Todo>): JsonArray {
        var jsonList = arrayListOf<JsonObject>()

        // create a json object for each todo
        jsonList = todos.mapTo(jsonList) {
            jsonObject(
                    "id" to it.id,
                    "description" to it.description,
                    "completed" to it.completed
            )
        }

        return jsonArray(jsonList)
    }

}
