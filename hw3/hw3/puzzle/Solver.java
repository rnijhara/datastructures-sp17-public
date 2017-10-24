package hw3.puzzle;

import edu.princeton.cs.algs4.MinPQ;
import java.util.LinkedList;
/**
 * Created by XWEN on 3/20/2017.
 */
public class Solver {
    private int totalMoves;
    private LinkedList<WorldState> solution;

    /* SearchNodes contain the current WorldState, its previous SearchNode and the
       moves required to get to its WorldState
     */
    private class SearchNode implements Comparable<SearchNode> {
        WorldState state;
        int movesReq;
        SearchNode prev;
        int priority;

        SearchNode(WorldState curr, int moves, SearchNode prev) {
            state = curr;
            movesReq = moves;
            this.prev = prev;
            priority = movesReq + state.estimatedDistanceToGoal();
        }

        @Override
        public int compareTo(SearchNode other) {
            if (this.priority == other.priority) {
                return 0;
            } else if (this.priority > other.priority) {
                return 1;
            } else {
                return -1;
            }
        }
    }

    /* Assumes a WorldState puzzle can be solved and computes the solution */
    public Solver(WorldState initial) {
        SearchNode initialSearch = new SearchNode(initial, 0, null);
        MinPQ<SearchNode> aStar = new MinPQ<>();
        boolean solved = false;
        aStar.insert(initialSearch);
        while (!solved) {
            SearchNode next = aStar.delMin();
            if (next.state.isGoal()) {
                solved = true;
                totalMoves = next.movesReq;
                solution = new LinkedList<>();
                for (SearchNode n = next; n != null; n = n.prev) {
                    solution.addFirst(n.state);
                }
            } else {
                for (WorldState neighbor : next.state.neighbors()) {
                    SearchNode toAdd = new SearchNode(neighbor, next.movesReq + 1, next);
                    if (next.prev == null) {
                        aStar.insert(toAdd);
                    } else if (!neighbor.equals(next.prev.state)) {
                        aStar.insert(toAdd);
                    }

                }
            }
        }
    }

    /* Returns number of moves required to solve the puzzle */
    public int moves() {
        return totalMoves;
    }

    /* Returns an iterable of the step-wise solution from initial to sgoal */
    public Iterable<WorldState> solution() {
        return solution;
    }
}
