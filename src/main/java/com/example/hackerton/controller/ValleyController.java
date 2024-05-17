package com.example.hackerton.controller;


import com.example.hackerton.entity.MountainEntity;
import com.example.hackerton.entity.ValleyEntity;
import com.example.hackerton.service.ValleyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/valley")

public class ValleyController {

    @Autowired
    private ValleyService valleyService;


    @GetMapping()
    public List<ValleyEntity> getAllMountains(){
        return valleyService.getValley();

    }


    @GetMapping("/nearest")
    public List<ValleyEntity> getNearestMountains(@RequestParam double latitude, @RequestParam double longitude) {
        return valleyService.findNearestValleys(latitude, longitude);
    }



}
