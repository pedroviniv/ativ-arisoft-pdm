package pdm.agifpb.firstapp.domain;

/**
 * Created by Pedro Arthur on 20/02/2017.
 */

public interface JsonObject<T> {

    String toJson();
    T fromJson(String json);

}
