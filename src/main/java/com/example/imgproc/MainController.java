package com.example.imgproc;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import service.ImageFileService;
import service.ImageProcessingService;

import java.awt.image.BufferedImage;
import java.io.IOException;

import static java.lang.Integer.parseInt;

public class MainController implements ImageFileController {
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
    @FXML private TextField diffColorStepPerPixel;

    private BufferedImage buffImage1;
    private BufferedImage buffImage2;
    private BufferedImage buffImage3;

    @Override
    public void setImageFileService(ImageFileService imageFileService) {
        this.imageFileService = imageFileService;
    }

    @Override
    public void setImageProcessingService(ImageProcessingService imageProcessingService) {
        this.imageProcessingService = imageProcessingService;
    }

    private ImageFileService imageFileService;
    private ImageProcessingService imageProcessingService;

    public void initialize() {
        // Allow only numeric values
        diffThreshold.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                diffThreshold.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        diffColorStepPerPixel.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                diffColorStepPerPixel.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    @FXML
    protected void onExecuteButtonClick() {
        if (buffImage1 == null) {
            mainLabel.setText("Please upload Image 1");
            return;
        }
        if (buffImage2 == null) {
            mainLabel.setText("Please upload Image 2");
            return;
        }

        int diffThresholdVal = getColorLevel(diffThreshold);
        int diffColorStep = getColorLevel(diffColorStepPerPixel);
        if (diffThresholdVal == -1 || diffColorStep == -1) {
            return;
        }

        try {
            buffImage3 = this.imageProcessingService.compareImagesByIntensity(buffImage1,
                    buffImage2,
                    imageView3,
                    mainLabel,
                    diffThresholdVal,
                    diffColorStep
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    protected void onSaveButtonClick() {
        if (buffImage3 == null) {
            mainLabel.setText("Please execute processing first");
            return;
        }

        try {
            this.imageFileService.save(buffImage3);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onUploadButtonClick(ActionEvent event) {
        BufferedImage bufferedImage;
        try {
            if (event.getSource() == uploadButton1) {
                bufferedImage = this.imageFileService.upload(imageView1, mainLabel);
                buffImage1 = (bufferedImage == null) ? buffImage1 : bufferedImage;
            } else {
                bufferedImage = this.imageFileService.upload(imageView2, mainLabel);
                buffImage2 = (bufferedImage == null) ? buffImage2 : bufferedImage;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onGrayscaleButtonClick(ActionEvent event) {
        try {
            if (event.getSource() == grayscaleButton1) {
                this.imageProcessingService.makeGrayscale(buffImage1, imageView1, mainLabel);
            } else {
                this.imageProcessingService.makeGrayscale(buffImage2, imageView2, mainLabel);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}