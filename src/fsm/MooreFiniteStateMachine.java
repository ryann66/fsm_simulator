package fsm;

/**
 * Moore FSM, output values are tied to states, not edges
 */
public interface MooreFiniteStateMachine<K, V> extends FiniteStateMachine<K, V> {
    /**
     * Adds a new directed edge between the nodes with the given two IDs
     * @param originId the node that the edge starts at
     * @param destId the node that the edge ends at
     * @param label the label of the edge
     * @throws IllegalArgumentException if originId or destId do not exist
     */
    void link(int originId, int destId, K label) throws IllegalArgumentException;

    /**
     * Adds a new node with the given id and value
     * @param id the id of the node to create
     * @param value the value of the created node
     * @throws IllegalArgumentException if the given id is already in use
     */
    void add(int id, V value) throws IllegalArgumentException;
}
