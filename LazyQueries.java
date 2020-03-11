package pt.isel.mpd.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class LazyQueries {
     /**
     * Versao 4
     */
    public static <T> Iterable<T> filter(Iterable<T> src, Predicate<T> pred) {
        return () -> new Iterator<T>() {
			
            Iterator<T> iter = src.iterator();
            T temp = null;
			
            @Override
            public boolean hasNext() {
                if (temp != null) return true;
				if (!iter.hasNext()) return false;
				
                temp = iter.next();

                while(iter.hasNext() && !pred.test(temp)){
                    temp = iter.next();
                }
                return temp != null;
            }

            @Override
            public T next() {
                if(hasNext()) {
					T tmp = temp;
					temp = null;
					return tmp;
				}
                else
                    throw new IndexOutOfBoundsException("Iterator has been depleted");
                
            }
        };

    }
    public static <T> Iterable<T> skip(Iterable<T> src, int nr) {
        return () -> {
            Iterator<T> iter = src.iterator();
            int idx = nr;
            while(idx-- > 0 && iter.hasNext()) iter.next();
            return iter;
        };
    }
    public static <T, R> Iterable<R> map(Iterable<T> src, Function<T, R> mapper) {
        return () -> new Iterator<R>() {
            Iterator<T> iter = src.iterator();
            @Override
            public boolean hasNext() {
                return iter.hasNext();
            }
            @Override
            public R next() {
                return mapper.apply(iter.next());
            }
        };
    }

    /**
     * Terminal operation
     */
    public static <T> int count(Iterable<T> src) {
        int nr = 0;
        for (T item: src) { nr++; }
        return nr;
    }
    /**
     * Terminal operation
     */
    public static <T extends Comparable<T>> T max(Iterable<T> src) {
        Iterator<T> iter = src.iterator();
        if(!iter.hasNext()) throw new IllegalArgumentException("Source sequence is empty!");
        T first = iter.next();
        while(iter.hasNext()) {
            T curr = iter.next();
            if(curr.compareTo(first) > 0)
                first = curr;
        }
        return first;
    }


}
