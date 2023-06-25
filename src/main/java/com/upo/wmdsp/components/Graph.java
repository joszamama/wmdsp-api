package com.upo.wmdsp.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import com.upo.wmdsp.components.utils.MatrixReader;

public class Graph {

    private final Double GRAPH_K_WEIGHT;

    private List<HashMap<Integer, Double>> edges = new ArrayList<>();
    private Set<Integer> vertices = new HashSet<>();
    private Set<Integer> supportVertices = new HashSet<>();

    private HashMap<Set<Integer>, Boolean> dominatingCache = new HashMap<>();
    private HashMap<Set<Integer>, Set<Integer>> redundancyCache = new HashMap<>();
    private HashMap<Set<Integer>, HashMap<Integer, Double>> coverageCache = new HashMap<>();

    public Graph(String filename, Double K_VALUE) {
        List<Double>[] weightsList = MatrixReader.matrixToList(MatrixReader.readGraph(filename));
        GRAPH_K_WEIGHT = K_VALUE;

        for (int i = 0; i < weightsList.length; i++) {
            vertices.add(i);
            edges.add(new HashMap<>());
        }

        for (int i = 0; i < weightsList.length; i++) {
            for (int j = i + 1; j < weightsList.length; j++) {
                if (weightsList[i].get(j) != 0.0) {
                    edges.get(i).put(j, weightsList[i].get(j));
                    edges.get(j).put(i, weightsList[i].get(j));
                }
            }
            edges.get(i).put(i, GRAPH_K_WEIGHT); // Set weight of vertex i to 1
        }

        supportVertices.addAll(vertices.stream()
                .filter(v -> edges.get(v).values().stream().mapToDouble(Double::doubleValue).sum() < K_VALUE)
                .collect(Collectors.toSet()));

    }

    public Set<Integer> getVertices() {
        return vertices;
    }

    public Integer getNumVertices() {
        return vertices.size();
    }

    public List<HashMap<Integer, Double>> getEdges() {
        return edges;
    }

    public Integer getNumEdges() {
        return edges.stream().mapToInt(HashMap::size).sum() / 2;
    }

    public HashMap<Integer, Double> getContributionOfVertex(Integer vertex) {
        return edges.get(vertex);
    }

    public Set<Integer> getSupportVertices() {
        return supportVertices;
    }

    public boolean isSupportVertex(Integer vertex) {
        return supportVertices.contains(vertex);
    }

    public Double getEdgeWeight(Integer vertex1, Integer vertex2) {
        return edges.get(vertex1).get(vertex2);
    }

    public Map<Set<Integer>, Boolean> getDominatingCache() {
        return dominatingCache;
    }

    public Map<Set<Integer>, Set<Integer>> getRedundancyCache() {
        return redundancyCache;
    }

    public HashMap<Set<Integer>, HashMap<Integer, Double>> getCoverageCache() {
        return coverageCache;
    }

    public Integer getRandomVertex() {
        Random random = new Random();
        return random.nextInt(vertices.size());
    }

    public Integer getRandomVertex(Set<Integer> dset) {
        if (dset.isEmpty()) {
            return getRandomVertex();
        }
        Random random = new Random();
        List<Integer> list = new ArrayList<>(vertices);
        list.removeAll(dset);
        return list.get(random.nextInt(list.size()));
    }

    public HashMap<Integer, Double> getCoverageMap(Set<Integer> dset) {
        if (coverageCache.containsKey(dset)) {
            return coverageCache.get(dset);
        }

        HashMap<Integer, Double> vertexCoverage = new HashMap<>();
        for (Integer vertex : vertices) {
            vertexCoverage.put(vertex, 0.0);
        }

        for (Integer vertex : dset) {
            HashMap<Integer, Double> vertexContribution = edges.get(vertex);
            for (Integer vertex2 : vertexContribution.keySet()) {
                vertexCoverage.put(vertex2,
                        vertexCoverage.getOrDefault(vertex2, 0.0) + vertexContribution.get(vertex2));
            }
        }

        coverageCache.put(dset, vertexCoverage);
        return vertexCoverage;
    }

    public Set<Integer> getCoveredVertices(Set<Integer> dset) {
        HashMap<Integer, Double> vertexCoverage = getCoverageMap(dset);
        return vertexCoverage.entrySet().stream().filter(e -> e.getValue() >= GRAPH_K_WEIGHT)
                .map(Map.Entry::getKey).collect(Collectors.toSet());

    }

    public Set<Integer> getUncoveredVertices(Set<Integer> dset) {
        HashMap<Integer, Double> vertexCoverage = getCoverageMap(dset);
        Map<Integer, Double> uncoveredVertices = vertexCoverage.entrySet().stream()
                .filter(e -> e.getValue() < GRAPH_K_WEIGHT).collect(Collectors.toMap(Map.Entry::getKey,
                        Map.Entry::getValue));
        return uncoveredVertices.keySet();
    }

    public boolean isDominatingSet(Set<Integer> dset) {
        if (dominatingCache.containsKey(dset)) {
            return dominatingCache.get(dset);
        }

        Set<Integer> verticesCovered = getCoveredVertices(dset);
        boolean isDominatingSet = verticesCovered.size() == vertices.size();

        dominatingCache.put(dset, isDominatingSet);
        return isDominatingSet;
    }

    public Set<Integer> removeRedundantVertices(Set<Integer> dset) {
        if (redundancyCache.containsKey(dset)) {
            return redundancyCache.get(dset);
        }

        Set<Integer> checkerset = new HashSet<>(dset);
        Integer coveredVertices = getCoveredVertices(dset).size();
        for (Integer vertex : dset) {
            checkerset.remove(vertex);
            int coveredVerticesWithoutVertex = getCoveredVertices(checkerset).size();
            if (coveredVerticesWithoutVertex == coveredVertices) {
                Set<Integer> cleanset = new HashSet<>(dset);
                cleanset.remove(vertex);
                redundancyCache.put(dset, cleanset);
                return removeRedundantVertices(cleanset);
            } else {
                checkerset.add(vertex);
                redundancyCache.put(dset, checkerset);
            }
        }

        redundancyCache.put(dset, checkerset);
        return dset;
    }

    public Integer getNextBestEdgeBasedVertex(Set<Integer> vertexset) throws IndexOutOfBoundsException{
        try {
            Integer bestVertex = -1;
            int bestContribution = Integer.MIN_VALUE;

            Set<Integer> verticesToCheck = getUncoveredVertices(vertexset);

            for (Integer vertex : verticesToCheck) {
                int contributionSum = edges.get(vertex).size();
                if (contributionSum > bestContribution
                        || (contributionSum == bestContribution && Math.random() >= 0.5)) {
                    bestVertex = vertex;
                    bestContribution = contributionSum;
                }
            }
            if (vertexset.contains(bestVertex)) {
                return getRandomVertex(vertexset);
            }
            return bestVertex;
        } catch (Exception e) {
            System.out.println("Error: " + e);
            return getRandomVertex(vertexset);
        }
        
    }

    public Integer getNextWorstEdgeBasedVertex(Set<Integer> dset) {
        Integer worstVertex = -1;
        int worstContribution = Integer.MAX_VALUE;

        for (Integer vertex : dset) {
            if (isSupportVertex(vertex)) {
                continue;
            }
            int contributionSum = edges.get(vertex).size();
            if (contributionSum < worstContribution
                    || (contributionSum == worstContribution && Math.random() >= 0.5)) {
                worstVertex = vertex;
                worstContribution = contributionSum;
            }
        }
        return worstVertex;
    }

    public Integer getNextBestContributionBasedVertex(Set<Integer> vertexset) {
        Integer bestVertex = -1;
        Double bestContribution = Double.MIN_VALUE;

        Set<Integer> verticesToCheck = getUncoveredVertices(vertexset);

        for (Integer vertex : verticesToCheck) {
            Double contributionSum = getContributionOfVertex(vertex).values().stream()
                    .mapToDouble(Double::doubleValue).sum();
            if (contributionSum > bestContribution
                    || (contributionSum == bestContribution && Math.random() >= 0.5)) {
                bestVertex = vertex;
                bestContribution = contributionSum;
            }
        }
        if (vertexset.contains(bestVertex)) {
            return getRandomVertex(vertexset);
        }
        return bestVertex;
    }

    public Integer getNextWorstContributionBasedVertex(Set<Integer> dset) {
        Integer worstVertex = -1;
        Double worstContribution = Double.MAX_VALUE;

        for (Integer vertex : dset) {
            if (isSupportVertex(vertex)) {
                continue;
            }
            Double contributionSum = getContributionOfVertex(vertex).values().stream()
                    .mapToDouble(Double::doubleValue).sum();

            if (contributionSum < worstContribution
                    || (contributionSum == worstContribution && Math.random() >= 0.5)) {
                worstVertex = vertex;
                worstContribution = contributionSum.doubleValue();
            }
        }
        return worstVertex;
    }

    public Integer getNextBestCompleteBasedVertex(Set<Integer> vertexset) {
        Integer bestVertex = -1;
        int setcoverage = getCoveredVertices(vertexset).size();

        for (Integer vertex : vertices) {
            Set<Integer> temp = new HashSet<>(vertexset);
            temp.add(vertex);
            Integer newsetcoverage = getCoveredVertices(temp).size();

            if (newsetcoverage > setcoverage
                    || (newsetcoverage == setcoverage && Math.random() >= 0.5)) {
                bestVertex = vertex;
                setcoverage = newsetcoverage;
            }
        }
        if (vertexset.contains(bestVertex)) {
            return getRandomVertex(vertexset);
        }
        return bestVertex;

    }

    public Integer getNextWorstCompleteBasedVertex(Set<Integer> dset) {
        Integer worstVertex = -1;
        int setcoverage = getCoveredVertices(dset).size();

        for (Integer vertex : dset) {
            if (supportVertices.contains(vertex)) {
                continue;
            }
            vertices.remove(vertex);
            Integer newsetcoverage = getCoveredVertices(vertices).size();

            if (newsetcoverage < setcoverage
                    || (newsetcoverage == setcoverage && Math.random() >= 0.5)) {
                worstVertex = vertex;
                setcoverage = newsetcoverage;
            }
            vertices.add(vertex);
        }
        return worstVertex;
    }

    public static void main(String[] args) {
        Graph graph = new Graph("literature/w_lesmis.txt", 0.25);

        // add 32, 0, 38, 9, 74, 10, 45, 46, 48, 49, 50, 51, 21, 23, 25, 57
        Set<Integer> dset = new HashSet<>();
        dset.add(32);
        dset.add(0);
        dset.add(38);
        dset.add(9);
        dset.add(74);
        dset.add(10);
        dset.add(45);
        dset.add(46);
        dset.add(48);
        dset.add(49);
        dset.add(50);
        dset.add(51);
        dset.add(21);
        dset.add(23);
        dset.add(25);
        dset.add(57);

        while (true) {
            System.out.println(graph.getNextBestEdgeBasedVertex(dset));
        }
    }
}