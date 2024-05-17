package com.example.hackerton.dto.address;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Meta {
    @JsonProperty("is_end")
    private boolean isEnd;
    @JsonProperty("pageable_count")
    private int pageableCount;
    @JsonProperty("total_count")
    private int totalCount;

    // Getters and Setters
}
