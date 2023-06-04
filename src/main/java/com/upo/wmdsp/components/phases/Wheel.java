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

    public void updateWheel(HashMap<WheelMethod, List<Integer>> methodResults, int iterations) {
        double totalGood = 0.0;
        double totalBad = 0.0;

        // Calculate the total number of good and bad results
        for (List<Integer> counts : methodResults.values()) {
            totalGood += counts.get(0);
            totalBad += counts.get(2);
        }

        // Update the wheelMap based on the performance of each method
        for (Map.Entry<WheelMethod, List<Integer>> entry : methodResults.entrySet()) {
            WheelMethod method = entry.getKey();
            List<Integer> counts = entry.getValue();

            double goodPercentage = (totalGood != 0.0) ? counts.get(0) / totalGood : 1.0;
            double badPercentage = counts.get(2) / totalBad;

            double newProbability = wheelMap.get(method) * (1 + goodPercentage) / (1 + badPercentage);
            wheelMap.put(method, newProbability);
        }

        // Recreate the availableMethods list based on the updated probabilities
        availableMethods.clear();
        for (Map.Entry<WheelMethod, Double> entry : wheelMap.entrySet()) {
            WheelMethod method = entry.getKey();
            double probability = entry.getValue();

            int repetitions = Math.max(1, (int) (iterations * probability)); // Minimum 1 repetition
            for (int i = 0; i < repetitions; i++) {
                availableMethods.add(method);
            }
        }

        // Shuffle the availableMethods list
        Collections.shuffle(availableMethods);
    }

    public WheelMethod pickMethod() {
        if (availableMethods.isEmpty()) {
            return new WheelMethod(DestructionMethod.RANDOM_BASED_DESTRUCTION,
                    ReconstructionMethod.RANDOM_BASED_RECONSTRUCTION);
        }
        return availableMethods.remove(0);
    }
}
