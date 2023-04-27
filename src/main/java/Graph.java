package main.java;

import java.io.*;
import java.util.*;

public class Graph{
    //size, isEmpty, contains, clear,print, insert, delete, bfs, dfs
    //can't insert a node that is already in the graph
    private ArrayList<ArrayList<Integer>> graph = new ArrayList<>();
    private Integer v, e;
    private HashMap<ArrayList<Integer>, Integer> edgeWeight = new HashMap<>();
    //V E
    //i j w
    private boolean isNegative(int number){
        return number < 0;
    }
    private boolean isValidNumber(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    private boolean contains(Integer vertex1, Integer vertex2){
        if(graph.get(vertex1) == null) return false;
        return graph.get(vertex1).contains(vertex2);
    }
    private void clear(){
        graph.clear();
        edgeWeight.clear();
    }
    public void print(){
        System.out.println(graph);
        for (Map.Entry<ArrayList<Integer>, Integer> w : edgeWeight.entrySet()){
            System.out.print("[");
            System.out.print(w.getKey().get(0));
            System.out.print(", ");
            System.out.print(w.getKey().get(1));
            System.out.print("] -> ");
            System.out.println(w.getValue());
        }
    }
    public Integer size(){
        return v;
    }
    //return false if the edge already exists and don't add the edge
    private boolean insert(Integer vertex1, Integer vertex2, Integer weight){
        if(contains(vertex1, vertex2) || vertex1 >= v || vertex1 < 0 || vertex2 >= v || vertex2 < 0) {
            System.out.println("check");
            return false;
        }
        if(graph.get(vertex1) == null) graph.set(vertex1, new ArrayList<>());
        graph.get(vertex1).add(vertex2);
        ArrayList<Integer> key = new ArrayList<>(); key.add(vertex1); key.add(vertex2);
        edgeWeight.put(key, weight);
        return true;
    }
    public boolean initialize(String path){
        ArrayList<String> inputs = new ArrayList<>();
        try {
            File myObj = new File(path);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNext()) {
                inputs.add(myReader.next());
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Please, enter a valid path..."); return false;
        }
        int n = inputs.size();
        if (n <= 5) {
            System.out.println("1");
            return false;
        }
        for (String input : inputs) {
            if (!isValidNumber(input)) {
                System.out.println("2");
                return false;
            }
        }
        clear();
        ArrayList<Integer> inputInt = new ArrayList<>();
        for (String input : inputs) inputInt.add(Integer.parseInt(input));
        v = inputInt.get(0); e = inputInt.get(1);
        if(isNegative(v) || isNegative(e) || e < v) {
            System.out.println("4");
            return false;
        }
        if(3 * e != n - 2) {
            System.out.println("3");
            return false;
        }
        boolean flag;
        for (int i = 0; i < v; i++){
            graph.add(i, null);
        }
        for (int i = 2; i < n; i+=3){
            System.out.println("---------------------");
            System.out.println(inputInt.get(i));
            System.out.println(inputInt.get(i + 1));
            System.out.println(inputInt.get(i + 2));
            System.out.println("---------------------");
            flag = insert(inputInt.get(i), inputInt.get(i + 1), inputInt.get(i + 2));
            if(!flag) {
                clear();
                System.out.println(i + 3);
                return false;
            }
        }
        return true;
    }
    public void dijkstra(Integer sourceNode, ArrayList<Integer> costs, ArrayList<Integer> parents){
        ArrayList<Boolean> visited = new ArrayList<>();
        for (int i = 0; i < v; i++){
            costs.add(i, Integer.MAX_VALUE);
            parents.add(i, null);
            visited.add(i, false);
        }
        costs.set(sourceNode, 0);
        visited.set(sourceNode, true);
        for(int i = 0; i < v; i++){
            ArrayList<Integer> children = graph.get(sourceNode);
            if (children != null) {
                for (Integer child : children){
                    if(!visited.get(child)){
                        ArrayList<Integer> key = new ArrayList<>(); key.add(sourceNode); key.add(child);
                        costs.set(child, Integer.min(costs.get(child), costs.get(sourceNode) + edgeWeight.get(key)));
                        parents.set(child, sourceNode);
                    }
                }
            }
            sourceNode = null;
            for (int j = 0; j < v; j++){
                if(!visited.get(j)){
                    if(sourceNode == null) {
                        sourceNode = j; continue;
                    }
                    if(costs.get(j) < costs.get(sourceNode)) sourceNode = j;
                }
            }
            if(sourceNode == null) break;
            visited.set(sourceNode, true);
        }
    }
}