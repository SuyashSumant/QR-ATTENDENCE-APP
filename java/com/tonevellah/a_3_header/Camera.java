package com.tonevellah.a_3_header;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Camera extends JFrame {

    //Camera Screen
    private JLabel cameraScreen;
    private JButton btnCapture;
    private VideoCapture capture;
    private Mat image;
    private boolean clicked = false;

    public Camera(){
        // design ui
        cameraScreen = new JLabel();
        cameraScreen.setBounds(5,35,50,50);

        add(cameraScreen);

//        btnCapture = new JButton("Take a Picture");
//        btnCapture.setBounds(10,610,150,300); // Adjusted button position
//        add(btnCapture);
//
//        btnCapture.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                clicked = true;
//            }
//        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                if (capture != null && capture.isOpened()) {
                    capture.release();
                }
                if (image != null && !image.empty()) {
                    image.release();
                }
                System.exit(0);
            }
        });

        setSize(new Dimension(150,150)); // Adjusted window size
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    //Create Camera
    public void startCamera(){
        capture = new VideoCapture(0);
        image = new Mat();
        byte [] imageData;
        ImageIcon icon;

        if (!capture.isOpened()) {
            JOptionPane.showMessageDialog(this, "Cannot open camera!", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        while(true){
            //read image to matrix
            if (capture.read(image)) { // Check if frame was read successfully
                //convert matrix to byte;
                MatOfByte buf = new MatOfByte();
                Imgcodecs.imencode(".jpg", image, buf);

                imageData = buf.toArray();
                //add to JLabel
                icon = new ImageIcon(imageData);
                cameraScreen.setIcon(icon);
                //capture and save to file
                if(clicked){
                    //prompt for enter image name
                    String name = JOptionPane.showInputDialog("Enter image name");
                    if(name == null){
                        name = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
                    }
                    //write to file
                    Imgcodecs.imwrite("images/" + name + ".jpg", image);
                    clicked = false;
                }
            } else {
                JOptionPane.showMessageDialog(this, "Could not read frame from camera.", "Camera Error", JOptionPane.ERROR_MESSAGE);
                break; // Exit the loop if frame cannot be read
            }
        }
        capture.release();
        image.release();
    }

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Camera camera = new Camera();
                // start camera in the thread
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        camera.startCamera();
                    }
                }).start();
            }
        });
    }
}
