package in.calibrage.wsm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReqDist {
    @SerializedName("StateIds")
    @Expose
    private String StateIds;

    public String getDistrictIds() {
        return StateIds;
    }

    public void setStateIdsIds(String StateIds) {
        this.StateIds = StateIds;
    }
}

