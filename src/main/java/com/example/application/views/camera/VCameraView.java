package com.example.application.views.camera;


import com.example.application.Application;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.vaadin.vcamera.VCamera;

@Route(value = "")
public class VCameraView extends VerticalLayout {
    private VCamera camera;

    File latest;

    Button takePicture = new Button("Take picture");

    Button onoff = new Button("Close camera");

    Button startRecording = new Button("Start recording");

    Button stopRecording = new Button("Stop recording");

    Div imageContainer = new Div();

    Div videoContainer = new Div();

    public VCameraView() {

        camera = new RearCameraVCamera(); /*here were are using our extended  custom VCamera class to use Rear Camera*/
        camera.setReceiver((String mimeType) -> {
            String suffix;
            if (mimeType.contains("jpeg")) {
                suffix = ".jpeg";
            } else if (mimeType.contains("matroska")) {
                suffix = ".mkv";
            } else {
                suffix = ".mp4";
            }
            if (latest != null) {
//                latest.delete();
            	System.out.println("Leaving the old Images as it is !");
            }
            try {
                latest = File.createTempFile("camera", suffix, Application.imagesDirectory.toFile());
                System.out.println("Streaming to temp file " + latest);
                return new FileOutputStream(latest);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        
        add(camera);
        add(new HorizontalLayout(takePicture, onoff, startRecording, stopRecording));
        add(imageContainer);
        add(videoContainer);

        takePicture.addClickListener(e -> {
            camera.takePicture();
        });

        onoff.addClickListener(e -> {
            if(camera.isCameraOpen()) {
                camera.closeCamera();
                onoff.setText("Open camera");
                takePicture.setEnabled(false);
                startRecording.setEnabled(false);
            } else {
                camera.openCamera();
                onoff.setText("Close camera");
                takePicture.setEnabled(true);
                startRecording.setEnabled(true);
            }
        });

        startRecording.addClickListener(e -> {
            camera.startRecording();
            stopRecording.setEnabled(true);
        });

        stopRecording.setEnabled(false);
        stopRecording.addClickListener(e -> {
            camera.stopRecording();
            stopRecording.setEnabled(false);
        });

        camera.openCamera();
        camera.addFinishedListener(e -> {
            System.out.println("Received image or video to the server side");
            String mime = e.getMime();
            if (mime.contains("image")) {
                setImage();
            } else if (mime.contains("video")) {
                setVideo(e.getMime());
            }
        });

    }

    private void clearImageAndVideo() {
        imageContainer.removeAll();
        videoContainer.removeAll();
    }

    private void setImage() {
        clearImageAndVideo();
        File file = latest;
        if (file != null) {
            InputStreamFactory f = () -> {
                try {
                    return new FileInputStream(file);
                } catch (FileNotFoundException e) {
                }
                return null;
            };
            Image image = new Image(new StreamResource("image", f),
                    "The captured image");
            imageContainer.add(image);
        }
    }

    private void setVideo(String mime) {
        clearImageAndVideo();
        File file = latest;
        if (file != null) {
            VideoComponent videoComponent = new VideoComponent();
            // Spring MVC serves /videos/** from the videos tmp directory
            // See Application.java how it is configured
            videoComponent.setSrc("/videos/" + file.getName());
            imageContainer.add(videoComponent);
        }
    }

}



