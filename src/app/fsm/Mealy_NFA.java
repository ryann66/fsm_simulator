package app.fsm;

import app.keychain.Keychain;

import java.util.*;

public class Mealy_NFA<K, V> implements MealyFiniteStateMachine<K, V> {
    final Map<Integer, Mealy_NFA_Node<K, V>> nfaBuild;

    Set<Mealy_NFA_State<K, V>> currentStates;
    final Mealy_NFA_Node<K, V> origin;
    final V initValue;

    public Mealy_NFA(int id, V value) {
        nfaBuild = new HashMap<>();
        currentStates = new HashSet<>();
        origin = new Mealy_NFA_Node<>(id);
        initValue = value;
        reset();
    }

    @Override
    public Set<? extends State<K, V>> step() {
        Set<Mealy_NFA_State<K, V>> newStates = new HashSet<>();
        for (Mealy_NFA_State<K, V> mnfas : currentStates) {
            for (Map.Entry<K, Pair<V, Mealy_NFA_Node<K, V>>> ent : mnfas.node.next.entrySet()) {
                newStates.add(new Mealy_NFA_State<>(ent.getValue().right, ent.getValue().left, mnfas, ent.getKey()));
            }
        }
        currentStates = newStates;
        return currentStates;
    }

    @Override
    public Set<? extends State<K, V>> step(K input) {
        Set<Mealy_NFA_State<K, V>> newStates = new HashSet<>();
        for (Mealy_NFA_State<K, V> mnfas : currentStates) {
            Pair<V, Mealy_NFA_Node<K, V>> node = mnfas.node.next.get(input);
            if (node != null) {
                newStates.add(new Mealy_NFA_State<>(node.right, node.left, mnfas, input));
            }
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
        currentStates.add(new Mealy_NFA_State<>(origin, initValue));
    }

    @Override
    public void link(int originId, int destId, K label, V value) throws IllegalArgumentException {
        if (!nfaBuild.containsKey(originId) || !nfaBuild.containsKey(destId)) {
            throw new IllegalArgumentException("Using invalid node id");
        }
        Mealy_NFA_Node<K, V> origin = nfaBuild.get(originId);
        if (origin.next.containsKey(label)) {
            throw new IllegalArgumentException("Edge already defined");
        }
        origin.next.put(label, new Pair<>(value, nfaBuild.get(destId)));
    }

    @Override
    public void unlink(int originId, int destId, K label) throws IllegalArgumentException {
        Mealy_NFA_Node<K, V> origin = nfaBuild.get(originId);
        if (origin == null) throw new IllegalArgumentException("Invalid originId");
        Pair<V, Mealy_NFA_Node<K, V>> dest = origin.next.get(label);
        if (dest == null) throw new IllegalArgumentException("Invalid label");
        if (dest.right.id != destId) throw new IllegalArgumentException("Invalid destId");
        origin.next.remove(label);
    }

    @Override
    public void add(int id) throws IllegalArgumentException {
        if (nfaBuild.containsKey(id)) throw new IllegalArgumentException("ID already exists");
        nfaBuild.put(id, new Mealy_NFA_Node<>(id));
    }

    @Override
    public void remove(int id) throws IllegalArgumentException {
        Mealy_NFA_Node<K, V> origin = nfaBuild.remove(id);
        if (origin == null) throw new IllegalArgumentException("Invalid id");
        for (Mealy_NFA_Node<K, V> stat : nfaBuild.values()) {
            stat.next.values().removeIf(pair -> pair.right.id == id);
        }
    }

    private static class Mealy_NFA_Node<K, V> {
        final Map<K, Pair<V, Mealy_NFA_Node<K, V>>> next;
        final int id;

        private Mealy_NFA_Node(int id) {
            next = new HashMap<>();
            this.id = id;
        }
    }

    private static class Mealy_NFA_State<K, V> implements State<K, V> {
        final Mealy_NFA_Node<K, V> node;
        final Keychain<K> keys;
        final V value;

        private Mealy_NFA_State(Mealy_NFA_Node<K, V> node, V value, Mealy_NFA_State<K, V> parent, K key) {
            if (parent == null) throw new IllegalArgumentException("Null parent");
            this.node = node;
            this.value = value;
            this.keys = Keychain.add(parent.keys, key);
        }

        private Mealy_NFA_State(Mealy_NFA_Node<K, V> node, V value) {
            this.node = node;
            this.value = value;
            this.keys = null;
        }

        @Override
        public V output() {
            return value;
        }

        @Override
        public int id() {
            return node.id;
        }

        @Override
        public Iterator<K> iterator() {
            return keys.iterator();
        }

        /**
         * This implementation of equals only checks id equality
         * This means that duplicate states are pruned, though it may result in only
         * one possible keypath being shown to the user (even if multiple are possible)
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj instanceof Mealy_NFA_State<?,?> stat) {
                return this.node.id == stat.node.id;
            }
            return false;
        }
    }

    private record Pair<A, B>(A left, B right) { }
}
