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

    private String CSV_FILE_NAME = "literature-ig.csv";
    private String GRAPH_FOLDER = "literature";

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

    public void startWheel() {
        File folder = new File("./src/main/resources/graphs/" + GRAPH_FOLDER + "/");
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".txt")) {
                    List<Result> results = launchWheel(GRAPH_FOLDER + "/" + file.getName());
                    exportWheel(results);
                }
            }
        } else {
            System.out.println("No files found in the folder");
        }
    }

    public List<Result> launchGreedy(String filename) {
        List<Result> results = new ArrayList<>();

        List<Double> kWeights = List.of(0.1, 0.25, 0.5, 0.75, 1.0);
        List<Integer> maxIterations = List.of(160);
        List<Double> removeVerticesPercentages = List.of(0.2);

        List<InsertionMethod> insertionMethods = List.of(
                InsertionMethod.COMPLETE_BASED_INSERTION);

        List<DestructionMethod> destructionMethods = List.of(
                DestructionMethod.RANDOM_BASED_DESTRUCTION,
                DestructionMethod.EDGE_BASED_DESTRUCTION,
                DestructionMethod.CONTRIBUTION_BASED_DESTRUCTION,
                DestructionMethod.COMPLETE_BASED_DESTRUCTION);

        List<ReconstructionMethod> reconstructionMethods = List.of(
                ReconstructionMethod.RANDOM_BASED_RECONSTRUCTION,
                ReconstructionMethod.EDGE_BASED_RECONSTRUCTION,
                ReconstructionMethod.CONTRIBUTION_BASED_RECONSTRUCTION,
                ReconstructionMethod.COMPLETE_BASED_RECONSTRUCTION);

        for (Double kWeight : kWeights) {
            for (Integer maxIteration : maxIterations) {
                for (Double removeVerticesPercentage : removeVerticesPercentages) {
                    for (InsertionMethod insertionMethod : insertionMethods) {
                        for (DestructionMethod destructionMethod : destructionMethods) {
                            for (ReconstructionMethod reconstructionMethod : reconstructionMethods) {

                                IG ig = new IG(filename, kWeight, maxIteration, removeVerticesPercentage,
                                        0, insertionMethod, destructionMethod, reconstructionMethod);
                                System.out.println("Starting " + ig.toString());
                                        results.add(ig.runGreedy());
                            }
                        }
                    }
                }
            }
        }
        return results;
    }

    public List<Result> launchWheel(String filename) {
        List<Result> results = new ArrayList<>();

        List<Double> kWeights = List.of(0.1, 0.25, 0.5, 0.75, 1.0);
        List<Integer> maxIterations = List.of(160);
        List<Double> removeVerticesPercentages = List.of(0.2);
        List<Integer> wheelIterations = List.of(32);

        InsertionMethod insertionMethod = InsertionMethod.COMPLETE_BASED_INSERTION;

        for (Double kWeight : kWeights) {
            for (Integer maxIteration : maxIterations) {
                for (Double removeVerticesPercentage : removeVerticesPercentages) {
                    for (Integer wheelIteration : wheelIterations) {

                        IG ig = new IG(filename, kWeight, maxIteration, removeVerticesPercentage,
                                wheelIteration, insertionMethod, null, null);
                        System.out.println("Starting " + ig.toString());
                        results.add(ig.runWheel());
                    }
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
                        "filename;numVertices;numEdges;kWeight;maxIterations;removeVerticesPercentage;wheelIterations;insertionMethod;destructionMethod;reconstructionMethod;runtime(ms);size;solution");
            }

            for (Result result : results) {
                writer.println(result.greedyToCSV());
            }
        } catch (IOException e) {
            System.err.println("Error writing results to CSV: " + e.getMessage());
        }
    }

    private void exportWheel(List<Result> results) {
        String CSV_PATH = "./src/main/resources/results/" + CSV_FILE_NAME;
        boolean fileExists = Files.exists(Paths.get(CSV_PATH));

        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(CSV_PATH, true)))) {
            if (!fileExists) {
                writer.println(
                        "filename;numVertices;numEdges;kWeight;maxIterations;removeVerticesPercentage;wheelIterations;runtime(ms);size;solution");
            }

            for (Result result : results) {
                writer.println(result.wheelToCSV());
            }
        } catch (IOException e) {
            System.err.println("Error writing results to CSV: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Launcher launcher = new Launcher("subset-wheel.csv", "subset");
        launcher.startWheel();
    }
}