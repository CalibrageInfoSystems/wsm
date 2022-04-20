package in.calibrage.wsm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReqDevision {
    @SerializedName("DistrictIds")
    @Expose
    private String DistrictIds;

    public String getDistrictIdsIds() {
        return DistrictIds;
    }

    public void setDistrictIds(String DistrictIds) {
        this.DistrictIds = DistrictIds;
    }

}