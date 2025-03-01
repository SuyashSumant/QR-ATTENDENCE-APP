package com.qrattendancesystem.service;

public class Main {
    public static void main(String[] args) {
        // Data to be stored in the QR code (e.g., user ID, timestamp, etc.)
        String qrData = "Username: Pratik, UID:107, Timestamp: 2024-02-26";

        // Path where the QR code image will be saved
        String filePath = "qr_code.png";

        // QR code width and height
        int width = 300;
        int height = 300;

        // Generate the QR code
        QRCodeGenerator.generateQRCode(qrData, filePath, width, height);
    }
}
