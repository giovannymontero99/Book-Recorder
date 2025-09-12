package com.castor.bookrecorder.core.domain.repository.mappers

import com.castor.bookrecorder.core.data.local.entity.CharacterEntity
import com.castor.bookrecorder.core.data.remote.dto.CharacterDto
import com.castor.bookrecorder.core.domain.model.Character

fun CharacterEntity.toModel(): Character {
    return Character(
        id = this.id,
        bookId = this.bookId,
        name = this.name,
        description = this.description,
        firstAppearancePage = this.firstAppearancePage,
    )
}

fun Character.toEntity(): CharacterEntity {
    return CharacterEntity(
        id = this.id,
        bookId = this.bookId,
        name = this.name,
        description = this.description,
        firstAppearancePage = this.firstAppearancePage,
    )
}

fun CharacterDto.toCharacter() = Character(
    id = this.id,
    bookId = this.bookId,
    name = this.name,
    description = this.description,
    firstAppearancePage = this.firstAppearancePage,
)

fun CharacterDto.toEntity() = CharacterEntity(
    id = this.id,
    bookId = this.bookId,
    name = this.name,
    description = this.description,
    firstAppearancePage = this.firstAppearancePage,
)