package com.vnapnic.p2p.socket

import com.fasterxml.jackson.databind.ObjectMapper
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
class SignalHandler : TextWebSocketHandler() {
//
//    private val log = LoggerFactory.getLogger(SignalHandler::class.java)
//
//    @Autowired
//    lateinit var roomService: RoomService
//
//    private val objectMapper = ObjectMapper()

    // session id to room mapping
//    private val sessionIdToRoomMap: MutableMap<String, Room> = hashMapOf()

//    /**
//     * Corresponding to the open event
//     */
//    override fun afterConnectionEstablished(session: WebSocketSession) {
//        log.info("[afterConnectionEstablished][session({}) access]", session)
//        // webSocket has been opened, send a message to the client
//        // when data field contains 'true' value, the client starts negotiating
//        // to establish peer-to-peer connection, otherwise they wait for a counterpart
//        sendMessage(session, WebSocketMessage("Server", MSG_TYPE_JOIN, sessionIdToRoomMap.isNotEmpty().toString(), null, null))
//    }
//
//    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
//        log.debug("[ws] Session has been closed with status {}", status)
//        sessionIdToRoomMap.remove(session.id)
//    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val clientMessage = message.payload
        if (clientMessage.startsWith("hello") || clientMessage.startsWith("greet")) {
            session.sendMessage(TextMessage("Hello there!"));
        } else if (clientMessage.startsWith("time")) {
            val currentTime = LocalTime.now();
            session.sendMessage(TextMessage(currentTime.toString()));
        } else {
            session.sendMessage(TextMessage("Unknown command"));
        }

        // a message has been received
//        try {
//            val wsMessage = objectMapper.readValue(message.payload, WebSocketMessage::class.java)
//            log.debug("[ws] Message of {} type from {} received", wsMessage.type, wsMessage.from)
//            val userName: String? = wsMessage.from // origin of the message
//            val data: String? = wsMessage.data // payload
//
//            when (wsMessage.type) {
//                // text message from client has been received
//                MSG_TYPE_TEXT -> {
//                    log.debug("[ws] Text message: {}", wsMessage.data)
//                    // message.data is the text sent by client
//                    // process text message if needed
//                }
//                // process signal received from client
//                MSG_TYPE_OFFER, MSG_TYPE_ANSWER, MSG_TYPE_ICE -> {
//                    val candidate: Any? = wsMessage.candidate
//                    val sdp: Any? = wsMessage.sdp
//                    log.debug("[ws] Signal: {}",
//                            candidate?.toString()?.substring(0, 64) ?: sdp?.toString()?.substring(0, 64))
//
//                    val rm = sessionIdToRoomMap[session.id]
//                    rm?.let { room ->
//                        val clients = roomService.getClients(room)
//                        clients?.forEach { key, value ->
//                            if (key == userName) {
//                                sendMessage(value, WebSocketMessage(userName, wsMessage.type, data, candidate, sdp))
//                            }
//                        }
//                    }
//                }
//                // identify user and their opponent
//                MSG_TYPE_JOIN -> {
//                    // message.data contains connected room id
//                    log.debug("[ws] {} has joined Room: #{}", userName, wsMessage.data)
//                    val room = roomService.findRoomByStringId(data)
//                            .orElseThrow { IOException("Invalid room number received!") }
//                    roomService.addClient(room, userName, session)
//                    sessionIdToRoomMap[session.id] = room
//                }
//                MSG_TYPE_LEAVE -> {
//                    // message data contains connected room id
//                    log.debug("[ws] {} is going to leave Room: #{}", userName, wsMessage.data)
//                    // room id taken by session id
//                   val room = sessionIdToRoomMap[session.id]
//                    // remove the client which leaves from the Room clients list
//                    val client = roomService.getClients(room)
//                            ?.entries
//                            ?.stream()
//                            ?.filter { entry: Map.Entry<String?, WebSocketSession> -> entry.value.id == session.id}
//                            ?.map { map-> map.key }
//                            ?.findAny()
//                    client?.ifPresent { c -> roomService.removeClientByName(room, c) }
//                }
//                else -> {
//                    log.debug("[ws] Type of the received message {} is undefined!", wsMessage.type)
//                }
//            }
//
//        } catch (e: IOException) {
//            log.debug("An error occured: {}", e.message)
//        }
    }

//    private fun sendMessage(session: WebSocketSession, message: WebSocketMessage) {
//        try {
//            val json = objectMapper.writeValueAsString(message)
//            session.sendMessage(TextMessage(json))
//        } catch (e: IOException) {
//            log.debug("An error occured: {}", e.message)
//        }
//    }
//
//    companion object {
//        // message types, used in signalling:
//        // text message
//        private const val MSG_TYPE_TEXT = "text"
//
//        // SDP Offer message
//        private const val MSG_TYPE_OFFER = "offer"
//
//        // SDP Answer message
//        private const val MSG_TYPE_ANSWER = "answer"
//
//        // New ICE Candidate message
//        private const val MSG_TYPE_ICE = "ice"
//
//        // join room data message
//        private const val MSG_TYPE_JOIN = "join"
//
//        // leave room data message
//        private const val MSG_TYPE_LEAVE = "leave"
//    }
}