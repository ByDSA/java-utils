package es.danisales.tasks;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

public class ActionList implements Action, List<Action> {
	final ActionInternalAdapter<ActionList> actionAdapter;
	private final List<Action> listAdapter = new ArrayList<>();

	private final ConcurrentHashMap<Action, Integer> times;
	private final List<Consumer<Action>> beforeEachList = new ArrayList<>();
	private final List<Consumer<Action>> onRemoveListeners = new ArrayList<>();
	private final List<Consumer<Action>> onAddListeners = new ArrayList<>();
	private final AtomicBoolean doneAll = new AtomicBoolean(false);

    @SuppressWarnings("WeakerAccess")
    protected ActionList(Mode m) {
        times = new ConcurrentHashMap<>();
		actionAdapter = new ActionInternalAdapter.Builder<ActionList>()
                .setMode(m)
                .setRun(this::innerRun)
                .setCaller(this)
                .addSuccessRule(doneAll::get)
                .build();
    }

	public static ActionList of(@NonNull Mode mode, Action... actions) {
		return of(mode, Arrays.asList(actions));
	}

	public static ActionList of(@NonNull Mode mode, @NonNull Collection<Action> actions) {
		ActionList ret = new ActionList(mode);
		ret.addAll(actions);

		return ret;
	}


    @SuppressWarnings("WeakerAccess")
    protected void innerRun(@NonNull ActionList self) {
		doneAll.set(false);
		self.secureForEach((Action action) -> {
			synchronized (self.beforeEachList) {
				for (Consumer<Action> c : self.beforeEachList)
					c.accept(action);
			}
			self.checkAndDoCommon(action);
		});

		self.waitForChildren();
		doneAll.set(true);
	}

	@SuppressWarnings("unused")
	public void addBeforeEach(@NonNull Consumer<Action> r) {
		synchronized (beforeEachList) {
			beforeEachList.add(r);
		}
	}

	@SuppressWarnings("unused")
	public void addOnRemoveListener(@NonNull Consumer<Action> r) {
		synchronized (onRemoveListeners) {
			onRemoveListeners.add(r);
		}
	}

	@SuppressWarnings("unused")
	public void addOnAddListener(@NonNull Consumer<Action> r) {
		synchronized (onAddListeners) {
			onAddListeners.add(r);
		}
	}

	private void secureForEach(@NonNull Consumer<? super Action> f) {
		List<Action> calledActions;
		calledActions = new ArrayList<>();

		Action action;
		synchronized(this) {
			if (size() > 0)
				action = get(0);
			else
				return;
		}
		while (action != null) {
			if (!calledActions.contains(action)) {
				f.accept(action);
				calledActions.add(action);
			}

			synchronized(this) {
				int index = indexOf(action) + 1;
				if (index >= size()) {
					if (!calledActions.containsAll(this) && size() > 0)
						action = get(0); // Restart
					else
						action = null;
				} else
					action = get(index);
			}
		}
	}


	@SuppressWarnings("WeakerAccess")
	public void waitForChildren() {
        secureForEach(Action::waitFor);
	}

	@Override
	public int waitFor() {
		return actionAdapter.waitFor();
	}

	@Override
	public int waitForNext() {
		actionAdapter.waitForNext();

		secureForEach(Action::waitForNext);

		return ActionValues.OK.intValue();
	}

	@Override
	public String getName() {
		return actionAdapter.getName();
	}

	@Override
	public void setName(String s) {
		actionAdapter.setName(s);
	}

	@Override
	public boolean hasPrevious(@NonNull Action a) {
		return actionAdapter.hasPrevious(a);
	}

	@Override
	public boolean hasNext(@NonNull Action a) {
		return actionAdapter.hasNext(a);
	}

	@Override
	public Object getContext() {
		return actionAdapter.getContext();
	}

	@Override
	public void run(@NonNull Object context) {
		actionAdapter.run(context);
	}

	@Override
	@NonNull
	public Consumer<ActionList> getFunc() {
		return actionAdapter.getFunc();
	}

	private void checkAndDoCommon(@NonNull Action action) {
		if (actionAdapter.status != ActionStatus.EXECUTING)
			return;

		boolean condition = action.isReady() && !action.isRunning();
		if ( condition ) {
			checkNotNull(times);
			Integer n = times.get( action );
			if (n == null)
				n = 0;
			action.run(this);
			times.put( action, n+1 );
		}
	}

	@Override
    public void addAfterListener(@NonNull Runnable r) {
        actionAdapter.addAfterListener(r);
	}

	@Override
    public void addOnInterruptListener(@NonNull Runnable a) {
        actionAdapter.addOnInterruptListener(a);
	}

	@Override
	public boolean isRunning() {
		return actionAdapter.isRunning();
	}

	@Override
	public boolean isDone() {
		return actionAdapter.isDone();
	}

	@Override
	public boolean isReady() {
		return actionAdapter.isReady();
	}

	@Override
	public boolean isSuccessful() {
		return actionAdapter.isSuccessful();
	}

    @Override
    public boolean isLaunched() {
        return actionAdapter.isLaunched();
    }

	@Override
	public void interrupt() {
		actionAdapter.interrupt();
		for (final Action action : this) {
			new Thread(() -> {
				if (action.isRunning())
					action.interrupt();
				else
					remove(action);
			}).start();
		}

		clear();
	}

	@Override
	public Mode getMode() {
		return actionAdapter.getMode();
	}

	@Override
	public void addNext(@NonNull Action a) {
		actionAdapter.addNext(a);
	}

	@Override
	public void addPrevious(@NonNull Action a) {
		actionAdapter.addPrevious(a);
	}

	@Override
	public int size() {
		synchronized (listAdapter) {
			return listAdapter.size();
		}
	}

	@Override
	public boolean isEmpty() {
		synchronized (listAdapter) {
			return listAdapter.isEmpty();
		}
	}

	@Override
	public boolean contains(Object o) {
		synchronized (listAdapter) {
			return listAdapter.contains(o);
		}
	}

	@Override
    @NonNull
	public Iterator<Action> iterator() {
		synchronized (listAdapter) {
			return listAdapter.iterator();
		}
	}

	@Override
    public void forEach(@NonNull Consumer<? super Action> action) {
		synchronized (listAdapter) {
			listAdapter.forEach(action);
		}
	}

	@Override
    @NonNull
	public Object[] toArray() {
		synchronized (listAdapter) {
			return listAdapter.toArray();
		}
	}

	@SuppressWarnings("SuspiciousToArrayCall")
	@Override
    public @NonNull <T> T[] toArray(@NonNull T[] a) {
		synchronized (listAdapter) {
			return listAdapter.toArray(a);
		}
	}

	@Override
	public boolean add(Action action) {
		synchronized (listAdapter) {
			int index = indexOf(action);
			if (index >= 0 && get(index) == action)
				throw new AddedException(action);

			synchronized (onAddListeners) {
				for (Consumer<Action> c : onAddListeners)
					c.accept(action);
			}

			return listAdapter.add(action);
		}
	}

	@Override
	public boolean remove(Object o) {
		if (!(o instanceof Action))
			return false;

		synchronized (listAdapter) {
			synchronized (onRemoveListeners) {
				for (Consumer<Action> c : onRemoveListeners)
					c.accept((Action) o);
			}

			return listAdapter.remove(o);
		}
	}

	@Override
    public boolean containsAll(@NonNull Collection<?> c) {
		synchronized (listAdapter) {
			return listAdapter.containsAll(c);
		}
	}

	@Override
    public boolean addAll(@NonNull Collection<? extends Action> c) {
		synchronized (listAdapter) {
			return listAdapter.addAll(c);
		}
	}

	@Override
    public boolean addAll(int index, @NonNull Collection<? extends Action> c) {
		synchronized (listAdapter) {
			return listAdapter.addAll(c);
		}
	}

	@Override
    public boolean removeAll(@NonNull Collection<?> c) {
		synchronized (listAdapter) {
			return listAdapter.removeAll(c);
		}
	}

	@Override
	public boolean removeIf(Predicate<? super Action> filter) {
		synchronized (listAdapter) {
			return listAdapter.removeIf(filter);
		}
	}

	@Override
    public boolean retainAll(@NonNull Collection<?> c) {
		synchronized (listAdapter) {
			return listAdapter.retainAll(c);
		}
	}

	@Override
	public void replaceAll(UnaryOperator<Action> operator) {
		synchronized (listAdapter) {
			listAdapter.replaceAll(operator);
		}
	}

	@Override
	public void sort(Comparator<? super Action> c) {
		synchronized (listAdapter) {
			listAdapter.sort(c);
		}
	}

	@Override
	public void clear() {
		synchronized (listAdapter) {
			listAdapter.clear();
		}
	}

	@Override
	public Action get(int index) {
		synchronized (listAdapter) {
			return listAdapter.get(index);
		}
	}

	@Override
	public Action set(int index, Action element) {
		synchronized (listAdapter) {
			return listAdapter.set(index, element);
		}
	}

	@Override
	public void add(int index, Action element) {
		synchronized (listAdapter) {
			listAdapter.add(index, element);
		}
	}

	@Override
	public Action remove(int index) {
		synchronized (listAdapter) {
			return listAdapter.remove(index);
		}
	}

	@Override
	public int indexOf(Object o) {
		synchronized (listAdapter) {
			return listAdapter.indexOf(o);
		}
	}

	@Override
	public int lastIndexOf(Object o) {
		synchronized (listAdapter) {
			return listAdapter.lastIndexOf(o);
		}
	}

	@Override
    @NonNull
	public ListIterator<Action> listIterator() {
		synchronized (listAdapter) {
			return listAdapter.listIterator();
		}
	}

	@Override
    @NonNull
	public ListIterator<Action> listIterator(int index) {
		synchronized (listAdapter) {
			return listAdapter.listIterator(index);
		}
	}

	@Override
    @NonNull
	public List<Action> subList(int fromIndex, int toIndex) {
		synchronized (listAdapter) {
			return listAdapter.subList(fromIndex, toIndex);
		}
	}

	@Override
	public Spliterator<Action> spliterator() {
		synchronized (listAdapter) {
			return listAdapter.spliterator();
		}
	}

	@Override
	public Stream<Action> stream() {
		synchronized (listAdapter) {
			return listAdapter.stream();
		}
	}

	@Override
	public Stream<Action> parallelStream() {
		synchronized (listAdapter) {
			return listAdapter.parallelStream();
		}
	}

	@Override
	public void run() {
		actionAdapter.run();
	}

	@Override
	public String toString() {
		return actionAdapter.toString();
	}

	@SuppressWarnings("WeakerAccess")
	class AddedException extends RuntimeException {
		public AddedException(Action a) {
			super("Action " + a + " already added in this listAdapter");
		}
	}
}
