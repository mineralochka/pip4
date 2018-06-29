package com.pip.lab4;


import com.google.gson.Gson;
import com.pip.lab4.entity.Checks;
import com.pip.lab4.entity.UserAccount;
import com.pip.lab4.repository.CheckRepository;
import com.pip.lab4.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class JsonController {

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private Gson gson = new Gson();

    private UserRepository userRepository;

    private CheckRepository checkRepository;

    @Autowired
    public JsonController(UserRepository userRepository, CheckRepository checkRepository){
        this.userRepository = userRepository;
        this.checkRepository = checkRepository;
    }

    @RequestMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public String register(@RequestParam("id") Long id, @RequestParam("password") String password){

        UserAccount user = userRepository.findById(id);
        if (user != null){
            return gson.toJson(false); // id already taken
        }

        user = new UserAccount();
        user.setId(id);

        user.setPasswordHash(passwordEncoder.encode(password));
        userRepository.save(user);
        return gson.toJson(true);
    }

    @RequestMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public String login(@RequestParam("id") Long id, @RequestParam("password") String password){

        UserAccount user;

        if ((user = userRepository.findById(id)) == null){
            return gson.toJson(false); // no such user
        }
        if (passwordEncoder.matches(password, user.getPasswordHash())){
            return gson.toJson(true);
        }
        return gson.toJson(false);
    }

    /**
     * Returns whether the (x; y) point is inside the area defined by current r
     *
     * @param  x                        x parameter
     * @param  y                        y parameter
     * @param  r                        r parameter
     * @param  user                     user identifier
     *
     * @return                          {@code true} if the point is in the area
     *                                  {@code false} otherwise
     */
    @RequestMapping(value = "/check", produces = MediaType.APPLICATION_JSON_VALUE)
    public String check(@RequestParam("x") Double x,
                        @RequestParam("y") Double y,
                        @RequestParam("r") Double r,
                        @RequestParam("user") Long user) {

        /* invert for negative r and treat like normal after that */
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
        Checks myCheck = new Checks(x, y, r, user);
        myCheck.setResult(result);
        checkRepository.save(myCheck);
        return gson.toJson(result);
    }

    @RequestMapping(value = "/previousChecks", produces = MediaType.APPLICATION_JSON_VALUE)
    public String previousChecks(@RequestParam("user") Long user){
        return gson.toJson(checkRepository.findAllByUserIdEquals(user));
    }

}
