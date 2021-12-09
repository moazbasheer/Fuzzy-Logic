import java.util.ArrayList;
import java.util.List;

public class Variable {
    String variableName;
    int variableType;
    ArrayList<FuzzySet> sets;
    private double crispValue;
    
    public Variable(String variableName, int variableType) {
        this.variableName = variableName;
        this.variableType = variableType;
        this.sets = new ArrayList<>();
    }

    public Object getVariableName() {
        return this.variableName;
    }

    public void addFuzzySet(String fuzzySetName, int fuzzySetType, List<Double> boundaries) {
        this.sets.add(new FuzzySet(fuzzySetName, fuzzySetType, boundaries));
    }
    
    public FuzzySet getFuzzySet(String fuzzySetName) {
        for(FuzzySet fuzzySet: this.sets) {
            if(fuzzySet.getFuzzySetName().equals(fuzzySetName)) {
                return fuzzySet;
            }
        }
        return null;
    }

    public void setCrispValue(double val) {
        this.crispValue = val;
    }
    
    public double getCrispValue() {
        return this.crispValue;
    }

    public List<FuzzySet> getFuzzySets() {
        return new ArrayList(this.sets);
    }

    public int getVariableType() {
        return this.variableType;
    }
    
}
