package com.yeoji.kotodo.data

import com.yeoji.kotodo.model.Todo

/**
 * This is an interface used for any data saving/loading in Kotodo
 * Created by jq on 20/04/2017.
 */

interface DataHandlerInterface {

    /**
     * Save the list of todos
     */
    fun saveData(todos: List<Todo>)

    /**
     * Load a list of todos from saved data
     */
    fun loadData(): List<Todo>

}