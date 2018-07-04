package checkversion.solar.com.checkversion.itemdata;

public class ItemDataApp {
    private  String nameApp;
    private  String nameVersionApp;
    private String namePackage;
    private String pathImage;
    private int codeVersion;
    private boolean fragVersion;

    public boolean isFragVersion() {
        return fragVersion;
    }

    public void setFragVersion(boolean fragVersion) {
        this.fragVersion = fragVersion;
    }

    public int getCodeVersion() {
        return codeVersion;
    }

    public void setCodeVersion(int codeVersion) {
        this.codeVersion = codeVersion;
    }

    public String getPathImage() {
        return pathImage;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }

    public String getNamePackage() {
        return namePackage;
    }

    public void setNamePackage(String namePackage) {
        this.namePackage = namePackage;
    }

    private String pathApp;

    public String getNameApp() {
        return nameApp;
    }

    public void setNameApp(String nameApp) {
        this.nameApp = nameApp;
    }

    public String getNameVersionApp() {
        return nameVersionApp;
    }

    public void setNameVersionApp(String nameVersionApp) {
        this.nameVersionApp = nameVersionApp;
    }

    public String getPathApp() {
        return pathApp;
    }

    public void setPathApp(String pathApp) {
        this.pathApp = pathApp;
    }
}
