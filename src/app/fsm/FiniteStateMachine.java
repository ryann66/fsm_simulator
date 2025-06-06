package app.fsm;

import java.util.Iterator;
import java.util.Set;

/**
 * Abstract idea of a finite state machine
 * Can step through the machine without input to get all nodes that are
 * one step downstream of the current states
 * @param <K> the value of the edges, must implement hashCode and equals
 * @param <V> the value of the output
 */
public interface FiniteStateMachine<K, V> {
    /**
     * Takes one step from every current state
     * @return all states that are one step downstream of the current states
     */
    Set<? extends State<K, V>> step();

    /**
     * Takes one step from the current states along any edge that is equal
     * to the given input
     * @param input the edge value to follow
     * @return all states that are one step downstream along an edge labelled with input
     *         from the current states
     */
    Set<? extends State<K, V>> step(K input);

    /**
     * Returns true iff there are no active states
     * @return true iff there are no active states
     */
    boolean isDead();

    /**
     * Resets the machine, setting active states to only the origin state
     */
    void reset();

    /**
     * Wrapper state class to be returned by methods
     * @param <K> the input type
     * @param <V> the output type
     */
    interface State<K, V> extends Iterable<K> {
        /**
         * Returns the output value of this state
         * @return the output value of this state
         */
        V output();

        /**
         * Returns the ID that this state was constructed with
         * @return the ID that this state was constructed with
         */
        int id();

        /**
         * Returns an iterator of the inputs used to reach this state
         * @return an iterator of the inputs used to reach this state
         */
        @Override
        Iterator<K> iterator();
    }
}
