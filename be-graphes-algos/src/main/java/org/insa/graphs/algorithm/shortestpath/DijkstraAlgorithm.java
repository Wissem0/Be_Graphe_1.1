package org.insa.graphs.algorithm.shortestpath;

import java.util.*;
import org.insa.graphs.algorithm.utils.Label;
import org.insa.graphs.algorithm.utils.ElementNotFoundException;
import org.insa.graphs.model.*;
import org.insa.graphs.algorithm.AbstractSolution.Status;
import org.insa.graphs.algorithm.utils.BinaryHeap;



public class DijkstraAlgorithm extends ShortestPathAlgorithm {
	
	  protected final ShortestPathData data = getInputData();
	  protected Graph graph = data.getGraph();
	  protected int nbNodes = graph.size();
	
    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }
    
	
    protected Label[] InitialiseLabels() {
    
    	// un array de labels 
	    Label[] ArrayLabels = new Label[nbNodes];
	    // La liste des noeuds
	    List<Node> nodes = graph.getNodes();
	    // On remplie l'array de labels avec les labels associés aux noeuds
	    for (Node node : nodes) {
	    	ArrayLabels[node.getId()] = new Label(node);
	    }
	    
	    return ArrayLabels ; 
    }
    
    @Override
    public ShortestPathSolution doRun() {
    	
    	//// INITIALISATION \\\\
    	
    	Label[] ArrayLabels ; 
    	ArrayLabels = InitialiseLabels() ; 
    	int nbre_explored = 0 ;
    	int nbre_mark = 0 ;
    	 
    	// Notifier les observateurs (origin processed)
        notifyOriginProcessed(data.getOrigin());
        
        // Initialisation du tas 
        BinaryHeap<Label> Heap = new BinaryHeap<>();
        
        // L'origine
        Node Origin = data.getOrigin();
        Label OriginLabel = ArrayLabels[Origin.getId()];
        
        Node Destination = data.getDestination();
        Label DestinationLabel = ArrayLabels[Destination.getId()];
       
        /* Variables pour calculer le nombre d'iteration et le nb d'arcs
        int nbrIter = 0;
        int nbrArcs = 0;  */ 
				
        // Insertion de l'origine dans le tas 
        OriginLabel.setCost(0);
        Heap.insert(OriginLabel);
        
        Label LabelActuel = null;
        ShortestPathSolution solution = null;
        
        //// ITERATIONS \\\\
        
        // On tourne la boucle tant qu'il existe des sommets non marqués, le tas n'est pas vide et on n'a pas encore trouvé une solution 
        while (!DestinationLabel.getMark() && !Heap.isEmpty() && solution == null ) {
        	// nbrIter += 1 ; 
        	
        	LabelActuel = Heap.deleteMin() ; 
        	LabelActuel.setMark();
        	nbre_mark ++ ; 
        	
        	System.out.println("Cout :" + LabelActuel.getCost());
        	
        	// Notifier l'observateur du noeud marqué 
        	notifyNodeMarked(LabelActuel.getNode());

            List<Arc> ListeSuccesseurs = LabelActuel.getNode().getSuccessors() ;
            
            // On parcourt la liste des successeurs du sommet actuel
        	for (Arc myArc: ListeSuccesseurs ) {
        		// nbrArcs += 1 ;
     
        		// On teste si le chemin est allowed
        		if (data.isAllowed(myArc)) {
        			
	        		Label IterDestination = ArrayLabels[myArc.getDestination().getId()];
	        		
	        		// Notifier l'observateur que le noeud est atteint 
	        		notifyNodeReached(myArc.getDestination());
	        		
	        		if(! IterDestination.getMark()) {
	        			
		        			if (!IterDestination.getMark() && IterDestination.getCost() > LabelActuel.getCost() + data.getCost(myArc)) {
		        				
		        				try {
		        					Heap.remove(IterDestination);
		        				} catch(ElementNotFoundException e) {}
		        				
		        				nbre_explored ++ ;
		        				IterDestination.setCost(LabelActuel.getCost() + data.getCost(myArc));
		        				IterDestination.setFather(myArc);
		        			
		        				Heap.insert(IterDestination);
		        			}
	        		}
        		}
        	}
        }
       
        // Si la destination n'a pas de père alors le chemin est impossible
		if((DestinationLabel.getFather()==null && (data.getOrigin().compareTo(data.getDestination())!= 0)  ) || !DestinationLabel.getMark()) {
			System.out.println("Chemin impossible") ; 
			solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        } else {
        	
        	// Sinon on atteint la destination et on notifie l'observateur
            notifyDestinationReached(data.getDestination());
            
            // Chemin vide
            if(data.getOrigin().compareTo(data.getDestination()) == 0) { 
            	System.out.println("Chemin Vide") ; 
               solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph));
                
            }else {
   
            // Creation du path
        	ArrayList<Arc> arcs = new ArrayList<>();
        	
        	while(!LabelActuel.equals(OriginLabel)) {
        		// System.out.println("Cost of label " + CurrentLabel.getCost());
        		arcs.add(LabelActuel.getFather());
        		LabelActuel = ArrayLabels[LabelActuel.getFather().getOrigin().getId()];
        		
        	}
        	// Reverse le chemin
            Collections.reverse(arcs);
          
            Path path = new Path(graph, arcs) ; 
            
            if(path.isValid()) {
                // Creation de la solution finale
                solution = new ShortestPathSolution(data, Status.OPTIMAL, path);
                System.out.println("Chemin Valide") ; 
                
            } else {
            	System.out.println("Chemin non Valide") ; 
            }
      
            }
        	
        }

		/* System.out.println(" Iterations : " + nbrIter + "iterations"); 
        System.out.println(" Arcs: " + nbrArcs + "arcs"); */ 
		System.out.println(" Nombre de Noeud visités " + nbre_explored );
		System.out.println(" Nombre de Noeud Marqués " + nbre_mark );
		return solution;
    }

}
