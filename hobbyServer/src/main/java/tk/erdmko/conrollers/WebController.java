package tk.erdmko.conrollers;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

import javax.imageio.ImageIO;

import tk.erdmko.models.MessageModel;
import tk.erdmko.models.SocketResponseModel;

@Controller
public class WebController {

    @Autowired
    AmqpTemplate template;

    @Autowired
    SimpMessagingTemplate wsTemplate;

    @Value("${file.upload.directory}")
    private String fileUploadDirectory;

    @MessageMapping("/wsIn")
    public SocketResponseModel wsHandler(MessageModel message) {
        SocketResponseModel out = new SocketResponseModel(message.getText());
        template.convertAndSend("hello", message.getText());
        wsTemplate.convertAndSend("/wsOut", out);
        return out;
    }
    @RequestMapping(value = "/images/{fileName}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@PathVariable String fileName) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            FileInputStream fileInputStream = new FileInputStream(fileUploadDirectory + fileName+".jpg");
            BufferedImage image = ImageIO.read(fileInputStream);
            ImageIO.write(image, "jpg", byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (java.io.IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
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
        template.convertAndSend("hello", "main, page");
        return "index";
    }
}
