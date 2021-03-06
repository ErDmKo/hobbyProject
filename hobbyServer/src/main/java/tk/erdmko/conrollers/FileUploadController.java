package tk.erdmko.conrollers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.io.FileOutputStream;

import javax.servlet.http.HttpServletRequest;

import tk.erdmko.models.FailResponse;
import tk.erdmko.models.OkResponse;
import tk.erdmko.models.SimpleJsonResponse;
import tk.erdmko.models.UrlResponse;

@Controller
public class FileUploadController {

    @Value("${file.upload.directory}")
    private String fileUploadDirectory;

    private SimpMessagingTemplate template;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    public FileUploadController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @RequestMapping(value="/upload", method=RequestMethod.POST)
    public @ResponseBody
    SimpleJsonResponse handleFileUpload(@RequestParam("name") String name,
                            @RequestParam("file") MultipartFile file){
        if (!file.isEmpty()) {
            try {
                String originalFileExtension = file
                        .getOriginalFilename()
                        .substring(file.getOriginalFilename().lastIndexOf("."));
                String fileName = name + originalFileExtension;
                String fullFilename = fileUploadDirectory + fileName;
                FileOutputStream newFile = new FileOutputStream(fullFilename);
                newFile.write(file.getBytes());
                newFile.close();
                String imagePath =
                    MvcUriComponentsBuilder.fromMappingName("WC#getImage").arg(0, name).build();

                this.template.convertAndSend(
                        "/wsOut",
                        new UrlResponse(imagePath)
                );
                return new OkResponse("File downloaded to '"+fullFilename+"' path");
            } catch (Exception e) {
                return new FailResponse(name+" => "+e.getMessage());
            }
        } else {
            return new FailResponse("You failed to upload " + name + " because the file was empty.");
        }
    }
}
