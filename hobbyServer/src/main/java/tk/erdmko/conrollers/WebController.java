package tk.erdmko.conrollers;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
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

    @Value("${file.upload.directory}")
    private String fileUploadDirectory;

    @MessageMapping("/wsIn")
    @SendTo("/wsOut")
    public SocketResponseModel wsHandler(MessageModel message ) {
        template.convertAndSend("hello", message.getText());
        return new SocketResponseModel(message.getText());
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
        template.convertAndSend("hello", "main page");
        return "index";
    }
}
