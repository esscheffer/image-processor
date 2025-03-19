package com.bix.image_processor;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SmokeTestController {
    @GetMapping("/smoke-test")
    public String smokeTest() {
        return "Hello, this is a smoke test";
    }
}
