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
    public static void MSTPCST(AdjacencyList adjList, int[] prizes)
    {
        /* Init */
        AdjacencyList g = adjList;
        
        /* compute targets from prizes */
        boolean[] isTarget = new boolean[prizes.length];
        for (int i = 0; i < prizes.length; i++)
        {
            /* Non-negative prize means it's a target */
            isTarget[i] = (prizes[i] >= 0);
        }
        
        int targetCount = 0;
        for (int i = 0; i < isTarget.length; i++)
            { if (isTarget[i]) { targetCount++; } }
        
        /* targets is called S in the paper */
        int[] targets = new int[targetCount];
        int   l1      = 0;
        
        for (int i = 0; i < isTarget.length; i++)
            { if (isTarget[i]) { targets[l1] = i; l1++; } }
        
        int[] s       = targets.clone(); // keep original targets
        
        /* C <- ∞ */
        float c;
        c = Float.POSITIVE_INFINITY;
        
        /* C' <- Σ(e ∈ E) c_e */
        float c_prime = (float) 0.0;
        for (int i = 0; i < g.getEdgeCount(); i++)
            { c_prime += g.getWeight(i); }
        
        /* Phase I */
        System.out.println("Phase I");
        
        while (c_prime <= c)
        {
            /* C <- C' */
            c = c_prime;
            
            /* Construct complete Graph G' = (V', E') where V' = S and 
               and each arc in E' corresponds to the shortest path in G */
            Dijkstra[] dijkstras = Dijkstra.allToAll(g);
            AdjacencyList g_prime = AdjacencyList.toCompleteGraph(targets,
                                                                  dijkstras);
            
            /* Solve MST on G' and obtain tree T */
            SteinerTree t = g_prime.mst(targets);
            
            /* C' <- Σ(e ∈ T) c_e */ 
            c_prime = t.getTotalWeight();
            
            /* Convert T into original Graph G and obtain a subgraph T' */
            AdjacencyList t_prime = t.toGraph();
            
            /* S <- all nodes in T' */
            targets = t_prime.getConnectedNodes();
            
            g = t_prime;
        }
        
        /* Phase II */
        System.out.println("Phase II");
        
        for (int node : g.getConnectedNodes())
        {
            /* Only check/prune non-steiner nodes */
            if (prizes[node] >= 0)
            {
                float cc = g.getConnectionCost(node);
            
                if (prizes[node] <= cc)
                    { g.prune(node); }
            }
        }
    }
}
