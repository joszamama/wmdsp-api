package com.upo.wmdsp.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.upo.wmdsp.components.IG;
import com.upo.wmdsp.components.Result;
import com.upo.wmdsp.components.methods.InsertionMethod;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.springframework.http.MediaType;

@RestController
@RequestMapping("/api/v1/wmdsp/wheel")
public class WheelController {

    @PostMapping(value = "/compute", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String computeAlgorithm(@RequestParam("file") MultipartFile file,
            @RequestParam("MAX_ITERATIONS") int maxIterations,
            @RequestParam("K_WEIGHT") double weight,
            @RequestParam("REMOVE_VERTICES_PERCENTAGE") double REMOVE_VERTICES_PERCENTAGE,
            @RequestParam("WHEEL_ITERATIONS") int wheelIterations,
            @RequestParam("initialSolution") String insertionMethodString) {

        try {
            // Save the uploaded file to the configured directory
            String fileName = file.getOriginalFilename();
            Path filePath = Path.of("./src/main/resources/graphs/execution/" + fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            InsertionMethod insertionMethod = InsertionMethod.valueOf(insertionMethodString);

            // Create a new IG object with the provided parameters
            IG ig = new IG("execution/" + fileName, weight, maxIterations,
                    REMOVE_VERTICES_PERCENTAGE, wheelIterations, insertionMethod, null, null);

            // Run the algorithm and get the result
            Result result = ig.runWheel();

            // Return the result as a string
            return result.wheelToString();
        } catch (IOException e) {
            e.printStackTrace();
            return "An error occurred during computation.";
        }
    }
}
