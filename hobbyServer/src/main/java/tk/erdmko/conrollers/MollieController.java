package tk.erdmko.conrollers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tk.erdmko.models.TestModel;

@RestController
public class MollieController {
    @RequestMapping("/client.json")

    public TestModel getJson() {
        return new TestModel();
    }

}
