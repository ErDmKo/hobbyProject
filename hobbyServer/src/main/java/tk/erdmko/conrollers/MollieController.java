package tk.erdmko.conrollers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tk.erdmko.models.OkResponse;

@RestController
public class MollieController {
    @RequestMapping("/client.json")
    public OkResponse getJson() {
        return new OkResponse("Ok in client json");
    }

}

