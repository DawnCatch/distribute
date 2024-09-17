package com.zhou03.distribute.dao

import org.ktorm.database.Database
import org.ktorm.entity.*
import org.ktorm.schema.ColumnDeclaring
import org.ktorm.schema.Table
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
abstract class BaseDao<E : Entity<E>, T : Table<E>>(private val tableObject: T) {
    @Autowired
    lateinit var database: Database

    open fun updateUnion(entity: E): Int {
        return 0
    }

    open fun add(entity: E): Int {
        return database.sequenceOf(tableObject).add(entity)
    }

    open fun addOrUpdate(entity: E): Int {
        return try {
            add(entity)
        } catch (e: Exception) {
            updateUnion(entity)
        }
    }

    open fun batchAdd(entities: List<E>) {
        for (entity in entities) add(entity)
    }

    open fun batchAddOrUpdate(entities: List<E>) {
        for (entity in entities) addOrUpdate(entity)
    }

    open fun update(entity: E): Int {
        return database.sequenceOf(tableObject).update(entity)
    }

    open fun deleteIf(predicate: (T) -> ColumnDeclaring<Boolean>): Int {
        return database.sequenceOf(tableObject).removeIf(predicate)
    }

    open fun allMatched(predicate: (T) -> ColumnDeclaring<Boolean>): Boolean {
        return database.sequenceOf(tableObject).all(predicate)
    }

    open fun anyMatched(predicate: (T) -> ColumnDeclaring<Boolean>): Boolean {
        return database.sequenceOf(tableObject).any(predicate)
    }

    open fun noneMatched(predicate: (T) -> ColumnDeclaring<Boolean>): Boolean {
        return database.sequenceOf(tableObject).none(predicate)
    }

    open fun count(): Int {
        return database.sequenceOf(tableObject).count()
    }

    open fun count(predicate: (T) -> ColumnDeclaring<Boolean>): Int {
        return database.sequenceOf(tableObject).count(predicate)
    }

    open fun findOne(predicate: (T) -> ColumnDeclaring<Boolean>): E? {
        return database.sequenceOf(tableObject).find(predicate)
    }

    open fun findList(predicate: (T) -> ColumnDeclaring<Boolean>): List<E> {
        return database.sequenceOf(tableObject).filter(predicate).toList()
    }

    open fun findEntitySequence(
        predicate: (T) -> ColumnDeclaring<Boolean>,
    ): EntitySequence<E, T> {
        return database.sequenceOf(tableObject).filter(predicate)
    }

    open fun findAll(): List<E> {
        return database.sequenceOf(tableObject).toList()
    }
}