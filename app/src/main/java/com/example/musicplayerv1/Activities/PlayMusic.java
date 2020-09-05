package com.example.musicplayerv1.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;


import android.os.Environment;
import android.os.IBinder;

import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.musicplayerv1.APIQuery.QueryTrackUrl;
import com.example.musicplayerv1.Common.ProgressDialogSingleton;
import com.example.musicplayerv1.Common.Timer;
import com.example.musicplayerv1.Interfaces.IPassUrl;
import com.example.musicplayerv1.Model.Track;
import com.example.musicplayerv1.R;
import com.example.musicplayerv1.Services.MusicPlayService;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlayMusic extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener,SeekBar.OnSeekBarChangeListener {
    Toolbar toolbar;
    CircleImageView thumbnail;
    ImageButton options;
    PopupMenu popup;
    ServiceConnection serviceConnection;
    MusicPlayService musicPlayService;
    String streamLink;
    String urlThumbnail;
    String title;
    String author;
    String shortDescription;
    long duration;
    TextView trackName;
    TextView channelName;
    TextView description;
    SeekBar seekBar;
    TextView durationBegin;
    TextView durationFinish;
    TextView status;
    boolean isLiked;
    long milDuration;
    ImageView heart;
    Timer timer;
    ImageButton next;
    ImageButton previous;
    ImageButton stopPlay;
    ArrayList<Track> tracks;
    QueryTrackUrl queryTrackUrl;
    RequestQueue requestQueue;
    TrackBroadcastReceiver trackBroadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_act);

        Intent intentReceiver = getIntent();
        tracks = (ArrayList<Track>) intentReceiver.getSerializableExtra("tracks");
        final int position = intentReceiver.getIntExtra("position",0);
        initView();
        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.mainhthene));
        options.setOnClickListener(this);
        //handle receive intent

            queryTrackUrl = new QueryTrackUrl(tracks.get(position).getId(),requestQueue,this);
            queryTrackUrl.returnUrl(new IPassUrl() {
                @Override
                public void getUr(Track url) {
                    duration = url.getDuration();
                    author = url.getArtist();
                    title = url.getTrackName();
                    shortDescription = url.getDescription();
                    urlThumbnail = tracks.get(position).getUrlThumbnail();
                    streamLink = url.getStreamLink();
                    Glide.with(PlayMusic.this).load(urlThumbnail).into(thumbnail);
                    trackName.setText(author);
                    channelName.setText(title);
                    description.setText(shortDescription);

                    milDuration = duration * 1000;
                    seekBar.setMax((int) (milDuration/1000));
                    timer.countDown(seekBar,0L,milDuration,durationBegin,durationFinish,requestQueue);
                    seekBar.setOnSeekBarChangeListener(PlayMusic.this);
                    playMusic();
                }

            });

            heart.setOnClickListener(this);
            stopPlay.setOnClickListener(this);

    }
    void initView(){
        options = findViewById(R.id.options);
        thumbnail = findViewById(R.id.thumbnail);
        trackName = findViewById(R.id.track_name);
        channelName = findViewById(R.id.channel_name);
        description = findViewById(R.id.description);
        durationBegin = findViewById(R.id.durationBegin);
        durationFinish = findViewById(R.id.durationFinish);
        seekBar = findViewById(R.id.seekbar);
        timer = new Timer(tracks,this);
        next = findViewById(R.id.next);
        previous = findViewById(R.id.previous);
        stopPlay = findViewById(R.id.play);
        heart = findViewById(R.id.heart);
        status = findViewById(R.id.statusTrack);
        requestQueue = Volley.newRequestQueue(this);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.options:
                showOptions(v);
                popup.setOnMenuItemClickListener(PlayMusic.this);
                break;
            case R.id.heart:
                Animation animation = AnimationUtils.loadAnimation(this,R.anim.like_anim);
                heart.startAnimation(animation);
                if(isLiked){
                    heart.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                    isLiked= false;
                }
                else{
                    heart.setImageResource(R.drawable.ic_baseline_favorite_24);
                    isLiked = true;
                }
            case R.id.next:
                //TODO: next track
                break;
            case R.id.play:
                //TODO: pause/play track
               if(musicPlayService.isPlaying()) {
                   musicPlayService.paused();
                   stopPlay.setImageResource(R.drawable.play);
                    status.setText("PAUSED");
                   Toast.makeText(this,"Paused",Toast.LENGTH_SHORT).show();
               }
               else {
                   musicPlayService.play();
                   stopPlay.setImageResource(R.drawable.pause);
                   status.setText("PLAYING");
                   Toast.makeText(this, "Play", Toast.LENGTH_SHORT).show();

               }
                break;
            case R.id.previous:
                //TODO: move to previous track
                break;
            case R.id.backBtn:
                //TODO: move to previous activity
                break;
            default:
                break;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void showOptions(View v){
        popup = new PopupMenu(this,v);
        MenuInflater inflater = popup.getMenuInflater();
        popup.setGravity(Gravity.END);
        inflater.inflate(R.menu.toolbar_button,popup.getMenu());
        popup.show();

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.Download:
                downloadFromUrl(streamLink,"Downloading",author,false);
                break;
            case R.id.addToPlaylist:
                break;
            case R.id.share:
                break;
            default:
                break;
        }
        return true;
    }
    void playMusic() {
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                MusicPlayService.MediaBinder mediaBinder = (MusicPlayService.MediaBinder) service;
                musicPlayService = mediaBinder.getService();
                musicPlayService.play();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        Intent intent = new Intent(this,MusicPlayService.class);
        intent.putExtra("streamLink",streamLink);
        intent.putExtra("Title",author);
        startService(intent);
        Objects.requireNonNull(this).bindService(intent,serviceConnection, Context.BIND_AUTO_CREATE);
    }
    private long downloadFromUrl(String youtubeDlUrl, String downloadTitle, String fileName, boolean hide) {
        Uri uri = Uri.parse(youtubeDlUrl);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(downloadTitle);
        if (hide) {
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
            request.setVisibleInDownloadsUi(false);
        } else
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        return manager.enqueue(request);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(fromUser){
                    long milProgress = progress*1000;
                    long currentDuration = milDuration-milProgress;
                timer.countDown(seekBar,milProgress,currentDuration,durationBegin,durationFinish,requestQueue);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    musicPlayService.seekTo(milProgress);
                }
            }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
    class TrackBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(final Context context, Intent intent) {
            unbindService(serviceConnection);
            stopService(new Intent(context, MusicPlayService.class));
            String channelNameStr = intent.getStringExtra("channelName");
            String trackNameStr = intent.getStringExtra("Title");
            long duration = intent.getLongExtra("duration",0L);
            milDuration = duration * 1000;
            streamLink = intent.getStringExtra("streamLink");
            urlThumbnail = intent.getStringExtra("urlThumbnail");
            shortDescription = intent.getStringExtra("description");
            channelName.setText(channelNameStr);
            trackName.setText(trackNameStr);
            description.setText(shortDescription);
            Glide.with(getApplicationContext()).load(urlThumbnail).into(thumbnail);
            playMusic();

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("Pass Track to Home Fragment");
        trackBroadcastReceiver = new TrackBroadcastReceiver();
        registerReceiver(trackBroadcastReceiver,intentFilter);

    }
    @Override
    protected void onResume() {
        super.onResume();

    }


}