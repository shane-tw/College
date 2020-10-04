package pm.shane.alexaclone.api;

import java.util.List;

import io.reactivex.Observable;
import pm.shane.alexaclone.api.response.GenericResponse;
import pm.shane.alexaclone.api.response.data.Business;
import pm.shane.alexaclone.api.response.data.Carer;
import pm.shane.alexaclone.api.response.data.Company;
import pm.shane.alexaclone.api.response.data.Patient;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Shane on 20/10/2017.
 */

public interface CareApi {

    String BASE_URL = "http://vca.shane.pm/api/";
    @POST("me")
    Observable<GenericResponse<Patient>> me(@Body Patient patient);
    @POST("login")
    Observable<GenericResponse<Patient>> login(@Body Credentials credentials);
    @POST("register")
    Observable<GenericResponse<Patient>> register(@Body Credentials credentials);
    @GET("logout")
    Observable<GenericResponse<Void>> logout();
    @GET("patients")
    Observable<GenericResponse<List<Patient>>> getPatients();
    @GET("carers")
    Observable<GenericResponse<List<Carer>>> getCarers();
    @GET("companies")
    Observable<GenericResponse<List<Company>>> getCompanies();
    @POST("places")
    Observable<GenericResponse<List<Business>>> getPlaces(@Body PlaceFilter placeFilter);
    @POST("carers")
    Observable<GenericResponse<List<Carer>>> getCarers(@Body PlaceFilter placeFilter);
}