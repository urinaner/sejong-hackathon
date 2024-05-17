package com.example.hackerton.repository;

import com.example.hackerton.entity.MountainEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MountainRepository extends JpaRepository<MountainEntity, Long> {
}
