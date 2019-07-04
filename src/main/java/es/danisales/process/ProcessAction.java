package es.danisales.process;

import es.danisales.log.string.Logging;
import es.danisales.strings.StringUtils;
import es.danisales.tasks.Action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class ProcessAction extends Action {
    @SuppressWarnings("WeakerAccess")
    protected String[] paramsWithName;

    private AtomicInteger resultCode = new AtomicInteger();

    private final List<Runnable> notFoundListeners = new ArrayList<>();
    private final List<Runnable> beforeListeners = new ArrayList<>();
    private final List<Consumer<String>> errorLineListeners = new ArrayList<>();
    private final List<Consumer<String>> outLineListeners = new ArrayList<>();
    private final List<Consumer<Integer>> errorListeners = new ArrayList<>();
    private final List<Runnable> interruptedListeners = new ArrayList<>();
    private final List<Runnable> onNoArgumentsListeners = new ArrayList<>();

    @SuppressWarnings("unused")
    public ProcessAction(String fname, List<String> params) {
        super(Mode.CONCURRENT);

        setFilenameAndParams(fname, params);
    }

    @SuppressWarnings("unused")
    public ProcessAction(String fname, String... params) {
        super(Mode.CONCURRENT);

        setFilenameAndParams(fname, params);
    }

    public ProcessAction() {
        super(Mode.CONCURRENT);
    }

    @SuppressWarnings("unused")
    public boolean addNotFoundListener(Runnable runnable) {
        synchronized (notFoundListeners) {
            return notFoundListeners.add(runnable);
        }
    }

    @SuppressWarnings("unused")
    public boolean addBeforeListener(Runnable runnable) {
        synchronized (beforeListeners) {
            return beforeListeners.add(runnable);
        }
    }

    @SuppressWarnings("unused")
    public boolean addErrorLineListener(Consumer<String> consumer) {
        synchronized (errorLineListeners) {
            return errorLineListeners.add(consumer);
        }
    }

    @SuppressWarnings("unused")
    public boolean addOutLineListener(Consumer<String> consumer) {
        synchronized (outLineListeners) {
            return outLineListeners.add(consumer);
        }
    }

    @SuppressWarnings("unused")
    public boolean addErrorListener(Consumer<Integer> consumer) {
        synchronized (errorListeners) {
            return errorListeners.add(consumer);
        }
    }

    @SuppressWarnings("unused")
    public boolean addInterruptedListener(Runnable runnable) {
        synchronized (interruptedListeners) {
            return interruptedListeners.add(runnable);
        }
    }

    @SuppressWarnings("unused")
    public boolean addOnNoArgumentsListener(Runnable runnable) {
        synchronized (onNoArgumentsListeners) {
            return onNoArgumentsListeners.add(runnable);
        }
    }

    @SuppressWarnings("unused")
    public boolean removeNotFoundListener(Runnable runnable) {
        synchronized (notFoundListeners) {
            return notFoundListeners.remove(runnable);
        }
    }

    @SuppressWarnings("unused")
    public boolean removeBeforeListener(Runnable runnable) {
        synchronized (beforeListeners) {
            return beforeListeners.remove(runnable);
        }
    }

    @SuppressWarnings("unused")
    public boolean removeErrorLineListener(Consumer<String> consumer) {
        synchronized (errorLineListeners) {
            return errorLineListeners.remove(consumer);
        }
    }

    @SuppressWarnings("unused")
    public boolean removeOutLineListener(Consumer<String> consumer) {
        synchronized (outLineListeners) {
            return outLineListeners.remove(consumer);
        }
    }

    @SuppressWarnings("unused")
    public boolean removeErrorListener(Consumer<Integer> consumer) {
        synchronized (errorListeners) {
            return errorListeners.remove(consumer);
        }
    }

    @SuppressWarnings("unused")
    public boolean removeInterruptedListener(Runnable runnable) {
        synchronized (interruptedListeners) {
            return interruptedListeners.remove(runnable);
        }
    }

    @SuppressWarnings("unused")
    public boolean removeOnNoArgumentsListener(Runnable runnable) {
        synchronized (onNoArgumentsListeners) {
            return onNoArgumentsListeners.remove(runnable);
        }
    }

    @SuppressWarnings("unused")
    public void clearNotFoundListeners() {
        synchronized (notFoundListeners) {
            notFoundListeners.clear();
        }
    }

    @SuppressWarnings("unused")
    public void clearBeforeListeners() {
        synchronized (beforeListeners) {
            beforeListeners.clear();
        }
    }

    @SuppressWarnings("unused")
    public void clearErrorLineListeners() {
        synchronized (errorLineListeners) {
            errorLineListeners.clear();
        }
    }

    @SuppressWarnings("unused")
    public void clearOutLineListener() {
        synchronized (outLineListeners) {
            outLineListeners.clear();
        }
    }

    @SuppressWarnings("unused")
    public void clearErrorListeners() {
        synchronized (errorListeners) {
            errorListeners.clear();
        }
    }

    @SuppressWarnings("unused")
    public void clearInterruptedListeners() {
        synchronized (interruptedListeners) {
            interruptedListeners.clear();
        }
    }

    @SuppressWarnings("unused")
    public void clearOnNoArgumentsListeners() {
        synchronized (onNoArgumentsListeners) {
            onNoArgumentsListeners.clear();
        }
    }

    @SuppressWarnings("WeakerAccess")
    protected void setFilenameAndParams(String fname, List<String> params) {
        setFilenameAndParams(fname, params.toArray(new String[0]));
    }

    protected void setFilenameAndParams(String fname, String... params) {
        paramsWithName = new String[ params.length +1 ];
        paramsWithName[0] = fname;
        System.arraycopy(params, 0, paramsWithName, 1, params.length);
    }

    @Override
    protected void innerRun() {
        synchronized (beforeListeners) {
            for (Runnable r : beforeListeners)
                r.run();
        }
        try {
            if (paramsWithName == null || paramsWithName.length == 0)
                throw new NoArgumentsException();
            else
                for (String str : paramsWithName)
                    if (str == null)
                        throw new NoArgumentsException();

            Logging.log("Executing " + paramsWithName[0] + " " + StringUtils.join(" ", paramsWithName, 1));
            final Process p = Runtime.getRuntime().exec(paramsWithName);

            Thread normalOutputThread = startNormalOutputListener(p);
            startErrorOutputListener(p);

            resultCode.set(p.waitFor());
            normalOutputThread.join();
            if (resultCode.get() != 0) {
                synchronized (errorListeners) {
                    for (Consumer<Integer> c : errorListeners)
                        c.accept(resultCode.get());
                }
            }
        } catch (IOException e) {
            synchronized (notFoundListeners) {
                for (Runnable r : notFoundListeners)
                    r.run();
            }
        } catch (InterruptedException e) {
            synchronized (interruptedListeners) {
                for (Runnable r : interruptedListeners)
                    r.run();
            }
        } catch(NoArgumentsException e) {
            synchronized (onNoArgumentsListeners) {
                for (Runnable r : onNoArgumentsListeners)
                    r.run();
            }
        }
    }

    private Thread startNormalOutputListener(Process p) {
        Thread normalOutputThread = new Thread(() -> {
            try {
                String line;
                BufferedReader input =
                        new BufferedReader
                                (new InputStreamReader(p.getInputStream()));

                while ((line = input.readLine()) != null)
                    synchronized (outLineListeners) {
                        for (Consumer<String> c : outLineListeners)
                            c.accept(line);
                    }


                input.close();
            } catch(Exception ignored) {}
        });

        normalOutputThread.start();

        return normalOutputThread;
    }

    private void startErrorOutputListener(Process p) {
        Thread errorOutputThread = new Thread(() -> {
            try {
                String line;
                BufferedReader input =
                        new BufferedReader
                                (new InputStreamReader(p.getErrorStream()));

                while ((line = input.readLine()) != null)
                    synchronized (errorLineListeners) {
                        for (Consumer<String> c : errorLineListeners)
                            c.accept(line);
                    }


                input.close();
            } catch(Exception ignored) {}
        });

        errorOutputThread.start();
    }

    @SuppressWarnings("WeakerAccess")
    public Integer getResultCode() {
        return resultCode.get();
    }

    @SuppressWarnings("unused")
    public String getFileName() {
        return paramsWithName == null || paramsWithName.length == 0 ? null : paramsWithName[0];
    }

    @SuppressWarnings({"unused","WeakerAccess"})
    public int joinResult() {
        try {
            join();
        } catch (InterruptedException ignored) {}

        return getResultCode();
    }

    @SuppressWarnings("unused")
    public int runAndJoinResult() {
        run();
        return joinResult();
    }


    @SuppressWarnings("WeakerAccess")
    public static class NoArgumentsException extends RuntimeException {
        public NoArgumentsException() {
            super("No arguments");
        }
    }
}
