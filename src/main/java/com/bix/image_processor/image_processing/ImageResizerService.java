package com.bix.image_processor.image_processing;

import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageResizerService {

    public byte[] resizeImage(ImageEntity image, double scale) throws IOException {
        var resizedImage = Thumbnails.of(new ByteArrayInputStream(image.getImage()))
                .scale(scale)
                .outputFormat("jpg")
                .asBufferedImage();

        var baos = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, "jpg", baos);

        return baos.toByteArray();
    }
}
