@file:SharedCode
@file:UseContextualSerialization(Instant::class, UUID::class, ServerFile::class, LocalDate::class)

package com.lightningkite.template

import com.lightningkite.khrysalis.SharedCode
import com.lightningkite.lightningdb.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseContextualSerialization
import java.time.Instant
import java.time.LocalDate
import java.util.*

@Serializable
@DatabaseModel
data class User(
    override val _id: UUID = UUID.randomUUID(),
    @Unique override val email: String,
    val termsAgreed: Instant = Instant.EPOCH,
    val isSuperUser: Boolean = false,
    @Index val subscriptionId: String? = null,
    @Index val customerId: String? = null,
) : HasId<UUID>, HasEmail

@Serializable
@DatabaseModel
data class FcmToken(
    override val _id: String,
    @References(User::class) val user: UUID
) : HasId<String>
