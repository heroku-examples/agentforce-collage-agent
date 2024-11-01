package com.herokudevrel.agentforce.collageagent.services;

import com.herokudevrel.agentforce.collageagent.domain.Booking;
import com.herokudevrel.agentforce.collageagent.domain.Experience;
import com.herokudevrel.agentforce.collageagent.repositories.BookingRepository;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Collage API", description = "Generates Collages")
@RestController
@RequestMapping("/api/")
public class CollageService {

    private static final Logger logger = LoggerFactory.getLogger(CollageService.class);

    @Autowired
    private BookingRepository bookingRepository;

    @PostMapping("/generate")
    public CollageResponse generate(@RequestBody CollageRequest request, HttpServletRequest httpServletRequest) {

        logger.info("Received contact Id " + request.contactId);
        CollageResponse response = new CollageResponse();
        try {

            // Retrieve bookings for the given contact
            List<Booking> bookings = bookingRepository.findBookingsByContactExternalId(request.contactId);

            // Map reduce the experiences the contact booked by the experience URL (experience picture)
            Set<String> urls =
                bookings.stream()
                        .map(booking -> booking.getSession().getExperience()) // Get the Experience from each Booking
                        .filter(experience -> experience != null && experience.getPictureUrl() != null) // Filter out null experiences or URLs
                        .map(Experience::getPictureUrl) // Map to the picture URL
                        .collect(Collectors.toSet()); // Collect into a Set for uniqueness

            // Generate a unique filename for the image to download
            String guid = UUID.randomUUID().toString();
            String filePath = "./downloads/" + guid + ".png";
            File outputFile = new File(filePath);

            // Generate a simple collage of the experiences
            BufferedImage collage = createCollage(urls, 10, 20, 500, 20, 20, "Each day you made new discoveries and enriched your soul", "Oh my goodness those sunsets!");
            boolean success = ImageIO.write(collage, "png", outputFile);
            logger.info("Collage " + (success ? "" : "not ") + "saved at: " + outputFile.getAbsolutePath());

            // Calculate the fully qualified URL to return to the client to allow it to download the image
            String fullUrl = String.format("%s://%s:%d/downloads/%s.png",
                    httpServletRequest.getScheme(),
                    httpServletRequest.getServerName(),
                    httpServletRequest.getServerPort(),
                    guid);
            response.downloadUrl = fullUrl;
            return response;

        } catch (IOException e) {
            logger.error("Failed to create collage: {}", e.getMessage());
            response.downloadUrl = "error:" + e.getMessage();
        }
        return response;
    }

    public static class CollageRequest {
        @Schema(example = "Contact.001")
        public String contactId;
    }

    public static class CollageResponse {
        public String downloadUrl;
    }

    public static BufferedImage createCollage(Set<String> imageUrls, int borderSize, int padding, int overlap, int innerMargin, int edgeThickness, String customMessage, String quote) throws IOException {
        // Load images from URLs
        List<BufferedImage> images = new ArrayList<>();
        for (String url : imageUrls) {
            try {
                images.add(ImageIO.read(new URL(url)));
            } catch (IOException e) {
                System.err.println("Failed to load image from URL: " + url);
                // Optionally continue loading the rest instead of throwing an exception
            }
        }

        if (images.isEmpty()) {
            throw new IOException("No valid images were loaded.");
        }

        // Define postcard size (1024 x 768 pixels)
        int postcardWidth = 1024;
        int postcardHeight = 768;

        // Define border frame size
        int totalWidthWithFrame = postcardWidth + 2 * borderSize + 2 * edgeThickness;
        int totalHeightWithFrame = postcardHeight + 2 * borderSize + 2 * edgeThickness;

        // Resize images to fit within the border and margin
        List<BufferedImage> resizedImages = new ArrayList<>();
        for (BufferedImage img : images) {
            Image scaledImage = img.getScaledInstance(postcardWidth, postcardHeight, Image.SCALE_SMOOTH);
            BufferedImage bufferedScaledImage = new BufferedImage(postcardWidth, postcardHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D bGr = bufferedScaledImage.createGraphics();
            bGr.drawImage(scaledImage, 0, 0, null);
            bGr.dispose();
            resizedImages.add(bufferedScaledImage);
        }

        // Calculate the canvas width (with overlap)
        int collageWidth = totalWidthWithFrame * (images.size()) - (overlap * (images.size() - 1)) + 2 * padding;
        int collageHeight = totalHeightWithFrame + 2 * padding;

        // Create a blank canvas with a light gray background
        BufferedImage collage = new BufferedImage(collageWidth, collageHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = collage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, collageWidth, collageHeight);

        // Draw each image with a border frame and inner margin, rotating the entire block
        int xPosition = padding; // Initial x position
        Random rand = new Random();
        for (BufferedImage img : resizedImages) {
            int rotationAngle = rand.nextInt(21) - 10; // Random rotation between -10 and 10 degrees

            // Create an off-screen image to apply the border and rotation
            BufferedImage framedImage = new BufferedImage(totalWidthWithFrame, totalHeightWithFrame, BufferedImage.TYPE_INT_ARGB);
            Graphics2D fg = framedImage.createGraphics();
            fg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw the black border (outer edge)
            fg.setColor(Color.BLACK);
            fg.fillRect(0, 0, totalWidthWithFrame, totalHeightWithFrame);

            // Draw the white border frame inside the black edge
            fg.setColor(Color.WHITE);
            fg.fillRect(edgeThickness, edgeThickness, totalWidthWithFrame - 2 * edgeThickness, totalHeightWithFrame - 2 * edgeThickness);

            // Draw the image inside the border frame with an inner margin
            fg.drawImage(img, borderSize + edgeThickness + innerMargin, borderSize + edgeThickness + innerMargin,
                    postcardWidth - 2 * innerMargin, postcardHeight - 2 * innerMargin, null);

            fg.dispose();

            // Apply rotation and draw the framed image onto the collage
            g.rotate(Math.toRadians(rotationAngle), xPosition + totalWidthWithFrame / 2, padding + totalHeightWithFrame / 2);
            g.drawImage(framedImage, xPosition, padding, null);
            g.rotate(Math.toRadians(-rotationAngle), xPosition + totalWidthWithFrame / 2, padding + totalHeightWithFrame / 2);

            // Move to the next x position with overlap
            xPosition += totalWidthWithFrame - overlap;
        }

        // Read the logo from classpath
        BufferedImage logo = ImageIO.read(ClassLoader.getSystemResource("coralcloudrestorts.png"));

        // Set fonts for the quote and the custom message
        Font quoteFont = new Font("Serif", Font.ITALIC, 42);
        Font messageFont = new Font("SansSerif", Font.PLAIN, 16);
        g.setFont(quoteFont);
        FontMetrics quoteMetrics = g.getFontMetrics(quoteFont);
        int quoteWidth = quoteMetrics.stringWidth("\"" + quote + "\"");
        int quoteHeight = quoteMetrics.getHeight();

        g.setFont(messageFont);
        FontMetrics messageMetrics = g.getFontMetrics(messageFont);
        int messageWidth = messageMetrics.stringWidth(customMessage);
        int messageHeight = messageMetrics.getHeight();

        // Calculate dimensions for the white box based on the quote, message, and logo size
        int boxPadding = 30;
        int boxWidth = Math.max(Math.max(quoteWidth, messageWidth), logo.getWidth()) + 2 * boxPadding;
        int boxHeight = quoteHeight + logo.getHeight() + messageHeight + 4 * boxPadding;

        // Center the white box with a drop shadow
        int boxX = (collageWidth - boxWidth) / 2;
        int boxY = (collageHeight - boxHeight) / 2;
        g.setColor(new Color(0, 0, 0, 150)); // Semi-transparent black for the shadow
        g.fillRoundRect(boxX + 5, boxY + 5, boxWidth, boxHeight, 15, 15); // Drop shadow offset by 5 pixels

        g.setColor(Color.WHITE);
        g.fillRoundRect(boxX, boxY, boxWidth, boxHeight, 15, 15);

        // Draw the quote centered at the top of the white box
        int quoteX = boxX + (boxWidth - quoteWidth) / 2;
        int quoteY = boxY + boxPadding + quoteMetrics.getAscent();
        g.setFont(quoteFont);
        g.setColor(Color.BLACK);
        g.drawString("\"" + quote + "\"", quoteX, quoteY);

        // Draw the logo centered below the quote
        int logoX = boxX + (boxWidth - logo.getWidth()) / 2;
        int logoY = quoteY + quoteHeight / 2 + boxPadding;
        g.drawImage(logo, logoX, logoY, null);

        // Draw the custom message centered below the logo
        int textX = boxX + (boxWidth - messageWidth) / 2;
        int textY = logoY + logo.getHeight() + messageMetrics.getAscent() + boxPadding;
        g.setFont(messageFont);
        g.drawString(customMessage, textX, textY);

        // Dispose of the graphics context
        g.dispose();
        return collage;
    }
}