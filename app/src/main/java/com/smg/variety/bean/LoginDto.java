package com.smg.variety.bean;

public class LoginDto {
    private String  access_token;
    private String  token_type;
    private int     expires_in;
    public int     status;
    private DataDto<PersonalInfoDto> user;


    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public DataDto<PersonalInfoDto> getUser() {
        return user;
    }

    public void setUser(DataDto<PersonalInfoDto> user) {
        this.user = user;
    }
}
