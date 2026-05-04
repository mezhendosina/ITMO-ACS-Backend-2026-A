package com.mezhendosina.routes

import com.mezhendosina.database.Users
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

data class UserPrincipal(val userId: Int, val role: String) : Principal

fun ApplicationCall.principalUserId(): Int? {
    val principal = this.principal<JWTPrincipal>() ?: return null
    return principal.payload.getClaim("userId")?.asInt()
}

fun ApplicationCall.principalUserRole(): String? {
    val userId = principalUserId() ?: return null
    return transaction {
        Users.select(Users.id eq userId).singleOrNull()?.get(Users.role)
    }
}
