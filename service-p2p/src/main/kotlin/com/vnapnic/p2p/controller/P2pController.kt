package com.vnapnic.p2p.controller

import com.vnapnic.p2p.services.P2pService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView

@Controller
@ControllerAdvice
class MainController @Autowired constructor() {

    @Autowired
    lateinit var p2pService: P2pService

    @GetMapping(*["", "/", "/index", "/home", "/main"])
    fun displayMainPage(id: Long?, uuid: String?): ModelAndView? {
        return p2pService.displayMainPage(id, uuid)
    }

    @PostMapping(value = ["/room"], params = ["action=create"])
    fun processRoomSelection(@ModelAttribute("id") sid: String?, @ModelAttribute("uuid") uuid: String?, binding: BindingResult?): ModelAndView? {
        return p2pService.processRoomSelection(sid, uuid, binding)
    }

    @GetMapping("/room/{sid}/user/{uuid}")
    fun displaySelectedRoom(@PathVariable("sid") sid: String?, @PathVariable("uuid") uuid: String?): ModelAndView? {
        return p2pService.displaySelectedRoom(sid, uuid)
    }

    @GetMapping("/room/{sid}/user/{uuid}/exit")
    fun processRoomExit(@PathVariable("sid") sid: String?, @PathVariable("uuid") uuid: String?): ModelAndView? {
        return p2pService.processRoomExit(sid, uuid)
    }

    @GetMapping("/room/random")
    fun requestRandomRoomNumber(@ModelAttribute("uuid") uuid: String?): ModelAndView? {
        return p2pService.requestRandomRoomNumber(uuid)
    }

    @GetMapping("/offer")
    fun displaySampleSdpOffer(): ModelAndView {
        return ModelAndView("sdp_offer")
    }

    @GetMapping("/stream")
    fun displaySampleStreaming(): ModelAndView {
        return ModelAndView("streaming")
    }
}