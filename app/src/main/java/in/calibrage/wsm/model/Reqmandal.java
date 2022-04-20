package in.calibrage.wsm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Reqmandal {

    @SerializedName("DivisionIds")
    @Expose
    private String divisionIds;

    public String getDivisionIds() {
        return divisionIds;
    }

    public void setDivisionIds(String divisionIds) {
        this.divisionIds = divisionIds;
    }

}