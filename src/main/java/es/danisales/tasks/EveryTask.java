package es.danisales.tasks;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Date;

import static com.google.common.base.Preconditions.checkArgument;

public class EveryTask extends ActionAdapter {
	private Date lastApply = new Date(0);
	private long every;

	private EveryTask(Builder builder) {
        super(builder);
		every = builder.every;
	}

	public static EveryTask newInstance(long every) {
		return new Builder().setEvery(every).build();
	}

	public static Builder builder() {
		return new Builder();
	}

	@Override
	public boolean isReady() {
		return lastApply.getTime() == 0 || lastApply.getTime() + every - new Date().getTime() < 0;
	}

    public static class Builder extends ActionBuilder<Builder, EveryTask, EveryTask> {
		long every = 0;

		private Builder() {
		}

		@Override
        public @NonNull EveryTask buildOnce() {
			checkArgument(every > 0);
            checkArgument(instance == null, "Just one instantiation");
            instance = new EveryTask(this);
            return instance;
		}

		@SuppressWarnings("WeakerAccess")
        public @NonNull Builder setEvery(long every) {
			checkArgument(every > 0);
			this.every = every;

			return self();
		}

		@Override
        protected @NonNull Builder self() {
			return this;
		}
	}
}
