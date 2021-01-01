package com.vnapnic.user.services

import com.vnapnic.user.repositories.UserRepository
import org.springframework.stereotype.Service

@Service
class SignUpService(val repository: UserRepository) {

}