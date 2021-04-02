package org.openrsc.editor.gui;

import org.openrsc.editor.gui.controls.toolbar.ToolSelector;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageUtils {
    public static Image getIconImage(String resource, int height) throws IOException {
        BufferedImage original = ImageIO.read(
                ToolSelector.class.getResourceAsStream(resource)
        );
        double scaleY = height * 1.0 / original.getHeight();
        int targetHeight = (int) (scaleY * original.getHeight());
        int targetWidth = (int) (scaleY * original.getWidth());
        return original.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);
    }
}
