import java.util.*;
import java.util.PriorityQueue;

public class Scheduling {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        while (true) {
            int num = in.nextInt();
            int num1 = in.nextInt();
            int num2 = in.nextInt();

            if (num == 0 && num1 == 0 && num2 == 0) {
                break;
            }

            ArrayList<String> strlist = new ArrayList<>();
            ArrayList<String> classlist = new ArrayList<>();
            HashMap<String, ArrayList<String>> pairs = new HashMap<>();

            graph graph = new graph();
            graph residual = new graph();

            for (int count = 0; count < num; count++) {
                String num3 = in.next();
                String num4 = in.next();

                pairs.putIfAbsent(num3, new ArrayList<>());

                if (!strlist.contains(num3)) {
                    strlist.add(num3);
                }

                if (!pairs.get(num3).contains(num4)) {
                    pairs.get(num3).add(num4);
                }

                if (!classlist.contains(num4)) {
                    classlist.add(num4);
                }
            }

            int size = num1 + strlist.size() + 2;
            graph.adjMatrix = new int[size][size];
            graph.adjCap = new int[size][size];
            residual.adjMatrix = new int[size][size];
            residual.adjCap = new int[size][size];

            for (String s : pairs.keySet()) {
                graph.addCap(0, strlist.indexOf(s) + 1, num2);
                graph.addEdge(0, strlist.indexOf(s) + 1, 0);
                residual.addEdge(0, strlist.indexOf(s) + 1, num2);
                residual.addEdge(strlist.indexOf(s) + 1, 0, 0);

                for (String k : pairs.get(s)) {
                    graph.addCap(strlist.indexOf(s) + 1, size - classlist.indexOf(k) - 2, 1);
                    graph.addEdge(strlist.indexOf(s) + 1, size - classlist.indexOf(k) - 2, 0);
                    residual.addEdge(strlist.indexOf(s) + 1, size - classlist.indexOf(k) - 2, 1);
                    residual.addEdge(size - classlist.indexOf(k) - 2, strlist.indexOf(k) + 1, 0);
                }
            }

            for (String s : classlist) {
                graph.addCap(size - classlist.indexOf(s) - 2, size - 1, 0);
                graph.addEdge(size - classlist.indexOf(s) - 2, size - 1, 0);
                residual.addEdge(size - classlist.indexOf(s) - 2, size - 1, 0);
                residual.addEdge(size - 1, size - classlist.indexOf(s) - 2, 0);
            }

            for (int count = 0; count < num1; count++) {
                String label = in.next();
                int cap = in.nextInt();

                for (String n : classlist) {
                    if (n.equals(label)) {
                        graph.adjCap[size - classlist.indexOf(n) - 2][size - 1] = cap;
                        residual.adjMatrix[size - classlist.indexOf(n) - 2][size - 1] = cap;
                    }
                }
            }

            while (residual.DFS(residual) == 1) {
                FF(graph, residual);
                residual.visited = new ArrayList<>();
                residual.visited1 = new ArrayList<>();
            }

            int max = Arrays.stream(graph.adjMatrix[0]).sum();
            System.out.println(max == num2 * strlist.size() ? "Yes" : "No");
        }
    }

    public static void FF(graph G, graph R) {
        int min = R.visited1.get(1);
        for (int n = 1; n < R.visited1.size(); n++) {
            min = Math.min(min, R.visited1.get(n));
        }
        for (int i = 0; i < R.visited.size() - 1; i++) {
            G.adjMatrix[R.visited.get(i)][R.visited.get(i + 1)] += min;
        }
        for (int i = 0; i < R.visited.size() - 1; i++) {
            R.adjMatrix[R.visited.get(i)][R.visited.get(i + 1)] -= min;
            R.adjMatrix[R.visited.get(i + 1)][R.visited.get(i)] += min;
        }
    }

    static class graph {
        HashMap<Node, ArrayList<NW>> adjNodes;
        ArrayList<Integer> visited = new ArrayList<>();
        ArrayList<Integer> visited1 = new ArrayList<>();
        int[][] adjMatrix;
        int[][] adjCap;

        public void addNode(String label) {
            adjNodes.putIfAbsent(new Node(label), new ArrayList<>());
        }

        public void addCap(int label1, int label2, int c) {
            adjCap[label1][label2] = c;
        }

        public void addEdge(int label1, int label2, int w) {
            adjMatrix[label1][label2] = w;
        }

        public int DFS(graph G) {
            visited1.add(0);
            DFS_Visit(G, 0, visited);
            return visited.contains(adjMatrix.length - 1) ? 1 : 0;
        }

        public void DFS_Visit(graph G, int u, ArrayList<Integer> visited) {
            visited.add(u);

            if (visited.contains(adjMatrix.length - 1)) {
                return;
            }

            for (int i = 0; i < adjMatrix.length; i++) {
                if (visited.contains(adjMatrix.length - 1)) {
                    return;
                }
                if (adjMatrix[u][i] != 0 && !visited.contains(i)) {
                    visited1.add(adjMatrix[u][i]);
                    DFS_Visit(G, i, visited);
                }
            }

            if (visited.contains(adjMatrix.length - 1)) {
                return;
            }

            visited.remove(visited.size() - 1);
            visited1.remove(visited1.size() - 1);
        }
    }

    static class NW implements Comparable<NW> {
        Node Node;
        int weight;
        int cap;
        Node p;

        public NW(Node Node, int weight, int cap) {
            this.Node = Node;
            this.weight = weight;
            this.cap = cap;
        }

        @Override
        public int hashCode() {
            return Objects.hash(Node, weight);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            NW other = (NW) obj;
            return Objects.equals(Node, other.Node) && weight == other.weight;
        }

        @Override
        public int compareTo(NW s1) {
            return Integer.compare(this.weight, s1.weight);
        }
    }

    static class Node {
        String label;
        String color;
        int d;
        int f;
        Node pi;
        Node light;
        String type;

        Node(String label) {
            this.label = label;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Node other = (Node) obj;
            return Objects.equals(label, other.label);
        }

        @Override
        public int hashCode() {
            return Objects.hash(label);
        }
    }
}
