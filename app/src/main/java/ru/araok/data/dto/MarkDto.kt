package ru.araok.data.dto

import ru.araok.entites.Mark

data class MarkDto(
    override val id: Int? = null,
    override val start: Int? = null,
    override val end: Int? = null,
    override val repeat: Int? = null,
    override val delay: Int? = null
): Mark