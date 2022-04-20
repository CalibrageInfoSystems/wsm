package in.calibrage.wsm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResTripImages {


    @SerializedName("affectedRows")
    @Expose
    private Integer affectedRows;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public Integer getAffectedRows() {
        return affectedRows;
    }

    public void setAffectedRows(Integer affectedRows) {
        this.affectedRows = affectedRows;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public class Datum {

        @SerializedName("TripImageUrl")
        @Expose
        private String tripImageUrl;
        @SerializedName("Id")
        @Expose
        private Integer id;
        @SerializedName("Code")
        @Expose
        private String code;
        @SerializedName("FileName")
        @Expose
        private String fileName;
        @SerializedName("FileLocation")
        @Expose
        private String fileLocation;
        @SerializedName("FileExtension")
        @Expose
        private String fileExtension;
        @SerializedName("SqNo")
        @Expose
        private Integer sqNo;
        @SerializedName("CreatedByUserId")
        @Expose
        private Integer createdByUserId;
        @SerializedName("CreatedBy")
        @Expose
        private String createdBy;
        @SerializedName("CreatedDate")
        @Expose
        private String createdDate;
        @SerializedName("UpdatedByUserId")
        @Expose
        private Integer updatedByUserId;
        @SerializedName("UpdatedDate")
        @Expose
        private String updatedDate;
        @SerializedName("IsActive")
        @Expose
        private Boolean isActive;
        @SerializedName("UpdatedBy")
        @Expose
        private String updatedBy;
        @SerializedName("StatusTypeId")
        @Expose
        private Integer statusTypeId;
        @SerializedName("Status")
        @Expose
        private String status;
        @SerializedName("Address")
        @Expose
        private String address;
        @SerializedName("Comments")
        @Expose
        private Object comments;

        public String getTripImageUrl() {
            return tripImageUrl;
        }

        public void setTripImageUrl(String tripImageUrl) {
            this.tripImageUrl = tripImageUrl;
        }

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

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getFileLocation() {
            return fileLocation;
        }

        public void setFileLocation(String fileLocation) {
            this.fileLocation = fileLocation;
        }

        public String getFileExtension() {
            return fileExtension;
        }

        public void setFileExtension(String fileExtension) {
            this.fileExtension = fileExtension;
        }

        public Integer getSqNo() {
            return sqNo;
        }

        public void setSqNo(Integer sqNo) {
            this.sqNo = sqNo;
        }

        public Integer getCreatedByUserId() {
            return createdByUserId;
        }

        public void setCreatedByUserId(Integer createdByUserId) {
            this.createdByUserId = createdByUserId;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
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

        public Boolean getIsActive() {
            return isActive;
        }

        public void setIsActive(Boolean isActive) {
            this.isActive = isActive;
        }

        public String getUpdatedBy() {
            return updatedBy;
        }

        public void setUpdatedBy(String updatedBy) {
            this.updatedBy = updatedBy;
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

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public Object getComments() {
            return comments;
        }

        public void setComments(Object comments) {
            this.comments = comments;
        }

    }
}
