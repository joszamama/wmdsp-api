package com.upo.wmdsp.components;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.upo.wmdsp.components.methods.DestructionMethod;
import com.upo.wmdsp.components.methods.InsertionMethod;
import com.upo.wmdsp.components.methods.ReconstructionMethod;
import com.upo.wmdsp.components.methods.WheelMethod;
import com.upo.wmdsp.components.phases.Destruction;
import com.upo.wmdsp.components.phases.Insertion;
import com.upo.wmdsp.components.phases.Reconstruction;
import com.upo.wmdsp.components.phases.Wheel;

public class IG {

    String filename;

    Double K_WEIGHT;
    Integer MAX_ITERATIONS_WITHOUT_IMPROVEMENT;
    Double REMOVE_VERTICES_PERCENTAGE;
    Integer WHEEL_ITERATIONS;
    Integer MINUTES_TIMEOUT = 3;

    InsertionMethod insertionMethod;
    DestructionMethod destructionMethod;
    ReconstructionMethod reconstructionMethod;

    long runtime;
    Set<Integer> solution;

    public IG(String filename, Double K_WEIGHT, Integer MAX_ITERATIONS_WITHOUT_IMPROVEMENT,
            Double REMOVE_VERTICES_PERCENTAGE, Integer WHEEL_ITERATIONS, InsertionMethod insertionMethod,
            DestructionMethod destructionMethod, ReconstructionMethod reconstructionMethod) {

        this.filename = filename;
        this.K_WEIGHT = K_WEIGHT;

        this.K_WEIGHT = K_WEIGHT;
        this.MAX_ITERATIONS_WITHOUT_IMPROVEMENT = MAX_ITERATIONS_WITHOUT_IMPROVEMENT;
        this.REMOVE_VERTICES_PERCENTAGE = REMOVE_VERTICES_PERCENTAGE;
        this.WHEEL_ITERATIONS = WHEEL_ITERATIONS;

        this.insertionMethod = insertionMethod;
        this.destructionMethod = destructionMethod;
        this.reconstructionMethod = reconstructionMethod;
    }

    public Result runGreedy() {
        Graph graph = new Graph(filename, K_WEIGHT);

        long startTime = System.currentTimeMillis();

        Set<Integer> startingSolution = Insertion.getInitialSolution(graph, insertionMethod);
        Set<Integer> currentBestSolution = new HashSet<>(startingSolution);

        int i = 0;
        while (i < MAX_ITERATIONS_WITHOUT_IMPROVEMENT) {
            Set<Integer> nextSolution = new HashSet<>(currentBestSolution);
            Set<Integer> unfeasableSolution = Destruction.getUnfeasibleSolution(graph, nextSolution,
                    destructionMethod, REMOVE_VERTICES_PERCENTAGE, MINUTES_TIMEOUT, System.currentTimeMillis());
            Set<Integer> feasibleSolution = Reconstruction.getFeasibleSolution(graph, unfeasableSolution,
                    reconstructionMethod, MINUTES_TIMEOUT, System.currentTimeMillis());

            if (feasibleSolution.size() < currentBestSolution.size()) {
                currentBestSolution = new HashSet<>(feasibleSolution);
                i = 0;
            } else {
                i++;
            }

            if (System.currentTimeMillis() - startTime > MINUTES_TIMEOUT * 60 * 1000) {
                System.out.println("Global Timeout reached");
                break;
            }
        }

        long endTime = System.currentTimeMillis();

        this.runtime = (endTime - startTime);
        this.solution = currentBestSolution;

        return new Result(filename, graph.getNumVertices(), graph.getNumEdges(), K_WEIGHT, MAX_ITERATIONS_WITHOUT_IMPROVEMENT, REMOVE_VERTICES_PERCENTAGE,
                0, insertionMethod, destructionMethod, reconstructionMethod, runtime, solution.size(), solution);
    }

    public Result runWheel() {
        Graph graph = new Graph(filename, K_WEIGHT);

        Wheel wheel = new Wheel(graph, WHEEL_ITERATIONS);
        HashMap<WheelMethod, List<Integer>> wheelResults = new HashMap<WheelMethod, List<Integer>>();

        long startTime = System.currentTimeMillis();

        Set<Integer> startingSolution = Insertion.getInitialSolution(graph, insertionMethod);
        Set<Integer> currentBestSolution = new HashSet<>(startingSolution);

        int i = 0;
        int j = 0;
        while (i < MAX_ITERATIONS_WITHOUT_IMPROVEMENT) {
            if (j >= WHEEL_ITERATIONS) {
                wheel.updateWheel(wheelResults, WHEEL_ITERATIONS);
                wheelResults = new HashMap<WheelMethod, List<Integer>>();
                j = 0;
            }

            Set<Integer> nextSolution = new HashSet<>(currentBestSolution);

            WheelMethod wheelMethod = wheel.pickMethod();
            wheelResults.putIfAbsent(wheelMethod, new ArrayList<>(Arrays.asList(0, 0, 0)));

            Set<Integer> unfeasableSolution = Destruction.getUnfeasibleSolution(graph, nextSolution,
                    wheelMethod.getDestructionMethod(), REMOVE_VERTICES_PERCENTAGE, MINUTES_TIMEOUT, System.currentTimeMillis());
            Set<Integer> feasibleSolution = Reconstruction.getFeasibleSolution(graph, unfeasableSolution,
                    wheelMethod.getReconstructionMethod(), MINUTES_TIMEOUT, System.currentTimeMillis());

            if (feasibleSolution.size() < currentBestSolution.size()) {
                wheelResults.get(wheelMethod).set(0, wheelResults.get(wheelMethod).get(0) + 1);
                currentBestSolution = new HashSet<>(feasibleSolution);
                i = 0;
                j++;
            }
            if (feasibleSolution.size() == currentBestSolution.size()) {
                wheelResults.get(wheelMethod).set(1, wheelResults.get(wheelMethod).get(1) + 1);
                i++;
                j++;
            } else {
                wheelResults.get(wheelMethod).set(2, wheelResults.get(wheelMethod).get(2) + 1);
                i++;
                j++;
            }

            if (System.currentTimeMillis() - startTime > MINUTES_TIMEOUT * 60 * 1000) {
                System.out.println("Global timeout reached");
                break;
            }
        }

        long endTime = System.currentTimeMillis();

        this.runtime = (endTime - startTime);
        this.solution = currentBestSolution;

        return new Result(filename, graph.getNumVertices(), graph.getNumEdges(), K_WEIGHT, MAX_ITERATIONS_WITHOUT_IMPROVEMENT, REMOVE_VERTICES_PERCENTAGE,
                WHEEL_ITERATIONS, insertionMethod, destructionMethod, reconstructionMethod, runtime, solution.size(), solution);
    }

    // tostring
    public String toString() {
        return "IG [filename=" + filename + ", K_WEIGHT=" + K_WEIGHT + ", MAX_ITERATIONS_WITHOUT_IMPROVEMENT="
                + MAX_ITERATIONS_WITHOUT_IMPROVEMENT + ", REMOVE_VERTICES_PERCENTAGE=" + REMOVE_VERTICES_PERCENTAGE
                + ", WHEEL_ITERATIONS=" + WHEEL_ITERATIONS + ", MINUTES_TIMEOUT=" + MINUTES_TIMEOUT
                + ", insertionMethod=" + insertionMethod + ", destructionMethod=" + destructionMethod
                + ", reconstructionMethod=" + reconstructionMethod + "]";
    }

    public static void main(String args[]) {
        String file = "subset/MOD_w_rnd_graph_4000_50_1.txt";

        Double K = 0.75;
        double REMOVE_VERTICES_PERCENTAGE = 0.2;
        int MAX_ITERATIONS_WITHOUT_IMPROVEMENT = 50;

        InsertionMethod insertionMethod = InsertionMethod.COMPLETE_BASED_INSERTION;
        DestructionMethod destructionMethod = DestructionMethod.RANDOM_BASED_DESTRUCTION;
        ReconstructionMethod reconstructionMethod = ReconstructionMethod.EDGE_BASED_RECONSTRUCTION;

        IG ig = new IG(file, K, MAX_ITERATIONS_WITHOUT_IMPROVEMENT, REMOVE_VERTICES_PERCENTAGE, 32,
                insertionMethod, destructionMethod, reconstructionMethod);

        System.out.println(ig.runGreedy().greedyToString());
    }
}
