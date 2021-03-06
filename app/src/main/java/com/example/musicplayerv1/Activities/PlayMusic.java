package com.example.musicplayerv1.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import com.bumptech.glide.request.RequestOptions;
import com.example.musicplayerv1.APIQuery.QueryTrackUrl;
import com.example.musicplayerv1.Common.Timer;
import com.example.musicplayerv1.Injection;
import com.example.musicplayerv1.Interfaces.IPassUrl;
import com.example.musicplayerv1.Model.Track;
import com.example.musicplayerv1.R;
import com.example.musicplayerv1.Services.MusicPlayService;
import com.example.musicplayerv1.SubFragment.BottomSheetFragment;
import com.example.musicplayerv1.YoutubeConfig.YoutubeConstant;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.hdodenhof.circleimageview.CircleImageView;

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
    static ServiceConnection serviceConnection;
    static MusicPlayService musicPlayService;
    long intentCurrentMil;
    ImageButton next;
    ImageButton previous;
    ImageButton stopPlay;
    static ArrayList<Track> tracks;
    QueryTrackUrl queryTrackUrl;
    public static RequestQueue requestQueue;
    ExecutorService executorService;
    ArrayList<String> keyLst;
    TrackBroadcastReceiver trackBroadcastReceiver;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ImageButton repeatBtn;
    ImageButton backBtn;
    public static boolean isRepeat;
    public static int position;
    public static boolean isBind = false;
    public static boolean isAlive;
    public static ArrayList<Track> getTracks() {
        return tracks;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_act);
        Intent intentReceiver = getIntent();
        tracks = new ArrayList<>();
        tracks.clear();
        tracks.addAll((ArrayList<Track>) intentReceiver.getSerializableExtra("tracks"));
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

        if (musicPlayService != null) {
            musicPlayService.release();
        }
        else{
            serviceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    MusicPlayService.MediaBinder mediaBinder = (MusicPlayService.MediaBinder) service;
                    musicPlayService = mediaBinder.getService();
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {

                }
            };
            bindService(new Intent(getApplicationContext(), MusicPlayService.class), serviceConnection, Context.BIND_AUTO_CREATE);
            isBind= true;
        }

        triggerMusic(position, intentCurrentMil);

        heart.setOnClickListener(this);
        stopPlay.setOnClickListener(this);
        next.setOnClickListener(this);
        previous.setOnClickListener(this);
        repeatBtn.setOnClickListener(this);
        backBtn.setOnClickListener(this);
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
        backBtn = findViewById(R.id.backBtn);
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
                    heart.setAlpha(0.5f);
                    isLiked = true;

                    editor.putString(tracks.get(position).getId(), tracks.get(position).getTrackName());
                }
                editor.apply();
                break;
            case R.id.next:
                //TODO: next track
                serviceConnection = new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName name, IBinder service) {
                        MusicPlayService.MediaBinder mediaBinder = (MusicPlayService.MediaBinder) service;
                        musicPlayService = mediaBinder.getService();
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName name) {

                    }
                };
                musicPlayService.release();
                stopService(new Intent(getApplicationContext(),MusicPlayService.class));
                position += 1;
                triggerMusic(position, 0L);


                break;
            case R.id.play:
                //TODO: pause/play track
                serviceConnection = new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName name, IBinder service) {
                        MusicPlayService.MediaBinder mediaBinder = (MusicPlayService.MediaBinder) service;
                        musicPlayService = mediaBinder.getService();
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName name) {

                    }
                };
                if (musicPlayService.isPlaying()) {
                    musicPlayService.paused();
                    timer.cancel();
                    stopPlay.setImageResource(R.drawable.play);
                    status.setText("PAUSED");

                } else {
                    musicPlayService.play();
                    stopPlay.setImageResource(R.drawable.pause);
                    status.setText("PLAYING");

                    long milProgress = seekBar.getProgress() * 1000;
                    long currentDuration = milDuration - milProgress;
                    timer.countDown(seekBar, milProgress, currentDuration, durationBegin, durationFinish, requestQueue, false);

                }
                break;
            case R.id.previous:
                //TODO: move to previous track
                serviceConnection = new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName name, IBinder service) {
                        MusicPlayService.MediaBinder mediaBinder = (MusicPlayService.MediaBinder) service;
                        musicPlayService = mediaBinder.getService();
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName name) {

                    }
                };
                musicPlayService.release();
                stopService(new Intent(getApplicationContext(),MusicPlayService.class));
                position -= 1;
                triggerMusic(position, 0L);
                break;
            case R.id.repeatBtn:
                serviceConnection = new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName name, IBinder service) {
                        MusicPlayService.MediaBinder mediaBinder = (MusicPlayService.MediaBinder) service;
                        musicPlayService = mediaBinder.getService();
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName name) {

                    }
                };
                if (isRepeat) {
                    musicPlayService.dismissRepeat();
                    repeatBtn.setImageResource(R.drawable.repeat_24);
                    isRepeat = false;

                } else {
                    musicPlayService.repeat();
                    repeatBtn.setImageResource(R.drawable.repeat_one_24);
                    isRepeat = true;
                }
                break;
            case R.id.backBtn:
                onBackPressed();
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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Download:
                int MY_READ_REQUEST = 1;
                if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},MY_READ_REQUEST);
                }
                downloadFromUrl(streamLink, "Downloading", title, false);
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC), title+".mp3");
                final String path = "file://"+file.getPath();
                Log.d("check", String.valueOf(file.exists()));
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        Injection.getProvidedTrackLocalStorage(getApplicationContext()).updateDownload(title,true);
                        Injection.getProvidedTrackLocalStorage(getApplicationContext()).updateStreamLink(title,path);
                    }
                });
                break;
            case R.id.addToPlaylist:
                try {
                    BottomSheetFragment bottomSheetDialogFragment = new BottomSheetFragment();
                    bottomSheetDialogFragment.show(getSupportFragmentManager(), "playlist adding");
                } catch (Exception e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.share:
                Intent send = new Intent(Intent.ACTION_SEND);
                send.setType("text/plain");
                send.putExtra(Intent.EXTRA_TEXT, YoutubeConstant.YOUTUBE_LINK+tracks.get(position).getId());
                startActivity(send);
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
        intent.putExtra("liked",isLiked);
        getApplicationContext().startService(intent);

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

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC, fileName+".mp3");

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
            final long milProgress = progress * 1000;
            long currentDuration = milDuration - milProgress;

            timer.countDown(seekBar, milProgress, currentDuration, durationBegin, durationFinish, requestQueue, false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                serviceConnection = new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName name, IBinder service) {
                        MusicPlayService.MediaBinder mediaBinder = (MusicPlayService.MediaBinder) service;
                        musicPlayService = mediaBinder.getService();

                    }

                    @Override
                    public void onServiceDisconnected(ComponentName name) {

                    }
                };
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
            musicPlayService.release();
            stopService(new Intent(getApplicationContext(),MusicPlayService.class));
            String channelNameStr = intent.getStringExtra("channelName");
            String trackNameStr = intent.getStringExtra("Title");
            long duration = intent.getLongExtra("duration", 0L);
            milDuration = duration * 1000;
            streamLink = intent.getStringExtra("streamLink");
            urlThumbnail = intent.getStringExtra("urlThumbnail");
            shortDescription = intent.getStringExtra("description");
            channelName.setText(channelNameStr);
            trackName.setText(trackNameStr);
            Glide.with(getApplicationContext()).load(urlThumbnail).into(thumbnail);
           serviceConnection = new ServiceConnection() {
               @Override
               public void onServiceConnected(ComponentName name, IBinder service) {
                   MusicPlayService.MediaBinder mediaBinder = (MusicPlayService.MediaBinder) service;
                   musicPlayService = mediaBinder.getService();
               }

               @Override
               public void onServiceDisconnected(ComponentName name) {
               }
           };
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("Pass Track to Home Fragment");
        trackBroadcastReceiver = new TrackBroadcastReceiver();
        registerReceiver(trackBroadcastReceiver, intentFilter);
        isAlive = true;
        isRepeat = false;
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
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                boolean isDownloaded =false;
                Track track = null;
                List<Boolean> checks = Injection.getProvidedTrackLocalStorage(getApplicationContext()).getDownloaded(tracks.get(position).getTrackName());
                if(!checks.isEmpty()) {
                     isDownloaded = checks.get(0);
                    track =  Injection.getProvidedTrackLocalStorage(getApplicationContext()).getA(tracks.get(position).getId());
                }
                if(isDownloaded){

                    streamLink = track.getStreamLink();
                    duration = track.getDuration();
                    author = tracks.get(position).getArtist();
                    title = tracks.get(iDPosition).getTrackName();
                    shortDescription = track.getDescription();
                    urlThumbnail = tracks.get(position).getUrlThumbnail();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"downloaded",Toast.LENGTH_SHORT).show();
                            Glide.with(PlayMusic.this).load(urlThumbnail).into(thumbnail);
                            trackName.setText(title);
                            channelName.setText(author);
                            milDuration = duration * 1000;
                            seekBar.setMax((int) (milDuration / 1000));
                            timer.countDown(seekBar, currentMil, milDuration, durationBegin, durationFinish, requestQueue, true);
                            seekBar.setOnSeekBarChangeListener(PlayMusic.this);
                        }
                    });

                    playMusic();
                }
                else{
                    queryTrackUrl = new QueryTrackUrl(tracks.get(position).getId(), requestQueue, getApplicationContext());
                    queryTrackUrl.returnUrl(new IPassUrl() {
                        @Override
                        public void getUr(Track url) {
                            duration = url.getDuration();
                            author = tracks.get(position).getArtist();
                            title = tracks.get(iDPosition).getTrackName();
                            shortDescription = url.getDescription();
                            urlThumbnail = tracks.get(position).getUrlThumbnail();
                            streamLink = url.getStreamLink();
                            Glide.with(PlayMusic.this).load(urlThumbnail).apply(RequestOptions.centerCropTransform()).into(thumbnail);
                            Animation rotate = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotateanim);
                            thumbnail.startAnimation(rotate);
                            trackName.setText(title);
                            channelName.setText(author);
                            final Track track = new Track(tracks.get(position).getId(), author, title, urlThumbnail, isLiked, shortDescription,duration, streamLink);
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
        });


        }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(trackBroadcastReceiver);
        isAlive = false;

    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}