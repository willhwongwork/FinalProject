package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.support.v4.util.Pair;
import android.test.AndroidTestCase;
import org.mockito.Mock;

import java.util.concurrent.TimeUnit;


public class EndpointsAsyncTaskTest extends AndroidTestCase {
    EndpointsAsyncTask task;
    Pair result;
    @Mock
    Context mockContext;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        result = null;
        task = new EndpointsAsyncTask(){
            @Override
            protected void onPostExecute(Pair<Context, String> result){
                //No need to launch intent, so override this method
            }
        };
    }

    public void testAsyncReturnType() {

        try{
            Pair pair = new Pair<>(mockContext, "joke01");
            task.execute(pair);
            result = task.get(10, TimeUnit.SECONDS);
            assertNotNull(result.second);

        }catch (Exception e){
            fail("Timed out");
        }
    }
}