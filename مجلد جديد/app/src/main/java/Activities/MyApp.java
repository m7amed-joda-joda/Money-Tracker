package Activities;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.ViewModelProvider;

import com.example.myapplication111111111.R;

import DataBase.Category;
import DataBase.MyDatabase;
import DataBase.MyViewModel;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        boolean darkMode = prefs.getBoolean("dark_mode", false);

        if(darkMode){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }



        insertDefaultCategories();

    }


    private void insertDefaultCategories(){

        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        boolean isInserted = prefs.getBoolean("categories_inserted", false);

        if(isInserted) return;

        new Thread(() -> {

            MyDatabase db = MyDatabase.getDatabase(this);

            db.categoryDao().insertCategory(new Category("Food", R.drawable.food, "#FF9800"));
            db.categoryDao().insertCategory(new Category("Salary", R.drawable.salary, "#FF9800"));
            db.categoryDao().insertCategory(new Category("Education", R.drawable.education, "#FF9800"));
            db.categoryDao().insertCategory(new Category("Car", R.drawable.car, "#FF9800"));
            db.categoryDao().insertCategory(new Category("Donations", R.drawable.donations, "#FF9800"));
            db.categoryDao().insertCategory(new Category("Home", R.drawable.home, "#FF9800"));
            db.categoryDao().insertCategory(new Category("Travel", R.drawable.travel, "#FF9800"));
            db.categoryDao().insertCategory(new Category("Health", R.drawable.health, "#FF9800"));
            db.categoryDao().insertCategory(new Category("Shopping", R.drawable.shopping, "#FF9800"));
            db.categoryDao().insertCategory(new Category("Clothing", R.drawable.clothing, "#FF9800"));
            db.categoryDao().insertCategory(new Category("Gift", R.drawable.gift, "#FF9800"));
            db.categoryDao().insertCategory(new Category("Social", R.drawable.social, "#FF9800"));


        }).start();

        prefs.edit().putBoolean("categories_inserted", true).apply();
    }

}