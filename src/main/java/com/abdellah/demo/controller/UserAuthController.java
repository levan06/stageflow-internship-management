package com.abdellah.demo.controller;

import org.springframework.web.bind.annotation.RestController;

import com.abdellah.demo.entity.User;
import com.abdellah.demo.service.UserAuthService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController 
public class UserAuthController 
{
    private final UserAuthService userService;

    public UserAuthController( UserAuthService userService )
    {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody User user ) 
    {
        String registerResponse = this.userService.isUserRegisterValid(user); 

        if( ! registerResponse.equals("Valid") )
        {
            return ResponseEntity.badRequest()
                             .body(registerResponse);
        }

        userService.createUser(user);
        return ResponseEntity.ok("Account Created");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(
            @RequestBody User user,
            HttpServletRequest request )
    {
        String loginResponse = this.userService.isUserLoginValid(user);

        if( ! loginResponse.equals("Valid") )
        {
            return ResponseEntity.badRequest()
                            .body( loginResponse );
        }

        User dbUser = userService.findByEmail( user.getEmail() );

        HttpSession session = request.getSession();
        request.changeSessionId();
        session.setAttribute( "userId", dbUser.getId() );

        return ResponseEntity.ok("Valid Account");
    }

    @GetMapping("/session")
    public String sessionInfo(HttpServletRequest request)
    {
        HttpSession session = request.getSession(false);
    
        if(session == null)
            return "No session";
    
        return "User ID: " + session.getAttribute("userId");
    }
}
