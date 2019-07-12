import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;


public class Graph<T> {

    HashMap<T, Vertex<T>> vertices;
    HashSet<T> visited;
    HashMap<T, T> paths;
    HashMap<T, Integer> distant;
    boolean isDirected;

    PriorityQueue<Vertex<T>> queueD;

    public Graph() {
        this.isDirected = false;
        vertices = new HashMap<>();
    }

    public Graph(boolean isDirected) {
        this.isDirected = isDirected;
        vertices = new HashMap<>();
    }

    Vertex<T> getByValue(T value) {
        if (!vertices.containsKey(value))
            throw new IllegalArgumentException("Non-existing vertex with value " + value);
        return vertices.get(value);
    }

    public synchronized void addVertex(T t) {
        Vertex<T> vertex = new Vertex<>(t);
        vertices.put(t, vertex);
    }

    public synchronized void addEdge(T fromT, T toT) {
        Vertex<T> vertexFrom = getByValue(fromT);
        Vertex<T> vertexTo = getByValue(toT);
        vertexFrom.addEdge(vertexTo);
        if (!isDirected) {
            vertexTo.addEdge(vertexFrom);
        }
    }

    @Override
    public String toString() {
        return vertices.values().stream()
                .map(Vertex::toString)
                .collect(Collectors.joining(";"));
    }

    private ArrayList<T> bfs(T from, T to) {
        visited = new HashSet<>();
        paths = new HashMap<>();

        Queue<Vertex<T>> queue = new LinkedList<>();

        Vertex<T> verticesFrom = getByValue(from);
        Vertex<T> verticesTo = getByValue(to);

        queue.add(verticesFrom);

        boolean isFound = false;
        while (!queue.isEmpty()) {
            Vertex<T> current = queue.poll();
            if (current.equals(verticesTo)) {
                isFound = true;
                break;
            }

            visited.add(current.value);

            if (!current.edges.isEmpty()) {
                for (Edge edge : current.edges) {
                    if (!visited.contains(edge.vertexTo.value)) {
                        paths.put((T) edge.vertexTo.value, current.value);
                        queue.add(edge.vertexTo);
                    }
                }
            }
        }

        return isFound ? restorePath(verticesFrom, verticesTo) : null;

    }

    public synchronized ArrayList<T> getPath(T from, T to){
        return bfs(from,to);
    }

    protected ArrayList<T> restorePath(Vertex<T> startVertices, Vertex<T> endVertices) {
        ArrayList<T> result = new ArrayList<>();
        T current = paths.get(endVertices.value);
        result.add(0, endVertices.value);
        while (!current.equals(startVertices.value)) {
            result.add(0, current);
            current = paths.get(current);
        }
        result.add(0, startVertices.value);
        return result;
    }

    public synchronized void traverse(Function<T,T> function){
        HashMap<T, Vertex<T>> newVertices = new HashMap<>();

        vertices.keySet().forEach(k -> {
            T newKey = function.apply(k);
            Vertex<T> vertex = vertices.get(k);
            vertex.value = function.apply(vertex.value);
            newVertices.put(newKey, vertex);
        });

        vertices = newVertices;
    }

    //Vertex
    public static class Vertex<T> {
        T value;
        ArrayList<Edge<T>> edges;

        public Vertex(T value) {
            this.value = value;
            edges = new ArrayList<>();
        }

        public void addEdge(Vertex<T> vertexTo) {
            addEdge(vertexTo, 0);
        }

        public void addEdge(Vertex<T> vertexTo, int weight) {
            Edge<T> edge = new Edge<T>(vertexTo, weight);
            edges.add(edge);
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder("(" + value);

            if (!edges.isEmpty()) {
                String str = edges.stream()
                        .map(e -> e.vertexTo.value.toString())
                        .collect(Collectors.joining(", "));
                stringBuilder.append(" -> ").append(str);
            }

            return stringBuilder.append(")").toString();
        }
    }

    //Edge
    public static class Edge<T> {
        int weight;
        Vertex<T> vertexTo;

        public Edge(Vertex<T> vertexTo) {
            this(vertexTo, 0);
        }

        public Edge(Vertex<T> vertexTo, int weight) {
            this.weight = weight;
            this.vertexTo = vertexTo;
        }
    }
}
