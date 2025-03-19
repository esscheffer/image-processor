package com.bix.image_processor.image_processing;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, Long> {
}
