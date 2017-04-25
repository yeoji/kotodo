package com.yeoji.kotodo.controller.timer

import com.yeoji.kotodo.controller.ControllerManager
import com.yeoji.kotodo.controller.todos.TodosController
import com.yeoji.kotodo.model.Todo
import javafx.beans.property.SimpleLongProperty
import org.apache.commons.lang3.time.StopWatch
import tornadofx.Controller
import tornadofx.toProperty
import java.util.*

/**
 * This class manages the starting/stopping of the timer
 * For a todo
 *
 * Created by jq on 23/04/2017.
 */
class TimerController : Controller() {

    /**
     * The Todo that the timer is managing at the moment
     */
    var managedTodo: Todo? = null

    /**
     * The Stopwatch used to keep track of time
     */
    val stopwatch: StopWatch = StopWatch()

    /**
     * Start the timer for the todo that has been passed in
     *
     * A timer can only be started for one todo at a time
     * So if the timer was already managing another todo,
     * The timer for that todo will have to be stopped
     * in order to start a timer for the new todo
     */
    fun startTimer(todo: Todo) {
        if (managedTodo != null) {
            // stop the timer for the managed todo
            stopTimer()
        }

        // start timer for the todo in param
        managedTodo = todo
        stopwatch.start()
    }

    /**
     * Stop the timer for the currently managed Todo
     */
    fun stopTimer() {
        if (stopwatch.isStarted() && managedTodo != null) {
            stopwatch.stop()

            // update the managed Todo with the recorded time
            managedTodo!!.timeTaken += stopwatch.time
            val todosController = ControllerManager.getInstance().retrieveController(TodosController::class) as TodosController
            todosController.updateTodo(managedTodo!!)
        }

        stopwatch.reset()
        managedTodo = null
    }

    /**
     * Checks if the timer has started
     */
    fun isTimerStarted(todo: Todo): Boolean {
        return (stopwatch.isStarted && todo.id == managedTodo!!.id)
    }

}