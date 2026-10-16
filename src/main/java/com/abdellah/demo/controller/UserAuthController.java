package com.abdellah.demo.controller;

import com.abdellah.demo.entity.User;
import com.abdellah.demo.service.EmailService;
import com.abdellah.demo.service.UserAuthService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@Controller 
public class UserAuthController 
{
    private final UserAuthService userService;
    private final EmailService    emailService;

    public UserAuthController( UserAuthService userService, EmailService emailService )
    {
        this.userService  = userService;
        this.emailService = emailService;
    }


    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody User user ) 
    {
        String registerResponse = this.userService.isUserRegisterValid(user); 
        if( !registerResponse.equals("Valid") )
        {
            return ResponseEntity.badRequest().body(registerResponse);
        }

        this.userService.createUser(user);

        this.userService.createEmailVerificationToken(user);
        this.emailService.sendVerificationEmail(
            user.getEmail(),
            user.getEmailVerificationToken()
        );

        return ResponseEntity.ok("Account created. Please verify your email.");
    }


    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam("token") String emailToken)
    {
        if (!userService.verifyEmail(emailToken)) {
            return "redirect:/login.html?verification=invalid";
        }

        return "redirect:/login.html?verification=success";
    }


    @PostMapping("/login")
    public ResponseEntity<String> login( @RequestBody User user, HttpServletRequest request )
    {
        String loginResponse = this.userService.isUserLoginValid(user);

        if( ! loginResponse.equals("Valid") )
        {
            return ResponseEntity.badRequest()
                            .body( loginResponse );
        }

        User dbUser = this.userService.findByEmail( user.getEmail() );

        HttpSession session = request.getSession();
        request.changeSessionId();
        session.setAttribute( "userId", dbUser.getId() );
        session.setAttribute( "userEmail", dbUser.getEmail() );

        return ResponseEntity.ok("Valid Account");
    }


    /*-----------------------*/
    /*       FOR TESTS       */
    /*-----------------------*/
    @GetMapping("/session")
    public String sessionInfo(HttpServletRequest request)
    {
        HttpSession session = request.getSession(false);
    
        if(session == null)
            return "No session";
    
        return "User ID: " + session.getAttribute("userId");
    }

    @GetMapping("/test-email")
    public ResponseEntity<String> testEmail()
    {
        this.emailService.sendTestEmail("abdoachafik@gmail.com");
        return ResponseEntity.ok( "Test email sent" );
    }
}
