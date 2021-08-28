package com.delta_inductions.killer_app;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.Viewholder> {
private List<ApplicationInfo> applist;
private Context context;
private PackageManager manager;
    private Onclicklistner onitemclicklistner;

    public Adapter(Context context,List<ApplicationInfo> applist) {
        this.context = context;
        this.applist = applist;
        manager = context.getPackageManager();
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_layout, parent, false);
        return new Viewholder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        ApplicationInfo appinfo = applist.get(position);
            holder.packagename.setText(appinfo.packageName);
            holder.appname.setText(appinfo.loadLabel(manager));
            Drawable icon = null;
            try {
                icon = context.getPackageManager().getApplicationIcon(appinfo.packageName);
                holder.applogo.setImageDrawable(icon);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

    }
    @Override
    public int getItemCount() {
        return applist.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private ImageView applogo;
        private TextView appname;
        private TextView packagename;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            packagename = itemView.findViewById(R.id.packagename);
            applogo = itemView.findViewById(R.id.applogo);
            appname = itemView.findViewById(R.id.appname);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onitemclicklistner != null) {
                        int position = getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION)
                            onitemclicklistner.onclick(position,applist.get(position));
                    }
                }
            });
        }
    }

 public void setOnitemclicklistner(Onclicklistner onitemclicklistner)
 {
     this.onitemclicklistner = onitemclicklistner;

 }
 public interface Onclicklistner
 {
     void onclick(int position,ApplicationInfo pkginfo);
 }
}
