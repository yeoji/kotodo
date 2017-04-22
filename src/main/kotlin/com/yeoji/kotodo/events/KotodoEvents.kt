package com.yeoji.kotodo.events

import com.yeoji.kotodo.controller.todos.Todo
import tornadofx.FXEvent

/**
 * This file contains all the events that
 * Can be fired/listened to in Kotodo
 *
 * Created by jq on 13/04/2017.
 */


// This event is fired when a todo is added
class TodoAddedEvent(val todo: Todo) : FXEvent()

// This event is fired when a todo is removed
class TodoRemovedEvent(val todo: Todo?) : FXEvent()

// This event is fired when a todo is completed
class TodoCompletedEvent(val todo: Todo) : FXEvent()