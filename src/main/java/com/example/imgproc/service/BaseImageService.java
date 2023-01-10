package com.example.imgproc.service;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

abstract class BaseImageService {
    protected void setImage(BufferedImage buffImage, ImageView imageView) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        ImageIO.write(buffImage, "png", outStream);
        imageView.setImage(
                new Image(new ByteArrayInputStream(outStream.toByteArray()))
        );
    }
}
