package me.arminb.sara;


public interface UserService {
    public void addUser(UserBean user);
    public UserBean getUser(long userid);

}