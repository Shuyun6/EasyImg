package com.shuyun.easyimg.common;

import java.util.Objects;

public interface Predicate<T> {
    boolean test(T var1);

    default Predicate<T> and(Predicate<? super T> var1) {
        Objects.requireNonNull(var1);
        return (var2) -> this.test(var2) && var1.test(var2);
    }

    default Predicate<T> negate() {
        return (var1) -> !this.test(var1);
    }

    default Predicate<T> or(Predicate<? super T> var1) {
        Objects.requireNonNull(var1);
        return (var2) -> this.test(var2) || var1.test(var2);
    }

    static <T> Predicate<T> isEqual(Object var0) {
        return null == var0 ? (var1) -> true : var0::equals;
    }
}
