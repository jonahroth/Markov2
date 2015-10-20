import java.io.File;

/**
 * Created by jonahrr on 10/19/15.
 */
public class Runner {

    public static void main(String[] args) {
        StringGraph graph = new StringGraph();
        graph.importText("/Users/jonahrr/Desktop/hp1.txt");
        System.out.println(graph.buildChain(10));
    }

}
