package com.yunus.research.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
@Slf4j
@RequiredArgsConstructor
public class GmailEmailService {

    private final JavaMailSender mailSender;

    @Value("${mail.sender.email:biochemical.research.lab@gmail.com}")
    private String senderEmail;

    @Value("${mail.sender.name:Biochemical and Environmental Research Lab}")
    private String senderName;

    public void sendPasswordResetCode(String recipientEmail, String recipientName, String code) {
        String displayName = recipientName == null || recipientName.isBlank() ? recipientEmail : recipientName;
        String subject = "Password reset code for your research account";
        String textContent = buildPasswordResetText(displayName, code);
        String htmlContent = buildPasswordResetHtml(displayName, code);
        sendEmail(recipientEmail, subject, textContent, htmlContent, "password reset code");
    }

    public void sendNoAccountExistsNotice(String recipientEmail) {
        String subject = "Account assistance update";
        String textContent = buildNoAccountText();
        String htmlContent = buildNoAccountHtml();

        sendEmail(recipientEmail, subject, textContent, htmlContent, "account-not-found notice");
    }

    private void sendEmail(String recipientEmail, String subject, String textContent, String htmlContent,
            String emailType) {
        if (senderEmail == null || senderEmail.isBlank()) {
            throw new IllegalStateException("Gmail sender email is not configured");
        }

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    "UTF-8");
            helper.setFrom(senderEmail, senderName);
            helper.setTo(recipientEmail);
            helper.setReplyTo(senderEmail);
            helper.setSubject(subject);
            helper.setText(textContent, htmlContent);

            log.info("Sending {} email to {} using Gmail sender {} <{}>", emailType, recipientEmail, senderName,
                    senderEmail);
            mailSender.send(mimeMessage);
            log.info("Gmail {} email sent to {}", emailType, recipientEmail);
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("Gmail {} email build failed for {}", emailType, recipientEmail, e);
            throw new IllegalStateException("Failed to build email message", e);
        } catch (org.springframework.mail.MailException e) {
            log.error("Gmail {} email failed for {}", emailType, recipientEmail, e);
            throw new IllegalStateException("Failed to send password reset email", e);
        }
    }

    private String buildPasswordResetText(String displayName, String code) {
        return String.format(
                "Hello %s,%n%nWe received a request to reset the password for your research group account.%n%nUse this 6-digit verification code to continue:%n%n%s%n%nThis code expires in 15 minutes.%nIf you did not request a password reset, you can safely ignore this email.%n%nBest regards,%n%s",
                displayName,
                code,
                senderName);
    }

    private String buildPasswordResetHtml(String displayName, String code) {
        return "<!DOCTYPE html>"
                + "<html lang=\"en\">"
                + "<head><meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"></head>"
                + "<body style=\"margin:0;padding:0;background:#eef5fb;font-family:Arial,Helvetica,sans-serif;color:#1f2937;\">"
                + "<table role=\"presentation\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"background:#eef5fb;padding:32px 12px;\">"
                + "<tr><td align=\"center\">"
                + "<table role=\"presentation\" width=\"100%\" style=\"max-width:640px;background:#ffffff;border-radius:18px;overflow:hidden;border:1px solid #dbe7f3;box-shadow:0 12px 30px rgba(15,23,42,0.08);\">"
                + "<tr><td style=\"padding:28px 32px;background:linear-gradient(135deg,#0f766e,#1d4ed8);color:#ffffff;\">"
                + "<div style=\"font-size:14px;letter-spacing:1px;text-transform:uppercase;opacity:0.9;\">Biochemical and Environmental Research Lab</div>"
                + "<div style=\"font-size:26px;font-weight:700;margin-top:10px;\">Password reset verification</div>"
                + "</td></tr>"
                + "<tr><td style=\"padding:32px;\">"
                + "<p style=\"margin:0 0 16px;font-size:16px;line-height:1.7;\">Hello " + escapeHtml(displayName)
                + ",</p>"
                + "<p style=\"margin:0 0 16px;font-size:16px;line-height:1.7;\">We received a request to reset the password for your research group account.</p>"
                + "<div style=\"margin:28px 0;padding:22px 18px;text-align:center;background:#f8fafc;border:1px solid #cbd5e1;border-radius:14px;\">"
                + "<div style=\"font-size:13px;letter-spacing:0.12em;text-transform:uppercase;color:#64748b;margin-bottom:10px;\">Verification code</div>"
                + "<div style=\"font-size:34px;font-weight:800;letter-spacing:8px;color:#0f172a;\">" + code + "</div>"
                + "</div>"
                + "<p style=\"margin:0 0 12px;font-size:15px;line-height:1.7;color:#475569;\">This code expires in 15 minutes.</p>"
                + "<p style=\"margin:0 0 24px;font-size:15px;line-height:1.7;color:#475569;\">If you did not request a password reset, you can safely ignore this email.</p>"
                + "<p style=\"margin:0;font-size:15px;line-height:1.7;\">Best regards,<br><strong>"
                + escapeHtml(senderName)
                + "</strong></p>"
                + "</td></tr>"
                + "<tr><td style=\"padding:18px 32px 28px;color:#94a3b8;font-size:12px;line-height:1.6;border-top:1px solid #e2e8f0;\">"
                + "This is an automated message. Please do not reply to this email."
                + "</td></tr>"
                + "</table>"
                + "</td></tr>"
                + "</table>"
                + "</body></html>";
    }

    private String buildNoAccountText() {
        return String.format(
                "Hello,%n%nWe could not find an account associated with this email address.%n%nIf you believe this is an error or you would like to create a new account, please contact the Professor.%n%nBest regards,%n%s",
                senderName);
    }

    private String buildNoAccountHtml() {
        return "<!DOCTYPE html>"
                + "<html lang=\"en\">"
                + "<head><meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"></head>"
                + "<body style=\"margin:0;padding:0;background:#eef5fb;font-family:Arial,Helvetica,sans-serif;color:#1f2937;\">"
                + "<table role=\"presentation\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"background:#eef5fb;padding:32px 12px;\">"
                + "<tr><td align=\"center\">"
                + "<table role=\"presentation\" width=\"100%\" style=\"max-width:640px;background:#ffffff;border-radius:18px;overflow:hidden;border:1px solid #dbe7f3;box-shadow:0 12px 30px rgba(15,23,42,0.08);\">"
                + "<tr><td style=\"padding:28px 32px;background:linear-gradient(135deg,#0f766e,#1d4ed8);color:#ffffff;\">"
                + "<div style=\"font-size:14px;letter-spacing:1px;text-transform:uppercase;opacity:0.9;\">Biochemical and Environmental Research Lab</div>"
                + "<div style=\"font-size:26px;font-weight:700;margin-top:10px;\">Account assistance update</div>"
                + "</td></tr>"
                + "<tr><td style=\"padding:32px;\">"
                + "<p style=\"margin:0 0 16px;font-size:16px;line-height:1.7;\">Hello,</p>"
                + "<p style=\"margin:0 0 16px;font-size:16px;line-height:1.7;\">We could not find an account associated with this email address.</p>"
                + "<p style=\"margin:0 0 24px;font-size:16px;line-height:1.7;\">If you believe this is an error or you would like to create a new account, please contact the Professor.</p>"
                + "<p style=\"margin:0;font-size:15px;line-height:1.7;\">Best regards,<br><strong>"
                + escapeHtml(senderName)
                + "</strong></p>"
                + "</td></tr>"
                + "<tr><td style=\"padding:18px 32px 28px;color:#94a3b8;font-size:12px;line-height:1.6;border-top:1px solid #e2e8f0;\">"
                + "This is an automated message. Please do not reply to this email."
                + "</td></tr>"
                + "</table>"
                + "</td></tr>"
                + "</table>"
                + "</body></html>";
    }

    private String escapeHtml(String value) {
        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}