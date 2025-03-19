package com.bix.image_processor.image_processing;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Blob;

@Entity(name = "image")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Lob
    private byte[] image;
}
