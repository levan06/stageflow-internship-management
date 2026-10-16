package com.abdellah.demo.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService
{
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender)
    {
        this.mailSender = mailSender;
    }


    
    public void sendVerificationEmail(String recipient, String token)
    {
        String verificationLink = "http://localhost:8080/verify-email?token=" + token;

        try
        {
            MimeMessage message = mailSender.createMimeMessage();
            
            // Set standard encoding and multipart flag to true
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("bootspring19@gmail.com", "StageFlow");
            helper.setTo(recipient);
            helper.setSubject("StageFlow - Verify your email");

            // Build the HTML body string for Better Message Style
            String htmlContent = """
                <!DOCTYPE html>
                <html>
                <head>
                  <meta charset="UTF-8">
                  <meta name="viewport" content="width=device-width, initial-scale=1.0">
                </head>
                <body style="margin: 0; padding: 0; background-color: #f4f5f7; font-family: system-ui, -apple-system, sans-serif; -webkit-font-smoothing: antialiased;">
                  <table width="100%%" border="0" cellspacing="0" cellpadding="0" style="background-color: #f4f5f7; padding: 40px 20px;">
                    <tr>
                      <td align="center">
                        <table width="100%%" style="max-width: 550px; background-color: #ffffff; border-radius: 12px; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05); overflow: hidden; border-collapse: collapse;">
                          
                          <!-- Accent Header Line -->
                          <tr><td height="5" style="background-color: #6366f1; line-height: 5px; font-size: 0;">&nbsp;</td></tr>
                          
                          <tr>
                            <td style="padding: 40px 32px; color: #1f2937;">
                              <h2 style="margin: 0 0 24px 0; font-size: 22px; font-weight: 700; color: #6366f1; letter-spacing: -0.5px;">StageFlow</h2>
                              <h1 style="margin: 0 0 16px 0; font-size: 24px; font-weight: 700; color: #111827; letter-spacing: -0.5px;">Verify your email address</h1>
                              <p style="margin: 0 0 28px 0; font-size: 16px; line-height: 1.6; color: #4b5563;">
                                Hello!<br><br>
                                Thank you for registering with StageFlow. Please click the button below to verify your email address and activate your account.
                              </p>
                              
                              <!-- CTA Button -->
                              <table border="0" cellspacing="0" cellpadding="0" style="margin: 0 auto 28px auto;">
                                <tr>
                                  <td align="center" bgcolor="#6366f1" style="border-radius: 6px;">
                                    <a href="%s" target="_blank" style="display: inline-block; padding: 14px 32px; font-size: 16px; font-weight: 600; color: #ffffff; text-decoration: none; border-radius: 6px;">
                                      Verify Email Address
                                    </a>
                                  </td>
                                </tr>
                              </table>
                              
                              <p style="margin: 0 0 24px 0; font-size: 14px; color: #ef4444; font-weight: 500;">
                                ⏳ This link will expire in 15 minutes.
                              </p>
                              <p style="margin: 0 0 32px 0; font-size: 13px; line-height: 1.5; color: #9ca3af;">
                                If you did not create a StageFlow account, you can safely ignore this email.
                              </p>
                              
                              <hr style="border: 0; border-top: 1px solid #e5e7eb; margin: 0 0 24px 0;">
                              <p style="margin: 0; font-size: 12px; color: #9ca3af; text-align: center;">
                                © 2026 StageFlow. All rights reserved.
                              </p>
                            </td>
                          </tr>
                        </table>
                      </td>
                    </tr>
                  </table>
                </body>
                </html>
                """.formatted(verificationLink);

            // The second argument 'true' tells Spring to render it as HTML text not plain text
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace(); 
        }
    }

    

    /*-----------------------*/
    /*       FOR TESTS       */
    /*-----------------------*/
    public void sendTestEmail(String recipient)
    {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(recipient);
        message.setSubject("StageFlow - Test email");
        message.setText(
            "Hello!\n\n" +
            "This is a test email from StageFlow.\n" +
            "Your email configuration is working."
        );

        mailSender.send(message);
    }
}
