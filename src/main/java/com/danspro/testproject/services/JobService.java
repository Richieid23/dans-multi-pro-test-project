package com.danspro.testproject.services;

import com.danspro.testproject.dto.JobResponse;
import com.danspro.testproject.dto.Response;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
public class JobService {
    @Autowired
    private RestTemplate restTemplate;

    public Response getJobList() {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://dev3.dansmultipro.co.id/api/recruitment/positions.json", String.class);
        String jsonString = responseEntity.getBody();

        List<JobResponse> jobResponse = new Gson().fromJson(jsonString, List.class);
        return Response.builder()
                .rc("000")
                .message("success")
                .data(jobResponse)
                .build();
    }

    public Response getJobById(String id) {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://dev3.dansmultipro.co.id/api/recruitment/positions/"+id, String.class);
        String jsonString = responseEntity.getBody();

        JobResponse jobResponse = new Gson().fromJson(jsonString, JobResponse.class);
        return Response.builder()
                .rc("000")
                .message("success")
                .data(jobResponse)
                .build();
    }
}
