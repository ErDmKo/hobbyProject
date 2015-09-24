package tk.erdmko.conrollers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by erdmko on 23/09/15.
 */
@Controller
public class mainPage {
    @RequestMapping("/")
    public String index(@RequestParam(value = "name", required = false, defaultValue = "undef") String name, Model model){
        model.addAttribute("name", name);
        return "Hi";
    }
}
