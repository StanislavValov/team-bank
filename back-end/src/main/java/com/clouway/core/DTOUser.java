package com.clouway.core;

/**
 * Created by clouway on 14-9-25.
 */
public class DTOUser {

    private String username;
    private String password;


    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DTOUser DTOUser = (DTOUser) o;

        if (password != null ? !password.equals(DTOUser.password) : DTOUser.password != null) return false;
        if (username != null ? !username.equals(DTOUser.username) : DTOUser.username != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }
}
