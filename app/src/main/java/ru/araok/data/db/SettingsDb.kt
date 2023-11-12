package ru.araok.data.db

import androidx.room.Entity
import ru.araok.data.dto.MarkDto
import ru.araok.entites.Settings

@Entity(tableName = "setting")
data class SettingsDb(
    override val id: Int? = null
): Settings