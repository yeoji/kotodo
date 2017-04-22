package com.yeoji.kotodo.view

import com.yeoji.kotodo.view.todos.TodoFormView
import com.yeoji.kotodo.view.todos.TodoListView
import tornadofx.*

/**
 * This class is the main view for Kotodo
 * It displays the list of todos and is the
 * entry point for adding, removing, editing todos
 *
 * Created by jq on 9/04/2017.
 */

class KotodoGUI : View("Kotodo") {

    /**
     * The todo list UI
     */
    val todoListView: TodoListView by inject()

    /**
     * Define the Kotodo UI
     */
    override val root = borderpane {
        center {
            this += todoListView.root
        }

        right {
            // Add the TodoFormView to the right pane and pass the todoModel as a param
            this += find<TodoFormView>(mapOf(TodoFormView::todoModel to todoListView.todoModel))
        }
    }
}