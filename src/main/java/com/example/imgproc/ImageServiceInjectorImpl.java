package com.example.imgproc;

import service.ImageFileServiceImpl;
import service.ImageProcessingServiceImpl;

public class ImageServiceInjectorImpl implements ImageServiceInjector {
    @Override
    public void setServices(ImageFileController imageFileController) {
        imageFileController.setImageFileService(new ImageFileServiceImpl());
        imageFileController.setImageProcessingService(new ImageProcessingServiceImpl());
    }
}
