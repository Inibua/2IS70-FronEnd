package com.example.group16.journaloo.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;
import com.example.group16.journaloo.R;
import com.example.group16.journaloo.models.Entry;
import com.example.group16.journaloo.models.Journey;
import com.example.group16.journaloo.models.User;
import com.google.gson.Gson;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by s169096 on 14-3-2018.
 */

public class APIWrapper {
    public static final HttpUrl baseUrl = HttpUrl.parse("https://polar-cove-19347.herokuapp.com");

    // DECLARATION VARIABLES
    private static final String TAG = APIWrapper.class.getName();
    private static final MediaType JSON = MediaType.parse("application/json");

    private static final Gson gson = new Gson();
    private static APIWrapper wrapper;
    private final OkHttpClient client;
    // TODO: Use proper callbacks instead of instance variables for data passing
    private String token;
    private User loggedInUser;
    private Journey activeJourney;
    private Bitmap bitmap;


    private APIWrapper() {
        this.client = new OkHttpClient();
    }

    // Singleton pattern, public
    public static APIWrapper getWrapper() {
        if (wrapper == null) {
            wrapper = new APIWrapper();
        }
        return wrapper;
    }

    private static String fromBase64(String strEncoded) throws UnsupportedEncodingException {
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        return new String(decodedBytes, "UTF-8");
    }

    public String getToken() {
        return token;
    }

    // Only used when logging in and updating user
    public void decodeAndStore(String JWTEncoded) throws UnsupportedEncodingException {
        token = JWTEncoded;
        String[] split = JWTEncoded.split("\\.");
        loggedInUser = gson.fromJson(fromBase64(split[1]), User.class);
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
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegment("user")
                .build();

        JSONObject obj = new JSONObject();

        try {
            obj.put("username", userToBeCreated.username);
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
     * @param user - User who is going to be logged in
     */
    public void login(User user, final MainThreadCallback responseHandler) { // POST
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegment("user")
                .addPathSegment("login")
                .build();

        JSONObject obj = new JSONObject();

        try {
            obj.put("username", user.username);
            obj.put("password", user.password);
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
                responseHandler.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    try {
                        decodeAndStore(response.body().string());
                    } catch (Exception e) {
                        onFailure(call, new IOException(e));
                        return;
                    }
                    responseHandler.onSuccess(token);
                } else {
                    responseHandler.onFailure(call, new IOException("Login unsuccessful"));
                }
            }
        });
    }

    /**
     * Function logout(). Discards auth token
     *
     */
    public void logout(Context context) {
        SharedPreferences sharedPref = context.getSharedPreferences("credentials", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(context.getString(R.string.journaloo_auth));
        editor.apply();
    }

    /**
     * Function used to retrieve a user from backend.
     *
     * @param userId - user_id used to get User
     */
    public void getUser(int userId, MainThreadCallback responseHandler) { // GET
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegment("user")
                .addPathSegment(String.valueOf(userId))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(responseHandler);
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
            obj.put("username", updatedUser.username);
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
    public void deleteUser(User currentUser) { // DELETE
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegment("user")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .delete()
                .addHeader("Authorization", token)
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
     * Function which is used to get the currently logged in users journey.
     */
    public void requestPasswordResetMail(final String email, final MainThreadCallback responseHandler) {
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegment("user")
                .addPathSegment(String.valueOf(email))
                .addPathSegment("reset")
                .build();

        JSONObject obj = new JSONObject();

        try {
            obj.put("email", email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(JSON, obj.toString());
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .build();

        client.newCall(request).enqueue(responseHandler);
    }

    /**
     * Creates a journey for the user
     *
     * @param journey - Journey that user created
     */
    public void createJourney(Journey.NewJourney journey, MainThreadCallback responseHandler) { // POST
        assert activeJourney == null;

        HttpUrl url = baseUrl.newBuilder()
                .addPathSegment("journey")
                .build();

        RequestBody body = RequestBody.create(JSON, gson.toJson(journey));
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", token)
                .build();

        client.newCall(request).enqueue(responseHandler);
    }

    /**
     * Function which is used to get the currently logged in users journey.
     */
    public void getActiveJourney(final MainThreadCallback responseHandler) {
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegment("journey")
                .addPathSegment(String.valueOf(loggedInUser.id))
                .addPathSegment("active")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Authorization", token)
                .build();

        client.newCall(request).enqueue(responseHandler);
    }

    public Journey getActiveJourney() {
        return activeJourney;
    }

    public void setActiveJourney(Journey journey) {
        activeJourney = journey;
    }

    /**
     * Gets an array of the current user's journeys, aka his past journeys.
     *
     * @param pageNr - which page number (which 10 journeys exactly)
     * @return array with past journeys of given user (userId) and pageNr
     */
    synchronized public void getUserJourneys(int userId, int pageNr, MainThreadCallback responseHandler) {
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegment("journey")
                .addPathSegment("user")
                .addPathSegment(String.valueOf(userId))
                .addQueryParameter("page", String.valueOf(pageNr))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(responseHandler);
    }

    /**
     * Retrieves Journeys from all users.
     *
     * @param pageNr - which page number (which 10 journeys exactly)
     */
    public void getAllJourneys(int pageNr, MainThreadCallback responseHandler) { // GET or post as parameters
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegment("journey")
                .addPathSegment("all")
                .addQueryParameter("page", String.valueOf(pageNr))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(responseHandler);
    }

    /**
     * Updates a journey for the user
     *
     * @param journey - Journey that user updated
     */
    public void updateJourney(Journey journey) { // PUT
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegment("journey")
                .addPathSegment(String.valueOf(journey.id))
                .build();

        JSONObject obj = new JSONObject();

        try {
            obj.put("id", journey.id);
            obj.put("user_id", loggedInUser.id);
            obj.put("start_date", journey.start_date);
            obj.put("end_date", journey.end_date);
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
     * Ends a journey
     *
     * @param journey the journey to end
     */
    public void endJourney(Journey journey, final MainThreadCallback responseHandler) {
        OkHttpClient client = new OkHttpClient();
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegment("journey")
                .addPathSegment(String.valueOf(journey.id))
                .addPathSegment("end")
                .build();

        MediaType empty = MediaType.parse("");
        RequestBody body = RequestBody.create(empty, "");
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .addHeader("Authorization", token)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                responseHandler.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    activeJourney = null;
                    responseHandler.onResponse(call, response);
                } else {
                    onFailure(call, new IOException("Failed"));
                }
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
                .addPathSegment(String.valueOf(journey.id))
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
     * @param entryID - EntryID user wants to get
     * @return entry
     */
    public void getEntry(int entryID, MainThreadCallback responseHandler) { // GET?POST
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegment("entry")
                .addPathSegment(String.valueOf(entryID))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(responseHandler);
    }

    /**
     * Retrieves all the entries that belong to a certain journey
     *
     * @param journeyId - Journey from which the entries are retrieved
     * @param pageNr    - page to retrieve journeys of
     */
    public void getJourneyEntries(int journeyId, int pageNr, MainThreadCallback responseHandler) { // GET?POST
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

        client.newCall(request).enqueue(responseHandler);
    }

    /**
     * Retrieves all the entries
     *
     * @param pageNr - which page number (which 10 entries exactly)
     */
    public void getAllEntries(int pageNr, MainThreadCallback responseHandler) { // GET What should this do
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegment("entry")
                .addPathSegment("all")
                .addQueryParameter("page", String.valueOf(pageNr))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(responseHandler);
    }

    /**
     * Creates an entry for the user
     *
     * @param entry - Entry user wants to create
     */
    public void createEntry(Entry.NewEntry entry, final String filename, final Context cxt, final MainThreadCallback responseHandler) { // POST
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegment("entry")
                .build();

        Log.i(TAG, "pre POST req");
        entry.journey_id = activeJourney.id;
        RequestBody body = RequestBody.create(JSON, gson.toJson(entry));
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", token)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                responseHandler.onFailure(call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    onFailure(call, new IOException("Failed"));
                    return;
                }

                Log.i(TAG, "received response");

                Entry entry = gson.fromJson(response.body().string(), Entry.class);

                HttpUrl url = baseUrl.newBuilder()
                        .addPathSegment("entry")
                        .addPathSegment(String.valueOf(entry.id))
                        .addPathSegment("image")
                        .build();

                FileInputStream is = cxt.openFileInput(filename);
                byte[] bytes = new byte[is.available()];
                is.read(bytes);
                MediaType PNG = MediaType.parse("image/png");
                RequestBody body = RequestBody.create(PNG, bytes);
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .addHeader("Content-Type", "image/png")
                        .addHeader("Authorization", token)
                        .build();

                client.newCall(request).enqueue(responseHandler);
            }
        });
    }

    /**
     * Updates an entry for the user
     */
    public void updateEntry(Entry entry, MainThreadCallback responseHandler) { // PUT
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegment("entry")
                .addPathSegment(String.valueOf(entry.id))
                .build();

        RequestBody body = RequestBody.create(JSON, gson.toJson(entry));
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", token)
                .build();

        client.newCall(request).enqueue(responseHandler);
    }

    /**
     * Deletes an entry for the user
     */
    public void deleteEntry(int entryId, MainThreadCallback responseHandler) { //DELETE
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegment("entry")
                .addPathSegment(String.valueOf(entryId))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .delete()
                .build();

        client.newCall(request).enqueue(responseHandler);
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

    public Bitmap getImageCurrentEntryBitmap() {
        return bitmap;
    }

    public void setImageCurrentEntryBitmap(Bitmap imageCurrentEntryBitmap) {
        bitmap = imageCurrentEntryBitmap;
    }

    public void getImage(int entry_id) {

    }

    public void getJourney(int journeyId, MainThreadCallback responseHandler) {
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegment("journey")
                .addPathSegment(String.valueOf(journeyId))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(responseHandler);
    }
}
