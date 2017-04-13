package com.yeoji.kotodo.view

import com.yeoji.kotodo.events.TodoAddedEvent
import com.yeoji.kotodo.events.TodoRemovedEvent
import com.yeoji.kotodo.todos.Todo
import com.yeoji.kotodo.todos.TodosController
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.scene.control.TextField
import tornadofx.*

/**
 * This is the main application
 * It is the entry point for the view
 *
 * Created by jq on 10/04/2017.
 */
class KotodoApp : App(KotodoGUI::class)

/**
 * This class is the main view for Kotodo
 * It displays the list of todos and is the
 * entry point for adding, removing, editing todos
 *
 * Created by jq on 9/04/2017.
 */

class KotodoGUI : View("Kotodo") {

    /**
     * The Todos controller that manages the todos in the app
     */
    val todosController: TodosController by inject()

    /**
     * The field that allows users to add a todo
     */
    var addTodoField: TextField by singleAssign()

    /**
     * Add a new todo to the application
     */
    val addTodo: EventHandler<ActionEvent>? = EventHandler {
        val desc: String = addTodoField.text
        if (desc.length > 0) {
            todosController.addTodo(desc)
            addTodoField.clear()
        }
    }

    /**
     * Remove a todo from the application
     */
    val removeTodo: EventHandler<ActionEvent>? = EventHandler { event ->
        val buttonId: String = (event.source as Button).id
        todosController.removeTodo(Integer.parseInt(buttonId))
    }

    /**
     * Define the Kotodo UI
     */
    override val root = vbox {
        label("Kotodo")
        hbox {
            textfield() {
                addTodoField = this
                onAction = addTodo
            }
            button("+").onAction = addTodo
        }
    }

    init {
        with(root) {
            this += createTodoList()
        }
    }

    /**
     * Create the list that will show all todos
     */
    private fun createTodoList(): ListView<Todo> {
        return listview {
            listOf(todosController.getAllTodos())

            // customize the list cell to show todo info
            cellCache {
                hbox {
                    button("-") {
                        id = Integer.toString(it.id)
                        onAction = removeTodo
                    }
                    label(it.description)
                }
            }

            // subscribe to the todo events so UI can update
            subscribe<TodoAddedEvent> { event ->
                items.add(event.todo)
            }
            subscribe<TodoRemovedEvent> { event ->
                if (event.todo != null) {
                    items.remove(event.todo)
                }
            }
        }
    }
}