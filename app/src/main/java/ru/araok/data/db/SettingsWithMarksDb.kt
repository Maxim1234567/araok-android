package ru.araok.data.db

import androidx.room.Junction
import androidx.room.Relation
import ru.araok.entites.Mark
import ru.araok.entites.Settings
import ru.araok.entites.SettingsWithMarks

data class SettingsWithMarksDb(
    override val settingDb: Settings = SettingsDb(),
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            MarkDb::class,
            parentColumn = "setting_id",
            entityColumn = "mark_id"
        )
    )
    override val marksDb: List<Mark> = emptyList()
): SettingsWithMarks