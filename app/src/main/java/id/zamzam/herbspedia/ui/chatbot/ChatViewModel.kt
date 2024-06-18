package id.zamzam.herbspedia.ui.chatbot

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import id.zamzam.herbspedia.ui.chatbot.Constants
import id.zamzam.herbspedia.ui.chatbot.MessageResponse
import kotlinx.coroutines.launch

//package id.zamzam.herbspedia.chatbot
//
//import androidx.compose.runtime.mutableStateListOf
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.google.ai.client.generativeai.GenerativeModel
//import com.google.ai.client.generativeai.type.content
//import kotlinx.coroutines.launch
//
class ChatViewModel : ViewModel() {
    val generativeModel: GenerativeModel = GenerativeModel(
        modelName = "gemini-pro",
        apiKey = Constants.apiKey
    )

    val messageList by lazy {
        mutableStateListOf<MessageResponse>()
    }

    fun sendMessage(question: String) {
        viewModelScope.launch {
            try {
                val chat = generativeModel.startChat(
                    history = messageList.map {
                        content(it.role) {
                            text(it.message)
                        }
                    }.toList()
                )

                messageList.add(MessageResponse(question, "user"))
                messageList.add(MessageResponse("Berpikir...", "model"))

                val response = chat.sendMessage(question)

                messageList.removeLast()
                messageList.add(MessageResponse(response.text.toString(), "model"))
//                Log.d("From Gemini", "ques")
            } catch (e: Exception) {
                messageList.removeLast()
                messageList.add(MessageResponse("Error: " + e.message.toString(), "model"))
            }
        }
    }
}