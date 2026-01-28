package com.yunus.research.controller;

import com.cloudinary.Cloudinary;
import com.yunus.research.service.CloudinaryService;
import com.yunus.research.service.MemberService;
import com.yunus.research.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
@Slf4j
public class UploadController {

    private final CloudinaryService cloudinaryService;
    private final MemberService memberService;
    private final Cloudinary cloudinary;

    /**
     * Test endpoint to verify Cloudinary configuration
     */
    @GetMapping("/test-cloudinary")
    public ResponseEntity<Map<String, Object>> testCloudinary() {
        try {
            String cloudName = cloudinary.config.cloudName;
            log.info("Cloudinary cloud name: {}", cloudName);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "cloudName", cloudName != null ? cloudName : "NOT SET",
                    "message", "Cloudinary is configured"));
        } catch (Exception e) {
            log.error("Cloudinary test failed", e);
            return ResponseEntity.internalServerError().body(Map.of(
                    "success", false,
                    "error", e.getMessage()));
        }
    }

    /**
     * Upload a member's profile photo to Cloudinary
     * 
     * @param file     The image file to upload
     * @param username The member's username
     * @return The URL of the uploaded image
     */
    @PostMapping("/member-photo/{username}")
    public ResponseEntity<Map<String, Object>> uploadMemberPhoto(
            @RequestParam("file") MultipartFile file,
            @PathVariable String username) {

        // Validate file
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", "No file provided"));
        }

        // Validate file type
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", "File must be an image"));
        }

        // Validate file size (max 5MB)
        if (file.getSize() > 5 * 1024 * 1024) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "error", "File size must be less than 5MB"));
        }

        try {
            log.info("Received upload request for member: {}, file: {}, size: {} bytes",
                    username, file.getOriginalFilename(), file.getSize());

            // Upload to Cloudinary
            String photoUrl = cloudinaryService.uploadMemberPhoto(file, username);

            // Update member's photoUrl in database
            MemberDto updateDto = new MemberDto(
                    null, null, null, null, null, null, null, null,
                    photoUrl, null, null, null);

            var updatedMember = memberService.updateMemberProfile(username, updateDto);

            if (updatedMember.isPresent()) {
                log.info("Photo uploaded successfully for member: {}", username);
                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "photoUrl", photoUrl,
                        "message", "Photo uploaded successfully"));
            } else {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "error", "Member not found"));
            }
        } catch (Exception e) {
            log.error("Failed to upload photo for member: {} - Error: {}", username, e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of(
                    "success", false,
                    "error", "Failed to upload photo: " + e.getMessage()));
        }
    }
}
