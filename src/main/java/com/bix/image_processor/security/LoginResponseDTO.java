package com.bix.image_processor.security;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LoginResponseDTO(@JsonProperty("access_token") String accessToken) {
}
