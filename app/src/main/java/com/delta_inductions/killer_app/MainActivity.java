package com.delta_inductions.killer_app;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Adapter.Onclicklistner {
    private static final String TAG = "MainActivity";
    private PackageManager manager;
    private RecyclerView recyclerView;
    private Adapter adapter;
    private List<ApplicationInfo> applist = new ArrayList<>();
    private List<ApplicationInfo> packages;
    private ActivityManager activityManager;
    private boolean found = false;
//    List<RunningTaskInfo> tasks = am.getRunningTasks(1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager = getPackageManager();
        activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        packages = manager.getInstalledApplications(PackageManager.GET_META_DATA);
        for (int i=0; i<packages.size();i++)
        {
            if(!((packages.get(i).flags & ApplicationInfo.FLAG_SYSTEM)==1))
            {
                applist.add(packages.get(i));
            }
        }
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this, applist);
        adapter.setOnitemclicklistner(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onclick(int position,ApplicationInfo pkginfo) {
        getRunningtask(pkginfo);
//        Toast.makeText(this, packagename, Toast.LENGTH_SHORT).show();
    }

    private void getRunningtask(ApplicationInfo pkginfo) {
//        List<ActivityManager.RunningAppProcessInfo> runningAppInfo = activityManager.getRunningAppProcesses();
//        for (int i = 0; i < runningAppInfo.size(); i++) {
//            if (runningAppInfo.get(i).processName.equals(pkginfo.packageName)) {
//                found = true;
//                break;
//
//            }
//        }
        if(((pkginfo.flags & ApplicationInfo.FLAG_STOPPED )!=0))
        {
            Toast.makeText(this, "App not Running in Background", Toast.LENGTH_SHORT).show();
            Intent intent = getPackageManager().getLaunchIntentForPackage(pkginfo.packageName);
            if (intent != null) {
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(),
                        "No launch intent for " + pkginfo.packageName, Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(this, "App is Running hence going to kill it", Toast.LENGTH_SHORT).show();
            activityManager.killBackgroundProcesses(pkginfo.processName);
            Toast.makeText(this, "App has been killed", Toast.LENGTH_SHORT).show();
        }

        }
//    public static boolean isForeground(Context ctx, String myPackage){
//        ActivityManager manager = (ActivityManager) ctx.getSystemService(ACTIVITY_SERVICE);
//        List< ActivityManager.RunningTaskInfo > runningTaskInfo = manager.getRunningTasks(1);
//
//        ComponentName componentInfo = runningTaskInfo.get(0).topActivity;
//        if(componentInfo.getPackageName().equals(myPackage)) {
//            return true;
//        }
//        return false;
//    }
@Override
public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.searchmenu,menu);
    MenuItem searchitem = menu.findItem(R.id.action_search);
    androidx.appcompat.widget.SearchView searchView =(androidx.appcompat.widget.SearchView) searchitem.getActionView();
    searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            return false;
        }
        @Override
        public boolean onQueryTextChange(String newText) {
            searchView.setQueryHint("Search by App Name....");
            adapter.getFilter().filter(newText);
            return false;
        }
    });
    return true;
}
    }
