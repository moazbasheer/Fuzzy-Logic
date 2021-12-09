
public class FuzzyRule {
	
	String antecedant;
	String consequent;
	private class Pointer {
		int num = 0;
		public void step() {
			this.num ++;
		}
		public int getStep() {
			return num;
		}
	}
	public FuzzyRule(String antecedant, String consequent) {
		this.antecedant = antecedant;
		this.consequent = consequent;
	}

	public String getConsequent() {
		return new String(consequent);
	}

	public double getAntecedantValue(FuzzyLogic logic) {
		Pointer pointer = new Pointer();
		return this.getExpressionValue(logic, pointer);
	}

	private double getExpressionValue(FuzzyLogic logic, Pointer pointer) {
		double termValue1 = this.getTermValue(logic, pointer);
		if(pointer.getStep() == antecedant.length())
			return termValue1;
		pointer.step();
		String symbol = this.getSymbol(pointer);
		pointer.step();
		double termValue2 = this.getTermValue(logic, pointer);
		if(pointer.getStep() != antecedant.length())
			pointer.step();
	
		return calculate(termValue1, symbol, termValue2);
	}

	private String getSymbol(Pointer pointer) {
		char letter = antecedant.charAt(pointer.getStep());
		pointer.step();
		return String.valueOf(letter);
	}

	private double getTermValue(FuzzyLogic logic, Pointer pointer) {
		StringBuilder builder = new StringBuilder();
		while(pointer.getStep() != antecedant.length()) {
			char letter = antecedant.charAt(pointer.getStep());
			if(letter == '(') {
				pointer.step();
				return this.getExpressionValue(logic, pointer);
			} else if(letter == ' ' || letter == ')') {
				String[] arr = new String(builder).split("=");
				String key = arr[0];
				String value = arr[1];
				if(letter == ')')
					pointer.step();
				return logic.getVariable(key).getFuzzySet(value).getIntercept();
			} else if(letter == '!') {
				pointer.step();
				return 1 - this.getTermValue(logic, pointer);
			}
			builder.append(letter);
			pointer.step();
		}
		String[] arr = new String(builder).split("=");
		String key = arr[0];
		String value = arr[1];
		return logic.getVariable(key).getFuzzySet(value).getIntercept();
	}
	
	private double calculate(double termValue1, String symbol, double termValue2) {
		if(symbol.equals("|")) {
			return Math.max(termValue1, termValue2);
		} else if(symbol.equals("&")) {
			return Math.min(termValue1, termValue2);
		}
		
		return 0;
	}
}
