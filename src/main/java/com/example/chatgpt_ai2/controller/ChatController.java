package com.example.chatgpt_ai2.controller;

import com.example.chatgpt_ai2.service.ChatGptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chat")
public class ChatController {

    static String SYSTEM_MESSAGE = "You are gonna be the 2nd actor in a chatroom, given a specific personality and/or topic. You can start the conversation with a message.";

    static int interactionCounter = 1000; // Set the initial value of the counter


    @Autowired
    private ChatGptService chatGptService;

    @PostMapping("/response")
    @CrossOrigin(origins = "http://localhost:8080")
    public String postChatResponse(@RequestBody String prompt) {
        return chatGptService.getChatResponse(prompt, SYSTEM_MESSAGE);
    }

    @PostMapping("/system-message")
    @CrossOrigin(origins = "http://localhost:8080")
    public ResponseEntity<String> updateSystemMessage(@RequestBody String newSystemMessage) {
        SYSTEM_MESSAGE = newSystemMessage;
        return ResponseEntity.ok("System message updated successfully");
    }

    @PostMapping("/send-message")
    @CrossOrigin(origins = "http://localhost:8080")
    public ResponseEntity<String> sendMessage(@RequestBody MessageRequest request) {
        if (interactionCounter <= 0) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Interaction limit reached.");
        }
        interactionCounter--; // Decrement the counter after each interaction
        String message = request.getMessage();
        String response = generateResponse(message);
        return ResponseEntity.ok(response);
    }

    private String generateResponse(String message) {
        // Call the ChatGptService to generate a response based on the input message
        return chatGptService.getChatResponse(message, SYSTEM_MESSAGE);
    }


    static class MessageRequest {
        private String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

}
