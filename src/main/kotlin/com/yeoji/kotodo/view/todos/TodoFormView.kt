package com.yeoji.kotodo.view.todos

import com.yeoji.kotodo.controller.ControllerManager
import com.yeoji.kotodo.controller.todos.TodosController
import com.yeoji.kotodo.model.TodoModel
import com.yeoji.kotodo.model.TodoPriority
import javafx.collections.FXCollections.observableArrayList
import javafx.scene.control.Alert
import tornadofx.*

/**
 * This class defines the UI for the Edit Todo form
 * Created by jq on 14/04/2017.
 */

class TodoFormView : View() {

    /**
     * The Todos controller that manages the todos in the app
     */
    val todosController = ControllerManager.getInstance().retrieveController(TodosController::class) as TodosController

    /**
     * The Todo ViewModel
     */
    val todoModel: TodoModel by param()

    override val root = form {
        fieldset("Edit Todo") {
            field("Description") {
                textfield(todoModel.description)
            }
            field("Priority") {
                combobox<TodoPriority>(todoModel.priority) {
                    items = observableArrayList(TodoPriority.values().toCollection(ArrayList<TodoPriority>()))
                }
            }
            button("Save") {
                disableProperty().bind(todoModel.dirtyStateProperty().not())
                setOnAction {
                    saveTodo()
                }
            }
            button("Reset") {
                setOnAction {
                    todoModel.rollback()
                }
            }
        }
    }

    /**
     * This function saves an updated todo
     */
    private fun saveTodo() {
        if (todoModel.isNotEmpty) {
            // Flush changes from the text fields into the model
            todoModel.commit()
            val todo = todoModel.item

            todosController.updateTodo(todo)
        } else {
            alert(Alert.AlertType.ERROR, "No Todo Selected!", "Please select a Todo before trying to edit.")
            todoModel.rollback()
        }
    }
}
