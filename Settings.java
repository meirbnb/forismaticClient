package sample;

/* Singleton class Settings for sending parameters between forms */
public class Settings{
    private static Settings setting;
    private String lang = "English"; // default language is English
    private boolean dropBookmarks = false; // set false so that bookmarks will not be vanished

    // singleton method to return single instance of an object
    public static synchronized Settings getInstance(){
        if (setting == null)
            setting = new Settings();
        return setting;
    }

    // setter for language
    public void setLang(String lang) {
        this.lang = lang;
    }

    // setter for dropBookmarks
    public void setDropBookmarks(boolean dropBookmarks) {
        this.dropBookmarks = dropBookmarks;
    }

    // getter for language
    public String getLang() {
        return lang;
    }

    // getter for dropBookmarks
    public boolean isDropBookmarks() {
        return dropBookmarks;
    }
}