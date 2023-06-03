package com.upo.wmdsp.components.phases;

import java.util.Set;

import com.upo.wmdsp.components.Graph;
import com.upo.wmdsp.components.methods.ReconstructionMethod;

public class Reconstruction {

    public static Set<Integer> getFeasibleSolution(Graph graph, Set<Integer> dset,
            ReconstructionMethod reconstructionMethod) {
        while (!graph.isDominatingSet(dset)) {
            switch (reconstructionMethod) {
                case RANDOM_BASED_RECONSTRUCTION:
                    dset.add(graph.getRandomVertex());
                    break;
                case EDGE_BASED_RECONSTRUCTION:
                    dset.add(graph.getNextBestEdgeBasedVertex(dset));
                    break;
                case CONTRIBUTION_BASED_RECONSTRUCTION:
                    dset.add(graph.getNextBestContributionBasedVertex(dset));
                    break;
                case COMPLETE_BASED_RECONSTRUCTION:
                    dset.add(graph.getNextBestCompleteBasedVertex(dset));
                    break;
                default:
                    dset.add(graph.getRandomVertex());
            }
        }
        return dset;
    }
}