package tk.erdmko.conrollers;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import tk.erdmko.models.OkResponse;
import tk.erdmko.models.SimpleJsonResponse;
import tk.erdmko.users.model.User;
import tk.erdmko.users.service.SecurityService;
import tk.erdmko.users.service.UserService;

@RestController
public class UserController {

    private static final transient org.slf4j.Logger LOG = LoggerFactory.getLogger(UserController.class.getName());

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/users/register", method = RequestMethod.POST)
    public SimpleJsonResponse registration(@RequestBody User input) {
        input.setEnabled(true);
        String pass = input.getPassword();
        userService.save(input);
        securityService.autoLogin(input.getUsername(), pass);
        return new OkResponse("OK");
    }
    @RequestMapping(value = "/users/authenticate", method = RequestMethod.POST)
    public SimpleJsonResponse auth(@RequestBody User input) {
        securityService.autoLogin(input.getUsername(), input.getPassword());
        return new OkResponse("OK");
    }

}
