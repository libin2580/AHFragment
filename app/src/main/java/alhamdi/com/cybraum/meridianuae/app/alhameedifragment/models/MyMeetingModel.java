package alhamdi.com.cybraum.meridianuae.app.alhameedifragment.models;

/**
 * Created by anvin on 12/27/2016.
 */

public class MyMeetingModel {

    String meet_id;
    String description;
    String user_id;
    String date;
    String time_from;
    String name;
    String email;
    String phone;
    String based_cases;

    public String getLawyers() {
        return lawyers;
    }

    public void setLawyers(String lawyers) {
        this.lawyers = lawyers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBased_cases() {
        return based_cases;
    }

    public void setBased_cases(String based_cases) {
        this.based_cases = based_cases;
    }

    String lawyers;

    public String getMeet_id() {
        return meet_id;
    }

    public void setMeet_id(String meet_id) {
        this.meet_id = meet_id;
    }



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime_from() {
        return time_from;
    }

    public void setTime_from(String time_from) {
        this.time_from = time_from;
    }


}
