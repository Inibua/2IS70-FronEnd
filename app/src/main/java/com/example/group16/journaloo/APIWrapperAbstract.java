package com.example.group16.journaloo;

import java.lang.Object;
//import java.lang.Enum<Token.Type>;

/**
 * Created by s169096 on 14-3-2018.
 */

public interface APIWrapperAbstract {
    // DECLARATION VARIABLES
    //private static final Token.Type ACCESSTOKEN;
    private String googleGeolocationAPIKey;
    private User currentUser;

    // DECLARATION METHODS

    /**
     * Function signup(). Accepts as parameters the current user that is being created.
     * uses getUser after successful sign-up
     *
     * @param currentUser - User to be created in data base
     */
    public abstract void signup(User currentUser);

    /**
     * Function login(). Accepts the current user from fields in login activity.
     * uses getUser() function to retrieve from backend.
     *
     * @param currentUser - User who is goind to be logged in
     */
    public abstract void login(User currentUser);

    /**
     * Function logout() just logouts the currently logged in user.
     *
     * @param currentUser - User who is currently logged in and will we logged out
     */
    public abstract void logout(User currentUser);

    /**
     * Gets new password from newPass field. G
     * and submits it to server to update the passed as parameter user in the backend.
     *
     * @param currentUser - User whose password will be changed
     */
    public abstract void resetPassword(User currentUser);

    /**
     * Function used to retrieve a user from backend.
     *
     * @param userId - userId used to get User
     * @return User
     */
    public abstract User getUser(String userId);

    /**
     * Function which updates the current users info. Like profile pic, name, description, age
     *
     * @param currentUser - the user to be updated
     */
    public abstract void updateUser(User currentUser);

    /**
     * Function which deletes the current user.
     *
     * @param currentUser - User to be deleted
     */
    public abstract void deleteUser(User currentUser);

    /**
     * Function which is used to get the currently logged in users journey.
     *
     * @param userId - User's id whose current journey will be returned if it is existing
     * @return currentJourney
     * @throws NoJourneyException if there is no active journey
     */
    public abstract Journey getCurrentJourney(String userId) throws NoJourneyException;

    /**
     * Gets an array of the current user's journeys, aka his past journeys.
     *
     * @param userId - User's Id whose journeys are retrieved
     * @param pageNr - which page number (which 10 journeys exactly)
     * @return Journey[] - array with 10 journeys depending on the pageNr
     */
    public abstract Journey[] getUserJourneys(String userId, int pageNr);

    /**
     * Retrieves Journeys from all users.
     *
     * @param pageNr
     * @return Journey[] - array with all the journeys that excist
     */
    public abstract Journey[] getAllJourneys(int pageNr);

    /**
     * Creates a journey for the user
     *
     * @param journey - Journey that user created
     */
    public abstract void createJourney(Journey journey);

    /**
     * Updates a journey for the user
     *
     * @param journey - Journey that user updated
     */
    public abstract void updatejourney(Journey journey);

    /**
     * Deletes a journey for the user
     *
     * @param journey - Journey that user deleted
     */
    public abstract void deleteJourney(Journey journey);

    /**
     * Retrieves a specific entry from the user
     *
     * @param userid - User's Id whose journeys are retrieved
     * @param entry - Entry user wants to get
     * @return entry
     */
    public abstract Entry getEntry(String userid, Entry entry);

    /**
     * Retrieves all the entries that belong to a certain journey
     *
     * @param userId - User's Id whose entries are retrieved
     * @param journey - Journey from which the entries are retrieved
     * @return Entry[] - array with all entries of that journey in it
     */
    public abstract Entry[] getJourneyEntries(String userId, Journey journey);

    /**
     * Retrieves all the entries that belong to a certain user
     *
     * @param userId
     * @param pageNum
     * @return Entry[] - array with all the entries of that person
     */
    public abstract Entry[] getAllEntries(String userId, int pageNum);

    /**
     * Creates an entry for the user
     *
     * @param entry
     */
    public abstract void createEntry(Entry entry);

    /**
     * Updates an entry for the user
     * @param entry
     */
    public abstract void updateEntry(Entry entry);

    /**
     * Deletes an entry for the user
     *
     * @param entry
     */
    public abstract void deleteEntry(Entry entry);

    /**
     * Getting coordinates to determine the location
     *
     * @param coordinates
     * @return coordinates - location from Google API
     */
    // Return the location from Google API
    public abstract String getLocation(String coordinates);
}
