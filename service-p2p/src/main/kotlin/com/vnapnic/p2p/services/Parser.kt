package com.vnapnic.p2p.services

import org.slf4j.LoggerFactory
import java.util.*

interface Parser {
    fun parseId(sid:String?): Optional<Long>
}

class ParserImpl : Parser{
    private val log = LoggerFactory.getLogger(Parser::class.java)

    override fun parseId(sid: String?): Optional<Long> {
        var id: Long? = null
        try {
            id = java.lang.Long.valueOf(sid)
        } catch (e: Exception) {
            log.debug("An error occured: {}", e.message)
        }
        return Optional.ofNullable(id)
    }

}