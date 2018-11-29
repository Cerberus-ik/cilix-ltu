package de.cilix.ltu.events

import de.cilix.ltu.common.GameFound

class LTUEventManager {

    private val listener = mutableListOf<LTUListener>()

    fun registerListener(listener: LTUListener){
        if (!this.listener.contains(listener))
            this.listener.add(listener)
    }

    fun removeListener(listener: LTUListener){
        if (this.listener.contains(listener))
            this.listener.add(listener)
    }

    fun callGameFound(gameFound: GameFound){
        this.listener.forEach { it.onGameFound(gameFound) }
    }
}