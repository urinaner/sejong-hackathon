package com.example.hackerton.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class MountainEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mntnNm;
    private String mntnLocplcRegionNm;
    private double latitude;
    private double longitude;

}
