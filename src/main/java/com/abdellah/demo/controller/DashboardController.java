package com.abdellah.demo.controller;

import org.springframework.ui.Model;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;


@Controller 
public class DashboardController 
{
    // Using SecureRandom for cryptographically secure and unpredictable values.
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    @GetMapping("/dashboard")
    public String getDashboardPage(
            HttpServletRequest request,
            Model model) 
    {
        /* No session ==> login page */
        HttpSession session = request.getSession(false);
        if (session == null) 
        {
            return "redirect:/login.html";
        }

        /* No userId ==> login page */
        Object id = session.getAttribute("userId");
        if (id == null) 
        {
            return "redirect:/login.html";
        }

        /* Valid Session ==> Get the existing token or create a new one */
        String csrfToken = (String) session.getAttribute("csrfToken");

        if (csrfToken == null) 
        {
            byte[] tokenBytes = new byte[32];
            SECURE_RANDOM.nextBytes(tokenBytes);

            csrfToken = Base64.getUrlEncoder()
                    .withoutPadding()
                    .encodeToString(tokenBytes);

            session.setAttribute("csrfToken", csrfToken);
        }

        // Make the token available to Thymeleaf.
        model.addAttribute("csrfToken", csrfToken);

        return "dashboard";
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            HttpServletRequest request,
            HttpServletResponse response)
    {
        HttpSession session = request.getSession(false);
        /*----------------------*/
        /* Session Verification */
        /*----------------------*/
        if (session == null)
        {
            return ResponseEntity.status(401)
                    .body("Not logged in");
        }

        /*----------------------------------*/
        /*     Validate the CSRF Token      */
        /*----------------------------------*/
        String expectedToken  = (String) session.getAttribute("csrfToken");
        String submittedToken = request.getHeader("X-CSRF-TOKEN");

        if (expectedToken == null 
                || submittedToken == null 
                || !secureTokenEquals(expectedToken, submittedToken)) 
        {
            return ResponseEntity.status(403)
                    .body("Invalid CSRF token");
        }

        /*----------------------------------*/
        /* Session destruction and deletion */
        /*----------------------------------*/
        session.invalidate();

        // Creating a cookie with the same name
        Cookie cookie = new Cookie("JSESSIONID", "");
        cookie.setMaxAge(0); // Delete immediately
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(request.isSecure());

        // Browser removes the cookie
        response.addCookie(cookie);

        return ResponseEntity.ok("Logged Out");
    }

    /**
     * Hashes both inputs to a fixed 32-byte size before running MessageDigest.isEqual.
     * This protects against timing attacks that leak token string lengths.
     */
    private boolean secureTokenEquals(String expected, String submitted) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            
            byte[] expectedHash  = md.digest(expected.getBytes(StandardCharsets.UTF_8));
            byte[] submittedHash = md.digest(submitted.getBytes(StandardCharsets.UTF_8));
            
            return MessageDigest.isEqual(expectedHash, submittedHash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 algorithm is missing from JVM execution environment", e);
        }
    }
}
