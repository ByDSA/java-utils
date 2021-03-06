package es.danisales.tasks;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.function.Consumer;

public class ActionAdapter implements Action {
    final Action action;

    @SuppressWarnings("WeakerAccess")
    protected <CALLER extends ActionAdapter> ActionAdapter(ActionBuilder<?, ?, CALLER> actionBuilder) {
        actionBuilder.setCaller((CALLER) this);
        action = actionBuilder.build();
    }

    @Override
    public void addAfterListener(@NonNull Runnable r) {
        action.addAfterListener(r);
    }

    @Override
    public void addOnInterruptListener(@NonNull Runnable a) {
        action.addOnInterruptListener(a);
    }

    @Override
    public boolean isRunning() {
        return action.isRunning();
    }

    @Override
    public boolean isDone() {
        return action.isDone();
    }

    @Override
    public boolean isReady() {
        return action.isReady();
    }

    @Override
    public boolean isSuccessful() {
        return action.isSuccessful();
    }

    @Override
    public boolean isLaunched() {
        return action.isLaunched();
    }

    @Override
    public void interrupt() {
        action.interrupt();
    }

    @Override
    public Mode getMode() {
        return action.getMode();
    }

    @Override
    public void addNext(@NonNull Action a) {
        action.addNext(a);
    }

    @Override
    public void addPrevious(@NonNull Action a) {
        action.addPrevious(a);
    }

    @Override
    public int waitFor() {
        return action.waitFor();
    }

    @Override
    public int waitForNext() {
        return action.waitForNext();
    }

    @Override
    public String getName() {
        return action.getName();
    }

    @Override
    public void setName(String s) {
        action.setName(s);
    }

    @Override
    public boolean hasPrevious(@NonNull Action a) {
        return action.hasPrevious(a);
    }

    @Override
    public boolean hasNext(@NonNull Action a) {
        return action.hasNext(a);
    }

    @Override
    public Object getContext() {
        return action.getContext();
    }

    @Override
    public void run(@NonNull Object context) {
        action.run(context);
    }

    @Override
    @NonNull
    public Consumer<? extends Action> getFunc() {
        return action.getFunc();
    }

    @Override
    public void run() {
        action.run();
    }

    @Override
    public String toString() {
        return action.toString();
    }

    @Override
    public int hashCode() {
        return action.hashCode();
    }
}
