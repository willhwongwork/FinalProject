package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.util.Pair;

import com.bbr.jokes.backend.myApi.MyApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;

import bbr.androidjokes.JokeActivity;

class EndpointsAsyncTask extends AsyncTask<Pair<Context, String>, Void, Pair<Context, String>> {
    private static MyApi myApiService = null;
    private Context context;

    @Override
    protected Pair<Context, String> doInBackground(Pair<Context, String>... params) {
        if(myApiService == null) {  // Only do this once
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // options for running against local devappserver
                    // - 10.0.2.2 is localhost's IP address in Android emulator
                    // - turn off compression when running against local devappserver
                    .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            // end options for devappserver

            myApiService = builder.build();
        }

        context = params[0].first;
        String jokename = params[0].second;

        try {
            String joke = myApiService.getJoke(jokename).execute().getData();
            Pair p = new Pair<>(context, joke);
            return p;

        } catch (IOException e) {
            return new Pair<>(context,e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(Pair<Context, String> result) {
        Context context = result.first;
        String joke = result.second;
        Intent intent = new Intent(context, JokeActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, joke);
        context.startActivity(intent);
    }
}