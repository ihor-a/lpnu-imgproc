package service;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageProcessingServiceImpl extends BaseImageService implements ImageProcessingService {
    @Override
    public BufferedImage compareImagesByIntensity(BufferedImage bufferedImage1,
                                         BufferedImage bufferedImage2,
                                         ImageView imageViewRes,
                                         Label label,
                                         int diffThresholdVal,
                                         int redValStep) throws IOException {

        BufferedImage bufferedImageRes = new BufferedImage(bufferedImage1.getWidth(), bufferedImage1.getHeight(), BufferedImage.TYPE_INT_RGB);
        int diffIntensity, redVal;
        int diffPixels = 0, totalPixels = 0, cropPixels = 0;
        int minDiffIntensity = Integer.MAX_VALUE, maxDiffIntensity = 0, sumDiffIntensity = 0;
        Color destColor;

        for (int x = 0; x < bufferedImage1.getWidth(); x++) {
            for (int y = 0; y < bufferedImage1.getHeight(); y++) {
                Color color1 = new Color(bufferedImage1.getRGB(x, y));

                // Images overlap
                if (x < bufferedImage2.getWidth() && y < bufferedImage2.getHeight()) {
                    Color color2 = new Color(bufferedImage2.getRGB(x, y));
                    diffIntensity =
                            Math.abs((color1.getRed() + color1.getGreen() + color1.getBlue())/3 -
                                    (color2.getRed() + color2.getGreen() + color2.getBlue())/3
                            );
                    if (diffIntensity >= diffThresholdVal) {
                        redVal = diffIntensity * redValStep;
                        destColor = new Color(Math.min(redVal, 255), 0, 0);
                        bufferedImageRes.setRGB(x, y, destColor.getRGB());

                        // Stats
                        minDiffIntensity = Math.min(diffIntensity, minDiffIntensity);
                        maxDiffIntensity = Math.max(diffIntensity, maxDiffIntensity);
                        sumDiffIntensity += diffIntensity;
                        diffPixels++;
                    } else {
                        bufferedImageRes.setRGB(x, y, color1.getRGB());
                    }

                } else {
                    // Image 1 is cropped by image 2
                    destColor = new Color(0, 0, color1.getBlue());
                    bufferedImageRes.setRGB(x, y, destColor.getRGB());
                    cropPixels++;
                }

                totalPixels++;
            }
        }

        // Display stats
        label.setText(
                String.format("Image size:   %d x %d\n", bufferedImage1.getWidth(), bufferedImage1.getHeight()) +
                        String.format("Intensity diff min / avg / max:   %d / %d / %d\n",
                                diffPixels > 0 ? minDiffIntensity : 0,
                                diffPixels > 0 ? sumDiffIntensity/diffPixels : 0,
                                maxDiffIntensity
                        ) +
                        String.format("Pixels diff / cropped / total:   %d / %d / %d", diffPixels, cropPixels, totalPixels)
        );

        setImage(bufferedImageRes, imageViewRes);
        return bufferedImageRes;
    }

    @Override
    public void makeGrayscale(BufferedImage bufferedImage, ImageView imageView, Label label) throws IOException {
        if (bufferedImage == null) {
            label.setText("Please upload the image");
            return;
        }

        for (int x = 0; x < bufferedImage.getWidth(); x++) {
            for (int y = 0; y < bufferedImage.getHeight(); y++) {
                Color origColor = new Color(bufferedImage.getRGB(x, y));
                int grayRed = (int) (origColor.getRed() * 0.299);
                int grayGreen = (int) (origColor.getGreen() * 0.587);
                int grayBlue = (int) (origColor.getBlue() * 0.114);
                Color grayColor = new Color(
                        grayRed + grayGreen + grayBlue,
                        grayRed + grayGreen + grayBlue,
                        grayRed + grayGreen + grayBlue
                );
                bufferedImage.setRGB(x, y, grayColor.getRGB());
            }
        }

        setImage(bufferedImage, imageView);
    }
}
