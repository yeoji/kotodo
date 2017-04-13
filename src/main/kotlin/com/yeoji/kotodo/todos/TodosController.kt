package com.yeoji.kotodo.todos

import com.yeoji.kotodo.events.TodoAddedEvent
import tornadofx.Controller
import java.util.*

/**
 * This class contains and manages the list of todos in the application.
 *
 * Created by jq on 9/04/2017.
 */

class TodosController : Controller() {

    /**
     * The list of todos in the application
     */
    var todos: MutableList<Todo> = ArrayList()

    /**
     * This returns the list of todos in the application
     */
    fun getAllTodos(): List<Todo> {
        return this.todos
    }

    /**
     * This function adds a new todo to the list
     */
    fun addTodo(todoDesc: String) {
        val todoId: Int = generateTodoId()
        val todo: Todo = Todo(todoId, todoDesc)
        this.todos.add(todo)

        fire(TodoAddedEvent(todo))
    }

    /**
     * This function removes a todo from the list
     */
    fun removeTodo(todo: Todo) {
        // remove the todo
        this.todos.remove(todo)
    }

    /**
     * This function updates a todo in the list
     */
    fun updateTodo(todo: Todo) {
        // find the todo by ID
        val oldTodo: Todo? = this.todos.find {
            it.id == todo.id
        }
        if (oldTodo != null) {
            this.todos.remove(oldTodo)
            this.todos.add(todo)
        }
    }

    /**
     * This function returns the next ID that can
     * be used by a new Todo.
     *
     * IDs are incremental, and if no todo was found,
     * it returns a 1.
     */
    private fun generateTodoId(): Int {
        val lastTodo: Todo? = this.todos.maxBy {
            it.id
        }
        // increment the ID
        if (lastTodo != null) {
            return lastTodo.id + 1
        }

        // default ID is 1 (for no todos found)
        return 1
    }

}

/**
 * This is the Todo data class
 * It contains information about a todo
 */
data class Todo(val id: Int, var description: String)