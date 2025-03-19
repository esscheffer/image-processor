package com.bix.image_processor.image_processing;

import com.bix.image_processor.security.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ImageProcessorMessenger {

    private final ImageProcessorRabbitConfig rabbitConfig;

    private final RabbitTemplate rabbitTemplate;

    private final ImageRepository imageRepository;

    private final UserService userService;

    private final ImageResizerService imageResizerService;

    private final ImageFilterService imageFilterService;

    private final EmailService emailService;

    public void sendResizeMessage(ImageProcessMessageDTO message) {
        rabbitTemplate.convertAndSend(rabbitConfig.getResizeExchange(), rabbitConfig.getResizeRoutingKey(), message);
    }

    public void sendGrayscaleMessage(ImageProcessMessageDTO message) {
        rabbitTemplate.convertAndSend(rabbitConfig.getGrayscaleExchange(), rabbitConfig.getGrayscaleRoutingKey(), message);
    }


    @RabbitListener(queues = "${processor.rabbitmq.resize_queue:resize_queue}")
    public void receiveResizeMessage(ImageProcessMessageDTO message) {
        imageRepository.findById(message.imageId())
                .ifPresentOrElse(imageToResize -> {
                            try {
                                var resizedImage = imageResizerService.resizeImage(imageToResize, message.scale());
                                var requestingUser = userService.getUserByUsername(message.username());
                                emailService.sendEmailWithAttachment(requestingUser.getEmail(),
                                        "Your image is ready",
                                        "Thank you for using image processor. See your image attached.",
                                        imageToResize.getName(),
                                        resizedImage);
                                imageRepository.deleteById(message.imageId());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        },
                        () -> System.out.println("Image not found"));
    }

    @RabbitListener(queues = "${processor.rabbitmq.grayscale_queue:grayscale_queue}")
    public void receiveGrayscaleMessage(ImageProcessMessageDTO message) {
        imageRepository.findById(message.imageId())
                .ifPresentOrElse(imageToFilter -> {
                            try {
                                var grayscaleImage = imageFilterService.convertToGrayscale(imageToFilter);
                                var requestingUser = userService.getUserByUsername(message.username());
                                emailService.sendEmailWithAttachment(requestingUser.getEmail(),
                                        "Your image is ready",
                                        "Thank you for using image processor. See your image attached",
                                        imageToFilter.getName(),
                                        grayscaleImage);
                                imageRepository.deleteById(message.imageId());
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        },
                        () -> System.out.println("Image not found"));
    }
}
