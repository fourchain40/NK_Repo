package main;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class BoardGames {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        // Read the number of nodes (num1)
        int num1 = in.nextInt();
        if (num1 > 1000) {
            System.exit(0);
        }

        // Create a graph and initialize its adjacency matrix
        Graph graph = new Graph();
        graph.initAdjMatrix(num1);

        // Read the number of edges (num2)
        int num2 = in.nextInt();
        if (num2 > Math.pow(10, 6)) {
            System.exit(0);
        }

        // Read and add edges to the graph
        for (int count = 0; count < num2; count++) {
            int n1 = in.nextInt();
            int n2 = in.nextInt();
            if (n1 < 0 || n1 > num1 - 1 || n2 < 0 || n2 > num1 - 1) {
                System.exit(0);
            }
            graph.addEdge(n1, n2);
        }

        // Read the number of nodes to remove (num3)
        int num3 = in.nextInt();
        if (num3 > 1000) {
            System.exit(0);
        }

        // Remove nodes from the graph
        for (int count = 0; count < num3; count++) {
            int n = in.nextInt();
            graph.removeNode(n);
        }

        in.close();

        // Perform DFS and print the result
        graph.DFS(0);
    }

    static class Graph {
        int[][] adjMatrix;
        ArrayList<Integer> visited = new ArrayList<>();

        public void initAdjMatrix(int size) {
            adjMatrix = new int[size][size];
        }

        public void addEdge(int label1, int label2) {
            adjMatrix[label1][label2] = 1;
            adjMatrix[label2][label1] = 1;
        }

        public void removeNode(int n) {
            for (int i = 0; i < adjMatrix.length; i++) {
                adjMatrix[i][n] = 0;
                adjMatrix[n][i] = 0;
            }
        }

        public void DFS(int start) {
            DFS_Visit(start);
        }

        public void DFS_Visit(int u) {
            visited.add(u);
            if (visited.contains(adjMatrix.length - 1)) {
                StringBuilder ans = new StringBuilder();
                for (int a : visited) {
                    if (visited.indexOf(a) == visited.size() - 1) {
                        ans.append(a);
                    } else {
                        ans.append(a).append("-");
                    }
                }
                System.out.println(ans.toString());
                visited.remove(visited.size() - 1);
                return;
            }
            for (int i = 0; i < adjMatrix.length; i++) {
                if (adjMatrix[u][i] == 1 && !visited.contains(i)) {
                    DFS_Visit(i);
                }
            }
            visited.remove(visited.size() - 1);
        }
    }

    static class Node {
        String label;
        String color;
        int d;
        int f;
        Node pi;

        Node(String label) {
            this.label = label;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Node other = (Node) obj;
            return Objects.equals(label, other.label);
        }

        @Override
        public int hashCode() {
            return Objects.hash(label);
        }

        public int compareTo(Node another) {
            int num = Integer.valueOf(label);
            int num1 = Integer.valueOf(another.label);
            if (num < num1) {
                return -1;
            } else if (num > num1) {
                return 1;
            } else {
                return 0;
            }
        }
    }
}
