package com.ironhack.ironbank.controller;

import com.ironhack.ironbank.dto.ThirdPartyDTO;
import com.ironhack.ironbank.service.ThirdPartyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/third-parties")
@RequiredArgsConstructor
public class ThirdPartyController {

    private final ThirdPartyService thirdPartyService;

    @GetMapping
    public ResponseEntity<List<ThirdPartyDTO>> findAll() {
        return ResponseEntity.ok(thirdPartyService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ThirdPartyDTO> findById(@PathVariable @Valid Long id) {
        return ResponseEntity.ok(thirdPartyService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ThirdPartyDTO> create(@RequestBody @Valid ThirdPartyDTO thirdPartyDTO) {
        return ResponseEntity.ok(thirdPartyService.create(thirdPartyDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ThirdPartyDTO> update(@PathVariable @Valid Long id, @RequestBody @Valid ThirdPartyDTO thirdPartyDTO) {
        return ResponseEntity.ok(thirdPartyService.update(id, thirdPartyDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Valid Long id) {
        thirdPartyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
