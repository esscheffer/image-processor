package com.bix.image_processor.image_processing;

import com.bix.image_processor.security.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageProcessService {
    private final UserService userService;
    private final ImageRepository imageRepository;
    private final ImageProcessorMessenger imageProcessorMessenger;

    public ProcessRequestResponseDTO resizeImage(MultipartFile image, double scale) throws IOException {
        var username = validateUserQuota();

        var savedImage = imageRepository.save(ImageEntity.builder()
                .name(image.getOriginalFilename())
                .image(image.getBytes())
                .build());

        imageProcessorMessenger.sendResizeMessage(new ImageProcessMessageDTO(savedImage.getId(), username, scale));

        return new ProcessRequestResponseDTO("Image send for resizing. Result will be send by email");
    }

    public ProcessRequestResponseDTO toGrayscale(MultipartFile image) throws IOException {
        var username = validateUserQuota();

        var savedImage = imageRepository.save(ImageEntity.builder()
                .name(image.getOriginalFilename())
                .image(image.getBytes())
                .build());

        imageProcessorMessenger.sendGrayscaleMessage(new ImageProcessMessageDTO(savedImage.getId(), username, 0.0));

        return new ProcessRequestResponseDTO("Image send for grayscale conversion. Result will be send by email");
    }

    private String validateUserQuota() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new RuntimeException("User is not authenticated");
        }

        var userDetails = (UserDetails) authentication.getPrincipal();

        if (userDetails.getAuthorities()
                .stream()
                .anyMatch(a -> a.getAuthority().equals("SIMPLE"))) {
            var user = userService.getUserByUsername(userDetails.getUsername());
            if (user.getQuota() <= 0) {
                throw new QuotaExceededException("User quota exceeded");
            } else {
                user.setQuota(user.getQuota() - 1);
                userService.saveUser(user);
            }
        }

        return userDetails.getUsername();
    }
}
