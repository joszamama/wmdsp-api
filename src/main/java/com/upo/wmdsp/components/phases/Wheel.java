package com.upo.wmdsp.components.phases;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.upo.wmdsp.components.Graph;
import com.upo.wmdsp.components.methods.DestructionMethod;
import com.upo.wmdsp.components.methods.ReconstructionMethod;
import com.upo.wmdsp.components.methods.WheelMethod;

public class Wheel {

    private HashMap<WheelMethod, Double> wheelMap = new HashMap<WheelMethod, Double>();
    public List<WheelMethod> availableMethods = new ArrayList<>();

    public Wheel(Graph graph, int iterations) {
        DestructionMethod[] destructionMethods = DestructionMethod.values();
        ReconstructionMethod[] reconstructionMethods = ReconstructionMethod.values();

        int combinations = destructionMethods.length * reconstructionMethods.length;

        for (DestructionMethod destructionMethod : destructionMethods) {
            for (ReconstructionMethod reconstructionMethod : reconstructionMethods) {
                WheelMethod method = new WheelMethod(destructionMethod, reconstructionMethod);
                wheelMap.put(method, 1.0 / combinations);

                // Add the method to availableMethods 'iterations' times
                for (int i = 0; i < (iterations / combinations); i++) {
                    availableMethods.add(method);
                }
            }
        }

        // Shuffle the availableMethods list
        Collections.shuffle(availableMethods);
    }

    public Wheel(HashMap<WheelMethod, List<Integer>> methodResults) {
        availableMethods = new ArrayList<>();
        wheelMap = new HashMap<>();

        double decayFactor = 0.1; // Adjust the decay factor as needed

        for (Map.Entry<WheelMethod, List<Integer>> entry : methodResults.entrySet()) {
            WheelMethod method = entry.getKey();
            List<Integer> results = entry.getValue();

            int goodCount = 0;
            int sameCount = 0;
            int worseCount = 0;

            if (!results.isEmpty()) {
                goodCount = results.get(0);
                sameCount = results.get(1);
                worseCount = results.get(2);
            }

            // Calculate the total count
            int totalCount = goodCount + sameCount + worseCount;

            // Calculate the weight based on the counts
            double weight = (double) goodCount / totalCount;

            // Apply decay to weight if worseCount is non-zero
            if (worseCount > 0) {
                weight *= (1 - decayFactor);
            }

            wheelMap.put(method, weight);

            // Add the method to availableMethods based on counts
            for (int i = 0; i < goodCount; i++) {
                availableMethods.add(method);
            }

            // Remove the method from availableMethods if its weight has decreased to 0
            if (weight == 0) {
                availableMethods.remove(method);
            }
        }

        // Shuffle the availableMethods list
        Collections.shuffle(availableMethods);
    }

    public WheelMethod pickMethod() {
        if (availableMethods.isEmpty()) {
            // Regenerate the availableMethods list if it becomes empty
            availableMethods.addAll(wheelMap.keySet());

            // Shuffle the availableMethods list
            Collections.shuffle(availableMethods);
        }
        // Select the first method from the availableMethods list
        WheelMethod selectedMethod = availableMethods.get(0);

        // Remove the selected method from the availableMethods list
        availableMethods.remove(0);

        // Shuffle the availableMethods list
        Collections.shuffle(availableMethods);

        return selectedMethod;
    }

    @Override
    public String toString() {
        return wheelMap.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Wheel) {
            Wheel other = (Wheel) obj;
            return wheelMap.equals(other.wheelMap);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return wheelMap.hashCode();
    }

}
