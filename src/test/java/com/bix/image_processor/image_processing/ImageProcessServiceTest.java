package com.bix.image_processor.image_processing;

import com.bix.image_processor.security.UserEntity;
import com.bix.image_processor.security.UserRole;
import com.bix.image_processor.security.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageProcessServiceTest {

    @Mock
    private UserService userService;
    @Mock
    private ImageRepository imageRepository;
    @Mock
    private ImageProcessorMessenger imageProcessorMessenger;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private ImageProcessService imageProcessService;

    private MultipartFile mockImage;
    private MockedStatic mockStatic;


    @BeforeEach
    void init() {
        mockStatic = mockStatic(SecurityContextHolder.class);

        when(SecurityContextHolder.getContext())
                .thenReturn(securityContext);
        when(securityContext.getAuthentication())
                .thenReturn(authentication);

        var bytes = new byte[]{1, 2, 3};
        var filename = "test.jpg";
        var contentType = "image/jpeg";
        mockImage = new MockMultipartFile(filename, filename, contentType, bytes);
    }


    @AfterEach
    void afterEach() {
        mockStatic.close();
    }

    @Test
    void resizeImage_userQuotaExceeded() {
        var user = UserEntity.builder()
                .role(UserRole.SIMPLE)
                .username("test")
                .build();

        when(authentication.getPrincipal())
                .thenReturn(user);

        when(userService.getUserByUsername(user.getUsername()))
                .thenReturn(UserEntity.builder().quota(0L).build());

        assertThrows(QuotaExceededException.class, () -> imageProcessService.resizeImage(mockImage, 0));
    }

    @Test
    void resizeImage_successPremiumUser() throws IOException {
        var user = UserEntity.builder()
                .role(UserRole.PREMIUM)
                .username("premiumUser")
                .build();

        when(authentication.getPrincipal())
                .thenReturn(user);

        when(imageRepository.save(any(ImageEntity.class)))
                .thenReturn(ImageEntity.builder().id(1L).build());

        var response = imageProcessService.resizeImage(mockImage, 0.5);
        assertEquals("Image send for resizing. Result will be send by email", response.message());
        verifyNoInteractions(userService);
    }

    @Test
    void resizeImage_success() throws IOException {
        var user = UserEntity.builder()
                .role(UserRole.SIMPLE)
                .username("test")
                .build();

        when(authentication.getPrincipal())
                .thenReturn(user);

        when(userService.getUserByUsername(user.getUsername()))
                .thenReturn(UserEntity.builder().quota(1L).build());

        when(imageRepository.save(any(ImageEntity.class)))
                .thenReturn(ImageEntity.builder().id(1L).build());

        var response = imageProcessService.resizeImage(mockImage, 0.5);

        verify(imageProcessorMessenger).sendResizeMessage(any(ImageProcessMessageDTO.class));
        assertEquals("Image send for resizing. Result will be send by email", response.message());
    }

    @Test
    void toGrayscale_userQuotaExceeded() {
        var user = UserEntity.builder()
                .role(UserRole.SIMPLE)
                .username("test")
                .build();

        when(authentication.getPrincipal())
                .thenReturn(user);

        when(userService.getUserByUsername(user.getUsername()))
                .thenReturn(UserEntity.builder().quota(0L).build());

        assertThrows(QuotaExceededException.class, () -> imageProcessService.toGrayscale(mockImage));
    }

    @Test
    void toGrayscale_successPremiumUser() throws IOException {
        var user = UserEntity.builder()
                .role(UserRole.PREMIUM)
                .username("premiumUser")
                .build();

        when(authentication.getPrincipal())
                .thenReturn(user);

        when(imageRepository.save(any(ImageEntity.class)))
                .thenReturn(ImageEntity.builder().id(1L).build());

        var response = imageProcessService.toGrayscale(mockImage);
        assertEquals("Image send for grayscale conversion. Result will be send by email", response.message());
        verifyNoInteractions(userService);
    }

    @Test
    void toGrayscale_success() throws IOException {
        var user = UserEntity.builder()
                .role(UserRole.SIMPLE)
                .username("test")
                .build();

        when(authentication.getPrincipal())
                .thenReturn(user);

        when(userService.getUserByUsername(user.getUsername()))
                .thenReturn(UserEntity.builder().quota(1L).build());

        when(imageRepository.save(any(ImageEntity.class)))
                .thenReturn(ImageEntity.builder().id(1L).build());

        var response = imageProcessService.toGrayscale(mockImage);

        verify(imageProcessorMessenger).sendGrayscaleMessage(any(ImageProcessMessageDTO.class));
        assertEquals("Image send for grayscale conversion. Result will be send by email", response.message());
    }
}