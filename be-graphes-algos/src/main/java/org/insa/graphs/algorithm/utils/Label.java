package org.insa.graphs.algorithm.utils;



import org.insa.graphs.model.*;

public class Label implements Comparable<Label> {
	
	private double cost;
	private boolean marked; // vrai si le noeud a été marqué
	private Arc father;
	private Node node;
	//private boolean inTas; // vrai si le noeud a été mis dans le tas
	
	public Label(Node noeud){
		this.node = noeud;
		this.marked = false;
		this.cost = Float.POSITIVE_INFINITY;
		this.father = null; 
		//this.inTas = false;
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
	
	/* Retourne true si le noeud a été marqué */
	public boolean getMark() {
		return this.marked;
	}
	
	/* Retourne le père */
	public Arc getFather() {
		return this.father;
	}
	
	/* Retourne true si le noeud a été mis dans le tas 
	public boolean getInTas() {
		return this.inTas;
	}	*/
	
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
	
	/*public void setInTas() {
		this.inTas = true;
	}*/
	
	// Compare les Labels selon leur coût 
	public int compareTo(Label autre) {
		return Double.compare(this.getTotalCost(), autre.getTotalCost() );
	} 
	
}