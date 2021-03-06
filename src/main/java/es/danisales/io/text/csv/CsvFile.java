package es.danisales.io.text.csv;

import com.google.common.collect.ImmutableList;
import es.danisales.datastructures.ListMap;
import es.danisales.io.text.LinearStringFile;
import es.danisales.log.string.Logging;
import es.danisales.others.Keyable;

import java.nio.file.Path;
import java.util.List;

public abstract class CsvFile<ID, L extends Keyable<ID>> extends LinearStringFile<L> implements Iterable<L> {
    private String separator = ";";

    private ListMap<ID, L> _listMap = new ListMap<>();

    @SuppressWarnings("WeakerAccess")
    public CsvFile(Path path) {
        super(path);
	}

	public L get(ID id) {
        return _listMap.getByKey(id);
	}

	@Override
    protected L stringToLine(long i, String lStr) {
		if (lStr.startsWith( "//" ))
            return null;
		String[] o = lStr.split( separator );
        try {
            L l = readLine(o);
            if (l != null) {
                _listMap.put(l.getKey(), l);
            }

            return l;
        } catch (Exception e) {
            Logging.error("Error leyendo la línea " + i + " del archivo " + this + "\r\n" + lStr);
            throw e;
        }
	}

    @Override
    protected String lineToString(long i, L l) {
        StringBuilder sb = new StringBuilder();
        List<String> params = saveLine(l);
        boolean first = true;
        for (String str : params) {
            if (first) {
                first = false;
            } else
                sb.append(separator);
            sb.append(str);
        }
        return sb.toString();
    }

    protected abstract L readLine(String[] e);

    protected abstract List<String> saveLine(L l);

    @SuppressWarnings("unused")
    public void setSeparator(String s) {
        separator = s;
	}

    @SuppressWarnings("unused")
    public ImmutableList<L> lines() {
        return ImmutableList.copyOf(_listMap.values());
    }

    @SuppressWarnings("unused")
    public ListMap<ID, L> listMap() {
        return _listMap;
    }
}
