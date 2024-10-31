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
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;
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

        // Retrieve bookings for the given contact
        List<Booking> bookings = bookingRepository.findBookingsByContactExternalId(request.contactId);

        // Map reduce the experiences the contact booked by the experience URL (experience picture)
        Set<String> urls =
            bookings.stream()
                    .map(booking -> booking.getSession().getExperience()) // Get the Experience from each Booking
                    .filter(experience -> experience != null && experience.getPictureUrl() != null) // Filter out null experiences or URLs
                    .map(Experience::getPictureUrl) // Map to the picture URL
                    .collect(Collectors.toSet()); // Collect into a Set for uniqueness

        try {

            // Generate a unique filename for the image to download
            String guid = UUID.randomUUID().toString();
            String filePath = "./downloads/" + guid + ".jpg";
            File outputFile = new File(filePath);

            // Generate a simple collage of the experiences
            BufferedImage collage = createCollage(urls, 10, 20);
            ImageIO.write(collage, "jpg", outputFile);
            logger.info("Collage saved at: " + outputFile.getAbsolutePath());

            // Calculate the fully qualified URL to return to the client to allow it to download the image
            String fullUrl = String.format("%s://%s:%d/downloads/%s.jpg",
                    httpServletRequest.getScheme(),
                    httpServletRequest.getServerName(),
                    httpServletRequest.getServerPort(),
                    guid);
            CollageResponse response = new CollageResponse();
            response.downloadUrl = fullUrl;
            return response;

        } catch (IOException e) {
            logger.error("Failed to create collage: {}", e.getMessage());
        }
        CollageResponse response = new CollageResponse();
        response.downloadUrl = "";
        return response;
    }

    public static class CollageRequest {
        @Schema(example = "003Kj00002WDfF8IAL")
        public String contactId;
    }

    public static class CollageResponse {
        public String downloadUrl;
    }

    public static BufferedImage createCollage(Set<String> imageUrls, int borderSize, int padding) throws IOException {
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

