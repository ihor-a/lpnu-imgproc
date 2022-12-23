package com.example.imgproc;

import service.ImageFileService;
import service.ImageProcessingService;

public interface ImageFileController {
    void setImageFileService(ImageFileService imageFileService);

    void setImageProcessingService(ImageProcessingService imageProcessingService);
}
