package com.example.application.views.camera;

import org.vaadin.vcamera.VCamera;

/**
 * 
 * @author caelu
 *
 *	This extends the default class of VCamera 
 *	This extends the feature of accessing the Rear camera instead of default Front one.
 */
public class RearCameraVCamera extends VCamera{
	  @Override
	    public void openCamera() {
	        super.openCamera("{ video: { facingMode: 'environment' } }");
	    }
}
