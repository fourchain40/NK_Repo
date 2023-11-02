import java.util.*;

public class Tasks {
    public static void main(String[] args) {
        Graph graph = new Graph();
        Scanner in = new Scanner(System.in);

        int num1 = in.nextInt();
        int num2 = in.nextInt();

        if (num1 > Math.pow(10, 4) || num2 > Math.pow(10, 6)) {
            System.exit(0);
        }

        for (int count = 0; count < num1; count++) {
            String x = in.next();
            graph.addNode(x);
        }

        for (int count = 0; count < num2; count++) {
            String x = in.next();
            String y = in.next();
            graph.addEdge(x, y);
        }

        graph.DFS();
        
        if (!graph.isDAG()) {
            System.out.println("IMPOSSIBLE");
            System.exit(0);
        }

        for (Node node : graph.getTopologicalList()) {
            System.out.print(node.label + " ");
        }
    }

    static class Graph {
        int time = 0;
        HashMap<Node, ArrayList<Node>> adjNodes;
        ArrayList<Node> topologicalList = new ArrayList<>();

        public void addNode(String label) {
            adjNodes.putIfAbsent(new Node(label), new ArrayList<>());
        }

        public void addEdge(String label1, String label2) {
            Node node1 = new Node(label1);
            Node node2 = new Node(label2);
            adjNodes.get(node1).add(node2);
        }

        public void DFS() {
            ArrayList<Node> visited = new ArrayList<>();

            for (Node node : adjNodes.keySet()) {
                if (!visited.contains(node)) {
                    DFSVisit(node, visited);
                }
            }
        }

        public void DFSVisit(Node u, ArrayList<Node> visited) {
            visited.add(u);
            time++;
            u.d = time;

            for (Node v : adjNodes.get(u)) {
                if (!visited.contains(v)) {
                    v.pi = u;
                    DFSVisit(v, visited);
                }
            }

            time++;
            u.f = time;
            topologicalList.add(0, u);
        }

        public boolean isDAG() {
            for (Node n : topologicalList) {
                for (Node n1 : adjNodes.get(n)) {
                    if (topologicalList.indexOf(n1) < topologicalList.indexOf(n)) {
                        return false;
                    }
                }
            }
            return true;
        }

        public ArrayList<Node> getTopologicalList() {
            return topologicalList;
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
    }
}
