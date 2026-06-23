/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package session;


import models.mUser;

public class session {
    
    private static mUser currentUser;

    public static mUser getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(mUser currentUser) {
        session.currentUser = currentUser;
    }

    public static void closeSession() {
        currentUser = null;
    }
}

