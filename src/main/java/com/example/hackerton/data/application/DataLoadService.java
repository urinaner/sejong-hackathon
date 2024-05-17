package com.example.hackerton.data.application;


import com.example.hackerton.dto.ResponseDTO;
import com.example.hackerton.dto.address.RootDto;
import com.example.hackerton.entity.MountainEntity;
import com.example.hackerton.entity.ValleyEntity;
import com.example.hackerton.repository.MountainRepository;
import com.example.hackerton.repository.ValleyRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.*;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class DataLoadService {

    @Autowired
    private MountainRepository mountainRepository;

    @Autowired
    private ValleyRepository valleyRepository;

    String address;
    double longtitude;
    double latitude;



    public void getMountain() {
        ObjectMapper mapper = new ObjectMapper();

        try {
            ClassPathResource resource = new ClassPathResource("mountain_test.json");
            JsonNode root = mapper.readTree(resource.getInputStream()); // JSON 데이터를 JsonNode로 읽음

            for (JsonNode node : root) { //돌면서 저장
                MountainEntity mountain = new MountainEntity();
                mountain.setMntnNm(node.get("MNTN_NM").asText());
                mountain.setMntnLocplcRegionNm(node.get("MNTN_LOCPLC_REGION_NM").asText());
                address = node.get("MNTN_LOCPLC_REGION_NM").asText();
                longtitude = generateCoordinate(address).getLongitude();
                latitude = generateCoordinate(address).getLatitude();
                mountain.setLongitude(longtitude);
                mountain.setLatitude(latitude);


                mountainRepository.save(mountain);
            }
        } catch (IOException e) {
            System.out.print(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseDTO generateCoordinate(String address) throws UnsupportedEncodingException { //주소 -> 위,경도 생성
        ResponseEntity<RootDto> responseEntity = requestCoordinate("6697ce651492e186db0ea6d0c9dc850a", address); //요청 api, 주소

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            System.out.print(responseEntity.getBody());
            RootDto rootDto = responseEntity.getBody();
            if(rootDto.isDocumentsEmpty()){ //검색이 되지않는다면..
                ResponseDTO responseDTO = new ResponseDTO(128.53, 37.51);
                return responseDTO;
            }
            ResponseDTO responseDTO = new ResponseDTO(rootDto.getDocuments().get(0).getX(), rootDto.getDocuments().get(0).getY()); //추가
            return responseDTO;
        }

        return null;
    }

    private ResponseEntity<RootDto> requestCoordinate(String apiKey, String address) throws UnsupportedEncodingException {
        String encodedAddress = URLEncoder.encode(address, "UTF-8");

        String apiUrl = "https://dapi.kakao.com/v2/local/search/address.json";
        URI uri = UriComponentsBuilder //url생성
                .fromUriString(apiUrl)
                .queryParam("query", address)
                .build()
                .encode()
                .toUri();

        HttpHeaders headers = new HttpHeaders(); //헤더 생성
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "KakaoAK " + apiKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<RootDto> response = restTemplate.exchange(uri, HttpMethod.GET, entity, RootDto.class); //url + 헤더 + get요청 후 Dto변환
        return response;
        }


    public void saveValleysFromJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        ClassPathResource resource = new ClassPathResource("livers_test.json");
        JsonNode jsonNode = mapper.readTree(resource.getInputStream()); // JSON 데이터를 JsonNode로 읽음
//        JsonNode valleyListNode = jsonNode.path("result").path("place").path("list");
        JsonNode valleyListNode = jsonNode;


        List<ValleyEntity> valleyEntities = new ArrayList<>();

        for (JsonNode valleyNode : valleyListNode) {
            ValleyEntity valley = new ValleyEntity();
            valley.setName(valleyNode.path("name").asText());
            valley.setLatitude(valleyNode.path("y").asDouble());
            valley.setLongitude(valleyNode.path("x").asDouble());

            valleyEntities.add(valley);
        }


        valleyRepository.saveAll(valleyEntities);
    }


}
