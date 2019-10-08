

import SetFieldAttribute;

import java.util.List;
/**
 * Created by kzyuan on 2019-09-05 11:33
 */
public class UserBean {
    private static final long i = 1L;

    @SetFieldAttribute(fieldName="userId")
    private String id;

    @SetFieldAttribute(fieldName="userName")
    private String name;

    @SetFieldAttribute(fieldName="userAge")
    private String age;

    @SetFieldAttribute(fieldName="userEmail")
    private String email;

    @SetFieldAttribute(fieldName="userFavourite", fieldType="list")
    private List<String> favourite;

    /**
     * @return the id
     */
    @SetFieldAttribute(fieldName="userGetId")
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    @SetFieldAttribute(fieldName="userGetName")
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the age
     */
    @SetFieldAttribute(fieldName="userGetAge")
    public String getAge() {
        return age;
    }

    /**
     * @param age the age to set
     */
    public void setAge(String age) {
        this.age = age;
    }

    /**
     * @return the email
     */
    @SetFieldAttribute(fieldName="userGetEmail")
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the favourite
     */
    @SetFieldAttribute(fieldName="userGetFavourite", fieldType="list")
    public List<String> getFavourite() {
        return favourite;
    }

    /**
     * @param favourite the favourite to set
     */
    public void setFavourite(List<String> favourite) {
        this.favourite = favourite;
    }
}