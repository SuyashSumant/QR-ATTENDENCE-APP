package com.qrattendancesystem.controller;  

import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/wifi")
public class WifiController {

    // Allowed Wi-Fi SSIDs (Hardcoded for now; can be stored in DB)
    private static final List<String> ALLOWED_SSIDS = Arrays.asList("Office_WiFi", "Company_Network");

    @PostMapping("/validate")
    public String validateWiFi(@RequestParam String ssid) {
        if (ALLOWED_SSIDS.contains(ssid)) {
            return "Wi-Fi is valid, attendance allowed.";
        } else {
            return "Invalid Wi-Fi, attendance denied.";
        }
    }
}
