package com.dania.one;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import com.dania.one.Adapters.AllCategoriesAdapter;
import com.dania.one.Adapters.CategoriesAdapter2;
import com.dania.one.Model.CategoriesModel;
import com.dania.one.Model.CategoriesPublicModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

public class SearchCategories extends AppCompatActivity {

    private RecyclerView rv_top,rv_added, rv;
    private FirebaseFirestore db;
    private CollectionReference catReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_categories);
        rv = findViewById(R.id.rv);
        rv_top = findViewById(R.id.rv_top);
        rv_added = findViewById(R.id.rv_added);
        rv.setHasFixedSize(true);
        rv_top.setHasFixedSize(true);
        rv_added.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),3);
        rv.setLayoutManager(gridLayoutManager);
        LinearLayoutManager lm = new LinearLayoutManager(getApplicationContext());
        lm.setOrientation(RecyclerView.HORIZONTAL);
        rv_top.setLayoutManager(lm);
        LinearLayoutManager lm2 = new LinearLayoutManager(getApplicationContext());
        lm2.setOrientation(RecyclerView.HORIZONTAL);
        rv_added.setLayoutManager(lm2);
        getDefaultCategories();
        getAddedCategories();
        getAllCategories();
    }

    private void getAllCategories() {
        CategoriesGlobal.allCatNames = new ArrayList<>();
        CategoriesGlobal.allCategories = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        catReference = db.collection("Categories Public");
        Query query = catReference;
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.size()>0){
                    for (QueryDocumentSnapshot querySnapshot : queryDocumentSnapshots){
                        CategoriesPublicModel cpm = querySnapshot.toObject(CategoriesPublicModel.class);
                        if (!CategoriesGlobal.allCatNames.contains(cpm.getName())){
                            CategoriesGlobal.allCategories.add(cpm);
                        }
                    }
                    AllCategoriesAdapter allCategoriesAdapter = new AllCategoriesAdapter(SearchCategories.this);
                    rv.setAdapter(allCategoriesAdapter);
                }
            }
        });
    }

    private void getDefaultCategories() {
        final ArrayList<CategoriesModel> categoriesModels = new ArrayList<>();
        CategoriesModel cat1, cat2, cat3;
        cat1 = new CategoriesModel("Trending","https://firebasestorage.googleapis.com/v0/b/projectdns-120a2.appspot.com/o/Categories%2FCategoriesATrending1601368832-1127976149.jpg?alt=media&token=a147815f-f976-48cf-b79c-a4225fbf14cb",1,true);
        cat2 = new CategoriesModel("Nearby","https://firebasestorage.googleapis.com/v0/b/projectdns-120a2.appspot.com/o/Categories%2FCategoriesBNearby1601707264-524083585.jpg?alt=media&token=7dfa767c-9893-429a-9885-f55308945947",2,false);
        cat3 = new CategoriesModel("Buddies","https://firebasestorage.googleapis.com/v0/b/projectdns-120a2.appspot.com/o/Categories%2FCategoriesCBuddies1601707392859287040.jpg?alt=media&token=beb3c78b-36f7-4963-9975-cf30a95e559b",3,false);
        categoriesModels.add(cat1);
        categoriesModels.add(cat2);
        categoriesModels.add(cat3);
        CategoriesAdapter2 categoriesAdapter2 = new CategoriesAdapter2(this,categoriesModels, null);
        rv_top.setAdapter(categoriesAdapter2);
    }

    private void getAddedCategories() {
        final ArrayList<CategoriesModel> categoriesModels = new ArrayList<>();
        CategoriesModel cat1, cat2;
        cat1 = new CategoriesModel("Memes","https://firebasestorage.googleapis.com/v0/b/projectdns-120a2.appspot.com/o/Categories%2FCategoriesATrending1601368832-1127976149.jpg?alt=media&token=a147815f-f976-48cf-b79c-a4225fbf14cb",1,true);
        cat2 = new CategoriesModel("Sports","https://firebasestorage.googleapis.com/v0/b/projectdns-120a2.appspot.com/o/Categories%2FCategoriesBNearby1601707264-524083585.jpg?alt=media&token=7dfa767c-9893-429a-9885-f55308945947",2,false);
        categoriesModels.add(cat1);
        categoriesModels.add(cat2);
        CategoriesAdapter2 categoriesAdapter2 = new CategoriesAdapter2(this,categoriesModels, null);
        rv_added.setAdapter(categoriesAdapter2);
    }
}