package com.upo.wmdsp.components.methods;

public class WheelMethod {

    private final DestructionMethod destructionMethod;
    private final ReconstructionMethod reconstructionMethod;

    public WheelMethod(DestructionMethod destructionMethod,
            ReconstructionMethod reconstructionMethod) {
        this.destructionMethod = destructionMethod;
        this.reconstructionMethod = reconstructionMethod;
    }

    public DestructionMethod getDestructionMethod() {
        return destructionMethod;
    }

    public ReconstructionMethod getReconstructionMethod() {
        return reconstructionMethod;
    }

    @Override
    public String toString() {
        return destructionMethod + "_WITH_" + reconstructionMethod;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof WheelMethod) {
            WheelMethod other = (WheelMethod) obj;
            return this.destructionMethod.equals(other.destructionMethod)
                    && this.reconstructionMethod.equals(other.reconstructionMethod);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.destructionMethod.hashCode() + this.reconstructionMethod.hashCode();
    }

}
