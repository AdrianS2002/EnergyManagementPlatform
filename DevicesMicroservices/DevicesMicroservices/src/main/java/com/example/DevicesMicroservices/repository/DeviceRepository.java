package com.example.DevicesMicroservices.repository;

import com.example.DevicesMicroservices.model.Device;
import com.example.DevicesMicroservices.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    public Device findByDeviceId(Long deviceId);
    public List<Device> findAllByUser(User user);

}
