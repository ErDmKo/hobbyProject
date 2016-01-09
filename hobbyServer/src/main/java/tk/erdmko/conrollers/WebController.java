package tk.erdmko.conrollers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import tk.erdmko.models.MessageModel;
import tk.erdmko.models.SocketResponseModel;

@Controller
public class WebController {

    @MessageMapping("/wsIn")
    @SendTo("/wsOut")
    public SocketResponseModel wsHandler(MessageModel message ) {
        return new SocketResponseModel(message.getText());
    }
    @RequestMapping("/")
    public String index(
        @RequestParam(
            value = "name",
            required = false,
            defaultValue = ""
        )
        String name,
        Model model
    ) {
        model.addAttribute("name", name);
        return "index";
    }
}
