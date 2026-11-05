package com.Tonkapa7.Piwpul.domain.usecase

import com.Tonkapa7.Piwpul.core.config.GameConfig
import com.Tonkapa7.Piwpul.data.sample.SampleData
import com.Tonkapa7.Piwpul.domain.model.Catch
import com.Tonkapa7.Piwpul.domain.model.ChallengeKind
import com.Tonkapa7.Piwpul.domain.model.DailyChallenge
import com.Tonkapa7.Piwpul.domain.model.Rarity
import com.Tonkapa7.Piwpul.domain.repository.AnglerRepository

class UpdateDailyChallengeUseCase(private val repository: AnglerRepository) {

    fun ensureToday(dayIndex: Long): DailyChallenge {
        val current = repository.daily.value
        if (current.dayIndex == dayIndex) return current
        val gap = dayIndex - current.dayIndex
        val keptStreak = if (gap == 1L && current.complete) current.streak else 0
        val rolled = DailyChallenge(
            dayIndex = dayIndex,
            template = SampleData.templateForDay(dayIndex),
            progress = 0,
            claimed = false,
            streak = keptStreak,
            weekDone = shiftWeek(current.weekDone, gap)
        )
        repository.saveDaily(rolled)
        return rolled
    }

    fun applySession(dayIndex: Long, catches: List<Catch>, points: Int): DailyChallenge {
        val current = ensureToday(dayIndex)
        if (current.complete) return current
        val gained = when (current.template.kind) {
            ChallengeKind.CATCH_SPECIES -> catches.count { it.speciesId == current.template.speciesId }
            ChallengeKind.SCORE_POINTS -> if (points >= current.template.target) current.template.target else 0
            ChallengeKind.CATCH_RARE -> catches.count { it.rarity == Rarity.RARE || it.rarity == Rarity.LEGENDARY }
            ChallengeKind.TOTAL_CATCHES -> catches.size
        }
        if (gained == 0) return current
        val progress = (current.progress + gained).coerceAtMost(current.template.target)
        val complete = progress >= current.template.target
        val week = current.weekDone.toMutableList()
        if (complete) week[week.lastIndex] = true
        val updated = current.copy(
            progress = progress,
            streak = if (complete) current.streak + 1 else current.streak,
            weekDone = week
        )
        repository.saveDaily(updated)
        return updated
    }

    fun claim(dayIndex: Long): Int {
        val current = ensureToday(dayIndex)
        if (!current.complete || current.claimed) return 0
        repository.saveDaily(current.copy(claimed = true))
        return GameConfig.DAILY_REWARD_POINTS
    }

    private fun shiftWeek(week: List<Boolean>, gap: Long): List<Boolean> {
        if (gap <= 0L || gap >= 7L || week.size != 7) return List(7) { false }
        val step = gap.toInt()
        val shifted = MutableList(7) { false }
        for (index in 0 until 7) {
            val source = index + step
            if (source < 7) shifted[index] = week[source]
        }
        return shifted
    }
}
