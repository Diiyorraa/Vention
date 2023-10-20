package com.example.demo4.UserRoles;


import java.util.Set;
public class PermissionMan{
    private Set<String> moderators;
    private Set<String> users;

    public PermissionMan(Set<String> moderators, Set<String> users) {
        this.moderators = moderators;
        this.users = users;
    }

    public boolean hasPermissionForModerator(String username) {
        return moderators.contains(username);
    }

    public boolean hasPermissionForUser(String username) {
        return users.contains(username);
    }
}
