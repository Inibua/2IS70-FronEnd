package com.example.group16.journaloo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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

public class APIWrapper extends AppCompatActivity {
    // DECLARATION VARIABLES

    private static final String TAG = MainActivity.class.getName();
    private String token;
    private static APIWrapper wrapper;
    private JSONObject obj;
    private OkHttpClient client;
    private String url;
    private Request request;

    //Object that need to be passed between the app itself
    private User loggedInUser;
    public Journey activeJourney;

    public String getToken() {
        return token;
    }

    // Only used when logging in and updating user
    public void decoded(String JWTEncoded) throws Exception {
        try {
            String[] split = JWTEncoded.split("\\.");
            Log.d("TOKEN", token);
            Log.d("JWT_DECODED", "Header: " + getJson(split[0]));
            Log.d("JWT_DECODED", "Body: " + getJson(split[1]));

            JSONObject jsonUSER = new JSONObject(getJson(split[1]));
            String id = String.valueOf(jsonUSER.get("id"));
            String username = String.valueOf(jsonUSER.get("username"));
            String email = String.valueOf(jsonUSER.get("email"));

            loggedInUser = new User(id, username, email);
            if (loggedInUser == null) {
                Log.d("LOGGED IN USER", "I DO NOT WORK");
            } else {
                Log.d("LOGGED IN USER", "I WORK");
            }

            //Goes to
        } catch (UnsupportedEncodingException e) {
            //Error
            Log.d("hoi","hoi");
        }
    }

    private static String getJson(String strEncoded) throws UnsupportedEncodingException{
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        return new String(decodedBytes, "UTF-8");
    }

    APIWrapper () {

    }

    // Singleton pattern, public
    public static APIWrapper getWrapper() {
        if (wrapper == null) {
            wrapper = new APIWrapper();
        }
        return wrapper;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }


    // DECLARATION METHODS

    /**
     * Function signup(). Accepts as parameters the current user that is being created.
     * uses getUser after successful sign-up
     *
     * @param userToBeCreated - User to be created in data base
     */
    public void signup(User userToBeCreated) { // POST
        obj = new JSONObject();
        url = "https://polar-cove-19347.herokuapp.com/user";
        client = new OkHttpClient();

        try {
            obj.put("username", userToBeCreated.userName);
            obj.put("email", userToBeCreated.email);
            obj.put("password", userToBeCreated.password);
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
                .addHeader("Postman-Token", "3a858617-5a45-42c9-826e-a313c211c012")
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
     * @param userToBeLoggedIn - User who is goind to be logged in
     */
    public void login(User userToBeLoggedIn) { // POST
        obj = new JSONObject();
        url = "https://polar-cove-19347.herokuapp.com/user/login";
        client = new OkHttpClient();

        try {
            obj.put("username", userToBeLoggedIn.userName);
            obj.put("password", userToBeLoggedIn.password);
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
                Log.i("ERRRRRROR", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    //Log.i("TOKEN NOT TOKEN", response.body().string());
                    if (response.code() == 404) {
                        throw new Exception("status 404");
                    } else {
                        token = response.body().string();
                        Log.d("TOKEN", token);
                        decoded(token);
                    }

                } catch (Exception e) {
                    Log.i("ERROR IN CATCH", "ERRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRROOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOR");
                    e.printStackTrace();
                }
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

        token = null;

        // transition to log in screen
    }

    /**
     * Gets new password from newPass field. G
     * and submits it to server to update the passed as parameter user in the backend.
     *
     * @param email - User whose password will be changed
     */
    public void resetPassword(String email) { // PUT
        obj = new JSONObject();
        url = "https://polar-cove-19347.herokuapp.com/user/"+ email +"/reset_password";
        client = new OkHttpClient();

        try {
            obj.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MediaType JSON = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(JSON,
                obj.toString());
        request = new Request.Builder()
                .url(url)
                .put(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Cache-Control", "no-cache")
                .addHeader("Postman-Token", "0705dae9-f363-4fb0-8ba2-cfbb03b5ee85")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, response.body().toString());
                if (response.code() == 202) {
                    //Display a toast that email is sending
                    Log.d("Code 202", "Request is still being processed");
                } else if (response.code() == 404) {
                    // Display toast that email is not found
                    Log.d("Code 404", "Email not found");
                } else {
                    Log.d("What happened????","This shouldn't be reached.");
                }
            }
        });
    }

    /**
     * Function used to retrieve a user from backend.
     *
     * @param userId - userId used to get User
     * @return User
     */
    public User getUser(String userId) { // GET?
        url = "https://polar-cove-19347.herokuapp.com/user/" + userId;
        client = new OkHttpClient();

        request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Content-Type", "application/json")
                .addHeader("Cache-Control", "no-cache")
                .addHeader("Postman-Token", "a2725cba-dd4f-40dd-b33b-f5e34f5c9031")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, e.getMessage());
                // Display some toast
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, response.body().toString());
                //create new user from retrieved data
            }
        });

        return new User();
    }

    /**
     * Function which updates the current users info. Like profile pic, name, description, age
     *
     * @param updatedUser - the user to be updated
     */
    public void updateUser(User updatedUser) { // PUT
        obj = new JSONObject();
        url = "https://polar-cove-19347.herokuapp.com/user";
        client = new OkHttpClient();

        try {
            obj.put("username", updatedUser.userName);
            obj.put("email", updatedUser.email);
            obj.put("password", updatedUser.password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MediaType JSON = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(JSON,
                obj.toString());
        request = new Request.Builder()
                .url(url)
                .put(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Cache-Control", "no-cache")
                .addHeader("Postman-Token", "0705dae9-f363-4fb0-8ba2-cfbb03b5ee85")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, response.body().toString());
                // Do something to front end
            }
        });
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
                .delete(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Cache-Control", "no-cache")
                .addHeader("Postman-Token", "4670edef-4cc4-8d2c-5fc9-19b09d9c3e11")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, e.getMessage());
                // Display some toast
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, response.body().toString());
            }
        });
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

        try {
            obj.put("title", journey.title);
            obj.put("userId", loggedInUser.userId);
            //obj.put("startDate", journey.startDate);
            //obj.put("endDate", journey.endDate);
            //obj.put("privacy", journey.privacy);
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
                .addHeader("Authorization", token)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, response.body().toString());
                // create new journey here, fill in data do front end stuff
            }
        });
    }

    /**
     * Function which is used to get the currently logged in users journey.
     *
     * @param journeyId - Journey's id of which journey will be returned if it is existing
     * @return currentJourney
     * @throws NoJourneyException if there is no active journey
     */
    public Journey getCurrentJourney(String journeyId) throws NoJourneyException {
        // GET? POST
        // how exactly do we get current journey?
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
    public Journey[] getUserJourneys(String userId, int pageNr){ // GET?POST
        obj = new JSONObject();
        url = "https://polar-cove-19347.herokuapp.com/journey/" + userId;
        client = new OkHttpClient();
        return new Journey[pageNr];
    }

    /**
     * Retrieves Journeys from all users.
     *
     * @param pageNr - which page number (which 10 journeys exactly)
     * @return Journey[] - array with all the journeys that excist
     */
    public Journey[] getAllJourneys(int pageNr) { // GET or post as parameters
        obj = new JSONObject();
        url = "https://polar-cove-19347.herokuapp.com/journey/all";
        client = new OkHttpClient();
        return new Journey[5];
    }

    /**
     * Updates a journey for the user
     *
     * @param journey - Journey that user updated
     * @param currentUser - logged in user whose Id we get
     * @param stopped - to detect whether the journey is being updated or just stopped
     */
    public void updateJourney(Journey journey, User currentUser, boolean stopped) { // PUT
        obj = new JSONObject();
        url = "https://polar-cove-19347.herokuapp.com/journey/" + journey.journeyId;
        client = new OkHttpClient();


        if (!stopped) {
            try {
                obj.put("id", journey.journeyId);
                obj.put("userId", currentUser.userId);
                obj.put("startDate", journey.startDate);
                obj.put("endDate", journey.endDate);
                obj.put("privacy", journey.privacy);
                obj.put("title", journey.title);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            obj = null;
        }

        MediaType JSON = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(JSON,
                obj.toString());
        request = new Request.Builder()
                .url(url)
                .put(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Cache-Control", "no-cache")
                .addHeader("Postman-Token", "0705dae9-f363-4fb0-8ba2-cfbb03b5ee85")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, response.body().toString());
                // Do something to front end
            }
        });
    }

    /**
     * Deletes a journey for the user
     *
     * @param journey - Journey that user deleted
     */
    public void deleteJourney(Journey journey, User currentUser) { //DELETE
        obj = new JSONObject();
        url = "https://polar-cove-19347.herokuapp.com/journey/" + journey.journeyId;
        client = new OkHttpClient();

        try {
            obj.put("id", journey.journeyId);
            obj.put("userId", currentUser.userId);
            obj.put("startDate", journey.startDate);
            obj.put("endDate", journey.endDate);
            obj.put("privacy", journey.privacy);
            obj.put("title", journey.title);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MediaType JSON = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(JSON,
                obj.toString());
        request = new Request.Builder()
                .url(url)
                .delete(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Cache-Control", "no-cache")
                .addHeader("Postman-Token", "4670edef-4cc4-8d2c-5fc9-19b09d9c3e11")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, e.getMessage());
                //Display some Toast
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, response.body().toString());
            }
        });
    }

    /**
     * Retrieves a specific entry from the user
     *
     * @param userId - User's Id whose journeys are retrieved
     * @param entry - Entry user wants to get
     * @return entry
     */
    public Entry getEntry(String userId, Entry entry) { // GET?POST
        obj = new JSONObject();
        url = "https://polar-cove-19347.herokuapp.com/entry/" + entry.entryId;
        client = new OkHttpClient();

        request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Content-Type", "application/json")
                .addHeader("Cache-Control", "no-cache")
                .addHeader("Postman-Token", "890eefa8-9be1-49dc-bf65-8699b2f7b517")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, response.body().toString());
                // Do something to front end
            }
        });

        return new Entry();
    }

    /**
     * Retrieves all the entries that belong to a certain journey
     *
     * @param userId - User's Id whose entries are retrieved
     * @param journey - Journey from which the entries are retrieved
     * @return Entry[] - array with all entries of that journey in it
     */
    public Entry[] getJourneyEntries(String userId, Journey journey) { // GET?POST
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
    public void createEntry(Entry entry) { // POST
        obj = new JSONObject();
        url = "https://polar-cove-19347.herokuapp.com/entry/" + activeJourney.journeyId;
        client = new OkHttpClient();

        try {
            obj.put("journeyId", activeJourney.journeyId);
            obj.put("description", entry.description);
            obj.put("location", entry.location);
            //obj.put("coordinates", entry.coordinates);
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
                // create new entry here, fill in data do front end stuff
            }
        });
    }

    /**
     * Updates an entry for the user
     * @param entry - Entry user wants to update
     */
    public void updateEntry(Entry entry, Journey journey) { // PUT
        obj = new JSONObject();
        url = "https://polar-cove-19347.herokuapp.com/entry/" + entry.entryId;
        client = new OkHttpClient();

        try {
            obj.put("journeyId", journey.journeyId);
            obj.put("description", entry.description);
            obj.put("location", entry.location);
            obj.put("coordinates", entry.coordinates);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MediaType JSON = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(JSON,
                obj.toString());
        request = new Request.Builder()
                .url(url)
                .put(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Cache-Control", "no-cache")
                .addHeader("Postman-Token", "0705dae9-f363-4fb0-8ba2-cfbb03b5ee85")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, response.body().toString());
                // Do something to front end
            }
        });
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

        try {
            obj.put("entryId", entry.entryId);
            // other necessary params
        } catch (JSONException e) {
            e.printStackTrace();
        }

        MediaType JSON = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(JSON,
                obj.toString());
        request = new Request.Builder()
                .url(url)
                .delete(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Cache-Control", "no-cache")
                .addHeader("Postman-Token", "4670edef-4cc4-8d2c-5fc9-19b09d9c3e11")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, e.getMessage());
                //Display some Toast
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, response.body().toString());
            }
        });
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
