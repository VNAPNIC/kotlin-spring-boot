package com.vnapnic.p2p.services

import com.vnapnic.p2p.entities.Room
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.socket.WebSocketSession
import java.util.*

interface RoomService {

    fun getRooms(): Set<Room>?
    fun addRoom(room: Room): Boolean
    fun findRoomByStringId(sid: String?): Optional<Room>
    fun getRoomId(room: Room): Long?

    fun getClients(room: Room?): Map<String?, WebSocketSession>?
    fun addClient(room: Room, name: String?, session: WebSocketSession): WebSocketSession?
    fun removeClientByName(room: Room?, name: String?): WebSocketSession?
}

@Service
class RoomServiceImpl : RoomService {
    // repository substitution since this is a very simple realization
    private val rooms: MutableSet<Room> = TreeSet<Room>(compareBy { it.id })

    @Autowired
    lateinit var parser: Parser

    override fun getRooms(): Set<Room>? {
        val defensiveCopy: TreeSet<Room> = TreeSet<Room>(compareBy { it.id })
        defensiveCopy.addAll(rooms)
        return defensiveCopy
    }

    override fun addRoom(room: Room): Boolean = rooms.add(room)

    override fun findRoomByStringId(sid: String?): Optional<Room> = rooms.stream().filter { r -> r.id == parser.parseId(sid).get() }.findAny()

    override fun getRoomId(room: Room): Long? = room.id

    override fun getClients(room: Room?): Map<String?, WebSocketSession>? = Optional
            .ofNullable(room)
            .map { r -> Collections.unmodifiableMap(r.clients) }
            .orElse(Collections.emptyMap())

    override fun addClient(room: Room, name: String?, session: WebSocketSession): WebSocketSession? = room.clients?.put(name, session)

    override fun removeClientByName(room: Room?, name: String?): WebSocketSession? = room?.clients?.remove(name)
}