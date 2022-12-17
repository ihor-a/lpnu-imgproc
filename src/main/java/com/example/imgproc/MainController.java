package com.example.imgproc;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import static java.lang.Integer.parseInt;

public class MainController {
    @FXML
    private Label mainLabel;

    @FXML
    private ImageView imageView1;
    @FXML
    private ImageView imageView2;
    @FXML
    private ImageView imageView3;
    @FXML
    private Button grayscaleButton1;
    @FXML
    private Button uploadButton1;

    @FXML private TextField diffThreshold;
    @FXML private TextField redValStepPerDiffPixel;

    private BufferedImage buffImage1;
    private BufferedImage buffImage2;
    private BufferedImage buffImage3;

    public void initialize() {
        // Allow only numeric values
        diffThreshold.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                diffThreshold.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        redValStepPerDiffPixel.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                redValStepPerDiffPixel.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    @FXML
    protected void onExecuteButtonClick() throws IOException {
        if (buffImage1 == null) {
            mainLabel.setText("Please upload Image 1");
            return;
        }
        if (buffImage2 == null) {
            mainLabel.setText("Please upload Image 2");
            return;
        }

        int diffThresholdVal = getColorLevel(diffThreshold);
        if (diffThresholdVal == -1) {
            return;
        }
        int redValStep = getColorLevel(redValStepPerDiffPixel);
        if (redValStep == -1) {
            return;
        }

        buffImage3 = new BufferedImage(buffImage1.getWidth(), buffImage1.getHeight(), BufferedImage.TYPE_INT_RGB);
        int diffIntensity, redVal;
        int diffPixels = 0, totalPixels = 0, cropPixels = 0;
        int minDiffIntensity = Integer.MAX_VALUE, maxDiffIntensity = 0, sumDiffIntensity = 0;
        Color destColor;

        for (int x = 0; x < buffImage1.getWidth(); x++) {
            for (int y = 0; y < buffImage1.getHeight(); y++) {
                Color color1 = new Color(buffImage1.getRGB(x, y));

                // Images overlap
                if (x < buffImage2.getWidth() && y < buffImage2.getHeight()) {
                    Color color2 = new Color(buffImage2.getRGB(x, y));
                    diffIntensity =
                            Math.abs(color1.getRed() - color2.getRed()) +
                                    Math.abs(color1.getGreen() - color2.getGreen()) +
                                    Math.abs(color1.getBlue() - color2.getBlue())
                    ;
                    if (diffIntensity >= diffThresholdVal) {
                        redVal = diffIntensity * redValStep;
                        destColor = new Color(Math.min(redVal, 255), 0, 0);
                        buffImage3.setRGB(x, y, destColor.getRGB());

                        // Stats
                        minDiffIntensity = Math.min(diffIntensity, minDiffIntensity);
                        maxDiffIntensity = Math.max(diffIntensity, maxDiffIntensity);
                        sumDiffIntensity += diffIntensity;
                        diffPixels++;
                    } else {
                        buffImage3.setRGB(x, y, color1.getRGB());
                    }

                } else {
                    // Image 1 is cropped by image 2
                    destColor = new Color(0, 0, color1.getBlue());
                    buffImage3.setRGB(x, y, destColor.getRGB());
                    cropPixels++;
                }

                totalPixels++;
            }
        }

        // Display stats
        mainLabel.setText(
                String.format("Image size:   %d x %d\n", buffImage1.getWidth(), buffImage1.getHeight()) +
                String.format("Intensity diff min / avg / max:   %d / %d / %d\n",
                        diffPixels > 0 ? minDiffIntensity : 0,
                        diffPixels > 0 ? sumDiffIntensity/diffPixels : 0,
                        maxDiffIntensity
                ) +
                String.format("Pixels diff / cropped / total:   %d / %d / %d", diffPixels, cropPixels, totalPixels)
        );

        setImage(buffImage3, imageView3);
    }

    private int getColorLevel(TextField textField) {
        int result;
        if (textField.getText().equals("")
                || (result = parseInt(textField.getText())) < 0
                || result > 255
        ) {
            textField.setText("");
            mainLabel.setText("Threshold range 0-255");
            return -1;
        }
        return result;
    }

    @FXML
    protected void onSaveButtonClick() throws IOException {
        if (buffImage3 == null) {
            mainLabel.setText("Please execute processing first");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File in png format");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PNG Files", "*.png")
        );
        File selectedFile = fileChooser.showSaveDialog(null);

        if (selectedFile != null) {
            ImageIO.write(buffImage3, "png", selectedFile);
        }
    }

    @FXML
    protected void onUploadButtonClick(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose File. Available formats: png, jpg, bmp, tiff");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.bmp", "*.tiff")
        );
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            if (event.getSource() == uploadButton1) {
                buffImage1 = procUploadImage(selectedFile, imageView1);
            } else {
                buffImage2 = procUploadImage(selectedFile, imageView2);
            }
        }
    }

    private BufferedImage procUploadImage(File selectedFile, ImageView imageView) throws IOException {
        BufferedImage buffImage = ImageIO.read(selectedFile);
        setImage(buffImage, imageView);
        mainLabel.setText("");
        return buffImage;
    }

    @FXML
    protected void onGrayscaleButtonClick(ActionEvent event) throws IOException {
        if (event.getSource() == grayscaleButton1) {
            makeGrayscale(buffImage1, imageView1);
        } else {
            makeGrayscale(buffImage2, imageView2);
        }
    }

    private void makeGrayscale(BufferedImage buffImage, ImageView imageView) throws IOException {
        if (buffImage == null) {
            mainLabel.setText("Please upload the image");
            return;
        }

        for (int x = 0; x < buffImage.getWidth(); x++) {
            for (int y = 0; y < buffImage.getHeight(); y++) {
                Color origColor = new Color(buffImage.getRGB(x, y));
                int grayRed = (int) (origColor.getRed() * 0.299);
                int grayGreen = (int) (origColor.getGreen() * 0.587);
                int grayBlue = (int) (origColor.getBlue() * 0.114);
                Color grayColor = new Color(
                        grayRed + grayGreen + grayBlue,
                        grayRed + grayGreen + grayBlue,
                        grayRed + grayGreen + grayBlue
                );
                buffImage.setRGB(x, y, grayColor.getRGB());
//                System.out.printf("%d, %d, %d%n", grayColor.getRed(), grayColor.getGreen(), grayColor.getBlue());
            }
        }
        System.out.println();

        setImage(buffImage, imageView);
    }

    private void setImage(BufferedImage buffImage, ImageView imageView) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        ImageIO.write(buffImage, "png", outStream);
        imageView.setImage(
                new Image(new ByteArrayInputStream(outStream.toByteArray()))
        );
    }
}