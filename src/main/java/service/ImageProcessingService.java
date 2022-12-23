package service;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface ImageProcessingService {
    void makeGrayscale(BufferedImage buffImage, ImageView imageView, Label label) throws IOException;

    BufferedImage compareImagesByIntensity(BufferedImage buffImage1,
                                  BufferedImage buffImage2,
                                  ImageView imageViewRes,
                                  Label label,
                                  int diffThresholdVal,
                                  int redValStep) throws IOException;
}
