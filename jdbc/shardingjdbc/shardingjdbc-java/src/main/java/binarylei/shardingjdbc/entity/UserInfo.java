package binarylei.shardingjdbc.entity;

import lombok.Data;

@Data
public class UserInfo {
    private Long userId;
    private String userName;
    private String account;
    private String password;

    @Override
    public String toString() {
        return " -------- UserInfo { " +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
