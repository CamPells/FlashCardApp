package cpe81.flashcard.app.datastore

import cpe81.flashcard.app.models.Identifiable
import kotlinx.coroutines.flow.Flow

interface Storage<T> where T : Identifiable {
    fun insert(data: T): Flow<Int>
    fun insertAll(data: List<T>): Flow<Int>
    fun getAll(): Flow<List<T>>
    fun delete(identifier: Int): Flow<Int>
    fun edit(identifier: Int, data: T): Flow<Int>
    fun get(where: (T) -> Boolean): Flow<T>
}