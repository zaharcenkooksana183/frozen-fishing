package com.Tonkapa7.Piwpul.core.di

import android.content.Context
import com.Tonkapa7.Piwpul.data.local.PreferencesStore
import com.Tonkapa7.Piwpul.data.repository.AnglerRepositoryImpl
import com.Tonkapa7.Piwpul.data.repository.SettingsRepositoryImpl
import com.Tonkapa7.Piwpul.domain.repository.AnglerRepository
import com.Tonkapa7.Piwpul.domain.repository.SettingsRepository
import com.Tonkapa7.Piwpul.domain.usecase.RollCatchUseCase
import com.Tonkapa7.Piwpul.domain.usecase.SaveSessionUseCase
import com.Tonkapa7.Piwpul.domain.usecase.UnlockGearUseCase
import com.Tonkapa7.Piwpul.domain.usecase.UpdateDailyChallengeUseCase

object ServiceLocator {

    private var store: PreferencesStore? = null

    lateinit var anglerRepository: AnglerRepository
        private set

    lateinit var settingsRepository: SettingsRepository
        private set

    lateinit var rollCatch: RollCatchUseCase
        private set

    lateinit var saveSession: SaveSessionUseCase
        private set

    lateinit var unlockGear: UnlockGearUseCase
        private set

    lateinit var updateDailyChallenge: UpdateDailyChallengeUseCase
        private set

    fun init(context: Context) {
        if (store != null) return
        val created = PreferencesStore(context)
        store = created
        anglerRepository = AnglerRepositoryImpl(created)
        settingsRepository = SettingsRepositoryImpl(created)
        rollCatch = RollCatchUseCase()
        updateDailyChallenge = UpdateDailyChallengeUseCase(anglerRepository)
        saveSession = SaveSessionUseCase(anglerRepository, updateDailyChallenge)
        unlockGear = UnlockGearUseCase(anglerRepository)
    }
}
