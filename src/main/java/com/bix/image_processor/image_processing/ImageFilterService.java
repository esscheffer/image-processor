package com.bix.image_processor.image_processing;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class ImageFilterService {
    public byte[] convertToGrayscale(ImageEntity imageToFilter) throws IOException {
        // Load the image file into a BufferedImage object
        var bufferedImage = ImageIO.read(new ByteArrayInputStream(imageToFilter.getImage()));

        // Create a new BufferedImage object with a grayscale color model
        var grayscaleImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        grayscaleImage.getGraphics().drawImage(bufferedImage, 0, 0, null);

        // Iterate over each pixel in the original image and calculate the grayscale value
        for (int y = 0; y < bufferedImage.getHeight(); y++) {
            for (int x = 0; x < bufferedImage.getWidth(); x++) {
                int rgb = bufferedImage.getRGB(x, y);
                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = rgb & 0xFF;
                int gray = (int) (0.299 * red + 0.587 * green + 0.114 * blue);
                grayscaleImage.setRGB(x, y, gray);
            }
        }

        // Save the grayscale image to a file or return it as a response
        var baos = new ByteArrayOutputStream();
        ImageIO.write(grayscaleImage, "jpg", baos);
        return baos.toByteArray();
    }
}
