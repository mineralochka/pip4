package com.pip.lab4;


import com.google.gson.Gson;
import com.pip.lab4.entity.Checks;
import com.pip.lab4.entity.UserAccount;
import com.pip.lab4.repository.CheckRepository;
import com.pip.lab4.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class JsonController {
    private Gson gson = new Gson();
    private UserRepository userRepository;
    private CheckRepository checkRepository;


    @Autowired
    public JsonController(UserRepository userRepository, CheckRepository checkRepository){
        this.userRepository = userRepository;
        this.checkRepository = checkRepository;
    }

    @RequestMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public String register(@RequestParam("id") String id, @RequestParam("password") String password){
        Long userId;
        try{
            userId = Long.valueOf(id);
        }
        catch (NumberFormatException e){
            return gson.toJson(false);
        }
        UserAccount user = userRepository.findById(userId);
        if (user != null){
            return gson.toJson(false); // id already taken
        }
        user = new UserAccount();
        user.setId(userId);
        user.setPasswordHash(password.hashCode());
        userRepository.save(user);
        return gson.toJson(true);
    }

    @RequestMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public String login(@RequestParam("id") String id, @RequestParam("password") String password){

        UserAccount user;
        Long userId;
        try{
            userId = Long.valueOf(id);
        }
        catch (NumberFormatException e){
            return gson.toJson(false);
        }
        if ((user = userRepository.findById(userId)) == null){
            return gson.toJson(false);
        }
        if (user.getPasswordHash() != password.hashCode()){
            return gson.toJson(false);
        }
        return gson.toJson(true);
    }

    @RequestMapping(value = "/check", produces = MediaType.APPLICATION_JSON_VALUE)
    public String check(@RequestParam("x") String string_x,
                        @RequestParam("y") String string_y,
                        @RequestParam("r") String string_r,
                        @RequestParam("user") String user){
        Double x = Double.parseDouble(string_x);
        Double y = Double.parseDouble(string_y);
        Double r = Double.parseDouble(string_r);
        if (r < 0){
            x = -x;
            y = -y;
            r = -r;
        }
        boolean result = false;
        if (x <= 0 && y <= 0 && y >= (-2 * x - r)) {
            result = true;
        }
        else if (x <= 0 && y >= 0 && x * x + y * y <= r * r / 4) {
            result = true;
        }
        else if (x >= 0 && y <= 0 && x <= r && y >= - r / 2) {
            result = true;
        }
        Checks myCheck = new Checks(x, y, r, Long.valueOf(user));
        myCheck.setResult(result);
        checkRepository.save(myCheck);
        return gson.toJson(result);
    }

    @RequestMapping(value = "/previousChecks", produces = MediaType.APPLICATION_JSON_VALUE)
    public String previousChecks(@RequestParam("user") String user){
        return gson.toJson(checkRepository.findAllByUserIdEquals(Long.valueOf(user)));
    }

}
