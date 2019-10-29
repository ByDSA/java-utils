package es.danisales.datastructures;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.EnumSet;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

@SuppressWarnings("unused")
public class SetUtils {
	private SetUtils() {
	} // noninstantiable

    public static <E extends Enum<E>> EnumSet<E> concat(@NonNull List<EnumSet<E>> sets) {
        checkArgument(sets.size() > 0);

        EnumSet<E> ret = EnumSet.copyOf(sets.get(0));
        for (int i = 1; i < sets.size(); i++)
            ret.addAll(sets.get(i));
		
		return ret;
	}
}
