import java.util.*;

public class GraphWithWeight<T> extends Graph<T> {

    @Override
    public synchronized void addEdge(T fromT, T toT) {
        addEdge(fromT, toT, 0);
    }

    public synchronized void addEdge(T fromT, T toT, int weight) {
        Vertex<T> vertexFrom = getByValue(fromT);
        Vertex<T> vertexTo = getByValue(toT);
        vertexFrom.addEdge(vertexTo, weight);
        if (!isDirected) {
            vertexTo.addEdge(vertexFrom, weight);
        }
    }

    @Override
    public synchronized ArrayList<T> getPath(T from, T to){
        return dikstr(from,to);
    }

    private ArrayList<T> dikstr(T start, T end) {
        distant = new HashMap<>();

        visited = new HashSet<>();
        paths = new HashMap<>();

        Vertex<T> startVertex = getByValue(start);
        Vertex<T> endVertex = getByValue(end);

        queueD = new PriorityQueue<>(new Comparator<Vertex<T>>() {
            @Override
            public int compare(Vertex<T> o1, Vertex<T> o2) {
                int weght1 = distant.containsKey(o1.value) ? distant.get(o1.value) : Integer.MAX_VALUE;
                int weght2 = distant.containsKey(o2.value) ? distant.get(o2.value) : Integer.MAX_VALUE;
                return weght1 - weght2;
            }
        });

        queueD.add(startVertex);
        distant.put(start,0);

        while (!queueD.isEmpty()) {
            Vertex<T> current = queueD.poll();

            visited.add(current.value);
            if (current.equals(endVertex)) {
                return restorePath(startVertex, endVertex);
            }

            for (Edge<T> edge : current.edges) {
                if (visited.contains(edge.vertexTo.value)) {
                    continue;
                }

                int distantsValue = getDistants(edge.vertexTo);
                if (distantsValue == Integer.MAX_VALUE) {
                    int suggestDistant = distantsValue + edge.weight;

                    if (distantsValue > suggestDistant) {
                        distant.put(edge.vertexTo.value, suggestDistant);
                        paths.put(edge.vertexTo.value, current.value);
                    }
                    queueD.add(edge.vertexTo);
                }
            }
        }
        return null;
    }

    private int getDistants(Vertex<T> vertex) {
        return distant.containsKey(vertex.value) ? distant.get(vertex.value) : Integer.MAX_VALUE;
    }
}
