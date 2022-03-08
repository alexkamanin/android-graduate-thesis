package ru.kamanin.nstu.graduate.thesis.shared.session.domain.repository

import ru.kamanin.nstu.graduate.thesis.shared.session.domain.entity.Session

interface LocalSessionRepository {

	suspend fun save(session: Session)

	fun get(): Session

	suspend fun clear()
}