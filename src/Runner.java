import java.io.File;

/**
 * Created by jonahrr on 10/19/15.
 */
public class Runner {

    public static void main(String[] args) {
        StringGraph graph = new StringGraph();
        graph.importText("/texts/Earnest.txt");
        System.out.println(graph.buildChain(10));
    }

}
