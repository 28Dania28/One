package com.dania.one;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.dania.one.Adapters.AddCategoriesAdapter;
import com.dania.one.Adapters.SearchAddCategoriesAdapter;
import com.dania.one.Adapters.SelectedAddCategoriesAdapter;
import com.dania.one.Model.CategoriesPublicModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AddCategories extends AppCompatActivity implements AddCategoriesListener,SearchAddCategoriesListener{


    private FirebaseFirestore db;
    private CollectionReference catReference;
    private RecyclerView rv, rv_top, rv_search;
    private Button doneBtn;
    AddCategoriesAdapter addCategoriesAdapter;
    private Dialog confirDialog;
    private Button dc_discard;
    private Button dc_keep;
    private SearchView sv;
    private ArrayList<CategoriesPublicModel> catPublicModels = new ArrayList<>();
    private ArrayList<String> catNames = new ArrayList<>();
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_categories);
        doneBtn = findViewById(R.id.doneBtn);
        rv = findViewById(R.id.rv);
        rv_top = findViewById(R.id.rv_top);
        rv_search = findViewById(R.id.rv_search);
        sv = findViewById(R.id.sv);
        back = findViewById(R.id.back);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv_top.setHasFixedSize(true);
        confirDialog = new Dialog(AddCategories.this);
        confirDialog.setContentView(R.layout.dialog_confirm);
        confirDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        confirDialog.setCanceledOnTouchOutside(false);
        dc_discard = confirDialog.findViewById(R.id.dc_discard);
        dc_keep = confirDialog.findViewById(R.id.dc_keep);
        LinearLayoutManager lm = new LinearLayoutManager(getApplicationContext());
        lm.setOrientation(RecyclerView.HORIZONTAL);
        rv_top.setLayoutManager(lm);
        rv_search.setHasFixedSize(true);
        rv_search.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        db = FirebaseFirestore.getInstance();
        catReference = db.collection("Categories Public");
        if (AddStoryGlobal.selectedCategoriesGlobal.size()>0){
            doneBtn.setEnabled(true);
            doneBtn.setTextColor(getResources().getColor(R.color.colorAnahata));
            catPublicModels.addAll(AddStoryGlobal.selectedCategoriesGlobal);
            SelectedAddCategoriesAdapter selectedAddCategoriesAdapter = new SelectedAddCategoriesAdapter(getApplicationContext(),AddStoryGlobal.selectedCategoriesGlobal);
            rv_top.setAdapter(selectedAddCategoriesAdapter);
            addToRv(catPublicModels);
            for (int i=0;i<AddStoryGlobal.selectedCategoriesGlobal.size();i++){
                catNames.add(AddStoryGlobal.selectedCategoriesGlobal.get(i).getName());
            }
        }
        Query query = catReference.limit(20);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.size()>0){
                    for (QueryDocumentSnapshot querySnapshot : queryDocumentSnapshots){
                        CategoriesPublicModel cpm = querySnapshot.toObject(CategoriesPublicModel.class);
                        if (!catNames.contains(cpm.getName())){
                            catPublicModels.add(cpm);
                        }
                    }
                    addToRv(catPublicModels);
                }
            }
        });
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchDatabase(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchDatabase(newText);
                return true;
            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddStoryGlobal.selectedCategoriesGlobal = addCategoriesAdapter.getSelectedCategories();
                finish();
            }
        });

        dc_discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddStoryGlobal.selectedCategoriesGlobal = new ArrayList<>();
                finish();
            }
        });

        dc_keep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirDialog.dismiss();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void searchDatabase(String query) {
        ArrayList<CategoriesPublicModel> allCPM = addCategoriesAdapter.categoriesPublicModels;
        final ArrayList<CategoriesPublicModel> resultCPM = new ArrayList<>();
        if (!query.trim().isEmpty()) {

            for (int i = 0; i < allCPM.size(); i++) {
                if (allCPM.get(i).getName().toLowerCase().contains(query.toLowerCase())) {
                    resultCPM.add(allCPM.get(i));
                }
            }

            /*
            Query query1 = catReference.orderBy("name").startAt(query).endAt(query + "\uf8ff").limit(10);
            query1.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot querySnapshot : queryDocumentSnapshots){
                        CategoriesPublicModel cpm = querySnapshot.toObject(CategoriesPublicModel.class);
                        resultCPM.add(cpm);
                    }
                }
            });

            /*

            /*String capQuery = capitalize(query);
            Query query2 = catReference.orderBy("name").startAt(capQuery).endAt(capQuery + "\uf8ff").limit(10);
            query2.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot querySnapshot : queryDocumentSnapshots){
                        CategoriesPublicModel cpm = querySnapshot.toObject(CategoriesPublicModel.class);
                        resultCPM.add(cpm);
                    }
                }
            });

            Query query3 = catReference.orderBy("name").startAt(query.toLowerCase()).endAt(query.toLowerCase() + "\uf8ff").limit(10);
            query3.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot querySnapshot : queryDocumentSnapshots){
                        CategoriesPublicModel cpm = querySnapshot.toObject(CategoriesPublicModel.class);
                        resultCPM.add(cpm);
                    }
                }
            });


             */

            SearchAddCategoriesAdapter searchAddCategoriesAdapter = new SearchAddCategoriesAdapter(getApplicationContext(), resultCPM,this);
            rv_search.setAdapter(searchAddCategoriesAdapter);
        }else {
            SearchAddCategoriesAdapter searchAddCategoriesAdapter = new SearchAddCategoriesAdapter(getApplicationContext(), resultCPM,this);
            rv_search.setAdapter(searchAddCategoriesAdapter);
        }


    }

    private void addToRv(ArrayList<CategoriesPublicModel> catPublicModels) {
        addCategoriesAdapter = new AddCategoriesAdapter(getApplicationContext(),catPublicModels,this);
        rv.setAdapter(addCategoriesAdapter);
    }

    @Override
    public void OnAction(Boolean isSelected) {
        if (isSelected){
            doneBtn.setEnabled(true);
            doneBtn.setTextColor(getResources().getColor(R.color.colorAnahata));
            ArrayList<CategoriesPublicModel> categoriesPublicModels = addCategoriesAdapter.getSelectedCategories();
            SelectedAddCategoriesAdapter selectedAddCategoriesAdapter = new SelectedAddCategoriesAdapter(getApplicationContext(),categoriesPublicModels);
            rv_top.setAdapter(selectedAddCategoriesAdapter);
            searchDatabase(sv.getQuery().toString());
        }else {
            doneBtn.setEnabled(false);
            doneBtn.setTextColor(getResources().getColor(R.color.txt2));
            ArrayList<CategoriesPublicModel> categoriesPublicModels = addCategoriesAdapter.getSelectedCategories();
            SelectedAddCategoriesAdapter selectedAddCategoriesAdapter = new SelectedAddCategoriesAdapter(getApplicationContext(),categoriesPublicModels);
            rv_top.setAdapter(selectedAddCategoriesAdapter);
            searchDatabase(sv.getQuery().toString());
        }
    }

    @Override
    public void OnSearchAction(Boolean isSelected) {
        if (isSelected){
            doneBtn.setEnabled(true);
            doneBtn.setTextColor(getResources().getColor(R.color.colorAnahata));
            ArrayList<CategoriesPublicModel> categoriesPublicModels = addCategoriesAdapter.getSelectedCategories();
            SelectedAddCategoriesAdapter selectedAddCategoriesAdapter = new SelectedAddCategoriesAdapter(getApplicationContext(),categoriesPublicModels);
            rv_top.setAdapter(selectedAddCategoriesAdapter);
        }else {
            doneBtn.setEnabled(false);
            doneBtn.setTextColor(getResources().getColor(R.color.txt2));
            ArrayList<CategoriesPublicModel> categoriesPublicModels = addCategoriesAdapter.getSelectedCategories();
            SelectedAddCategoriesAdapter selectedAddCategoriesAdapter = new SelectedAddCategoriesAdapter(getApplicationContext(),categoriesPublicModels);
            rv_top.setAdapter(selectedAddCategoriesAdapter);
        }
    }

    @Override
    public void OnSelect(CategoriesPublicModel categoriesPublicModel) {
        for (int i=0;i<addCategoriesAdapter.categoriesPublicModels.size();i++){
            if (addCategoriesAdapter.categoriesPublicModels.get(i).getName().equals(categoriesPublicModel.getName())){
                addCategoriesAdapter.categoriesPublicModels.get(i).setSelected(categoriesPublicModel.getSelected());
            }
        }
        addCategoriesAdapter = new AddCategoriesAdapter(getApplicationContext(),addCategoriesAdapter.categoriesPublicModels,this);
        rv.setAdapter(addCategoriesAdapter);
    }

    @Override
    public void OnDeSelect(CategoriesPublicModel categoriesPublicModel) {
        for (int i=0;i<addCategoriesAdapter.categoriesPublicModels.size();i++){
            if (addCategoriesAdapter.categoriesPublicModels.get(i).getName().equals(categoriesPublicModel.getName())){
                addCategoriesAdapter.categoriesPublicModels.get(i).setSelected(categoriesPublicModel.getSelected());
            }
        }
        addCategoriesAdapter = new AddCategoriesAdapter(getApplicationContext(),addCategoriesAdapter.categoriesPublicModels,this);
        rv.setAdapter(addCategoriesAdapter);
    }

    @Override
    public void onBackPressed() {
        if (addCategoriesAdapter.getSelectedCategories().size()>0){
            confirDialog.show();
        }else {
            AddStoryGlobal.selectedCategoriesGlobal = new ArrayList<>();
            super.onBackPressed();
        }
    }
}