package com.yunus.research.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryService {

    private final Cloudinary cloudinary;

    /**
     * Upload an image to Cloudinary
     * 
     * @param file     The file to upload
     * @param folder   The folder in Cloudinary to store the image
     * @param publicId Optional custom public ID for the image
     * @return The secure URL of the uploaded image
     */
    public String uploadImage(MultipartFile file, String folder, String publicId) throws IOException {
        try {
            log.info("Starting upload to Cloudinary. Folder: {}, PublicId: {}, FileSize: {} bytes",
                    folder, publicId, file.getSize());

            Map<String, Object> options = ObjectUtils.asMap(
                    "folder", folder,
                    "overwrite", true);

            if (publicId != null && !publicId.isEmpty()) {
                options.put("public_id", publicId);
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), options);

            log.info("Cloudinary upload result: {}", uploadResult);

            String secureUrl = (String) uploadResult.get("secure_url");
            log.info("Image uploaded successfully to Cloudinary: {}", secureUrl);

            return secureUrl;
        } catch (Exception e) {
            log.error("Failed to upload image to Cloudinary: {}", e.getMessage(), e);
            throw new IOException("Failed to upload image: " + e.getMessage(), e);
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
