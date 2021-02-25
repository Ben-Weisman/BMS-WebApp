package engine.users;

import java.util.*;

public class ActiveUsersManager {
    private final Set<Integer> activeUsersIDs;

    public ActiveUsersManager(){
        activeUsersIDs = new HashSet<>();
    }
    public void addUserID(int memberUserID){
        activeUsersIDs.add(memberUserID);
    }
    public void removeUsername(Integer username){
        activeUsersIDs.remove(username);
    }

    public boolean isUserActive(String username){
        return activeUsersIDs.contains(username);
    }
    public Set<Integer> getUsers(){
        return Collections.unmodifiableSet(activeUsersIDs);
    }


}
