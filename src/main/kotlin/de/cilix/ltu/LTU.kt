package de.cilix.ltu

import de.cilix.ltu.common.GameFuture
import de.cilix.ltu.events.LTUEventManager
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import no.stelar7.api.l4j8.basic.APICredentials
import no.stelar7.api.l4j8.basic.cache.impl.MemoryCacheProvider
import no.stelar7.api.l4j8.basic.calling.DataCall
import no.stelar7.api.l4j8.basic.constants.api.Platform
import no.stelar7.api.l4j8.impl.L4J8
import no.stelar7.api.l4j8.impl.raw.SummonerAPI
import no.stelar7.api.l4j8.pojo.summoner.Summoner

class LTU(apiKey: String, private val checkInterval: Int) {

    private val players = mutableListOf<Summoner>()
    private val eventManager = LTUEventManager()
    private val games = mutableListOf<Long>()

    fun addPlayer(platform: Platform, playerName: String): Boolean {
        val summoner = SummonerAPI.getInstance().getSummonerByName(platform, playerName)
        if (summoner == null || this.players.contains(summoner))
            return false
        this.players.add(summoner)
        return true
    }

    private fun startTimer() {
        GlobalScope.launch {
            while (true) {
                players.forEach {
                    val gameFound = GameFuture(it).get()
                    if (gameFound != null) {
                        if(games.contains(gameFound.currentGameInfo.gameId))
                            return@launch
                        games.add(gameFound.currentGameInfo.gameId)
                        eventManager.callGameFound(gameFound)
                    }
                }
                delay(checkInterval.toLong())
            }
        }
    }

    init {
        DataCall.setCacheProvider(MemoryCacheProvider(Math.max(10, checkInterval).toLong()))
        L4J8(APICredentials(apiKey))
        this.startTimer()
    }
}