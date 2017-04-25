package com.yeoji.kotodo

import com.yeoji.kotodo.controller.ControllerManager
import com.yeoji.kotodo.controller.timer.TimerController
import com.yeoji.kotodo.data.DataHandlerInterface
import com.yeoji.kotodo.data.properties.DataHandlerConstants
import com.yeoji.kotodo.data.properties.DataHandlerProperties
import com.yeoji.kotodo.controller.todos.TodosController
import com.yeoji.kotodo.model.Todo
import com.yeoji.kotodo.view.KotodoGUI
import tornadofx.App

/**
 * This is the main application
 * It is the entry point for the view
 *
 * Created by jq on 10/04/2017.
 */
class KotodoApp : App(KotodoGUI::class) {

    /**
     * The Data Handler instance
     */
    lateinit var dataHandler: DataHandlerInterface

    init {
        initControllers()
        initDataHandler()
    }

    /**
     * Initialize our controllers and register them with the ControllerManager
     */
    private fun initControllers() {
        // register controllers
        ControllerManager.getInstance().registerController(TodosController())
        ControllerManager.getInstance().registerController(TimerController())
    }

    /**
     * Initialize the data handler implementation
     * And populate the todos controller with existing data
     */
    private fun initDataHandler() {
        // instantiate the data handler to be used on startup
        val impl: String = DataHandlerProperties.getInstance().getHandlerProperty(DataHandlerConstants.COMMON_MODULE_PREFIX, "Implementation")
        this.dataHandler = Class.forName(impl).newInstance() as DataHandlerInterface

        // load todos from the data handler and populate the controller
        val todosController = ControllerManager.getInstance().retrieveController(TodosController::class) as TodosController
        val todos: List<Todo> = dataHandler.loadData()
        for (todo in todos) {
            todosController.addTodo(todo)
        }
    }

    override fun stop() {
        super.stop()
        saveData()
    }

    /**
     * Saves all todos using the initialized data handler
     */
    private fun saveData() {
        val todosController = ControllerManager.getInstance().retrieveController(TodosController::class) as TodosController
        val todos: List<Todo> = todosController.getAllTodos()
        this.dataHandler.saveData(todos)
    }
}