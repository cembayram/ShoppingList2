package com.example.shoppinglist2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoppinglist2.db.Category;
import com.example.shoppinglist2.viewmodel.MainActivtiyViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements CategoryListAdapter.HandleCategoryClick {

    private MainActivtiyViewModel viewModel;
    private TextView noResulttextView;
    private RecyclerView recyclerView;
    private CategoryListAdapter categoryListAdapter;
    private Category categoryForEdit;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Shopping List");
        noResulttextView = findViewById(R.id.noResult);
        recyclerView = findViewById(R.id.recyclerView);

       ImageView addNew = findViewById(R.id.addNewCategoryImageView);
       ImageView shareNew = findViewById(R.id.addSend);

       shareNew.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(MainActivity.this, MailSend.class);
               startActivity(intent);
           }
       });

       addNew.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               showAddCategoryDialog(false);}
       });
        initViewModel();
        initRecyclerView();
        viewModel.getAllCategoryList();

    }

    private void  initRecyclerView() {
        recyclerView.setLayoutManager( new LinearLayoutManager(this));
        categoryListAdapter = new CategoryListAdapter(this, this);
        recyclerView.setAdapter(categoryListAdapter);
    }

    private  void initViewModel() {
        viewModel = new ViewModelProvider(this).get(MainActivtiyViewModel.class);
        viewModel.getCategoryListObserver().observe(this, new Observer<List<Category>>() {
            @Override
            public void onChanged(List<Category> categories) {
                if (categories == null){
                    noResulttextView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }else{
                    //recycler view gösterilir
                    categoryListAdapter.setCategoryList(categories);
                    recyclerView.setVisibility(View.VISIBLE);
                    noResulttextView.setVisibility(View.GONE);
                }
            }
        });

    }

    private void showAddCategoryDialog(boolean isForEdit) {
        AlertDialog dialogBuilder = new AlertDialog.Builder(this).create();
        View dialogView = getLayoutInflater().inflate( R.layout.add_category_layout, null);
        EditText enterCategoryInput = dialogView.findViewById(R.id.enterCategoryInput);
        TextView createButton = dialogView.findViewById(R.id.createButton);
        TextView cancelButton = dialogView.findViewById(R.id.cancelButton);

        if(isForEdit){
            createButton.setText("Güncellendi");
            enterCategoryInput.setText(categoryForEdit.categoryName);
        }

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogBuilder.dismiss();
            }
        });
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String name = enterCategoryInput.getText().toString();
               if(TextUtils.isEmpty(name)){
                   Toast.makeText(MainActivity.this,"Alışveris Listesi eklendi",Toast.LENGTH_SHORT).show();
                   return;
               }
               if(isForEdit){
                   categoryForEdit.categoryName = name;
                   viewModel.updateCategory(categoryForEdit);
               }else{
                   //view modeli çağıramaya ihtiyacımız var.
                   viewModel.insertCategory(name);
               }
                dialogBuilder.dismiss();
            }
        });
        dialogBuilder.setView(dialogView);
        dialogBuilder.show();
    }

    @Override
    public void itemClick(Category category) {
        Intent intent = new Intent(MainActivity.this,ShowItemsListActivity.class);
        intent.putExtra("category id",category.uid);
        intent.putExtra("category name",category.categoryName);

        startActivity(intent);
    }

    @Override
    public void removeItem(Category category) {
        viewModel.deleteCategory(category);
    }

    @Override
    public void editItem(Category category) {
        this.categoryForEdit = category;
        showAddCategoryDialog(true);
    }

}