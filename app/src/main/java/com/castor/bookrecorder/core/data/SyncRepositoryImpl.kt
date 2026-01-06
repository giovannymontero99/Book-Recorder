package com.castor.bookrecorder.core.data

import com.castor.bookrecorder.core.data.local.dao.BookDao
import com.castor.bookrecorder.core.data.local.dao.CharacterDao
import com.castor.bookrecorder.core.data.local.entity.BookEntity
import com.castor.bookrecorder.core.data.local.entity.CharacterEntity
import com.castor.bookrecorder.core.data.remote.dto.BookDto
import com.castor.bookrecorder.core.data.remote.service.book.BookService
import com.castor.bookrecorder.core.data.remote.service.user.UserService
import com.castor.bookrecorder.core.domain.model.Character
import com.castor.bookrecorder.core.domain.repository.SyncRepository
import com.castor.bookrecorder.core.domain.repository.mappers.toBookEntity
import com.castor.bookrecorder.core.domain.repository.mappers.toCharacter
import com.castor.bookrecorder.core.domain.repository.mappers.toEntity
import javax.inject.Inject

class SyncRepositoryImpl @Inject constructor(
    private val bookDao: BookDao,
    private val userService: UserService,
    private val bookService: BookService,
    private val characterDao: CharacterDao,
): SyncRepository {
    override suspend fun syncData() {
        val userId = userService.getCurrentUserId()
        val listBookEntity: MutableList<BookEntity> = mutableListOf()
        if (userId != null){
            try {
                val documents = bookService.getBooksByUserID(userId)
                val listCharacter = mutableListOf<CharacterEntity>()
                for (document in documents) {
                    val bookDto = document.toObject(BookDto::class.java)
                    val bookEntity = BookEntity(
                        id = document.id,
                        title = bookDto.title,
                        author = bookDto.author,
                        genre = bookDto.genre,
                        startDate = bookDto.startDate,
                        finishDate = bookDto.finishDate,
                        progress = bookDto.progress,
                        totalPages = bookDto.totalPages,
                        notes = bookDto.notes,
                        summary = bookDto.summary,
                        quotes = bookDto.quotes,
                        coverImageUri = null,
                        isFinished = document.getBoolean("isFinished") ?: false,
                        userID = bookDto.userID,
                        isFavorite = document.getBoolean("isFavorite") ?: false
                    )
                    listBookEntity.add(bookEntity)
                    for (character in bookDto.characters) {
                        val newCharacter = CharacterEntity(
                            id = character.id,
                            bookId = document.id,
                            name = character.name,
                            description = character.description,
                            firstAppearancePage = character.firstAppearancePage,
                        )
                        listCharacter.add(newCharacter)
                    }
                }
                bookDao.cleanAndUpsertBook(listBookEntity)
                characterDao.cleanAndUpsertCharacter(listCharacter)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}