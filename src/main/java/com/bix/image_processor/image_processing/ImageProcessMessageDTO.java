package com.bix.image_processor.image_processing;

import java.io.Serializable;

public record ImageProcessMessageDTO(Long imageId, String username, double scale) implements Serializable {
}
