package com.example.group16.journaloo;

/**
 * Created by h1p3r on 16-Mar-18.
 */

public class APIWrapperUserActivities extends APIWrapperAbstract {
    @Override
    public void signup(User currentUser) {
        
    }

    @Override
    public void login(User currentUser) {

    }

    @Override
    public void logout(User currentUser) {

    }

    @Override
    public void resetPassword(User currentUser) {

    }

    @Override
    public User getUser(String userId) {
        return null;
    }

    @Override
    public void updateUser(User currentUser) {

    }

    @Override
    public void deleteUser(User currentUser) {

    }

    @Override
    public Journey getCurrentJourney(String userId) throws NoJourneyException {
        return null;
    }

    @Override
    public Journey[] getUserJourneys(String userId, int pageNr) {
        return new Journey[0];
    }

    @Override
    public Journey[] getAllJourneys(int pageNr) {
        return new Journey[0];
    }

    @Override
    public void createJourney(Journey journey) {

    }

    @Override
    public void updatejourney(Journey journey) {

    }

    @Override
    public void deleteJourney(Journey journey) {

    }

    @Override
    public Entry getEntry(String userid, Entry entry) {
        return null;
    }

    @Override
    public Entry[] getJourneyEntries(String userId, Journey journey) {
        return new Entry[0];
    }

    @Override
    public Entry[] getAllEntries(String userId, int pageNum) {
        return new Entry[0];
    }

    @Override
    public void createEntry(Entry entry) {

    }

    @Override
    public void updateEntry(Entry entry) {

    }

    @Override
    public void deleteEntry(Entry entry) {

    }

    @Override
    public String getLocation(String coordinates) {
        return null;
    }
}
