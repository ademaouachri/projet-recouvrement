package com.example.backend.Controller;

import com.example.backend.Model.Parameter;
import com.example.backend.Service.ParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/parameters")
//
public class ParameterRestController {

    @Autowired
    private ParameterService parameterService;

    @GetMapping
    public List<Parameter> getAll() {
        return parameterService.getAll();
    }

    @PostMapping("/update")
    public Parameter update(@RequestBody Parameter parameter) {
        return parameterService.updateParam(parameter);
    }
}