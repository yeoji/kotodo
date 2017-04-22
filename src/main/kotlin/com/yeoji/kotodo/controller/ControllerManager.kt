package com.yeoji.kotodo.controller

import tornadofx.Controller
import kotlin.reflect.KClass

/**
 * This class manages the controllers in Kotodo
 *
 * Created by jq on 22/04/2017.
 */

class ControllerManager private constructor() {

    /**
     * List of the controllers in the app
     */
    val controllers: MutableList<Controller> = ArrayList()

    companion object Singleton {
        /**
         * The singleton instance of the ControllerManager
         */
        private var controllerManager: ControllerManager? = null

        /**
         * Return the instance of ControllerManager
         */
        fun getInstance(): ControllerManager {
            if (controllerManager == null) {
                controllerManager = ControllerManager()
            }

            return controllerManager as ControllerManager
        }
    }

    /**
     * Register a new controller if it doesn't already exist
     */
    fun registerController(controller: Controller) {
        // only register the controller if it's not already registered
        if (retrieveController(controller::class) == null) {
            controllers.add(controller)
        }
    }

    /**
     * Retrieve a controller by class name
     */
    fun retrieveController(controllerClass: KClass<out Controller>): Controller? {
        val controller: Controller? = controllers.find {
            it::class == controllerClass
        }

        return controller
    }

}
