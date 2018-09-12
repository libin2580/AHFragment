package alhamdi.com.cybraum.meridianuae.app.alhameedifragment.models;

/**
 * Created by anvin on 1/13/2017.
 */

public class MyLawyerModel {

    String law_id;
    String name;
    String email;
    String phone;
    String department;
    String description;

    public String getCases() {
        return cases;
    }

    public void setCases(String cases) {
        this.cases = cases;
    }

    String cases;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }



    String image;

    public String getLaw_id() {
        return law_id;
    }

    public void setLaw_id(String law_id) {
        this.law_id = law_id;
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
