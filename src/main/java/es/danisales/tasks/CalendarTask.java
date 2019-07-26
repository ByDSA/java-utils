package es.danisales.tasks;

import es.danisales.rules.Rule;
import es.danisales.time.Calendar;
import es.danisales.time.CalendarInterface;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Consumer;

public class CalendarTask implements Action, CalendarInterface {
	private final Calendar calendar = new Calendar();
	private final Action actionAdapter;

	private CalendarTask(Mode m, Consumer<CalendarTask> f) {
		actionAdapter = Action.of(m, f);
	}

	public static CalendarTask of(Mode m, Consumer<CalendarTask> f) {
		return new CalendarTask(m, f);
	}

	@Override
	public boolean add(Rule e) {
		return calendar.add( e );
	}

	public boolean add(Calendar c) {
		calendar.addAll( c );
		for(Rule e : c.getExceptions())
			calendar.addException( e );
		
		return true;
	}
	
	public List<Rule> getExceptions() {
		return calendar.getExceptions();
	}

	@Override
	public void add(int index, Rule element) {
		calendar.add( index, element );
	}

	@Override
	public boolean addAll(Collection<? extends Rule> c) {
		return calendar.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends Rule> c) {
		return calendar.addAll(index, c);
	}

	@Override
	public void clear() {
		calendar.clear();
	}

	@Override
	public boolean contains(Object o) {
		return calendar.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return calendar.containsAll(c);
	}

	@Override
	public Rule get(int index) {
		return calendar.get(index);
	}

	@Override
	public int indexOf(Object o) {
		return calendar.indexOf(o);
	}

	@Override
	public boolean isEmpty() {
		return calendar.isEmpty();
	}

	@Override
	public Iterator<Rule> iterator() {
		return calendar.iterator();
	}

	@Override
	public int lastIndexOf(Object o) {
		return calendar.lastIndexOf(o);
	}

	@Override
	public ListIterator<Rule> listIterator() {
		return calendar.listIterator();
	}

	@Override
	public ListIterator<Rule> listIterator(int index) {
		return calendar.listIterator(index);
	}

	@Override
	public boolean remove(Object o) {
		return calendar.remove(o);
	}

	@Override
	public Rule remove(int index) {
		return calendar.remove(index);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return calendar.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return calendar.retainAll(c);
	}

	@Override
	public Rule set(int index, Rule element) {
		return calendar.set(index, element);
	}

	@Override
	public int size() {
		return calendar.size();
	}

	@Override
	public List<Rule> subList(int fromIndex, int toIndex) {
		return calendar.subList(fromIndex, toIndex);
	}

	@Override
	public Object[] toArray() {
		return calendar.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return calendar.toArray( a );
	}

	@Override
	public void addException(Rule r) {
		calendar.addException( r );
	}

	@Override
	public void addAfter(Runnable r) {
		actionAdapter.addAfter(r);
	}

	@Override
	public void addOnInterrupt(Runnable a) {
		actionAdapter.addOnInterrupt(a);
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
	public void interrupt() {
		actionAdapter.interrupt();
	}

	@Override
	public Mode getMode() {
		return actionAdapter.getMode();
	}

	@Override
	public void addNext(Action a) {
		actionAdapter.addNext(a);
	}

	@Override
	public void addPrevious(Action a) {
		actionAdapter.addPrevious(a);
	}

	@Override
	public boolean check() {
		return calendar.check();
	}

	@Override
	public int waitFor() {
		return actionAdapter.waitFor();
	}

	@Override
	public int waitForNext() {
		return actionAdapter.waitForNext();
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
	public boolean hasPrevious(Action a) {
		return actionAdapter.hasPrevious(a);
	}

	@Override
	public boolean hasNext(Action a) {
		return actionAdapter.hasNext(a);
	}

	@Override
	public Object getContext() {
		return actionAdapter.getContext();
	}

	@Override
	public void run(Object context) {
		actionAdapter.run(context);
	}

	@Override
	public Consumer<? extends Action> getFunc() {
		return actionAdapter.getFunc();
	}

	@Override
	public void run() {
		actionAdapter.run();
	}
}
