package com.pip.lab4;


import com.google.gson.Gson;
import com.pip.lab4.entity.Checks;
import com.pip.lab4.entity.User;
import com.pip.lab4.repository.CheckRepository;
import com.pip.lab4.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
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
        User user = userRepository.findById(Long.valueOf(id));
        if (user != null){
            return gson.toJson(false); // id already taken
        }
        user = new User();
        user.setId(Long.valueOf(id));
        user.setPasswordHash(password.hashCode());
        userRepository.save(user);
        return gson.toJson(true);
    }

    @RequestMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public String login(@RequestParam("id") String id, @RequestParam("password") String password){
        User user;
        if ((user = userRepository.findById(Long.valueOf(id))) == null){
            return gson.toJson(false);
        }
        if (user.getPasswordHash() != password.hashCode()){
            return gson.toJson(false);
        }
        return gson.toJson(true);
    }

    @RequestMapping(value = "/check", produces = MediaType.APPLICATION_JSON_VALUE)
    public String check(@RequestParam("x") String x,
                        @RequestParam("y") String y,
                        @RequestParam("r") String r,
                        @RequestParam("user") String user){
        Checks myCheck = new Checks();
        myCheck.setX(Double.parseDouble(x));
        myCheck.setY(Double.parseDouble(y));
        myCheck.setR(Double.parseDouble(r));
        myCheck.setUser(Long.valueOf(user));
        //TODO checking the values
        boolean result = true;
        myCheck.setResult(result);
        checkRepository.save(myCheck);
        return gson.toJson(result);
    }

    @RequestMapping(value = "/previousChecks", produces = MediaType.APPLICATION_JSON_VALUE)
    public String previousChecks(@RequestParam("user") String user){
        return gson.toJson(checkRepository.findAllByUserEquals(Long.valueOf(user)));
    }

}
