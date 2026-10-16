package com.abdellah.demo.service;

import com.abdellah.demo.entity.User;
import com.abdellah.demo.repository.UserRepository;

import java.time.LocalDateTime;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service 
public class UserAuthService 
{
    private final UserRepository        userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenService          tokenService;

    public UserAuthService( UserRepository        userRepository, 
                            BCryptPasswordEncoder passwordEncoder,
                            TokenService          tokenService )
    {
        this.userRepository  = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService    = tokenService;
    }

    /**
     * 
     * @param user informations to verify after register
     * @return "Valid" if everything is verified else return the wrong field
     */
    public String isUserRegisterValid( User user )
    {
        if( user == null ) 
            return "User null";

        /* Name Verifications */
        if( user.getName() == null || user.getName().isBlank() ) 
            return "Enter Name";

        /* Email Verifications */
        if( user.getEmail() == null || user.getEmail().isBlank() )
            return "Enter Email";

        if(this.userRepository.existsByEmail(user.getEmail()))
            return "Email already exists";

        /* Password Verifications */
        if(user.getPassword() == null || user.getPassword().length() < 8)
            return "Invalid Password";

        return "Valid";
    }

    /**
     * Method to create a Token for email Verification
     * 
     * @param user
     */
    public void createEmailVerificationToken(User user)
    {
        String token = tokenService.generateSecureToken();

        user.setEmailVerificationToken(token);
        user.setEmailVerificationTokenExpiry(LocalDateTime.now().plusMinutes(15));

        userRepository.save(user);
    }


    public boolean verifyEmail(String token)
    {
        User user = userRepository.findByEmailVerificationToken(token); 

        if (user == null || user.getEmailVerificationTokenExpiry() == null
                         || user.getEmailVerificationTokenExpiry().isBefore(LocalDateTime.now()))
        {
            if (user != null) 
            {
                user.setEmailVerificationToken(null);
                user.setEmailVerificationTokenExpiry(null);
                userRepository.save(user);
            }
            return false;
        }

        // If Evrything Is Valid
        user.setEmailVerified(true);
        user.setEmailVerificationToken(null);
        user.setEmailVerificationTokenExpiry(null);
        
        userRepository.save(user);

        return true;
    }


    /**
     * 
     * @param user informations to verify after login
     * 
     * @return  'Valid' if everything is verified; otherwise, return 'Invalid' without 
     * specifying whether the issue is with the email or the login for security reasons.
     */
    public String isUserLoginValid( User user )
    {
        if( user == null || user.getEmail()    == null || user.getEmail().isBlank()
                         || user.getPassword() == null || user.getPassword().isBlank() ) 
        {
            return "Invalid Login"; 
        }

        User dbUser = this.userRepository.findByEmail(user.getEmail());
        if( dbUser == null )
            return "Invalid Login";

        if (!dbUser.isEmailVerified())
            return "Please verify your email before logging in";

        /* Password Verifications */
        if( ! passwordEncoder.matches( user.getPassword(), dbUser.getPassword() ) )
            return "Invalid Login";

        return "Valid";
    }

    public void createUser( User user )
    {
        user.setPassword(
            passwordEncoder.encode(user.getPassword())
        );

        this.userRepository.save(user);
    }


    /* REPOSITORY CALLS */
    public User findByEmail(String email)
    {
        return userRepository.findByEmail(email);
    }

    public User findByEmailVerificationToken(String token)
    {
        User userSearched = userRepository.findByEmailVerificationToken(token);
        if (userSearched == null)
            return null;

        LocalDateTime tokenExpiryTime = userSearched.getEmailVerificationTokenExpiry();

        // Email Token Expired
        if(tokenExpiryTime == null || tokenExpiryTime.isBefore(LocalDateTime.now()))
        {
            // Set the properties to null
            userSearched.setEmailVerificationToken(null);
            userSearched.setEmailVerificationTokenExpiry(null);

            // Pass it to the standard save method
            userRepository.save(userSearched);

            return null;
        }

        return userSearched;
    }

}
