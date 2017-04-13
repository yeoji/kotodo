package com.yeoji.kotodo.view

import com.yeoji.kotodo.events.TodoAddedEvent
import com.yeoji.kotodo.todos.Todo
import com.yeoji.kotodo.todos.TodosController
import javafx.event.ActionEvent
import javafx.event.EventHandler
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
        }
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

        listview<Todo> {
            listOf(todosController.getAllTodos())

            subscribe<TodoAddedEvent> { event ->
                items.add(event.todo)
            }
        }
    }

}