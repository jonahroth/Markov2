import java.io.File;

/**
 * Created by jonahrr on 10/19/15.
 */
public class Runner {

    public static void main(String[] args) {
        StringGraph graph = new StringGraph(3);
        graph.importText("texts/Earnest.txt");
        System.out.println(graph.buildChain(1000));
    }

}
