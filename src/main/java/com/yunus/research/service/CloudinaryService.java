package com.yunus.research.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryService {

    private final Cloudinary cloudinary;

    /**
     * Upload an image or file to Cloudinary with auto resource type
     *
     * @param file     The file to upload
     * @param folder   The folder in Cloudinary to store the file
     * @param publicId Optional custom public ID for the file
     * @return The secure URL of the uploaded file
     */
    public String uploadImage(MultipartFile file, String folder, String publicId) throws IOException {
        try {
            log.info("Starting upload to Cloudinary. Folder: {}, PublicId: {}, FileSize: {} bytes",
                    folder, publicId, file.getSize());

            Map<String, Object> options = ObjectUtils.asMap(
                    "folder", folder,
                    "resource_type", "auto",
                    "overwrite", true);

            if (publicId != null && !publicId.isEmpty()) {
                options.put("public_id", publicId);
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), options);

            log.info("Cloudinary upload result: {}", uploadResult);

            String secureUrl = (String) uploadResult.get("secure_url");
            log.info("File uploaded successfully to Cloudinary: {}", secureUrl);

            return secureUrl;
        } catch (Exception e) {
            log.error("Failed to upload file to Cloudinary: {}", e.getMessage(), e);
            String message = e.getMessage();
            if (message != null && message.contains("cloud_name is disabled")) {
                message = "Cloudinary account/cloud_name is disabled or suspended. Please verify your CLOUDINARY_CLOUD_NAME and credentials in .env";
            }
            throw new IOException("Failed to upload file: " + message, e);
        }
    }

    /**
     * Upload a member profile photo
     *
     * @param file     The photo file
     * @param username The member's username (used as public ID)
     * @return The secure URL of the uploaded photo
     */
    public String uploadMemberPhoto(MultipartFile file, String username) throws IOException {
        return uploadImage(file, "research-group/members", username);
    }

    /**
     * Upload a news image to Cloudinary
     *
     * @param file The image file
     * @return The secure URL of the uploaded image
     */
    public String uploadNewsImage(MultipartFile file) throws IOException {
        String publicId = "news-" + UUID.randomUUID();
        return uploadImage(file, "research-group/news", publicId);
    }

    /**
     * Upload a hero image to Cloudinary
     *
     * @param file The image file
     * @return The secure URL of the uploaded image
     */
    public String uploadHeroImage(MultipartFile file) throws IOException {
        String publicId = "hero-" + UUID.randomUUID();
        return uploadImage(file, "research-group/hero", publicId);
    }

    /**
     * Upload a notice file (image, PDF, document) to Cloudinary
     *
     * @param file The file
     * @return The secure URL of the uploaded file
     */
    public String uploadNoticeFile(MultipartFile file) throws IOException {
        String publicId = "notice-" + UUID.randomUUID();
        return uploadImage(file, "research-group/notices", publicId);
    }

    /**
     * Delete an image from Cloudinary
     *
     * @param publicId The public ID of the image to delete
     */
    public void deleteImage(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            log.info("Image deleted from Cloudinary: {}", publicId);
        } catch (IOException e) {
            log.error("Failed to delete image from Cloudinary: {}", publicId, e);
        }
    }
}
