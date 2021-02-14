package engine.users;

import engine.classes.member.Member;
import engine.customExceptions.UserIsAlreadyActiveException;

import java.util.*;

public class ActiveUsersManager {
    private final Set<String> activeUsernames;

    public ActiveUsersManager(){
        activeUsernames = new HashSet<>();
    }
    public void addUsername (String username){
        activeUsernames.add(username);
    }
    public void removeUsername(String username){
        activeUsernames.remove(username);
    }

    public boolean isUserActive(String username){
        return activeUsernames.contains(username);
    }
    public Set<String> getUsers(){
        return Collections.unmodifiableSet(activeUsernames);
    }


}
