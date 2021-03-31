package com.vnapnic.user.services

import com.vnapnic.user.repositories.DemoRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class DemoService(@Autowired private val demoRepository: DemoRepository) {
    fun test(name:String) :String = demoRepository.test(name)
}