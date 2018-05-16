package custom.frame.bean;

import java.io.Serializable;

/**
 * 登录成功
 *
 * @author DUNDUN
 */
public class LoginSuccessBean implements Serializable {
    private static final long serialVersionUID = 1574016439324324068L;

    private int focusStatus;
    private String doctorId;
    private String phone;
    private int gender;
    private String doctorDescription;
    private int hospitalId;
    private int communityRequired;
    private int communityId;
    private int visited;
    private int weight;
    private int departmentId;
    private String title;
    private String portraitUrl;
    private int hosptialRequired;
    private String hospital;
    private String department;
    private int status;
    private String name;
    /**
     * 医生认证
     * <0-未上传资料（可修改）；1-已上传资料等待审核（不可修改）；2-审核未通过（可修改）；6-审核已通过（不可修改）>
     */
    private int checked;

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getFocusStatus() {
        return focusStatus;
    }

    public void setFocusStatus(int focusStatus) {
        this.focusStatus = focusStatus;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getDoctorDescription() {
        return doctorDescription;
    }

    public void setDoctorDescription(String doctorDescription) {
        this.doctorDescription = doctorDescription;
    }

    public int getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(int hospitalId) {
        this.hospitalId = hospitalId;
    }

    public int getCommunityRequired() {
        return communityRequired;
    }

    public void setCommunityRequired(int communityRequired) {
        this.communityRequired = communityRequired;
    }

    public int getCommunityId() {
        return communityId;
    }

    public void setCommunityId(int communityId) {
        this.communityId = communityId;
    }

    public int getVisited() {
        return visited;
    }

    public void setVisited(int visited) {
        this.visited = visited;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPortraitUrl() {
        return portraitUrl;
    }

    public void setPortraitUrl(String portraitUrl) {
        this.portraitUrl = portraitUrl;
    }

    public int getHosptialRequired() {
        return hosptialRequired;
    }

    public void setHosptialRequired(int hosptialRequired) {
        this.hosptialRequired = hosptialRequired;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getChecked()
    {
        return checked;
    }

    public void setChecked(int checked)
    {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return "[LoginSuccessBean]->doctorId:" + doctorId
                + ",phone:" + phone
                + ",focusStatus:" + focusStatus
                + ",gender:" + gender
                + ",doctorDescription:" + doctorDescription
                + ",hospitalId:" + hospitalId
                + ",communityRequired:" + communityRequired
                + ",communityId:" + communityId
                + ",visited:" + visited
                + ",weight:" + weight
                + ",departmentId:" + departmentId
                + ",title:" + title
                + ",portraitUrl:" + portraitUrl
                + ",hosptialRequired:" + hosptialRequired
                + ",hospital:" + hospital
                + ",department:" + department
                + ",status:" + status
                + ",checked:" + checked
                + ",name:" + name;
    }
}
