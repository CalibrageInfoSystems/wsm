package in.calibrage.wsm.service;

public interface APIConstantURL {

     public static  final  String LOCAL_URL="http://183.82.111.111/WSM/API/api/";
     public static final String LOGIN_URL = "Account/Login";
     public static final String ADD_UPDATE_TRIP = "Trip/AddUpdateTrip";
     public static final String ADD_TRIP_IMAGE = "Trip/AddUpdateTripImage";
     public static final String GetTripImagesByCode = "Trip/GetTripImagesByCode/";
     public static final String DELETE_TRIP_IMAGE = "Trip/DeleteImageById/";
     public static final String GET_ALL_TRIPS_BY_USERID = "Trip/GetTripDetailsByUserId";
     public static final String AddGeoLocation = "GeoLocation/AddGeoLocation";
     public static final String GetCurrentDayTripsByUser = "Trip/GetCurrentDayTripsByUser/";


     public static final String GetDistrictByStateIds = "Districts/GetDistrictByStateIds";
     public static final String GetDivision = "Divisions/GetDivisionByDistrictIds";
     public static final String GetMandal = "Mandals/GetMandalByDivisionIds";
     public static final String GetVillage = "Villages/GetVillageByMandalIds";

     public static final String Register = "Account/Register";


}
