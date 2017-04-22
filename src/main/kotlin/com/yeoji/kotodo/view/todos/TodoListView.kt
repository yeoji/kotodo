package com.yeoji.kotodo.view.todos

import com.yeoji.kotodo.controller.ControllerManager
import com.yeoji.kotodo.events.TodoAddedEvent
import com.yeoji.kotodo.events.TodoCompletedEvent
import com.yeoji.kotodo.events.TodoRemovedEvent
import com.yeoji.kotodo.controller.todos.Todo
import com.yeoji.kotodo.controller.todos.TodoModel
import com.yeoji.kotodo.controller.todos.TodosController
import javafx.event.ActionEvent
import javafx.scene.control.Button
import javafx.scene.control.CheckBox
import javafx.scene.control.ListView
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import tornadofx.*

/**
 * This class defines the UI for the todo list
 * Created by jq on 14/04/2017.
 */

class TodoListView : View() {

    /**
     * The Todos controller that manages the todos in the app
     */
    val todosController = ControllerManager.getInstance().retrieveController(TodosController::class) as TodosController

    /**
     * The Todo ViewModel
     */
    val todoModel = TodoModel()

    /**
     * The field that allows users to add a todo
     */
    var addTodoField: TextField by singleAssign()

    override val root = vbox {
        hbox {
            textfield() {
                addTodoField = this
                setOnAction { addTodo(text) }
            }
            button("+") {
                setOnAction { addTodo(addTodoField.text) }
            }
        }

        squeezebox {
            fold("Todos", expanded = true) {
                this += createTodoList()
            }
            fold("Completed", expanded = true) {
                this += createCompletedTodoList()
            }
        }
    }

    /**
     * Create the list that will show all todos
     */
    private fun createTodoList(): ListView<Todo> {
        return listview {
            val todos: List<Todo> = todosController.getAllTodos().filter { !it.completed }
            for (todo in todos) {
                items.add(todo)
            }

            // customize the list cell to show todo info
            cellCache {
                createCustomListCell(it)
            }

            // Update the todo inside the view model on selection change
            bindSelected(todoModel)

            // subscribe to the todo events so UI can update
            subscribe<TodoAddedEvent> { event ->
                items.add(event.todo)
            }
            subscribe<TodoRemovedEvent> { event ->
                if (event.todo != null) {
                    items.remove(event.todo)
                }
            }
            subscribe<TodoCompletedEvent> { event ->
                val todo: Todo = event.todo
                if (!todo.completed) {
                    items.add(todo)
                } else {
                    items.remove(todo)
                }
            }
        }
    }

    /**
     * Add a new todo to the list
     */
    private fun addTodo(desc: String) {
        if (desc.length > 0) {
            todosController.addTodo(desc)
            addTodoField.clear()
        }
    }

    /**
     * Mark a todo as completed or not completed
     * Depending on status of the checkbox
     */
    private fun completeTodo(event: ActionEvent) {
        val checkbox: CheckBox = event.source as CheckBox
        val todo: Todo? = todosController.getTodoById(Integer.parseInt(checkbox.id))
        if (todo != null) {
            todo.completed = checkbox.isSelected
            fire(TodoCompletedEvent(todo))
        }
    }

    /**
     * Creates a list of the completed todos
     */
    private fun createCompletedTodoList(): ListView<Todo> {
        return listview {
            val todos: List<Todo> = todosController.getAllTodos().filter { it.completed }
            for (todo in todos) {
                items.add(todo)
            }

            // customize the list cell to show todo info
            cellCache {
                createCustomListCell(it)
            }

            // Update the todo inside the view model on selection change
            bindSelected(todoModel)

            // subscribe to the todo events so UI can update
            subscribe<TodoRemovedEvent> { event ->
                if (event.todo != null) {
                    items.remove(event.todo)
                }
            }
            subscribe<TodoCompletedEvent> { event ->
                val todo: Todo = event.todo
                if (todo.completed) {
                    items.add(todo)
                } else {
                    items.remove(todo)
                }
            }
        }
    }

    /**
     * Creates the custom list cell to show todo info
     */
    private fun createCustomListCell(todo: Todo): HBox {
        return hbox {
            checkbox {
                id = Integer.toString(todo.id)
                bind(todo.completedProperty())
                setOnAction { event -> completeTodo(event) }
            }
            label(todo.descriptionProperty())

            button("-") {
                id = Integer.toString(todo.id)
                setOnAction { event ->
                    val buttonId: String = (event.source as Button).id
                    todosController.removeTodo(Integer.parseInt(buttonId))
                }
            }
        }
    }

}
