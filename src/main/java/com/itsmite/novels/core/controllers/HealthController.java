package com.itsmite.novels.core.controllers;

import com.itsmite.novels.core.annotations.JsonRequestMapping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static com.itsmite.novels.core.constants.EndpointConstants.API_HEALTH_V1_ENDPOINT;
import static javax.ws.rs.core.MediaType.WILDCARD;

@Slf4j
@RestController
@RequestMapping(value = API_HEALTH_V1_ENDPOINT)
public class HealthController {

    @JsonRequestMapping(path = "", method = RequestMethod.GET, consumes= {WILDCARD})
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> isServiceRunning() {
        return new HashMap<>(){{
            put("health", "ok");
        }};
    }
}
