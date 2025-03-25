package keychain;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

/**
 * Efficient storage class for an alternative form of stack that is great at
 * storing branching data where many suffixes use the same prefix data
 * @param <K> the data type, for effective use, K should be immutable
 */
public class Keychain<K> implements Iterable<K> {
    /*
     * NOTE: to use this class, initialize keychain to null
     * Each time you want to add to the back of the chain, use the static
     * add method with the keychain as an argument. The original keychain
     * instance will not be modified but the returned keychain will be the
     * original with the new value added on
     */

    /**
     * Returns a new keychain that is the same as the previous keychain, only with
     * value appended onto it
     * @param chain the chain to add on to (creates new chain if null)
     * @param value the value to append
     * @return a new keychain with the value appended on
     * @param <K> the data type of the keychain
     */
    public static <K> Keychain<K> add(Keychain<K> chain, K value) {
        return new Keychain<>(chain, value);
    }

    // nullable, previous link in keychain
    Keychain<K> past;
    K value;

    private Keychain(Keychain<K> past, K value) {
        this.past = past;
        this.value = value;
    }

    /**
     * Collects the elements of the keychain into a queue
     * @return a queue of the elements ordered such that first in keychain is first in queue
     */
    public Queue<K> collect() {
        LinkedList<K> coll = new LinkedList<>();
        Keychain<K> curr = this;
        while (curr != null) {
            coll.push(curr.value);
            curr = curr.past;
        }
        return coll;
    }

    @Override
    public Iterator<K> iterator() {
        return collect().iterator();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj instanceof Keychain<?> key) {
            return Objects.equals(this.value, key.value) && Objects.equals(this.past, key.past);
        }
        return false;
    }
}
