package fsm;

/**
 * Mealy FSM, output values are tied to edges, not states
 */
public interface MealyFiniteStateMachine<K, V> extends FiniteStateMachine<K, V> {
    /**
     * Links the nodes with the given two IDs with a new directed edge
     * @param originId the id of the node this edge starts at
     * @param destId the id of the node this edge ends at
     * @param label the label of this edge
     * @param value the output value of this edge
     * @throws IllegalArgumentException if originId or destId do not exist
     */
    void link(int originId, int destId, K label, V value) throws IllegalArgumentException;

    /**
     * Adds a new node with the given ID
     * @param id the id of the new node
     * @throws IllegalArgumentException if the given ID is already in use
     */
    void add(int id) throws IllegalArgumentException;
}