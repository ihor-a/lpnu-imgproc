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
    @FXML private TextField redValStepPerDiffPixel;

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
        int redValStep = getColorLevel(redValStepPerDiffPixel);
        if (diffThresholdVal == -1 || redValStep == -1) {
            return;
        }

        buffImage3 = this.imageProcessingService.compareImagesByIntensity(buffImage1,
                buffImage2,
                imageView3,
                mainLabel,
                diffThresholdVal,
                redValStep
        );
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

        this.imageFileService.save(buffImage3);
    }

    @FXML
    protected void onUploadButtonClick(ActionEvent event) throws IOException {
        if (event.getSource() == uploadButton1) {
            buffImage1 = this.imageFileService.upload(imageView1, mainLabel);
        } else {
            buffImage2 = this.imageFileService.upload(imageView2, mainLabel);
        }
    }

    @FXML
    protected void onGrayscaleButtonClick(ActionEvent event) throws IOException {
        if (event.getSource() == grayscaleButton1) {
            this.imageProcessingService.makeGrayscale(buffImage1, imageView1, mainLabel);
        } else {
            this.imageProcessingService.makeGrayscale(buffImage2, imageView2, mainLabel);
        }
    }
}