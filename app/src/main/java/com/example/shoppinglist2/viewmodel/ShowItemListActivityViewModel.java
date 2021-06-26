package com.example.shoppinglist2.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.shoppinglist2.db.AppDatabase;
import com.example.shoppinglist2.db.Category;
import com.example.shoppinglist2.db.Items;

import java.util.List;

public class ShowItemListActivityViewModel extends AndroidViewModel {

    private MutableLiveData<List<Items>> listOfItems;
    private AppDatabase appDatabase;

    public ShowItemListActivityViewModel(Application application){
        super(application);
        listOfItems = new MutableLiveData<>();
        appDatabase = AppDatabase.getDBinstance(getApplication().getApplicationContext());

    }


    public MutableLiveData<List<Items>> getItemsListObserver() {
        return listOfItems;
    }

    public void  getAllItemList(int categoryID) {
     List<Items> itemsList = appDatabase.shoppingListDao().getAllItemsList(categoryID);
     if(itemsList.size() > 0){
         listOfItems.postValue(itemsList);
     }else{
         listOfItems.postValue(null);
     }
    }

    public void insertItems(Items item) {

        appDatabase.shoppingListDao().insertItems(item);
        getAllItemList(item.categoryId);

    }

    public void updateItems(Items item) {
        appDatabase.shoppingListDao().updateItems(item);
        getAllItemList(item.categoryId);
    }

    public void deleteItems(Items item) {
        appDatabase.shoppingListDao().deletetItems(item);
        getAllItemList(item.categoryId);
    }
}
