package com.truevault.rollbar.utilities;

import com.fasterxml.jackson.annotation.JsonValue;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.annotation.Nullable;
import javax.annotation.concurrent.NotThreadSafe;

/**
 * Base class for classes that can be extended with arbitrary metadata (as per the
 * <a href='https://rollbar.com/docs/api/items_post/'>Rollbar spec</a>).
 * This class, unlike the rest of the classes is mutable. Extra caution is therefore warranted.
 * @param <T> The extensible type itself.
 */
@NotThreadSafe
public abstract class Extensible<T extends Extensible<T>> {
    private Set<String> knownMembers;

    private final TreeMap<String, Object> members;

    protected Extensible(@Nullable Map<String, Object> members) {
        if (members == null) {
            this.members = new TreeMap<>();
        } else {
            this.members = new TreeMap<>(members);
        }
    }

    /**
     * Get the member, or null if not present.
     * @param name the member name
     * @return null or the member at that key
     */
    public Object get(String name) {
        return members.get(name);
    }

    /**
     * Copy this item. Needs to be custom per subclass. Should return the subclass itself (not an Extensible).
     * @return An exact copy of this object.
     */
    public abstract T copy();

    /**
     * Returns the 'known' keys, that are specially treated by Rollbar
     * @return the set of known keys
     */
    protected abstract Set<String> getKnownMembers();

    /**
     * Sets the member. Cannot be used to set known members.
     * @param name the member name to set.
     * @param value the value to set.
     * @return this object.
     */
    public T put(String name, Object value) throws IllegalArgumentException {
        if (isKnownMember(name)) {
            final String msgFmt = "'%s' is a known member and must be set with the corresponding method";
            throw new IllegalArgumentException(String.format(msgFmt, name));
        }
        Extensible<T> returnVal = this.copy();
        returnVal.members.put(name, value);
        @SuppressWarnings("unchecked")
        T returned = (T) returnVal;
        return returned;
    }

    private Set<String> knownMembers() {
        if (knownMembers == null) {
            knownMembers = getKnownMembers();
        }
        return knownMembers;
    }

    private boolean isKnownMember(String name) {
        return knownMembers().contains(name);
    }

    /**
     * MUTATING. Use only in Constructor.
     * @param name the key
     * @param value the value
     */
    protected void putKnown(String name, Object value) {
        if (!isKnownMember(name)) {
            final String msg = "Can only set known values with this method. %s not known";
            throw new IllegalArgumentException(String.format(msg, name));
        }
        this.members.put(name, value);
    }

    /**
     * Get the keys in this extensible
     * @param withoutKnownMembers true if you want to leave out known members
     * @return the keys
     */
    public Set<String> keys(boolean withoutKnownMembers) {
        Set<String> keys = new TreeSet<String>(members.keySet());
        if (withoutKnownMembers) {
            keys.removeAll(knownMembers());
        }
        return keys;
    }

    /**
     * Get a copy of the members.
     * @return a copy of the members in this Extensible.
     */
    public Map<String, Object> getMembers() {
        return new TreeMap<>(members);
    }

    @JsonValue
    public Map<String, Object> asJson() {
        LinkedHashMap<String, Object> json = new LinkedHashMap<String, Object>();
        for(String key : knownMembers()) {
            if (this.members.containsKey(key) && this.members.get(key) != null) {
                json.put(key, this.members.get(key));
            }
        }
        for(Map.Entry<String, Object> entry : this.members.entrySet()) {
            if (!json.containsKey(entry.getKey())) {
                json.put(entry.getKey(), entry.getValue());
            }
        }
        return json;
    }
}
