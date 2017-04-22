package com.yeoji.kotodo.view.todos

import com.yeoji.kotodo.controller.ControllerManager
import com.yeoji.kotodo.controller.todos.TodoModel
import com.yeoji.kotodo.controller.todos.TodosController
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
        // Flush changes from the text fields into the model
        todoModel.commit()
        val todo = todoModel.item

        todosController.updateTodo(todo)
    }
}
