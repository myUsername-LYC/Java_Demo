package day05.douyizhu;

import java.util.ArrayList;
import java.util.Iterator;
/**
 * 重写了toString方法，避免输出空格和左右方括号
 **/
public class MyList<E> extends ArrayList<E> {
    @Override
    public String toString() {
        Iterator<E> it = iterator();
        if (! it.hasNext())
            return "[]";

        StringBuilder sb = new StringBuilder();
        for (;;) {
            E e = it.next();
            sb.append(e == this ? "(this Collection)" : e);
            if (! it.hasNext())
                return sb.toString();
            sb.append(',');
        }
    }
}
