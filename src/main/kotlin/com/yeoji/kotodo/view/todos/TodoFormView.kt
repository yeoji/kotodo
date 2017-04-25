package com.yeoji.kotodo.view.todos

import com.yeoji.kotodo.controller.ControllerManager
import com.yeoji.kotodo.controller.timer.TimerController
import com.yeoji.kotodo.controller.todos.TodosController
import com.yeoji.kotodo.events.TodoCompletedEvent
import com.yeoji.kotodo.events.TodoSelectedEvent
import com.yeoji.kotodo.model.Todo
import com.yeoji.kotodo.model.TodoModel
import com.yeoji.kotodo.model.TodoPriority
import javafx.application.Platform
import javafx.collections.FXCollections.observableArrayList
import javafx.scene.control.Alert
import javafx.scene.control.Label
import tornadofx.*
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * The constant value that holds one second in MS
 */
const val ONE_SECOND_IN_MS: Long = 1000

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
     * The Timer controller that manages the time tracking of todos
     */
    val timerController = ControllerManager.getInstance().retrieveController(TimerController::class) as TimerController

    /**
     * The Todo ViewModel
     */
    val todoModel: TodoModel by param()

    /**
     * The label that shows the time taken for a Todo
     */
    var timeTakenLabel: Label by singleAssign()

    /**
     * The Timer used to schedule updates to the elapsed time
     */
    val timer: Timer = Timer()

    /**
     * The timer task used to update the time taken label with time elapsed
     */
    lateinit var timerTask: TimerTask

    /**
     * The time elapsed since the timer has started
     */
    var timeElapsed: Long = 0

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
            field("Time Taken") {
                label("") {
                    timeTakenLabel = this

                    dynamicContent(todoModel.timeTaken, { time ->
                        this.text = formatTime(time!!)
                    })
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
            button(getTimerButtonText()) {
                setOnAction {
                    if (todoModel.isNotEmpty) {
                        handleTimer(todoModel.item)
                        this.text = getTimerButtonText()
                    }
                }

                subscribe<TodoSelectedEvent> {
                    text = getTimerButtonText()
                }
                subscribe<TodoCompletedEvent> { event ->
                    if (event.todo.completed) {
                        handleTimer(event.todo)
                        text = getTimerButtonText()
                    }
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

    /**
     * Starts the timer on the selected Todo if it's not already started
     * Or stops it if the timer is started
     */
    private fun handleTimer(todo: Todo) {
        if (timerController.isTimerStarted(todo)) {
            timerController.stopTimer()

            // cancel the timer task
            timerTask.cancel()
            timer.purge()
            timeElapsed = 0
        } else {
            timerController.startTimer(todo)

            // create a new timer task
            timerTask = object : TimerTask() {
                override fun run() {
                    Platform.runLater {
                        if(todo.id == todoModel.item.id) {
                            val totalTime: Long = todo.timeTaken + timeElapsed
                            timeTakenLabel.text = formatTime(totalTime)
                        }
                        timeElapsed += ONE_SECOND_IN_MS
                    }
                }
            }
            timer.schedule(timerTask, 0, ONE_SECOND_IN_MS)
        }
    }

    /**
     * Returns the text to show on the timer button
     * If the timer is started on the selected todo, returns "Stop"
     * Otherwise, returns "Start"
     */
    private fun getTimerButtonText(): String {
        if (todoModel.isNotEmpty) {
            val selectedTodo: Todo = todoModel.item
            return if (timerController.isTimerStarted(selectedTodo)) "Stop" else "Start"
        }
        return "Start"
    }

    /**
     * Formats the time in Milliseconds to hh:mm:ss format
     */
    private fun formatTime(timeMs: Long): String {
        val hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(timeMs),
                TimeUnit.MILLISECONDS.toMinutes(timeMs) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(timeMs) % TimeUnit.MINUTES.toSeconds(1))
        return hms
    }
}
