package com.qrattendancesystem.controller;  

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/vpn")
public class VpnController {

    private static final String VPN_CHECK_URL = "http://ip-api.com/json/";

    @GetMapping("/detect")
    public String detectVPN(HttpServletRequest request) {
        String ipAddress = request.getRemoteAddr();  // Get the user's IP address
        String apiUrl = VPN_CHECK_URL + ipAddress;

        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> response = restTemplate.getForObject(apiUrl, Map.class);

        if (response != null && response.containsKey("proxy")) {
            boolean isProxy = Boolean.TRUE.equals(response.get("proxy"));
            if (isProxy) {
                return "VPN detected, attendance denied.";
            }
        }
        return "No VPN detected, attendance allowed.";
    }
}
