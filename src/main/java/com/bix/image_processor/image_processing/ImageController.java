package com.bix.image_processor.image_processing;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageProcessService imageProcessService;

    @PostMapping(value = "/resize")
    public ResponseEntity<ProcessRequestResponseDTO> resizeImage(@RequestParam("image") MultipartFile image, @RequestParam("scale") double scale) throws IOException {
        try {
            return ResponseEntity.ok().body(imageProcessService.resizeImage(image, scale));
        } catch (QuotaExceededException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ProcessRequestResponseDTO(e.getMessage()));
        }
    }

    @PostMapping(value = "/grayscale")
    @ResponseBody
    public ResponseEntity<ProcessRequestResponseDTO> toGrayscale(@RequestParam("image") MultipartFile image) throws IOException {
        try {
            return ResponseEntity.ok().body(imageProcessService.toGrayscale(image));
        } catch (QuotaExceededException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ProcessRequestResponseDTO(e.getMessage()));
        }
    }
}
