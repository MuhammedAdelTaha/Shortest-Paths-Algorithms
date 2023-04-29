package main.java;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Graph graph = new Graph();
        System.out.println(graph.initialize("files/test4.txt"));
        graph.print();
        ArrayList<Integer> costs = new ArrayList<>();
        ArrayList<Integer> parents = new ArrayList<>();
        ArrayList<ArrayList<Integer>> costs2 = new ArrayList<>();
        ArrayList<ArrayList<Integer>> predecessors = new ArrayList<>();
        System.out.println(graph.floydWarshall(costs2, predecessors));
        for (int i = 0; i < graph.size(); i++){
            graph.dijkstra(i, costs, parents);
            System.out.println("Dijkstra costs  :" + costs);
            System.out.println("Dijkstra parents:" + parents);
            costs.clear(); parents.clear();
            System.out.println(graph.bellmanFord(i, costs, parents));
            System.out.println("Bellman-ford costs  :" + costs);
            System.out.println("Bellman-ford parents:" + parents);
            costs.clear(); parents.clear();
            System.out.println("Floyd-warshall costs  :" + costs2.get(i));
            System.out.println("Floyd-warshall parents  :" + predecessors.get(i));
            System.out.println("----------------------------------------------------------");
        }
    }
}