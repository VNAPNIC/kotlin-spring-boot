package com.vnapnic.common.service

import com.vnapnic.common.exception.SequenceException
import com.vnapnic.database.beans.SequenceIdBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.FindAndModifyOptions
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import kotlin.jvm.Throws

interface SequenceGeneratorService {
    fun nextSequenceId(key: String): Int?
}

open class SequenceGeneratorServiceImpl : SequenceGeneratorService {
    @Autowired
    lateinit var mongoOperations: MongoOperations

    @Throws(SequenceException::class)
    override fun nextSequenceId(key: String): Int? {
        //get sequence id
        val query = Query(Criteria.where("_id").`is`(key))

        //increase sequence id by 1
        val update = Update()
        update.inc("seq", 1)

        //return new increased id
        val options = FindAndModifyOptions()
        options.returnNew(true).upsert(true)

        // modify in document
        //if no id, throws SequenceException
        //optional, just a way to tell user when the sequence id is failed to generate.
        val sequenceId: SequenceIdBean = mongoOperations
                .findAndModify(query,
                        update, options,
                        SequenceIdBean::class.java)
                ?: throw SequenceException("Unable to get sequence id for key : $key")

        return sequenceId.seq
    }
}