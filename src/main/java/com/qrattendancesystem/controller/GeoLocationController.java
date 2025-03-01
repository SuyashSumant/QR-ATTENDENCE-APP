package com.qrattendancesystem.controller;  

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/geo") // Updated path to match your API call
public class GeoLocationController {

    // Office coordinates (example: latitude & longitude)
    private static final double OFFICE_LAT = 28.6139;  // Replace with actual latitude
    private static final double OFFICE_LON = 77.2090;  // Replace with actual longitude
    private static final double ALLOWED_RADIUS_METERS = 500;  // 500 meters radius

    @PostMapping("/validate")
    public String validateLocation(@RequestParam double latitude, @RequestParam double longitude) {
        double distance = calculateDistance(OFFICE_LAT, OFFICE_LON, latitude, longitude);
        
        if (distance <= ALLOWED_RADIUS_METERS) {
            return "Location is valid, attendance allowed.";
        } else {
            return "Invalid location, attendance denied.";
        }
    }

    // Method to calculate distance between two coordinates using Haversine formula
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371000; // Earth's radius in meters
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // Distance in meters
    }
}
