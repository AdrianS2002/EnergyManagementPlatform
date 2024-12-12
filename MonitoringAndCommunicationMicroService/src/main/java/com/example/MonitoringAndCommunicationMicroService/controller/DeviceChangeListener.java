package com.example.MonitoringAndCommunicationMicroService.controller;

import com.example.MonitoringAndCommunicationMicroService.model.Device;
import com.example.MonitoringAndCommunicationMicroService.repository.DeviceRepository;
import com.example.MonitoringAndCommunicationMicroService.config.DeviceChangeEvent;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceChangeListener {

    @Autowired
    private DeviceRepository deviceRepository;

    @RabbitListener(queues = "deviceChangesQueue")
    public void handleDeviceChangeEvent(DeviceChangeEvent event) {
        try {
            switch (event.getOperation().toLowerCase()) {
                case "create":
                    Device newDevice = new Device(event.getDeviceId(), event.getHourConsumption(), event.getUserId());
                    deviceRepository.save(newDevice);
                    System.out.println("Device created: " + newDevice);
                    break;

                case "update":
                    Device existingDevice = deviceRepository.findById(event.getDeviceId())
                            .orElseThrow(() -> new RuntimeException("Device not found"));
                    existingDevice.setHourConsumption(event.getHourConsumption());
                    deviceRepository.save(existingDevice);
                    System.out.println("Device updated: " + existingDevice);
                    break;

                case "delete":
                    deviceRepository.deleteById(event.getDeviceId());
                    System.out.println("Device deleted: " + event.getDeviceId());
                    break;

                default:
                    System.err.println("Unknown operation: " + event.getOperation());
            }
        } catch (Exception e) {
            System.err.println("Error processing device change event: " + e.getMessage());
            e.printStackTrace();
        }
    }
}