package engine.alerts;

import java.util.ArrayList;
import java.util.List;

public class MemberNotificationManager {
    List<MemberNotification> autoNotifications;
    List<MemberNotification> adminNotifications;

    public MemberNotificationManager(){
        autoNotifications = new ArrayList<>();
        adminNotifications = new ArrayList<>();
    }

    public List<MemberNotification> getAutoNotifications() {

        return this.autoNotifications;

    }

    public List<MemberNotification> getAdminNotifications(int offset){
        if (offset < 0 || offset > adminNotifications.size())
            offset = 0;
        return adminNotifications.subList(offset, adminNotifications.size());
    }

    public void addAdminNotification(MemberNotification mn){
        this.adminNotifications.add(mn);
    }
    public void addAutoNotification(MemberNotification mn){this.autoNotifications.add(mn);}


    public void clearAutoMessages(){
        this.autoNotifications.clear();
    }

    public int getVersion(){
        return autoNotifications.size();
    }
}
