package com.herokudevrel.agentforce.collageagent.services;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;

@Tag(name = "Collage API", description = "Generates Collages")
@RestController
public class CollageService {

    private static final Logger logger = LoggerFactory.getLogger(CollageService.class);

    @PostMapping("/generate")
    public String greetings(@RequestBody CollageRequest request) {

        List<String> urls = List.of(
                "https://s3-us-west-2.amazonaws.com/dev-or-devrl-s3-bucket/sample-apps/coral-clouds/sgxvz18thlsctwwqafec.jpg",
                "https://s3-us-west-2.amazonaws.com/dev-or-devrl-s3-bucket/sample-apps/coral-clouds/sjahfb9mmbzzyogf87fk.jpg",
                "https://s3-us-west-2.amazonaws.com/dev-or-devrl-s3-bucket/sample-apps/coral-clouds/ugpauqyr6k4ykemyumuu.png",
                "https://s3-us-west-2.amazonaws.com/dev-or-devrl-s3-bucket/sample-apps/coral-clouds/mukt3fxxtxz6fgzltiv9.png"
        );
        int borderSize = 10;
        int padding = 20;

        try {
            BufferedImage collage = createCollage(urls, borderSize, padding);
            ImageIO.write(collage, "jpg", new java.io.File("./collage_output.jpg"));
            System.out.println("Collage created successfully!");
        } catch (IOException e) {
            System.err.println("Failed to create collage: " + e.getMessage());
        }

        logger.info("Saying hello to user {}", request.name);
        return """
            Welcome %s to the Matrix""".formatted(request.name);
    }

    public static class CollageRequest {
        @Schema(example = "Neo")
        public String name;
    }

    public static BufferedImage createCollage(List<String> imageUrls, int borderSize, int padding) throws IOException {
        // Load images from URLs
        List<BufferedImage> images = new ArrayList<>();
        for (String url : imageUrls) {
            images.add(ImageIO.read(new URL(url)));
        }

        // Calculate canvas dimensions based on images and spacing
        int collageWidth = 0;
        int collageHeight = 0;
        for (BufferedImage img : images) {
            collageWidth = Math.max(collageWidth, img.getWidth() + 2 * (borderSize + padding));
            collageHeight += img.getHeight() + 2 * (borderSize + padding);
        }

        // Create a blank canvas with a light gray background
        BufferedImage collage = new BufferedImage(collageWidth, collageHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = collage.createGraphics();
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, collageWidth, collageHeight);

        // Draw each image with a white border
        int yPosition = padding; // Initial y position
        for (BufferedImage img : images) {
            int imageWidth = img.getWidth();
            int imageHeight = img.getHeight();

            // Draw the white border
            g.setColor(Color.WHITE);
            g.fillRect(padding, yPosition, imageWidth + 2 * borderSize, imageHeight + 2 * borderSize);

            // Draw the image
            g.drawImage(img, padding + borderSize, yPosition + borderSize, null);

            // Move to the next y position for the next image
            yPosition += imageHeight + 2 * (borderSize + padding);
        }

        // Dispose of the graphics context
        g.dispose();
        return collage;
    }
}

