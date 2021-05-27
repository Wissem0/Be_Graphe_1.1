package org.insa.graphs.algorithm.shortestpath;

import org.insa.graphs.model.*;
import java.util.List;
import org.insa.graphs.algorithm.AbstractInputData;
import org.insa.graphs.algorithm.utils.*;

public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }
    
    protected Label[] InitialiseLabels() {
 	   LabelStar ArrayLabels[] = new LabelStar[nbNodes] ;
 	   List<Node> nodes = graph.getNodes();
 	   
 	   double Cost = 0;

 	   int MaxSpeed = Speed() ; 
 	   
 	   Point pointDestination = data.getDestination().getPoint() ; 
 	   
 	   for (Node node : nodes) {
 		   ArrayLabels[node.getId()] = new LabelStar(node);
 		   
 		   // Si c'est la mode Length: Le cout est la distance entre ce noeud et la destination
 		   if(data.getMode() == AbstractInputData.Mode.LENGTH) {
 			   Cost = node.getPoint().distanceTo(pointDestination);
 			   
 			// Sinon c'est le temps
 	       	} else {
 	       		Cost = 3.6* node.getPoint().distanceTo(pointDestination) / MaxSpeed; 
 	       	}
 		   
 		   ArrayLabels[node.getId()].setEstimatedCost(Cost);
 	   }
 	   return ArrayLabels ; 
     }
   // Pour éviter le problème des sommets marqués qui font des cercle dans toute la carte
    private int Speed() {
 	   int MaxSpeedData =  data.getMaximumSpeed() ; 
 	   int MaxSpeedGraph = graph.getGraphInformation().getMaximumSpeed() ;
 	   int Speed = Math.min(MaxSpeedData, MaxSpeedGraph) ; 
 	   
 	   if (MaxSpeedData ==  GraphStatistics.NO_MAXIMUM_SPEED && MaxSpeedGraph ==  GraphStatistics.NO_MAXIMUM_SPEED ) {
 		   Speed = 130 ;
 	   }
 	   if (MaxSpeedData ==  GraphStatistics.NO_MAXIMUM_SPEED) {
 		   Speed = MaxSpeedGraph; 
 	   }
 	   if (MaxSpeedGraph ==  GraphStatistics.NO_MAXIMUM_SPEED) {
 		   Speed = MaxSpeedData; 
 	   }
 	return Speed ; 
    }

}
