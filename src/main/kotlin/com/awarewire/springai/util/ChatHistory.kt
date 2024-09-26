package com.awarewire.springai.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.ai.chat.messages.AssistantMessage
import org.springframework.ai.chat.messages.Message
import org.springframework.ai.chat.messages.MessageType
import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

@Component
class ChatHistory {

    private val logger: Logger = LoggerFactory.getLogger(ChatHistory::class.java)

    private val chatHistoryLog: MutableMap<String, MutableList<Message>> = ConcurrentHashMap()
    private val messageAggregations: MutableMap<String, MutableList<Message>> = ConcurrentHashMap()

    fun addMessage(chatId: String, message: Message) {
        val groupId = toGroupId(chatId, message)

        messageAggregations.computeIfAbsent(groupId) { mutableListOf() }.add(message)

        if (messageAggregations.size > 1) {
            logger.warn("Multiple active sessions: {}", messageAggregations.keys)
        }

        val finishReason = getProperty(message, "finishReason")
        if (finishReason.equals("STOP", ignoreCase = true) || message.messageType == MessageType.USER) {
            finalizeMessageGroup(chatId, groupId)
        }
    }

    private fun toGroupId(chatId: String, message: Message): String {
        val messageId = getProperty(message, "id")
        return "$chatId:$messageId"
    }

    private fun getProperty(message: Message, key: String): String {
        return message.metadata.getOrDefault(key, "").toString()
    }

    private fun finalizeMessageGroup(chatId: String, groupId: String) {
        val sessionMessages = messageAggregations.remove(groupId)

        if (sessionMessages != null) {
            if (sessionMessages.size == 1) {
                commitToHistoryLog(chatId, sessionMessages[0])
            } else {
                val aggregatedContent = sessionMessages
                    .mapNotNull { it.content }
                    .joinToString("")
                commitToHistoryLog(chatId, AssistantMessage(aggregatedContent))
            }
        } else {
            logger.warn("No active session for groupId: {}", groupId)
        }
    }

    private fun commitToHistoryLog(chatId: String, message: Message) {
        chatHistoryLog.computeIfAbsent(chatId) { mutableListOf() }.add(message)
    }

    fun getAll(chatId: String): List<Message> {
        return chatHistoryLog.getOrDefault(chatId, emptyList())
    }

    fun getLastN(chatId: String, lastN: Int): List<Message> {
        val response = getAll(chatId)

        if (response.size <= lastN) {
            return response
        }

        val from = response.size - lastN
        val to = response.size
        logger.debug("Returning last {} messages from {} to {}", lastN, from, to)

        return ArrayList(response.subList(from, to))
    }
}