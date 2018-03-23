package com.example.group16.journaloo;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.Object;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
//import java.lang.Enum<Token.Type>;

/**
 * Created by s169096 on 14-3-2018.
 */

public class APIWrapper {
    // DECLARATION VARIABLES

    private static final String TAG = MainActivity.class.getName();
    private APIWrapper wrapper;
    private JSONObject obj;
    private OkHttpClient client;
    private String url;
    private Request request;

    APIWrapper () {

    }

    // Singleton pattern, public
    public APIWrapper getWrapper() {
        if (wrapper == null) {
            wrapper = new APIWrapper();
        }
        return wrapper;
    }


    // DECLARATION METHODS

    /**
     * Function signup(). Accepts as parameters the current user that is being created.
     * uses getUser after successful sign-up
     *
     * @param currentUser - User to be created in data base
     */
    public void signup(User currentUser) { // POST
        obj = new JSONObject();
        url = "https://polar-cove-19347.herokuapp.com/user";
        client = new OkHttpClient();

        try {
            obj.put("username", currentUser.userName);
            obj.put("email", currentUser.email);
            obj.put("password", currentUser.password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MediaType JSON = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(JSON,
                obj.toString());
        request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Cache-Control", "no-cache")
                .addHeader("Postman-Token", "57ab0c2b-088b-1811-2c38-9c469fae5b69")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, response.body().toString());
            }
        });
    }

    /**
     * Function login(). Accepts the current user from fields in login activity.
     * uses getUser() function to retrieve from backend.
     *
     * @param currentUser - User who is goind to be logged in
     */
    public void login(User currentUser) { // POST
        obj = new JSONObject();
        url = "https://polar-cove-19347.herokuapp.com/user/login";
        client = new OkHttpClient();

        try {
            obj.put("username", currentUser.userName);
            obj.put("password", currentUser.password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MediaType JSON = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(JSON,
                obj.toString());
        request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Cache-Control", "no-cache")
                .addHeader("Postman-Token", "4670edef-4cc4-8d2c-5fc9-19b09d9c3e11")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, e.getMessage());
                //create ne logged in user by using one of the constructors
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, response.body().toString());
            }
        });
    }

    /**
     * Function logout() just logouts the currently logged in user.
     *
     * @param currentUser - User who is currently logged in and will we logged out
     */
    public void logout(User currentUser) { // POST?
        obj = new JSONObject();
        url = "https://polar-cove-19347.herokuapp.com/user/logout";
        client = new OkHttpClient();
    }

    /**
     * Gets new password from newPass field. G
     * and submits it to server to update the passed as parameter user in the backend.
     *
     * @param currentUser - User whose password will be changed
     */
    public void resetPassword(User currentUser) { // PUT
        obj = new JSONObject();
        url = "https://polar-cove-19347.herokuapp.com/user/reset_password";
        client = new OkHttpClient();
    }

    /**
     * Function used to retrieve a user from backend.
     *
     * @param userId - userId used to get User
     * @return User
     */
    public User getUser(String userId) { // GET?
        obj = new JSONObject();
        url = "https://polar-cove-19347.herokuapp.com/user?userId=" + userId;
        client = new OkHttpClient();
        return new User();
    }

    /**
     * Function which updates the current users info. Like profile pic, name, description, age
     *
     * @param currentUser - the user to be updated
     */
    public void updateUser(User currentUser) { // PUT
        obj = new JSONObject();
        url = "https://polar-cove-19347.herokuapp.com/user";
        client = new OkHttpClient();
    }

    /**
     * Function which deletes the current user.
     *
     * @param currentUser - User to be deleted
     */
    public void deleteUser(User currentUser) { // DELETE
        obj = new JSONObject();
        url = "https://polar-cove-19347.herokuapp.com/user";
        client = new OkHttpClient();
    }

    /**
     * Creates a journey for the user
     *
     * @param journey - Journey that user created
     */
    public void createJourney(Journey journey) { // POST
        obj = new JSONObject();
        url = "https://polar-cove-19347.herokuapp.com/journey";
        client = new OkHttpClient();
    }

    /**
     * Function which is used to get the currently logged in users journey.
     *
     * @param journeyId - Journey's id of which journey will be returned if it is existing
     * @return currentJourney
     * @throws NoJourneyException if there is no active journey
     */
    public Journey getCurrentJourney(String journeyId) throws NoJourneyException { // GET? // hwo exactly do we get current journey?
        obj = new JSONObject();
        url = "https://polar-cove-19347.herokuapp.com/journey?journeyId=" + journeyId;
        client = new OkHttpClient();
        return new Journey();
    }

    /**
     * Gets an array of the current user's journeys, aka his past journeys.
     *
     * @param userId - User's Id whose journeys are retrieved
     * @param pageNr - which page number (which 10 journeys exactly)
     * @return Journey[] - array with 10 journeys depending on the pageNr
     */
    public Journey[] getUserJourneys(String userId, int pageNr){ // GET
        obj = new JSONObject();
        url = "https://polar-cove-19347.herokuapp.com/journey?userId=" + userId;
        client = new OkHttpClient();
        return new Journey[5];
    }

    /**
     * Retrieves Journeys from all users.
     *
     * @param pageNr - which page number (which 10 journeys exactly)
     * @return Journey[] - array with all the journeys that excist
     */
    public Journey[] getAllJourneys(int pageNr) { // GET How do we get all journeys?
        obj = new JSONObject();
        url = "https://polar-cove-19347.herokuapp.com/journey";
        client = new OkHttpClient();
        return new Journey[5];
    }

    /**
     * Updates a journey for the user
     *
     * @param journey - Journey that user updated
     */
    public void updateJourney(Journey journey) { // PUT
        obj = new JSONObject();
        url = "https://polar-cove-19347.herokuapp.com/journey?journeyId=" + journey.journeyId;
        client = new OkHttpClient();
    }

    /**
     * Deletes a journey for the user
     *
     * @param journey - Journey that user deleted
     */
    public void deleteJourney(Journey journey) { //DELETE
        obj = new JSONObject();
        url = "https://polar-cove-19347.herokuapp.com/journey?journeyId=" + journey.journeyId;
        client = new OkHttpClient();
    }

    /**
     * Retrieves a specific entry from the user
     *
     * @param userId - User's Id whose journeys are retrieved
     * @param entry - Entry user wants to get
     * @return entry
     */
    public Entry getEntry(String userId, Entry entry) { // GET
        obj = new JSONObject();
        url = "https://polar-cove-19347.herokuapp.com/entry?entryId=" + entry.entryId;
        client = new OkHttpClient();
        return new Entry();
    }

    /**
     * Retrieves all the entries that belong to a certain journey
     *
     * @param userId - User's Id whose entries are retrieved
     * @param journey - Journey from which the entries are retrieved
     * @return Entry[] - array with all entries of that journey in it
     */
    public Entry[] getJourneyEntries(String userId, Journey journey) { // GET
        obj = new JSONObject();
        url = "https://polar-cove-19347.herokuapp.com/entry?journeyId=" + journey.journeyId;
        client = new OkHttpClient();
        return new Entry[5];
    }

    /**
     * Retrieves all the entries that belong to a certain user
     *
     * @param userId - User's Id whose entries are retrieved
     * @param pageNum - which page number (which 10 entries exactly)
     * @return Entry[] - array with all the entries of that person
     */
    public Entry[] getAllEntries(String userId, int pageNum) { // GET What should this do
        obj = new JSONObject();
        url = "https://polar-cove-19347.herokuapp.com/entry";
        client = new OkHttpClient();
        return new Entry[5];
    }

    /**
     * Creates an entry for the user
     *
     * @param entry - Entry user wants to create
     */
    public void createEntry(Entry entry, Journey journey) { // POST
        obj = new JSONObject();
        url = "https://polar-cove-19347.herokuapp.com/entry/" + journey.journeyId;
        client = new OkHttpClient();
    }

    /**
     * Updates an entry for the user
     * @param entry - Entry user wants to update
     */
    public void updateEntry(Entry entry) { // PUT
        obj = new JSONObject();
        url = "https://polar-cove-19347.herokuapp.com/entry/" + entry.entryId;
        client = new OkHttpClient();
    }

    /**
     * Deletes an entry for the user
     *
     * @param entry - Entry user wants to delete
     */
    public void deleteEntry(Entry entry) { //DELETE
        obj = new JSONObject();
        url = "https://polar-cove-19347.herokuapp.com/entry/" + entry.entryId;
        client = new OkHttpClient();
    }

    /**
     * Getting coordinates to determine the location
     *
     * @param coordinates - coordinates of GPS
     * @return coordinates - location from Google API
     */
    // Return the location from Google API
    public String getLocation(String coordinates) { // GET Nothing on swagger? Research?
        obj = new JSONObject();
        url = "https://polar-cove-19347.herokuapp.com/user";
        client = new OkHttpClient();
        return "";
    }
}
