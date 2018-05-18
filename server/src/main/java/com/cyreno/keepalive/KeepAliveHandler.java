package com.cyreno.keepalive;

import com.cyreno.availability.AvailabilityService;
import org.springframework.stereotype.Service;

@Service
public class KeepAliveHandler {

    private final AvailabilityService availabilityService;

    public KeepAliveHandler(AvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    void handleMessage(KeepAliveMessage message) {
        try {
            availabilityService.saveNewOrExtendAvailability(message.getRestaurantId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
