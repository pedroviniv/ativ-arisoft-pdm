package pdm.agifpb.firstapp.services;

import java.util.List;

import pdm.agifpb.firstapp.domain.Chat;
import pdm.agifpb.firstapp.domain.User;

/**
 * Created by Pedro Arthur on 20/02/2017.
 */

public class UserSession {

    private static User loggedUser;

    public static void login(User user) {
        loggedUser = user;
    }

    public static void logout() {
        loggedUser = null;
    }

    public static User getLoggedUser() {
        return loggedUser;
    }
}
