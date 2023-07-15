package com.upo.wmdsp.components;

import com.upo.wmdsp.components.methods.DestructionMethod;
import com.upo.wmdsp.components.methods.ReconstructionMethod;

public class Result {
    private String filename;
    private Integer numVertices;
    private Integer numEdges;
    private double kWeight;
    private int maxIterations;
    private double removeVerticesPercentage;
    private int rxrSize = 0;
    private double rxrTime = 0;
    private int rxeSize = 0;
    private double rxeTime = 0;
    private int rxcoSize = 0;
    private double rxcoTime = 0;
    private int rxcmSize = 0;
    private double rxcmTime = 0;
    private int exrSize = 0;
    private double exrTime = 0;
    private int exeSize = 0;
    private double exeTime = 0;
    private int excoSize = 0;
    private double excoTime = 0;
    private int excmSize = 0;
    private double excmTime = 0;
    private int coxrSize = 0;
    private double coxrTime = 0;
    private int coxeSize = 0;
    private double coxeTime = 0;
    private int coxcoSize = 0;
    private double coxcoTime = 0;
    private int coxcmSize = 0;
    private double coxcmTime = 0;
    private int cmxrSize = 0;
    private double cmxrTime = 0;
    private int cmxeSize = 0;
    private double cmxeTime = 0;
    private int cmxcoSize = 0;
    private double cmxcoTime = 0;
    private int cmxcmSize = 0;
    private double cmxcmTime = 0;
    
    public Result(String filename, Integer numVertices, Integer numEdges, double kWeight, int maxIterations, double removeVerticesPercentage) {
        this.filename = filename;
        this.numVertices = numVertices;
        this.numEdges = numEdges;
        this.kWeight = kWeight;
        this.maxIterations = maxIterations;
        this.removeVerticesPercentage = removeVerticesPercentage;
    }

    public void setValues(int size, double solution, DestructionMethod destructionMethod, ReconstructionMethod reconstructionMethod) {
        if (DestructionMethod.RANDOM_BASED_DESTRUCTION == destructionMethod) {
            if (ReconstructionMethod.RANDOM_BASED_RECONSTRUCTION == reconstructionMethod) {
                rxrSize = size;
                rxrTime = solution;
            } else if (ReconstructionMethod.EDGE_BASED_RECONSTRUCTION == reconstructionMethod) {
                rxeSize = size;
                rxeTime = solution;
            } else if (ReconstructionMethod.CONTRIBUTION_BASED_RECONSTRUCTION == reconstructionMethod) {
                rxcoSize = size;
                rxcoTime = solution;
            } else if (ReconstructionMethod.COMPLETE_BASED_RECONSTRUCTION == reconstructionMethod) {
                rxcmSize = size;
                rxcmTime = solution;
            }
        }
        if (DestructionMethod.EDGE_BASED_DESTRUCTION == destructionMethod) {
            if (ReconstructionMethod.RANDOM_BASED_RECONSTRUCTION == reconstructionMethod) {
                exrSize = size;
                exrTime = solution;
            } else if (ReconstructionMethod.EDGE_BASED_RECONSTRUCTION == reconstructionMethod) {
                exeSize = size;
                exeTime = solution;
            } else if (ReconstructionMethod.CONTRIBUTION_BASED_RECONSTRUCTION == reconstructionMethod) {
                excoSize = size;
                excoTime = solution;
            } else if (ReconstructionMethod.COMPLETE_BASED_RECONSTRUCTION == reconstructionMethod) {
                excmSize = size;
                excmTime = solution;
            }
        }
        if (DestructionMethod.CONTRIBUTION_BASED_DESTRUCTION == destructionMethod) {
            if (ReconstructionMethod.RANDOM_BASED_RECONSTRUCTION == reconstructionMethod) {
                coxrSize = size;
                coxrTime = solution;
            } else if (ReconstructionMethod.EDGE_BASED_RECONSTRUCTION == reconstructionMethod) {
                coxeSize = size;
                coxeTime = solution;
            } else if (ReconstructionMethod.CONTRIBUTION_BASED_RECONSTRUCTION == reconstructionMethod) {
                coxcoSize = size;
                coxcoTime = solution;
            } else if (ReconstructionMethod.COMPLETE_BASED_RECONSTRUCTION == reconstructionMethod) {
                coxcmSize = size;
                coxcmTime = solution;
            }
        }
        if (DestructionMethod.COMPLETE_BASED_DESTRUCTION == destructionMethod) {
            if (ReconstructionMethod.RANDOM_BASED_RECONSTRUCTION == reconstructionMethod) {
                cmxrSize = size;
                cmxrTime = solution;
            } else if (ReconstructionMethod.EDGE_BASED_RECONSTRUCTION == reconstructionMethod) {
                cmxeSize = size;
                cmxeTime = solution;
            } else if (ReconstructionMethod.CONTRIBUTION_BASED_RECONSTRUCTION == reconstructionMethod) {
                cmxcoSize = size;
                cmxcoTime = solution;
            } else if (ReconstructionMethod.COMPLETE_BASED_RECONSTRUCTION == reconstructionMethod) {
                cmxcmSize = size;
                cmxcmTime = solution;
            }
        }
    }

    public String exportToCSV() {
        return filename + ";" + numVertices + ";" + numEdges + ";" + kWeight + ";" + maxIterations + ";" + removeVerticesPercentage + ";" + rxrSize + ";" + rxrTime + ";" + rxeSize + ";" + rxeTime + ";" + rxcoSize + ";" + rxcoTime + ";" + rxcmSize + ";" + rxcmTime + ";" + exrSize + ";" + exrTime + ";" + exeSize + ";" + exeTime + ";" + excoSize + ";" + excoTime + ";" + excmSize + ";" + excmTime + ";" + coxrSize + ";" + coxrTime + ";" + coxeSize + ";" + coxeTime + ";" + coxcoSize + ";" + coxcoTime + ";" + coxcmSize + ";" + coxcmTime + ";" + cmxrSize + ";" + cmxrTime + ";" + cmxeSize + ";" + cmxeTime + ";" + cmxcoSize + ";" + cmxcoTime + ";" + cmxcmSize + ";" + cmxcmTime;
    }
}
