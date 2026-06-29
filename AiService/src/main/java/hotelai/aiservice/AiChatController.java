package hotelai.aiservice;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
public class AiChatController {

    private final ChatModel chatModel;

    // Dependency injection handles assigning the active auto-configured OpenAI model bean here
    public AiChatController(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    /**
     * Endpoint to converse with your AI Assistant.
     * URL: http://localhost:8089/api/ai/chat?message=hello
     */
    @GetMapping("/chat")
    public String askHotelConcierge(@RequestParam(value = "message") String message) {
        try {
            // Fires the message block straight to OpenAI
            return chatModel.call(message);
        } catch (Exception e) {
            return "Error calling the AI Engine: " + e.getMessage() +
                    ". Please make sure your system's OPENAI_API_KEY environment variable is populated.";
        }
    }

    /**
     * Test Endpoint to verify the controller is alive.
     * URL: http://localhost:8089/api/ai/test
     */
    @GetMapping("/test")
    public String testEndpoint() {
        return "Tomcat and AiChatController are fully working! The 404 issue is coming from the OpenAI engine connection.";
    }
}
