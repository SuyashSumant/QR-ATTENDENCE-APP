package com.example.demo;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class loginController {
    @FXML
    private Label dateLabel;
    @FXML
    private Label timeLabel;

    private final DateTimeFormatter dateTimeFormatter=DateTimeFormatter.ofPattern("dd-MMMM-yyyy");
    private final DateTimeFormatter TimeFormatter=DateTimeFormatter.ofPattern("hh:mm a");

    public void initialize(){
        startClock();
    }
    private void startClock(){
        Timeline timeline =new Timeline(
                new KeyFrame(Duration.seconds(1),event->{
                    LocalDateTime now= LocalDateTime.now();
                    dateLabel.setText("Date:"+ now.format(dateTimeFormatter));
                    timeLabel.setText("Time:"+now.format(TimeFormatter));
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

}


