@file:SharedCode
@file:UseContextualSerialization(Instant::class, UUID::class, ServerFile::class)

package com.lightningkite.template

import com.lightningkite.khrysalis.SharedCode
import com.lightningkite.lightningdb.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseContextualSerialization
import java.time.Instant
import java.util.*

@Serializable
@DatabaseModel
data class User(
    override val _id: UUID = UUID.randomUUID(),
    override val email: String,
    val isSuperUser: Boolean = false,
    @Index val subscriptionId: String? = null,
    @Index val customerId: String? = null,
) : HasId<UUID>, HasEmail
