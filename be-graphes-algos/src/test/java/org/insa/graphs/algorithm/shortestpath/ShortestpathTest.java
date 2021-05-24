package org.insa.graphs.algorithm.shortestpath;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.insa.graphs.algorithm.AbstractSolution;
import org.insa.graphs.algorithm.ArcInspectorFactory;
import org.insa.graphs.model.Arc;
import org.insa.graphs.model.Graph;
import org.insa.graphs.model.Node;
import org.insa.graphs.model.Path;
import org.insa.graphs.model.RoadInformation;
import org.insa.graphs.model.RoadInformation.RoadType;
import org.insa.graphs.model.io.BinaryGraphReader;
import org.insa.graphs.model.io.BinaryPathReader;
import org.insa.graphs.model.io.GraphReader;
import org.insa.graphs.model.io.PathReader;
import org.junit.BeforeClass;

import org.junit.Test;



public class ShortestpathTest {
	
	// Graphes pour faire les test ( carré et Haute-Garone )
	private static Graph graphCarre, graphHauteGaronne, graphGuadeloupe ;
	private static ArrayList<Node> noeudsCarre, noeudsHauteGaronne, noeudsGuadeloupe ;
	
	// Déclation des variable pour stocker des solution 
	
	/* Mode0 : Shortest path, all roads allowed
	 * Mode1 : Shortest path, only roads open for cars
	 * Mode2 : Fastest path, all roads allowed
	 * Mode3 : Fastest path, only roads open for cars 
	 * Mode4 : Fastest path for pedestrians */
	
	/* Solution avec un seul noeud ( origine = destination), Dijkstra avec des differents mode */
	protected static ShortestPathSolution 	unNoeudDijkstra0, unNoeudDijkstra2 ;
	/* Solution avec un seul noeud ( origine = destination), AStar avec des differents mode */
	protected static ShortestPathSolution  unNoeudAStar0, unNoeudAStar2 ;
	/* Solution Dijkstra et AStar, à comparer avec Bellman */
	protected static ShortestPathSolution longDijkstra0, longDijkstra2, longAStar0, longAStar2 ;
	/* Solution infeasible Dijkstra et AStar */
	protected static ShortestPathSolution infeasibleDijkstra0,infeasibleDijkstra1,infeasibleDijkstra2,infeasibleDijkstra3, infeasibleDijkstra4,  
										  infeasibleAStar0, infeasibleAStar1, infeasibleAStar2, infeasibleAStar3, infeasibleAStar4 ;
	
	
	// Des paths pour les tests
	protected static Path unNoeudPath ; 
	protected static Path HGPath_Length, HGPath_Time ; 
	
	/* Des array de solutions pour stocker des solutions entre deux noeuds aléatoires avec 
	 * Dijkstra et Astar pour les comparer après avec ce qu'on trouve avec Bellman */
	// On va tester des differents modes \\
	protected static ShortestPathSolution[] Sol_Alea_DijkstraMode0 = new ShortestPathSolution[20] ;
	protected static ShortestPathSolution[] Sol_Alea_AStarMode0 = new ShortestPathSolution[20]; 
	protected static ShortestPathSolution[] Sol_Alea_BellmanMode0 = new ShortestPathSolution[20]; 
	protected static ShortestPathSolution[] Sol_Alea_DijkstraMode1 = new ShortestPathSolution[20]; 
	protected static ShortestPathSolution[] Sol_Alea_AStarMode1 = new ShortestPathSolution[20]; 
	protected static ShortestPathSolution[] Sol_Alea_BellmanMode1 = new ShortestPathSolution[20]; 
	protected static ShortestPathSolution[] Sol_Alea_DijkstraMode2 = new ShortestPathSolution[20]; 
	protected static ShortestPathSolution[] Sol_Alea_AStarMode2 = new ShortestPathSolution[20]; 
	protected static ShortestPathSolution[] Sol_Alea_BellmanMode2 = new ShortestPathSolution[20]; 
	protected static ShortestPathSolution[] Sol_Alea_DijkstraMode3 = new ShortestPathSolution[20]; 
	protected static ShortestPathSolution[] Sol_Alea_AStarMode3 = new ShortestPathSolution[20]; 
	protected static ShortestPathSolution[] Sol_Alea_BellmanMode3 = new ShortestPathSolution[20]; 
	protected static ShortestPathSolution[] Sol_Alea_DijkstraMode4 = new ShortestPathSolution[20]; 
	protected static ShortestPathSolution[] Sol_Alea_AStarMode4 = new ShortestPathSolution[20]; 
	protected static ShortestPathSolution[] Sol_Alea_BellmanMode4 = new ShortestPathSolution[20]; 
	
	
	         ///////\\\\\\\
	//////////BEFORE CLASS\\\\\\\\\\
         
	@BeforeClass
	public static void initAll() throws IOException {
		
		// Lecture du map carré
		String mapCarre = "C:/Users/wisse/Desktop/INSA/3emeannee/S2/Graphes/BE/Maps/extras/carre.mapgr" ; 
		GraphReader readerCarre = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(mapCarre))));
		graphCarre = readerCarre.read() ; 
		// Les noeuds du graphe carré
		noeudsCarre = new ArrayList<>(graphCarre.getNodes()) ;
		
		// Lecture du map Haute-Garonne
		String mapHauteGaronne = "C:/Users/wisse/Desktop/INSA/3emeannee/S2/Graphes/BE/Maps/europe/france/haute-garonne.mapgr" ;
		GraphReader readerHG = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(mapHauteGaronne))));
		graphHauteGaronne = readerHG.read() ; 
		// Les noeuds du graphe Haute-Garonne
		noeudsHauteGaronne = new ArrayList<>(graphHauteGaronne.getNodes()) ;
		
		// Lecture du map Guadeloupe
		String mapGuadeloupe = "C:/Users/wisse/Desktop/INSA/3emeannee/S2/Graphes/BE/Maps/europe/france/guadeloupe.mapgr" ;
		GraphReader readerGuadeloupe = new BinaryGraphReader(new DataInputStream(new BufferedInputStream(new FileInputStream(mapGuadeloupe))));
		graphGuadeloupe = readerGuadeloupe.read() ; 
		// Les noeuds du graphe Guadeloupe
		noeudsGuadeloupe = new ArrayList<>(graphGuadeloupe.getNodes()) ;
		
		// Lecture du path INSA - Aeroport (On l'utilisera après pour vérifier les solutions Dijkstra et AStar)
		// INSA- Bikini canal
		String PathINSA0 = "C:/Users/wisse/Desktop/INSA/3emeannee/S2/Graphes/BE/Paths/path_fr31_insa_bikini_canal.path" ; 
		PathReader readerPath0 = new BinaryPathReader(new DataInputStream(new BufferedInputStream(new FileInputStream(PathINSA0)))) ;
		HGPath_Length = readerPath0.readPath(graphHauteGaronne) ; 
		// INSA- aeroport Time
		String PathINSA2 = "C:/Users/wisse/Desktop/INSA/3emeannee/S2/Graphes/BE/Paths/path_fr31_insa_aeroport_time.path" ; 
		PathReader readerPath2 = new BinaryPathReader(new DataInputStream(new BufferedInputStream(new FileInputStream(PathINSA2)))) ;
		HGPath_Time = readerPath2.readPath(graphHauteGaronne) ; 
		
		
		////Test chemin avec un seul noeuds\\\\
		
		// Un path composé d'un seul noeud aléatoire
		Random rand = new Random(); 
		Node unNoeud = noeudsCarre.get(rand.nextInt(noeudsCarre.size()));
		unNoeudPath = new Path(graphCarre, unNoeud);
		
		// data un seul noeud mode0
		ShortestPathData data0 = new ShortestPathData(graphCarre, unNoeud, unNoeud, ArcInspectorFactory.getAllFilters().get(0));
		// data un seul noeud mode2
		ShortestPathData data2 = new ShortestPathData(graphCarre, unNoeud, unNoeud, ArcInspectorFactory.getAllFilters().get(2));
		
		
		// DIJKSTRA 
		// Mode 0 
		DijkstraAlgorithm singleDijkstra0 = new DijkstraAlgorithm(data0);
		unNoeudDijkstra0 = singleDijkstra0.doRun();
		// Mode 2 
		DijkstraAlgorithm singleDijkstra2 = new DijkstraAlgorithm(data2);
		unNoeudDijkstra2 = singleDijkstra2.doRun();
		
		
		// ASTAR  
		// Mode 0 
		AStarAlgorithm singleAStar0 = new AStarAlgorithm(data0) ; 
		unNoeudAStar0 = singleAStar0.doRun() ; 
		// Mode 2 
		AStarAlgorithm singleAStar2 = new AStarAlgorithm(data2) ; 
		unNoeudAStar2 = singleAStar2.doRun() ;
		
		
		
		
		
		
		//// Test pour 20 paires de noeuds aléatoires \\\\
		// On va utiliser le graphe carré puisque l'algorithme de Bellman prend beaucoup de teps sur les grands maps 
		int i = 0 ; 
		while ( i < 20 ) {
			boolean equal = true ; // Variable pour verifier que les deux noeuds sont différents
			
			// Génération des deux noeuds
			Node premierN = noeudsCarre.get(rand.nextInt(noeudsCarre.size())) ; 
			Node secondN = noeudsCarre.get(rand.nextInt(noeudsCarre.size())) ;  
			
			// S'ils sont égaux, on skip et on passe au paire suivant
			if (premierN.equals(secondN)) {
				equal = false ; 	
			} 
			
			// Executer les algorithme sur les paires aléatoires
			if (equal) {
				// Data
				ShortestPathData dataAlea0 = new ShortestPathData(graphCarre, premierN, secondN, ArcInspectorFactory.getAllFilters().get(0)) ; 
    			ShortestPathData dataAlea1 = new ShortestPathData(graphCarre, premierN, secondN, ArcInspectorFactory.getAllFilters().get(1)) ; 
    			ShortestPathData dataAlea2 = new ShortestPathData(graphCarre, premierN, secondN, ArcInspectorFactory.getAllFilters().get(2)) ; 
    			ShortestPathData dataAlea3 = new ShortestPathData(graphCarre, premierN, secondN, ArcInspectorFactory.getAllFilters().get(3)) ; 
    			ShortestPathData dataAlea4 = new ShortestPathData(graphCarre, premierN, secondN, ArcInspectorFactory.getAllFilters().get(4)) ; 
    			
				// Dijkstra
    			DijkstraAlgorithm dijkstra0 = new DijkstraAlgorithm(dataAlea0) ; //Mode0
    			Sol_Alea_DijkstraMode0[i] = dijkstra0.doRun() ; 
    			DijkstraAlgorithm dijkstra1 = new DijkstraAlgorithm(dataAlea1) ; //Mode1
    			Sol_Alea_DijkstraMode1[i] = dijkstra1.doRun() ; 
    			DijkstraAlgorithm dijkstra2 = new DijkstraAlgorithm(dataAlea2) ; //Mode2
    			Sol_Alea_DijkstraMode2[i] = dijkstra2.doRun() ;
    			DijkstraAlgorithm dijkstra3 = new DijkstraAlgorithm(dataAlea3) ; //Mode3
    			Sol_Alea_DijkstraMode3[i] = dijkstra3.doRun() ;
    			DijkstraAlgorithm dijkstra4 = new DijkstraAlgorithm(dataAlea4) ; //Mode4
    			Sol_Alea_DijkstraMode4[i] = dijkstra4.doRun() ; 
    			
    			// AStar
    			AStarAlgorithm astar0 = new AStarAlgorithm(dataAlea0) ; //Mode0
    			Sol_Alea_AStarMode0[i] = astar0.doRun() ; 
    			AStarAlgorithm astar1 = new AStarAlgorithm(dataAlea1) ; //Mode1
    			Sol_Alea_AStarMode1[i] = astar1.doRun() ; 
    			AStarAlgorithm astar2 = new AStarAlgorithm(dataAlea2) ; //Mode2
    			Sol_Alea_AStarMode2[i] = astar2.doRun() ; 
    			AStarAlgorithm astar3 = new AStarAlgorithm(dataAlea3) ; //Mode3
    			Sol_Alea_AStarMode3[i] = astar3.doRun() ; 
    			AStarAlgorithm astar4 = new AStarAlgorithm(dataAlea4) ; //Mode4
    			Sol_Alea_AStarMode4[i] = astar4.doRun() ; 
    			
    			// Bellman Ford
    			BellmanFordAlgorithm bellman0 = new BellmanFordAlgorithm(dataAlea0) ; //Mode0
    			Sol_Alea_BellmanMode0[i] = bellman0.doRun() ; 
    			BellmanFordAlgorithm bellman1 = new BellmanFordAlgorithm(dataAlea1) ; //Mode1
    			Sol_Alea_BellmanMode1[i] = bellman1.doRun() ; 
    			BellmanFordAlgorithm bellman2 = new BellmanFordAlgorithm(dataAlea2) ; //Mode2
    			Sol_Alea_BellmanMode2[i] = bellman2.doRun() ;
    			BellmanFordAlgorithm bellman3 = new BellmanFordAlgorithm(dataAlea3) ; //Mode3
    			Sol_Alea_BellmanMode3[i] = bellman3.doRun() ;
    			BellmanFordAlgorithm bellman4 = new BellmanFordAlgorithm(dataAlea4) ; //Mode4
    			Sol_Alea_BellmanMode4[i] = bellman4.doRun() ;
    			
    			i++;
			}
		
			
		}
		
		
		
		
		
		
		//// Test d'un long chemin \\\\
		// Ici on ne peut pas utiliser Bellman pour vérifier le chemin car ça va prendre beaucoups de temps
		ShortestPathData dataHG0 = new ShortestPathData(graphHauteGaronne, HGPath_Length.getOrigin(), HGPath_Length.getDestination(), ArcInspectorFactory.getAllFilters().get(0));
		ShortestPathData dataHG2 = new ShortestPathData(graphHauteGaronne, HGPath_Time.getOrigin(), HGPath_Time.getDestination(), ArcInspectorFactory.getAllFilters().get(2));
		
		// Dijkstra : Mode 0,2
		DijkstraAlgorithm longAlgoDijkstra0 = new DijkstraAlgorithm(dataHG0);
		longDijkstra0 = longAlgoDijkstra0.doRun();
		DijkstraAlgorithm longAlgoDijkstra2 = new DijkstraAlgorithm(dataHG2);
		longDijkstra2 = longAlgoDijkstra2.doRun();
		
		// Dijkstra : Mode 0,2
		AStarAlgorithm longAlgoAStar0 = new AStarAlgorithm(dataHG0);
		longAStar0 = longAlgoAStar0.doRun();
		AStarAlgorithm longAlgoAStar2 = new AStarAlgorithm(dataHG2);
		longAStar2 = longAlgoAStar2.doRun();
		
		
		

		
		
		
		
		//// Test chemin impossible \\\\
		
		// On va utiliser le map de Guadeloupe et on prend deux noeuds séparé par la mer 
		ShortestPathData dataInfeasible0 = new ShortestPathData(graphGuadeloupe, graphGuadeloupe.get(14195), graphGuadeloupe.get(15804), ArcInspectorFactory.getAllFilters().get(0)) ; 
		ShortestPathData dataInfeasible1 = new ShortestPathData(graphGuadeloupe, graphGuadeloupe.get(14195), graphGuadeloupe.get(15804), ArcInspectorFactory.getAllFilters().get(1)) ; 
		ShortestPathData dataInfeasible2 = new ShortestPathData(graphGuadeloupe, graphGuadeloupe.get(14195), graphGuadeloupe.get(15804), ArcInspectorFactory.getAllFilters().get(2)) ; 
		ShortestPathData dataInfeasible3 = new ShortestPathData(graphGuadeloupe, graphGuadeloupe.get(14195), graphGuadeloupe.get(15804), ArcInspectorFactory.getAllFilters().get(3)) ; 
		ShortestPathData dataInfeasible4 = new ShortestPathData(graphGuadeloupe, graphGuadeloupe.get(14195), graphGuadeloupe.get(15804), ArcInspectorFactory.getAllFilters().get(4)) ;
		
		// Dijkstra
		DijkstraAlgorithm infeasibleDijkstraAlgo0 = new DijkstraAlgorithm(dataInfeasible0) ; 
		infeasibleDijkstra0 = infeasibleDijkstraAlgo0.doRun() ; 
		DijkstraAlgorithm infeasibleDijkstraAlgo1 = new DijkstraAlgorithm(dataInfeasible1) ; 
		infeasibleDijkstra1 = infeasibleDijkstraAlgo1.doRun() ; 
		DijkstraAlgorithm infeasibleDijkstraAlgo2 = new DijkstraAlgorithm(dataInfeasible2) ; 
		infeasibleDijkstra2 = infeasibleDijkstraAlgo2.doRun() ; 
		DijkstraAlgorithm infeasibleDijkstraAlgo3 = new DijkstraAlgorithm(dataInfeasible3) ; 
		infeasibleDijkstra3 = infeasibleDijkstraAlgo3.doRun() ; 
		DijkstraAlgorithm infeasibleDijkstraAlgo4 = new DijkstraAlgorithm(dataInfeasible4) ; 
		infeasibleDijkstra4 = infeasibleDijkstraAlgo4.doRun() ; 
		
		// AStar
		AStarAlgorithm infeasibleAStarAlgo0 = new AStarAlgorithm(dataInfeasible0) ; 
		infeasibleAStar0 = infeasibleAStarAlgo0.doRun() ; 
		AStarAlgorithm infeasibleAStarAlgo1 = new AStarAlgorithm(dataInfeasible1) ; 
		infeasibleAStar1 = infeasibleAStarAlgo1.doRun() ; 
		AStarAlgorithm infeasibleAStarAlgo2 = new AStarAlgorithm(dataInfeasible2) ; 
		infeasibleAStar2 = infeasibleAStarAlgo2.doRun() ; 
		AStarAlgorithm infeasibleAStarAlgo3 = new AStarAlgorithm(dataInfeasible3) ; 
		infeasibleAStar3 = infeasibleAStarAlgo3.doRun() ;
		AStarAlgorithm infeasibleAStarAlgo4 = new AStarAlgorithm(dataInfeasible4) ; 
		infeasibleAStar4 = infeasibleAStarAlgo4.doRun() ;
	}
	
	
	
	
	
	
    		 ///////\\\\\\\
		//////////TEST\\\\\\\\\\
	

	    // Test Dijkstra, Origin=Destination, Mode0
		@Test
		public void TestDijkstra_LongueurNulle_Mode0() {
			assertEquals(0, unNoeudPath.getOrigin().compareTo(unNoeudDijkstra0.getPath().getOrigin()));
			assertTrue(Math.abs(unNoeudPath.getLength() - unNoeudDijkstra0.getPath().getLength()) < 0.001);
			assertTrue(unNoeudDijkstra0.getPath().isValid());
		}
		

		// Test Dijkstra, Origin=Destination, Mode2
		@Test
		public void TestDijkstra_LongueurNulle_Mode2() {
			assertEquals(0, unNoeudPath.getOrigin().compareTo(unNoeudDijkstra2.getPath().getOrigin()));
			assertTrue(Math.abs(unNoeudPath.getMinimumTravelTime() - unNoeudDijkstra2.getPath().getMinimumTravelTime()) < 0.001);
			assertTrue(unNoeudDijkstra2.getPath().isValid());
		}
		
		
	    // Test AStar, Origin=Destination, Mode0
		@Test
		public void TestAStar_LongueurNulle_Mode0() {
			assertEquals(0, unNoeudPath.getOrigin().compareTo(unNoeudAStar0.getPath().getOrigin())) ; 
			assertTrue(Math.abs(unNoeudPath.getLength() - unNoeudAStar0.getPath().getLength()) < 0.001) ; 
			assertTrue(unNoeudAStar0.getPath().isValid()) ; 
		}
		
		
	    // Test AStar, Origin=Destination, Mode2
		@Test
		public void TestAStar_LongueurNulle_Mode2() {
			assertEquals(0, unNoeudPath.getOrigin().compareTo(unNoeudAStar2.getPath().getOrigin())) ; 
			assertTrue(Math.abs(unNoeudPath.getMinimumTravelTime()- unNoeudAStar2.getPath().getMinimumTravelTime()) < 0.001) ; 
			assertTrue(unNoeudAStar2.getPath().isValid()) ; 
		}
		
		// Test Dijkstra, paires de noeuds aléatoires, Mode0 
		@Test
		public void Dijkstra_CompareTo_BellmanMode0() {
			for (int k=0; k<20; k++) {
				assertTrue(Math.abs(Sol_Alea_DijkstraMode0[k].getPath().getLength() - Sol_Alea_BellmanMode0[k].getPath().getLength()) < 0.001) ; 
				assertTrue(Sol_Alea_DijkstraMode0[k].getPath().isValid()) ; 
			}	
		}
		
		
		// Test Dijkstra, paires de noeuds aléatoires, Mode1
		@Test
		public void Dijkstra_CompareTo_Bellman_Mode1() {
			for (int k=0; k<20; k++) {
				assertTrue(Math.abs(Sol_Alea_DijkstraMode1[k].getPath().getLength() - Sol_Alea_BellmanMode1[k].getPath().getLength()) < 0.001) ; 
				assertTrue(Sol_Alea_DijkstraMode1[k].getPath().isValid()) ; 
			}	
		}
		
		
		// Test Dijkstra, paires de noeuds aléatoires, Mode2
		@Test
		public void Dijkstra_CompareTo_Bellman_Mode2() {
			for (int k=0; k<20; k++) {
				assertTrue(Math.abs(Sol_Alea_DijkstraMode2[k].getPath().getMinimumTravelTime() - Sol_Alea_BellmanMode2[k].getPath().getMinimumTravelTime()) < 0.001) ; 
				assertTrue(Sol_Alea_DijkstraMode2[k].getPath().isValid()) ; 
			}	
		}
		
		
		// Test Dijkstra, paires de noeuds aléatoires, Mode3
		@Test
		public void Dijkstra_CompareTo_Bellman_Mode3() {
			for (int k=0; k<20; k++) {
				assertTrue(Math.abs(Sol_Alea_DijkstraMode3[k].getPath().getMinimumTravelTime() - Sol_Alea_BellmanMode3[k].getPath().getMinimumTravelTime()) < 0.001) ; 
				assertTrue(Sol_Alea_DijkstraMode3[k].getPath().isValid()) ; 
			}	
		}
		
		
		// Test Dijkstra, paires de noeuds aléatoires, Mode4
		@Test
		public void Dijkstra_CompareTo_Bellman_Mode4() {
			for (int k=0; k<20; k++) {
				assertTrue(Math.abs(Sol_Alea_DijkstraMode4[k].getPath().getMinimumTravelTime() - Sol_Alea_AStarMode4[k].getPath().getMinimumTravelTime()) < 0.001) ; 
				assertTrue(Sol_Alea_DijkstraMode4[k].getPath().isValid()) ; 
			}	
		}
		
		
		// Test AStar, paires de noeuds aléatoires, Mode0
		@Test
		public void AStar_CompareTo_Bellman_Mode0() {
			for (int k=0; k<20; k++) {
				assertTrue(Math.abs(Sol_Alea_AStarMode0[k].getPath().getLength() - Sol_Alea_BellmanMode0[k].getPath().getLength()) < 0.001) ; 
				assertTrue(Sol_Alea_AStarMode0[k].getPath().isValid()) ; 
			}
		}
		
		
		// Test AStar, paires de noeuds aléatoires, Mode1
		@Test
		public void AStar_CompareTo_Bellman_Mode1() {
			for (int k=0; k<20; k++) {
				assertTrue(Math.abs(Sol_Alea_AStarMode1[k].getPath().getLength() - Sol_Alea_BellmanMode1[k].getPath().getLength()) < 0.001) ; 
				assertTrue(Sol_Alea_AStarMode1[k].getPath().isValid()) ; 
			}
		}
		
		
		// Test AStar, paires de noeuds aléatoires, Mode2
		@Test
		public void AStar_CompareTo_Bellman_Mode2() {
			for (int k=0; k<20; k++) {
				assertTrue(Math.abs(Sol_Alea_AStarMode2[k].getPath().getMinimumTravelTime() - Sol_Alea_BellmanMode2[k].getPath().getMinimumTravelTime()) < 0.001) ; 
				assertTrue(Sol_Alea_AStarMode2[k].getPath().isValid()) ; 
			}
		}
		
		
		// Test AStar, paires de noeuds aléatoires, Mode3
		@Test
		public void AStar_CompareTo_Bellman_Mode3() {
			for (int k=0; k<20; k++) {
				assertTrue(Math.abs(Sol_Alea_AStarMode3[k].getPath().getMinimumTravelTime() - Sol_Alea_BellmanMode3[k].getPath().getMinimumTravelTime()) < 0.001) ; 
				assertTrue(Sol_Alea_AStarMode3[k].getPath().isValid()) ; 
			}
		}
		
		
		// Test AStar, paires de noeuds aléatoires, Mode4
		@Test
		public void AStar_CompareTo_Bellman_Mode4() {
			for (int k=0; k<20; k++) {
				assertTrue(Math.abs(Sol_Alea_AStarMode4[k].getPath().getMinimumTravelTime() - Sol_Alea_BellmanMode4[k].getPath().getMinimumTravelTime()) < 0.001) ; 
				assertTrue(Sol_Alea_AStarMode4[k].getPath().isValid()) ; 
			}
		}
		
		
		// Test Dijkstra , Long chemin , Mode0 INSA-aéroport
		@Test 
		public void TestDijkstra_LongMode0() {
			assertTrue(Math.abs(longDijkstra0.getPath().getMinimumTravelTime() - HGPath_Length.getMinimumTravelTime()) < 0.001) ; 
			assertTrue(longDijkstra0.getPath().isValid()) ; 
		}
		
		
		// Test Dijkstra , Long chemin , Mode2 INSA-aéroport
		@Test 
		public void TestDijkstra_LongMode2() {
			assertTrue(Math.abs(longDijkstra2.getPath().getMinimumTravelTime() - HGPath_Time.getMinimumTravelTime()) < 0.001) ; 
			assertTrue(longDijkstra2.getPath().isValid()) ; 
		}
		
		
		// Test AStar , Long chemin , Mode0 INSA-aéroport
		@Test 
		public void TestAStar_LongMode0() {
			assertTrue(Math.abs(longAStar0.getPath().getLength() - HGPath_Length.getLength()) < 0.001) ; 
			assertTrue(longAStar0.getPath().isValid()) ; 
		}
		
		
		// Test Dijkstra , Long chemin , Mode2 INSA-aéroport
		@Test 
		public void TestAStar_LongMode2() {
			assertTrue(Math.abs(longAStar2.getPath().getMinimumTravelTime() - HGPath_Time.getMinimumTravelTime()) < 0.001) ; 
			assertTrue(longAStar2.getPath().isValid()) ; 
		}
		
		
		// Test Dijkstra, infeasible , Mode0 //
		@Test 
		public void Dijkstra_Infeasible_Mode0() {
			assertEquals(infeasibleDijkstra0.getStatus(), AbstractSolution.Status.INFEASIBLE);
		}
		
		
		// Test Dijkstra, infeasible , Mode1 //
		@Test 
		public void Dijkstra_Infeasible_Mode1() {
			assertEquals(infeasibleDijkstra1.getStatus(), AbstractSolution.Status.INFEASIBLE);
		}
			
		
		// Test Dijkstra, infeasible , Mode2 //
		@Test 
		public void Dijkstra_Infeasible_Mode2() {
			assertEquals(infeasibleDijkstra2.getStatus(), AbstractSolution.Status.INFEASIBLE);
		}
		
		
		// Test Dijkstra, infeasible , Mode3 //
		@Test 
		public void Dijkstra_Infeasible_Mode3() {
			assertEquals(infeasibleDijkstra3.getStatus(), AbstractSolution.Status.INFEASIBLE);
		}

		
		// Test Dijkstra, infeasible , Mode4 //
		@Test 
		public void Dijkstra_Infeasible_Mode4() {
			assertEquals(infeasibleDijkstra4.getStatus(), AbstractSolution.Status.INFEASIBLE);
		}
		
		
		// Test AStar, infeasible , Mode0 //
		@Test 
		public void AStar_Infeasible_Mode0() {
			assertEquals(infeasibleDijkstra0.getStatus(), AbstractSolution.Status.INFEASIBLE);
		}
		
		
		// Test AStar, infeasible , Mode1 //
		@Test 
		public void AStar_Infeasible_Mode1() {
			assertEquals(infeasibleAStar1.getStatus(), AbstractSolution.Status.INFEASIBLE);
		}
			
		
		// Test AStar, infeasible , Mode2 //
		@Test 
		public void AStar_Infeasible_Mode2() {
			assertEquals(infeasibleAStar2.getStatus(), AbstractSolution.Status.INFEASIBLE);
		}
		
		
		// Test AStar, infeasible , Mode3 //
		@Test 
		public void AStar_Infeasible_Mode3() {
			assertEquals(infeasibleAStar3.getStatus(), AbstractSolution.Status.INFEASIBLE);
		}

		
		// Test AStar, infeasible , Mode4 //
		@Test 
		public void AStar_Infeasible_Mode4() {
			assertEquals(infeasibleAStar4.getStatus(), AbstractSolution.Status.INFEASIBLE);
		}
		

}
