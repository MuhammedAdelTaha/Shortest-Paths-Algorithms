package main.java;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Graph graph = new Graph();
        System.out.println(graph.initialize("test.txt"));
        graph.print();
        ArrayList<Integer> costs = new ArrayList<>();
        ArrayList<Integer> parents = new ArrayList<>();
        graph.dijkstra(0, costs, parents);
        System.out.println(costs);
        System.out.println(parents);
    }
}