package pm.shane.alexaclone.api.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Shane on 20/10/2017.
 */

public class GenericResponse<T> {

    @SerializedName("data")
    private T data;
    @SerializedName("errors")
    private List<Error> errors;

    public T getData() {
        return data;
    }

    public List<Error> getErrors() {
        return errors;
    }

}
