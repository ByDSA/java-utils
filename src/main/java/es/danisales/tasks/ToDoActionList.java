package es.danisales.tasks;

import es.danisales.log.string.Logging;

public class ToDoActionList extends ActionList {
    public ToDoActionList() {
        super(Mode.CONCURRENT);

        addNext(this);

        run();
    }

    @Override
    public boolean add(Action a) {
        boolean ret = super.add(a);

        a.addAfter(() -> {
            Logging.log("End remove ToDoActionList " + a);
            remove(a);
        });
        a.addInterruptionListener(()-> {
            remove(a);
            interrupt();
        });

        forceCheck();

        return ret;
    }

    @Override
    public boolean check() {
        return !isRunning() && !isEnding() && size() > 0;
    }
}
