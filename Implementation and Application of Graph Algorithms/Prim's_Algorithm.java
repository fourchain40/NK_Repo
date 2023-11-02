import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Wiring {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int numJunctions = in.nextInt();
        int numConnections = in.nextInt();
        int count = 0;

        Graph graph = new Graph();
        graph.adjNodes = new HashMap<>();
        String start = "";
        Node sourceNode = new Node("");

        while (count < numJunctions) {
            String junctionLabel = in.next();

            if (count == 0) {
                start = junctionLabel;
            }

            String junctionType = in.next();

            if (!isValidJunctionType(junctionType)) {
                System.exit(0);
            }

            if (junctionType.charAt(0) == 's') {
                sourceNode = new Node(junctionLabel);
            }

            graph.addNode(junctionLabel, junctionType, sourceNode);
            count++;
        }

        count = 0;

        while (count < numConnections) {
            String node1Label = in.next();
            String node2Label = in.next();
            int weight = in.nextInt();

            if (areBothNodesLight(node1Label, node2Label, graph)) {
                Node node1 = graph.getNodeByLabel(node1Label);
                Node node2 = graph.getNodeByLabel(node2Label);

                if (node1.light.equals(node2.light)) {
                    graph.addEdge(node1Label, node2Label, weight);
                }
            } else if (isNodeLight(node1Label, graph)) {
                Node node1 = graph.getNodeByLabel(node1Label);

                if (node1.light.equals(graph.getNodeByLabel(node2Label))) {
                    graph.addEdge(node1Label, node2Label, weight);
                }
            } else if (isNodeLight(node2Label, graph)) {
                Node node2 = graph.getNodeByLabel(node2Label);

                if (node2.light.equals(graph.getNodeByLabel(node1Label))) {
                    graph.addEdge(node1Label, node2Label, weight);
                }
            } else if (!areBothNodesSwitch(node1Label, node2Label, graph)) {
                graph.addEdge(node1Label, node2Label, weight);
            }

            count++;
        }

        graph.Prims(new Node(start));
        System.out.println(graph.total);
    }

    static class Graph {
        HashMap<Node, ArrayList<NW>> adjNodes;
        PriorityQueue<NW> PQBox = new PriorityQueue<>();
        PriorityQueue<NW> PQSwitch = new PriorityQueue<>();
        ArrayList<Node> visited = new ArrayList<>();
        int total = 0;

        public void addNode(String label, String type, Node lightNode) {
            adjNodes.putIfAbsent(new Node(label, type, lightNode), new ArrayList<>());
        }

        public void addEdge(String label1, String label2, int weight) {
            Node node1 = new Node(label1);
            Node node2 = aNode(label2);
            adjNodes.get(node1).add(new NW(node2, weight));
            adjNodes.get(node2).add(new NW(node1, weight));
        }

        public String getEdge(String label) {
            for (Node node : adjNodes.keySet()) {
                if (node.label.equals(label)) {
                    return node.type;
                }
            }
            return null;
        }

        public Node getNodeByLabel(String label) {
            for (Node node : adjNodes.keySet()) {
                if (node.label.equals(label)) {
                    return node;
                }
            }
            return null;
        }


        public void Prims(Node s) {
            NW start = new NW(s, 0);
            PQBox.add(start);
            while (PQBox.size() != 0) {
                PriorityQueue<NW> PQBox1 = new PriorityQueue<>();
                for (NW n : PQBox) {
                    PQBox1.add(n);
                }
                PQBox = PQBox1;

                NW v = PQBox.poll();

                if (visited.contains(v.node)) {
                    continue;
                }
                visited.add(v.node);
                total = total + v.weight;
                for (NW n : adjNodes.get(v.node)) {
                    if (!(visited.contains(n.node))) {
                        boolean found = false;
                        for (NW i : PQBox) {
                            if (n.node.equals(i.node)) {
                                found = true;
                                if (n.weight < i.weight) {
                                    i.weight = n.weight;
                                    i.node.pi = v.node;
                                    found = true;
                                }
                            }
                        }
                        for (NW k : PQSwitch) {
                            if (n.node.equals(k.node)) {
                                found = true;
                                if (n.weight < k.weight) {
                                    k.weight = n.weight;
                                    k.node.pi = v.node;
                                    found = true;
                                }
                            }
                        }
                        if (!found) {
                            if (getEdge(n.node.label).equals("switch")) {
                                PQSwitch.add(new NW(n.node, n.weight));
                                n.node.pi = v.node;
                            } else {
                                PQBox.add(new NW(n.node, n.weight));
                                n.node.pi = v.node;
                            }
                        }
                    }
                }
            }

            while (PQSwitch.size() != 0) {
                PriorityQueue<NW> PQSwitch1 = new PriorityQueue<>();
                for (NW n : PQSwitch) {
                    PQSwitch1.add(n);
                }
                PQSwitch = PQSwitch1;

                NW v = PQSwitch.poll();
                if (visited.contains(v.node)) {
                    continue;
                }
                visited.add(v.node);
                total = total + v.weight;
                for (NW n : adjNodes.get(v.node)) {
                    if (!(visited.contains(n.node))) {
                        boolean found = false;
                        for (NW i : PQSwitch) {
                            if (n.node.equals(i.node)) {
                                found = true;
                                if (n.weight < i.weight) {
                                    i.weight = n.weight;
                                    i.node.pi = v.node;
                                    found = true;
                                }
                            }
                        }
                        if (!found) {
                            PQSwitch.add(new NW(n.node, n.weight));
                            n.node.pi = v.node;
                        }
                    }
                }
            }
        }
    }

    

    // The isValidJunctionType method checks if the junction type is valid.
    static boolean isValidJunctionType(String type) {
        return type.charAt(0) == 'l' || type.charAt(0) == 'b' || type.charAt(0) == 's' || type.charAt(0) == 'o';
    }

    // The areBothNodesLight method checks if both nodes are light nodes.
    static boolean areBothNodesLight(String node1Label, String node2Label, Graph graph) {
        return graph.getEdge(node1Label).equals("light") && graph.getEdge(node2Label).equals("light");
    }

    // The isNodeLight method checks if a node is a light node.
    static boolean isNodeLight(String nodeLabel, Graph graph) {
        return graph.getEdge(nodeLabel).equals("light");
    }

    // The areBothNodesSwitch method checks if both nodes are switch nodes.
    static boolean areBothNodesSwitch(String node1Label, String node2Label, Graph graph) {
        return graph.getEdge(node1Label).equals("switch") && graph.getEdge(node2Label).equals("switch");
    }
}

class NW implements Comparable<NW> {
    Node node;
    int weight;

    public NW(Node node, int weight) {
        this.node = node;
        this.weight = weight;
    }

    @Override
    public int hashCode() {
        return Objects.hash(node, weight);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        NW other = (NW) obj;
        return Objects.equals(node, other.node) && weight == other.weight;
    }

    @Override
    public int compareTo(NW s1) {
        if (this.weight > s1.weight) {
            return 1;
        } else if (this.weight < s1.weight) {
            return -1;
        }
        return 0;
    }
}

class Node {
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

    public Node(String label, String type, Node light) {
        this.label = label;
        this.type = type;
        this.light = light;
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
