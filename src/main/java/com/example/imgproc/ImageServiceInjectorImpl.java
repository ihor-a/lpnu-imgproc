package com.example.imgproc;

import com.example.imgproc.service.ImageFileServiceImpl;
import com.example.imgproc.service.ImageProcessingServiceImpl;

public class ImageServiceInjectorImpl implements ImageServiceInjector {
    @Override
    public void setServices(ImageFileController imageFileController) {
        imageFileController.setImageFileService(new ImageFileServiceImpl());
        imageFileController.setImageProcessingService(new ImageProcessingServiceImpl());
    }
}
