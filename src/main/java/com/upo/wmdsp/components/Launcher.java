package com.upo.wmdsp.components;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.upo.wmdsp.components.methods.DestructionMethod;
import com.upo.wmdsp.components.methods.InsertionMethod;
import com.upo.wmdsp.components.methods.ReconstructionMethod;

public class Launcher {

    private String CSV_FILE_NAME;
    private String GRAPH_FOLDER;

    public Launcher(String CSV_NAME, String GRAPH_FOLDER) {
        this.CSV_FILE_NAME = CSV_NAME;
        this.GRAPH_FOLDER = GRAPH_FOLDER;
    }

    public void startGreedy() {
        File folder = new File("./src/main/resources/graphs/" + GRAPH_FOLDER + "/");
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".txt")) {
                    List<Result> results = launchGreedy(GRAPH_FOLDER + "/" + file.getName());
                    exportGreedy(results);
                }
            }
        } else {
            System.out.println("No files found in the folder");
        }
    }

    public List<Result> launchGreedy(String filename) {
        List<Result> results = new ArrayList<>();

        Graph graph = new Graph(filename, 1.0);

        List<Double> kWeights = List.of(0.1, 0.25, 0.5, 0.75, 1.0);
        List<Integer> maxIterations = List.of(64);
        List<Double> removeVerticesPercentages = List.of(0.2);

        List<InsertionMethod> insertionMethods = List.of(
                InsertionMethod.COMPLETE_BASED_INSERTION);

        List<DestructionMethod> destructionMethods = List.of(
                DestructionMethod.RANDOM_BASED_DESTRUCTION);

        List<ReconstructionMethod> reconstructionMethods = List.of(
                ReconstructionMethod.COMPLETE_BASED_RECONSTRUCTION);

        for (Double kWeight : kWeights) {
            for (Integer maxIteration : maxIterations) {
                for (Double removeVerticesPercentage : removeVerticesPercentages) {
                    Result instance = new Result(filename, graph.getNumVertices(), graph.getNumEdges(), kWeight,
                            maxIteration, removeVerticesPercentage);
                    
                    for (InsertionMethod insertionMethod : insertionMethods) {
                        for (DestructionMethod destructionMethod : destructionMethods) {
                            for (ReconstructionMethod reconstructionMethod : reconstructionMethods) {
                                IG ig = new IG(filename, kWeight, maxIteration, removeVerticesPercentage,
                                        insertionMethod, destructionMethod, reconstructionMethod);
                                System.out.println("Starting " + ig.toString());
                                List<Double> solution = ig.runGreedy();
                                int size = solution.get(0).intValue();
                                double runtime = solution.get(1);                    
                                instance.setValues(size, runtime, destructionMethod, reconstructionMethod);}
                        }
                    }
                    results.add(instance);
                }
            }
        }
        return results;
    }

    private void exportGreedy(List<Result> results) {
        String CSV_PATH = "./src/main/resources/results/" + CSV_FILE_NAME;
        boolean fileExists = Files.exists(Paths.get(CSV_PATH));

        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(CSV_PATH, true)))) {
            if (!fileExists) {
                writer.println(
                        "filename;numVertices;numEdges;kWeight;maxIterations;removeVerticesPercentage;rxrSize;rxrTime;rxeSize;rxeTime;rxcoSize;rxcoTime;rxcmSize;rxcmTime;exrSize;exrTime;exeSize;exeTime;excoSize;excoTime;excmSize;excmTime;coxrSize;coxrTime;coxeSize;coxeTime;coxcoSize;coxcoTime;coxcmSize;coxcmTime;cmxrSize;cmxrTime;cmxeSize;cmxeTime;cmxcoSize;cmxcoTime;cmxcmSize;cmxcmTime");
            }

            for (Result result : results) {
                writer.println(result.exportToCSV());
            }

        } catch (IOException e) {
            System.err.println("Error writing results to CSV: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Launcher launcher = new Launcher("rerun-greedy-subset.csv", "subset");
        launcher.startGreedy();
    }
}