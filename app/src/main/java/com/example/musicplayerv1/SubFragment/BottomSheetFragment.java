package com.example.musicplayerv1.SubFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayerv1.Adapters.CardStackAdapter;
import com.example.musicplayerv1.Model.StackModel;
import com.example.musicplayerv1.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;
import com.yuyakaido.android.cardstackview.StackFrom;
import com.yuyakaido.android.cardstackview.SwipeableMethod;

import java.util.ArrayList;
import java.util.List;

public class BottomSheetFragment extends BottomSheetDialogFragment {
    View v;
    RecyclerView cardStackView;
    LinearLayoutManager cardStackLayoutManager;
    CardStackAdapter cardStackAdapter;
    List<StackModel> models;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v =  inflater.inflate(R.layout.bottom_sheet_add_playlist,container,false);
        initView();
        addData();

        cardStackView.setLayoutManager(cardStackLayoutManager);
        cardStackView.setAdapter(cardStackAdapter);
        Toast.makeText(getContext(),String.valueOf(models.size()),Toast.LENGTH_SHORT).show();
        cardStackView.setItemAnimator(new DefaultItemAnimator());
        return v;
    }

    private void addData() {
        models.add(new StackModel("Phuc","https://upload.wikimedia.org/wikipedia/commons/thumb/d/d8/ILLENIUM.jpg/1200px-ILLENIUM.jpg","35 tracks"));
        models.add(new StackModel("Phuc","https://upload.wikimedia.org/wikipedia/commons/thumb/d/d8/ILLENIUM.jpg/1200px-ILLENIUM.jpg","35 tracks"));
        models.add(new StackModel("Phuc","https://upload.wikimedia.org/wikipedia/commons/thumb/d/d8/ILLENIUM.jpg/1200px-ILLENIUM.jpg","35 tracks"));
        models.add(new StackModel("Phuc","https://upload.wikimedia.org/wikipedia/commons/thumb/d/d8/ILLENIUM.jpg/1200px-ILLENIUM.jpg","35 tracks"));
        models.add(new StackModel("Phuc","https://upload.wikimedia.org/wikipedia/commons/thumb/d/d8/ILLENIUM.jpg/1200px-ILLENIUM.jpg","35 tracks"));
        models.add(new StackModel("Phuc","https://upload.wikimedia.org/wikipedia/commons/thumb/d/d8/ILLENIUM.jpg/1200px-ILLENIUM.jpg","35 tracks"));
    }

    void initView(){
        cardStackView = v.findViewById(R.id.stack_view);
        models = new ArrayList<>();
        cardStackAdapter = new CardStackAdapter(models,getContext());
        cardStackLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL,false);
    }
}
