package com.upo.wmdsp.components.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.List;

public class MatrixCreator {

    public static void main(String[] args) {
        // Create a list with 100, 200 and 300 vertices
        List<Integer> vertices = List.of(100, 200, 300, 400);
        List<Double> densities = List.of(0.2, 0.3, 0.4, 0.5);

        for (int numOfVertices : vertices) {
            for (double densityPercentage : densities) {
                int j = 1;
                for (int i = 0; i < 4; i++) {
                    int[][] adjacencyMatrix = generateAdjacencyMatrix(numOfVertices, densityPercentage);
                    exportAdjacencyMatrixToFile(adjacencyMatrix, "./src/main/resources/graphs/newfresh/" + "lit_graph_"
                            + numOfVertices + "_" + (int) (densityPercentage * 100) + "_" + (j) + ".txt");
                    j++;
                }
            }
        }

    }

    public static int[][] generateAdjacencyMatrix(int numOfVertices, double densityPercentage) {
        int[][] adjacencyMatrix = new int[numOfVertices][numOfVertices];
        Random random = new Random();

        for (int i = 0; i < numOfVertices; i++) {
            for (int j = i + 1; j < numOfVertices; j++) {
                if (random.nextDouble() < densityPercentage) {
                    adjacencyMatrix[i][j] = 1;
                    adjacencyMatrix[j][i] = 1;
                }
            }
        }

        return adjacencyMatrix;
    }

    public static void exportAdjacencyMatrixToFile(int[][] adjacencyMatrix, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            int numOfVertices = adjacencyMatrix.length;

            for (int i = 0; i < numOfVertices; i++) {
                for (int j = 0; j < numOfVertices; j++) {
                    writer.write(adjacencyMatrix[i][j] + " ");
                }
                writer.write(System.lineSeparator());
            }

            System.out.println("Adjacency matrix exported to " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
