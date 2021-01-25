package ru.nsu.manasyan.treechat.util

open class Observable<V> {
    enum class Event {
        UPDATE,
        REMOVE
    }

    fun interface Observer<V> {
        fun onUpdate(event: Event, value: V)
    }

    private val observers = mutableListOf<Observer<V>>()

    fun notifyObservers(event: Event, newValue: V) {
        observers.forEach {
            it.onUpdate(event, newValue)
        }
    }

    fun observe(observer: Observer<V>) {
        observers.add(observer)
    }
}