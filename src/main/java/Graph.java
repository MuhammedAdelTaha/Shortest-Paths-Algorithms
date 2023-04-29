package main.java;

import java.io.*;
import java.util.*;

public class Graph{
    //size, isEmpty, contains, clear,print, insert, delete, bfs, dfs
    //can't insert a node that is already in the graph
    private ArrayList<ArrayList<Integer>> graph = new ArrayList<>();
    private Integer v, e;
    private HashMap<ArrayList<Integer>, Integer> edgesWeights = new HashMap<>();
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
    private boolean isEmpty(){
        return graph.isEmpty();
    }
    private boolean contains(Integer vertex1, Integer vertex2){
        if(graph.get(vertex1) == null) return false;
        return graph.get(vertex1).contains(vertex2);
    }
    private void clear(){
        graph.clear();
        edgesWeights.clear();
    }
    public void print(){
        System.out.println(graph);
        for (Map.Entry<ArrayList<Integer>, Integer> w : edgesWeights.entrySet()){
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
        edgesWeights.put(key, weight);
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
        if(isNegative(v) || isNegative(e) || e < v || e > v*(v-1)) {
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
    //Single source shortest-path algorithm with O(v^2) time complexity
    public void dijkstra(Integer sourceNode, ArrayList<Integer> costs, ArrayList<Integer> parents){
        if(isEmpty()) return;
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
                        Integer currentCost = costs.get(child);
                        Integer newCost = costs.get(sourceNode);
                        if(newCost != Integer.MAX_VALUE) newCost += edgesWeights.get(key);
                        costs.set(child, Integer.min(currentCost, newCost));
                        if(newCost < currentCost) parents.set(child, sourceNode);
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
    private void relaxation(ArrayList<Integer> parents, ArrayList<Integer> costs, boolean setParent) {
        for (Map.Entry<ArrayList<Integer>, Integer> edgeWeight : edgesWeights.entrySet()){
            Integer from = edgeWeight.getKey().get(0);
            Integer to = edgeWeight.getKey().get(1);
            Integer w = edgeWeight.getValue();
            Integer currentCost = costs.get(to);
            Integer newCost = costs.get(from);
            if(costs.get(from) != Integer.MAX_VALUE) newCost += w;
            costs.set(to, Math.min(currentCost, newCost));
            if((newCost < currentCost) && setParent) parents.set(to, from);
        }
    }
    /*Bellman-ford Algorithm :
    * single source shortest-path algorithm
    * takes O(n^2) time for regular graph and O(n^3) for complete graph
    * can detect if there is a negative weighted cycle in the graph
    **/
    public boolean bellmanFord(Integer sourceNode, ArrayList<Integer> costs, ArrayList<Integer> parents){
        if (isEmpty()) return true;
        for (int i = 0; i < v; i++){
            costs.add(i, Integer.MAX_VALUE);
            parents.add(i, null);
        }
        costs.set(sourceNode, 0);
        for(int i = 0; i < v-1; i++){
            relaxation(parents, costs, true);
        }
        //doing another iteration on this temp array and find if the costs have been changed or not.
        //if they are changed that means that there are negative weighted cycles, and we will return false.
        ArrayList<Integer> temp = (ArrayList<Integer>) costs.clone();
        relaxation(parents, temp, false);
        return temp.equals(costs);
    }
    /*Floyd-warshall Algorithm :
    * all pairs shortest-path algorithm
    * runs in O(n^3)
    * */
    private void floydWarshallLoop(ArrayList<ArrayList<Integer>> costs, ArrayList<ArrayList<Integer>> predecessors){
        for (int k = 0; k < v; k++){
            for(int i = 0; i < v; i++){
                for (int j = 0; j < v; j++){
                    if (i == k || j == k || i == j) continue;
                    Integer cost1 = costs.get(i).get(k), cost2 = costs.get(k).get(j);
                    Integer newCost = Integer.MAX_VALUE, currentCost = costs.get(i).get(j);
                    if(cost1 != Integer.MAX_VALUE && cost2 != Integer.MAX_VALUE) newCost = cost1 + cost2;
                    costs.get(i).set(j, Math.min(newCost, currentCost));
                    if (newCost < currentCost) {
                        predecessors.get(i).set(j, k);
                    }
                }
            }
        }
    }
    public boolean floydWarshall(ArrayList<ArrayList<Integer>> costs, ArrayList<ArrayList<Integer>> predecessors){
        if(isEmpty()) return true;
        for(int i = 0; i < v; i++){
            costs.add(new ArrayList<>());
            predecessors.add(new ArrayList<>());
            for(int j = 0; j < v; j++){
                predecessors.get(i).add(null);
                if(i == j) {
                    costs.get(i).add(0);
                    continue;
                }
                costs.get(i).add(Integer.MAX_VALUE);
            }
        }
        for(int i = 0; i < v; i++){
            ArrayList<Integer> children = graph.get(i);
            if(children == null) continue;
            for (Integer child : children){
                ArrayList<Integer> key = new ArrayList<>(); key.add(i); key.add(child);
                costs.get(i).set(child, edgesWeights.get(key));
                predecessors.get(i).set(child, i);
            }
        }
        floydWarshallLoop(costs, predecessors);
        ArrayList<ArrayList<Integer>> clonedCosts = clone(costs);
        ArrayList<ArrayList<Integer>> clonedPredecessors = clone(predecessors);
        floydWarshallLoop(clonedCosts, clonedPredecessors);
        return clonedCosts.equals(costs);
    }
    private ArrayList<ArrayList<Integer>> clone(ArrayList<ArrayList<Integer>> arrays){
        ArrayList<ArrayList<Integer>> clonedArray = new ArrayList<>();
        for (ArrayList<Integer> array : arrays){
            clonedArray.add((ArrayList<Integer>) array.clone());
        }
        return clonedArray;
    }
}