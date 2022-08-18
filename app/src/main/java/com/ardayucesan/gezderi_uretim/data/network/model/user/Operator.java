package com.ardayucesan.gezderi_uretim.data.network.model.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Operator implements Serializable {

    private static final long serialVersionUID = -7060210544600464481L;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("auth")
    @Expose
    private String auth;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("machine")
    @Expose
    private String machine;

    /**
     * No args constructor for use in serialization
     *
     */
    public Operator() {
    }

    /**
     *
     * @param auth
     * @param machine
     * @param name
     * @param id
     * @param title
     * @param token
     */
    public Operator(String id, String name, String title, String auth, String token, String machine) {
        super();
        this.id = id;
        this.name = name;
        this.title = title;
        this.auth = auth;
        this.token = token;
        this.machine = machine;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMachine() {
        return machine;
    }

    public void setMachine(String machine) {
        this.machine = machine;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Operator.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("id");
        sb.append('=');
        sb.append(((this.id == null)?"<null>":this.id));
        sb.append(',');
        sb.append("name");
        sb.append('=');
        sb.append(((this.name == null)?"<null>":this.name));
        sb.append(',');
        sb.append("title");
        sb.append('=');
        sb.append(((this.title == null)?"<null>":this.title));
        sb.append(',');
        sb.append("auth");
        sb.append('=');
        sb.append(((this.auth == null)?"<null>":this.auth));
        sb.append(',');
        sb.append("token");
        sb.append('=');
        sb.append(((this.token == null)?"<null>":this.token));
        sb.append(',');
        sb.append("machine");
        sb.append('=');
        sb.append(((this.machine == null)?"<null>":this.machine));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}