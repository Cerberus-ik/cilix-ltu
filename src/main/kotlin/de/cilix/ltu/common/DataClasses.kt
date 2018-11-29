package de.cilix.ltu.common

import no.stelar7.api.l4j8.pojo.match.Match
import no.stelar7.api.l4j8.pojo.spectator.SpectatorGameInfo
import no.stelar7.api.l4j8.pojo.summoner.Summoner

data class GameFound(val currentGameInfo: SpectatorGameInfo, val summoner: Summoner)