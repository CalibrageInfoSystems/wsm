package in.calibrage.wsm.model;

import java.io.Serializable;

public class AddressModel implements Serializable {
    private Double lat;
    private Double log;
    private String Adrs;

    public AddressModel(Double lat, Double log, String adrs) {
        this.lat = lat;
        this.log = log;
        this.Adrs = adrs;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLog() {
        return log;
    }

    public void setLog(Double log) {
        this.log = log;
    }

    public String getAdrs() {
        return Adrs;
    }

    public void setAdrs(String adrs) {
        Adrs = adrs;
    }


}
