package com.upo.wmdsp.components.utils;

import java.io.File;

public class MatrixMover {

    public static String[] graphs = {
            "w_rnd_graph_1000_30_1.txt",
            "w_rnd_graph_2000_30_3.txt",
            "w_rnd_graph_500_50_1.txt",
            "w_rnd_graph_500_30_3.txt",
            "w_rnd_graph_2000_30_1.txt",
            "w_rnd_graph_500_30_1.txt",
            "w_rnd_graph_500_20_1.txt",
            "w_rnd_graph_500_20_2.txt",
            "w_rnd_graph_500_50_3.txt",
            "w_rnd_graph_500_30_4.txt",
            "w_rnd_graph_1000_20_1.txt",
            "w_rnd_graph_1000_20_2.txt",
            "w_rnd_graph_3000_30_1.txt",
            "w_rnd_graph_1000_40_3.txt",
            "w_rnd_graph_1000_20_3.txt",
            "w_rnd_graph_1000_50_3.txt",
            "w_rnd_graph_500_20_3.txt",
            "w_rnd_graph_2000_50_2.txt",
            "w_rnd_graph_500_40_1.txt",
            "w_rnd_graph_3000_20_4.txt",
            "w_rnd_graph_4000_50_2.txt",
            "w_rnd_graph_2000_40_3.txt",
            "w_rnd_graph_3000_50_2.txt",
            "w_rnd_graph_4000_20_1.txt",
            "w_rnd_graph_500_40_3.txt",
            "w_rnd_graph_4000_30_3.txt",
            "w_rnd_graph_500_30_2.txt",
            "w_rnd_graph_2000_20_1.txt",
            "w_rnd_graph_3000_30_2.txt",
            "w_rnd_graph_3000_40_2.txt",
            "w_rnd_graph_2000_50_1.txt",
            "w_rnd_graph_2000_40_2.txt",
            "w_rnd_graph_1000_40_4.txt",
            "w_rnd_graph_500_40_2.txt",
            "w_rnd_graph_4000_40_1.txt",
            "w_rnd_graph_4000_20_3.txt",
            "w_rnd_graph_2000_40_1.txt",
            "w_rnd_graph_3000_50_3.txt",
            "w_rnd_graph_4000_50_4.txt",
            "w_rnd_graph_3000_40_3.txt",
            "w_rnd_graph_1000_50_2.txt",
            "w_rnd_graph_4000_40_4.txt",
            "w_rnd_graph_500_20_4.txt",
            "w_rnd_graph_4000_30_1.txt",
            "w_rnd_graph_2000_30_2.txt",
            "w_rnd_graph_2000_20_2.txt",
            "w_rnd_graph_3000_50_1.txt",
            "w_rnd_graph_4000_50_3.txt",
            "w_rnd_graph_4000_30_2.txt",
            "w_rnd_graph_1000_50_1.txt",
            "w_rnd_graph_2000_30_4.txt",
            "w_rnd_graph_2000_20_3.txt",
            "w_rnd_graph_2000_50_4.txt",
            "w_rnd_graph_2000_20_4.txt",
            "w_rnd_graph_1000_40_2.txt",
            "w_rnd_graph_3000_30_3.txt",
            "w_rnd_graph_3000_40_1.txt",
            "w_rnd_graph_1000_40_1.txt",
    };

    public static void main(String[] args) {
        moveFiles();
    }

    public static void moveFiles() {
        // move files from folder ./src/main/java/resources/graphs/random to
        // ./src/main/java/resources/graphs/icebox if they are in the list
        for (String graph : graphs) {
            File file = new File("./src/main/resources/graphs/random/" + graph);
            if (file.exists()) {
                System.out.println("Moving file " + graph);
                file.renameTo(new File("./src/main/resources/graphs/icebox/" + graph));
            }
        }
    }

}
