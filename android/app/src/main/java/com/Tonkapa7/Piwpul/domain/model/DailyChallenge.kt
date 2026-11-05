package com.Tonkapa7.Piwpul.domain.model

data class DailyChallenge(
    val dayIndex: Long,
    val template: ChallengeTemplate,
    val progress: Int,
    val claimed: Boolean,
    val streak: Int,
    val weekDone: List<Boolean>
) {
    val complete: Boolean
        get() = progress >= template.target
}
