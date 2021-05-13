package com.vnapnic.auth.services

import com.vnapnic.common.models.Account
import com.vnapnic.common.property.ApplicationProperty
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.security.Key
import java.util.*
import java.util.UUID
import javax.crypto.spec.SecretKeySpec
import javax.xml.bind.DatatypeConverter


interface AuthService {
    fun bySocialId(socialId: String?): Account?
    fun byEmail(email: String?): Account?

    //    fun save(account: Account?): Account?
    fun validatePassword(password: String?, account: Account?): Boolean
    fun encryptPassword(password: String?): String?
//    fun parseJWT(token: String?): Account?
}


@Service
class AuthServiceImpl : AuthService {

//    @Autowired
//    lateinit var accountRepository: AccountRepository

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder



    override fun bySocialId(socialId: String?): Account? = Account("123", email = "test@gmail.com", password = passwordEncoder.encode("123456"))

//    override fun byEmail(email: String?): Account? = accountRepository.findByEmail(email)

    override fun byEmail(email: String?): Account? = Account("123", email = "test@gmail.com", password = passwordEncoder.encode("123456"))

//    override fun save(account: Account?): Account? = if(account!=null) accountRepository.save(account) else null

    override fun validatePassword(password: String?, account: Account?): Boolean = passwordEncoder.matches(password, account?.password)

    override fun encryptPassword(password: String?): String? = passwordEncoder.encode(password)


//    override fun parseJWT(token: String?): Account? {
//        //This line will throw an exception if it is not a signed JWS (as expected)
//        val jwsClaims = Jwts.parser()
//                .setSigningKey(DatatypeConverter.parseBase64Binary(property.jwtPhase))
//                .parseClaimsJws(token)
//
//        if (jwsClaims.header.getAlgorithm() != SignatureAlgorithm.HS256.value) throw AuthenticationException("Invalid JWT token." + jwsClaims.header.getAlgorithm())
//        val claims = jwsClaims.body
//
//        // Find record from redis
//        val record: String = redis.opsForValue().get("tkn:" + claims.id)
//        if (record == null || !token.equals(record)) {
//            throw AuthenticationException("Token is expired.")
//        }
//
//        val account = accountRepository.findOne()
//                ?: throw AuthenticationException("Cannot find user account.")
//        if (!account.active!!) throw AuthenticationException("Account is inactivated.")
//
//        return account
//    }

}