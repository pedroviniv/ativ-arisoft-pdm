package pdm.agifpb.firstapp.view.util;

import java.util.Collection;
import java.util.List;

/**
 * Created by Pedro Arthur on 23/02/2017.
 */

public class Print<T> {

    public String print(List<T> list) {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        int size = list.size();
        for(int i = 0; i < size; i++) {
            builder.append(list.get(i));
            if(i < size-1) {
                builder.append(", ");
            }
        }
        builder.append(" }");

        return builder.toString();
    }
}
