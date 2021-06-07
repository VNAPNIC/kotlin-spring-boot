package com.vnapnic.p2p.component

import com.fasterxml.jackson.databind.ObjectMapper
import com.vnapnic.common.service.RedisService
import com.vnapnic.p2p.entities.Room
import com.vnapnic.p2p.entities.WebSocketMessage
import com.vnapnic.p2p.services.RoomService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.io.IOException
import java.time.LocalTime


@Component
class CallHandler : TextWebSocketHandler() {

    private val log = LoggerFactory.getLogger(CallHandler::class.java)

    @Autowired
    lateinit var redisService: RedisService

    private val objectMapper = ObjectMapper()

    /**
     * Corresponding to the open event
     */
    override fun afterConnectionEstablished(session: WebSocketSession) {
        log.info("[afterConnectionEstablished][session({}) access]", session)
        sendMessage(session, WebSocketMessage("Server", DIAL, null, null, null))
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        log.info("[ws] Session has been closed with status {}", status)
//        sessionIdToRoomMap.remove(session.id)
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        // a message has been received
        try {
            val wsMessage = objectMapper.readValue(message.payload, WebSocketMessage::class.java)
            log.debug("[ws] Message of {} type from {} received", wsMessage.type, wsMessage.from)
            val userName: String? = wsMessage.from // origin of the message
            val data: String? = wsMessage.data // payload
            when (wsMessage.type) {
                DIAL -> {
                    //TODO(Gửi tin nhắn tới người nhận yêu cầu nhắc máy)
                }
                BUSY -> {
                    //TODO(Người nhận đang bận)
                }
                GET_THROUGH -> {
                    //TODO(Người nhận đã nhận được cuộc gọi)
                }
                PICKED_UP -> {
                    //TODO(Người nhận đã nhấc máy)
                }
                HANG_UP -> {
                    //TODO(Người nhận đã dập máy)
                }
                TIME_OUT -> {
                    //TODO(Người nhận không nhấc máy)
                }
                MISS_CALL -> {
                    //TODO(Bạn có cuộc gọi nhỡ)
                }
                END_CALL -> {
                    //TODO(Người gọi đã dập máy)
                }
                VIDEO_ON -> {
                }
                VIDEO_OFF -> {
                }
                AUDIO_ON -> {
                }
                AUDIO_OFF -> {
                }
                else -> {
                    log.info("[ws] Type of the received message {} is undefined!", wsMessage.type)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            log.info("An error occured: {}", e.message)
        }
    }

    private fun sendMessage(session: WebSocketSession, message: WebSocketMessage) {
        try {
            val json = objectMapper.writeValueAsString(message)
            session.sendMessage(TextMessage(json))
        } catch (e: IOException) {
            log.info("An error occured: {}", e.message)
        }
    }

    companion object {

        /**
         * Đang quay số
         */
        private const val DIAL = "dial"

        /**
         * Đang bận
         */
        private const val BUSY = "busy"

        /**
         * được nối máy với người mà bạn muốn gặp
         */
        private const val GET_THROUGH = "getThrough"

        /**
         * Nhấc máy
         */
        private const val PICKED_UP = "pickedUp"

        /**
         * Dập máy
         */
        private const val HANG_UP = "hangUp"

        /**
         * Có cuộc gọi nhỡ
         */
        private const val MISS_CALL = "missCall"

        /**
         * Người gọi dập máy
         */
        private const val END_CALL = "endCall"

        /**
         * Không nghe máy
         */
        private const val TIME_OUT = "timeOut"

        private const val VIDEO_ON = "videoOn"
        private const val VIDEO_OFF = "videoOff"
        private const val AUDIO_ON = "audioOn"
        private const val AUDIO_OFF = "audioOff"
    }
}