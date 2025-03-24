package fsm;

import java.util.*;

public class Moore_NFA<K, V> implements MooreFiniteStateMachine<K, V> {
    final Map<Integer, Moore_NFA_State<K, V>> nfaBuild;

    Set<Moore_NFA_State<K, V>> currentStates;
    final Moore_NFA_State<K, V> origin;

    public Moore_NFA(int id, V value) {
        nfaBuild = new HashMap<>();
        currentStates = new HashSet<>();
        origin = new Moore_NFA_State<>(id, value);
        nfaBuild.put(id, origin);
        currentStates.add(origin);
    }

    @Override
    public Set<? extends State<K, V>> step() {
        Set<Moore_NFA_State<K, V>> newStates = new HashSet<>();
        for (Moore_NFA_State<K, V> mnfas : currentStates) {
            newStates.addAll(mnfas.next.values());
        }
        currentStates = newStates;
        return currentStates;
    }

    @Override
    public Set<? extends State<K, V>> step(K input) {
        Set<Moore_NFA_State<K, V>> newStates = new HashSet<>();
        for (Moore_NFA_State<K, V> mnfas : currentStates) {
            if (mnfas.next.containsKey(input))
                newStates.add(mnfas.next.get(input));
        }
        currentStates = newStates;
        return currentStates;
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

    private static class Moore_NFA_State<K, V> implements State<K, V> {
        final int id;
        final V value;
        final Map<K, Moore_NFA_State<K, V>> next;

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
