package pm.shane.alexaclone.api;

import java.util.List;

import io.reactivex.Observable;
import pm.shane.alexaclone.api.response.GenericResponse;
import pm.shane.alexaclone.api.response.data.Carer;
import pm.shane.alexaclone.api.response.data.Company;
import pm.shane.alexaclone.api.response.data.Patient;
import pm.shane.alexaclone.api.response.data.User;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Shane on 20/10/2017.
 */

public interface CareApi {

    String BASE_URL = "http://192.168.2.126/api/";

    @Headers("X-Requested-With: XMLHttpRequest")
    @POST("login")
    Observable<GenericResponse<User>> login(@Body Credentials credentials);
    @POST("register")
    Observable<GenericResponse<User>> register(@Body Credentials credentials);
    @GET("logout")
    Observable<GenericResponse<Void>> logout();
    @GET("patients")
    Observable<GenericResponse<List<Patient>>> getPatients();
    @GET("carers")
    Observable<GenericResponse<List<Carer>>> getCarers();
    @GET("companies")
    Observable<GenericResponse<List<Company>>> getCompanies();

}