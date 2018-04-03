package custom.base.entity;

public class UserSimpleInfo {
	private int userid = -1;
	private String phpssouid = "";// 头像或标号(老版本用来存头像，新版本存标号)
	private String nickname = "";
	private String chatusername = "";
	private String profile_image_url;

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public String getPhpssouid() {
		return phpssouid;
	}

	public void setPhpssouid(String phpssouid) {
		this.phpssouid = phpssouid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getChatusername() {
		return chatusername;
	}

	public void setChatusername(String chatusername) {
		this.chatusername = chatusername;
	}

	public String getProfile_image_url() {
		return profile_image_url;
	}

	public void setProfile_image_url(String profile_image_url) {
		this.profile_image_url = profile_image_url;
	}

	@Override
	public String toString() {
		return "UserSimpleInfo [userid=" + userid + ", phpssouid=" + phpssouid
				+ ", nickname=" + nickname + ", chatusername=" + chatusername
				+ ", profile_image_url=" + profile_image_url + "]";
	}

}
