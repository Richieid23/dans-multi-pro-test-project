package com.danspro.testproject.controllers;

import com.danspro.testproject.dto.Response;
import com.danspro.testproject.security.JwtService;
import com.danspro.testproject.services.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/jobs")
public class JobController {
    @Autowired
    private JobService jobService;

    @Autowired
    private JwtService jwtService;

    @GetMapping
    public ResponseEntity<Response> getAllJobs(HttpServletRequest httpServletRequest) {
        Response jwtResp = jwtService.filter(httpServletRequest);
        if (!jwtResp.getRc().equals("000")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.builder().rc("403").message("Unauthorize").build());
        }
        Response response = jobService.getJobList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response> getJobById(HttpServletRequest httpServletRequest, @PathVariable("id") String id) {
        Response jwtResp = jwtService.filter(httpServletRequest);
        if (!jwtResp.getRc().equals("000")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.builder().rc("403").message("Unauthorize").build());
        }
        Response response = jobService.getJobById(id);

        return ResponseEntity.ok(response);
    }
}
