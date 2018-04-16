package com.example.group16.journaloo.api;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.example.group16.journaloo.model.Entry;
import com.example.group16.journaloo.model.Journey;
import com.example.group16.journaloo.model.User;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
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
    private static final String TAG = APIWrapper.class.getName();
    private static final MediaType JSON = MediaType.parse("application/json");
    private static final HttpUrl baseUrl = HttpUrl.parse("https://polar-cove-19347.herokuapp.com");
    private static final Gson gson = new Gson();
    private final OkHttpClient client;

    private static APIWrapper wrapper;

    // TODO: Use proper callbacks instead of instance variables for data passing
    private String token;
    private User loggedInUser;
    private Journey activeJourney;
    private User userFromGetUser;
    private Bitmap bitmap;
    private Journey[] array; // ARRAY OF JOURNEYS. IS TO BE USED FOR PAST JOURNEYS OR EXPLORE JOURNEYS


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

    public String getToken() {
        return token;
    }

    // Only used when logging in and updating user
    private void decode(String JWTEncoded) throws JSONException {
        try {
            String[] split = JWTEncoded.split("\\.");
            Log.d("TOKEN", token);
            Log.d("JWT_DECODED", "Header: " + fromBase64(split[0]));
            Log.d("JWT_DECODED", "Body: " + fromBase64(split[1]));

            loggedInUser = gson.fromJson(fromBase64(split[1]), User.class);
            //Goes to
        } catch (UnsupportedEncodingException e) {
            //Error
            Log.d("hoi", "hoi");
        }
    }

    private static String fromBase64(String strEncoded) throws UnsupportedEncodingException {
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
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    token = response.body().string();
                    try {
                        decode(token);
                    } catch (JSONException e) {
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
     * Function logout() just logouts the currently logged in user.
     */
    public void logout() {
        token = null;

        // transition to log in screen
    }

    /**
     * Gets new password from newPass field.
     * and submits it to server to update the passed as parameter user in the backend.
     */
    public void resetPassword(String email) { // PUT
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
     * @param userId - user_id used to get User
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

        JSONObject obj = new JSONObject();

        try {
            obj.put("username", currentUser.username);
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
    public void refreshActiveJourney(final MainThreadCallback responseHandler) {
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
    synchronized public void getUserJourneys(int pageNr) {
        Log.d("I ENTERED", "1");
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegment("journey")
                .addPathSegment("user")
                .addPathSegment(String.valueOf(loggedInUser.id))
                .addQueryParameter("page", String.valueOf(pageNr))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Log.d("I ENTERED", "2");
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("I ENTERED", "4");
                Log.i(TAG, e.getMessage());
                // Display some toast
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("I ENTERED", "3");
                int code = response.code();
                if (code == 200){
                    String userJourneysString = response.body().string();
                    Log.d(TAG, userJourneysString);

                    try {
                        Log.d("I ENTERED", "5");
                        JSONArray userJourneysJson = new JSONArray(userJourneysString);
                        Log.d("I ENTERED", "6");
                        convertJSONArrayToNormalArray(userJourneysJson); // CONVERTS JSON ARRAY TO NORMAL ARRAY
                        // TODO: make json converter method from JSONObject to Journey and use it
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d("CODE", "DIFFERENT THAN 200");
                    Log.d("CODE", String.valueOf(code));
                }
            }
        });
    }

    private void convertJSONArrayToNormalArray(JSONArray jsonArrayJouneys) throws JSONException {
        Log.d("I ENTERED", "7");
        array = new Journey[jsonArrayJouneys.length()];
        for (int i = 0; i <= jsonArrayJouneys.length() - 1; i++) {
            JSONObject singleJouneyJSON = jsonArrayJouneys.getJSONObject(i);
            int id = (Integer) singleJouneyJSON.get("id");
            int user_id = (Integer) singleJouneyJSON.get("user_id");
            String title = String.valueOf(singleJouneyJSON.get("title"));
            Journey singleJourney = new Journey(id, user_id, title);

            array[i] = singleJourney;
            Log.d("ARRAY JOURNEYS SINGLE", array[i].title);
        }
    }

    public Journey[] getArrayWithJourneys() {
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return array;
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
                .addPathSegment(String.valueOf(journey.id))
                .build();

        JSONObject obj = new JSONObject();

        try {
            obj.put("id", journey.id);
            obj.put("user_id", loggedInUser.id);
            obj.put("start_date", journey.start_date);
            obj.put("end_date", journey.end_date);
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
     * @param entry - Entry user wants to get
     * @return entry
     */
    public Entry getEntry(Entry entry) { // GET?POST
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegment("entry")
                .addPathSegment(String.valueOf(entry.id))
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
     *
     * @param entry_id - Entry's id user wants to update
     * @param entryDescription - Entry's description user wants to update
     */
    public void updateEntry(int entry_id, String entryDescription) { // PUT
        HttpUrl url = baseUrl.newBuilder()
                .addPathSegment("entry")
                .addPathSegment(String.valueOf(entry_id))
                .build();

        JSONObject obj = new JSONObject();

        try {
            obj.put("id", entry_id);
            obj.put("description", entryDescription);
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
                .addPathSegment(String.valueOf(entry.id))
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

    public void getImage(int entryNoImageId) {

        Request request = new Request.Builder()
                .url("https://polar-cove-19347.herokuapp.com/entry/" + entryNoImageId + "/image")
                .get()
                .addHeader("Cache-Control", "no-cache")
                .addHeader("Postman-Token", "feeaba26-2966-4aa9-96f6-9d85c5648f8d")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int code = response.code();
                if (code == 200) {
                    Log.d("GET PICTURE", "OKAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAy");
                    Bitmap bmpGET = BitmapFactory.decodeStream(response.body().byteStream());
                    setImageCurrentEntryBitmap(bmpGET);
                } else {
                    Log.d("GET PICTURE", "BROOOOOOOOOOOOOOOOOOOOOOOOOOOOOOKKEEEEEEEEEEEEEEEEEEEEEN");
                }
                // create new entry here, fill in data do front end stuff
            }
        });
    }

    public void setImageCurrentEntryBitmap(Bitmap imageCurrentEntryBitmap) {
        bitmap = imageCurrentEntryBitmap;

    }

    public Bitmap getImageCurrentEntryBitmap() {
        return bitmap;
    }
}
