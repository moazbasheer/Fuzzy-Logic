import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FuzzyLogic {
	List<Variable> variables;
	List<FuzzyRule> rules;
	public FuzzyLogic() {
		this.variables = new ArrayList<>();
		this.rules = new ArrayList<>();
	}
	
	public void addVariable(String variableName, int variableType) {
		variables.add(new Variable(variableName, variableType));
	}
	
	public Variable getVariable(String variableName) {
		for(Variable variable:variables) {
			if(variable.getVariableName().equals(variableName)) {
				return variable;
			}
		}
		return null;
	}
	
	private void addRule(String antecedant, String consequent) {
		this.rules.add(new FuzzyRule(antecedant, consequent));
	}
	
	private void fuzzification() {
		// fuzzification
		System.out.println("Fuzzification\n");
		
		for(Variable var:variables) {
			if(var.getVariableType() == Constants.OUTPUT) continue;
			double crispValue = var.getCrispValue();
			List<FuzzySet> sets = var.getFuzzySets();
			System.out.println("variable name is " + var.getVariableName());
			for(FuzzySet set:sets) {
				List<Double> boundaries = set.getBoundaries();
				List<Double> yBoundaries = null;
				if(set.getFuzzyType() == Constants.TRAP) yBoundaries = Arrays.asList(new Double[]{0.0, 1.0, 1.0, 0.0});
				else yBoundaries = Arrays.asList(new Double[]{0.0, 1.0, 0.0});
				
				for(int i = 0; i < boundaries.size() - 1; i++) {
					double x1 = boundaries.get(i);
					double y1 = yBoundaries.get(i);
					double x2 = boundaries.get(i + 1);
					double y2 = yBoundaries.get(i + 1);
					
					if(x2 - x1 != 0 && crispValue >= x1 && crispValue <= x2) {
						double slope = (y2 - y1) / (x2 - x1);
						double intercept = y2 - slope * x2;
						set.setIntercept(slope * crispValue + intercept);
					}
				}
				System.out.println(set.getFuzzySetName() + ": " + set.getIntercept());
				
			}
		}
		System.out.println("\n");
	}
	
	private void applyInferenceRules() {
		System.out.println("Inference rules\n");
		int counter = 1;
		for(FuzzyRule rule:rules) {
			System.out.println("rule" + counter + ": ");
			
			String consequent = rule.getConsequent();
			String[] arr = consequent.split("=");
			double ruleValue = rule.getAntecedantValue(this);
			this.getVariable(arr[0]).getFuzzySet(arr[1]).setIntercept(ruleValue);
			System.out.println(arr[0] + ", " + arr[1] + "-> " + ruleValue);
			counter ++;
		}
		System.out.println("\n");
	}
	
	private void defuzzification() {
		System.out.println("defuzzification\n");
		
		ArrayList<Double> intercepts = new ArrayList<>();
		ArrayList<Double> centroids = new ArrayList<>();
		double interceptsSum = 0.0;
		for(Variable var:variables) {
			if(var.getVariableType() == Constants.OUTPUT) {
				List<FuzzySet> sets = var.getFuzzySets();
				for(FuzzySet set:sets) {
					intercepts.add(set.getIntercept());
					centroids.add(set.getCentroid());
					interceptsSum += set.getIntercept();
				}
			}
		}
		
		double weightedAverage = 0.0;
		for(int i = 0; i < intercepts.size(); i++) {
			weightedAverage += intercepts.get(i) * centroids.get(i);
		}
		weightedAverage /= interceptsSum;
		System.out.println("predicted value is " + weightedAverage);
		double mx = -1e9;
		String predictedFuzzySet = "";
		for(Variable var:variables) {
			if(var.getVariableType() == Constants.INPUT) continue;
			List<FuzzySet> sets = var.getFuzzySets();
			System.out.println("variable name is " + var.getVariableName());
			for(FuzzySet set:sets) {
				List<Double> boundaries = set.getBoundaries();
				List<Double> yBoundaries = null;
				if(set.getFuzzyType() == Constants.TRAP) yBoundaries = Arrays.asList(new Double[]{0.0, 1.0, 1.0, 0.0});
				else yBoundaries = Arrays.asList(new Double[]{0.0, 1.0, 0.0});
				
				for(int i = 0; i < boundaries.size() - 1; i++) {
					double x1 = boundaries.get(i); double y1 = yBoundaries.get(i);
					double x2 = boundaries.get(i + 1); double y2 = yBoundaries.get(i + 1);
					
					if(x2 - x1 != 0 && weightedAverage >= x1 && weightedAverage <= x2) {
						double slope = (y2 - y1) / (x2 - x1);
						double intercept = y2 - slope * x2;
						set.setIntercept(slope * weightedAverage + intercept);
					}
				}
				System.out.println(set.getFuzzySetName() + ": " + set.getIntercept());
				if(set.getIntercept() > mx) {
					mx = set.getIntercept();
					predictedFuzzySet = set.getFuzzySetName();
				}
			}
		}
		System.out.println("predicted fuzzy set is " + predictedFuzzySet);
		System.out.println("\n");
	}

	public static void main(String[] args) {
		FuzzyLogic logic = new FuzzyLogic();
		
		logic.addVariable("project_funding", Constants.INPUT);
		logic.getVariable("project_funding").addFuzzySet("very_low", Constants.TRAP, Arrays.asList(new Double[]{0.0, 0.0, 10.0, 30.0}));
		logic.getVariable("project_funding").addFuzzySet("low", Constants.TRAP, Arrays.asList(new Double[]{10.0, 30.0, 40.0, 60.0}));
		logic.getVariable("project_funding").addFuzzySet("medium", Constants.TRAP, Arrays.asList(new Double[]{40.0, 60.0, 70.0, 90.0}));
		logic.getVariable("project_funding").addFuzzySet("high", Constants.TRAP, Arrays.asList(new Double[]{70.0, 90.0, 100.0, 100.0}));
		
		logic.addVariable("team_experience_level", Constants.INPUT);
		logic.getVariable("team_experience_level").addFuzzySet("beginner", Constants.TRI, Arrays.asList(new Double[] {0.0, 15.0, 30.0}));
		logic.getVariable("team_experience_level").addFuzzySet("intermediate", Constants.TRI, Arrays.asList(new Double[] {15.0, 30.0, 45.0}));
		logic.getVariable("team_experience_level").addFuzzySet("expert", Constants.TRI, Arrays.asList(new Double[] {30.0, 60.0, 60.0}));
		
		logic.addVariable("risk", Constants.OUTPUT);
		logic.getVariable("risk").addFuzzySet("low", Constants.TRI, Arrays.asList(new Double[] {0.0, 25.0, 50.0}));
		logic.getVariable("risk").addFuzzySet("normal", Constants.TRI, Arrays.asList(new Double[] {25.0, 50.0, 75.0}));
		logic.getVariable("risk").addFuzzySet("high", Constants.TRI, Arrays.asList(new Double[] {50.0, 100.0, 100.0}));
		
		logic.addRule("project_funding=high | team_experience_level=expert", "risk=low");
		logic.addRule("project_funding=medium & (team_experience_level=intermediate | team_experience_level=beginner)", "risk=normal");
		logic.addRule("project_funding=very_low", "risk=high");
		logic.addRule("project_funding=low & team_experience_level=beginner", "risk=high");
		
		logic.getVariable("project_funding").setCrispValue(50.0);
		logic.getVariable("team_experience_level").setCrispValue(40.0);
		
		logic.fuzzification();
		
		logic.applyInferenceRules();
		
		logic.defuzzification();
	}

	

	

	

	

}
