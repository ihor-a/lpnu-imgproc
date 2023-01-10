package com.example.imgproc.service;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface ImageProcessingService {
    void makeGrayscale(BufferedImage bufferedImage, ImageView imageView, Label label) throws IOException;

    BufferedImage compareImagesByIntensity(BufferedImage bufferedImage1,
                                  BufferedImage bufferedImage2,
                                  ImageView imageViewRes,
                                  Label label,
                                  int diffThresholdVal,
                                  int diffColorStep) throws IOException;
}
