package com.castor.bookrecorder.core.data

import com.castor.bookrecorder.core.data.local.dao.BookDao
import com.castor.bookrecorder.core.data.local.entity.BookEntity
import com.castor.bookrecorder.core.data.remote.dto.BookDto
import com.castor.bookrecorder.core.data.remote.service.book.BookService
import com.castor.bookrecorder.core.data.remote.service.user.UserService
import com.castor.bookrecorder.core.domain.model.Character
import com.castor.bookrecorder.core.domain.repository.SyncRepository
import com.castor.bookrecorder.core.domain.repository.mappers.toBookEntity
import com.castor.bookrecorder.core.domain.repository.mappers.toCharacter
import javax.inject.Inject

class SyncRepositoryImpl @Inject constructor(
    private val bookDao: BookDao,
    private val userService: UserService,
    private val bookService: BookService
): SyncRepository {
    override suspend fun syncData() {
        val userId = userService.getCurrentUserId()
        val listBookEntity: MutableList<BookEntity> = mutableListOf()
        if (userId != null){
            try {
                val documents = bookService.getBooksByUserID(userId)
                val listCharacter = mutableListOf<Character>()
                for (document in documents) {
                    val bookDto = document.toObject(BookDto::class.java)
                    bookDto.id = document.id
                    bookDto.isFinished = document.getBoolean("isFinished") ?: false
                    listBookEntity.add(bookDto.toBookEntity())
                    for (character in bookDto.characters) {
                        listCharacter.add(character.toCharacter())
                    }
                }
                bookDao.cleanAndUpsertBook(listBookEntity)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }
}