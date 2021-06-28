package com.vnapnic.p2p.services

import com.vnapnic.p2p.entities.Room
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

interface P2pService {
    fun displayMainPage(id: Long?, uuid: String?): ModelAndView?
    fun processRoomSelection(sid: String?, uuid: String?, bindingResult: BindingResult?): ModelAndView?
    fun displaySelectedRoom(sid: String?, uuid: String?): ModelAndView?
    fun processRoomExit(sid: String?, uuid: String?): ModelAndView?
    fun requestRandomRoomNumber(uuid: String?): ModelAndView?
}

@Service
class P2pServiceImpl @Autowired constructor(private val roomService: RoomService, private val parser: Parser) : P2pService {

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    override fun displayMainPage(id: Long?, uuid: String?): ModelAndView? {
        val modelAndView = ModelAndView("main")
        modelAndView.addObject("id", id)
        modelAndView.addObject("rooms", roomService.getRooms())
        modelAndView.addObject("uuid", uuid)
        return modelAndView
    }

    override fun processRoomSelection(sid: String?, uuid: String?, bindingResult: BindingResult?): ModelAndView? {
        if (bindingResult?.hasErrors() == true) {
            // simplified version, no errors processing
            return ModelAndView(REDIRECT)
        }
        val optionalId: Optional<Long> = parser.parseId(sid)
        optionalId.ifPresent { id -> Optional.ofNullable(uuid).ifPresent { name -> roomService.addRoom(Room(id)) } }
        return displayMainPage(optionalId.orElse(null), uuid)
    }

    override fun displaySelectedRoom(sid: String?, uuid: String?): ModelAndView? {
        // redirect to main page if provided data is invalid
        var modelAndView = ModelAndView(REDIRECT)
        if (parser.parseId(sid).isPresent) {
            val room = roomService.findRoomByStringId(sid).orElse(null)
            if (room != null && uuid != null && uuid.isNotEmpty()) {
                logger.debug("User {} is going to join Room #{}", uuid, sid)
                // open the chat room
                modelAndView = ModelAndView("chat_room", "id", sid!!)
                modelAndView.addObject("uuid", uuid)
            }
        }
        return modelAndView
    }

    override fun processRoomExit(sid: String?, uuid: String?): ModelAndView? {
        if (sid != null && uuid != null) {
            logger.debug("User {} has left Room #{}", uuid, sid)
            // implement any logic you need
        }
        return ModelAndView(REDIRECT)
    }

    override fun requestRandomRoomNumber(uuid: String?): ModelAndView? {
        return displayMainPage(randomValue(), uuid)
    }

    private fun randomValue(): Long {
        return ThreadLocalRandom.current().nextLong(0, 100)
    }

    companion object {
        private const val REDIRECT = "redirect:/"
    }

}