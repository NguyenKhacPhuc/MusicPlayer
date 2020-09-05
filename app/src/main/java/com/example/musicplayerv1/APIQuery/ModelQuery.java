package com.example.musicplayerv1.APIQuery;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.musicplayerv1.Interfaces.ICallBackModel;
import com.example.musicplayerv1.Model.Model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class ModelQuery {
    private FirebaseFirestore db ;
    private ArrayList<Model> models ;
    public void getAllModel(final ICallBackModel iCallBackModel){
        models = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        db.collection("HomePLaylist")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot documentSnapshot: Objects.requireNonNull(task.getResult())){
                        String title = documentSnapshot.getString("Title");
                        String description = documentSnapshot.getString("Description");
                        String tag = documentSnapshot.getString("tag");
                        String idList = documentSnapshot.getString("idList");
                        Model model = new Model(title,description,tag,idList);
                        assert title != null;
                        Log.d("title",title);
                        models.add(model);
                    }
                    iCallBackModel.callBackModels(models);
                }
            }
        });
    }
}
