package util;

/**
 * Based on the following paper:
 * A fast heuristic for the prize-collecting Steiner tree problem.
 * (Akhmedov et al., 2014)
 * @author jbetzend
 */
public class PrizeCollecting
{
    /**
     * Prize-collecting algorithm
     * @param adjList
     * @param prizes
     */
    public static AdjacencyList MSTPCST(AdjacencyList adjList, boolean[] isTarget, float[] prizes)
    {
        /* Init */
        AdjacencyList g = adjList;
        
        int targetCount = 0;
        for (Boolean b: isTarget){
            if (b) {
                targetCount += 1;
            }
        }
        
        
        /* targets is called S in the paper */
        int[] targets = new int[targetCount];
        int   l1      = 0;
        
        for (int i = 0; i < isTarget.length; i++)
        {
            if (isTarget[i])
            {
                targets[l1] = i; l1++;
            }
        }
        
        assert targets.length == targetCount;
        
        //int[] s       = targets.clone(); // keep original targets
        
        /* C <- ∞ */
        float c;
        c = Float.POSITIVE_INFINITY;
        
        /* C' <- Σ(e ∈ E) c_e */
        float c_prime = 0.0f;
        for (int i = 0; i < g.getEdgeCount(); i++)
            { c_prime += g.getWeight(i); }
        
        g = phaseOne(c, c_prime, targets, g);
        //g = phaseTwo(g, prizes);
        
        return g;
    }
    
    public static AdjacencyList phaseOne(float c, float c_prime, int[] targets, AdjacencyList g)
    {
        /* Phase I */
        System.out.println("Phase I");
        
        while (c_prime <= c)
        {
            System.out.println("Start of WHILE");
            
            /* C <- C' */
            c = c_prime;
            
            /* Construct complete Graph G' = (V', E') where V' = S and 
               and each arc in E' corresponds to the shortest path in G */
            /*
            Targets richtig verwendet?
            */
            Dijkstra[] dijkstras = Dijkstra.allToAll(g);
            AdjacencyList g_prime = AdjacencyList.toCompleteGraph(g.getNodeCount(), targets,
                                                                  dijkstras);
            
            /* Solve MST on G' and obtain tree T */
//            SteinerTree t = g_prime.mst(targets);
//            System.out.println(t);

            AdjacencyList t_prime = g_prime.mst(targets, g, dijkstras);
            
            /* C' <- Σ(e ∈ T) c_e */ 
            // c_prime = t.getTotalWeight(false);
            
            /* Convert T into original Graph G and obtain a subgraph T' */
            //AdjacencyList t_prime = t.toGraph();
            
            /* S <- all nodes in T' */
            targets = t_prime.getConnectedNodes();
            
            g = t_prime;
        }
        
        return g;
    }
    
    public static AdjacencyList phaseTwo(AdjacencyList g, float[] prizes)
    {
        /* Phase II */
        System.out.println("Phase II");
        
        for (int node : g.getConnectedNodes())
        {
            /* Only check/prune non-steiner nodes */
            if (prizes[node] >= 0)
            {
                float cc = g.getConnectionCost(node);
            
                if (prizes[node] < cc)
                    { g.prune(node); }
            }
        }
        
        return g;
    }
}
