package org.insa.graphs.algorithm.utils;



import org.insa.graphs.model.*;

public class Label implements Comparable<Label> {
	
	private double cost;
	private boolean marked; // vrai si le noeud a été marqué
	private Arc father;
	private Node node;
	
	public Label(Node noeud){
		this.node = noeud;
		this.marked = false;
		this.cost = Float.POSITIVE_INFINITY;
		this.father = null; 
	}
	
	public Node getNode() {
		return this.node;
	}
	
	public double getCost() {
		return this.cost;
	}
	
	public double getTotalCost() {
		return this.getCost();
	}
	
	//Retourne true si le noeud a été marqué 
	public boolean getMark() {
		return this.marked;
	}
	
	// Retourne le père 
	public Arc getFather() {
		return this.father;
	}
	
	public void setMark() {
		this.marked = true;
	}
	
	public void setCost(double cout) {
		this.cost = cout;
	}
	
	public void setFather(Arc father) {
		this.father = father;
	}
	
	public void setNode(Node node) {
		this.node = node;
	}
	
	
	// Compare les Labels selon leur coût 
	public int compareTo(Label autre) {
		return Double.compare(this.getTotalCost(), autre.getTotalCost() );
	} 
	
}