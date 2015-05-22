package co.unruly.util.function;

@FunctionalInterface
public interface ExceptionalSupplier<T, E extends Exception> {
    T get() throws E;
}
