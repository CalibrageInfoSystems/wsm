package in.calibrage.wsm.service;

import com.google.gson.JsonObject;

import java.util.List;

import in.calibrage.wsm.model.ResDist;
import in.calibrage.wsm.model.ResDivision;
import in.calibrage.wsm.model.ResGeoLatLong;
import in.calibrage.wsm.model.ResLastTrip;
import in.calibrage.wsm.model.ResMandal;
import in.calibrage.wsm.model.ResRegister;
import in.calibrage.wsm.model.ResTripImages;
import in.calibrage.wsm.model.ResUserData;
import in.calibrage.wsm.model.ResVillage;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;
import rx.Observable;


public interface ApiService {

    @POST(APIConstantURL.LOGIN_URL)
    Observable<ResUserData> postLogin(@Body JsonObject data);

    @POST(APIConstantURL.ADD_UPDATE_TRIP)
    Observable<ResLastTrip.Tripdate> postTrip(@Body JsonObject data);

    @POST(APIConstantURL.ADD_TRIP_IMAGE)
    Observable<ResTripImages> postTripImage(@Body JsonObject data);

    @POST(APIConstantURL.GET_ALL_TRIPS_BY_USERID)
    Observable<List<ResLastTrip>> postGetTrips(@Body JsonObject data);

    @GET
    Observable<String> deleteTripImage(@Url String data);

    @GET
    Observable<ResTripImages> getTripImages(@Url String data);

    /*
     * @Get Current Day All Trips
     * if User have trips Count 10 show ten trips
     *
     * */
    @GET
    Observable<ResLastTrip> getCurrentDayTrips(@Url String data);

    @POST(APIConstantURL.AddGeoLocation)
    Observable<ResGeoLatLong> addGeoLocations(@Body JsonObject data);

    @POST(APIConstantURL.GetDistrictByStateIds)
    Observable<List<ResDist>> getDisticts(@Body JsonObject data);

    @POST(APIConstantURL.GetDivision)
    Observable<List<ResDivision>> getDevisions(@Body JsonObject data);

    @POST(APIConstantURL.GetMandal)
    Observable<List<ResMandal>> getMandals(@Body JsonObject data);

    @POST(APIConstantURL.GetVillage)
    Observable<List<ResVillage>> getVillages(@Body JsonObject data);

    @POST(APIConstantURL.Register)
    Observable<ResRegister> registerDriver(@Body JsonObject data);
}
