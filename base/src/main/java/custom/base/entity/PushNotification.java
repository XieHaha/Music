package custom.base.entity;

/**
 * Created by luozi on 2015/10/9.
 */
public class PushNotification {
    private int push_id;//推送id
    private String push_title;//标题
    private String push_state;//状态
    private String push_time;//时间
    private String push_receive_time;//接收时间
    private int push_action;//推送动作行为
    private String push_extras;//键值对
    private String push_user_id;//用户id
    private String push_user_paycard;//用户卡号

    public int getPush_id() {
        return push_id;
    }

    public void setPush_id(int push_id) {
        this.push_id = push_id;
    }

    public String getPush_title() {
        return push_title;
    }

    public void setPush_title(String push_title) {
        this.push_title = push_title;
    }


    public String getPush_state() {
        return push_state;
    }

    public void setPush_state(String push_state) {
        this.push_state = push_state;
    }

    public String getPush_time() {
        return push_time;
    }

    public void setPush_time(String push_time) {
        this.push_time = push_time;
    }

    public String getPush_receive_time() {
        return push_receive_time;
    }

    public void setPush_receive_time(String push_receive_time) {
        this.push_receive_time = push_receive_time;
    }


    public String getPush_extras() {
        return push_extras;
    }

    public void setPush_extras(String push_extras) {
        this.push_extras = push_extras;
    }

    public String getPush_user_id() {
        return push_user_id;
    }

    public void setPush_user_id(String push_user_id) {
        this.push_user_id = push_user_id;
    }

    public String getPush_user_paycard() {
        return push_user_paycard;
    }

    public void setPush_user_paycard(String push_user_paycard) {
        this.push_user_paycard = push_user_paycard;
    }

    public int getPush_action() {
        return push_action;
    }

    public void setPush_action(int push_action) {
        this.push_action = push_action;
    }

    @Override
    public String toString() {
        return "PushNotification{" +
                "push_id=" + push_id +
                ", push_title='" + push_title + '\'' +
                ", push_state='" + push_state + '\'' +
                ", push_time='" + push_time + '\'' +
                ", push_receive_time='" + push_receive_time + '\'' +
                ", push_action=" + push_action +
                ", push_extras='" + push_extras + '\'' +
                ", push_user_id='" + push_user_id + '\'' +
                ", push_user_paycard='" + push_user_paycard + '\'' +
                '}';
    }
}
