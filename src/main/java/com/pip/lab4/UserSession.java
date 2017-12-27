package com.pip.lab4;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class UserSession {
    Long getUser() {
        return user;
    }

    private Long user;

    boolean isLoggedIn() {
        return user != null;
    }

    void setUser(Long user) {
        this.user = user;
    }
}
