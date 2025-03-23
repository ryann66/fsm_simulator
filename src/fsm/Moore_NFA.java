package fsm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Moore_NFA<K, V> implements MooreFiniteStateMachine<K, V> {
    final Map<Integer, Moore_NFA_State<K, V>> nfaBuild;

    final Set<Moore_NFA_State<K, V>> currentStates;
    final Moore_NFA_State<K, V> origin;

    public Moore_NFA(int id, V value) {
        nfaBuild = new HashMap<>();
        currentStates = new HashSet<>();
        origin = new Moore_NFA_State<>(id, value);
        nfaBuild.put(id, origin);
        currentStates.add(origin);
    }

    @Override
    public Set<State<V>> step() {
        // todo: implement
    }

    @Override
    public Set<State<V>> step(K input) {
        // todo: implement
    }

    @Override
    public boolean isDead() {
        return currentStates.isEmpty();
    }

    @Override
    public void reset() {
        currentStates.clear();
        currentStates.add(origin);
    }

    @Override
    public void link(int originId, int destId, K label) throws IllegalArgumentException {
        // todo: implement
    }

    @Override
    public void add(int id, V value) throws IllegalArgumentException {
        // todo: implement
    }


    private static class Moore_NFA_State<K, V> implements State<V> {
        final int id;
        final V value;
        final Map<K, V> next;

        private Moore_NFA_State(int id, V value) {
            this.id = id;
            this.value = value;
            this.next = new HashMap<>();
        }

        @Override
        public V output() {
            return value;
        }

        @Override
        public int id() {
            return id;
        }
    }
}
