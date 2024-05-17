package com.example.hackerton.service;

import com.example.hackerton.entity.MountainEntity;
import com.example.hackerton.repository.MountainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MountainService {

    private final MountainRepository mountainRepository;

    public MountainService(MountainRepository mountainRepository){
        this.mountainRepository = mountainRepository;
    }

    public List<MountainEntity> getMountains(){
        return mountainRepository.findAll();
    }


    public List<MountainEntity> findNearestMountains(double latitude, double longitude) {
        List<MountainEntity> mountains = mountainRepository.findAll();
        return mountains.stream()
                .sorted(Comparator.comparingDouble(m -> distance(latitude, longitude, m.getLatitude(), m.getLongitude())))
                .limit(10)
                .collect(Collectors.toList());
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth in km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c; // convert to kilometers
        return distance;
    }

}
