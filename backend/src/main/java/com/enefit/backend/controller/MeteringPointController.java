package com.enefit.backend.controller;


import com.enefit.backend.entity.MeteringPoint;
import com.enefit.backend.service.MeteringPointService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller that handles requests related to a user's metering points.
 */
@RestController
@RequestMapping("/metering-points")
public class MeteringPointController {

    private final MeteringPointService meteringPointService;

    public MeteringPointController(MeteringPointService meteringPointService) {
        this.meteringPointService = meteringPointService;
    }

    /**
     * Returns all metering points associated with the authenticated user.
     */
    @GetMapping
    public ResponseEntity<List<MeteringPoint>> getUserMeteringPoints(Authentication authentication) {
        String username = authentication.getName();
        List<MeteringPoint> points = meteringPointService.getMeteringPointsForUser(username);
        return ResponseEntity.ok(points);
    }
}
