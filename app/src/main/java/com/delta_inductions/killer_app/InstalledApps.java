package com.delta_inductions.killer_app;

public class InstalledApps {
        private String packagename;
        private String sourcedir;

    public InstalledApps(String packagename, String sourcedir) {
        this.packagename = packagename;
        this.sourcedir = sourcedir;
    }

    public String getPackagename() {
        return packagename;
    }

    public String getSourcedir() {
        return sourcedir;
    }
}
