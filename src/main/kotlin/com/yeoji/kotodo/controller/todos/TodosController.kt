package com.yeoji.kotodo.controller.todos

import com.yeoji.kotodo.events.TodoAddedEvent
import com.yeoji.kotodo.events.TodoRemovedEvent
import tornadofx.*
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
     * Used when a new todo is added via GUI
     */
    fun addTodo(todoDesc: String) {
        val todoId: Int = generateTodoId()
        val todo: Todo = Todo(todoId, todoDesc)
        this.todos.add(todo)

        fire(TodoAddedEvent(todo))
    }

    /**
     * This function adds a new todo to the list
     * Used when todos are loaded from a data handler
     */
    fun addTodo(todo: Todo) {
        this.todos.add(todo)

        fire(TodoAddedEvent(todo))
    }

    /**
     * This function removes a todo from the list
     */
    fun removeTodo(todoId: Int) {
        val todo: Todo? = getTodoById(todoId)
        // remove the todo
        this.todos.remove(todo)

        fire(TodoRemovedEvent(todo))
    }

    /**
     * This function updates a todo in the list
     */
    fun updateTodo(todo: Todo) {
        val oldTodo: Todo? = getTodoById(todo.id)
        if (oldTodo != null) {
            this.todos.remove(oldTodo)
            this.todos.add(todo)
        }
    }

    /**
     * Find and return a Todo by its ID
     */
    fun getTodoById(todoId: Int): Todo? {
        // find the todo by ID
        val todo: Todo? = this.todos.find {
            it.id == todoId
        }
        return todo
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
class Todo(val id: Int = 0, description: String = "", completed: Boolean = false) {
    var description: String by property(description)
    fun descriptionProperty() = getProperty(Todo::description)

    var completed: Boolean by property(completed)
    fun completedProperty() = getProperty(Todo::completed)
}

/**
 * The Todo model used for the View
 */
class TodoModel : ItemViewModel<Todo>() {
    val description = bind { item?.observable(Todo::description) }
    val completed = bind { item?.observable(Todo::completed) }
}