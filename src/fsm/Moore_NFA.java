package fsm;

import keychain.Keychain;

import java.util.*;

public class Moore_NFA<K, V> implements MooreFiniteStateMachine<K, V> {
    final Map<Integer, Moore_NFA_Node<K, V>> nfaBuild;

    Set<Moore_NFA_State<K, V>> currentStates;
    final Moore_NFA_Node<K, V> origin;

    public Moore_NFA(int id, V value) {
        nfaBuild = new HashMap<>();
        currentStates = new HashSet<>();
        origin = new Moore_NFA_Node<>(id, value);
        nfaBuild.put(id, origin);
        reset();
    }

    @Override
    public Set<? extends State<K, V>> step() {
        Set<Moore_NFA_State<K, V>> newStates = new HashSet<>();
        for (Moore_NFA_State<K, V> mnfas : currentStates) {
            for (Map.Entry<K, Moore_NFA_Node<K, V>> ent : mnfas.node.next.entrySet()) {
                newStates.add(new Moore_NFA_State<>(ent.getValue(), mnfas, ent.getKey()));
            }
        }
        currentStates = newStates;
        return currentStates;
    }

    @Override
    public Set<? extends State<K, V>> step(K input) {
        Set<Moore_NFA_State<K, V>> newStates = new HashSet<>();
        for (Moore_NFA_State<K, V> mnfas : currentStates) {
            Moore_NFA_Node<K, V> node = mnfas.node.next.get(input);
            if (node != null) {
                newStates.add(new Moore_NFA_State<>(node, mnfas, input));
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
        currentStates.add(new Moore_NFA_State<>(origin));
    }

    @Override
    public void link(int originId, int destId, K label) throws IllegalArgumentException {
        if (!nfaBuild.containsKey(originId) || !nfaBuild.containsKey(destId)) {
            throw new IllegalArgumentException("Using invalid node id");
        }
        Moore_NFA_Node<K, V> origin = nfaBuild.get(originId);
        if (origin.next.containsKey(label)) {
            throw new IllegalArgumentException("Edge already defined");
        }
        origin.next.put(label, nfaBuild.get(destId));
    }

    @Override
    public void unlink(int originId, int destId, K label) throws IllegalArgumentException {
        Moore_NFA_Node<K, V> origin = nfaBuild.get(originId);
        if (origin == null) throw new IllegalArgumentException("Invalid originId");
        Moore_NFA_Node<K, V> dest = origin.next.get(label);
        if (dest == null) throw new IllegalArgumentException("Invalid label");
        if (dest.id != destId) throw new IllegalArgumentException("Invalid destId");
        origin.next.remove(label);
    }

    @Override
    public void add(int id, V value) throws IllegalArgumentException {
        if (nfaBuild.containsKey(id)) {
            throw new IllegalArgumentException("ID already defined");
        }
        nfaBuild.put(id, new Moore_NFA_Node<>(id, value));
    }

    /**
     * Warning: this operation is very expensive in this implementation
     */
    @Override
    public void remove(int id) throws IllegalArgumentException {
        Moore_NFA_Node<K, V> origin = nfaBuild.remove(id);
        if (origin == null) throw new IllegalArgumentException("Invalid id");
        for (Moore_NFA_Node<K, V> stat : nfaBuild.values()) {
            stat.next.values().removeIf(node -> node.id == id);
        }
    }

    private static class Moore_NFA_Node<K, V> {
        final int id;
        final V value;
        final Map<K, Moore_NFA_Node<K, V>> next;

        private Moore_NFA_Node(int id, V value) {
            this.id = id;
            this.value = value;
            this.next = new HashMap<>();
        }
    }

    private static class Moore_NFA_State<K, V> implements State<K, V> {
        final Moore_NFA_Node<K, V> node;
        final Keychain<K> keys;

        private Moore_NFA_State(Moore_NFA_Node<K, V> node, Moore_NFA_State<K, V> parent, K key) {
            if (parent == null) throw new IllegalArgumentException("Null parent");
            this.node = node;
            this.keys = Keychain.add(parent.keys, key);
        }

        private Moore_NFA_State(Moore_NFA_Node<K, V> node) {
            this.node = node;
            this.keys = null;
        }

        @Override
        public V output() {
            return node.value;
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
            if (obj instanceof Moore_NFA.Moore_NFA_State<?,?> stat) {
                return this.node.id == stat.node.id;
            }
            return false;
        }
    }
}
