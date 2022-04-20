package in.calibrage.wsm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResLastTrip {


    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("statusMessage")
    @Expose
    private String statusMessage;
    @SerializedName("tripCount")
    @Expose
    private Integer tripCount;
    @SerializedName("tripdate")
    @Expose
    private List<Tripdate> tripdate = null;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public Integer getTripCount() {
        return tripCount;
    }

    public void setTripCount(Integer tripCount) {
        this.tripCount = tripCount;
    }

    public List<Tripdate> getTripdate() {
        return tripdate;
    }

    public void setTripdate(List<Tripdate> tripdate) {
        this.tripdate = tripdate;
    }


   public class Tripdate {

        @SerializedName("Id")
        @Expose
        private Integer id;
        @SerializedName("Code")
        @Expose
        private String code;
        @SerializedName("UserId")
        @Expose
        private Integer userId;
        @SerializedName("StartTime")
        @Expose
        private String startTime;
        @SerializedName("EndTime")
        @Expose
        private Object endTime;
        @SerializedName("CreatedByUserId")
        @Expose
        private Integer createdByUserId;
        @SerializedName("StatusTypeId")
        @Expose
        private Integer statusTypeId;
        @SerializedName("Status")
        @Expose
        private String status;
        @SerializedName("CreatedDate")
        @Expose
        private String createdDate;
        @SerializedName("UpdatedByUserId")
        @Expose
        private Integer updatedByUserId;
        @SerializedName("UpdatedDate")
        @Expose
        private String updatedDate;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public Object getEndTime() {
            return endTime;
        }

        public void setEndTime(Object endTime) {
            this.endTime = endTime;
        }

        public Integer getCreatedByUserId() {
            return createdByUserId;
        }

        public void setCreatedByUserId(Integer createdByUserId) {
            this.createdByUserId = createdByUserId;
        }

        public Integer getStatusTypeId() {
            return statusTypeId;
        }

        public void setStatusTypeId(Integer statusTypeId) {
            this.statusTypeId = statusTypeId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public Integer getUpdatedByUserId() {
            return updatedByUserId;
        }

        public void setUpdatedByUserId(Integer updatedByUserId) {
            this.updatedByUserId = updatedByUserId;
        }

        public String getUpdatedDate() {
            return updatedDate;
        }

        public void setUpdatedDate(String updatedDate) {
            this.updatedDate = updatedDate;
        }

    }
}