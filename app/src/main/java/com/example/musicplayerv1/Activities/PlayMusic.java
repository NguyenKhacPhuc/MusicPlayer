package com.example.musicplayerv1.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.example.musicplayerv1.R;

import de.hdodenhof.circleimageview.CircleImageView;


public class PlayMusic extends AppCompatActivity implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
    Toolbar toolbar;
    CircleImageView thumbnail;
    ImageButton options;
    PopupMenu popup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_act);
        initView();
        Window window = this.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.mainhthene));
        options.setOnClickListener(this);

    }
    void initView(){
        options = findViewById(R.id.options);
        thumbnail = findViewById(R.id.thumbnail);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.options:
                showOptions(v);
                popup.setOnMenuItemClickListener(PlayMusic.this);
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
        return true;
    }
}