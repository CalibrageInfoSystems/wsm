package in.calibrage.wsm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReqLogin {
    @SerializedName("UserName")
    @Expose
    private String userName;
    @SerializedName("Password")
    @Expose
    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
