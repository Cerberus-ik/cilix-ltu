package de.cilix.ltu.common

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import no.stelar7.api.l4j8.impl.builders.spectator.SpectatorBuilder
import no.stelar7.api.l4j8.pojo.summoner.Summoner
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

class GameFuture(private val summoner: Summoner) : Future<GameFound> {

    private var done = false
    private var cancelled = false

    override fun isDone(): Boolean {
        return this.done
    }

    override fun get(): GameFound? {
        val spectatorBuilder = SpectatorBuilder().withPlatform(summoner.platform)
        val currentGame = spectatorBuilder.withSummonerId(summoner.summonerId).currentGame
        return if(currentGame == null)
            null
        else
            GameFound(currentGame, summoner)
    }

    override fun get(timeout: Long, unit: TimeUnit?): GameFound? {
        var gameFound: GameFound? = null
        GlobalScope.launch {
            withTimeout(unit!!.toMillis(timeout)){
                gameFound = get()
            }
        }
        return gameFound
    }

    override fun cancel(mayInterruptIfRunning: Boolean): Boolean {
        this.cancelled = true
        return this.isCancelled
    }

    override fun isCancelled(): Boolean {
        return this.cancelled
    }
}