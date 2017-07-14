package de.daschubbm.alkchievements20.control

object Events {
    private val triggers: MutableMap<String, MutableSet<(params: List<Any>) -> Unit>> = mutableMapOf()

    fun trigger(event: String) {
        triggers[event]?.forEach { it.invoke(emptyList()) }
    }

    fun trigger(event: String, params: List<Any>) {
        triggers[event]?.forEach { it.invoke(params) }
    }

    fun addHandler(event: String, handler: (params: List<Any>) -> Unit) {
        if (triggers[event] == null) triggers[event] = mutableSetOf(handler)
        else triggers[event]!!.add(handler)
    }

    fun addHandler(event: String, handler: () -> Unit) {
        if (triggers[event] == null) triggers[event] = mutableSetOf({ _ -> handler.invoke() })
        else triggers[event]!!.add { _ -> handler.invoke() }
    }
}