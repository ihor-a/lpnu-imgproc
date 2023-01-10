package com.example.imgproc;

import com.example.imgproc.service.ImageFileService;
import com.example.imgproc.service.ImageProcessingService;

public interface ImageFileController {
    void setImageFileService(ImageFileService imageFileService);

    void setImageProcessingService(ImageProcessingService imageProcessingService);
}
