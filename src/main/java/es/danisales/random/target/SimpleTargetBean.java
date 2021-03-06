package es.danisales.random.target;

import es.danisales.utils.Bean;

import java.util.Objects;

@SuppressWarnings("unused")
class SimpleTargetBean<T> extends SimpleTarget implements Bean<T> {
	private T value;

	public SimpleTargetBean(T v) {
		super();

        set(v);
	}

	@Override
    public final T get() {
		return value;
	}

    @Override
    public final void set(T v) {
        value = Objects.requireNonNull(v);
    }

	@SuppressWarnings("unchecked")
	@Override
    public SimpleTargetBean<T> pickDart(long dart) {
        return (SimpleTargetBean<T>) super.pickDart(dart);
	}

	@SuppressWarnings("unchecked")
	@Override
	public SimpleTargetBean<T> pick() {
		return (SimpleTargetBean<T>) super.pick();
	}

    @Override
    public String toString() {
        return value.toString();
    }
}
