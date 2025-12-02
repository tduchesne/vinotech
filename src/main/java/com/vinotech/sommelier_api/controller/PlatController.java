package com.vinotech.sommelier_api.controller;

import com.vinotech.sommelier_api.model.Plat;
import com.vinotech.sommelier_api.repository.PlatRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/plats")
public class PlatController {

    private final PlatRepository platRepository;

    public PlatController(PlatRepository platRepository) {
        this.platRepository = platRepository;
    }

    @GetMapping
    public List<Plat> getAllPlats() {
        return platRepository.findAll();
    }
}