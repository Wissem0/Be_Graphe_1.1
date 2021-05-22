package org.insa.graphs.algorithm.utils;

import org.insa.graphs.model.Node;


public class LabelStar extends Label implements Comparable<Label> {

	private double coutEstime;
	
	public LabelStar (Node CurrentNode) {
		super(CurrentNode);
		this.coutEstime = Double.POSITIVE_INFINITY;
	}
	 @Override
	public double getTotalCost() {
		return this.coutEstime + this.getCost();
	}
	// Cout à vol d'oiseau
	public double getCoutEstime() {
	        return this.coutEstime;
	    }
	public void setCoutEstime(double EstimatedCost) {
	        this.coutEstime = EstimatedCost;
	    } 
    public int compareTo(LabelStar label) {
    	return Double.compare(this.getCoutEstime(), label.getCoutEstime());
	}
}