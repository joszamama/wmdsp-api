package com.upo.wmdsp.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.upo.wmdsp.components.IG;
import com.upo.wmdsp.components.Result;
import com.upo.wmdsp.components.methods.DestructionMethod;
import com.upo.wmdsp.components.methods.InsertionMethod;
import com.upo.wmdsp.components.methods.ReconstructionMethod;

@RestController
@RequestMapping("/api/v1/wmdsp/greedy")
public class IGController {

    @PostMapping(value = "/compute", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String computeAlgorithm(@RequestParam("file") MultipartFile file,
            @RequestParam("MAX_ITERATIONS") int maxIterations,
            @RequestParam("K_WEIGHT") double weight,
            @RequestParam("REMOVE_VERTICES_PERCENTAGE") double REMOVE_VERTICES_PERCENTAGE,
            @RequestParam("insertionMethod") String insertionMethodString,
            @RequestParam("destructionMethod") String destructionMethodString,
            @RequestParam("reconstructionMethod") String reconstructionMethodString) {

        try {
            // Save the uploaded file to the configured directory
            String fileName = file.getOriginalFilename();
            Path filePath = Path.of("./src/main/resources/graphs/execution/" + fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Convert destructionMethodString and reconstructionMethodString to enum values
            InsertionMethod insertionMethod = InsertionMethod.valueOf(insertionMethodString);
            DestructionMethod destructionMethod = DestructionMethod.valueOf(destructionMethodString);
            ReconstructionMethod reconstructionMethod = ReconstructionMethod.valueOf(reconstructionMethodString);

            // Create a new IG object with the provided parameters
            IG ig = new IG("execution/" + fileName, weight, maxIterations,
                    REMOVE_VERTICES_PERCENTAGE, 0, insertionMethod, destructionMethod, reconstructionMethod);

            // Run the algorithm and get the result
            Result result = ig.runGreedy();

            // Return the result as a string
            return result.greedyToString();
        } catch (IOException e) {
            e.printStackTrace();
            return "An error occurred during computation.";
        }
    }
}