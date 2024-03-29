package com.lightningkite.template

import com.lightningkite.lightningserver.aws.AwsAdapter
import com.lightningkite.lightningserver.serialization.Serialization
import com.lightningkite.lightningserver.settings.Settings
import kotlinx.serialization.decodeFromString
import software.amazon.awssdk.services.s3.S3Client

class AwsHandler: AwsAdapter() {
    companion object {
        init {
            Server
            S3Client.create().getObject {
                it.bucket(System.getenv("LIGHTNING_SERVER_SETTINGS_BUCKET")!!)
                it.key(System.getenv("LIGHTNING_SERVER_SETTINGS_FILE")!!)
            }.use {
                it.reader().readText().let {
                    Serialization.Internal.json.decodeFromString<Settings>(it)
                }
            }
        }
    }
    init {
        Companion
    }
}