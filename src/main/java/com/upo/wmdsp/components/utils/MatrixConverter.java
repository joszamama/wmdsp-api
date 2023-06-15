package com.upo.wmdsp.components.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class MatrixConverter {
    
    public static void fileToOutputFile(String inputFilename, String outputFilename) {
        try (Scanner scanner = new Scanner(new File("./src/main/resources/graphs/newfresh/" + inputFilename));
                PrintWriter writer = new PrintWriter(
                        new File("./src/main/resources/graphs/newfresh/" + outputFilename))) {

            scanner.nextLine();
            scanner.nextLine();

            String firstLine = scanner.nextLine();
            int nodes = firstLine.split(" ").length;

            // Populate the adjacency matrix with values from the file
            double[][] adjMatrix = new double[nodes][nodes];
            adjMatrix[0] = parseLine(firstLine);
            for (int i = 1; i < nodes; i++) {
                String line = scanner.nextLine();
                adjMatrix[i] = parseLine(line);
            }

            // Initialize modifiedPositions to an empty HashMap
            Map<String, Double> modifiedPositions = new HashMap<>();

            // Write the result to the output file
            for (int i = 0; i < nodes; i++) {
                for (int j = 0; j < nodes; j++) {
                    if (adjMatrix[i][j] == 1 && !modifiedPositions.containsKey(i + "," + j)) {
                        double randomValue = getRandomValue();
                        writer.print(randomValue + " ");
                        // set the corresponding value at (j, i) to the same random value
                        adjMatrix[j][i] = randomValue;

                        // add both positions to the modifiedPositions HashMap
                        String position1 = i + "," + j;
                        String position2 = j + "," + i;
                        modifiedPositions.put(position1, randomValue);
                        modifiedPositions.put(position2, randomValue);
                    } else {
                        writer.print("0.0 ");
                    }
                }
                writer.println();
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + inputFilename);
            e.printStackTrace();
        }

        File file = new File("./src/main/resources/graphs/newfresh/" + inputFilename);
        file.delete();
    }

    // Parses a line of values and returns a boolean array
    private static double[] parseLine(String line) {
        String[] parts = line.split(" ");
        double[] values = new double[parts.length];
        for (int i = 0; i < parts.length; i++) {
            values[i] = Double.parseDouble(parts[i]);
        }
        return values;
    }

    // Returns a random value between 0.01 and 1
    private static double getRandomValue() {
        Random random = new Random();
        double value = 0.1 + random.nextDouble() * 0.9;
        return Math.round(value * 10.0) / 10.0;
    }

    public static void main(String[] args) {
        File folder = new File("./src/main/resources/graphs/newfresh/");
        File[] listOfFiles = folder.listFiles();

        for (File file : listOfFiles) {
            if (file.isFile()) {
                String filename = file.getName();
                String outputFilename = "w_" + filename;
                fileToOutputFile(filename, outputFilename);
            }
        }
    }
}
