package com.example.musicplayerv1.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;


import android.os.Environment;
import android.os.IBinder;

import android.util.Log;
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
import com.example.musicplayerv1.App;
import com.example.musicplayerv1.Common.ProgressDialogSingleton;
import com.example.musicplayerv1.Common.Timer;
import com.example.musicplayerv1.Injection;
import com.example.musicplayerv1.Interfaces.IPassUrl;
import com.example.musicplayerv1.Model.Track;
import com.example.musicplayerv1.R;
import com.example.musicplayerv1.Services.MusicPlayService;
import com.example.musicplayerv1.SubFragment.BottomSheetFragment;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.musicplayerv1.App.CHANNEL_ID;
import static com.example.musicplayerv1.App.MUSICPLAYSERVICE;
import static com.example.musicplayerv1.App.SERVICECONNECTION;

public class PlayMusic extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener, SeekBar.OnSeekBarChangeListener {
    Toolbar toolbar;
    CircleImageView thumbnail;
    ImageButton options;
    PopupMenu popup;
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
    long intentCurrentMil;
    ImageButton next;
    ImageButton previous;
    ImageButton stopPlay;
    public  static ArrayList<Track> tracks;
    QueryTrackUrl queryTrackUrl;
    RequestQueue requestQueue;
    ExecutorService executorService;
    ArrayList<String> keyLst;
    TrackBroadcastReceiver trackBroadcastReceiver;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ImageButton repeatBtn;
    boolean isRepeat;
    public static int position;
    static boolean isBind = false;
  public static boolean isAlive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_act);
        Intent intentReceiver = getIntent();
        tracks = (ArrayList<Track>) intentReceiver.getSerializableExtra("tracks");
        position = intentReceiver.getIntExtra("position", 0);
        initView();

        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.mainhthene));
        options.setOnClickListener(this);
        //handle receive intent
//                unbindService(SERVICECONNECTION);

                triggerMusic(position, intentCurrentMil);

        heart.setOnClickListener(this);
        stopPlay.setOnClickListener(this);
        next.setOnClickListener(this);
        previous.setOnClickListener(this);
        repeatBtn.setOnClickListener(this);
        Set<String> keySet = sharedPreferences.getAll().keySet();
        keyLst = new ArrayList<>(keySet);
        if (keyLst.contains(tracks.get(position).getId())) {
            heart.setImageResource(R.drawable.ic_baseline_favorite_24);
            isLiked = true;
        }
        executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                ArrayList<Track> tracks = (ArrayList<Track>) Injection.getProvidedTrackLocalStorage(getApplicationContext()).getAll();
                for (Track track : tracks) {
                    //TODO:do sth here
                }
            }
        });

    }

    @SuppressLint("CommitPrefEdits")
    void initView() {
        options = findViewById(R.id.options);
        thumbnail = findViewById(R.id.thumbnail);
        trackName = findViewById(R.id.track_name);
        channelName = findViewById(R.id.channel_name);
        description = findViewById(R.id.description);
        durationBegin = findViewById(R.id.durationBegin);
        durationFinish = findViewById(R.id.durationFinish);
        seekBar = findViewById(R.id.seekbar);
        timer = new Timer(tracks, this, position);
        next = findViewById(R.id.next);
        previous = findViewById(R.id.previous);
        stopPlay = findViewById(R.id.play);
        repeatBtn = findViewById(R.id.repeatBtn);
        heart = findViewById(R.id.heart);
        status = findViewById(R.id.statusTrack);
        requestQueue = Volley.newRequestQueue(this);
        sharedPreferences = getSharedPreferences("LIKED", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        executorService = Executors.newSingleThreadExecutor();

    }

    @SuppressLint("CommitPrefEdits")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.options:
                showOptions(v);
                popup.setOnMenuItemClickListener(PlayMusic.this);
                break;
            case R.id.heart:
                Animation animation = AnimationUtils.loadAnimation(this, R.anim.like_anim);
                heart.startAnimation(animation);
                if (isLiked) {
                    heart.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                    isLiked = false;
                    editor.remove(tracks.get(position).getId());
                } else {
                    heart.setImageResource(R.drawable.ic_baseline_favorite_24);
                    isLiked = true;

                    editor.putString(tracks.get(position).getId(), tracks.get(position).getTrackName());
                }
                editor.apply();
                break;
            case R.id.next:
                //TODO: next track
                MUSICPLAYSERVICE.release();
                    getApplicationContext().stopService(new Intent(getApplicationContext(), MusicPlayService.class));
                    position += 1;
                    triggerMusic(position, 0L);

                break;
            case R.id.play:
                //TODO: pause/play track
                if (MUSICPLAYSERVICE.isPlaying()) {
                    MUSICPLAYSERVICE.paused();
                    timer.cancel();
                    stopPlay.setImageResource(R.drawable.play);
                    status.setText("PAUSED");

                } else {
                    MUSICPLAYSERVICE.play();
                    stopPlay.setImageResource(R.drawable.pause);
                    status.setText("PLAYING");

                    long milProgress = seekBar.getProgress() * 1000;
                    long currentDuration = milDuration - milProgress;
                    timer.countDown(seekBar, milProgress, currentDuration, durationBegin, durationFinish, requestQueue, false);

                }
                break;
            case R.id.previous:
                //TODO: move to previous track
                MUSICPLAYSERVICE.release();
                stopService(new Intent(getApplicationContext(), MusicPlayService.class));
                position -= 1;
                triggerMusic(position, 0L);
                break;
            case R.id.backBtn:
                //TODO: move to previous activity
                startActivity(new Intent(this,MainActivity.class));
                break;
            case R.id.repeatBtn:
                if(isRepeat){
                    MUSICPLAYSERVICE.dismissRepeat();
                    repeatBtn.setImageResource(R.drawable.repeat_24);
                    isRepeat = false;

                }
                else{
                    MUSICPLAYSERVICE.repeat();
                    repeatBtn.setImageResource(R.drawable.repeat_one_24);
                    isRepeat = true;
                }
                break;
            default:
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void showOptions(View v) {
        popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        popup.setGravity(Gravity.END);
        inflater.inflate(R.menu.toolbar_button, popup.getMenu());
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Download:
                downloadFromUrl(streamLink, "Downloading", title, false);
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC), title);
                final String path = file.getAbsolutePath();
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        Injection.getProvidedTrackLocalStorage(getApplicationContext()).updateDownload(true);
                        Injection.getProvidedTrackLocalStorage(getApplicationContext()).updateStreamLink(path);
                    }
                });
                break;
            case R.id.addToPlaylist:
                try {
                    BottomSheetFragment bottomSheetDialogFragment = new BottomSheetFragment();
                    bottomSheetDialogFragment.show(getSupportFragmentManager(), "playlist adding");
                }catch (Exception e){
                    Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.share:
                break;
            default:
                break;
        }
        return true;
    }

    void playMusic() {

        Intent intent = new Intent(getApplicationContext(), MusicPlayService.class);
        intent.putExtra("streamLink", streamLink);
        intent.putExtra("title", title);
        intent.putExtra("thumbnail", urlThumbnail);
        intent.putExtra("author", author);
        intent.putExtra("tracks", (Serializable) tracks);
        intent.putExtra("position", position);
        getApplicationContext().startService(intent);
        getApplicationContext().bindService(new Intent(getApplicationContext(),MusicPlayService.class),SERVICECONNECTION, Context.BIND_AUTO_CREATE);
        isBind = true;

    }

    private void downloadFromUrl(String youtubeDlUrl, String downloadTitle, String fileName, boolean hide) {
        Uri uri = Uri.parse(youtubeDlUrl);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle(downloadTitle);
        if (hide) {
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);
            request.setVisibleInDownloadsUi(false);
        } else
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC, fileName);

        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        assert manager != null;
         manager.enqueue(request);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            long milProgress = progress * 1000;
            long currentDuration = milDuration - milProgress;

            timer.countDown(seekBar, milProgress, currentDuration, durationBegin, durationFinish, requestQueue, false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                MUSICPLAYSERVICE.seekTo(milProgress);
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
            stopService(new Intent(getApplicationContext(), MusicPlayService.class));
            String channelNameStr = intent.getStringExtra("channelName");
            String trackNameStr = intent.getStringExtra("Title");
            long duration = intent.getLongExtra("duration", 0L);
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
        registerReceiver(trackBroadcastReceiver, intentFilter);
        isAlive =true;

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    private void triggerMusic(final int iDPosition, final long currentMil) {

        if ((tracks.get(position).isDownloaded())) {
            streamLink = tracks.get(position).getStreamLink();
            playMusic();
        } else {
            queryTrackUrl = new QueryTrackUrl(tracks.get(position).getId(), requestQueue, this);
            queryTrackUrl.returnUrl(new IPassUrl() {
                @Override
                public void getUr(Track url) {
                    duration = url.getDuration();
                    author = tracks.get(position).getArtist();
                    title = tracks.get(iDPosition).getTrackName();
                    shortDescription = url.getDescription();
                    urlThumbnail = tracks.get(position).getUrlThumbnail();
                    streamLink = url.getStreamLink();
                    Glide.with(PlayMusic.this).load(urlThumbnail).into(thumbnail);
                    trackName.setText(title);
                    channelName.setText(author);
                    description.setText(shortDescription);
                    final Track track = new Track(tracks.get(position).getId(), author, title, urlThumbnail, isLiked, shortDescription, streamLink);
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            Injection.getProvidedTrackLocalStorage(getApplicationContext()).insert(track);
                        }
                    });
                    milDuration = duration * 1000;
                    seekBar.setMax((int) (milDuration / 1000));
                    timer.countDown(seekBar, currentMil, milDuration, durationBegin, durationFinish, requestQueue, true);
                    seekBar.setOnSeekBarChangeListener(PlayMusic.this);
                    playMusic();
                }

            });

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        getApplicationContext().unbindService(SERVICECONNECTION);
        unregisterReceiver(trackBroadcastReceiver);
        isAlive = false;
    }
}