package com.example.hackerton.controller;

import com.example.hackerton.entity.MountainEntity;
import com.example.hackerton.repository.MountainRepository;
import com.example.hackerton.service.MountainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/mountain")
public class MountainController {
    private final MountainService mountainService;

    public MountainController(MountainService mountainService){
        this.mountainService = mountainService;
    }

    @GetMapping()
    public List<MountainEntity> getAllMountains(){
        return mountainService.getMountains();

    }


    @GetMapping("/nearest")
    public List<MountainEntity> getNearestMountains(@RequestParam double latitude, @RequestParam double longitude) {
        return mountainService.findNearestMountains(latitude, longitude);
    }

}
