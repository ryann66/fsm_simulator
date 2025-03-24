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
        if (!nfaBuild.containsKey(originId) || !nfaBuild.containsKey(destId)) {
            throw new IllegalArgumentException("Using invalid node id");
        }
        Moore_NFA_State<K, V> origin = nfaBuild.get(originId);
        if (origin.next.containsKey(label)) {
            throw new IllegalArgumentException("Edge already defined");
        }
        origin.next.put(label, nfaBuild.get(destId));
    }

    @Override
    public void unlink(int originId, int destId, K label) throws IllegalArgumentException {
        Moore_NFA_State<K, V> origin = nfaBuild.get(originId);
        if (origin == null) throw new IllegalArgumentException("Invalid originId");
        Moore_NFA_State<K, V> dest = origin.next.get(label);
        if (dest == null) throw new IllegalArgumentException("Invalid label");
        if (dest.id != destId) throw new IllegalArgumentException("Invalid destId");
        origin.next.remove(label);
    }

    @Override
    public void add(int id, V value) throws IllegalArgumentException {
        if (nfaBuild.containsKey(id)) {
            throw new IllegalArgumentException("ID already defined");
        }
        nfaBuild.put(id, new Moore_NFA_State<>(id, value));
    }

    /**
     * Warning: this operation is very expensive in this implementation
     */
    @Override
    public void remove(int id) throws IllegalArgumentException {
        Moore_NFA_State<K, V> origin = nfaBuild.remove(id);
        if (origin == null) throw new IllegalArgumentException("Invalid id");
        for (Moore_NFA_State<K, V> stat : nfaBuild.values()) {
            stat.next.values().removeIf(node -> node.id == id);
        }
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
