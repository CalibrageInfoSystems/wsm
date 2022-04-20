package in.calibrage.wsm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReqVillage {
    @SerializedName("MandalIds")
    @Expose
    private String mandalIds;

    public String getMandalIds() {
        return mandalIds;
    }

    public void setMandalIds(String mandalIds) {
        this.mandalIds = mandalIds;
    }

}
