package com.upo.wmdsp.components;

import java.util.Set;

import com.upo.wmdsp.components.methods.DestructionMethod;
import com.upo.wmdsp.components.methods.InsertionMethod;
import com.upo.wmdsp.components.methods.ReconstructionMethod;

public class Result {
    private String filename;
    private Integer numVertices;
    private Integer numEdges;
    private double kWeight;
    private int maxIterations;
    private double removeVerticesPercentage;
    private int wheelIterations;
    private InsertionMethod insertionMethod;
    private DestructionMethod destructionMethod;
    private ReconstructionMethod reconstructionMethod;
    private long runtime;
    private int size;
    private Set<Integer> solution;

    public Result(String filename, Integer numVertices, Integer numEdges, double kWeight, int maxIterations,
            double removeVerticesPercentage, int wheelIterations, InsertionMethod insertionMethod,
            DestructionMethod destructionMethod, ReconstructionMethod reconstructionMethod, long runtime, int size, Set<Integer> solution) {
        this.filename = filename;
        this.numVertices = numVertices;
        this.numEdges = numEdges;
        this.kWeight = kWeight;
        this.maxIterations = maxIterations;
        this.removeVerticesPercentage = removeVerticesPercentage;
        this.wheelIterations = wheelIterations;
        this.insertionMethod = insertionMethod;
        this.destructionMethod = destructionMethod;
        this.reconstructionMethod = reconstructionMethod;
        this.runtime = runtime;
        this.size = size;
        this.solution = solution;
    }

    public String greedyToString() {
        return "The W-MDSP solution for graph " + filename + ": {V=" + numVertices + ", E=" + numEdges + "} with kWeight " + kWeight
                + " and settings: {maxIterations:" + maxIterations + ", removeVerticesPercentage:"
                + removeVerticesPercentage + "}, using " + insertionMethod + ", " + destructionMethod + " and "
                + reconstructionMethod + " has a size of " + size + " and a runtime of " + runtime + " ms.";
    }

    public String greedyToCSV() {
        return filename + "," + numVertices + "," + numEdges + "," + kWeight + "," + maxIterations + "," + removeVerticesPercentage + ","
                + insertionMethod + "," + destructionMethod + "," + reconstructionMethod + "," + runtime + "," + size + "," + solution;
    }

    public String wheelToString() {
        return "The W-MDSP solution for graph " + filename + ": {V=" + numVertices + ", E=" + numEdges + "} with kWeight " + kWeight
                + " and settings: {maxIterations:" + maxIterations + ", removeVerticesPercentage:"
                + removeVerticesPercentage + ", wheelIterations:" + wheelIterations + "}, "
                + " has a size of " + size + " and a runtime of " + runtime + " ms.";
    }

    public String wheelToCSV() {
        return filename + "," + numVertices + "," + numEdges + "," + kWeight + "," + maxIterations + "," + removeVerticesPercentage + ","
                + wheelIterations + "," + runtime + "," + size + "," + solution;
    }

}
