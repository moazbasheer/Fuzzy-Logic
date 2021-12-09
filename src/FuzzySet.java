import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FuzzySet {
	private String fuzzySetName;
	private int fuzzySetType;
	private List<Double> boundaries;
	private double intercept;
	public FuzzySet(String fuzzySetName, int fuzzySetType, List<Double> boundaries) {
		this.fuzzySetName = fuzzySetName;
		this.fuzzySetType = fuzzySetType;
		this.boundaries = boundaries;
		this.intercept = 0;
	}
	
	public String getFuzzySetName() {
		return this.fuzzySetName;
	}
	
	public double getIntercept() {
		return intercept;
	}
	
	public void setIntercept(double intercept) {
		this.intercept = Math.max(this.intercept, intercept);
	}
	
	public List<Double> getBoundaries() {
		return new ArrayList(boundaries);
	}
	
	public int getFuzzyType() {
		return this.fuzzySetType;
	}

	public Double getCentroid() {
		if(fuzzySetType == Constants.TRI) {
			double sum = 0.0;
			for(Double d:boundaries) {
				sum += d;
			}
			return sum / 3.0;
		} else {
			double A = 0.0;
			double C = 0.0;
			List<Double> yBoundaries = Arrays.asList(new Double[] {0.0, 1.0, 1.0, 0.0});
			
			for(int i = 0; i < boundaries.size() - 1; i++) {
				A += yBoundaries.get(i + 1) * boundaries.get(i) - boundaries.get(i + 1) * yBoundaries.get(i);
				C += (boundaries.get(i) + boundaries.get(i + 1)) * 
						(boundaries.get(i) * yBoundaries.get(i + 1) - boundaries.get(i + 1) * yBoundaries.get(i));
			}
			A /= 2.0;
			C *= 1.0 / (6.0 * A);
			return C;
		}
	}

}
