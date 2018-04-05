package com.example.group16.journaloo;

import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by s169096 on 14-3-2018.
 */

public class APIWrapper {
    // DECLARATION VARIABLES
    private static final String TAG = MainActivity.class.getName();
    private static final MediaType JSON = MediaType.parse("application/json");
    private static final HttpUrl baseUrl = HttpUrl.parse("https://polar-cove-19347.herokuapp.com");
    private final OkHttpClient client;

    private static APIWrapper wrapper;

    // TODO: Use proper callbacks instead of instance variables for data passing
    private String token;
    private User loggedInUser;
    private Journey activeJourney;
    private User userFromGetUser;


    private APIWrapper() {
        this.client = new OkHttpClient();
    }

    // Singleton pattern, public
    public synchronized static APIWrapper getWrapper() {
        if (wrapper == null) {
            wrapper = new APIWrapper();
        }
        return wrapper;
    }

    public String getToken() {
        return token;
    }

    // Only used when logging in and updating user
    private void decoded(String JWTEncoded) throws Exception {
        try {
            String[] split = JWTEncoded.split("\\.");
            Log.d("TOKEN", token);
            Log.d("JWT_DECODED", "Header: " + getJson(split[0]));
            Log.d("JWT_DECODED", "Body: " + getJson(split[1]));

            JSONObject jsonUSER = new JSONObject(getJson(split[1]));
            String idString = String.valueOf(jsonUSER.get("id"));
            String username = String.valueOf(jsonUSER.get("username"));
            String email = String.valueOf(jsonUSER.get("email"));
            int id = Integer.valueOf(idString);

            loggedInUser = new User(id, username, email);
            //Goes to
        } catch (UnsupportedEncodingException e) {
            //Error
            Log.d("hoi", "hoi");
        }
    }

    private static String getJson(String strEncoded) throws UnsupportedEncodingException {
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        return new String(decodedBytes, "UTF-8");
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
    public synchronized void signup(User userToBeCreated) { // POST
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegment("user")
                .build();

        JSONObject obj = new JSONObject();

        try {
            obj.put("username", userToBeCreated.userName);
            obj.put("email", userToBeCreated.email);
            obj.put("password", userToBeCreated.password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(JSON, obj.toString());
        Log.i("BODY", obj.toString());

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) {
                Log.i("WHAT RESPONSE", response.body().toString());
            }
        });
    }

    /**
     * Function login(). Accepts the current user from fields in login activity.
     * uses getUser() function to retrieve from backend.
     *
     * @param userToBeLoggedIn - User who is goind to be logged in
     */
    public synchronized void login(User userToBeLoggedIn) { // POST
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegment("user")
                .addPathSegment("login")
                .build();

        JSONObject obj = new JSONObject();

        try {
            obj.put("username", userToBeLoggedIn.userName);
            obj.put("password", userToBeLoggedIn.password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(JSON, obj.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("ERRRRRROR", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) {
                try {
                    //Log.i("TOKEN NOT TOKEN", response.body().string());
                    int statusCode = response.code();
                    if (statusCode == 404 || statusCode == 401) {
                        loggedInUser = null;
                    } else {
                        token = response.body().string();
                        //Log.d("TOKEN", token);
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
     */
    public synchronized void logout() {
        token = null;

        // transition to log in screen
    }

    /**
     * Gets new password from newPass field. G
     * and submits it to server to update the passed as parameter user in the backend.
     */
    public synchronized void resetPassword(String email) { // PUT
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegment("user")
                .addPathSegment(email)
                .addPathSegment("reset")
                .build();

        RequestBody body = RequestBody.create(null, "");
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) {
                Log.i(TAG, response.body().toString());
                if (response.code() == 202) {
                    //Display a toast that email is sending
                    Log.d("Code 202", "Request received successfully; now processing");
                } else if (response.code() == 404) {
                    // Display toast that email is not found
                    Log.d("Code 404", "Email not found");
                } else {
                    Log.d("What happened????", "This shouldn't be reached.");
                }
            }
        });
    }

    /**
     * Function used to retrieve a user from backend.
     *
     * @param userId - userId used to get User
     */
    public void getUser(int userId) { // GET
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegment("user")
                .addPathSegment(String.valueOf(userId))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, e.getMessage());
                // Display some toast
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String userJsonString = response.body().string();
                try {
                    JSONObject jsonGetUser = new JSONObject(userJsonString);
                    setUserFromGetUserMethod(jsonGetUser);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i(TAG, userJsonString);
                //create new user from retrieved data
            }
        });
    }

    private void setUserFromGetUserMethod(JSONObject jsonGetUser) throws JSONException {
        String idString = String.valueOf(jsonGetUser.get("id"));
        String username = String.valueOf(jsonGetUser.get("username"));
        String email = String.valueOf(jsonGetUser.get("email"));
        int id = Integer.valueOf(idString);
        userFromGetUser = null;
        userFromGetUser = new User(id, username, email);
    }

    public User getUserFromGetUserMethod() {
        return userFromGetUser;
    }

    /**
     * Function which updates the current users info.
     * Updates username, email and password.
     *
     * @param updatedUser - the user to be updated
     */
    public void updateUser(User updatedUser) { // PUT
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegment("user")
                .build();

        JSONObject obj = new JSONObject();

        try {
            obj.put("username", updatedUser.userName);
            obj.put("email", updatedUser.email);
            obj.put("password", updatedUser.password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(JSON, obj.toString());
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", token)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) {
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
    public synchronized void deleteUser(User currentUser) { // DELETE
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegment("user")
                .build();

        JSONObject obj = new JSONObject();

        try {
            obj.put("username", currentUser.userName);
            obj.put("email", currentUser.email);
            obj.put("password", currentUser.password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(JSON, obj.toString());
        Request request = new Request.Builder()
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
            public void onResponse(Call call, Response response) {
                Log.i(TAG, response.body().toString());
            }
        });
    }

    /**
     * Creates a journey for the user
     *
     * @param journey - Journey that user created
     */
    public synchronized void createJourney(Journey journey) { // POST
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegment("journey")
                .build();

        JSONObject obj = new JSONObject();

        try {
            obj.put("user_id", loggedInUser.userId);
            obj.put("title", journey.title);
            //obj.put("startDate", journey.startDate);
            //obj.put("endDate", journey.endDate);
            //obj.put("privacy", journey.privacy);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(JSON, obj.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", token)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) {
                Log.i(TAG, response.body().toString());
                // create new journey here, fill in data do front end stuff
            }
        });
    }

    /**
     * Function which is used to get the currently logged in users journey.
     */
    public synchronized void getCurrentJourneyRequest() {
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegment("journey")
                .addPathSegment(String.valueOf(loggedInUser.userId))
                .addPathSegment("active")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Authorization", token)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, e.getMessage());
                // Display some toast
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String currentJourneyJsonString = response.body().string();
                Log.i(TAG, currentJourneyJsonString);
                try {
                    JSONObject jsonGetCurrentJourney = new JSONObject(currentJourneyJsonString);
                    setCurrentJourney(jsonGetCurrentJourney);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setCurrentJourney(JSONObject jsonJourney) throws JSONException {
        String idString = String.valueOf(jsonJourney.get("id"));
        String userIdString = String.valueOf(jsonJourney.get("user_id"));
        String title = String.valueOf(jsonJourney.get("title"));
        int id = Integer.valueOf(idString);
        int user_id = Integer.valueOf(userIdString);
        activeJourney = new Journey(id, user_id, title);
        Log.i("SET ACTIVE JOURNEY", activeJourney.title);
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Journey getCurrentJourney() { // MAYBE CALL REQUEST HERE SO THAT IT IS EASIER AT FRONT END
        //Log.i("ACTIVE JOURNEY API", activeJourney.title);
        return activeJourney;
    }

    /**
     * Gets an array of the current user's journeys, aka his past journeys.
     *
     * @param userId - User's Id whose journeys are retrieved
     * @param pageNr - which page number (which 10 journeys exactly)
     * @return Journey[] - array with 10 journeys depending on the pageNr
     */
    public Journey[] getUserJourneys(int userId, int pageNr) {
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegment("journey")
                .addPathSegment("all")
                .addQueryParameter("page", String.valueOf(pageNr))
                .addQueryParameter("user_id", String.valueOf(userId))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, e.getMessage());
                // Display some toast
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String userJourneysString = response.body().string();
                Log.i(TAG, userJourneysString);

                try {
                    JSONArray userJourneysJson = new JSONArray(userJourneysString);
                    ArrayList<Journey> userJourneys = new ArrayList<>();
                    // TODO: make json converter method from JSONObject to Journey and use it
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return new Journey[pageNr];
    }

    /**
     * Retrieves Journeys from all users.
     *
     * @param pageNr - which page number (which 10 journeys exactly)
     * @return Journey[] - array with all the journeys that excist
     */
    public Journey[] getAllJourneys(int pageNr) { // GET or post as parameters
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegment("journey")
                .addPathSegment("all")
                .addQueryParameter("page", String.valueOf(pageNr))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, e.getMessage());
                // Display some toast
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String userJourneysString = response.body().string();
                Log.i(TAG, userJourneysString);

                try {
                    JSONArray allJourneysJson = new JSONArray(userJourneysString);
                    ArrayList<Journey> allJourneys = new ArrayList<>();
                    // TODO: make json converter method from JSONObject to Journey and use it
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return new Journey[5];
    }

    /**
     * Updates a journey for the user
     *
     * @param journey - Journey that user updated
     */
    public void updateJourney(Journey journey) { // PUT
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegment("journey")
                .addPathSegment(String.valueOf(journey.journeyId))
                .build();

        JSONObject obj = new JSONObject();

        try {
            obj.put("id", journey.journeyId);
            obj.put("userId", loggedInUser.userId);
            obj.put("startDate", journey.startDate);
            obj.put("endDate", journey.endDate);
//            obj.put("privacy", journey.privacy);
            obj.put("title", journey.title);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(JSON, obj.toString());
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) {
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
    public void deleteJourney(Journey journey) { //DELETE
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegment("journey")
                .addPathSegment(String.valueOf(journey.journeyId))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, e.getMessage());
                //Display some Toast
            }

            @Override
            public void onResponse(Call call, Response response) {
                Log.i(TAG, response.body().toString());
            }
        });
    }

    /**
     * Retrieves a specific entry from the user
     *
     * @param entry - Entry user wants to get
     * @return entry
     */
    public Entry getEntry(Entry entry) { // GET?POST
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegment("entry")
                .addPathSegment(String.valueOf(entry.entryId))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) {
                Log.i(TAG, response.body().toString());
                // Do something to front end
            }
        });

        return new Entry();
    }

    /**
     * Retrieves all the entries that belong to a certain journey
     *
     * @param journeyId - Journey from which the entries are retrieved
     * @param pageNr    - page to retrieve journeys of
     * @return Entry[] - array with all entries of that journey in it
     */
    public Entry[] getJourneyEntries(int journeyId, int pageNr) { // GET?POST
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegment("entry")
                .addPathSegment("all")
                .addQueryParameter("page", String.valueOf(pageNr))
                .addQueryParameter("journey", String.valueOf(journeyId))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, e.getMessage());
                // Display some toast
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String journeyEntriesString = response.body().string();
                Log.i(TAG, journeyEntriesString);

                try {
                    JSONArray journeyEntriesJson = new JSONArray(journeyEntriesString);
                    ArrayList<Entry> journeyEntries = new ArrayList<>();
                    // TODO: make json converter method from JSONObject to Entry and use it
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return new Entry[5];
    }

    /**
     * Retrieves all the entries
     *
     * @param pageNr - which page number (which 10 entries exactly)
     * @return Entry[] - array with all the entries of that person
     */
    public Entry[] getAllEntries(int pageNr) { // GET What should this do
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegment("entry")
                .addPathSegment("all")
                .addQueryParameter("page", String.valueOf(pageNr))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, e.getMessage());
                // Display some toast
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String journeyEntriesString = response.body().string();
                Log.i(TAG, journeyEntriesString);

                try {
                    JSONArray journeyEntriesJson = new JSONArray(journeyEntriesString);
                    ArrayList<Entry> journeyEntries = new ArrayList<>();
                    // TODO: make json converter method from JSONObject to Entry and use it
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return new Entry[5];
    }

    /**
     * Creates an entry for the user
     *
     * @param entry - Entry user wants to create
     */
    public synchronized void createEntry(Entry entry) { // POST
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegment("entry")
                .build();

        JSONObject obj = new JSONObject();

        try {
            obj.put("journey_id", activeJourney.journeyId);
            obj.put("description", entry.description);
            //obj.put("location", entry.location);
            //obj.put("coordinates", entry.coordinates);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(JSON, obj.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", token)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) {
                Log.i(TAG, response.body().toString());
                // create new entry here, fill in data do front end stuff
            }
        });
    }

    /**
     * Updates an entry for the user
     *
     * @param entry - Entry user wants to update
     */
    public void updateEntry(Entry entry) { // PUT
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegment("entry")
                .addPathSegment(String.valueOf(entry.entryId))
                .build();

        JSONObject obj = new JSONObject();

        try {
            obj.put("journey_id", entry.journeyId);
            obj.put("description", entry.description);
            obj.put("location", entry.location);
            obj.put("coordinates", entry.coordinates);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(JSON, obj.toString());
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", token)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) {
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
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegment("entry")
                .addPathSegment(String.valueOf(entry.entryId))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, e.getMessage());
                //Display some Toast
            }

            @Override
            public void onResponse(Call call, Response response) {
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
        // TODO: query google places api
        return "";
    }
}
