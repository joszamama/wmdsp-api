package com.upo.wmdsp.components.phases;

import java.util.Set;

import com.upo.wmdsp.components.Graph;
import com.upo.wmdsp.components.methods.DestructionMethod;

public class Destruction {

    public static Set<Integer> getUnfeasibleSolution(Graph graph, Set<Integer> dset, DestructionMethod destructionMethod,
            Double REMOVAL_VERTEX_PERCENTAGE, Integer MINUTES_TIMEOUT, long startTime) {
        int verticesToRemove = (int) (dset.size() * REMOVAL_VERTEX_PERCENTAGE);
        for (int i = 0; i < verticesToRemove; i++) {
            if (System.currentTimeMillis() - startTime > MINUTES_TIMEOUT * 60 * 1000) {
                System.out.println("Destruction timeout reached");
                return dset;
            }
            switch (destructionMethod) {
                case RANDOM_BASED_DESTRUCTION:
                    dset.remove(graph.getRandomVertex());
                    break;
                case EDGE_BASED_DESTRUCTION:
                    dset.remove(graph.getNextWorstEdgeBasedVertex(dset));
                    break;
                case CONTRIBUTION_BASED_DESTRUCTION:
                    dset.remove(graph.getNextWorstContributionBasedVertex(dset));
                    break;
                case COMPLETE_BASED_DESTRUCTION:
                    dset.remove(graph.getNextWorstCompleteBasedVertex(dset));
                    break;
                default:
                    dset.remove(graph.getRandomVertex());
            }
        }
        return dset;
    }

}