package com.upo.wmdsp.components.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class MatrixRandomizer {

    public static double highValue() {
        return Math.round((Math.random() * 0.2 + 0.8) * 10) / 10.0;
    }

    public static double lowValue() {
        return Math.round((Math.random() * 0.2 + 0.1) * 10) / 10.0;
    }

    public static void main(String[] args) throws FileNotFoundException {
        // read all files in folder ./src/main/resources/graphs/subset
        // for each file, read the matrix and generate a random matrix
        // save the random matrix in the same folder with the same name

        File folder = new File("./src/main/resources/graphs/subset/");
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".txt")) {
                    System.out.println(file.getName());
                    // Read the matrix
                    double[][] adjMatrix = MatrixReader.readGraph("/subset/" + file.getName());
                    // for each position, if != 0, set a 50% chance of being 1 and 50% of being 0
                    for (int i = 0; i < adjMatrix.length; i++) {
                        for (int j = 0; j < adjMatrix[i].length; j++) {
                            if (adjMatrix[i][j] != 0) {
                                adjMatrix[i][j] = Math.random() < 0.5 ? lowValue() : highValue();
                            }
                        }
                    }
                    // Save the matrix file to the same folder
                    PrintWriter writer = new PrintWriter(new File("./src/main/resources/graphs/subset/MOD_" + file.getName()));

                    for (int i = 0; i < adjMatrix.length; i++) {
                        for (int j = 0; j < adjMatrix[i].length; j++) {
                            writer.print(adjMatrix[i][j] + " ");
                        }
                        writer.println();
                    }
                    writer.close();
                }
            }
        } else {
            System.out.println("No files found in the folder");
        }
    }
}
