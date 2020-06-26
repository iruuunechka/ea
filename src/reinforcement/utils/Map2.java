package reinforcement.utils;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * A two-dimensional map with a default value to be returned if there is no corresponding mapping.
 *
 * @author Maxim Buzdalov
 */
public final class Map2<K1, K2, V extends Comparable<? super V>> {
    private final Map<Tuple2<K1, K2>, LinkedEntry<K1, K2, V>> contents = new HashMap<>();
    private final V defaultValue;
    protected final Map<K1, LinkedEntry<K1, K2, V>> firstKeyLinks = new HashMap<>();

    public Map2(V defaultValue) {
        this.defaultValue = defaultValue;
    }

    public V defaultValue() {
        return defaultValue;
    }

    public Set<K1> keySet1() {
        return Collections.unmodifiableSet(firstKeyLinks.keySet());
    }

    public Set<K2> keySet2() {
        //Not that fast.
        return contents.keySet().stream().map(key -> key.second).collect(Collectors.toSet());
    }

    public V get(K1 k1, K2 k2) {
        LinkedEntry<K1, K2, V> rv = contents.get(Tuple2.of(k1, k2));
        return rv == null ? defaultValue : rv.v;
    }

    public void put(K1 k1, K2 k2, V value) {
        Tuple2<K1, K2> tuple = Tuple2.of(k1, k2);
        LinkedEntry<K1, K2, V> rv = contents.get(tuple);
        if (rv == null) {
            contents.put(tuple, rv = new LinkedEntry<>(k1, k2, value));
            LinkedEntry<K1, K2, V> entry = firstKeyLinks.get(k1);
            if (entry == null) {
                firstKeyLinks.put(k1, entry = new LinkedEntry<>(k1, null, null));
            }
            rv.insertMeAfter(entry);
        } else {
            rv.v = value;
        }
    }

    public void remove(K1 k1, K2 k2) {
        LinkedEntry<K1, K2, V> e = contents.remove(Tuple2.of(k1, k2));
        if (e != null) {
            e.removeMe();
        }
    }

    public void clear() {
        contents.clear();
        firstKeyLinks.clear();
    }

    public V max(K1 k1, List<K2> possibleK2s) {
        V result = null;
        int count = 0;
        LinkedEntry<K1, K2, V> e = firstKeyLinks.get(k1);

        while (e != null && !e.next.isRoot()) {
            ++count;
            e = e.next;
            V v = e.v;
            if (result == null) {
                result = v;
            } else {
                result = result.compareTo(v) > 0 ? result : v;
            }
        }
        V defV = defaultValue;
        if (result == null) {
            return defV;
        } else {
            if (count < possibleK2s.size() && defV.compareTo(result) > 0) {
                return defV;
            } else {
                return result;
            }
        }
    }

    public K2 argMax(K1 k1, List<K2> possibleK2s) {
        if (possibleK2s.isEmpty()) {
            throw new IllegalArgumentException("possibleK2s are empty");
        }
        V result = max(k1, possibleK2s);
        List<K2> candidates = new ArrayList<>();
        for (K2 k2 : possibleK2s) {
            if (get(k1, k2).compareTo(result) == 0) {
                candidates.add(k2);
            }
        }
        Random r = ThreadLocalRandom.current();
        if (candidates.isEmpty()) {
            throw new AssertionError("Should not happen ever");
        }
        return candidates.get(r.nextInt(candidates.size()));
    }

    private static class Tuple2<A, B> {
        public final A first;
        public final B second;

        private Tuple2(A first, B second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Tuple2<?, ?> tuple2 = (Tuple2<?, ?>) o;
            return first.equals(tuple2.first) && second.equals(tuple2.second);
        }

        @Override
        public int hashCode() {
            int result = first.hashCode();
            result = 31 * result + second.hashCode();
            return result;
        }

        public static <A, B> Tuple2<A, B> of(A a, B b) {
            return new Tuple2<>(a, b);
        }
    }

    protected static final class LinkedEntry<K1, K2, V> {
        public final K1 k1;
        public final K2 k2;
        public V v;

        protected LinkedEntry<K1, K2, V> next = this;
        protected LinkedEntry<K1, K2, V> prev = this;

        private LinkedEntry(K1 k1, K2 k2, V v) {
            this.k1 = k1;
            this.k2 = k2;
            this.v = v;
        }

        public void removeMe() {
            next.prev = prev;
            prev.next = next;
            prev = next = this;
        }

        public void insertMeAfter(LinkedEntry<K1, K2, V> that) {
            next = that.next;
            prev = that;
            next.prev = this;
            prev.next = this;
        }

        public boolean isRoot() {
            return k2 == null;
        }
    }
}