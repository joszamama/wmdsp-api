package com.upo.wmdsp.components.phases;

import java.util.HashSet;
import java.util.Set;

import com.upo.wmdsp.components.Graph;
import com.upo.wmdsp.components.methods.InsertionMethod;

public class Insertion {

    public static Set<Integer> getInitialSolution(Graph graph, InsertionMethod initialSolutionMethod) {
        Set<Integer> initialSolution = new HashSet<>(graph.getSupportVertices());
        while (!graph.isDominatingSet(initialSolution)) {
            switch (initialSolutionMethod) {
                case RANDOM_BASED_INSERTION:
                    initialSolution.add(graph.getRandomVertex(initialSolution));
                    break;
                case EDGE_BASED_INSERTION:
                    initialSolution.add(graph.getNextBestEdgeBasedVertex(initialSolution));
                    break;
                case CONTRIBUTION_BASED_INSERTION:
                    initialSolution.add(graph.getNextBestContributionBasedVertex(initialSolution));
                    break;
                case COMPLETE_BASED_INSERTION:
                    initialSolution.add(graph.getNextBestCompleteBasedVertex(initialSolution));
                    break;
                default:
                    initialSolution.add(graph.getRandomVertex());
            }
        }
        return graph.removeRedundantVertices(initialSolution);
    }

}