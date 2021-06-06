package com.gmail.uia059466.liska.domain.usecase

interface MessageRepository {
    fun saveMessage(s: Int)
    fun getMessage():Int?
}

class MessageRepositoryImpl : MessageRepository {

    companion object {
        private var INSTANCE: MessageRepository? = null

        fun getInstance(): MessageRepository {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = MessageRepositoryImpl()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

    var messageFor:Int= 0


    override fun saveMessage(s: Int) {
        messageFor=s
    }

    override fun getMessage(): Int? {
        val result=messageFor
        if (result==0) return null
        messageFor=0
        return result
    }
}
