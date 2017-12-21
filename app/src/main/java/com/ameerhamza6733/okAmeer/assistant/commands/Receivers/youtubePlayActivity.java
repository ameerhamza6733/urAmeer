package com.ameerhamza6733.okAmeer.assistant.commands.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.ameerhamza6733.okAmeer.R;
import com.ameerhamza6733.okAmeer.UI.fragment.voiceRecgonizationFragment;
import com.ameerhamza6733.okAmeer.interfacess.noNeedCommander;
import com.ameerhamza6733.okAmeer.interfacess.onGoogleSpeechRecognzerError;
import com.ameerhamza6733.okAmeer.utial.TTSService;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.youtube.player.YouTubePlayer;
import com.thefinestartist.ytpa.YouTubePlayerActivity;
import com.thefinestartist.ytpa.enums.Orientation;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by AmeerHamza on 7/5/2017.
 */

public class youtubePlayActivity extends AppCompatActivity implements noNeedCommander , onGoogleSpeechRecognzerError {
    private BroadcastReceiver broadcastReceiver;
   
    private RequestQueue requestQueue;
    private Runnable runnable;
    private Handler handler = new Handler();
    private voiceRecgonizationFragment newIntance;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_main);
        Intent i = new Intent(youtubePlayActivity.this, TTSService.class);
        i.putExtra("toSpeak", "आप कौन सा गाना सुनना चाहते हैं");
        i.putExtra("Language", "hi");
        this.startService(i);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String s = intent.getStringExtra("com.service.message");
                if(!youtubePlayActivity.this.getResources().getString(R.string.Aap_Ka__Gaana_Lagaaya_Ja_Raha_Ha).equals(s))
                showVoiceRegoniztionFragment();
            }
        };
        requestQueue = Volley.newRequestQueue(this);


    }

    private void showVoiceRegoniztionFragment() {

        
        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    if(!isFinishing()){
                        FragmentTransaction transactionFragment = getSupportFragmentManager().beginTransaction();
                         newIntance = voiceRecgonizationFragment.newInstance("en-IN", false, false);
                        newIntance.setStyle(1, R.style.AppTheme);
                        transactionFragment.add(android.R.id.content, newIntance).addToBackStack(null).commitAllowingStateLoss();


                        //  newIntance.show(fragmentManager, "fragment_voice_input");

                    }


                }catch (Exception e){
                    Toast.makeText(youtubePlayActivity.this, "error"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        };
        handler.postDelayed(runnable, 10);

    }

    private void initVolley(String url) {


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray = response.getJSONArray("items");
                    if (jsonArray.length() > 0) {


                        JSONObject jsonObject = jsonArray.getJSONObject(0).getJSONObject("id");
                        String videoID = jsonObject.getString("videoId");
                        Log.d("youutbe videos", "ID" + videoID);
                        Intent intent = new Intent(youtubePlayActivity.this, YouTubePlayerActivity.class);
                        intent.putExtra(YouTubePlayerActivity.EXTRA_VIDEO_ID, videoID);

                        intent.putExtra(YouTubePlayerActivity.EXTRA_PLAYER_STYLE, YouTubePlayer.PlayerStyle.DEFAULT);

                        intent.putExtra(YouTubePlayerActivity.EXTRA_ORIENTATION, Orientation.AUTO);

                        intent.putExtra(YouTubePlayerActivity.EXTRA_SHOW_AUDIO_UI, false);

                        intent.putExtra(YouTubePlayerActivity.EXTRA_HANDLE_ERROR, true);

                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });
        requestQueue.add(jsonObjectRequest);


    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver((broadcastReceiver),
                new IntentFilter("com.service.result"));
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        if(handler!=null)
        handler.removeCallbacks(runnable);
        super.onStop();
    }

    @Override
    public void onNoCommandrExcute(String Queary) {
        if (!Queary.isEmpty()) {
            Queary = Queary.replaceAll(" ", "%20");
            initVolley("https://www.googleapis.com/youtube/v3/search?part=id&q=" + Queary + "&type=video&key=AIzaSyA-C1pPmKSPZUONcWCFqMOyILan7DUFi7I");
            Intent i = new Intent(youtubePlayActivity.this, TTSService.class);
            i.putExtra("toSpeak", this.getResources().getString(R.string.Aap_Ka__Gaana_Lagaaya_Ja_Raha_Ha));
            i.putExtra("Language", "hi");
            this.startService(i);

        }


    }

    @Override
    public void onError(int Error) {
        Toast.makeText(youtubePlayActivity.this, "onSpeech Error "+Error, Toast.LENGTH_SHORT).show();
        if (newIntance != null)
            getSupportFragmentManager().beginTransaction().remove(newIntance).commitAllowingStateLoss();


    }
}
