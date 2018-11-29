package de.cilix.ltu.events

import de.cilix.ltu.common.GameFound

interface LTUListener{
    fun onGameFound(gameFound: GameFound)
}