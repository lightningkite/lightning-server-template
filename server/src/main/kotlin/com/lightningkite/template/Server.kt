package com.lightningkite.template

import com.lightningkite.lightningserver.core.ContentType
import com.lightningkite.lightningserver.core.ServerPath
import com.lightningkite.lightningserver.core.ServerPathGroup
import com.lightningkite.lightningserver.http.*

object Server : ServerPathGroup(ServerPath.root) {

    val index = get.handler {
        HttpResponse(HttpContent.Text("Hello World From Lightning Server!", ContentType.Text.Plain))
    }

}