import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

public class GraphTest {

    Graph<String> graphDirected = new Graph<String>(true);
    Graph<String> graphUnDirected = new Graph<String>();
    GraphWithWeight<String> graphWithWeight = new GraphWithWeight<>();

    @Before
    public void initGraph() {
        initVertex(graphDirected);
        initEdges(graphDirected);
        initVertex(graphUnDirected);
        initEdges(graphUnDirected);
        initVertex(graphWithWeight);
        initEdgesWidth(graphWithWeight);
    }

    private void initVertex(Graph<String> graph) {
        graph.addVertex("1");
        graph.addVertex("2");
        graph.addVertex("3");
        graph.addVertex("4");
        graph.addVertex("5");
        graph.addVertex("6");
    }

    private void initEdges(Graph<String> graph) {
        graph.addEdge("1", "2");
        graph.addEdge("1", "3");
        graph.addEdge("2", "3");
        graph.addEdge("2", "3");
        graph.addEdge("3", "4");
        graph.addEdge("3", "5");
        graph.addEdge("2", "4");
        graph.addEdge("4", "6");
        graph.addEdge("5", "6");
    }

    private void initEdgesWidth(GraphWithWeight<String> graph) {
        graph.addEdge("1", "2", 1);
        graph.addEdge("1", "3", 3);
        graph.addEdge("2", "3", 1);
        graph.addEdge("2", "3", 1);
        graph.addEdge("3", "4", 2);
        graph.addEdge("3", "5", 3);
        graph.addEdge("2", "4", 3);
        graph.addEdge("4", "6", 2);
        graph.addEdge("5", "6", 2);
    }

    @Test
    public void test_find_path_in_directed_graph() {
        ArrayList<String> arrayList = graphDirected.getPath("1", "6");
        StringBuilder stb = new StringBuilder();
        arrayList.forEach(stb::append);
        assertEquals("12356", stb.toString());
    }

    @Test
    public void test_find_another_path_in_directed_graph() {
        ArrayList<String> arrayList = graphDirected.getPath("5", "1");
        assertEquals(null, arrayList);
    }

    @Test
    public void test_find_path_in_undirected_graph() {
        ArrayList<String> arrayList = graphUnDirected.getPath("1", "6");
        StringBuilder stb = new StringBuilder();
        arrayList.forEach(stb::append);
        assertEquals("12356", stb.toString());
    }

    @Test
    public void test_find_another_path_in_undirected_graph() {
        ArrayList<String> arrayList = graphUnDirected.getPath("5", "1");
        StringBuilder stb = new StringBuilder();
        arrayList.forEach(stb::append);
        assertEquals("531", stb.toString());
    }

    @Test
    public void test_find_path_in_graph_with_weight() {
        ArrayList<String> arrayList = graphWithWeight.getPath("1", "6");
        StringBuilder stb = new StringBuilder();
        arrayList.forEach(stb::append);
        assertEquals("1246", stb.toString());
    }

    @Test
    public void test_find_another_path_in_graph_with_weight() {
        ArrayList<String> arrayList = graphWithWeight.getPath("6", "1");
        StringBuilder stb = new StringBuilder();
        arrayList.forEach(stb::append);
        assertEquals("6431", stb.toString());
    }

    @Test
    public void test_user_function_in_graph() {
        Function<String, String> userFunction = t ->
            t = "vertexName-" + t + ";";
        graphDirected.traverse(userFunction);
        ArrayList<String> arrayList = graphDirected.getPath("vertexName-1;", "vertexName-6;");
        StringBuilder stb = new StringBuilder();
        arrayList.forEach(stb::append);
        assertEquals("vertexName-1;vertexName-2;vertexName-3;vertexName-5;vertexName-6;", stb.toString());
    }
}
