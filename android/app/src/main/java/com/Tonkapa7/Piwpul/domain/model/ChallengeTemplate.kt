package com.Tonkapa7.Piwpul.domain.model

data class ChallengeTemplate(
    val id: Int,
    val kind: ChallengeKind,
    val target: Int,
    val speciesId: Int
)
