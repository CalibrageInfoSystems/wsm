package in.calibrage.wsm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResUserData {
    @SerializedName("access_token")
    @Expose
    private String accessToken;
    @SerializedName("token_type")
    @Expose
    private String tokenType;
    @SerializedName("expires_in")
    @Expose
    private Integer expiresIn;
    @SerializedName("UserInfos")
    @Expose
    private UserInfos userInfos;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    public UserInfos getUserInfos() {
        return userInfos;
    }

    public void setUserInfos(UserInfos userInfos) {
        this.userInfos = userInfos;
    }
    public class UserInfos {

        @SerializedName("Id")
        @Expose
        private Integer id;
        @SerializedName("UserId")
        @Expose
        private String userId;
        @SerializedName("FirstName")
        @Expose
        private String firstName;
        @SerializedName("MiddleName")
        @Expose
        private Object middleName;
        @SerializedName("LastName")
        @Expose
        private String lastName;
        @SerializedName("FullName")
        @Expose
        private String fullName;
        @SerializedName("UserName")
        @Expose
        private String userName;
        @SerializedName("Password")
        @Expose
        private String password;
        @SerializedName("RoleId")
        @Expose
        private Integer roleId;
        @SerializedName("RoleName")
        @Expose
        private String roleName;
        @SerializedName("ContactNumber")
        @Expose
        private String contactNumber;
        @SerializedName("Email")
        @Expose
        private String email;
        @SerializedName("IsActive")
        @Expose
        private Boolean isActive;
        @SerializedName("CreatedByUserId")
        @Expose
        private Integer createdByUserId;
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

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public Object getMiddleName() {
            return middleName;
        }

        public void setMiddleName(Object middleName) {
            this.middleName = middleName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

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

        public Integer getRoleId() {
            return roleId;
        }

        public void setRoleId(Integer roleId) {
            this.roleId = roleId;
        }

        public String getRoleName() {
            return roleName;
        }

        public void setRoleName(String roleName) {
            this.roleName = roleName;
        }

        public String getContactNumber() {
            return contactNumber;
        }

        public void setContactNumber(String contactNumber) {
            this.contactNumber = contactNumber;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Boolean getIsActive() {
            return isActive;
        }

        public void setIsActive(Boolean isActive) {
            this.isActive = isActive;
        }

        public Integer getCreatedByUserId() {
            return createdByUserId;
        }

        public void setCreatedByUserId(Integer createdByUserId) {
            this.createdByUserId = createdByUserId;
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
