package ets.listeners;

import java.util.ArrayList;
import java.util.List;

public class Refreshable {

    private static final List<Database> DATABASES = new ArrayList<>();    
    
    public interface Database {
        void updateDatabase();
    }

    public interface UI {
        void updateUI();
    }
    
    public static void invoke(){
        for(Database database : DATABASES){
            database.updateDatabase();
        }
    }
    
    public static void register(Database database){
        DATABASES.add(database);
    }
   
    public static void unregister(Database database){
        DATABASES.remove(database);
    }
}
