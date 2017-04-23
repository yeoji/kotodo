package com.yeoji.kotodo.model

import tornadofx.ItemViewModel
import tornadofx.getProperty
import tornadofx.observable
import tornadofx.property

/**
 * This is the enum to denote TodoPriority
 */
enum class TodoPriority {
    LOW, MEDIUM, HIGH
}

/**
 * This is the Todo data class
 * It contains information about a todo
 */
class Todo(val id: Int = 0, description: String = "", completed: Boolean = false, priority: TodoPriority = TodoPriority.LOW) {
    var description: String by property(description)
    fun descriptionProperty() = getProperty(Todo::description)

    var completed: Boolean by property(completed)
    fun completedProperty() = getProperty(Todo::completed)

    var priority: TodoPriority by property(priority)
    fun priorityProperty() = getProperty(Todo::priority)
}

/**
 * The Todo model used for the View
 */
class TodoModel : ItemViewModel<Todo>() {
    val description = bind { item?.observable(Todo::description) }
    val completed = bind { item?.observable(Todo::completed) }
    val priority = bind { item?.observable(Todo::priority) }
}