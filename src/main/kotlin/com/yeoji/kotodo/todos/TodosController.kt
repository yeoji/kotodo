package com.yeoji.kotodo.todos

import com.yeoji.kotodo.events.TodoAddedEvent
import com.yeoji.kotodo.events.TodoRemovedEvent
import tornadofx.Controller
import tornadofx.ViewModel
import tornadofx.getProperty
import tornadofx.property
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
class Todo(val id: Int = 0, description: String = "") {
    var description by property(description)
    fun descriptionProperty() = getProperty(Todo::description)
}

/**
 * The Todo model used for the View
 */
class TodoModel(var todo: Todo) : ViewModel() {
    val description = bind { todo.descriptionProperty() }
}