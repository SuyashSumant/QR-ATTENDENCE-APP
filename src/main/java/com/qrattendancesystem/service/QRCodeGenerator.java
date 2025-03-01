package com.qrattendancesystem.service;

//import com.google.zxing.BitMatrix;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class QRCodeGenerator {

    public static void generateQRCode(String data, String filePath, int width, int height) {
        try {
            // Generate the QR code matrix
            BitMatrix bitMatrix = new MultiFormatWriter().encode(
                data, BarcodeFormat.QR_CODE, width, height);

            // Define the path to save the QR code image
            Path path = FileSystems.getDefault().getPath(filePath);

            // Write the QR code to an image file
            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

            System.out.println("QR Code Generated Successfully: " + filePath);
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
    }
}
